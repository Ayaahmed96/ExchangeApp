package com.example.exchangeapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button logout;
    private android.support.design.widget.TabLayout TabLayout;
    private android.support.v4.view.ViewPager ViewPager;
    private Button Addproduct;
    private ArrayList<String> Ids=new ArrayList<String>();
    private ArrayList<String> Titles2=new ArrayList<String>();
    private AutoCompleteTextView searchView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference productRef = database.getReference("Product");
    public static final String SelectedProduct="product_Id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        logout=(Button) findViewById(R.id.logout);
        TabLayout=(TabLayout)findViewById(R.id.tablayout_id);
        ViewPager=(ViewPager)findViewById(R.id.viewpager_id);
        Addproduct=(Button) findViewById(R.id.Addproduct);
        searchView=(AutoCompleteTextView)findViewById(R.id.search_ID);
        ViewPageAdapter adapter=new ViewPageAdapter(getSupportFragmentManager());
        adapter.AddFragment(new FragmentClothes(),"Clothes");
        adapter.AddFragment(new FragmentWatchesandAccessories(),"Watches&Accessories");
        adapter.AddFragment(new FragmentMakeUp(),"MakeUp");
        adapter.AddFragment(new FragmentsOthers(),"Others");
        ViewPager.setAdapter(adapter);
        TabLayout.setupWithViewPager(ViewPager);

        Search();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser()!=null){
                    mAuth.signOut();
                    GoLogin();

                }

            }
        });
        Addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoAddProductPage();

            }
        });


       ArrayAdapter arrayadapter=new ArrayAdapter(MainActivity.this,android.R.layout.simple_spinner_dropdown_item,Titles2);
        searchView.setAdapter(arrayadapter);
        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                int selectedPos = Titles2.indexOf((((TextView)view).getText()).toString());
                String selecteditem =Ids.get(selectedPos);
                System.out.println("position is "+ selecteditem);

               Intent ProductIntent=new Intent(getApplicationContext(),ProductActivity.class);
               ProductIntent.putExtra(SelectedProduct,selecteditem);
               startActivity(ProductIntent);
            }
        });


    }

    @Override
    protected void onStart() {
        if(mAuth.getCurrentUser()==null){
            GoLogin();
        }

        super.onStart();
    }
    private void GoLogin(){
        Intent loginIntent=new Intent(MainActivity.this,LoginActivity.class);
        startActivity(loginIntent);
    }
    private void GoAddProductPage(){
        Intent addproduct=new Intent(MainActivity.this,AddProductActivity.class);
        startActivity(addproduct);
    }


    private void Search(){
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Titles2.clear();
                Ids.clear();
                for(DataSnapshot productsnapshot:dataSnapshot.getChildren()){
                    Product product=productsnapshot.getValue(Product.class);
                   Titles2.add(product.getProductTitle());
                    Ids.add(productsnapshot.getKey());


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
