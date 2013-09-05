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


//import android.util.Log;

public class Constraint {
	/**
	 * @uml.property  name="tAG"
	 */
	public String TAG = "APEX:Constraint";

	/**
	 * @uml.property  name="combingingAlgorithm"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="org.csrdu.apex.interfaces.IApexCombiningAlgorithms"
	 */
	public String combingingAlgorithm;
	/**
	 * @uml.property  name="expressions"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="org.csrdu.apex.policy.Expression"
	 */
	Vector<Expression> expressions;

	public Constraint() {
		expressions = new Vector<Expression>();
	}

	/**
	 * Get combining algorithm for this constraint
	 * @return  Combining algorithm
	 * @uml.property  name="combingingAlgorithm"
	 */
	public String getCombingingAlgorithm() {
		return combingingAlgorithm;
	}

	/**
	 * Set combining algorithm for this constraint
	 * @param combingingAlgorithm  Combining algo to set
	 * @uml.property  name="combingingAlgorithm"
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
	//My edition...........
	public Object evaluateInt(AttributeManager attributeManager, String packageName){
		Log.d(TAG, "Evaluating constraint with combining algo: " + this.combingingAlgorithm);
		//Vector<Boolean> exprResults = new Vector<Boolean>();
		Vector<Object> exprResultsInt = new Vector<Object>();// my edition
		/*if(exprResults.equals("true") || exprResults.equals("false"))
		{
		for (Expression expr : expressions) {
			Log.d(TAG,"Attribute Manager is.."+attributeManager);
			exprResults.add((Boolean) expr.evaluate(attributeManager, packageName));
		}
		}*/
		//else//my edition
		//{
			for(Expression expr : expressions){
				Log.d(TAG, "TAG, Attribute Manager is.."+attributeManager);
				exprResultsInt.add((Object) expr.evaluate(attributeManager, packageName));
			}
		

		IApexCombiningAlgorithms algo;
		try {
			algo = (IApexCombiningAlgorithms) Class.forName(this.combingingAlgorithm).newInstance();
		} catch (IllegalAccessException e) {
			Log.d(TAG, "IllegalAccessException in constraint combining algo. Granting access.");
			e.printStackTrace();
			return 1;
		} catch (InstantiationException e) {
			Log.d(TAG, "InstantiationException in constraint combining algo. Granting access.");
			e.printStackTrace();
			return 1;
		} catch (ClassNotFoundException e) {
			Log.d(TAG, "Combining algorithm not found: " + this.combingingAlgorithm + ". Granting access.");
			e.printStackTrace();
			return 1;
		}
		Log.d(TAG, "Calling functionId: " + this.combingingAlgorithm + " with values:");
		/*if(exprResults.equals("true") || exprResults.equals("false"))//my edition
		{//my edition
		for (Object _v : exprResults)
			Log.d(TAG, _v.toString());
		Boolean retVal = algo.evaluate(exprResults);
		Log.d(TAG, "CombiningAlgo [" + this.combingingAlgorithm + "] returning: " + retVal.toString());

		return retVal;
		}*/	//my edition
		//else{//my edition
			for(Object _v : exprResultsInt)//my edition
				Log.d(TAG, ""+_v);//my edition
			Object retValInt = algo.evaluateInt(exprResultsInt);//my edition
			Log.d(TAG, "CombiningAlgo [" + this.combingingAlgorithm + "] returning: " + retValInt.toString());//my edition
			return retValInt;
		//}//my edition
	
 
	} // My edition end
	public boolean evaluate(AttributeManager attributeManager, String packageName) {
		Log.d(TAG, "Evaluating constraint with combining algo: " + this.combingingAlgorithm);
		Vector<Boolean> exprResults = new Vector<Boolean>();
		
		for (Expression expr : expressions) {
			Log.d(TAG,"Attribute Manager is.."+attributeManager);
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
