package com.avidandrew.habittracker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.xmlpull.v1.XmlSerializer;

import com.example.first_app.R;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import au.com.bytecode.opencsv.CSVWriter;
import static com.avidandrew.habittracker.Constants.*;

public class ExportHelper {
	private DBHelper db = null;
	private Context context = null;
	
	public ExportHelper(Context c) {
		db = new DBHelper(c);
		context = c;
	}
	
	private void addToZipFile(String fileName, ZipOutputStream zos) throws FileNotFoundException, IOException {
		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file);
		ZipEntry zipEntry = new ZipEntry(file.getName());
		zos.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}
	
	public boolean exportTo_v0_9_0() {
		File outputDir = context.getCacheDir(); // context being the Activity pointer
		String items_filename = outputDir.getPath() + File.separator + "items.csv";
		String timestamps_filename = outputDir.getPath() + File.separator + "timestamps.csv";
		String manifest_filename = outputDir.getPath() + File.separator + "manifest.xml";
		String export_filename = Environment.getExternalStorageDirectory() + File.separator + APP_NAME.replace(" ", "_") + "_" + Util.getDatestamp() + "." + APP_EXTENSION;
		File exportFile = new File(export_filename);

		// get Items table
		ArrayList<ArrayList<String>> items = db.getResultsAsStrings(SQL_GET_ALL_ROWS);

		// get Timestamps table
		ArrayList<ArrayList<String>> timestamps = db.getResultsAsStrings(SQL_GET_ALL_TIMESTAMPS_ROWS);

		// create archive:
		// - items.csv - the contents of the items table
                // - timestamps.csv - the contents of the timestamps table
                // - manifest.xml - contains the app version
		
		try {
			// write the manifest file
			XmlSerializer serializer = Xml.newSerializer();
			FileWriter writer = new FileWriter(manifest_filename);
		    try {
		        serializer.setOutput(writer);
		        serializer.startDocument("UTF-8", true);
		        serializer.text("\n");
		            serializer.startTag("", "habittracker");
		            serializer.text("\n");
		            serializer.startTag("", "version");
		            serializer.text(Util.sanitizeVersion(VERSION));
		            serializer.endTag("", "version");
		            serializer.text("\n");
		            serializer.startTag("", "SQL_Items");
		            serializer.text(TABLE_CREATE_ITEMS);
		            serializer.endTag("", "SQL_Items");
		            serializer.text("\n");
		            serializer.startTag("", "SQL_Timestamps");
		            serializer.text(TABLE_CREATE_TIMESTAMPS);
		            serializer.endTag("", "SQL_Timestamps");
		            serializer.text("\n");
		            serializer.endTag("", "habittracker");
		        serializer.endDocument();
		        writer.close();
		    } catch (Exception e) {
		    	Log.v("asm", "RuntimeException");
		        throw new RuntimeException(e);
		    } 
			
			// write the Items CSV file
		    CSVWriter itemswriter = new CSVWriter(new FileWriter(items_filename), ',');
		    // write the header
		    itemswriter.writeNext(ITEMS_COLUMNS_ORDER.split(SEP));
		    
		    // write the data
		    for (ArrayList<String> item : items) {
		    	String[] thisLine = new String[item.size()];
		    	thisLine = item.toArray(thisLine);
		    	itemswriter.writeNext(thisLine);
		    }
		    itemswriter.close();

			// write the Timestamps CSV file
		    CSVWriter timestampswriter = new CSVWriter(new FileWriter(timestamps_filename), ',');
		    // write the header
		    timestampswriter.writeNext(TIMESTAMPS_COLUMNS_ORDER.split(SEP));
		    
		    // write the data
		    for (ArrayList<String> timestamp : timestamps) {
		    	String[] thisLine = new String[timestamp.size()];
		    	thisLine = timestamp.toArray(thisLine);
		    	timestampswriter.writeNext(thisLine);
		    }
		    timestampswriter.close();
		} catch(IOException exc) {
			Log.v("asm", "IOE");
			return false;
		}

		try {
			// open the zip archive for writing
			FileOutputStream fos = new FileOutputStream(exportFile);
			ZipOutputStream zos = new ZipOutputStream(fos);

			// write the files into the archive
			addToZipFile(manifest_filename, zos);
			File f = new File(manifest_filename);
			f.delete();
			addToZipFile(items_filename, zos);
			f = new File(items_filename);
			f.delete();
			addToZipFile(timestamps_filename, zos);
			f = new File(timestamps_filename);
			f.delete();

			Log.v("asm", exportFile.getAbsolutePath());
			
			// close the archive
			zos.close();
			fos.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
			Log.v("asm", "IOE while creating zip");
			return false;
		}

		// send the file somewhere
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(exportFile));
		sendIntent.setType("application/octet-stream");
		context.startActivity(Intent.createChooser(sendIntent, context.getResources().getText(R.string.send_to)));
		
		// TODO: remove 3 files and zip archive
		return true;
	}
	
	public boolean export() {
		return exportTo_v0_9_0();
	}
}
