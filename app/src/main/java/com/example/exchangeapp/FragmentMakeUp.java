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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class FragmentMakeUp extends Fragment {
View view;
    ArrayList<Product> MakeupProduct=new ArrayList<Product>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference productRef = database.getReference("Product");
    public FragmentMakeUp() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.makeupfragments,container,false);
        return view;
    }
    @Override
    public void onStart() {

        super.onStart();
        ReturnMakeUpProduct();
    }
    private void ReturnMakeUpProduct(){
        ListView clotheslist=(ListView) view.findViewById(R.id.makeupList);
        final ProductAdapter MakeUpProductAdapter=new ProductAdapter(getActivity(),MakeupProduct);
        clotheslist.setAdapter(MakeUpProductAdapter);
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MakeupProduct.clear();
                for(DataSnapshot productsnapshot:dataSnapshot.getChildren()){
                    Product product=productsnapshot.getValue(Product.class);
                    if(TextUtils.equals(product.getProductCateogry(),"MakeUp")){
                        Product pr=new Product();
                        pr.setProductOwnerId(productsnapshot.getKey());
                        pr.setPreferedProducttoExchage(product.getPreferedProducttoExchage());
                        pr.setProductTitle(product.getProductTitle());
                        pr.setProductCateogry(product.getProductCateogry());
                        pr.setProductDescription(product.getProductDescription());
                        if(product.getProductPhoto()!=null){
                            pr.setProductPhoto(product.getProductPhoto());
                        }
                        MakeupProduct.add(pr);
                    }

                }
                Collections.reverse(MakeupProduct);
                MakeUpProductAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
