/**
 * Part of the Apex Framework.
 */

package org.csrdu.apex.interfaces;
import java.util.Vector; 

/**
 * @author Nauman 
 *
 */
public interface IApexCombiningAlgorithms {

	Boolean evaluate(Vector<Boolean> exprResults);
	Object evaluateInt(Vector<Object> exprResultsStr);// my edition

}
