package org.csrdu.apex.helpers;

public class Log {
	// TODO: Create a log4j instance
	public static void d(String tag, String string) {
		System.out.println("D/" + tag +": " + string);
	}

	public static void i(String tag, String string) {
		System.out.println("I/" + tag +": " + string);
	}

	public static void w(String tag, String string) {
		System.out.println("W/" + tag +": " + string);
	}
	
	
}
