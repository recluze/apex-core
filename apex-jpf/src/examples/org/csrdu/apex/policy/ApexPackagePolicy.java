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
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

//import com.android.internal.util.XmlUtils;

//TODO: Fix before incorporation in AOSP
//import android.util.Log;
import org.csrdu.apex.helpers.Log;

//TODO: Fix before incorporation in AOSP
//import android.util.Xml;

public class ApexPackagePolicy extends DefaultHandler {
	/**
	 * @uml.property  name="tAG"
	 */
	public String TAG = "APEX:ApexPackagePolicy";
	/**
	 * @uml.property  name="packageName"
	 */
	public String packageName;
	/**
	 * @uml.property  name="policyDirectory"
	 */
	public String policyDirectory;
	/**
	 * @uml.property  name="policies"
	 */
	public Vector<ApexPolicy> policies;
	/**
	 * @uml.property  name="stck"
	 */
	Stack<Expression> stck;
	/**
	 * @uml.property  name="exp"
	 * @uml.associationEnd  readOnly="true"
	 */
	Expression exp;
	/**
	 * @uml.property  name="curExpr"
	 * @uml.associationEnd  
	 */
	Expression curExpr;
	/**
	 * @uml.property  name="_policy"
	 * @uml.associationEnd  
	 */
	ApexPolicy _policy = null;
	/**
	 * @uml.property  name="_constraint"
	 * @uml.associationEnd  
	 */
	Constraint _constraint = null;
	/**
	 * @uml.property  name="_expr"
	 * @uml.associationEnd  
	 */
	Expression _expr = null;
	/**
	 * @uml.property  name="_isConstraint"
	 */
	boolean _isConstraint = false;
	/**
	 * @uml.property  name="_isUpdates"
	 */
	boolean _isUpdates = false;
	/**
	 * @uml.property  name="_exprStack"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="org.csrdu.apex.policy.Expression"
	 */
	Stack<Expression> _exprStack;
	static XMLReader xr;
	static ApexPackagePolicy exprs;

	/**
	 * @uml.property  name="attributeManager"
	 * @uml.associationEnd  
	 */
	public AttributeManager attributeManager;

