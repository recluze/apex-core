package org.csrdu.apex.jpf;

import java.io.PrintWriter;


import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.ListenerAdapter;
import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.jvm.MethodInfo;
import gov.nasa.jpf.jvm.ThreadInfo;
import gov.nasa.jpf.jvm.Transition;


import gov.nasa.jpf.jvm.bytecode.INVOKESPECIAL;

import gov.nasa.jpf.jvm.bytecode.Instruction;
import gov.nasa.jpf.jvm.bytecode.InvokeInstruction;

import gov.nasa.jpf.jvm.bytecode.VirtualInvocation;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.search.heuristic.HeuristicSearch;
import gov.nasa.jpf.util.Printable;

//import org.csrdu.apex.helpers.Log;
import org.csrdu.apex.policy.*;

public class ApexListener extends ListenerAdapter { // make sure the compiled
													// class is in
	ApexPackagePolicy app;
	String TAG = "ApexListener : ";

	// Integer Variables
	int searchLevel = 0;
	int maxSearchLevel = 0;
	int newStates;
	int endStates;
	int backtracks;
	int revisitedStates;
	int processedStates;
	int restoredStates;
	int steps;
	int queueSize = 0;
	int maxHeapCount = 0;
	int depth;
	int logPeriod;

	// Long Variables
	long maxMemory;
	long totalMemory;
	long freeMemory;
	long time;
	long startTime;
	long startFreeMemory;
	long nextLog;

	// Boolean variables
	boolean isHeuristic = false;
	boolean showInsn = false;

	// String variables
	String lastLine;
	String linePrefix;

	MethodInfo lastMi;
	PrintWriter out;

	public ApexListener(Config config, JPF jpf) {
		super();
		showInsn = config.getBoolean("cg.show_insn");
		out = new PrintWriter(System.out, true);
		logPeriod = config.getInt("jpf.stack_tracker.log_period", 5000);
	}
	

	void logStack(ThreadInfo ti) {
		long time = System.currentTimeMillis();

		if (time < nextLog) {
			return;
		}

		nextLog = time + logPeriod;

		System.out.println();
		System.out.print("Thread: ");
		System.out.print(ti.getId());
		System.out.println(":");
		System.out.println(ti.getStackTrace());
		System.out.println();
	}

	public void executeInstruction(JVM vm) {

		Instruction insn = vm.getLastInstruction();
		MethodInfo mi = insn.getMethodInfo();
		ThreadInfo ti = vm.getLastThreadInfo();

		if (mi != lastMi) {
			// logStack(ti);
			lastMi = mi;

		} else if (insn instanceof InvokeInstruction) {
			MethodInfo callee;

			// that's the only little gist of it - if this is a
			// VirtualInvocation,
			// we have to dig the callee out by ourselves (it's not known
			// before execution)

			if (insn instanceof VirtualInvocation) {
				VirtualInvocation callInsn = (VirtualInvocation) insn;
				int objref = callInsn.getCalleeThis(ti);
				callee = callInsn.getInvokedMethod(ti, objref);
				// System.out.println("count = " + sa++ + "...IF callee.."
				// + callee);
				System.out.println("ApexListener 1 : "+callee);
		
			} else if (insn instanceof INVOKESPECIAL) {
				INVOKESPECIAL callInsn = (INVOKESPECIAL) insn;
				callee = callInsn.getInvokedMethod(ti);
				// System.out.println("count = " + sa++ + "...ELSE IF callee.."
				// + callee);
				System.out.println("ApexListener 2 : "+callee);

			} else {
				InvokeInstruction callInsn = (InvokeInstruction) insn;
				callee = callInsn.getInvokedMethod(ti);
				// System.out.println("count = " + sa++ + "...ELSE callee.."
				// + callee);
				System.out.println("ApexListener 3 : "+callee);
			}

			if (callee != null) {
				if (callee.isMJI()) {
					logStack(ti);
					System.out.println("ApexListener 4 : " + callee);
				}
			} else {
				out.println("ERROR: unknown callee of: " + insn);
			}
		}
	}

