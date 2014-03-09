package com.example.first_app;

import java.util.ArrayList;
import java.util.List;

import com.example.first_app.R;;

public class Items_Data {

	public Items_Data() {
		addItem(new Item("Fats, Oils, Sweets",1));
		addItem(new Item("Dairy",3));
		addItem(new Item("Meats, Eggs, Nuts", 3));
		addItem(new Item("Vegetables", 5));
		addItem(new Item("Fruits", 5));
		addItem(new Item("Breads, Carbs", 11));


	}

	/**
	 * Array of Food Pyramid Items
	 */

	private List<Item> Items = new ArrayList<Item>();
	public List<Item> getItems(){ return Items;};

private void addItem(Item item){Items.add(item);}
	


}
