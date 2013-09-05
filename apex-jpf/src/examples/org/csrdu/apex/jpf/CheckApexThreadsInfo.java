package org.csrdu.apex.jpf;

import gov.nasa.jpf.jvm.ElementInfo;
import gov.nasa.jpf.jvm.FieldInfo;
import gov.nasa.jpf.jvm.ThreadInfo;
import gov.nasa.jpf.jvm.bytecode.FieldInstruction;
import gov.nasa.jpf.jvm.bytecode.Instruction;

import java.io.PrintWriter;

import org.csrdu.apex.helpers.Log;

class CheckApexThreadsInfo extends PrintApexSharedMemory {
	public static String TAG = "ApexFieldRace";
	FieldInfo fieldInformation;

	static PrintApexSharedMemory check(PrintApexSharedMemory previouse,
			ThreadInfo ti, Instruction apexinsn,
			ElementInfo elementInformation, FieldInfo fieldInformation) {
		String line = apexinsn.getSourceLine();
		if (line != null) {
			Log.d(TAG, "Source Line is : " + line.trim());
		}
		Log.d(TAG, "Class ApexFieldRace.... Before Loop: " + previouse);

		for (PrintApexSharedMemory r = previouse; r != null; r = r.previouse) {
			Log.d(TAG, "Class ApexFieldRace.... Inner Loop: " + previouse);
			Log.d(TAG, "Class ApexFieldRace.... Befor If: "
					+ (r instanceof CheckApexThreadsInfo));
			if (r instanceof CheckApexThreadsInfo) {
				CheckApexThreadsInfo fr = (CheckApexThreadsInfo) r;
				// To check whether the same methods and same methods of
				// variable accessing the same location or not?
				if (fr.elementInformation == elementInformation
						&& fr.fieldInformation == fieldInformation) {
					if (!((FieldInstruction) fr.instructionA).isRead()
							|| ((FieldInstruction) apexinsn).isRead()) { // recly: removed ! before ins
						Log.d(TAG,
								"First thread accessing: "
										+ ((FieldInstruction) fr.instructionA)
												.isRead());
						Log.d(TAG, "Second thread acessing: "
								+ ((FieldInstruction) apexinsn).isRead());
						Log.d(TAG, "Source Line is : " + line.trim());
						fr.ThreadB = ti;
						fr.instructionB = apexinsn;
						return fr;
					}
				}
			}
		}
		/*
		 * To send these instructions threads and element information to catch
		 * APEX THREAD RACE.
		 */
		CheckApexThreadsInfo fr = new CheckApexThreadsInfo();
		fr.elementInformation = elementInformation;
		fr.ThreadA = ti;
		fr.instructionA = apexinsn;
		fr.fieldInformation = fieldInformation;
		fr.previouse = previouse;
		return fr;
	}

	void printOn(PrintWriter pw) {

		System.out.println("");
		System.out
				.println("===========================Race Condition Detect in APEX===========================");
		System.out.println("Race Condition Detect in APEX Framework : ");

		System.out.println("First Thread Info : " + ThreadA);
		System.out.println("\t  Bytecode Intruction Operation: " + instructionA);
		System.out.println("");
		System.out.println("Second Thread Info : " + ThreadB);
		System.out.println("\t  Bytecode Intruction Operation : " + instructionB);

		/*
		 * System.out.println("");
		 * System.out.println("Third Thread Info : "+Thread_C);
		 * System.out.println("\t  Intruction Operation : "+instruction_C);
		 */

		pw.print(elementInformation);
		// pw.print('.');
		System.out.println('.');
		System.out.println("Shared Varibles : " + fieldInformation.getName());
		// pw.println(fieldInformation.getName());

		super.printOn(pw);
	}
}