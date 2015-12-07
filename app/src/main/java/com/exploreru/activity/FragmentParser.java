package com.exploreru.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SAM on 6/19/2015.
 */
public class FragmentParser {

    List<Fragment> fragarray;
    List<String> fragtitles;

    public FragmentParser(int size){
        fragarray = new ArrayList<Fragment>();
        fragtitles = new ArrayList<String>();
        for(int i = 0;i < size;i++){
            fragarray.add(null);
            fragtitles.add(null);
        }
    }

    public List<Fragment> getFragmentArray(){
        return fragarray;
    }

    public void addFragment(Fragment f, String title,int position){
        fragarray.set(position-1,f);
        fragtitles.set(position-1,title);
    }

    public int getFragmentCount(){
        return fragarray.size();
    }

    public List<String> getFragmentTitleArray(){
        return fragtitles;
    }

    public Fragment getFragment(int index){
        return (Fragment)fragarray.get(index);
    }

    public String getFragmentTitle(int index){
        return (String)fragtitles.get(index);
    }

}
