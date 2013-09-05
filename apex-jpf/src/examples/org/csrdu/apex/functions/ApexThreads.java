package org.csrdu.apex.functions;

import org.csrdu.apex.helpers.Log;
import org.csrdu.apex.policy.ApexPackagePolicy;
import org.csrdu.apex.policy.PolicyNotFoundException;

public class ApexThreads {
	private static String TAG = "APEX:ApexThreads";
	public static int count = 5;
	static ApexPackagePolicy app = null;
	static String str;
	static String str2;
	/**
	 * @uml.property  name="packageName"
	 */
	public String packageName;

	public ApexThreads() {
		// TODO Auto-generated constructor stub
	}
	
	public ApexThreads(String packageName) {
		this();
		this.packageName = packageName;
	}
	// private static String permDirectory = "policies/";
	// public static String packageName = "com.android.mms";
	public static int test(int n, String packageName, String permDirectory) {
		PolicyEvaluater p = new PolicyEvaluater();
		PolicyVerifier c = new PolicyVerifier();
		Thread t = new Thread(c);
		/*String str = packageName.toString();
		String str2 = permDirectory.toString();*/
		t.start();
		p.run();
		try {
			t.join();
		} catch (Exception e) {
		}
		if (count != 5)
			throw new IllegalStateException("count=" + count);
		System.out.println("count is 5. OK!");
		return count;
	}

	public static class PolicyEvaluater implements Runnable {
		public void run() {
			try {
				Log.d(TAG, "PACKGE NAME IS " + str);
				Log.d(TAG, "PACKGE NAME IS " + str2);
				app.loadPoliciesForPackage(str, str2);
			} catch (PolicyNotFoundException e) {
				// TODO Auto-generated catch block
				Log.d(TAG, "Failed to load policy. Granting access.");
				// e.printStackTrace();
			}
		}
	}

	public static class PolicyVerifier implements Runnable {
		public void run() {
			try {
				Log.d(TAG, "PACKGE NAME IS " + str);
				Log.d(TAG, "PACKGE NAME IS " + str2);
				app.loadPoliciesForPackage(str, str2);
			} catch (PolicyNotFoundException e) {
				// TODO Auto-generated catch block
				Log.d(TAG, "Failed to load policy. Granting access.");
				// e.printStackTrace();
			}
		}
	}
}
