package com.example.exchangeapp;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private android.support.design.widget.TabLayout chattablayout_id;
    private android.support.v4.view.ViewPager chatviewpager_id;
    private Toolbar maintoolbar;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference groupsRef = database.getReference("Groups");


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chatmenu,menu);
         super.onCreateOptionsMenu(menu);
         return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);
         if(item.getItemId()==R.id.findFriends_Id){

         }
       else if(item.getItemId()==R.id.makegroup_Id){
         showMakeGroupPopUp();
        }
         return true;
    }

    private void showMakeGroupPopUp() {
        AlertDialog.Builder builder=new AlertDialog.Builder(ChatActivity.this,R.style.AlertDialog);
        builder.setTitle("Enter the group name");
        final EditText groupname=new EditText(ChatActivity.this);
        groupname.setHint("e.g makeup team");
        builder.setView(groupname);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(groupname.getText().toString().isEmpty()){
                    groupname.setError("group name can not be empty");
                }
                else{
                    createGroup(groupname.getText().toString());
                }
            }
        });

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

  builder.show();
    }


    private void createGroup(String groupname){
        if(mAuth.getCurrentUser()!=null){
            groupsRef.child(groupname).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ChatActivity.this,"group created succesfully",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(ChatActivity.this,"Error "+task.getException(),Toast.LENGTH_LONG).show();

                    }
                }
            });
        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mAuth=FirebaseAuth.getInstance();

        chattablayout_id=(TabLayout)findViewById(R.id.chattablayout_id);
        chatviewpager_id=(ViewPager)findViewById(R.id.chatviewpager_id);
        ViewPageAdapter adapter=new ViewPageAdapter(getSupportFragmentManager());
        adapter.AddFragment(new ChatFragment(),"Chat");
        adapter.AddFragment(new ContactFragment(),"Contacts");
        adapter.AddFragment(new GroupsFragment(),"Groups");
        chatviewpager_id.setAdapter(adapter);
        chattablayout_id.setupWithViewPager(chatviewpager_id);
        maintoolbar=(Toolbar)findViewById(R.id.toolbar_Id);
        setSupportActionBar(maintoolbar);
        getSupportActionBar().setTitle("Exchangeable App");

    }
}
