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
import org.csrdu.apex.interfaces.IApexFunction;

//import android.util.Log;

public class Expression {
	/**
	 * @uml.property  name="tAG"
	 */
	public String TAG = "APEX:Expression";

	/**
	 * @uml.property  name="functionId"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="org.csrdu.apex.interfaces.IApexFunction"
	 */
	public String functionId;

	/**
	 * @author   Saeed Iqbal
	 */
	public enum ExpressionType {
		/**
		 * @uml.property  name="applicationAttribute"
		 * @uml.associationEnd  
		 */
		ApplicationAttribute, /**
		 * @uml.property  name="expression"
		 * @uml.associationEnd  
		 */
		Expression, /**
		 * @uml.property  name="systemAttribute"
		 * @uml.associationEnd  
		 */
		SystemAttribute, /**
		 * @uml.property  name="constant"
		 * @uml.associationEnd  
		 */
		Constant, /**
		 * @uml.property  name="sharedAttribute"
		 * @uml.associationEnd  
		 */
		SharedAttribute
	}

	/**
	 * @uml.property  name="type"
	 * @uml.associationEnd  
	 */
	public ExpressionType type;
	/**
	 * @uml.property  name="attributeName"
	 */
	public String attributeName;
	/**
	 * @uml.property  name="defaultValue"
	 */
	public String defaultValue;
	/**
	 * @uml.property  name="subExpressions"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="org.csrdu.apex.policy.Expression"
	 */
	public Vector<Expression> subExpressions;

	/**
	 * Public constructor
	 */
	public Expression() {
		subExpressions = new Vector<Expression>();
	}

	/**
	 * Get the type of the expression
	 * @return  Type of the expression
	 * @uml.property  name="type"
	 */
	public ExpressionType getType() {
		return type;
	}

	/**
	 * Set the type of the expression
	 * @param type  Type of the expression
	 * @uml.property  name="type"
	 */
	public void setType(ExpressionType type) {
		this.type = type;
	}

	/**
	 * Add Sub Expression to the collection of SubExpressions
	 * 
	 * @param expr
	 *            Expression to add
	 */
	public void addSubExpression(Expression expr) {
		subExpressions.add(expr);
	}

	public String toString() {
		String strVal = "";
		if (this.type == ExpressionType.Expression) {
			strVal = "<Expression FunctionId='" + functionId + "'>\n";
			for (Expression expr : subExpressions) {
				strVal += expr.toString();
			}
			strVal += "</Expression>\n";
		} else if (this.type == ExpressionType.ApplicationAttribute) {
			strVal = "<ApplicationAttribute AttributeName='" + attributeName
					+ "' Default='" + defaultValue + "' />\n";
		} else if (this.type == ExpressionType.SharedAttribute) {//my code
			strVal = "	<SharedAttribute SharedAtt='" + attributeName
					+ "' Default='" + defaultValue + "' />\n";
			Log.d(TAG, "DEfault value is : "+defaultValue);
		} else if (this.type == ExpressionType.SystemAttribute) {
			strVal = "<SystemAttribute AttributeName='" + attributeName
					+ "' Default='" + defaultValue + "' />\n";
		} else if (this.type == ExpressionType.Constant) {
			strVal = "<Constant Value='" + defaultValue + "' />\n";
		}

		return strVal;
	}

	/**
	 * Set the Function ID for this expression
	 * @param functionId  Function ID to set
	 * @uml.property  name="functionId"
	 */
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	/**
	 * Get the attribute name
	 * @return  the attributeName
	 * @uml.property  name="attributeName"
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * Set the attribute name
	 * @param attributeName  the attributeName to set
	 * @uml.property  name="attributeName"
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	/**
	 * Get the default value for system and application attribute
	 * @return  the defaultValue
	 * @uml.property  name="defaultValue"
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Set the default value for application and system attribute
	 * @param defaultValue  the defaultValue to set
	 * @uml.property  name="defaultValue"
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * Evaluate the expression based on its type.
	 * 
	 * @param attributeManager
	 *            AttributeManager for resolving the application and system
	 *            attributes
	 * @param packageName
	 *            Package this expression is associated with
	 * 
	 * @return return value. Might be any Object.
	 */
	public Object evaluate(AttributeManager attributeManager, String packageName) {
		Object retVal = null;
		//Log.d(TAG, "Function ID is "+functionId);
		if (this.type == ExpressionType.Expression) {
			Log.d(TAG, "Evaluating expression with functionId: "
					+ this.functionId);
			Vector<Object> subVals = new Vector<Object>();
			Log.d(TAG,"AFTER VERCTOR>>>>>>>>>");
			for (Expression subExpr : this.subExpressions) {
				subVals.add(subExpr.evaluate(attributeManager, packageName));
				Log.d(TAG,"EXPRESION in For loop>>>>>>>>>");
			}
			try {
				Log.d(TAG,"EXPRESION in try block.....");
				IApexFunction func = (IApexFunction) Class.forName(
						this.functionId).newInstance();
				Log.d(TAG, "Calling functionId: " + this.functionId
						+ " with values:" +subVals);
				for (Object _v : subVals)
				retVal = func.evaluate(subVals);
				Log.d(TAG,"After func.evaluate is.. "+retVal);
				Log.d(TAG, "FunctionId [" + this.functionId + "] returning: "
						+ retVal);
			} catch (ClassNotFoundException e) {
				Log.d(TAG, "Failed to find/load function class: "
						+ this.functionId + ". Returning [FALSE].");
				return new Boolean(false);
			} catch (InstantiationException e) {
				Log.d(TAG, "Failed to instantiate function class: "
						+ this.functionId + ". Returning [FALSE].");
				return new Boolean(false);
			} catch (Exception e) {
				Log.d(TAG, "Unknown exception in function class: "
						+ this.functionId);
				e.printStackTrace(); //my edition
				Log.d(TAG, e.getMessage());
				Log.d(TAG, "Returning [FALSE].");
				return new Boolean(false);
			}
		} else if (this.type == ExpressionType.ApplicationAttribute) {
			// return default value if attribute manager returns null...
			retVal = attributeManager.lookupApplicationAttribute(packageName,
					this.attributeName);
			Log.d(TAG,"Return Value is : " +retVal);
		} else if (this.type == ExpressionType.SharedAttribute) { //my code
			retVal = attributeManager.lookupApplicationAttribute(packageName,
			this.attributeName);
			int retShVal = Integer.parseInt((String) retVal);
			Log.d(TAG,"Shared Attribute return Value is : " + --retShVal );
		} else if (this.type == ExpressionType.SystemAttribute) {
			retVal = attributeManager.lookupApplicationAttribute(packageName,
					this.attributeName);
			Log.d(TAG,"System Attribute Return Value is : " +retVal);
		} else if (this.type == ExpressionType.Constant) {
			Log.d(TAG, "Evaluated constant with value: " + this.defaultValue);
			return this.defaultValue;
		}

		return retVal;
	}

}
