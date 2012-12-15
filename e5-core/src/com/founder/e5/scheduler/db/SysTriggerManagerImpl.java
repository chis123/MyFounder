package com.founder.e5.scheduler.db;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.type.Type;

import com.founder.e5.context.BaseDAO;
import com.founder.e5.context.E5Exception;
import com.founder.e5.context.EUID;

/**
 * 系统任务触发器管理实现类
 * @author wanghc
 *
 */
public class SysTriggerManagerImpl implements SysTriggerManager
{

	/* (non-Javadoc)
	 * @see com.founder.e5.scheduler.db.SysTriggerManager#createTrigger(com.founder.e5.scheduler.db.SysTrigger)
	 */
	public void createTrigger(SysTrigger trigger) throws E5Exception
	{
		int id = (int)EUID.getID("SysTrigger");
		trigger.setTriggerID(id);
		BaseDAO baseDAO = new BaseDAO();
		baseDAO.save(trigger);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.scheduler.db.SysTriggerManager#deleteTrigger(int)
	 */
	public void deleteTrigger(int triggerID)
	{
		
		BaseDAO baseDAO = new BaseDAO();
		SysTrigger trigger = getTrigger(triggerID);
		baseDAO.delete(trigger);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.scheduler.db.SysTriggerManager#deleteTriggersByJob(int)
	 */
	public void deleteTriggersByJob(int jobID)
	{		
		BaseDAO baseDAO = new BaseDAO();
		baseDAO.delete("delete SysTrigger where jobID=:jobID",new Integer(jobID),Hibernate.INTEGER);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.scheduler.db.SysTriggerManager#getTrigger(int)
	 */
	public SysTrigger getTrigger(int triggerID)
	{
		BaseDAO baseDAO = new BaseDAO();
		
		return (SysTrigger)baseDAO.get(SysTrigger.class,new Integer(triggerID));
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.scheduler.db.SysTriggerManager#getTriggersByJob(int)
	 */
	public SysTrigger[] getTriggersByJob(int jobID)
	{
		BaseDAO baseDAO = new BaseDAO();
		List list = baseDAO.find("from SysTrigger r where r.jobID=:jobID order by r.triggerID",new Integer(jobID),Hibernate.INTEGER);
		if(list == null) return null;		
		
		return (SysTrigger[])list.toArray(new SysTrigger[0]);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.scheduler.db.SysTriggerManager#getTriggersByServer(java.lang.String)
	 */
	public SysTrigger getTriggersByServer(int jobID,String serverName)
	{
		//1个服务器的1个任务只能设置一个触发器
		BaseDAO baseDAO = new BaseDAO();
		
		List list = baseDAO.find("from SysTrigger r where r.active='Y' and r.jobID=:jobID and (r.server='ALL' or r.server=:server) order by r.triggerID",
				new String[]{"jobID","server"},
				new Object[]{new Integer(jobID),serverName},
				new Type[]{Hibernate.INTEGER,Hibernate.STRING});
		if(list == null || list.size() ==0) return null;
		
		return (SysTrigger)list.get(0);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.scheduler.db.SysTriggerManager#updateTrigger(com.founder.e5.scheduler.db.SysTrigger)
	 */
	public void updateTrigger(SysTrigger trigger)
	{		
		BaseDAO baseDAO = new BaseDAO();
		baseDAO.update(trigger);
	}

}
