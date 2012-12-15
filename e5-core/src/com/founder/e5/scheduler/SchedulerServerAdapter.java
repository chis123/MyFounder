package com.founder.e5.scheduler;


/**
 * 任务调度代理，如果不使用集群直接调用Scheudler，如果使用集群调用指定的server的服?
 * ，可以通过webservice调用
 */
public interface SchedulerServerAdapter 
{  

   
   /**
    * 调度全部符合条件的任务，将任务加入到调度器中等待执行，此时任务的状态为"等待执行"
    * 。
    * 返回调度的任务数量
    * @param server
    * @return int
    * @roseuid 449216B703B9
    */
   public int schedulerAll(String server);
   
   /**
    * 调度一个指定的任务，将任务加入到调度器中等待执行，此时任务的状态为"等待执行"
    * 返回是否成功，当前服务器对指定的任务可能没有配置。
    * @param server
    * @param jobID 
    * @return boolean
    * @roseuid 449216B703D8
    */
   public boolean scheduler(String server, int jobID);
   
   /**
    * 停止全部的任务，注意，有些任务如果正在处理可能不会立即被打断，需要具体的业务job?

    * 己判断状态退出
    * @param server
    * @return int
    * @roseuid 449216B8000F
    */
   public int stopAll(String server);
   
   /**
    * 停止一个指定的任务，注意，有些任务如果正在处理可能不会立即被打断，需要具体的业务
    * job自己判断状态退出
    * @param server
    * @param jobID 
    * @return boolean
    * @roseuid 449216B8002E
    */
   public boolean stop(String server, int jobID);
   
   /**
    * 关闭任务调度程序和所有的任务，释放线程、job、trigger
    * @param server
    * @roseuid 449216B8003E
    */
   public void shutdown(String server);
   
   /**
    * 取得所有节点的运行的job，包含所有状态的job，注意它不同于SchedulerServer的getJobs
    * 方法。
    * @return com.founder.e5.scheduler.JobViewObject[]
    * @roseuid 449216B8006D
    */
   public JobViewObject[] getJobs();
   
}
/**
 * SchedulerServerAdapter.getCurrentlyExectingJobs()
 */
