package com.founder.e5.scheduler.db;

import java.util.List;

import org.hibernate.Hibernate;

import com.founder.e5.context.BaseDAO;
import com.founder.e5.context.Context;
import com.founder.e5.context.E5Exception;
import com.founder.e5.context.EUID;

/**
 * 系统任务管理实现类
 * @author wanghc
 * @created 2006-6-21
 */
public class SysJobManagerImpl implements SysJobManager
{

	/* (non-Javadoc)
	 * @see com.founder.e5.scheduler.db.SysJobManager#createJob(com.founder.e5.scheduler.db.SysJob)
	 */
	public void createJob(SysJob job) throws E5Exception
	{	
		int id = (int)EUID.getID("sys_jobs");
		job.setJobID(id);
		BaseDAO baseDAO = new BaseDAO();
		baseDAO.save(job);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.scheduler.db.SysJobManager#deleteJob(int)
	 */
	public void deleteJob(int jobID)
	{
		//1.删除job
		BaseDAO baseDAO = new BaseDAO();
		SysJob job = getJob(jobID);
		baseDAO.delete(job);		
		
		//2.删除job下的trigger
		SysTriggerManager triggerManager = (SysTriggerManager)Context.getBean("SysTriggerManager");
		triggerManager.deleteTriggersByJob(jobID);		
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.scheduler.db.SysJobManager#getJob(int)
	 */
	public SysJob getJob(int jobID)
	{
		BaseDAO baseDAO = new BaseDAO();
		return (SysJob)baseDAO.get(SysJob.class,new Integer(jobID));
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.scheduler.db.SysJobManager#getJobs()
	 */
	public SysJob[] getJobs()
	{
		BaseDAO baseDAO = new BaseDAO();
		List list = baseDAO.find("from SysJob r order by r.jobID");
		if(list == null || list.size()==0) return null;
		
		return (SysJob[])list.toArray(new SysJob[0]);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.scheduler.db.SysJobManager#updateJob(com.founder.e5.scheduler.db.SysJob)
	 */
	public void updateJob(SysJob job)
	{
		BaseDAO baseDAO = new BaseDAO();
		baseDAO.update(job);		
	}

	public SysJob[] getJobsByActive(String active)
	{
		if(active == null)
			return getJobs();
		
		BaseDAO baseDAO = new BaseDAO();
		List list = baseDAO.find("from SysJob r where r.active=:active order by r.jobID",active,Hibernate.STRING);
		if(list == null || list.size()==0) return null;
		
		return (SysJob[])list.toArray(new SysJob[0]);		
	}

	public SysJob[] getAvailableJobs()
	{
		BaseDAO baseDAO = new BaseDAO();
		List list = baseDAO.find("from SysJob r where r.active=? or r.active=? order by r.jobID",
				new Object[]{SysJob.STATE_ENABLE_AUTO,SysJob.STATE_ENABLE_HUMAN});
		if(list == null || list.size()==0) return null;
		
		return (SysJob[])list.toArray(new SysJob[0]);	
	}
	
}