	/**
	 * Default constructor for the package
	 */
	public ApexPackagePolicy() {
		this.policies = new Vector<ApexPolicy>();
		stck = new Stack<Expression>();
		_exprStack = new Stack<Expression>();
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
	 * @param manager  Instance of an AttributeManager class suitable for this  package
	 * @uml.property  name="attributeManager"
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
	public void loadPoliciesForPackage(String packageName,
			String policyDirectory) throws PolicyNotFoundException {
		this.packageName = packageName;
		this.policyDirectory = policyDirectory;

		// load the policy file based on package name
		Log.d(TAG, "Loading policy file for : " + packageName + " from: "
				+ policyDirectory);
		// FileReader policyReader = getPolicyReader(policyDirectory +
		// packageName);

		// parse the file and populate the 'policies' field
		try {
			Log.d(TAG, ".........CHECKING..1.....");
			xr = XMLReaderFactory.createXMLReader();
			Log.d(TAG, ".........CHECKING..2.....");
			exprs = new ApexPackagePolicy();
			// xr.setContentHandler(exprs);
			xr.setContentHandler(this);
			/*xr.parse(new InputSource(new FileReader(
					"policies/com.android.clock")));*/

			Log.d(TAG, ".........CHECKING..3.......");
			xr.parse(new InputSource(new
			 FileReader("policies/com.android.mms")));

		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			org.xml.sax.Attributes attr) throws SAXException {
		// TODO Auto-generated method stub
		// super.startElement(uri, localName, qName, attr);
		Log.d(TAG, "..SAX ELEMENT START...:" + qName);

		if (localName.equals("Policies")) {
			Log.d(TAG, "..POLICIES...:" + qName);
		}

		else if (qName.equalsIgnoreCase("Policy")) {
			// create a new policy object
			_policy = new ApexPolicy();
			this.policies.add(_policy);

			if (curExpr != null)
				stck.push(curExpr);
			curExpr = new Expression();
			// find and set the effect
			String _attrEffect = attr.getValue("Effect");
			// String _attrName = attr.getValue("Name");
			ApexPolicy.PolicyEffect _policyEffect = _attrEffect
					.equalsIgnoreCase("Permit") ? ApexPolicy.PolicyEffect.PERMIT
					: ApexPolicy.PolicyEffect.DENY;

			_policy.setEffect(_policyEffect);
			Log.d(TAG, "Found and added new policy with effect: " + _attrEffect);
		} else if (qName.equalsIgnoreCase("Constraint")) {
			_constraint = new Constraint();
			_isConstraint = true;
			_policy.setConstraint(_constraint);
			String _combAlgo = attr.getValue("CombiningAlgorithm");
			_constraint.setCombingingAlgorithm(_combAlgo);
			Log.d(TAG, "Found and added new constraint with combining algo: "
					+ _combAlgo);
		} else if (qName.equalsIgnoreCase("Permission")) {
			if (curExpr != null)
				stck.push(curExpr);
			curExpr = new Expression();

			String _permName = attr.getValue("Name");
			_policy.setPermission(_permName);
			Log.d(TAG, "Found and added associated permission with name: "
					+ _permName);
		}

		else if (qName.equalsIgnoreCase("Expression")
				|| qName.equalsIgnoreCase("ApplicationAttribute")
				|| qName.equalsIgnoreCase("SharedAttribute")
				|| qName.equalsIgnoreCase("SystemAttribute")
				|| qName.equalsIgnoreCase("Constant")) {
			if (_expr != null) {
				Log.d(TAG, "Found nested expression of type " + qName
						+ ". Pushing current one on stack.");
				Expression _expr2 = new Expression();
				_expr.addSubExpression(_expr2);
				_exprStack.push(_expr);
				_expr = _expr2;
			} else {
				Log.d(TAG, "Found base expression of type " + qName);
				_expr = new Expression();
				if (_isConstraint) {
					Log.d(TAG, "Adding to base constraint");
					_constraint.addExpression(_expr);
				} else if (_isUpdates) {
					Log.d(TAG, "Adding to base updates");
					// add to updates instead
				}
			}

			// END First ELSE,,,,,,,,,

			if (qName.equalsIgnoreCase("Expression")) {
				String _functionId = attr.getValue("FunctionId");
				_expr.setFunctionId(_functionId);
				_expr.setType(Expression.ExpressionType.Expression);
				Log.d(TAG, "Found and added new expression with FunctionId: "
						+ _functionId);
			} else if (qName.equalsIgnoreCase("ApplicationAttribute")) {
				String _attribName = attr.getValue("AttributeName");
				String _defaultVal = attr.getValue("Default");
				_expr.setAttributeName(_attribName);
				_expr.setDefaultValue(_defaultVal);
				_expr.setType(Expression.ExpressionType.ApplicationAttribute);
				Log.d(TAG,
						"Found and added new ApplicationAttribute with attribute name: "
								+ _attribName + " and default:" + _defaultVal);
			} else if (qName.equalsIgnoreCase("SharedAttribute")) {
				String _sharedAttName = attr.getValue("SharedAtt");
				String _sharedDefValu = attr.getValue("Default");
				_expr.setAttributeName(_sharedAttName);
				_expr.setDefaultValue(_sharedDefValu);
				_expr.setType(Expression.ExpressionType.SharedAttribute);
				Log.d(TAG,
						"Found and added new SharedAttribute with attribute name: "
								+ _sharedAttName + " and default:"
								+ _sharedDefValu);
			} else if (qName.equalsIgnoreCase("SystemAttribute")) {
				String _attribName = attr.getValue("AttributeName");
				String _defaultVal = attr.getValue("Default");
				_expr.setAttributeName(_attribName);
				_expr.setDefaultValue(_defaultVal);
				_expr.setType(Expression.ExpressionType.SystemAttribute);
				Log.d(TAG,
						"Found and added new SystemAttribute with attribute name: "
								+ _attribName + " and default:" + _defaultVal);
			} else if (qName.equalsIgnoreCase("Constant")) {
				String _defaultValue = attr.getValue("Value");
				_expr.setDefaultValue(_defaultValue);
				_expr.setType(Expression.ExpressionType.Constant);
				Log.d(TAG, "Found and added new Constant with value : "
						+ _defaultValue);
			}

		} /*
		 * else if (qName.equalsIgnoreCase("Updates") ||
		 * qName.equalsIgnoreCase("Update") ||
		 * qName.equalsIgnoreCase("Expression") ||
		 * qName.equalsIgnoreCase("ApplicationAttribute") ||
		 * qName.equalsIgnoreCase("SharedAttribute") ||
		 * qName.equalsIgnoreCase("Constant")) { if (_expr != null) { Log.d(TAG,
		 * "Found nested Updates of type " + qName +
		 * ". Pushing current one on stack."); Expression _expr2 = new
		 * Expression(); _expr.addSubExpression(_expr2); _exprStack.push(_expr);
		 * _expr = _expr2; } else { Log.d(TAG, "Found base expression of type "
		 * + qName); _expr = new Expression(); if (_isConstraint) { Log.d(TAG,
		 * "Adding to base constraint"); _constraint.addExpression(_expr); }
		 * else if (_isUpdates) { Log.d(TAG, "Adding to base updates"); // add
		 * to updates instead } } if (qName.equalsIgnoreCase("Expression")) {
		 * String _functionId = attr.getValue("FunctionId");
		 * _expr.setFunctionId(_functionId);
		 * _expr.setType(Expression.ExpressionType.Expression); Log.d(TAG,
		 * "Found and added new expression with FunctionId: " + _functionId); }
		 * else if (qName.equalsIgnoreCase("ApplicationAttribute")) { String
		 * _attribName = attr.getValue("AttributeName"); String _defaultVal =
		 * attr.getValue("Default"); _expr.setAttributeName(_attribName);
		 * _expr.setDefaultValue(_defaultVal);
		 * _expr.setType(Expression.ExpressionType.ApplicationAttribute);
		 * Log.d(TAG,
		 * "Found and added new ApplicationAttribute with attribute name: " +
		 * _attribName + " and default:" + _defaultVal); } else if
		 * (qName.equalsIgnoreCase("SharedAttribute")) { String _sharedAttName =
		 * attr.getValue("SharedAtt"); String _sharedDefValu =
		 * attr.getValue("Default"); _expr.setAttributeName(_sharedAttName);
		 * _expr.setDefaultValue(_sharedDefValu);
		 * _expr.setType(Expression.ExpressionType.SharedAttribute); Log.d(TAG,
		 * "Found and added new SharedAttribute with attribute name: " +
		 * _sharedAttName + " and default:" + _sharedDefValu); } else if
		 * (qName.equalsIgnoreCase("Constant")) { String _defaultValue =
		 * attr.getValue("Value"); _expr.setDefaultValue(_defaultValue);
		 * _expr.setType(Expression.ExpressionType.Constant); Log.d(TAG,
		 * "Found and added new Constant with value : " + _defaultValue); } }
		 */
	}// End startElement....
		// Start enDElement

	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		// Log.d(TAG,"END.SAX ELEMENT...:"+qName);
		if (qName.equalsIgnoreCase("Expression")
				|| qName.equalsIgnoreCase("ApplicationAttribute")
				|| qName.equalsIgnoreCase("SharedAttribute")
				|| qName.equalsIgnoreCase("SystemAttribute")
				|| qName.equalsIgnoreCase("Constant")) {
			// only pop if we're not at the last one!
			if (_exprStack.size() > 0) {
				Log.d(TAG, "Found end of " + qName
						+ " tag. Popping last expression from stack of size: "
						+ _exprStack.size());
				_expr = _exprStack.pop();
				// Log.d(TAG, "Current expression is now:" +
				// _expr.toString());
			} else {
				Log.d(TAG, "Found end of " + qName
						+ " tag. Expression stack size is:" + _exprStack.size());
				_expr = null;
			}
			Log.d(TAG, "Expression stack size is now: " + _exprStack.size());
		} else {
			Log.d(TAG, "Encountered end tag: " + qName);
			Log.d("TEST ................. ", this.policies.toString());
		}
	} // End ......

	/*
	 * private FileReader getPolicyReader(String policyFile) throws
	 * PolicyNotFoundException { FileReader policyReader = null; try {
	 * policyReader = new FileReader(policyFile); } catch (FileNotFoundException
	 * e) { Log.w(TAG, "Couldn't find or policy file " + policyFile); throw new
	 * PolicyNotFoundException(); } return policyReader; }
	 */
	/**
	 * Load from previously parsed/generated policies
	 * 
	 * @param policies
	 *            Vector of policies to load
	 * 
	 */
	/*
	 * public void LoadPolicies(Vector<ApexPolicy> policies) { this.policies =
	 * policies; }
	 */

	/**
	 * Overridden toString function
	 */
	public String toString() {
		String strVal = "ApexPackagePolicy: ";
		Log.d(TAG, "....." + policies);
		for (ApexPolicy policy : this.policies) {
			Log.d(TAG, "L...");
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
	public boolean evaluatePolicies(AttributeManager attributeManager,
			String permName) {
		for (ApexPolicy ap : policies) {
			// if false is returned, return false to calling function. Any deny
			// is a full deny
			 Log.d(TAG,"APEX PP..........SAEED......"+permName);
			if (!ap.getPermission().equals(permName))
			{Log.d(TAG,"Inner If....evaluate"+!ap.getPermission().equals(permName));
				continue;}
			if (!ap.evaluatePolicy(attributeManager, packageName))
			{Log.d(TAG,"Inner If....evaluate"+!ap.evaluatePolicy(attributeManager, packageName));
				return false;}
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
		Log.d(TAG, "...Permission Name...." + permName);
		for (ApexPolicy policy : policies) {
			Log.d(TAG, "FOR LOOP...Permission Name...." + permName);
			if (policy.getPermission().equals(permName))
				return true;
		}
		return false;
	}
	/*
	 * @Override public Object getContent(URLConnection urlc) throws IOException
	 * { // TODO Auto-generated method stub return null; }
	 */
}
