package com.founder.e5.scheduler.db;

import com.founder.e5.context.E5Exception;


/**
 * 系统任务的维护接口
 */
public interface SysJobManager 
{
   
   /**
    * 创建一个任务
    * @param job
    * @roseuid 4487C7830167
    */
   public void createJob(SysJob job) throws E5Exception;
   
   /**
    * 更新一个任务
    * @param job
    * @roseuid 4487C789002E
    */
   public void updateJob(SysJob job);
   
   /**
    * 取得一个job
    * @param jobID
    * @return com.founder.e5.scheduler.db.SysJob
    * @roseuid 4487C78E01C5
    */
   public SysJob getJob(int jobID);
   
   /**
    * 查询全部的job列表
    * @return com.founder.e5.scheduler.db.SysJob[]
    * @roseuid 4487C7B20148
    */
   public SysJob[] getJobs();
   
   /**
    * 根据任务的状态查询任务列表
    * @param active
    * @return
    */
   public SysJob[] getJobsByActive(String active);
   
   /**
    * 查询可用的任务（包括手工启动和自动启动的任务）
    * @return
    */
   public SysJob[] getAvailableJobs();
   
   /**
    * 删除任务，需要一同删除任务下的触发器
    * @param jobID
    * @roseuid 4487C7C7005D
    */
   public void deleteJob(int jobID);
}
