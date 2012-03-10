/* 
	Part of the Apex Framework. 

	
	Provided under GPLv2. 
	
	author: Nauman (recluze@gmail.com) 
			http://csrdu.org/nauman 

 */
package org.csrdu.apex.policy;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;
import java.util.Vector;

import org.csrdu.apex.AttributeManager;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

//import com.android.internal.util.XmlUtils;
import org.csrdu.apex.helpers.XmlUtils;

//TODO: Fix before incorporation in AOSP
//import android.util.Log;
import org.csrdu.apex.helpers.Log;

//TODO: Fix before incorporation in AOSP
//import android.util.Xml;
import org.csrdu.apex.helpers.Xml;

public class ApexPackagePolicy {
	public String TAG = "APEX:ApexPackagePolicy";
	public String packageName;
	public String policyDirectory;
	public Vector<ApexPolicy> policies;

	public AttributeManager attributeManager;

	/**
	 * Default constructor for the package
	 */
	public ApexPackagePolicy() {
		this.policies = new Vector<ApexPolicy>();
	}

	/**
	 * Constructor on package name this ApexPackagePolicy is associated with
	 * 
	 * @param packageName
	 *            Name of the package
	 * 
	 */
	public ApexPackagePolicy(String packageName) {
		this();
		this.packageName = packageName;
	}

	/**
	 * Set the attribute manager for use with the policies
	 * 
	 * @param manager
	 *            Instance of an AttributeManager class suitable for this
	 *            package
	 * 
	 */
	public void setAttributeManager(AttributeManager manager) {
		this.attributeManager = manager;
	}

	/**
	 * Load policies for a package from the system file
	 * 
	 * @param packageName
	 *            Package name to load policies for. Defines the policy file
	 *            name as well.
	 * @throws PolicyNotFoundException 
	 * 
	 */
	public void loadPoliciesForPackage(String packageName, String policyDirectory) throws PolicyNotFoundException {
		this.packageName = packageName;
		this.policyDirectory = policyDirectory;

		// load the policy file based on package name
		Log.d(TAG, "Loading policy file for : " + packageName + " from: " + policyDirectory);
		FileReader policyReader = getPolicyReader(policyDirectory + packageName);

		// parse the file and populate the 'policies' field
		try {
			XmlPullParser parser = Xml.newPullParser();

			parser.setInput(policyReader);
			XmlUtils.beginDocument(parser, "Policies");

			// for the possible new policy etc
			ApexPolicy _policy = null;
			Constraint _constraint = null;
			Expression _expr = null;
			boolean _isConstraint = false;
			boolean _isUpdates = false;

			Stack<Expression> _exprStack = new Stack<Expression>();

			while (true) {
				// XmlUtils.nextElement(parser);
				parser.next();
				if (parser.getEventType() == XmlPullParser.END_DOCUMENT) {
					break;
				} else if (parser.getEventType() == XmlPullParser.START_TAG) {
					String name = parser.getName();
					if ("Policy".equals(name)) {
						// create a new policy object
						_policy = new ApexPolicy();
						this.policies.add(_policy);

						// find and set the effect
						String _attrEffect = parser.getAttributeValue(null, "Effect");
						String _attrName = parser.getAttributeValue(null, "Name");
						ApexPolicy.PolicyEffect _policyEffect = _attrEffect.equalsIgnoreCase("Permit") ? ApexPolicy.PolicyEffect.PERMIT
								: ApexPolicy.PolicyEffect.DENY;

						_policy.setEffect(_policyEffect);
						Log.d(TAG, "Found and added new policy with effect: " + _attrEffect);

					} else if ("Constraint".equals(name)) {
						_constraint = new Constraint();
						_isConstraint = true;
						_policy.setConstraint(_constraint);
						String _combAlgo = parser.getAttributeValue(null, "CombiningAlgorithm");
						_constraint.setCombingingAlgorithm(_combAlgo);

						Log.d(TAG, "Found and added new constraint with combining algo: " + _combAlgo);
					} else if ("Permission".equals(name)) {
						String _permName = parser.getAttributeValue(null, "Name");
						_policy.setPermission(_permName);
						Log.d(TAG, "Found and added associated permission with name: " + _permName);
					} else if ("Expression".equals(name) || "ApplicationAttribute".equals(name)
							|| "SystemAttribute".equals(name) || "Constant".equals(name)) {
						if (_expr != null) {
							Log.d(TAG, "Found nested expression of type " + name + ". Pushing current one on stack.");
							Expression _expr2 = new Expression();
							_expr.addSubExpression(_expr2);
							_exprStack.push(_expr);
							_expr = _expr2;
						} else {
							Log.d(TAG, "Found base expression of type " + name);
							_expr = new Expression();
							if (_isConstraint) {
								Log.d(TAG, "Adding to base constraint");
								_constraint.addExpression(_expr);
							} else if (_isUpdates) {
								Log.d(TAG, "Adding to base updates");
								// add to updates instead
							}
						}

						if ("Expression".equals(name)) {
							String _functionId = parser.getAttributeValue(null, "FunctionId");
							_expr.setFunctionId(_functionId);
							_expr.setType(Expression.ExpressionType.Expression);
							Log.d(TAG, "Found and added new expression with FunctionId: " + _functionId);
						} else if ("ApplicationAttribute".equals(name)) {
							String _attribName = parser.getAttributeValue(null, "AttributeName");
							String _defaultVal = parser.getAttributeValue(null, "Default");
							_expr.setAttributeName(_attribName);
							_expr.setDefaultValue(_defaultVal);
							_expr.setType(Expression.ExpressionType.ApplicationAttribute);
							Log.d(TAG, "Found and added new ApplicationAttribute with attribute name: " + _attribName
									+ " and default:" + _defaultVal);
						} else if ("SystemAttribute".equals(name)) {
							String _attribName = parser.getAttributeValue(null, "AttributeName");
							String _defaultVal = parser.getAttributeValue(null, "Default");
							_expr.setAttributeName(_attribName);
							_expr.setDefaultValue(_defaultVal);
							_expr.setType(Expression.ExpressionType.SystemAttribute);
							Log.d(TAG, "Found and added new SystemAttribute with attribute name: " + _attribName
									+ " and default:" + _defaultVal);
						} else if ("Constant".equals(name)) {
							String _defaultValue = parser.getAttributeValue(null, "Value");
							_expr.setDefaultValue(_defaultValue);
							_expr.setType(Expression.ExpressionType.Constant);
							Log.d(TAG, "Found and added new Constant with value : " + _defaultValue);
						}
					}
				} else if (parser.getEventType() == XmlPullParser.END_TAG) {
					String name = parser.getName();
					if ("Expression".equals(name) || "ApplicationAttribute".equals(name)
							|| "SystemAttribute".equals(name) || "Constant".equals(name)) {
						// only pop if we're not at the last one!
						if (_exprStack.size() > 0) {
							Log.d(TAG, "Found end of " + name + " tag. Popping last expression from stack of size: "
									+ _exprStack.size());
							_expr = _exprStack.pop();
							// Log.d(TAG, "Current expression is now:" +
							// _expr.toString());
						} else {
							Log.d(TAG, "Found end of " + name + " tag. Expression stack size is:" + _exprStack.size());
							_expr = null;
						}
						Log.d(TAG, "Expression stack size is now: " + _exprStack.size());
					} else {
						Log.d(TAG, "Encountered end tag: " + parser.getName());
					}
				}

			}

		} catch (XmlPullParserException e) {
			Log.d(TAG, "Error parsing policy file for package: " + packageName + "..." + e.getMessage());
			e.printStackTrace();
			throw new PolicyNotFoundException();
		} catch (IOException e) {
			Log.d(TAG, "Cannot read policy file for package: " + packageName + ". This is normal. Don't panic.");
			e.printStackTrace();
			throw new PolicyNotFoundException();
		}

	}

