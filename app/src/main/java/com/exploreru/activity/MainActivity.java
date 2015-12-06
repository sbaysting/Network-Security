package com.exploreru.activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.exploreru.R;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get the ViewPager which will hold all of the fragment activities
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        //The FragmentParser will take in and keep track of all of the fragments and tab titles
        //Enter the number of fragments being entered. Used for threading synchronization
        final FragmentParser frag = new FragmentParser(1);

        List<Thread> startupThreads = new ArrayList<Thread>();
        //ADD FRAGMENT PAGES (ACTIVITIES) HERE, start a new thread each time for start up performance:
        startupThreads.add(new Thread(new Runnable() {
            @Override
            public void run() {
                frag.addFragment(DiningFragment.newInstance(context), "Dining",1);
            }
        }));
        //Start the fragment threads and wait for them to load
        try {
            for(int i = 0;i < startupThreads.size();i++){
                startupThreads.get(i).start();
            }
            while(!startupThreads.isEmpty()){
                for(int i = 0;i < startupThreads.size();i++){
                    if(!startupThreads.get(i).isAlive()){
                        startupThreads.remove(i);
                        i = 0;
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        //Create the FragmentPagerAdapter from the FragmentParser to generate tabs and pass it to the ViewPager to link the tabs
        viewPager.setAdapter(new MainFragmentPagerAdapter(getSupportFragmentManager(), MainActivity.this, frag));

    }

}
