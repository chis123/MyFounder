package com.founder.e5.scheduler;

/**
 * <p>
 * 任务调度服务器管理接口，可以独立运行也可以运行在web 
 * 环境下，调用{@link #schedulerAll()}可以调度全部的任务.
 * 独立运行目前不支持集群，没有相应的集群控制类。
 * </p>
 * <p>
 * 这个接口可以有多个实现类，其中<code>SchedulerLocalServer</code>管理本服务器的所有任务调度，
 * 如有其它集群的调用由其它的实现如通过url控制集群的<code>SchedulerServerUrlClient</code>完成
 * <p>
 * @see SchedulerLocalServer
 * @see SchedulerServerUrlClient
 * 
 * @author wanghc
 * 
 */
public interface SchedulerServer 
{
   
   /**
    * 调度全部符合条件的任务，将任务加入到调度器中等待执行，此时任务的状态为"等待执行"
    * 。
    * 返回调度的任务数量
    * @return int
    * @roseuid 4490FC4C0222
    */
   public int schedulerAll();
   
   /**
    * 调度一个指定的任务，将任务加入到调度器中等待执行，此时任务的状态为"等待执行"
    * 返回是否成功，当前服务器对指定的任务可能没有配置。
    * @param jobID - jobID
    * @return boolean
    * @roseuid 4490FCC602BF
    */
   public boolean scheduler(int jobID);
   
   /**
    * 停止全部的任务，注意，有些任务如果正在处理可能不会立即被打断，需要具体的业务job
    * 己判断状态退出
    * @return int
    * @roseuid 4490FDF80128
    */
   public int stopAll();
   
   /**
    * <p>停止一个指定的任务，注意，有些任务如果正在处理可能不会立即被打断，需要具体的业务
    * job自己判断状态退出
    * </p>
    * 这个job被停止后，他的实例被移出调度器，再次调用shceduler方法调度时会创建新的实例
    * @param jobID - jobID
    * @return boolean
    * @roseuid 4490FE410242
    */
   public boolean stop(int jobID);
   
   /**
    * 关闭任务调度程序和所有的任务，释放线程、job、trigger
    * @roseuid 4490FF2102BF
    */
   public void shutdown();
   
   /**
    * 取得当前正在执行的job,返回job名称数组(job的名称=jobID_jobName)
    * @return String[]
    * @roseuid 44912AC30177
    */
   public String[] getCurrentlyExectingJobs();
   
   /**
    * 取得所有已经被调度job（包括正在执行和等待执行的）,返回job名称数组(job的名称=jobI
    * D_jobName)
    * @return String[]
    * @roseuid 44913075032C
    */
   public String[] getJobs();
}
