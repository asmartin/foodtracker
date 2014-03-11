package com.example.first_app;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.example.first_app.R;;

public class Items_Data {

	public Items_Data(Context c) {
		addItem(new Item(c, "Fats, Oils, Sweets",1));
		addItem(new Item(c, "Dairy",3));
		addItem(new Item(c, "Meats, Eggs, Nuts", 3));
		addItem(new Item(c, "Vegetables", 5));
		addItem(new Item(c, "Fruits", 5));
		addItem(new Item(c, "Breads, Carbs", 11));


	}

	/**
	 * Array of Food Pyramid Items
	 */

	private List<Item> Items = new ArrayList<Item>();
	public List<Item> getItems(){ return Items;};

private void addItem(Item item){Items.add(item);}
	


}
