package com.founder.e5.scheduler;

/**
 * 任务调度adapter工厂
 * 因为不能在spring中初始化(需要用到ConfigReader),所以在使用时初始化这个实例
 * @author wanghc
 *
 */
public class SchedulerServerAdapterFactory
{
	private static SchedulerServerAdapter adapter = null;
	
	static public synchronized SchedulerServerAdapter getSchedulerServerAdapter()
	{
		if(adapter==null)
		{
			adapter = new SchedulerServerAdapterImpl();
		}
		return adapter;
	}
}
