package com.avidandrew.foodtracker;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
 

 
public class FoodList extends ListFragment {
 
    private String[] meat = new String[] {
        "Beef",
        "Pork",
        "Ham",
        "Bacon",
        "Chicken",
        "Turkey"
    };
    
    private String[] dairy = new String[] {
            "Milk",
            "Yogurt",
            "Cheese",
            "Butter",
            "Ice Cream",
            "Cream"
    };
    
    private String[] other = new String[] {
            "Vegetables",
            "Fruit",
            "Beans",
            "Tofu",
            "Seiten",
            "Soy Product",
            "Almond Product",
            "Coconut Product"
    };
    
    private String[] active = other;
    
    public void setType(int type) {
    	switch (type) {
	    	case 0: active = meat; break;
	    	case 1: active = dairy; break;
	    	case 2: active = other; break;
	    	default: active = other; break;
    	}
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        /** Creating an array adapter to store the list of countries **/
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), R.layout.fragment_foodlist,active);
 
        /** Setting the list adapter for the ListFragment */
        setListAdapter(adapter);
 
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}