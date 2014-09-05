package com.avidandrew.habittracker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Environment;
import au.com.bytecode.opencsv.CSVReader;
import static com.avidandrew.habittracker.Constants.*;

class ImportInBackground extends AsyncTask<Void, Void, Void> implements DialogInterface.OnCancelListener {
	private ProgressDialog dialog;
	private Context c;
	private ImportHelper ih;
	private File selected;
	private boolean result = false;
	
	public ImportInBackground(Context c, ImportHelper ih, File selected) {
		this.c = c;
		this.ih = ih;
		this.selected = selected;
	}
	
	protected void onPreExecute() {
		dialog = ProgressDialog.show(c, "", c.getResources().getString(R.string.MSG_IMPORTING_WAIT), true);
	}
	
	protected Void doInBackground(Void... unused) { 
		result = ih.importer(selected);
		return null; 
	}
	
	protected void onPostExecute(Void unused) { 
		dialog.dismiss(); 
		if (result) {
			Util.okDialogReload(c, c.getResources().getString(R.string.MSG_IMPORT_SUCCESSFUL_TITLE), c.getResources().getString(R.string.MSG_IMPORT_SUCCESSFUL_TEXT));
		}
	}
	
	public void onCancel(DialogInterface dialog) { 
		dialog.dismiss(); 
	}
}

public class ImportHelper {
	private DBHelper db = null;
	private Context c = null;
	private FileDialog fileDialog = null;
	
	public ImportHelper(Context c) {
		db = new DBHelper(c);
		this.c = c;
		
		File mPath = Environment.getExternalStorageDirectory();
        fileDialog = new FileDialog(c, mPath);
        fileDialog.setFileEndsWith(APP_EXTENSION);
        fileDialog.addFileListener(new FileDialog.FileSelectedListener() {
            public void fileSelected(File file) {
            	confirm(file);
                //Log.d(getClass().getName(), "selected file " + file.toString());
            }
        });
        //fileDialog.addDirectoryListener(new FileDialog.DirectorySelectedListener() {
        //  public void directorySelected(File directory) {
        //      Log.d(getClass().getName(), "selected dir " + directory.toString());
        //  }
        //});
        //fileDialog.setSelectDirectoryOption(false);
        
	}


	
	public void selectFile() {
		fileDialog.showDialog();	// selected file stored in "selected" variable		
	}
	
	public void confirm(final File selected) {
		if (selected == null || !selected.exists()) {
			Util.okDialog(c, c.getResources().getString(R.string.MSG_FILE_NOT_FOUND_TITLE), c.getResources().getString(R.string.MSG_FILE_NOT_FOUND_TEXT));
			return;
		}
		
		final ImportHelper ih = this;
	
		AlertDialog alertDialog = new AlertDialog.Builder(c).create();
		Resources res = c.getResources();
		alertDialog.setTitle(c.getResources().getString(R.string.MSG_IMPORT_CONFIRM_TITLE));
		alertDialog.setMessage(String.format(res.getString(R.string.MSG_IMPORT_CONFIRM_TEXT), res.getString(R.string.app_name)));
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, c.getResources().getString(R.string.MSG_YES), new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			new ImportInBackground(c, ih, selected).execute();
		}
		});
		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, c.getResources().getString(R.string.MSG_NO), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			// here you can add functions
			}
		});
		//alertDialog.setIcon(R.drawable.icon);
		alertDialog.show();
	}
	
	/** 
	 * import data from an export created on version 0.9.9
	 * @param tablesInfo 2-d array, each element is a 3-element child array: { "Table Name", "SQL for table", "path to csv file for table" }
	 * @return
	 */
	private boolean importFrom_v0_9_0(ArrayList<String[]> tablesInfo) {
		int errors = 0;
		CSVReader reader;
		
		// empty the database before starting
		db.emptyDB();
		
		for (String[] info : tablesInfo) {
			String table = info[0];
			String sql = info[1];
			String file = info[2];
			
			// create the table
			db.execSQL(sql);
			
			// import the CSV data
			try {
				reader = new CSVReader(new FileReader(file));
				List<String[]> myEntries = reader.readAll();
				String[] header = myEntries.get(0);
				for (int row = 1; row < myEntries.size(); row++) {
					if (!db.insertFromCSV(table, header, myEntries.get(row))) {
						// this row had an error inserting
						errors++;
					}
				}
				reader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				errors++;
			} catch (IOException ioe) {
				ioe.printStackTrace();
				errors++;
			}
		}

		if (errors > 0) {
			return false;
		}
		
		return true;
	}
	
	public boolean importer(File f) {
		// extract zip
		File temp = new File(c.getExternalCacheDir() + "httmp");
		if(temp.exists())
	    {
			temp.delete();
	    }
		temp.mkdir();
		try {
			if (!Util.unpackZip(temp.getCanonicalPath(), f.getCanonicalPath())) {
				Util.okDialog(c, c.getResources().getString(R.string.MSG_IMPORT_FAILED_TITLE), c.getResources().getString(R.string.MSG_IMPORT_FAILED_EXTRACT_TEXT));
				temp.delete();
				return false;
			}
	
			File xml = new File(temp.getCanonicalPath() + "/" + MANIFEST_FILENAME);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xml);
		 
			doc.getDocumentElement().normalize();
		 
			//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		 
			// get version
			NodeList versionNodeList = doc.getElementsByTagName(MANIFEST_TAG_VERSION);
			NodeList sqlItemsNodeList = doc.getElementsByTagName(MANIFEST_TAG_SQL_ITEMS);
			NodeList sqlTimestampsNodeList = doc.getElementsByTagName(MANIFEST_TAG_SQL_TIMESTAMPS);
			if (versionNodeList.getLength() < 1 || sqlItemsNodeList.getLength() < 1 || sqlTimestampsNodeList.getLength() < 1) {
				Util.okDialog(c, c.getResources().getString(R.string.MSG_IMPORT_FAILED_TITLE), c.getResources().getString(R.string.MSG_IMPORT_FAILED_PARSE_TEXT));
				temp.delete();
				return false;
			}
			String version = versionNodeList.item(0).getTextContent();
			String sqlItems = sqlItemsNodeList.item(0).getTextContent();
			String sqlTimestamps = sqlTimestampsNodeList.item(0).getTextContent();
			
			if (version.equals("0.9.0")) {
				ArrayList<String[]> tables = new ArrayList<String[]>();
				String[] itemsData = { TABLE_ITEMS, sqlItems, temp.getCanonicalPath() + "/" + ITEMS_FILENAME };
				tables.add(itemsData);
				String[] timestampsData = { TABLE_TIMESTAMPS, sqlTimestamps, temp.getCanonicalPath() + "/" + TIMESTAMPS_FILENAME };
				tables.add(timestampsData);
				if (!importFrom_v0_9_0(tables)) {
					throw new Exception();
				}
			}
		 
		    } catch (Exception e) {
		    	e.printStackTrace();
		    	Util.okDialog(c, c.getResources().getString(R.string.MSG_IMPORT_FAILED_TITLE), c.getResources().getString(R.string.MSG_IMPORT_FAILED_READ_TEXT));
				temp.delete();
				return false;
		    }
		
		temp.delete();
		return true;
	}
}