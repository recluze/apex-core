/* 
	Part of the Apex Framework. 

	
	Provided under GPLv2. 
	
	author: Nauman (recluze@gmail.com) 
			http://csrdu.org/nauman 

 */

package org.csrdu.apex.policy;

import java.util.Vector;

import org.csrdu.apex.AttributeManager;

//import android.util.Log;
import org.csrdu.apex.helpers.Log;

public class ApexPolicy {
	/**
	 * @uml.property  name="tAG"
	 */
	public String TAG = "APEX:ApexPolicy";

	/**
	 * @author   Saeed Iqbal
	 */
	static enum PolicyEffect {
		/**
		 * @uml.property  name="pERMIT"
		 * @uml.associationEnd  
		 */
		PERMIT, /**
		 * @uml.property  name="dENY"
		 * @uml.associationEnd  
		 */
		DENY
	}

	/**
	 * @uml.property  name="name"
	 */
	private String name;
	/**
	 * @uml.property  name="effect"
	 * @uml.associationEnd  
	 */
	private PolicyEffect effect;
	/**
	 * @uml.property  name="constant"
	 * @uml.associationEnd  
	 */
	private Constant constant;
	/**
	 * @uml.property  name="permission"
	 */
	private String permission = "";
	/**
	 * @uml.property  name="constraint"
	 * @uml.associationEnd  
	 */
	private Constraint constraint;
	/**
	 * @uml.property  name="updates"
	 */
	private Vector<Update> updates;

	/**
	 * Get the constraint for this policy
	 * @return  The constraint
	 * @uml.property  name="constraint"
	 */
	public Constraint getConstraint() {
		return constraint;
	}

	/**
	 * Set the constraint for this policy
	 * @param constraint  Constraint to set
	 * @uml.property  name="constraint"
	 */
	public void setConstraint(Constraint constraint) {
		Log.d(TAG, "APEX SAEED>>>>>>" + constraint);
		this.constraint = constraint;
	}

	/**
	 * Get the Constant for this policy
	 * @return  The constant EDIT BY: SAEED IQBAL
	 * @uml.property  name="constant"
	 */
	public Constant getConstant() {
		Log.d(TAG, "SAEED IQBAL......." + constant);
		return constant;
	}

	/**
	 * Set the Constant for this policy
	 * @param constraint  constant to set EDIT BY: SAEED IQBAL
	 * @uml.property  name="constant"
	 */
	public void setConstant(Constant constant) {
		Log.d(TAG, "SAEED IQBAL......." + constant);
		this.constant = constant;
	}

	public Vector<Update> getUpdates() {
		Log.d(TAG, "APEX SAEED......" + updates);
		return updates;
	}

	public void setUpdates(Vector<Update> updates) {
		this.updates = updates;
	}

	/**
	 * Set the Effect of the policy
	 * @param effect  The effect. May be PERMIT or DENY
	 * @uml.property  name="effect"
	 */
	public void setEffect(PolicyEffect effect) {
		this.effect = effect;
	}

	/**
	 * Overridden toString function
	 */
	public String toString() {
		String strVal = "\n<ApexPolicy Effect='" + effect + "'>\n";
		strVal += "<Permission Name='" + permission + "'/>\n";

		if (this.constraint != null)
			strVal += this.constraint.toString();
		// also add updates here

		strVal += "</ApexPolicy>\n";
		return strVal;
	}

	/**
	 * Evaluate the policy.
	 * 
	 * @param attributeManager
	 *            AttributeManager for resolving application and system
	 *            attributes
	 * @param packageName
	 *            Package this policy is associated with
	 * 
	 * @return true if granted, false if denies.
	 */
	public boolean evaluatePolicy(AttributeManager attributeManager,
			String packageName) {
		Log.d(TAG, "Evaluating policy: " + this.name);
		// evaluate all constraints
		Log.d(TAG, "SAEED..APolicy...." + packageName + "... and..."
				+ attributeManager);// Only for Checking
		if (packageName.equalsIgnoreCase("com.android.clock")) {// My Edition
			boolean evaluationResult = constraint.evaluate(attributeManager,
					packageName);
			Log.d(TAG, "Running updates for policy: " + this.name);
			// updates are ALWAYS run. So, keeping them separate here...
			if (effect == PolicyEffect.PERMIT) {
				return evaluationResult;
				// return !evaluationResult;
			} else if (effect == PolicyEffect.DENY) {
				return !evaluationResult;
				// return evaluationResult;
			} else {
				Log.d(TAG, "Found unexpected Policy Effect. Returning false.");
				return false;
			}

		} else {// My Edition
			Log.d(TAG,"ELCE Condition is Running..evaluatePolicy");
			Object evaluationResultInt = constraint.evaluateInt(
					attributeManager, packageName);
			Log.d(TAG, "Running updates for policy: " + this.name);
			int evr = Integer.parseInt(evaluationResultInt.toString());
			boolean b1 = (evr != 0);// To change Integer value into Boolean "True"
			// updates are ALWAYS run. So, keeping them separate here...
			if (effect == PolicyEffect.PERMIT) {
				Log.d(TAG, "....PolicyEffect.PERMIT");
				return b1;
				// return !evaluationResult;
			} else if (effect == PolicyEffect.DENY) {
				Log.d(TAG, "....PolicyEffect.DENY");
				return !b1;
				// return evaluationResult;
			} else {
				Log.d(TAG, "Found unexpected Policy Effect. Returning false.");
				return false;
			}
		}// My Edition End
	}

	/**
	 * Set associated permission
	 * @param permission  the permission to set
	 * @uml.property  name="permission"
	 */
	public void setPermission(String permission) {
		this.permission = permission;
	}

	/**
	 * Get associated permission
	 * @param permission  the permission to set
	 * @return
	 * @uml.property  name="permission"
	 */
	public String getPermission() {
		Log.d(TAG, "getPermission....APEXSAEED....." + permission);
		return this.permission;
	}
}
