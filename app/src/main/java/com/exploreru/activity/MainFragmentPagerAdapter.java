package com.exploreru.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    private int PAGE_COUNT;
    private List<String> tabTitles;
    private List<Fragment> tabViews;
    private Context context;
    private TabLayout tabs;

    public MainFragmentPagerAdapter(FragmentManager fm, Context context, FragmentParser frag) {
        super(fm);
        this.context = context;
        this.tabTitles = frag.getFragmentTitleArray();
        this.tabViews = frag.getFragmentArray();
        this.PAGE_COUNT = frag.getFragmentCount();
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        // Generate view based on item position in tabViews array
        return tabViews.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position in tabTitles array
        return tabTitles.get(position);
    }

}