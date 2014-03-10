package com.example.first_app;

public class Item {
	public String item_name;
	public int total_counter;
	public int max_servings;
	public int day_counter;
	

	
	public Item(String id, int max_servings){
		
		this.item_name = id;
		this.max_servings = max_servings;
		
	}
	
public String getId(){
		
		return item_name;
	}

public int getMaxServings(){
	return max_servings;
}
	
	
	
	public int getMaxQuantity() {
		return max_servings;
	}
	
	public int getMaxQuantity() {
		return max_servings;
	}
	
	@Override
	public String toString() {
		return item_name;
	}
}