	public void stateAdvanced(Search search) {
		int id = search.getStateId();
		depth++;
		//newStates++;
		Instruction ins = search.getVM().getLastInstruction();
		MethodInfo mi = search.getVM().getLastMethodInfo();
		ThreadInfo ti = search.getVM().getLastThreadInfo();
		ThreadInfo ti1 = search.getVM().getCurrentThread();
		//InvokeInstruction in = (InvokeInstruction) ins;
		//mi = in.getInvokedMethod(ti);
		System.out.print("----------------------------------- [" + id
				+ "] forward: " + search.getDepth());
		System.out.println("Instruction is : "+ins);
		System.out.println("Method Info is : "+mi);
		System.out.println("Last Thread Info is : "+ti+ "Current Thread Info is : "+ti1);
		
		if (search.isNewState()) {
			newStates++;
			System.out.print(" new");
		} else {
			System.out.print(" visited");
		}

		if (search.isEndState()) {
			endStates++;
			System.out.println(" end");
		}

		System.out.println();

		lastLine = null; // in case we report by source line
		lastMi = null;
		linePrefix = null;
		steps += search.getTransition().getStepCount();
		Transition tra = search.getTransition();
		boolean bol = search.transitionOccurred();
		if(!bol)
		{
		System.out.println("Transistion : " + tra + " Steps : " + steps);
		}
		else 
			System.out.println("Nothings");
		
	}

	public void stateProcessed(Search search) {
		processedStates++; // To count State processed...
		int id = search.getStateId();
		System.out.println("----------------------------------- ["
				+ search.getDepth() + "] done: " + id);
	}

	public void stateBacktracked(Search search) {
		int id = search.getStateId();
		searchLevel = search.getDepth(); // to count the depth of states.
		backtracks++;
		depth--;
		lastLine = null;
		lastMi = null;
		System.out.println("----------------------------------- ["
				+ search.getDepth() + "] backtrack: " + id);
	}

	public void searchFinished(Search search) {
		System.out
				.println("----------------------------------- search finished");
		report("APEX JPF REPORT");
		
	}

	public void stateRestored(Search search) {
		depth = search.getDepth();
		searchLevel = search.getDepth();
		restoredStates++;
	}

	public void searchStarted(Search search) {
		if (search instanceof HeuristicSearch) {
			isHeuristic = true;
		}

		startTime = System.currentTimeMillis();

		Runtime rt = Runtime.getRuntime();
		startFreeMemory = rt.freeMemory();
		totalMemory = rt.totalMemory();
		maxMemory = rt.maxMemory();
	}

	public void methodExited(JVM vm) {
		// TODO Auto-generated method stub
		super.methodExited(vm);
	}

	public interface Property<VM> extends Printable {
		boolean check(Search search, VM vm);

		String getErrorMessage();
	}

	void reportRuntime() {
		long td = time - startTime;

		int h = (int) (td / 3600000);
		int m = (int) (td / 60000) % 60;
		int s = (int) (td / 1000) % 60;

		System.out.print("  Execution time:          ");
		if (h < 10)
			System.out.print("0");
		System.out.print(h);
		System.out.print(':');
		if (m < 10)
			System.out.print("0");
		System.out.print(m);
		System.out.print(':');
		if (s < 10)
			System.out.print("0");
		System.out.print(s);

		System.out.print("  (");
		System.out.print(td);
		System.out.println(" ms)");
	}

	void report(String header) {
		time = System.currentTimeMillis();

		System.out.println("==========================="+header+"===========================");

		reportRuntime();

		System.out.println();
		System.out.print("  search depth:      ");
		System.out.print(searchLevel);
		System.out.print(" (max: ");
		System.out.print(maxSearchLevel);
		System.out.println(")");

		System.out.print("  new states:        ");
		System.out.println(newStates);

		System.out.print("  revisited states:  ");
		System.out.println(revisitedStates);

		System.out.print("  end states:        ");
		System.out.println(endStates);

		System.out.print("  backtracks:        ");
		System.out.println(backtracks);

		System.out.print("  processed states:  ");
		System.out.print(processedStates);
		System.out.print(" (");
		// a little ad-hoc rounding
		double d = (double) backtracks / (double) processedStates;
		int n = (int) d;
		int m = (int) ((d - /* (double) */n) * 10.0);
		System.out.print(n);
		System.out.print('.');
		System.out.print(m);
		System.out.println(" bt/proc state)");

		System.out.print("  restored states:   ");
		System.out.println(restoredStates);

		if (isHeuristic) {
			System.out.print("  queue size:        ");
			System.out.println(queueSize);
		}

		System.out.println();
		System.out.print("  total memory [kB]: ");
		System.out.print(totalMemory / 1024);
		System.out.print(" (max: ");
		System.out.print(maxMemory / 1024);
		System.out.println(")");

		System.out.print("  free memory [kB]:  ");
		System.out.println(freeMemory / 1024);

		System.out.print("  max heap objects:  ");
		System.out.print(maxHeapCount);

		System.out.println();
	}
}
// Create a deadlock... how, write a shared attribute in policy, that shared
// attribute will be access by two threads
// at the same time to update or write in the shared attribute value.
// 