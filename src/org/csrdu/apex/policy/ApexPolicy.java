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
	public String TAG = "APEX:ApexPolicy";

	static enum PolicyEffect {
		PERMIT, DENY
	}

	private String name;
	private PolicyEffect effect;

	private String permission = "";
	private Constraint constraint;
	private Vector<Update> updates;

	/**
	 * Get the constraint for this policy
	 * 
	 * @return The constraint
	 */
	public Constraint getConstraint() {
		return constraint;
	}

	/**
	 * Set the constraint for this policy
	 * 
	 * @param constraint
	 *            Constraint to set
	 */
	public void setConstraint(Constraint constraint) {
		this.constraint = constraint;
	}

	public Vector<Update> getUpdates() {
		return updates;
	}

	public void setUpdates(Vector<Update> updates) {
		this.updates = updates;
	}

	/**
	 * Set the Effect of the policy
	 * 
	 * @param effect
	 *            The effect. May be PERMIT or DENY
	 * 
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
	public boolean evaluatePolicy(AttributeManager attributeManager, String packageName) {
		Log.d(TAG, "Evaluating policy: " + this.name);
		// evaluate all constraints
		boolean evaluationResult = constraint.evaluate(attributeManager, packageName);

		Log.d(TAG, "Running updates for policy: " + this.name);
		// updates are ALWAYS run. So, keeping them separate here...

		if (effect == PolicyEffect.PERMIT) {
			return evaluationResult;
		} else if (effect == PolicyEffect.DENY) {
			return !evaluationResult;
		} else {
			Log.d(TAG, "Found unexpected Policy Effect. Returning false.");
			return false;
		}
	}

	/**
	 * Set associated permission
	 * 
	 * @param permission
	 *            the permission to set
	 */
	public void setPermission(String permission) {
		this.permission = permission;
	}

	/**
	 * Get associated permission
	 * 
	 * @param permission
	 *            the permission to set
	 * @return
	 */
	public String getPermission() {
		return this.permission;
	}
}
