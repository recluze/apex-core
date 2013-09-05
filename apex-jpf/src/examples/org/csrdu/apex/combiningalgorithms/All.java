/* 
	Part of the Apex Framework. 

	
	Provided under GPLv2. 
	
	author: Nauman (recluze@gmail.com) 
			http://csrdu.org/nauman 

 */


package org.csrdu.apex.combiningalgorithms;
import java.util.Vector;

import org.csrdu.apex.helpers.Log;
import org.csrdu.apex.interfaces.IApexCombiningAlgorithms;


/**
 * @author Nauman
 * 
 */
public class All implements IApexCombiningAlgorithms {
/**
 * @uml.property  name="tAG"
 */
public String TAG = "APEX:All";
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.csrdu.apex.interfaces.IApexFunction#evaluate(java.util.Vector)
	 */
	@Override
	public Boolean evaluate(Vector<Boolean> params) {
		// a single false means false
		/*for (Boolean _p : params) {
			Log.d(TAG,"Paramater is..." +params);
			if (!_p) {
				return false;
			}
		}*/
		return true;
	}
	@Override
	public Object evaluateInt(Vector<Object> params) {
		// TODO Auto-generated method stub
		
		for(Object _p : params)
		{
			Log.d(TAG,"Parameter is ..."+params);
			if(_p.equals(0)){
				return 0;
		}
			}
		
		return 5;
	

}
}
