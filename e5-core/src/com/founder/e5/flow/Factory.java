package com.founder.e5.flow;

/**
 * @created 04-8-2005 14:16:05
 * @author Gong Lijie
 * @version 1.0
 */
public class Factory {
	private static FlowManager flowManager = null;
	private static FlowReader flowReader = null;
	private static ProcManager procManager = null;
	private static ProcReader procReader = null;
	private static ProcOrderManager procOrderManager = null;
	private static ProcOrderReader procOrderReader = null;
	private static ProcGroupManager procGroupManager = null;
	private static ProcGroupReader procGroupReader = null;
	
	public static FlowManager buildFlowManager(){
		if (flowManager != null)
			return flowManager;
		flowManager = new FlowManagerImpl();
		return flowManager;
	}

	public static FlowReader buildFlowReader(){
		if (flowReader != null)
			return flowReader;
		flowReader = new FlowReaderImpl();
		return flowReader;
	}
	
	public static ProcManager buildProcManager(){
		if (procManager != null)
			return procManager;
		procManager = new ProcManagerImpl();
		return procManager;
	}
	
	public static ProcReader buildProcReader(){
		if (procReader != null)
			return procReader;
		procReader = new ProcReaderImpl();
		return procReader;
	}	

	public static ProcOrderManager buildProcOrderManager(){
		if (procOrderManager != null)
			return procOrderManager;
		procOrderManager = new ProcOrderManagerImpl();
		return procOrderManager;
	}
	
	public static ProcOrderReader buildProcOrderReader(){
		if (procOrderReader != null)
			return procOrderReader;
		procOrderReader = new ProcOrderReaderImpl();
		return procOrderReader;
	}	
	public static ProcGroupManager buildProcGroupManager(){
		if (procGroupManager != null)
			return procGroupManager;
		procGroupManager = new ProcGroupManagerImpl();
		return procGroupManager;
	}
	
	public static ProcGroupReader buildProcGroupReader(){
		if (procGroupReader != null)
			return procGroupReader;
		procGroupReader = new ProcGroupReaderImpl();
		return procGroupReader;
	}	
}