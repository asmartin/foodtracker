package com.example.first_app;

import java.util.ArrayList;
import java.util.List;

import com.example.first_app.R;;

public class Items_Data {

	public Items_Data() {
		addItem(new Item("Vegetables", 2 ,2 ,2,6));
		addItem(new Item("Dairy", 1 ,1 ,1,3));
		addItem(new Item("Fats, Oils, Sweets",1,1,1,5));
		addItem(new Item("Breads, Carbs", 2, 2, 2, 7));
		addItem(new Item("Meats", 1, 1, 1, 4));
	}

	/**
	 * Array of Food Pyramid Items
	 */

	private List<Item> Items = new ArrayList<Item>();
	public List<Item> getItems(){ return Items;};

private void addItem(Item item){Items.add(item);}
	


}
