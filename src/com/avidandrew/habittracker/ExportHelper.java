package com.avidandrew.habittracker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.xmlpull.v1.XmlSerializer;

import com.avidandrew.habittracker.R;

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
		String items_filename = outputDir.getPath() + File.separator + ITEMS_FILENAME;
		String timestamps_filename = outputDir.getPath() + File.separator + TIMESTAMPS_FILENAME;
		String manifest_filename = outputDir.getPath() + File.separator + MANIFEST_FILENAME;
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
		            serializer.startTag("", MANIFEST_TAG_ROOT);
		            serializer.text("\n");
		            serializer.startTag("", MANIFEST_TAG_VERSION);
		            serializer.text(Util.sanitizeVersion(VERSION));
		            serializer.endTag("", MANIFEST_TAG_VERSION);
		            serializer.text("\n");
		            serializer.startTag("", MANIFEST_TAG_SQL_ITEMS);
		            serializer.text(TABLE_CREATE_ITEMS);
		            serializer.endTag("", MANIFEST_TAG_SQL_ITEMS);
		            serializer.text("\n");
		            serializer.startTag("", MANIFEST_TAG_SQL_TIMESTAMPS);
		            serializer.text(TABLE_CREATE_TIMESTAMPS);
		            serializer.endTag("", MANIFEST_TAG_SQL_TIMESTAMPS);
		            serializer.text("\n");
		            serializer.endTag("", MANIFEST_TAG_ROOT);
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
