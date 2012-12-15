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
 * <p>����������ȹ����ʵ����</p>
 * <p>
 * ͨ��quartz����������ȳ��������ͨ��Spring���ʵ��singletonģʽ��
 * ϵͳĬ������5���������̣߳�ͨ������classpath�µ�quartz.properties���������á�
 * ��������÷�������ο�quartz �� docs.
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
	/* ������������� */
	private Scheduler scheduler = null;
	
	/**
	 * ���񴴽�����
	 */
	private JobFactory jobFactory = null;
	
	private Log log = Context.getLog(Constants.LOG_NAME);

	
	/**
	 * ���캯����ʼ��������
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
		
		//��db��ȡ��job����
		SysJobManager jobManager = (SysJobManager)Context.getBean("SysJobManager");
		SysJob sysJob = jobManager.getJob(jobID);
		//����job
		return scheduler(sysJob);		
	}
	
	/**
	 * ��������
	 * @param sysJob
	 * @return
	 */
	private boolean scheduler(SysJob sysJob)
	{
		if(log.isInfoEnabled())
			log.info("start Server["+Constants.getServerName()+"] job["+sysJob.getJobID()+"]");
		
		try
		{
			//1.���job�Ƿ��Ѿ�����
			JobDetail already = scheduler.getJobDetail(sysJob.getJobDetailName(),null);
			if(already != null) return false;
			
			//2.��ѯ���������ȳ���
			int jobID = sysJob.getJobID();
			SysTriggerManager triggerManager = (SysTriggerManager)Context.getBean("SysTriggerManager");
			SysTrigger trigger = triggerManager.getTriggersByServer(jobID,Constants.getServerName());
			
			if(trigger == null)
			{
				log.error("Job ["+sysJob.getName()+"] of trigger is not exist");
				return false;
			}		
			
			//@todo �Ƿ���Ҫjob��һЩ����
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
			//1.֪ͨjob��ֹ,���job�����ڷ���false
			scheduler.interrupt(sysJob.getJobDetailName(),null);
			
			//2.�����������ɾ��job
			boolean success = scheduler.deleteJob(sysJob.getJobDetailName(),null);
			
			if(success)
			{
				if(log.isInfoEnabled())
					log.info("stop server["+Constants.getServerName()+"] job["+sysJob.getJobID()+"]");

				//3.�ӹ�����ɾ��job��ʵ��
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
		/*������ʱ�ͷ�ϵͳ��Դ*/
		if(scheduler!=null)
		{			
			scheduler.shutdown();
			scheduler = null;
		}
	}

}
