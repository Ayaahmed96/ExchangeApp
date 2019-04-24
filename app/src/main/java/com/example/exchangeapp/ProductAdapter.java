package com.example.exchangeapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;

public class ProductAdapter  extends ArrayAdapter<Product> {
    final long ONE_MEGABYTE = 1024 * 1024;
    private StorageReference productStorageRef= FirebaseStorage.getInstance().getReference("ProductsImages/");
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference productRef = database.getReference("Product");

    public ProductAdapter(Context context, ArrayList<Product> product_returned){

        super(context,0,product_returned);

    }



    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View productList=convertView;

        productList = LayoutInflater.from(getContext()).inflate(
                R.layout.productlist, parent, false);

          Product product= getItem(position);
          TextView productTitle=(TextView) productList.findViewById(R.id.productTitle_id);
          TextView ProductDescription=(TextView) productList.findViewById(R.id.productDescription_id);
          TextView productCateogry=(TextView) productList.findViewById(R.id.productCateogry_id);
        TextView preferedproductCateogry=(TextView) productList.findViewById(R.id.preferedProduct_id);
         final  ImageView productImage=(ImageView) productList.findViewById(R.id.productimg);
        productTitle.setText(product.getProductTitle());
        ProductDescription.setText(product.getProductDescription());
        productCateogry.setText(product.getProductCateogry());
        preferedproductCateogry.setText(product.getPreferedProducttoExchage());
        if(product.getProductPhoto()==null){
            productImage.setImageResource(R.drawable.defaultproduct);
        }
        else{
            productRef.child(product.getProductOwnerId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
         productStorageRef.child(dataSnapshot.getValue(Product.class).getProductPhoto()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            productImage.setImageBitmap(bm);

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        return productList;


    }

}
