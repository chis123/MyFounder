package com.founder.e5.scheduler;

import java.text.ParseException;
import java.util.List;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.founder.e5.commons.Log;
import com.founder.e5.context.Context;
import com.founder.e5.scheduler.db.SysJob;
import com.founder.e5.scheduler.db.SysJobManager;
import com.founder.e5.scheduler.db.SysTrigger;
import com.founder.e5.scheduler.db.SysTriggerManager;

/**
 * <p>本地任务调度管理的实现类</p>
 * <p>
 * 通过quartz管理任务调度程序，这个类通过Spring框架实现singleton模式。
 * 系统默认启动5个任务工作线程，通过设置classpath下的quartz.properties来进行设置。
 * 具体的设置方法，请参考quartz 的 docs.
 * 
 * http://www.opensymphony.com/quartz
 * 
 * </p>
 * 
 * @see SchedulerServer
 * 
 * @author wanghc
 * @created 2006-6-20
 */

public class SchedulerLocalServer implements SchedulerServer
{
	/* 本地任务调度器 */
	private Scheduler scheduler = null;
	
	/**
	 * 任务创建工厂
	 */
	private JobFactory jobFactory = null;
	
	private Log log = Context.getLog(Constants.LOG_NAME);

	
	/**
	 * 构造函数初始化调度器
	 * 
	 * @roseuid 44966D0A0399
	 */
	public SchedulerLocalServer()
	{
		log.info("Initializing SchedulerLocalServer.");
		
		try
		{
			jobFactory = new JobFactory();
			SchedulerFactory factory = new StdSchedulerFactory();
			scheduler = factory.getScheduler();
			scheduler.setJobFactory(jobFactory);
			scheduler.start();
			log.info("Initialize schedulerLocalServer successed.");
		}
		catch(Exception e)
		{
			log.error("initialize error:"+e.getMessage(),e);
		}		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.founder.e5.scheduler.SchedulerServer#getCurrentlyExectingJobs()
	 */
	public String[] getCurrentlyExectingJobs()
	{		
		
		String[] jobNames = null;
		try
		{
			List list = scheduler.getCurrentlyExecutingJobs();
			
			jobNames = new String[list.size()];
			for(int i=0;i<list.size();i++)
			{
				JobExecutionContext context = (JobExecutionContext)list.get(i);
				jobNames[i] = context.getJobDetail().getName();
			}
		}
		catch(Exception e)
		{
			log.error("getCurrentlyExectingJobs error:"+e.getMessage(),e);
		}
		
		return jobNames;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.founder.e5.scheduler.SchedulerServer#getJobs()
	 */
	public String[] getJobs()
	{		
		try
		{
	        String[] names = scheduler.getJobNames(null);
	        return names;
		}		
		catch(Exception e)
		{
			log.error("get jobs error:"+e.getMessage(),e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.founder.e5.scheduler.SchedulerServer#scheduler(int)
	 */
	public boolean scheduler(int jobID)
	{
		
		//从db中取得job定义
		SysJobManager jobManager = (SysJobManager)Context.getBean("SysJobManager");
		SysJob sysJob = jobManager.getJob(jobID);
		//调度job
		return scheduler(sysJob);		
	}
	
	/**
	 * 调度任务
	 * @param sysJob
	 * @return
	 */
	private boolean scheduler(SysJob sysJob)
	{
		if(log.isInfoEnabled())
			log.info("start Server["+Constants.getServerName()+"] job["+sysJob.getJobID()+"]");
		
		try
		{
			//1.检查job是否已经启动
			JobDetail already = scheduler.getJobDetail(sysJob.getJobDetailName(),null);
			if(already != null) return false;
			
			//2.查询触发器调度程序
			int jobID = sysJob.getJobID();
			SysTriggerManager triggerManager = (SysTriggerManager)Context.getBean("SysTriggerManager");
			SysTrigger trigger = triggerManager.getTriggersByServer(jobID,Constants.getServerName());
			
			if(trigger == null)
			{
				log.error("Job ["+sysJob.getName()+"] of trigger is not exist");
				return false;
			}		
			
			//@todo 是否需要job带一些参数
			//jobDetail.setJobDataMap()	
			
			JobDetail jobDetail = new JobDetail(sysJob.getJobDetailName(),null,
					getClass(sysJob.getJobClass()));
			jobDetail.setDescription(sysJob.getDescription());
			
			CronTrigger cronTrigger = new CronTrigger(sysJob.getJobDetailName(),
					null,trigger.getCronExpression());
			scheduler.scheduleJob(jobDetail,cronTrigger);
		} 
		catch (ParseException e)
		{
			log.error("Scheduler job parse cronExpression error:"+e.getMessage());
			return false;
		}
		catch(Exception e)
		{
			log.error("Scheduler job error:"+e.getMessage());
			return false;
		}
		
		return true;
	}
	
	private Class getClass(String className)
	{
		Class clazz = null;
		try
		{
			clazz = Class.forName(className);
		} 
		catch (ClassNotFoundException e)
		{
			log.error("ClassNotFoundException:"+className);
		}
		return clazz;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.founder.e5.scheduler.SchedulerServer#schedulerAll()
	 */
	public int schedulerAll()
	{
		
		SysJobManager jobManager = (SysJobManager)Context.getBean("SysJobManager");
		//SysJob[] jobs = jobManager.getJobs();
		SysJob[] jobs = jobManager.getAvailableJobs();//jobManager.getJobsByActive("Y");
		int successed = 0;
		for (int i = 0; jobs != null && i < jobs.length; i++)
		{
			if (scheduler(jobs[i]))
				successed++;
		}
		return successed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.founder.e5.scheduler.SchedulerServer#shutdown()
	 */
	public void shutdown()
	{		
		if(log.isInfoEnabled())
			log.info("shutdown scheduler["+Constants.getServerName()+"]");
		
		try
		{
			scheduler.shutdown();
		}
		catch(Exception e)
		{
			log.error("SchedulerServer shutdown error:"+e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.founder.e5.scheduler.SchedulerServer#stop(int)
	 */
	public boolean stop(int jobID)
	{
		
		SysJobManager jobManager = (SysJobManager)Context.getBean("SysJobManager");
		SysJob sysJob = jobManager.getJob(jobID);		
		return stop(sysJob);
	}
	
	private boolean stop(SysJob sysJob)
	{		
		try
		{			
			//1.通知job终止,如果job不存在返回false
			scheduler.interrupt(sysJob.getJobDetailName(),null);
			
			//2.在任务调度中删除job
			boolean success = scheduler.deleteJob(sysJob.getJobDetailName(),null);
			
			if(success)
			{
				if(log.isInfoEnabled())
					log.info("stop server["+Constants.getServerName()+"] job["+sysJob.getJobID()+"]");

				//3.从工厂中删除job的实例
				jobFactory.removeJob(sysJob.getJobDetailName());
			}
		}
		catch(Exception e)
		{
			log.error("stop job error:"+e.getMessage());
			return false;
		}		
		return true;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.founder.e5.scheduler.SchedulerServer#stopAll()
	 */
	public int stopAll()
	{
		
		SysJobManager jobManager = (SysJobManager)Context.getBean("SysJobManager");
		SysJob[] jobs = jobManager.getJobs();
		
		int successed = 0;
		for(int i=0;jobs!=null && i<jobs.length;i++)
		{
			if(stop(jobs[i]))
				successed++;
		}			
		return successed;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() throws Throwable
	{	
		/*被回收时释放系统资源*/
		if(scheduler!=null)
		{			
			scheduler.shutdown();
			scheduler = null;
		}
	}

}
