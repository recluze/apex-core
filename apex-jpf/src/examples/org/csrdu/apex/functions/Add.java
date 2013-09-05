
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
public class Add implements IApexFunction { 
/**
 * @uml.property  name="tAG"
 */
public String TAG = "APEX:Add";
/* (non-Javadoc)
 * @see org.csrdu.apex.interfaces.IApexFunction#evaluate(java.util.Vector)
 */
@Override
public Object evaluate(Vector<Object> params) {
	
	/*String s1 = (String) params.get(0);
	String s2 = (String) params.get(1);
	//String s3 = (String) params.get(2);
	//int  = Integer.parseInt(str);
*/	Object obj = (Object)params.get(0);
	Object obj1 = (Object)params.get(1);
	//Object obj2 = (Object)params.get(2);
	/*int INT1 = Integer.parseInt(s1);//(int) params.get(0);
	int INT2 = Integer.parseInt(s2);
	//int INT3 = Integer.parseInt(s3);
	*/
	Log.d(TAG, "Object 1 is...." + obj + "...Object 2 is ..." + obj1);
	//return string1.equals(string2);
	return (obj = obj1);
	
}

}
