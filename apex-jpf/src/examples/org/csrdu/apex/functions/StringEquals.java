/* 
	Part of the Apex Framework. 	

	
	Provided under GPLv2. 
	
	author: Nauman (recluze@gmail.com) 
			http://csrdu.org/nauman 

*/ 


package org.csrdu.apex.functions;
import java.util.Vector;

import org.csrdu.apex.helpers.Log;
import org.csrdu.apex.interfaces.IApexFunction;

/**
 * @author Nauman
 *
 */
public class StringEquals implements IApexFunction { 
	/**
	 * @uml.property  name="tAG"
	 */
	public String TAG = "APEX:StringEquals";
	/* (non-Javadoc)
	 * @see org.csrdu.apex.interfaces.IApexFunction#evaluate(java.util.Vector)
	 */
	@Override
	public Object evaluate(Vector<Object> params) {
		Log.d(TAG, "parameter is "+params);
		String string1 = (String) params.get(0);
		String string2 = (String) params.get(1);
		return string1.equals(string2);
	}

}
