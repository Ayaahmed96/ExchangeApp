package com.example.exchangeapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class AddProductActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button Addproduct;
    private Button Back;
    private String title,description,preferedproduct,productcateogry;
    private ImageView productImage;
    private EditText product_title;
    private EditText product_description;
    private Spinner spinner1;
    private Spinner spinner2;
    private Integer PICK_IMAGE_REQUEST=71;
    private Uri filepath;
    private String imagekey;
    private StorageReference mStorageRef;
    private ProgressDialog progressdialogue;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference productRef = database.getReference("Product");
    String[] cateogries = new String[]{"Clothes", "MakeUp", "Watches&Accessories","Others"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        mAuth=FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        Addproduct=(Button) findViewById(R.id.Addproduct);
        Back=(Button) findViewById(R.id.back);
        productImage=(ImageView)findViewById(R.id.productimg);
        product_title=(EditText)findViewById(R.id.productTitle_id);
        product_description=(EditText)findViewById(R.id.productDesc_id);
        spinner1=(Spinner)findViewById(R.id.spinner1);
        spinner2=(Spinner)findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cateogries);
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category=(String)parent.getItemAtPosition(position);
                productcateogry=category;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category=(String)parent.getItemAtPosition(position);
                preferedproduct=category;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BacktoHome();
            }
        });
        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"select image"),PICK_IMAGE_REQUEST);
            }
        });

        Addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() != null) {
                    if (validate()) {
                        showDialogue();
                        upload_product_photo();
                        Product product = new Product(title, description, imagekey, preferedproduct, productcateogry, mAuth.getCurrentUser().getUid());
                        productRef.push().setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    dismissProgressDialog();
                                    Toast.makeText(AddProductActivity.this,"Your product added Successfully",Toast.LENGTH_LONG).show();
                                    BacktoHome();
                                }else{
                                    dismissProgressDialog();
                                    Toast.makeText(AddProductActivity.this,"Error "+task.getException(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });



                    }
                }else{
                    Toast.makeText(AddProductActivity.this,"you are not connected o the internet ",Toast.LENGTH_LONG).show();

                }
            }
        });
    }


    private void BacktoHome(){
        Intent homeintent=new Intent(AddProductActivity.this,MainActivity.class);
        startActivity(homeintent);
        finish();
    }
    private boolean validate(){
        boolean valid=true;
        title=product_title.getText().toString().trim();
        description=product_description.getText().toString().trim();
        // Addresse=signup_addresse.getText().toString().trim();
        if(title.isEmpty()){
            product_title.setError("Username can not be empty");
            valid=false;
        }
        if(title.length()>25){
            product_title.setError("Username can not be that long");
            valid=false;
        }
        if(description.isEmpty()){
            product_description.setError("Email is mandatory, it can not be empty");
            valid=false;
        }
        if(productImage.getDrawable()==null){
            valid=false;
            Toast.makeText(AddProductActivity.this,"Image is mandatory",Toast.LENGTH_LONG).show();
        }

        return valid;
    }


    private void showDialogue(){
        if(progressdialogue==null){
            progressdialogue=new ProgressDialog(this);
            progressdialogue.setTitle("loading...");
            progressdialogue.setMessage("please wait...");

        }
        progressdialogue.show();
    }
    private void dismissProgressDialog() {
        if (progressdialogue != null && progressdialogue.isShowing()) {
            progressdialogue.dismiss();
        }
    }
    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null){
            filepath=data.getData();

            try{
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                productImage.setImageBitmap(bitmap);

            }
            catch (IOException e){
                e.printStackTrace();

            }
        }

    }

    private void upload_product_photo(){

        if(filepath!=null){
            imagekey = UUID.randomUUID().toString();
            StorageReference ref = mStorageRef.child("ProductsImages/"+imagekey);
            ref.putFile(filepath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        dismissProgressDialog();
                    }
                    else{
                        dismissProgressDialog();
                        Toast.makeText(AddProductActivity.this,"Photo uploading error !!"+task.getException(),Toast.LENGTH_LONG).show();

                    }
                }
            });

        }
    }

}
