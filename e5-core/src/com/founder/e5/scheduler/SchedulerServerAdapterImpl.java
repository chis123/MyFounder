package com.founder.e5.scheduler;

import java.util.HashMap;
import java.util.List;

//import com.founder.e5.commons.Log;
import com.founder.e5.config.ConfigReader;
import com.founder.e5.config.InfoServer;
import com.founder.e5.context.Context;
import com.founder.e5.scheduler.db.SysJob;
import com.founder.e5.scheduler.db.SysJobManager;
import com.founder.e5.scheduler.db.SysTrigger;
import com.founder.e5.scheduler.db.SysTriggerManager;


/**
 * SchedulerServerAdapter实现类
 * @author wanghc
 */
public class SchedulerServerAdapterImpl implements SchedulerServerAdapter 
{
	//private Log log = Context.getLog(Constants.LOG_NAME);
	
   /**
    * 是否集群
    */
   protected boolean cluster = false;    
   
   /**
    * @roseuid 44966F03002E
    */
   public SchedulerServerAdapterImpl() 
   {
	   init();
   }
   
   /**
    * @param server
    * @return int
    * @roseuid 44966F03004E
    */
   public int schedulerAll(String server) 
   {
	   SchedulerServer[] ss = getSchedulerServer(server);
	   int n = 0;
	   for(int i=0;i<ss.length;i++)
		   n = n + ss[i].schedulerAll();
	   return n;
   }
   
   /**
    * @param server
    * @param jobID
    * @return boolean
    * @roseuid 44966F0300BB
    */
   public boolean scheduler(String server, int jobID) 
   {
	   SchedulerServer[] ss = getSchedulerServer(server);
	   
	   for(int i=0;i<ss.length;i++)
		   ss[i].scheduler(jobID);
	   
	   return true;
   }
   
   /**
    * @param server
    * @return int
    * @roseuid 44966F030167
    */
   public int stopAll(String server) 
   {
	   SchedulerServer[] ss = getSchedulerServer(server);
	   int n = 0;
	   for(int i=0;i<ss.length;i++)
		   n = n + ss[i].stopAll();
	   return n;
   }
   
   /**
    * @param server
    * @param jobID
    * @return boolean
    * @roseuid 44966F0301E4
    */
   public boolean stop(String server, int jobID) 
   {
	   SchedulerServer[] ss = getSchedulerServer(server);
	   
	   for(int i=0;i<ss.length;i++)
		   ss[i].stop(jobID);
	   return true;
   }
   
   /**
    * @param server
    * @roseuid 44966F03029F
    */
   public void shutdown(String server) 
   {
	   SchedulerServer[] ss = getSchedulerServer(server);
	   
	   for(int i=0;i<ss.length;i++)
		   ss[i].shutdown();
   }
   
   /**
    * @return com.founder.e5.scheduler.JobViewObject[]
    * @roseuid 44966F03030D
    */
   public JobViewObject[] getJobs() 
   {
	   //查询所有可用的job
	   SysJobManager jobManager = (SysJobManager)Context.getBean("SysJobManager");
	   SysJob[] jobs = jobManager.getAvailableJobs();//jobManager.getJobsByActive("Y");
	   
	   if(jobs == null || jobs.length == 0)
		   return null;
	   
	   //设置job状态
	   if(cluster)
		   return getClusterJobs(jobs);
	   else
		   return getNoClusterJobs(jobs);
   }
   
   /**
    * 集群方式下取得job列表
    * @param jobs
    * @return
    */
   private JobViewObject[] getClusterJobs(SysJob[] jobs)
   {
	   //1.取得所有服务器的job状态
	   HashMap serverExecutingMap = new HashMap();
	   HashMap serverJobsMap      = new HashMap();
	   for(int i=0;i<servers.size();i++)
	   {
		   InfoServer infoServer = (InfoServer)servers.get(i);
		   SchedulerServer server= getSpecSchedulerServer(infoServer.getName());
		   
		   serverJobsMap.put(infoServer.getName(),server.getJobs());
		   serverExecutingMap.put(infoServer.getName(),server.getCurrentlyExectingJobs());
	   }
	   
	   //将SysJob转换成JobViewObject对象
	   JobViewObject jobViews[] = new JobViewObject[jobs.length];
	   for(int i=0;i<jobs.length;i++)
	   {
		   jobViews[i] = new JobViewObject();
		   jobViews[i].setJobID(jobs[i].getJobID());
		   jobViews[i].setJobName(jobs[i].getName());
		   
		   //设置job在每个server上的状态
		   JobState states[] = new JobState[servers.size()];
		   for(int serverIndex=0;serverIndex<servers.size();serverIndex++)
		   {
			   InfoServer infoServer = (InfoServer)servers.get(serverIndex);
			   //默认为未启动
			   JobState state = new JobState(infoServer.getName(),
					   Constants.JOB_STATE_NOSTART,
					   checkJobCanUseAtServer(jobs[i].getJobID(),infoServer.getName()));
			   
			   states[serverIndex] = state;
			   //设置server上的状态
			   setServerJobState(state,jobs[i].getJobID(),infoServer,serverExecutingMap,serverJobsMap);		   
		   }
		   jobViews[i].setJobStates(states);
	   }   

	   return jobViews;
   }
   
