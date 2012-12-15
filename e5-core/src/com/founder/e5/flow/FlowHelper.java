package com.founder.e5.flow;

import com.founder.e5.context.CacheReader;
import com.founder.e5.context.Context;

/**
 * @created on 2005-8-10
 * @author Gong Lijie
 * @version 1.0
 */
class FlowHelper
{
	static ProcOrderManager getProcOrderManager()
	{
		ProcOrderManager manager = (ProcOrderManager)Context.getBean(ProcOrderManager.class);
		if (manager == null)
			manager = Factory.buildProcOrderManager();
		return manager;
	}
	static ProcManager getProcManager()
	{
		ProcManager manager = (ProcManager)Context.getBean(ProcManager.class);
		if (manager == null)
			manager = Factory.buildProcManager();
		return manager;
	}
	static ProcGroupManager getProcGroupManager()
	{
		ProcGroupManager manager = (ProcGroupManager)Context.getBean(ProcGroupManager.class);
		if (manager == null)
			manager = Factory.buildProcGroupManager();
		return manager;
	}

	static FlowManager getFlowManager()
	{
		FlowManager manager = (FlowManager)Context.getBean(FlowManager.class);
		if (manager == null)
			manager = Factory.buildFlowManager();
		return manager;
	}
	
	static FlowReader getFlowReader()
	{
		FlowReader reader = (FlowReader)Context.getBean(FlowReader.class);
		if (reader == null)
			reader = Factory.buildFlowReader();
		return reader;
	}
	static FlowCache getFlowCache()
	{
		return (FlowCache)CacheReader.find(FlowCache.class);
	}
}
