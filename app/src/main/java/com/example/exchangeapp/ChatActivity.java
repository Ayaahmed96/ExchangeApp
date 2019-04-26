package com.example.exchangeapp;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TableLayout;

public class ChatActivity extends AppCompatActivity {

    private android.support.design.widget.TabLayout chattablayout_id;
    private android.support.v4.view.ViewPager chatviewpager_id;
    private Toolbar maintoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

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
