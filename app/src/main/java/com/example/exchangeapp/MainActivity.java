package com.example.exchangeapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private android.support.design.widget.TabLayout TabLayout;
    private android.support.v4.view.ViewPager ViewPager;
    private ArrayList<String> Ids=new ArrayList<String>();
    private ArrayList<String> Titles2=new ArrayList<String>();
    private AutoCompleteTextView searchView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference productRef = database.getReference("Product");
    public static final String SelectedProduct="product_Id";
    private Toolbar maintoolbar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
       getMenuInflater().inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.findfriendsOption_Id){

        }
        else if(item.getItemId()==R.id.addProductOption_Id){
            GoAddProductPage();

        }
        else if(item.getItemId()==R.id.profileOption_id){
            GoProfile();

        }
        else if(item.getItemId()==R.id.logoutOption_Id){
            LogOut();
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        TabLayout=(TabLayout)findViewById(R.id.tablayout_id);
        ViewPager=(ViewPager)findViewById(R.id.viewpager_id);
        searchView=(AutoCompleteTextView)findViewById(R.id.search_ID);
        ViewPageAdapter adapter=new ViewPageAdapter(getSupportFragmentManager());
        adapter.AddFragment(new FragmentClothes(),"Clothes");
        adapter.AddFragment(new FragmentWatchesandAccessories(),"Watches&Accessories");
        adapter.AddFragment(new FragmentMakeUp(),"MakeUp");
        adapter.AddFragment(new FragmentsOthers(),"Others");
        ViewPager.setAdapter(adapter);
        TabLayout.setupWithViewPager(ViewPager);
        maintoolbar=(Toolbar)findViewById(R.id.toolbar_Id);
        setSupportActionBar(maintoolbar);
        getSupportActionBar().setTitle("Exchangeable App");


        Search();




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
    private void LogOut(){
        if(mAuth.getCurrentUser()!=null){
            mAuth.signOut();
            GoLogin();

        }

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
    private void GoProfile(){
        Intent profile=new Intent(MainActivity.this,ProfileActivity.class);
        startActivity(profile);
        finish();
    }
}
