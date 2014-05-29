package com.avidandrew.habittracker;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang3.time.FastDateFormat;

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
}