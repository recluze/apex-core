/* 
 Part of the Apex Framework. 	


 Provided under GPLv2. 

 author: Nauman (recluze@gmail.com) 
 http://csrdu.org/nauman 

 */
package org.csrdu.apex.functions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import org.csrdu.apex.helpers.*;
import org.csrdu.apex.interfaces.IApexFunction;

/**
 * @author Nauman
 * 
 */
public class DateEqual implements IApexFunction {
	/**
	 * @uml.property  name="tAG"
	 */
	public String TAG = "APEX:DateEqual";
	/**
	 * @uml.property  name="up"
	 * @uml.associationEnd  readOnly="true"
	 */
	UpdatePolicy up;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.csrdu.apex.interfaces.IApexFunction#evaluate(java.util.Vector)
	 */

	/*
	 * public DateEqual() { up.removeLineFromFile(
	 * "C:/Users/Saeed Iqbal/workspace/apex-jpf/policies/attribs/com.android.mms"
	 * , "remSms=3"); }
	 */
	@Override
	public Object evaluate(Vector<Object> params) {
		// Log.d(TAG, "DATE Value is..." + params);

		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
		Date TodayDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		Date yesDate = calendar.getTime();
		// int DifferenceDate = (int) ((TodayDate.getTime() - yesDate.getTime())
		// / (1000 * 60 * 60 * 24));

		String todayAsString = formatter.format(TodayDate);
		String yesterdayAsString = formatter.format(yesDate);

		Object sentSms = (Object) params.get(0); // Total Sent SMS
		Object LastUsedDate = (Object) params.get(1); // Last Used Date..
		Object remSms = (Object) params.get(2);// Remaining SMS
		//Object TotalSms = (Object) params.get(3); // Total Qouta of Sending SMS
		Object CurrentDate = (Object) params.get(4); // Current Date...

		int intObj = Integer.parseInt((String) remSms);
		int sentSMs = Integer.parseInt((String) sentSms);
//		UpdatePolicy
	//			.deleteFile("C:/Users/Saeed Iqbal/workspace/apex-jpf/policies/attribs/com.android.mms");
		String content = "sentSms=" + ++sentSMs + "";
		String content1 = "lastUsedDay=" + yesterdayAsString + "";
		String content2 = "remSms=" + --intObj + "";
		String content3 = "totalSms=5";
		String content4 = "CurrentDate=" + todayAsString + "";
		Log.d(TAG, "====================================================");
		if (sentSMs > 5 || intObj < 0) {
			Log.d(TAG, "WARNING!!!! You can't send any SMS, Becuase the Qouta is overed");
			return false;
		} else if ((sentSMs <= 5) && (sentSMs != intObj) && !(CurrentDate.equals(LastUsedDate))) {
			UpdatePolicy
			.deleteFile("C:/Users/Saeed Iqbal/workspace/apex-jpf/policies/attribs/com.android.mms");
			UpdatePolicy.writeToFile(content);
			UpdatePolicy.writeToFile(content1);
			UpdatePolicy.writeToFile(content2);
			UpdatePolicy.writeToFile(content3);
			UpdatePolicy.writeToFile(content4);

			Log.d(TAG, "You can Send SMS");
			return true;
		} else if (intObj >= 5) {
			Log.d(TAG, "you CAN'T send SMS");
			return false;
		}
		Log.d(TAG, "DATE Value is..." + params);

		return false;

	}

}
