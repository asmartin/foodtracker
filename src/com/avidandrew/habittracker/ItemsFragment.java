package com.avidandrew.habittracker;


import static com.avidandrew.habittracker.Constants.EXTRA_MAX;
import static com.avidandrew.habittracker.Constants.EXTRA_NAME;
import static com.avidandrew.habittracker.Constants.EXTRA_PERIOD;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class ItemsFragment extends ListFragment implements Constants{

	private int period;

	private Items_ListViewAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		//GET TAB INDEX FROM ARGUMENTS
		Bundle bundle = getArguments();
		period = bundle.getInt(EXTRA_FRAGMENT_ID);

		//Inflate ListView of Items
		View view = inflater.inflate(R.layout.fragment_listview_items, container, false);

		//GET LIST OF ITEMS
		ArrayList<Item> list = getArrayofItems(period);

		//Load Sample Data if ArrayList is empty
		if(getArrayofItems(period).size() == 0 || list == null){
			//	list = loadSampleData();
		}

		adapter = new Items_ListViewAdapter(getActivity(), R.layout.listview_item, list);
		setListAdapter(adapter);

		return view;
	}

	/**
	 * GET THE ARRAY OF ITEMS DEPENDING ON THE PERIOD
	 * @return
	 */
	private ArrayList<Item> getArrayofItems(int period){
		DBHelper db = new DBHelper(this.getActivity());
		ArrayList<Item> items = db.getItemsByPeriod(period);
		return items;
	}

	/**
	 * Return Sample data if no items displayed
	 */
	private ArrayList<Item> loadSampleData() {
		ArrayList<Item> sampleData = new ArrayList<Item>();
		for (String[] sample : SAMPLE_DATA) {
			if (sample.length > 1) {
				int max = Integer.parseInt(sample[1]);
				int period = Integer.parseInt(sample[2]);
				if(period == this.period){
					sampleData.add(new Item(this.getActivity(), sample[0], period, max));
				}
			}
		}
		return sampleData;

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);


		ArrayList<Item> mItems = getArrayofItems(period);
		/// MINUS ON CLICK LISTENER

		switch(v.getId()){
		case R.id.button_subtract:
			mItems.get(position).decrement();
			adapter.notifyDataSetChanged();

			break;
		case R.id.button_add:
			mItems.get(position).increment();
			adapter.notifyDataSetChanged();

			break;
		case R.id.button_item_name:
			Intent edit_item = new Intent(getActivity(), Edit_Item.class);
			// make the name and max variables available in the new activity
			edit_item.putExtra(EXTRA_NAME, mItems.get(position).getName());
			edit_item.putExtra(EXTRA_MAX, mItems.get(position).getMaxInPeriod());
			edit_item.putExtra(EXTRA_PERIOD, mItems.get(position).getPeriod());

			// launch the new activity
			getActivity().startActivity(edit_item);

			break;
		default:
			break;
		}

	}

	/**
	 * CLASS EXTENDING THE LISTVIEW ADAPTER TO LOAD THE ITEMS
	 * @author emmanuel
	 *
	 */
	public class Items_ListViewAdapter extends ArrayAdapter<Item>{

		public Items_ListViewAdapter(Context context, 
				int textViewResourceId, ArrayList<Item> objects) {
			super(context,  textViewResourceId, objects);

			//Array passed when creating adapter
			this.mItems_Array = objects;
		}

		private ArrayList<Item> mItems_Array;
		Item mItem;


		/**
		 * View Holder class to fix all listviews rows pointing to same item
		 * I think this happens because converView is recycled when creating 
		 * the listview
		 * @author emmanuel
		 *
		 */
		public class ViewHolder{
			Button mSubtract, mItemName, mAdd;
			TextView mLabelGoal;
			Item item;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view = convertView;

			//Fields
			//Button mSubtract, mItemName, mAdd;
			//TextView mLabelGoal;

			final ViewHolder viewHolder;

			//Inflate view if empty
			if(view == null){
				LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.listview_item, null);

				viewHolder = new ViewHolder();
				//LINK VIEWS 
				//LINK VIEWS
				viewHolder.mSubtract = (Button) view.findViewById(R.id.button_subtract);
				viewHolder.mItemName = (Button) view.findViewById(R.id.button_item_name);
				viewHolder.mAdd = (Button) view.findViewById(R.id.button_add);
				viewHolder.mLabelGoal = (TextView) view.findViewById(R.id.textView_label_goal);
				
				//Store item in viewHolder
				viewHolder.item = mItems_Array.get(position);
				
				view.setTag(viewHolder);
			}

			else { 
				//view = convertView;
				viewHolder = (ViewHolder) view.getTag();
			}
			////////////////////////////---- SETUP LISTVIEW ROW ----////////////////////////////////
			//Get the item from the Array

			//Populate Buttons

			viewHolder.mSubtract.setText("-");
			viewHolder.mAdd.setText("+");
			viewHolder.mItemName.setText(viewHolder.item.getName());
			String goal = viewHolder.item.getCounterValue() + " / " + viewHolder.item.getMaxInPeriod();
			viewHolder.mLabelGoal.setText(goal);	

			////SUBTRACT ON CLICK LISTENER
			viewHolder.mSubtract.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					viewHolder.item.decrement();
					adapter.notifyDataSetChanged();
				}
			});
			////ITEM ON CLICK LISTENER
			viewHolder.mItemName.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent edit_item = new Intent(getContext(), Edit_Item.class);

					// make the name and max variables available in the new activity
					edit_item.putExtra(EXTRA_NAME, viewHolder.item.getName());
					edit_item.putExtra(EXTRA_MAX, viewHolder.item.getMaxInPeriod());
					edit_item.putExtra(EXTRA_PERIOD, viewHolder.item.getPeriod());

					// launch the new activity
					getContext().startActivity(edit_item);
				}
			});
			////ADD ON CLICK LISTENER
			viewHolder.mAdd.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					viewHolder.item.increment();
					adapter.notifyDataSetChanged();
				}
			});

			///////////////////////////////////////////////////////////////////////////

			return view;

		}
	}
}