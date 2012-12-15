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
 * ����job��,����������Ҫ�̳иó����࣬ʵ��execute()����,
 * �����������quartz��JobExecutionContext,JobExecutionException������,��������Ҳ���޷�ʹ��
 * �����ˡ�������Ŀ����ʹjob��������quartz�࣬��¼ÿ��job�����ȵ�ʱ�䣬�Ƿ񱻴�ϵĹ���
 * </p>
 * 
 * <p>
 * �����jobҲ�����Լ�ʵ��quartz��Job�ӿڣ�Ȼ��ʹ��quartz�����ԡ�
 * �÷���quartz��docs
 * </p>
 * 
 * @author wanghc
 */

public abstract class BaseJob implements StatefulJob,InterruptableJob
{
   protected Log log = Context.getLog(Constants.LOG_NAME);
   
	/**
	 * �Ƿ񱻴��
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
	 * job �Ƿ񱻴��
	 * @return
	 */
	protected boolean isInterrupt()
	{
		return interrupt;
	}

	public void execute(JobExecutionContext context) throws JobExecutionException
	{
		//��¼������־
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
	 * job��ִ�з���
	 * @throws E5Exception
	 */
	protected abstract void execute() throws E5Exception;
}
