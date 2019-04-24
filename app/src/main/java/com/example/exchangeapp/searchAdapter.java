package com.example.exchangeapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class searchAdapter extends ArrayAdapter<Product> {
    final long ONE_MEGABYTE = 1024 * 1024;
    private StorageReference productStorageRef= FirebaseStorage.getInstance().getReference("ProductsImages/");
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference productRef = database.getReference("Product");

    public searchAdapter(Context context, ArrayList<Product> product_returned){

        super(context,0,product_returned);

    }



    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View searchList=convertView;

        searchList = LayoutInflater.from(getContext()).inflate(
                R.layout.search_list, parent, false);

        Product product= getItem(position);
        TextView productTitle=(TextView) searchList.findViewById(R.id.search_title_Id);
        final ImageView productImage=(ImageView) searchList.findViewById(R.id.searchImg);
        productTitle.setText(product.getProductTitle());
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

        return searchList;


    }

}
