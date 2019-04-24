package com.example.exchangeapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.zip.Inflater;

public class FragmentClothes extends Fragment {
    View view;
    ArrayList<Product> clothesProduct=new ArrayList<Product>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference productRef = database.getReference("Product");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.clothesfragments,container,false);
        return view;
    }

    public FragmentClothes() {
    }

    @Override
    public void onStart() {

        super.onStart();
        RetrunClothesProduct();
    }
    private void RetrunClothesProduct(){
        ListView clotheslist=(ListView) view.findViewById(R.id.clothesList);
        final ProductAdapter clothesproductAdapter=new ProductAdapter(getActivity(),clothesProduct);
        clotheslist.setAdapter(clothesproductAdapter);
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clothesProduct.clear();
                for(DataSnapshot productsnapshot:dataSnapshot.getChildren()){
                    Product product=productsnapshot.getValue(Product.class);
                    if(TextUtils.equals(product.getProductCateogry(),"Clothes")){
                        Product pr=new Product();
                        pr.setProductOwnerId(productsnapshot.getKey());
                        pr.setPreferedProducttoExchage(product.getPreferedProducttoExchage());
                        pr.setProductTitle(product.getProductTitle());
                        pr.setProductCateogry(product.getProductCateogry());
                        pr.setProductDescription(product.getProductDescription());
                     if(product.getProductPhoto()!=null){
                         pr.setProductPhoto(product.getProductPhoto());
                        }
                        clothesProduct.add(pr);

                    }

                }
                Collections.reverse(clothesProduct);
                clothesproductAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
