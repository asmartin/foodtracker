package com.avidandrew.foodtracker;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.io.FileOutputStream;
import java.util.Locale;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Environment;
import android.util.Log;

import java.sql.Timestamp;
import java.util.Date;
import java.io.File;

public class MainActivity extends FragmentActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    private String storage = Environment.getExternalStorageDirectory() + "/foodtracker.csv";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private String getTimestamp() {
    	java.util.Date date= new java.util.Date();
   	    String full = new Timestamp(date.getTime()).toString();
   	    return full.substring(0, full.length()-4);
    }
    
    public void lBeef(View v) {
    	dataLogger("Beef");
    }
    
    public void lPork(View v) {
    	dataLogger("Pork");
    }
    
    public void lHam(View v) {
    	dataLogger("Ham");
    }
    
    public void lBacon(View v) {
    	dataLogger("Bacon");
    }
    
    public void lChicken(View v) {
    	dataLogger("Chicken");
    }
    
    public void lTurkey(View v) {
    	dataLogger("Turkey");
    }
    
    public void lMilk(View v) {
    	dataLogger("Milk");
    }
    
    public void lYogurt(View v) {
    	dataLogger("Yogurt");
    }
    
    public void lCheese(View v) {
    	dataLogger("Cheese");
    }
    
    public void lButter(View v) {
    	dataLogger("Butter");
    }
    
    public void lIceCream(View v) {
    	dataLogger("Ice Cream");
    }
    
    public void lCream(View v) {
    	dataLogger("Cream");
    }
    
    public void lEggs(View v) {
    	dataLogger("Eggs");
    }
    
    public void lVegetables(View v) {
    	dataLogger("Vegetables");
    }
    
    public void lFruit(View v) {
    	dataLogger("Fruit");
    }
    
    public void lBeans(View v) {
    	dataLogger("Beans");
    }
    
    public void lTofu(View v) {
    	dataLogger("Tofu/Seitan");
    }
    
    public void lSoy(View v) {
    	dataLogger("Soy Product");
    }
    
    public void lAlmond(View v) {
    	dataLogger("Almond Product");
    }
    
    public void lCoconut(View v) {
    	dataLogger("Coconut Product");
    }
    
    public void aUndo(MenuItem item) {
    	try {
	    	RandomAccessFile f = new RandomAccessFile(storage, "rw");
	    	long length = f.length() - 1;
	    	byte b;
	    	do {                     
	    	  length -= 1;
	    	  f.seek(length);
	    	  b = f.readByte();
	    	} while(b != 10);
	    	f.setLength(length+1);
    	} catch (IOException ioe) {
    		
    	}
    }
    
    public void aView(MenuItem item) {
    	Intent intent = new Intent();
    	intent.setAction(android.content.Intent.ACTION_VIEW);
    	File file = new File(storage);
    	intent.setDataAndType(Uri.fromFile(file), "text/plain");
    	startActivity(intent); 
    }
    
    public void aTruncate(MenuItem item) {
    	try {
    		FileOutputStream stream = new FileOutputStream(storage, true);
	    	FileChannel outChan = stream.getChannel();
	        outChan.truncate(0);
	        outChan.close();
	        stream.close();
    	} catch (FileNotFoundException fnfe) {
    		
    	} catch (IOException ioe) {
    		
    	}
    }
    
    public void aEmail(MenuItem item) {
    	Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND); 
    	emailIntent.setType("text/csv");
    	emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Food Tracker History"); 
    	emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "History Log from the FoodTracker Android App"); 
    	emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + storage));
    	startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }
    
    public void dataLogger(String s) {
    	try {
    	// get external storage file reference
    	FileWriter writer = new FileWriter(storage, true); 
    	// Writes the content to the file
    	writer.write(getTimestamp() + "," + s + "\n"); 
    	writer.flush();
    	writer.close();
    	} catch (IOException ioe) {
    		
    	}
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            Fragment fragment = new MeatSectionFragment();
            Bundle args = new Bundle();
            //args.putInt(MeatSectionFragment.ARG_SECTION_NUMBER, position);
            switch (position) {
            case 0: fragment = new MeatSectionFragment(); break;
            case 1: fragment = new DairySectionFragment(); break;
            case 2: fragment = new OtherSectionFragment(); break;
            }
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply
     * displays dummy text.
     */
    public static class MeatSectionFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";

        public MeatSectionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_meat, container, false);
            
            //TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
            //dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply
     * displays dummy text.
     */
    public static class DairySectionFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";

        public DairySectionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_dairy, container, false);
            
            //TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
            //dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply
     * displays dummy text.
     */
    public static class OtherSectionFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";

        public OtherSectionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_other, container, false);
            
            //TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
            //dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
    
    
}
