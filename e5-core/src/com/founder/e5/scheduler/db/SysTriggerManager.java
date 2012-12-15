package com.founder.e5.scheduler.db;

import com.founder.e5.context.E5Exception;


/**
 * 任务触发器管理
 * 
 * @author wanghc
 */
public interface SysTriggerManager 
{
   
   /**
    * 创建Trigger
    * @param trigger
    * @roseuid 44924E0700EA
    */
   public void createTrigger(SysTrigger trigger) throws E5Exception;
   
   /**
    * 更新trigger
    * @param trigger
    * @roseuid 44924E1100EA
    */
   public void updateTrigger(SysTrigger trigger);
   
   /**
    * 取得一个Trigger
    * @param triggerID
    * @return com.founder.e5.scheduler.db.SysTrigger
    * @roseuid 44924E170242
    */
   public SysTrigger getTrigger(int triggerID);
   
   /**
    * 根据jobID取得job下的所有触发器
    * @param jobID
    * @return com.founder.e5.scheduler.db.SysTrigger[]
    * @roseuid 44924E1E01C5
    */
   public SysTrigger[] getTriggersByJob(int jobID);
   
   /**
    * 取得一个server下的所有可用的job(active='Y')，包括server==ALL和server==自己的?自?

    * 的怎么识别
    * @return com.founder.e5.scheduler.db.SysTrigger
    * @roseuid 44924FAF007D
    */
   public SysTrigger getTriggersByServer(int jobID,String serverName);
   
   /**
    * 删除一个触发器
    * @param triggerID
    * @roseuid 44925B10001F
    */
   public void deleteTrigger(int triggerID);
   
   /**
    * 删除任务下的所有触发器
    * @param jobID
    * @roseuid 44925B17009C
    */
   public void deleteTriggersByJob(int jobID);
}
