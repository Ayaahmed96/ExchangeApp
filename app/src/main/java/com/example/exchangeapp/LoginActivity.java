package com.example.exchangeapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText login_email;
    private EditText login_password;
    private Button login_btn;
    private FirebaseAuth mAuth;
    private TextView login_register;
    private String Email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        login_email=(EditText) findViewById(R.id.login_email);
        login_password=(EditText) findViewById(R.id.login_password);
        login_btn=(Button)findViewById(R.id.login_btn);
        login_register=(TextView)findViewById(R.id.login_register);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    final ProgressDialog progressdialogue=new ProgressDialog(LoginActivity.this);
                    progressdialogue.setTitle("loading...");
                    progressdialogue.show();
                    mAuth.signInWithEmailAndPassword(Email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressdialogue.dismiss();
                                Toast.makeText(LoginActivity.this,"Login Successfully",Toast.LENGTH_LONG).show();
                                GoHome();
                            }
                            else{
                                progressdialogue.dismiss();
                                Toast.makeText(LoginActivity.this,"Error " +task.getException(),Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                }

            }
        });
        login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoSignupPage();
            }
        });




    }


    private boolean validate(){
        boolean valid=true;
        Email=login_email.getText().toString().trim();
        password=login_password.getText().toString().trim();

        if(Email.isEmpty()){
            login_email.setError("Email is mandatory, it can not be empty");
            valid=false;
        }
        if(Email.contains(" ")){
            Email = Email.replaceAll("\\s","");
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            login_email.setError("Email format not valid , please enter valid email");
            valid=false;
        }
        if(password.isEmpty()){
            login_password.setError("Password can not be empty");
            valid=false;
        }

        return valid;


    }

    private void GoHome(){
        Intent login_main=new Intent(LoginActivity.this,MainActivity.class);
        login_main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(login_main);
        finish();
    }

    private void GoSignupPage(){
        Intent login_signup=new Intent(LoginActivity.this,SignupActivity.class);
        startActivity(login_signup);
        finish();
    }



}