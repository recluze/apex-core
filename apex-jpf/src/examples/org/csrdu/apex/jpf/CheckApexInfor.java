package org.csrdu.apex.jpf;

import gov.nasa.jpf.jvm.ElementInfo;
import gov.nasa.jpf.jvm.ThreadInfo;
import gov.nasa.jpf.jvm.bytecode.ArrayInstruction;
import gov.nasa.jpf.jvm.bytecode.Instruction;

import java.io.PrintWriter;

class CheckApexInfor extends PrintApexSharedMemory {
    int index;

    static PrintApexSharedMemory check (PrintApexSharedMemory previouse, ThreadInfo ti, Instruction apexinsn, ElementInfo elementInformation, int index){
      for (PrintApexSharedMemory r = previouse; r != null; r = r.previouse){
        if (r instanceof CheckApexInfor){
          CheckApexInfor ar = (CheckApexInfor)r;
          if (ar.elementInformation == elementInformation && ar.index == index){
            if (!((ArrayInstruction)ar.instructionA).isRead() || !((ArrayInstruction)apexinsn).isRead()){
              ar.ThreadB = ti;
              ar.instructionB = apexinsn;
              return ar;
            }
          }
        }
      }

      CheckApexInfor ar = new CheckApexInfor();
      ar.elementInformation = elementInformation;
      ar.ThreadA = ti;
      ar.instructionA = apexinsn;
      ar.index = index;
      ar.previouse = previouse;
      return ar;
    }

    void printOn(PrintWriter pw){
      pw.print("Race Condition Detect in APEX Framework ");
      pw.print(elementInformation);
      pw.print('[');
      pw.print(index);
      pw.println(']');

      super.printOn(pw);
    }
  }