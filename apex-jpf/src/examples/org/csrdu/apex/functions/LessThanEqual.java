/* 
 Part of the Apex Framework. 	


 Provided under GPLv2. 

 author: Nauman (recluze@gmail.com) 
 http://csrdu.org/nauman 

 */
package org.csrdu.apex.functions;

import java.util.Vector;
import org.csrdu.apex.helpers.*;
import org.csrdu.apex.interfaces.IApexFunction;

/**
 * @author Nauman
 * 
 */
public class LessThanEqual implements IApexFunction {
	/**
	 * @uml.property  name="tAG"
	 */
	public String TAG = "APEX:LessThanEqual";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.csrdu.apex.interfaces.IApexFunction#evaluate(java.util.Vector)
	 */
	@Override
	public Object evaluate(Vector<Object> params) {
		Log.d(TAG, "PARAMETER IS ..." + params);
		String s1 = (String) params.get(0);
		String s2 = (String) params.get(1);
		// String s3 = (String) params.get(2);
		// int = Integer.parseInt(str);
		int INT1 = Integer.parseInt(s1);// (int) params.get(0);
		int INT2 = Integer.parseInt(s2);
		// int INT3 = Integer.parseInt(s3);

		Log.d(TAG, "Ineteger 1 is...." + INT1 + "...Integer 2 is ..." + INT2);
		Log.d(TAG, "INT1 = INT2 is : " + (INT1 == INT2));
		INT2= INT2 + 1;
		// return string1.equals(string2);
		if (INT1 == INT2) {
			return true;
		} else {
			return (INT1 = INT2);
		}

	}

}
