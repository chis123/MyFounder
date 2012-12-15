package com.founder.e5.scheduler;

import java.util.HashMap;

/**
 * @author wanghc
 * ά����Ⱥ�ڵ������Ĵ�������
 */
public final class SchedulerClientFactory 
{
   
   /**
    * key=serverName,value=�����SchedulerServerʵ����
    */
   private static HashMap ssClient = new HashMap();
   
    
   /**
    * ȡ��ָ����������������ȹ�����
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
