package com.founder.e5.scheduler;

import com.founder.e5.commons.Log;
import com.founder.e5.context.Context;
import com.founder.e5.scheduler.db.SysJob;
import com.founder.e5.scheduler.db.SysJobManager;

/**
 * <p>E5ϵͳ�����Զ���������������Ӧ�÷���������ʱ��������Ϊ���Զ���������ϵͳ����</p>
 * <p>�ڼ�Ⱥ�����£�ÿ���ڵ㣨e5Ӧ�ã��Զ������������������еĽڵ㲻��Ҫ�Զ�������������Ҫ��������e5config.xml������</p>
 * <p>e5config.xml���ã�</p>
 * <pre>
	&lt;restart-config&gt;
	    &lt;action invokeClass="com.founder.e5.scheduler.SchedulerLoader" invokeMethod="init"/&gt;
 * </pre>    
 * @author wanghc
 * @created 2009-1-6 ����12:33:23
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
