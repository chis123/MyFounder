package com.founder.e5.scheduler.db;

import com.founder.e5.context.E5Exception;


/**
 * ���񴥷�������
 * 
 * @author wanghc
 */
public interface SysTriggerManager 
{
   
   /**
    * ����Trigger
    * @param trigger
    * @roseuid 44924E0700EA
    */
   public void createTrigger(SysTrigger trigger) throws E5Exception;
   
   /**
    * ����trigger
    * @param trigger
    * @roseuid 44924E1100EA
    */
   public void updateTrigger(SysTrigger trigger);
   
   /**
    * ȡ��һ��Trigger
    * @param triggerID
    * @return com.founder.e5.scheduler.db.SysTrigger
    * @roseuid 44924E170242
    */
   public SysTrigger getTrigger(int triggerID);
   
   /**
    * ����jobIDȡ��job�µ����д�����
    * @param jobID
    * @return com.founder.e5.scheduler.db.SysTrigger[]
    * @roseuid 44924E1E01C5
    */
   public SysTrigger[] getTriggersByJob(int jobID);
   
   /**
    * ȡ��һ��server�µ����п��õ�job(active='Y')������server==ALL��server==�Լ���?��?

    * ����ôʶ��
    * @return com.founder.e5.scheduler.db.SysTrigger
    * @roseuid 44924FAF007D
    */
   public SysTrigger getTriggersByServer(int jobID,String serverName);
   
   /**
    * ɾ��һ��������
    * @param triggerID
    * @roseuid 44925B10001F
    */
   public void deleteTrigger(int triggerID);
   
   /**
    * ɾ�������µ����д�����
    * @param jobID
    * @roseuid 44925B17009C
    */
   public void deleteTriggersByJob(int jobID);
}
