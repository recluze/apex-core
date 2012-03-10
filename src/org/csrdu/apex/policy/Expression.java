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
	public String TAG = "APEX:Expression";

	public String functionId;

	public enum ExpressionType {
		ApplicationAttribute, Expression, SystemAttribute, Constant
	}

	public ExpressionType type;
	public String attributeName;
	public String defaultValue;
	public Vector<Expression> subExpressions;

	/**
	 * Public constructor
	 */
	public Expression() {
		subExpressions = new Vector<Expression>();
	}

	/**
	 * Get the type of the expression
	 * 
	 * @return Type of the expression
	 */
	public ExpressionType getType() {
		return type;
	}

	/**
	 * Set the type of the expression
	 * 
	 * @param type
	 *            Type of the expression
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
			strVal = "<ApplicationAttribute AttributeName='" + attributeName + "' Default='" + defaultValue + "' />\n";
		} else if (this.type == ExpressionType.SystemAttribute) {
			strVal = "<SystemAttribute AttributeName='" + attributeName + "' Default='" + defaultValue + "' />\n";
		} else if (this.type == ExpressionType.Constant) {
			strVal = "<Constant Value='" + defaultValue + "' />\n";
		}

		return strVal;
	}

	/**
	 * Set the Function ID for this expression
	 * 
	 * @param functionId
	 *            Function ID to set
	 */
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	/**
	 * Get the attribute name
	 * 
	 * @return the attributeName
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * Set the attribute name
	 * 
	 * @param attributeName
	 *            the attributeName to set
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	/**
	 * Get the default value for system and application attribute
	 * 
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Set the default value for application and system attribute
	 * 
	 * @param defaultValue
	 *            the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * Evaluate the expression based on its type.
	 * @param attributeManager AttributeManager for resolving the application and system attributes 
	 * @param packageName Package this expression is associated with
	 * 
	 * @return return value. Might be any Object.
	 */
	public Object evaluate(AttributeManager attributeManager, String packageName) {
		Object retVal = null;

		if (this.type == ExpressionType.Expression) {
			Log.d(TAG, "Evaluating expression with functionId: " + this.functionId);
			Vector<Object> subVals = new Vector<Object>();
			for (Expression subExpr : this.subExpressions) {
				subVals.add(subExpr.evaluate(attributeManager, packageName));
			}
			try {
				IApexFunction func = (IApexFunction) Class.forName(this.functionId).newInstance();
				Log.d(TAG, "Calling functionId: " + this.functionId + " with values:");
				for(Object _v : subVals)
					Log.d(TAG, _v.toString());
				retVal = func.evaluate(subVals);
				Log.d(TAG, "FunctionId [" + this.functionId + "] returning: " + retVal.toString());
			} catch (ClassNotFoundException e) {
				Log.d(TAG, "Failed to find/load function class: " + this.functionId + ". Returning [FALSE].");
				return new Boolean(false);
			} catch (InstantiationException e) {
				Log.d(TAG, "Failed to instantiate function class: " + this.functionId + ". Returning [FALSE].");
				return new Boolean(false);
			} catch (Exception e) {
				Log.d(TAG, "Unknown exception in function class: " + this.functionId);
				Log.d(TAG, e.getMessage());
				Log.d(TAG, "Returning [FALSE].");
				return new Boolean(false);
			}
		} else if (this.type == ExpressionType.ApplicationAttribute) {
			// return default value if attribute manager returns null...
			retVal = attributeManager.lookupApplicationAttribute(packageName, this.attributeName);
		} else if (this.type == ExpressionType.SystemAttribute) {

		} else if (this.type == ExpressionType.Constant) {
			Log.d(TAG, "Evaluated constant with value: " + this.defaultValue);
			return this.defaultValue;
		}

		
		return retVal;
	}

}
