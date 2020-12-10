package com.example.calendarw.home;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> lstFragment=new ArrayList<>();
    private final List<String> lstTitles=new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return lstFragment.get(position);
    }

    @Override
    public int getCount() {
        return lstFragment.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return lstTitles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void AddFragment(Fragment fragment, String string){
        lstFragment.add(fragment);
        lstTitles.add(string);

    }

    public void RemoveFragments(){
        lstFragment.removeAll(lstFragment);
        lstTitles.removeAll(lstTitles);
    }

}