	private FileReader getPolicyReader(String policyFile) throws PolicyNotFoundException {
		FileReader policyReader = null;
		try {
			policyReader = new FileReader(policyFile);
		} catch (FileNotFoundException e) {
			Log.w(TAG, "Couldn't find or policy file " + policyFile);
			throw new PolicyNotFoundException();
		}
		return policyReader;
	}

	/**
	 * Load from previously parsed/generated policies
	 * 
	 * @param policies
	 *            Vector of policies to load
	 * 
	 */
	public void LoadPolicies(Vector<ApexPolicy> policies) {
		this.policies = policies;
	}

	/**
	 * Overridden toString function
	 */
	public String toString() {
		String strVal = "ApexPackagePolicy: ";
		for (ApexPolicy policy : this.policies) {
			strVal += policy.toString();
		}
		return strVal;

	}

	/**
	 * Evaluate the policies. Apex dictates that the result should be 'true' if
	 * there are no 'DENY' results. If there is any deny result, return false.
	 * 
	 * @param attributeManager
	 * @param permName
	 *            Permission to check for
	 * 
	 * @return true if granted, false if denied
	 */
	public boolean evaluatePolicies(AttributeManager attributeManager, String permName) {
		for (ApexPolicy ap : policies) {
			// if false is returned, return false to calling function. Any deny
			// is a full deny
			if (!ap.getPermission().equals(permName))
				continue;

			if (!ap.evaluatePolicy(attributeManager, packageName))
				return false;
		}
		return true;
	}

	/**
	 * Check if this package has policies associated with permission
	 * 
	 * @param permName
	 *            the permission to check for
	 * @return True if has at least one policy for this permission, false
	 *         otherwise
	 */
	public boolean hasPoliciesForPermission(String permName) {
		for (ApexPolicy policy : policies) {
			if (policy.getPermission().equals(permName))
				return true;
		}
		return false;
	}
}
