package com.avidandrew.habittracker;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.example.first_app.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Chart_Activity extends Activity {


	private GraphicalView mChart;


	private String[] mMonth = new String[] {
			"Jan", "Feb" , "Mar", "Apr", "May", "Jun", "Jul", "Aug" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chart_activity);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		OpenChart();
	}

	private void OpenChart() {
		// Define the number of elements you want in the chart.
		int z[]={0,1,2,3,4,5,6,7}; //Months to add
		int x[]={10,18,32,21,48,20,53,80};


		// Create XY Series for X Series.
		XYSeries xSeries=new XYSeries("X Series");


		//  Adding data to the X Series.
		for(int i=0;i<z.length;i++)  { xSeries.add(z[i],x[i]); }

		// Create a Dataset to hold the XSeries.
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

		// Add X series to the Dataset.   
		dataset.addSeries(xSeries);


		// Create XYSeriesRenderer to customize XSeries
		XYSeriesRenderer Xrenderer=new XYSeriesRenderer();
		Xrenderer.setColor(Color.BLUE);
		Xrenderer.setPointStyle(PointStyle.POINT);
		Xrenderer.setDisplayChartValues(true);
		Xrenderer.setLineWidth(1);
		Xrenderer.setFillPoints(true);
		Xrenderer.setShowLegendItem(false);
		Xrenderer.setAnnotationsColor(Color.RED);
		Xrenderer.setDisplayChartValuesDistance(20);

		// Create XYMultipleSeriesRenderer to customize the whole chart
		XYMultipleSeriesRenderer mRenderer=new XYMultipleSeriesRenderer();
		mRenderer.setChartTitle("X Vs Y Chart");
		mRenderer.setXTitle("X Values");
		mRenderer.setYTitle("Y Values");
		mRenderer.setZoomButtonsVisible(true);
		mRenderer.setXLabels(0);
		mRenderer.setLabelsTextSize(20);
		mRenderer.setAxisTitleTextSize(15);
		mRenderer.setAxesColor(Color.GREEN);
		mRenderer.setBackgroundColor(Color.BLACK);
		mRenderer.setPanEnabled(true);

		mRenderer.setShowGrid(true);
		mRenderer.setClickEnabled(true);

		SimpleSeriesRenderer sRenderer = new SimpleSeriesRenderer();
		sRenderer.setDisplayBoundingPoints(true);
		
		for(int i=0;i<z.length;i++) { mRenderer.addXTextLabel(i, mMonth[i]);}

		// Adding the XSeriesRenderer to the MultipleRenderer. 
		mRenderer.addSeriesRenderer(Xrenderer);

		LinearLayout chart_container=(LinearLayout)findViewById(R.id.Chart_layout);

		// Creating an intent to plot line chart using dataset and multipleRenderer
		mChart=(GraphicalView)ChartFactory.getLineChartView(getBaseContext(), dataset, mRenderer);

		//  Adding click event to the Line Chart.
		mChart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				SeriesSelection series_selection=mChart.getCurrentSeriesAndPoint();

				if(series_selection!=null)
				{
					int series_index=series_selection.getSeriesIndex();

					String select_series="X Series";
					if(series_index==0) {select_series="X Series"; }
					else { select_series="Y Series";}

					String month=mMonth[(int)series_selection.getXValue()];

					int amount=(int)series_selection.getValue();

					Toast.makeText(getBaseContext(), select_series+"in" + month+":"+amount, Toast.LENGTH_SHORT).show();
				}
			}
		});

		// Add the graphical view mChart object into the Linear layout .
		chart_container.addView(mChart);


	}
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

}


