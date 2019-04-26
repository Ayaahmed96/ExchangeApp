package com.example.exchangeapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
    private Dialog checkpassdialogue;
    private TextView txtclose;
    private EditText passcheck;
    private ProgressDialog progressdialogue;
    private Button checkpassSubmit;
    private Dialog updateprofileDialogue;
    private EditText updatedname;
    private EditText updatedEmail;
    private Button submitUpdateProfile;
    private Spinner spinner1;
    private String Addresse,updatename,updateemail;
    String[] cities = new String[]{"Cairo", "Alexandria", "Giza","Port Said","Suez","Luxor","al-Mansura","El-Mahalla El-Kubra","Tanta","Asyut",
            "tIsmailia","Fayyum","Zagazig"," Aswan","Damietta","Damanhur","al-Minya","Beni Suef"," Qena","Sohag","Hurghada","6th of October City","Shibin El Kom",
            "Banha"," Kafr el-Sheikh","Arish","Mallawi","10th of Ramadan City","Bilbais","Marsa Matruh","Idfu","Mit Ghamr","Al-Hamidiyya","Desouk",
            "Qalyub","Abu Kabir","Kafr el-Dawwar","Girga","Akhmim","Matareya"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getAttributes();
        GetProfileDate();
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissProgressDialog();
                checkpassdialogue.dismiss();
            }
        });
        checkpassSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckPassword();
            }
        });
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String city=(String)parent.getItemAtPosition(position);
                Addresse="Egypt/"+city;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Addresse="Egypt";
            }
        });
        submitUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMynfo();
            }
        });

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
        checkpassdialogue = new Dialog(this);
        checkpassdialogue.setContentView(R.layout.checkpassword_popup);
        txtclose=(TextView) checkpassdialogue.findViewById(R.id.txtclose);
        passcheck=(EditText) checkpassdialogue.findViewById(R.id.passcheck);
        checkpassSubmit=(Button)checkpassdialogue.findViewById(R.id.checkpassSubmit);
        updateprofileDialogue= new Dialog(this);
        updateprofileDialogue.setContentView(R.layout.updateprofilepopup);
        spinner1=(Spinner)updateprofileDialogue.findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cities);
        spinner1.setAdapter(adapter);
        submitUpdateProfile=(Button) updateprofileDialogue.findViewById(R.id.updateprofilesubmit_id);
        updatedname=(EditText)updateprofileDialogue.findViewById(R.id.updatedname);
        updatedEmail=(EditText)updateprofileDialogue.findViewById(R.id.updatedemail);



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
            checkpassdialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            checkpassdialogue.show();

        }
        else if(item.getItemId()==R.id.updatestatus_option){


        }
        else if(item.getItemId()==R.id.changepassword_option){

        }

        return true;
    }


    private void CheckPassword(){
        if(mAuth.getCurrentUser()!=null) {
            final String pass = passcheck.getText().toString().trim();
            if (pass.isEmpty()) {
                passcheck.setError("password can not be empty");
            } else {
                showDialogue();
                UserRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(TextUtils.equals(dataSnapshot.getValue(Users.class).getUserPassword(),pass)){

                            dismissProgressDialog();
                            checkpassdialogue.dismiss();
                            passcheck.setText("");
                            updateprofileDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            updateprofileDialogue.show();


                        }
                        else{
                            dismissProgressDialog();
                            passcheck.setError("wrong password");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        }
        else{
            Toast.makeText(ProfileActivity.this, "you should login first", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean validate(){
        boolean valid=true;
     updatename=updatedname.getText().toString().trim();
   updateemail=updatedEmail.getText().toString().trim();
  if(updatename.isEmpty()){
      updatedname.setError("name can not be Empty");
      valid=false;
  }
  if(updateemail.isEmpty()){
      updatedEmail.setError("Email can not be Empty");
      valid=false;
  }
   if(updatename.length()>25){
      updatedname.setError("name can not be that long");
       valid=false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(updateemail).matches()){
            updatedEmail.setError("Email format is wrong");
            valid=false;
        }
       if(updateemail.contains(" ")){
           updateemail = updateemail.replaceAll("\\s","");
           valid=false;
        }

          return valid;
    }
    private void updateMynfo(){
        if(validate()) {
            showDialogue();
            if (mAuth.getCurrentUser() != null) {
                String currentuserId = mAuth.getCurrentUser().getUid();
                mAuth.signOut();
                UserRef.child(currentuserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final Users user = dataSnapshot.getValue(Users.class);

                        mAuth.signInWithEmailAndPassword(user.getUserEmail(), user.getUserPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    mAuth.getCurrentUser().updateEmail(updateemail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                user.setUserEmail(updateemail);
                                                user.setUsername(updatename);
                                                user.setLocation(Addresse);
                                                UserRef.child(mAuth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            dismissProgressDialog();
                                                            updateprofileDialogue.dismiss();
                                                            Toast.makeText(ProfileActivity.this, "  your info update successfully", Toast.LENGTH_LONG).show();


                                                        } else {
                                                            dismissProgressDialog();
                                                            Toast.makeText(ProfileActivity.this, "Error !! " + task.getException(), Toast.LENGTH_LONG).show();
                                                            updateprofileDialogue.dismiss();

                                                        }
                                                    }
                                                });

                                            } else {
                                                dismissProgressDialog();
                                                Toast.makeText(ProfileActivity.this, "Error " + task.getException(), Toast.LENGTH_LONG).show();

                                                System.out.println("Error " + task.getException());
                                            }
                                        }
                                    });

                                } else {
                                    dismissProgressDialog();
                                    Toast.makeText(ProfileActivity.this, "not sign in !! " + task.getException(), Toast.LENGTH_LONG).show();

                                }
                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }
    private void showDialogue(){
        if(progressdialogue==null){
            progressdialogue=new ProgressDialog(this);
            progressdialogue.setTitle("loading...");
            progressdialogue.setMessage("please wait...");

        }
        progressdialogue.show();
    }
    private void dismissProgressDialog(){
        if (progressdialogue != null && progressdialogue.isShowing()) {
            progressdialogue.dismiss();
        }
    }
    @Override
    protected void onDestroy(){
        dismissProgressDialog();
        super.onDestroy();
    }

}
