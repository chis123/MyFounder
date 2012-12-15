package com.founder.e5.scheduler;

import java.util.HashMap;

import org.quartz.Job;
import org.quartz.SchedulerException;
import org.quartz.spi.TriggerFiredBundle;

import com.founder.e5.commons.Log;
import com.founder.e5.context.Context;


/**
 * job�����࣬����job����Ҫʵ�ֽӿ�:org.quartz.spi.JobFactory<p>
 * ���е�jobʵ�����뵽hashTable��
 * @author wanghc
 */
public class JobFactory implements org.quartz.spi.JobFactory
{
	private Log log = Context.getLog(Constants.LOG_NAME);
	
	/**
	 * key=jobName,value=job instance
	 */
	private HashMap jobMap = new HashMap();
	
   /* (non-Javadoc)
	 * @see org.quartz.spi.JobFactory#newJob(org.quartz.spi.TriggerFiredBundle)
	 */
	public Job newJob(TriggerFiredBundle bundle) throws SchedulerException
	{
		String jobName = bundle.getJobDetail().getName();
		Object job = jobMap.get(jobName); 
		if(job == null)
		{			
			try
			{
				job = bundle.getJobDetail().getJobClass().newInstance();
				jobMap.put(jobName,job);
			}
			catch (Exception e)
			{			
				log.error("JobFactory new job error:"+e.getMessage());
				throw new SchedulerException(e);
			}			
		}
		
		return (Job)job;
	}

   
   /**
    * ��hashtable���Ƴ�jobʵ��
    * @param jobName
    * @roseuid 44967082009C
    */
   public void removeJob(String jobName) 
   {
	   if(jobMap.get(jobName)!=null)
	   {
		   jobMap.remove(jobName);
	   }
	   else
	   {
		   log.warn(jobName + "is not exist!");
	   }		   
   }
}
