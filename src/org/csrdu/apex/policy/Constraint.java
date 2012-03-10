/* 
	Part of the Apex Framework. 

	
	Provided under GPLv2. 
	
	author: Nauman (recluze@gmail.com) 
			http://csrdu.org/nauman 

 */

package org.csrdu.apex.policy;

import java.util.Vector;

import org.csrdu.apex.AttributeManager;
import org.csrdu.apex.helpers.Log;
import org.csrdu.apex.interfaces.IApexCombiningAlgorithms;
import org.csrdu.apex.interfaces.IApexFunction;

//import android.util.Log;

public class Constraint {
	public String TAG = "APEX:Constraint";

	public String combingingAlgorithm;
	Vector<Expression> expressions;

	public Constraint() {
		expressions = new Vector<Expression>();
	}

	/**
	 * Get combining algorithm for this constraint
	 * 
	 * @return Combining algorithm
	 */
	public String getCombingingAlgorithm() {
		return combingingAlgorithm;
	}

	/**
	 * Set combining algorithm for this constraint
	 * 
	 * @param combingingAlgorithm
	 *            Combining algo to set
	 */
	public void setCombingingAlgorithm(String combingingAlgorithm) {
		this.combingingAlgorithm = combingingAlgorithm;
	}

	public String toString() {
		String strVal = "<Constraint CombiningAlgorithm='" + combingingAlgorithm + "'>";
		for (Expression expr : expressions) {
			strVal += expr.toString();
		}
		strVal += "</Constraint>\n";
		return strVal;

	}

	/**
	 * Add an expression to the constraint
	 * 
	 * @param expr
	 *            Expression to be added
	 */
	public void addExpression(Expression expr) {
		expressions.add(expr);
	}

	/**
	 * Evaluate all expressions. Combination is dictated by the combiningAlgo.
	 * 
	 * @param attributeManager
	 *            AttributeManager for resolving the application and system
	 *            attributes
	 * 
	 * @return true if matched, false if it doesn't.
	 */
	public boolean evaluate(AttributeManager attributeManager, String packageName) {
		Log.d(TAG, "Evaluating constraint with combining algo: " + this.combingingAlgorithm);
		Vector<Boolean> exprResults = new Vector<Boolean>();
		for (Expression expr : expressions) {
			exprResults.add((Boolean) expr.evaluate(attributeManager, packageName));
		}

		IApexCombiningAlgorithms algo;
		try {
			algo = (IApexCombiningAlgorithms) Class.forName(this.combingingAlgorithm).newInstance();
		} catch (IllegalAccessException e) {
			Log.d(TAG, "IllegalAccessException in constraint combining algo. Granting access.");
			e.printStackTrace();
			return true;
		} catch (InstantiationException e) {
			Log.d(TAG, "InstantiationException in constraint combining algo. Granting access.");
			e.printStackTrace();
			return true;
		} catch (ClassNotFoundException e) {
			Log.d(TAG, "Combining algorithm not found: " + this.combingingAlgorithm + ". Granting access.");
			e.printStackTrace();
			return true;
		}
		Log.d(TAG, "Calling functionId: " + this.combingingAlgorithm + " with values:");
		for (Object _v : exprResults)
			Log.d(TAG, _v.toString());
		Boolean retVal = algo.evaluate(exprResults);
		Log.d(TAG, "CombiningAlgo [" + this.combingingAlgorithm + "] returning: " + retVal.toString());

		return retVal;
	}
}
