package com.example.exchangeapp;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPageAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragmentlist=new ArrayList<>();
    private final List<String> fragmentListtitle=new ArrayList<>();

    public ViewPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentListtitle.get(position);
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentlist.get(i);
    }

    @Override
    public int getCount() {
        return fragmentListtitle.size();
    }
    public  void AddFragment(Fragment fragment,String title){
        fragmentlist.add(fragment);
        fragmentListtitle.add(title);
    }
}
