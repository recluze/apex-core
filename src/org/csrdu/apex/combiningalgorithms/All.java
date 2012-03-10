/* 
	Part of the Apex Framework. 

	
	Provided under GPLv2. 
	
	author: Nauman (recluze@gmail.com) 
			http://csrdu.org/nauman 

 */

package org.csrdu.apex.combiningalgorithms;

import java.util.Vector;

import org.csrdu.apex.interfaces.IApexCombiningAlgorithms;
import org.csrdu.apex.interfaces.IApexFunction;

/**
 * @author Nauman
 * 
 */
public class All implements IApexCombiningAlgorithms {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.csrdu.apex.interfaces.IApexFunction#evaluate(java.util.Vector)
	 */
	@Override
	public Boolean evaluate(Vector<Boolean> params) {
		// a single false means false
		for (Boolean _p : params) {
			if (!_p) {
				return false;
			}
		}
		return true;
	}

}