   /**
    * 设置服务器上job的状态
    * @param state
    * @param jobID
    * @param server
    * @param serverExecutingMap
    * @param serverJobsMap
    */
   private void setServerJobState(JobState state,int jobID,InfoServer server,HashMap serverExecutingMap,HashMap serverJobsMap)
   {
	   String strJobID = jobID + "_";
	   //设置服务器的状态
	   String[] executingArray = (String[])serverExecutingMap.get(server.getName());
	   
	   //正在执行
	   for(int i=0;executingArray!=null && i<executingArray.length;i++)
	   {
		   if(executingArray[i].startsWith(strJobID))
		   {
			   state.setJobState(Constants.JOB_STATE_EXECUTING);
			   return;
		   }
	   }
	   //已经启动未执行
	   String[] jobsArray = (String[])serverJobsMap.get(server.getName());
	   
	   //正在执行
	   for(int i=0;jobsArray!=null && i<jobsArray.length;i++)
	   {
		   if(jobsArray[i].startsWith(strJobID))
		   {
			   state.setJobState(Constants.JOB_STATE_STARTED);
			   return;
		   }
	   }
   }
	
   /**
    * 检查任务在服务器上是否可用(是否设置了触发器)
    * 
    * @param jobID
    * @param serverName
    * @return
    */
	private boolean checkJobCanUseAtServer(int jobID,String serverName)
	{
		SysTriggerManager triggerManager = (SysTriggerManager)Context.getBean("SysTriggerManager");
		SysTrigger trigger = triggerManager.getTriggersByServer(jobID,serverName);
		return (trigger==null)?false:true;
	}
	
   /**
    * 非集群方式取得job列表
    * @param jobs
    * @return
    */
   private JobViewObject[] getNoClusterJobs(SysJob[] jobs)
   {
	   //将SysJob转换成JobViewObject对象
	   JobViewObject jobViews[] = new JobViewObject[jobs.length];
	   for(int i=0;i<jobs.length;i++)
	   {
		   jobViews[i] = new JobViewObject();
		   jobViews[i].setJobID(jobs[i].getJobID());
		   jobViews[i].setJobName(jobs[i].getName());
		   jobViews[i].setLogUrl(jobs[i].getLogUrl());
		   
		   //任务状态
		   JobState state = new JobState(Constants.getServerName(),
				   Constants.JOB_STATE_NOSTART,
				   checkJobCanUseAtServer(jobs[i].getJobID(),Constants.getServerName()));
		   
		   jobViews[i].setJobStates(new JobState[]{state});
		   
	   }
	   
	   //2.设置job在server上的状态
	   SchedulerServer ss[] = getSchedulerServer(null);
	   
	   //设置已经启动的job
	   String strJobs[]     = ss[0].getJobs();
	   for(int runIndex=0;runIndex<strJobs.length;runIndex++)
	   {		   
		   //jobName由JobID_JobName组成
		   int jobID = Integer.parseInt(strJobs[runIndex].split("_")[0]);
		   //查找并设置job的状态
		   for(int viewIndex=0;viewIndex<jobViews.length;viewIndex++)
		   {
			   if(jobID==jobViews[viewIndex].getJobID())
				   jobViews[viewIndex].getJobStates()[0].setJobState(Constants.JOB_STATE_STARTED);				   
		   }			   
	   }
	   
	   //设置正在执行的job
	   String strExecutes[] = ss[0].getCurrentlyExectingJobs();
	   for(int executeIndex=0;executeIndex<strExecutes.length;executeIndex++)
	   {
		   //jobName由JobID_JobName组成
		   int jobID = Integer.parseInt(strExecutes[executeIndex].split("_")[0]);
		   //查找并设置job的状态
		   for(int viewIndex=0;viewIndex<jobViews.length;viewIndex++)
		   {
			   if(jobID==jobViews[viewIndex].getJobID())
			   		jobViews[viewIndex].getJobStates()[0].setJobState(Constants.JOB_STATE_EXECUTING);				   
		   }			   
	   }		   
	   
	   return jobViews;
   }
   /**
    * 服务节点
    */
   private List servers = null;
   
   /*
   * 初始化Adapter,初始化cluster,服务器地址列表，即他们的的调用地址
   * 调用的service地址全路径，如http://localhost:8080/e5new/service/XXXX
   */   
   private void init() 
   {
	   ConfigReader configReader = ConfigReader.getInstance();
	   List list = configReader.getServers();
	   if(list == null || list.size()<=1)
		   cluster = false;
	   else
	   {
		   cluster = true;
		   servers = list;
	   }
   }
   
   /**
    * 根据server名称取得任务调度服务对象
    * 如果在集群环境下，SchedulerServer为SchedulerServerUrlClient,否则为SchedulerLocalServer
    * @param server
    * @return
    */
   private SchedulerServer[] getSchedulerServer(String server)
   {
	   SchedulerServer[] schedulerServer = null;
	   
	   //取得本地对象
	   if(!cluster)
		   schedulerServer = new SchedulerServer[]{(SchedulerServer)Context.getBean("SchedulerLocalServer")};
	   else
	   {
		   if(Constants.SERVER_ALL.equals(server))
		   {
			   schedulerServer = new SchedulerServer[servers.size()];
			   for(int i=0;i<servers.size();i++)
			   {
				  InfoServer infoServer = (InfoServer)servers.get(i);
				  schedulerServer[i] = SchedulerClientFactory.getSchedulerServerClient(infoServer.getUrl());				  
			   }
		   }
		   else
		   {
			   for(int i=0;i<servers.size();i++)
			   {
				  InfoServer infoServer = (InfoServer)servers.get(i);
				  if(server.equals(infoServer.getName()))
				  {
					  schedulerServer = new SchedulerServer[]{SchedulerClientFactory.getSchedulerServerClient(infoServer.getUrl())};
				  }
			   }
		   }
		   
	   }
	   return schedulerServer;
   }
   
   /**
    * 根据server名称返回指定的server
    * @param server
    * @return
    */
   private SchedulerServer getSpecSchedulerServer(String server)
   {
	   return getSchedulerServer(server)[0];
   }
}
