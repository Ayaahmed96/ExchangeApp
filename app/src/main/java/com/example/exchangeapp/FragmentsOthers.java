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

public class FragmentsOthers extends Fragment {
    View view;
    ArrayList<Product> otherProduct=new ArrayList<Product>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference productRef = database.getReference("Product");

    public FragmentsOthers() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.otherfragments,container,false);
        return view;
    }
    @Override
    public void onStart() {

        super.onStart();
        ReturnOtherProduct();
    }
    private void ReturnOtherProduct(){
        ListView otherList=(ListView) view.findViewById(R.id.otherList);
        final ProductAdapter otherProductAdapter=new ProductAdapter(getActivity(),otherProduct);
        otherList.setAdapter(otherProductAdapter);
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                otherProduct.clear();
                for(DataSnapshot productsnapshot:dataSnapshot.getChildren()){
                    Product product=productsnapshot.getValue(Product.class);
                    if(TextUtils.equals(product.getProductCateogry(),"Others")){
                        Product pr=new Product();
                        pr.setProductOwnerId(productsnapshot.getKey());
                        pr.setPreferedProducttoExchage(product.getPreferedProducttoExchage());
                        pr.setProductTitle(product.getProductTitle());
                        pr.setProductCateogry(product.getProductCateogry());
                        pr.setProductDescription(product.getProductDescription());
                        if(product.getProductPhoto()!=null){
                            pr.setProductPhoto(product.getProductPhoto());
                        }
                        otherProduct.add(pr);

                    }

                }
                 Collections.reverse(otherProduct);
                otherProductAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
