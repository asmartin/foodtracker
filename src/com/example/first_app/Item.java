package com.example.first_app;

public class Item {
	public String item_name;
	public int breakfast_counter;
	public int lunch_counter;
	public int dinner_counter;
	public int total_counter;

	
	public Item(String id, int breakfast_counter, int lunch_counter, int dinner_counter, int total_counter) {
	this.item_name = id;
	this.breakfast_counter = breakfast_counter;
	this.lunch_counter = lunch_counter;
	this.dinner_counter = dinner_counter;
	this.total_counter = total_counter;		
	}	
	
	public Item(String id, int total_counter ){
		
		this.item_name = item_name;
		this.total_counter = total_counter;
	}
	
	@Override
	public String toString() {
		return item_name;
	}
}
