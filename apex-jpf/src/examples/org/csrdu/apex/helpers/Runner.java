package org.csrdu.apex.helpers;

import org.csrdu.apex.AccessManager;

//import java.util.*;

/*public class Runner {

 public static void main(String args[]){ 
 AccessManager am = new AccessManager();
 //boolean res = false;
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

 }*/

/*public class Runner {

 public static void main(String args[]){ 
 AccessManager am = new AccessManager();
 boolean res = am.checkExtendedPermissionByPackage(
  "android.permission.SEND_SMS", "com.android.mms"
 );
 System.out.println(res);
 System.out.println("==========================================");
 res = am.checkExtendedPermissionByPackage(
  "android.permission.BLAH",
 "com.android.mms"
 );
 System.out.println(res);
 }	
 }*/
 

/*public class Runner extends Thread {
 public static String str1 = "str1";
 public static String str2 = "str2";
 public static String str3 = "str3";
 public static boolean res1 = false;
 //public static boolean res2 = false;
 static AccessManager am = new AccessManager();
 public static int count = 5;

 public static void main(String args[]) {
 test(2);
 }
 public static int test(int n) {
 ApexAccessManagerThreadFirst myThread = new ApexAccessManagerThreadFirst();
 ApexAccessManagerThreadSecond yourThread = new ApexAccessManagerThreadSecond();
 ApexAccessManagerThreadThird thirdThread = new ApexAccessManagerThreadThird();

 Thread t = new Thread(thirdThread);
 myThread.start();
 yourThread.start();
 thirdThread.start();
 return count;
 }

 private static class ApexAccessManagerThreadFirst extends Thread {
 public void run() {
 synchronized (str1) {
 res1 = am.checkExtendedPermissionByPackage(
 "android.permission.SEND_SMS", "com.android.mms");
 System.out.println(res1);
 System.out
 .println("==========================================");
 res1 = am.checkExtendedPermissionByPackage(
 "android.permission.BLAH", "com.android.mms");
 System.out.println(res1);
 try {
 Thread.sleep(100000);
 } catch (InterruptedException e) {
 }
 synchronized (str2) {

 }
 }
 }
 }

 private static class ApexAccessManagerThreadSecond extends Thread {
 public void run() {
 synchronized (str2) {
 res1 = am.checkExtendedPermissionByPackage(
 "android.permission.SEND_SMS", "com.android.mms");
 System.out.println(res1);
 System.out
 .println("==========================================");
 res1 = am.checkExtendedPermissionByPackage(
 "android.permission.BLAH", "com.android.mms");
 System.out.println(res1);
 try {
 Thread.sleep(1000);
 } catch (InterruptedException e) {
 }
 synchronized (str3) {
 }
 }
 }
 }
 private static class ApexAccessManagerThreadThird extends Thread {
 public void run() {
 synchronized (str3) {
 res1 = am.checkExtendedPermissionByPackage(
 
 "android.permission.SEND_SMS", "com.android.mms");
 
 System.out.println(res1);
 System.out
 .println("==========================================");
 res1 = am.checkExtendedPermissionByPackage(
 "android.permission.BLAH", "com.android.mms");
 System.out.println(res1);
 try {
 Thread.sleep(1);
 } catch (InterruptedException e) {
 }
 synchronized (str1) {

 }
 }
 }
 } 
 }*/

public class Runner extends Thread {
	public static String str1 = "str1";
	public static String str2 = "str2";
	public static String str3 = "str3";
	public static boolean res1 = false;
	// public static boolean res2 = false;
	static AccessManager am = new AccessManager();
	public static int count = 5;

	public static void main(String args[]) {
		test(2);
	}

	public static void test(int n) {
		ApexAccessManagerThreadFirst FirstThread = new ApexAccessManagerThreadFirst();
		ApexAccessManagerThreadSecond SecondThread = new ApexAccessManagerThreadSecond();
		// ApexAccessManagerThreadThird thirdThread = new
		// ApexAccessManagerThreadThird();

		Thread t = new Thread(SecondThread);
		t.start();
		FirstThread.run();
		try {
			t.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static class ApexAccessManagerThreadFirst implements Runnable {
		public void run() {

			boolean res1 = am.checkExtendedPermissionByPackage(
					"android.permission.SEND_SMS", "com.android.mms");
			System.out.println(res1);
			System.out.println("==========================================");
			res1 = am.checkExtendedPermissionByPackage(
					"android.permission.BLAH", "com.android.mms");
			System.out.println(res1);
		}
	}

	public static class ApexAccessManagerThreadSecond implements Runnable {
		public void run() {

			boolean res1 = am.checkExtendedPermissionByPackage(
					"android.permission.SEND_SMS", "com.android.mms");
			System.out.println(res1);
			System.out.println("==========================================");
			res1 = am.checkExtendedPermissionByPackage(
					"android.permission.BLAH", "com.android.mms");
			System.out.println(res1);
		}
	}
}