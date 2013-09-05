package org.csrdu.apex.jpf;

import gov.nasa.jpf.jvm.ElementInfo;
import gov.nasa.jpf.jvm.ThreadInfo;
import gov.nasa.jpf.jvm.bytecode.Instruction;

import java.io.PrintWriter;

//import org.csrdu.apex.jpf.PreciseRaceDetector.Race;

class PrintApexSharedMemory {
    PrintApexSharedMemory previouse;   // linked list

    ThreadInfo ThreadA;
    ThreadInfo ThreadB;
    ThreadInfo ThreadC;
    
    Instruction instructionA;
    Instruction instructionB;
    Instruction instructionC;
    ElementInfo elementInformation;

    boolean isRace() {
      return instructionB != null;
    }

    void printOn(PrintWriter pw){
      pw.print("  ");
      pw.print( ThreadA.getName());
      pw.print(" at ");
      pw.println(instructionA.getSourceLocation());
      String line = instructionA.getSourceLine();
      if (line != null){
        pw.print("\t\t\"" + line.trim());
      }
      pw.print("\"  : ");
      pw.println(instructionA);

      if (instructionB != null){
        pw.print("  ");
        pw.print(ThreadB.getName());
        pw.print(" at ");
        pw.println(instructionB.getSourceLocation());
        line = instructionB.getSourceLine();
        if (line != null){
          pw.print("\t\t\"" + line.trim());
        }
        pw.print("\"  : ");
        pw.println(instructionB);
      }
      if (instructionC != null){
          pw.print("  ");
          pw.print(ThreadC.getName());
          pw.print(" at ");
          pw.println(instructionC.getSourceLocation());
          line = instructionC.getSourceLine();
          if (line != null){
            pw.print("\t\t\"" + line.trim());
          }
          pw.print("\"  : ");
          pw.println(instructionC);
        }
    }
  }