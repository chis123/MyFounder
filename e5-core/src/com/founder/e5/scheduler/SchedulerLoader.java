package com.founder.e5.scheduler;

import com.founder.e5.commons.Log;
import com.founder.e5.context.Context;
import com.founder.e5.scheduler.db.SysJob;
import com.founder.e5.scheduler.db.SysJobManager;

/**
 * <p>E5系统任务自动启动程序，用于在应用服务器启动时启动设置为“自动启动”的系统任务</p>
 * <p>在集群环境下，每个节点（e5应用）自动启动本身的任务，如果有的节点不需要自动启动任务，则不需要设置下面e5config.xml的配置</p>
 * <p>e5config.xml配置：</p>
 * <pre>
	&lt;restart-config&gt;
	    &lt;action invokeClass="com.founder.e5.scheduler.SchedulerLoader" invokeMethod="init"/&gt;
 * </pre>    
 * @author wanghc
 * @created 2009-1-6 下午12:33:23
 */
public class SchedulerLoader
{
	private static Log log = Context.getLog(Constants.LOG_NAME);
	
	public static void init()
	{
		//AutoStart SysJobs
		SchedulerServer ss = (SchedulerServer)Context.getBean("SchedulerLocalServer");
		SysJobManager sysJobManager = (SysJobManager)Context.getBean("SysJobManager");
		SysJob[] sysJobs = sysJobManager.getJobs();
		int count = 0;		
		for(int i=0;sysJobs!=null && i<sysJobs.length;i++)
		{
			if(sysJobs[i].isAutoStart())
			{
				ss.scheduler(sysJobs[i].getJobID());
				if(log.isDebugEnabled())
					log.debug("[Scheduler]JobName="+sysJobs[i].getName()+" started.");
				count++;
			}
		}
		log.info("[Scheduler]System Jobs["+count+"] started.");
	}
}
