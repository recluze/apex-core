/* 
	Part of the Apex Framework. 
	
	Provided under GPLv2. 
	
	author: Nauman (recluze@gmail.com) 
			http://csrdu.org/nauman 

 */

package org.csrdu.apex;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

//import android.util.Log;
import org.csrdu.apex.helpers.Log;

public class AttributeManager {
	// TODO: Fix before incorporation in AOSP
	// private static String attributesDirectory = "/system/etc/apex/attribs/";
	private static String attributesDirectory = "policies/attribs/";
	private static AttributeManager _singletonInstance;
	private static String TAG = "APEX:AttributeManager";

	private Map<String, Map<String, Object>> applicationAttributes;

	private AttributeManager() {
		// hide the default constructor
	}

	private AttributeManager(String attribDirectory) {
		attributesDirectory = attribDirectory;
		initializeAttributesCache();
	}

	private void initializeAttributesCache() {
		Log.d(TAG, "Initializing application attributes cache...");
		if (this.applicationAttributes == null) {
			this.applicationAttributes = new HashMap<String, Map<String, Object>>();
			Log.d(TAG, "Created new cache object.");
		}
	}

	public Object lookupApplicationAttribute(String packageName,
			String attributeName) {
		Log.d(TAG, "Checking if application attributes are present in cache.");
		if (!applicationAttributes.containsKey(packageName)) {
			Log.d(TAG,
					"Package attributes not found in cache. Reading attributes from file.");
			readPackageAttributes(packageName);
		}
		// should now have the attributes. Return null if we don't have it.
		if (!applicationAttributes.containsKey(packageName)) {
			Log.d(TAG, "Failed to read package attributes. Returning null.");
			return null;
		} else {
			Map<String, Object> attribs = applicationAttributes
					.get(packageName);
			Object attribVal = attribs.get(attributeName);
			Log.d(TAG,
					"Found package attributes in cache. Returning value for attribute: "
							+ attribVal);
			return attribVal;
		}

	}

	private void readPackageAttributes(String packageName) {
		HashMap<String, Object> packageAttributes = new HashMap<String, Object>();

		BufferedReader input = null;
		try {
			Log.d(TAG, "Reading attribute file: " + attributesDirectory
					+ packageName);
			input = new BufferedReader(new FileReader(attributesDirectory
					+ packageName));
			String line = null;
			while ((line = input.readLine()) != null) {
				int pos = line.indexOf("=");
				String attribName = line.substring(0, pos);
				String attribVal = line.substring(pos + 1);
				Log.d(TAG, "Found new attribute: [" + attribName
						+ "] with value: [" + attribVal + "]");
				packageAttributes.put(attribName, attribVal);
			}
			Log.d(TAG, "Adding package attributes to cache for package: "
					+ packageName);
			applicationAttributes.put(packageName, packageAttributes);
		} catch (FileNotFoundException e) {
			Log.d(TAG, "Couldn't find package attribute file: "
					+ attributesDirectory + packageName);
			e.printStackTrace();
		} catch (IOException e) {
			Log.d(TAG, "I/O Exception while reading file: "
					+ attributesDirectory + packageName);
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static AttributeManager getSingletonInstance() {
		if (_singletonInstance == null) {
			Log.d(TAG, "Creating new singleton instance of AttributeManager.");
			_singletonInstance = new AttributeManager(attributesDirectory);
		}

		Log.d(TAG, "Returning singleton instance of AttributeManager.");
		return _singletonInstance;
	}
}
