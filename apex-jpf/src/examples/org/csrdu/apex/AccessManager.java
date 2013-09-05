/* 
Part of the Apex Framework.	 


Provided under GPLv2. 

author: Nauman (recluze@gmail.com) 
		http://csrdu.org/nauman 

 */


package org.csrdu.apex;

import java.util.HashMap;	

import org.csrdu.apex.policy.ApexPackagePolicy;

import org.csrdu.apex.policy.PolicyNotFoundException;
//import org.csrdu.apex.functions.*;

//import android.util.Log;
import org.csrdu.apex.helpers.Log;

public class AccessManager 
{
	Object lock = new Object(); 
	/**
	 * @uml.property  name="tAG"
	 */
	private String TAG = "APEX:AccessManager";
	
	// TODO: Fix before incorporation in AOSP
	// private String permDirectory = "/system/etc/apex/perms/";
	/**
	 * @uml.property  name="permDirectory"
	 */
	private String permDirectory = "policies/";

	/**
	 * @uml.property  name="packagePolicies"
	 * @uml.associationEnd  qualifier="packageName:java.lang.String org.csrdu.apex.policy.ApexPackagePolicy"
	 */
	private HashMap<String, ApexPackagePolicy> packagePolicies = new HashMap<String, ApexPackagePolicy>();
	/**
	 * @uml.property  name="attributeManager"
	 * @uml.associationEnd  
	 */
	private AttributeManager attributeManager;

	public AccessManager() 
	{
		Log.d(TAG, "Initializing AccessManager...");
	}

	public synchronized boolean checkExtendedPermissionByPackage(String permName, String packageName) 
	{
		// TODO: keep for AlarmClock only for now. Should get rid of it soon.
		// if (!packageName.equals("com.android.alarmclock"))
		// return true;

		// get the attribute manager
		// synchronized (lock) { 
			attributeManager = AttributeManager.getSingletonInstance();
		//} 
		long startTime = System.currentTimeMillis();

		Log.i(TAG, "Checking permission: " + permName + " for " + packageName);
		Log.d(TAG, "Checking if package policy is present in cache.");
		ApexPackagePolicy app = null;   
		//ApexThreads apTh = null;
		Log.d(TAG,"ACCESS MANAGER..."+(packagePolicies.containsKey(packageName)));
		if ((packagePolicies.containsKey(packageName)))
		{
			Log.d(TAG, "Loaded package policy from cache. Skipping file parsing.");
			app = packagePolicies.get(packageName);
		}
		else
		{
			Log.d(TAG, "Failed to find package policy in cache. Parsing file...");
			app = new ApexPackagePolicy(packageName);
			try {
				app.loadPoliciesForPackage(packageName, permDirectory);
			} catch (PolicyNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*apTh = new ApexThreads(packageName);
			Log.d(TAG,"Package Name"+packageName+"....DIrectory..."+ permDirectory);
			apTh.test(2,packageName, permDirectory);*/
			Log.d(TAG, "Policy: " + app.toString());
			packagePolicies.put(packageName, app);
			Log.d(TAG, "Saved policy for package in cache: " + packageName);
		}

		long endTime = System.currentTimeMillis();
		Log.d(TAG, "Loaded policy in " + (endTime - startTime) + " ms");

		startTime = System.currentTimeMillis();

		Log.d(TAG, "Checking if there are any policies associated with permission: " + permName);
		boolean evaluationResult;
		//int evaluationResultInt; //my edition
		if (!app.hasPoliciesForPermission(permName))// s k sath kuch karna hi... let me check....12/17/12
		{
			Log.d(TAG, "No policies for permission: " + permName + ". Granting access.");
			return true;
		}
		try
		{
			Log.d(TAG, "Evaluating policy for: " + packageName);
			Log.d(TAG, "APEX SAEED AM..... "+attributeManager+"........sssss........"+permName);
			evaluationResult = app.evaluatePolicies(attributeManager, permName);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Log.d(TAG, "Unexpected error while evaluating policies for: " + packageName);
			Log.d(TAG, "Grudgingly allowing access...");
			return true;
		}
		endTime = System.currentTimeMillis();
		Log.d(TAG, "Evaluated policy in " + (endTime - startTime) + " ms");
		// return true if all checks pass
		return evaluationResult;
		
	}

	/**
	 * Remove package policy from the cahce. Might be useful when the user
	 * updates policy from the package manager. After invalidation, the policy
	 * will be parsed automatically when it's next needed.
	 * 
	 * @param packageName
	 *            The package for which the policy should be removed from the
	 *            cache.
	 */
	public void invalidatePackagePolicyInCache(String packageName) {
		Log.d(TAG, "Invalidating package policy for package: " + packageName);
		this.packagePolicies.remove(packageName);
	}
}
