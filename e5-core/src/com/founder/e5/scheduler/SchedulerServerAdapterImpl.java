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
 * SchedulerServerAdapterʵ����
 * @author wanghc
 */
public class SchedulerServerAdapterImpl implements SchedulerServerAdapter 
{
	//private Log log = Context.getLog(Constants.LOG_NAME);
	
   /**
    * �Ƿ�Ⱥ
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
	   //��ѯ���п��õ�job
	   SysJobManager jobManager = (SysJobManager)Context.getBean("SysJobManager");
	   SysJob[] jobs = jobManager.getAvailableJobs();//jobManager.getJobsByActive("Y");
	   
	   if(jobs == null || jobs.length == 0)
		   return null;
	   
	   //����job״̬
	   if(cluster)
		   return getClusterJobs(jobs);
	   else
		   return getNoClusterJobs(jobs);
   }
   
   /**
    * ��Ⱥ��ʽ��ȡ��job�б�
    * @param jobs
    * @return
    */
   private JobViewObject[] getClusterJobs(SysJob[] jobs)
   {
	   //1.ȡ�����з�������job״̬
	   HashMap serverExecutingMap = new HashMap();
	   HashMap serverJobsMap      = new HashMap();
	   for(int i=0;i<servers.size();i++)
	   {
		   InfoServer infoServer = (InfoServer)servers.get(i);
		   SchedulerServer server= getSpecSchedulerServer(infoServer.getName());
		   
		   serverJobsMap.put(infoServer.getName(),server.getJobs());
		   serverExecutingMap.put(infoServer.getName(),server.getCurrentlyExectingJobs());
	   }
	   
	   //��SysJobת����JobViewObject����
	   JobViewObject jobViews[] = new JobViewObject[jobs.length];
	   for(int i=0;i<jobs.length;i++)
	   {
		   jobViews[i] = new JobViewObject();
		   jobViews[i].setJobID(jobs[i].getJobID());
		   jobViews[i].setJobName(jobs[i].getName());
		   
		   //����job��ÿ��server�ϵ�״̬
		   JobState states[] = new JobState[servers.size()];
		   for(int serverIndex=0;serverIndex<servers.size();serverIndex++)
		   {
			   InfoServer infoServer = (InfoServer)servers.get(serverIndex);
			   //Ĭ��Ϊδ����
			   JobState state = new JobState(infoServer.getName(),
					   Constants.JOB_STATE_NOSTART,
					   checkJobCanUseAtServer(jobs[i].getJobID(),infoServer.getName()));
			   
			   states[serverIndex] = state;
			   //����server�ϵ�״̬
			   setServerJobState(state,jobs[i].getJobID(),infoServer,serverExecutingMap,serverJobsMap);		   
		   }
		   jobViews[i].setJobStates(states);
	   }   

	   return jobViews;
   }
   
   /**
    * ���÷�������job��״̬
    * @param state
    * @param jobID
    * @param server
    * @param serverExecutingMap
    * @param serverJobsMap
    */
   private void setServerJobState(JobState state,int jobID,InfoServer server,HashMap serverExecutingMap,HashMap serverJobsMap)
   {
	   String strJobID = jobID + "_";
	   //���÷�������״̬
	   String[] executingArray = (String[])serverExecutingMap.get(server.getName());
	   
	   //����ִ��
	   for(int i=0;executingArray!=null && i<executingArray.length;i++)
	   {
		   if(executingArray[i].startsWith(strJobID))
		   {
			   state.setJobState(Constants.JOB_STATE_EXECUTING);
			   return;
		   }
	   }
	   //�Ѿ�����δִ��
	   String[] jobsArray = (String[])serverJobsMap.get(server.getName());
	   
	   //����ִ��
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
    * ��������ڷ��������Ƿ����(�Ƿ������˴�����)
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
    * �Ǽ�Ⱥ��ʽȡ��job�б�
    * @param jobs
    * @return
    */
   private JobViewObject[] getNoClusterJobs(SysJob[] jobs)
   {
	   //��SysJobת����JobViewObject����
	   JobViewObject jobViews[] = new JobViewObject[jobs.length];
	   for(int i=0;i<jobs.length;i++)
	   {
		   jobViews[i] = new JobViewObject();
		   jobViews[i].setJobID(jobs[i].getJobID());
		   jobViews[i].setJobName(jobs[i].getName());
		   jobViews[i].setLogUrl(jobs[i].getLogUrl());
		   
		   //����״̬
		   JobState state = new JobState(Constants.getServerName(),
				   Constants.JOB_STATE_NOSTART,
				   checkJobCanUseAtServer(jobs[i].getJobID(),Constants.getServerName()));
		   
		   jobViews[i].setJobStates(new JobState[]{state});
		   
	   }
	   
	   //2.����job��server�ϵ�״̬
	   SchedulerServer ss[] = getSchedulerServer(null);
	   
	   //�����Ѿ�������job
	   String strJobs[]     = ss[0].getJobs();
	   for(int runIndex=0;runIndex<strJobs.length;runIndex++)
	   {		   
		   //jobName��JobID_JobName���
		   int jobID = Integer.parseInt(strJobs[runIndex].split("_")[0]);
		   //���Ҳ�����job��״̬
		   for(int viewIndex=0;viewIndex<jobViews.length;viewIndex++)
		   {
			   if(jobID==jobViews[viewIndex].getJobID())
				   jobViews[viewIndex].getJobStates()[0].setJobState(Constants.JOB_STATE_STARTED);				   
		   }			   
	   }
	   
	   //��������ִ�е�job
	   String strExecutes[] = ss[0].getCurrentlyExectingJobs();
	   for(int executeIndex=0;executeIndex<strExecutes.length;executeIndex++)
	   {
		   //jobName��JobID_JobName���
		   int jobID = Integer.parseInt(strExecutes[executeIndex].split("_")[0]);
		   //���Ҳ�����job��״̬
		   for(int viewIndex=0;viewIndex<jobViews.length;viewIndex++)
		   {
			   if(jobID==jobViews[viewIndex].getJobID())
			   		jobViews[viewIndex].getJobStates()[0].setJobState(Constants.JOB_STATE_EXECUTING);				   
		   }			   
	   }		   
	   
	   return jobViews;
   }
   /**
    * ����ڵ�
    */
   private List servers = null;
   
   /*
   * ��ʼ��Adapter,��ʼ��cluster,��������ַ�б������ǵĵĵ��õ�ַ
   * ���õ�service��ַȫ·������http://localhost:8080/e5new/service/XXXX
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
    * ����server����ȡ��������ȷ������
    * ����ڼ�Ⱥ�����£�SchedulerServerΪSchedulerServerUrlClient,����ΪSchedulerLocalServer
    * @param server
    * @return
    */
   private SchedulerServer[] getSchedulerServer(String server)
   {
	   SchedulerServer[] schedulerServer = null;
	   
	   //ȡ�ñ��ض���
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
    * ����server���Ʒ���ָ����server
    * @param server
    * @return
    */
   private SchedulerServer getSpecSchedulerServer(String server)
   {
	   return getSchedulerServer(server)[0];
   }
}
