package org.csrdu.apex.helpers;

import org.csrdu.apex.AccessManager;

public class Runner {

	public static void main(String args[]){ 
		AccessManager am = new AccessManager(); 
		boolean res = am.checkExtendedPermissionByPackage(
						// "android.permission.ACCESS_FINE_LOCATION", 
						"android.permission.WRITE_SETTINGS", 
						"com.android.clock");
		
		System.out.println(res);
		System.out.println("==========================================");
		res = am.checkExtendedPermissionByPackage(
				// "android.permission.ACCESS_FINE_LOCATION", 
				"android.permission.BLAH", 
				"com.android.clock");

		System.out.println(res);
	}
}
