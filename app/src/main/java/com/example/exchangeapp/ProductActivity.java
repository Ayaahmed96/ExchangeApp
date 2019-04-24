package com.example.exchangeapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProductActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference productRef = database.getReference("Product");
    private TextView productTitle_id;
    private TextView productDescription_id;
    private TextView productCateogry_id;
    private TextView preferedProduct_id;
    private ImageView productimg;
    private StorageReference productStorageRef= FirebaseStorage.getInstance().getReference("ProductsImages/");
    final long ONE_MEGABYTE = 1024 * 1024;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productlist);

        productTitle_id=(TextView) findViewById(R.id.productTitle_id);
        productDescription_id=(TextView) findViewById(R.id.productDescription_id);
        productCateogry_id=(TextView)findViewById(R.id.productCateogry_id);
        preferedProduct_id=(TextView)findViewById(R.id.preferedProduct_id);
        productimg=(ImageView)findViewById(R.id.productimg);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ViewProduct();
    }
    private void ViewProduct(){
        Intent ProductIntent = getIntent();
        final String product_Id = ProductIntent.getStringExtra(MainActivity.SelectedProduct);
        productRef.child(product_Id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Product product=dataSnapshot.getValue(Product.class);
                productTitle_id.setText(product.getProductTitle());
                productDescription_id.setText(product.getProductDescription());
                productCateogry_id.setText(product.getProductCateogry());
                preferedProduct_id.setText(product.getPreferedProducttoExchage());
                if(product.getProductPhoto()!=null){
                    productStorageRef.child(product.getProductPhoto()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            productimg.setImageBitmap(bm);

                        }
                    });
                }


                else{
                    productimg.setImageResource(R.drawable.defaultproduct);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
