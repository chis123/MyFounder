package com.founder.e5.scheduler;

import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.quartz.UnableToInterruptJobException;

import com.founder.e5.commons.Log;
import com.founder.e5.context.Context;
import com.founder.e5.context.E5Exception;


/**
 * <p>
 * 基本job类,其它任务需要继承该抽象类，实现execute()方法,
 * 这个类隐藏了quartz的JobExecutionContext,JobExecutionException等特征,这样子类也就无法使用
 * 他们了。这样的目的是使job类能脱离quartz类，记录每个job被调度的时间，是否被打断的功能
 * </p>
 * 
 * <p>
 * 具体的job也可以自己实现quartz的Job接口，然后使用quartz的特性。
 * 用法见quartz的docs
 * </p>
 * 
 * @author wanghc
 */

public abstract class BaseJob implements StatefulJob,InterruptableJob
{
   protected Log log = Context.getLog(Constants.LOG_NAME);
   
	/**
	 * 是否被打断
	 */
   private boolean interrupt = false;
   
    /* (non-Javadoc)
	 * @see org.quartz.InterruptableJob#interrupt()
	 */
	public void interrupt() throws UnableToInterruptJobException
	{
		interrupt = true;		
	}	
	
	/**
	 * job 是否被打断
	 * @return
	 */
	protected boolean isInterrupt()
	{
		return interrupt;
	}

	public void execute(JobExecutionContext context) throws JobExecutionException
	{
		//记录调度日志
		if(log.isInfoEnabled())
			log.info("Job["+context.getJobDetail().getName()+"] is invoked at "+context.getFireTime());
		try
		{
			execute();
		}
		catch(E5Exception e)
		{
			throw new JobExecutionException(e);
		}
	}
	
	/**
	 * job的执行方法
	 * @throws E5Exception
	 */
	protected abstract void execute() throws E5Exception;
}
