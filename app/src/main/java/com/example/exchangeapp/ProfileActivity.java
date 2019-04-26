package com.example.exchangeapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference UserRef = database.getReference("Users");
    private TextView name;
    private TextView email;
    private TextView location;
    private TextView gender;
    private TextView statue;
    private CircleImageView profile_image;
    private StorageReference mStorageRef;
    final long ONE_MEGABYTE = 1024 * 1024;
    private Toolbar maintoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getAttributes();
        GetProfileDate();

    }

    private void getAttributes(){
        mAuth = FirebaseAuth.getInstance();
        maintoolbar=(Toolbar)findViewById(R.id.toolbar_Id);
        setSupportActionBar(maintoolbar);
        getSupportActionBar().setTitle("Exchangeable App");
        mStorageRef = FirebaseStorage.getInstance().getReference("UserImages/");
        name=(TextView) findViewById(R.id.profile_name);
        email=(TextView)findViewById(R.id.profile_email);
        location=(TextView)findViewById(R.id.profile_location);
        gender=(TextView)findViewById(R.id.profile_gender);
        statue=(TextView)findViewById(R.id.statue);
        profile_image=(CircleImageView)findViewById(R.id.profile_image);

    }
    private void GetProfileDate(){
        if(mAuth.getCurrentUser()!=null) {
            UserRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Users user=dataSnapshot.getValue(Users.class);
                    name.setText(user.getUsername());
                    email.setText(user.getUserEmail());
                    location.setText(user.getLocation());
                    if(user.getProfile_pic_id()!=null){
                        mStorageRef.child(user.getProfile_pic_id()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                profile_image.setImageBitmap(bm);

                            }
                        });
                    }
                    if(user.getStatue()!=null){
                        statue.setText(user.getStatue());
                    }
                    if(user.isGender()==true){
                        gender.setText("Male");
                    }
                    else{
                        gender.setText("Female");
                    }




                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.updateprofile_option){

        }
        else if(item.getItemId()==R.id.updatestatus_option){


        }
        else if(item.getItemId()==R.id.changepassword_option){

        }

        return true;
    }
}
