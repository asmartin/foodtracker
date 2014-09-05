package com.avidandrew.habittracker;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang3.time.FastDateFormat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;

public class Util {
	
	private static boolean checkSDCardWritable() {
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
		    return true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    // We can only read the media
		    return false;
		} else {
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
		    return false;
		}
	}
	public static boolean writeToSD(String data) {
		if (!checkSDCardWritable()) {
			// can't write to the SD Card, fail immediately
			return false;
		}
		
		// get external storage file reference
		try {
			FileWriter writer = new FileWriter(Environment.getExternalStorageDirectory()); 
			// Writes the content to the file
			writer.write(data); 
			writer.flush();
			writer.close();
		} catch (IOException ioe) {
			return false;
		}
		
		return true;
	}
	
	/** 
	 * Sanitize the version number to only include numbers and periods
	 * @param version the original version number to sanitize (e.g 1.0.0pre)
	 * @return the sanitized version number (e.g 1.0.0)
	 */
	public static String sanitizeVersion(String version) {
		return version.replaceAll("[^\\d.]", "");
	}
	
	public static String getDatestamp() {
		Date myDate = new Date();
		FastDateFormat fdf = FastDateFormat.getInstance("yyyy-MM-dd");
		return fdf.format(myDate);
	}
	
	public static void okDialog(Context context, String title, String msg) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(msg);
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, context.getResources().getString(R.string.MSG_OK), new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
		// here you can add functions
		}
		});
		//alertDialog.setIcon(R.drawable.icon);
		alertDialog.show();
	}
	
	public static void okDialogReload(final Context context, String title, String msg) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(msg);
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, context.getResources().getString(R.string.MSG_OK), new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			Intent refresh = new Intent(context, MainActivity.class);
			context.startActivity(refresh);
		}
		});
		//alertDialog.setIcon(R.drawable.icon);
		alertDialog.show();
	}
	
	public static boolean unpackZip(String path, String zipname)
	{       
	     InputStream is;
	     ZipInputStream zis;
	     try 
	     {
	         String filename;
	         is = new FileInputStream(zipname);
	         zis = new ZipInputStream(new BufferedInputStream(is));          
	         ZipEntry ze;
	         byte[] buffer = new byte[1024];
	         int count;

	         while ((ze = zis.getNextEntry()) != null) 
	         {
	             filename = ze.getName();

	             // Need to create directories if not exists, or
	             // it will generate an Exception...
	             if (ze.isDirectory()) {
	                File fmd = new File(path + "/" + filename);
	                fmd.mkdirs();
	                continue;
	             }

	             FileOutputStream fout = new FileOutputStream(path + "/" +  filename);

	             while ((count = zis.read(buffer)) != -1) 
	             {
	                 fout.write(buffer, 0, count);             
	             }

	             fout.close();               
	             zis.closeEntry();
	         }

	         zis.close();
	     } 
	     catch(IOException e)
	     {
	         e.printStackTrace();
	         return false;
	     }

	    return true;
	}
}