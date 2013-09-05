package org.csrdu.apex.jpf;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.jvm.ChoiceGenerator;
import gov.nasa.jpf.jvm.ElementInfo;
import gov.nasa.jpf.jvm.FieldInfo;
import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.jvm.MethodInfo;
import gov.nasa.jpf.jvm.ThreadInfo;
import gov.nasa.jpf.jvm.bytecode.ArrayInstruction;
import gov.nasa.jpf.jvm.bytecode.FieldInstruction;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import gov.nasa.jpf.jvm.choice.ThreadChoiceFromSet;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.util.StringSetMatcher;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.csrdu.apex.helpers.Log;

/*
 * Author: Saeed Iqbal
 */

public class AccessSharedMemory extends PropertyListenerAdapter {
	public static String TAG = "AccessSharedMemory";
	
  PrintApexSharedMemory PAS;


 
  StringSetMatcher includes = null; 
  StringSetMatcher excludes = null; 


  public AccessSharedMemory (Config conf) {
    includes = StringSetMatcher.getNonEmpty(conf.getStringArray("PAS.include"));
    excludes = StringSetMatcher.getNonEmpty(conf.getStringArray("PAS.exclude"));
  }
  
  public boolean check(Search search, JVM vm) {
    return (PAS == null);
  }

  public void reset() {
    PAS = null;
  }


  public String getErrorMessage () {
    if (PAS != null){
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      PAS.printOn(pw);
      pw.flush();
      return sw.toString();
    } else {
      return null;
    }
  }

  boolean checkRace (ThreadInfo[] threads){
    PrintApexSharedMemory candidate = null;

    for (int i = 0; i < threads.length; i++) {
      ThreadInfo apexThreadInfo = threads[i];
      Instruction insn = apexThreadInfo.getPC();
      MethodInfo mi = insn.getMethodInfo();
      String line = insn.getSourceLine(); if (line != null) { Log.d(TAG,
			  "Source Line is : " + line.trim()); }
      if (StringSetMatcher.isMatch(mi.getBaseName(), includes, excludes)) {
    	  Log.d(TAG,"String Matcher is ....."+(StringSetMatcher.isMatch(mi.getBaseName(), excludes, excludes)));
    	  
        if (insn instanceof FieldInstruction) {
        	Log.d(TAG,"insn instanceof FieldInstruction ....."+(insn instanceof FieldInstruction));
        	// To check the method of class type
          FieldInstruction apexFieldInformation = (FieldInstruction) insn;
			// Which variables and methods are called... ending
			// variables are methods
          FieldInfo ApexfieldInformation = apexFieldInformation.getFieldInfo();
       // which Methods are called
          ElementInfo elementInformation = apexFieldInformation.peekElementInfo(apexThreadInfo);
          Log.d(TAG, "FieldInstruction..." + apexFieldInformation);
			Log.d(TAG, "FieldInfo..." + ApexfieldInformation);
			Log.d(TAG, "ElementInfo..." + elementInformation);
          candidate = CheckApexThreadsInfo.check(candidate, apexThreadInfo, apexFieldInformation, elementInformation, ApexfieldInformation);

        } else if (insn instanceof ArrayInstruction) {
          ArrayInstruction ainsn = (ArrayInstruction) insn;
          int aref = ainsn.getArrayRef(apexThreadInfo);
          int idx = ainsn.getIndex(apexThreadInfo);
          ElementInfo elementInformation = apexThreadInfo.getElementInfo(aref);

          candidate = CheckApexInfor.check(candidate, apexThreadInfo, ainsn, elementInformation, idx);
        }
      }
    //Log.d(TAG, "SAEED IQBAL....."+candidate);
      if (candidate != null && candidate.isRace()){
        PAS = candidate;
        return true;
      }
    }

    return false;
  }
  public void choiceGeneratorSet(JVM vm) {
    ChoiceGenerator<?> apexChoiceGen = vm.getLastChoiceGenerator();

    if (apexChoiceGen instanceof ThreadChoiceFromSet) {
      ThreadInfo[] threads = ((ThreadChoiceFromSet)apexChoiceGen).getAllThreadChoices();
      checkRace(threads);
    }
  }

  public void executeInstruction (JVM jvm) {
    if (PAS != null) {
      // we're done, report as quickly as possible
      ThreadInfo apexThreadInfo = jvm.getLastThreadInfo();
      //apexThreadInfo.skipInstruction();
      apexThreadInfo.breakTransition();
    }
  }

}