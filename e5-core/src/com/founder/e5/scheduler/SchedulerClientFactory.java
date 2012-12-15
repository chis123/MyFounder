package com.founder.e5.scheduler;

import java.util.HashMap;

/**
 * @author wanghc
 * 维护集群节点服务类的创建工厂
 */
public final class SchedulerClientFactory 
{
   
   /**
    * key=serverName,value=具体的SchedulerServer实现类
    */
   private static HashMap ssClient = new HashMap();
   
    
   /**
    * 取得指定服务器的任务调度管理类
    * @param server
    * @return com.founder.e5.scheduler.SchedulerServer
    * @roseuid 4492671300FA
    */
   public static synchronized SchedulerServer getSchedulerServerClient(String serverUrl) 
   {
	   SchedulerServer client = (SchedulerServer)ssClient.get(serverUrl);
	   if(client == null)
	   {
		   client = new SchedulerServerUrlClient(serverUrl);
		   ssClient.put(serverUrl,client);
	   }	   
	   return client;
   }
}
