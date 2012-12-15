package com.founder.e5.scheduler;


/**
 * ������ȴ��������ʹ�ü�Ⱥֱ�ӵ���Scheudler�����ʹ�ü�Ⱥ����ָ����server�ķ�?
 * ������ͨ��webservice����
 */
public interface SchedulerServerAdapter 
{  

   
   /**
    * ����ȫ���������������񣬽�������뵽�������еȴ�ִ�У���ʱ�����״̬Ϊ"�ȴ�ִ��"
    * ��
    * ���ص��ȵ���������
    * @param server
    * @return int
    * @roseuid 449216B703B9
    */
   public int schedulerAll(String server);
   
   /**
    * ����һ��ָ�������񣬽�������뵽�������еȴ�ִ�У���ʱ�����״̬Ϊ"�ȴ�ִ��"
    * �����Ƿ�ɹ�����ǰ��������ָ�����������û�����á�
    * @param server
    * @param jobID 
    * @return boolean
    * @roseuid 449216B703D8
    */
   public boolean scheduler(String server, int jobID);
   
   /**
    * ֹͣȫ��������ע�⣬��Щ����������ڴ�����ܲ�����������ϣ���Ҫ�����ҵ��job?

    * ���ж�״̬�˳�
    * @param server
    * @return int
    * @roseuid 449216B8000F
    */
   public int stopAll(String server);
   
   /**
    * ֹͣһ��ָ��������ע�⣬��Щ����������ڴ�����ܲ�����������ϣ���Ҫ�����ҵ��
    * job�Լ��ж�״̬�˳�
    * @param server
    * @param jobID 
    * @return boolean
    * @roseuid 449216B8002E
    */
   public boolean stop(String server, int jobID);
   
   /**
    * �ر�������ȳ�������е������ͷ��̡߳�job��trigger
    * @param server
    * @roseuid 449216B8003E
    */
   public void shutdown(String server);
   
   /**
    * ȡ�����нڵ�����е�job����������״̬��job��ע������ͬ��SchedulerServer��getJobs
    * ������
    * @return com.founder.e5.scheduler.JobViewObject[]
    * @roseuid 449216B8006D
    */
   public JobViewObject[] getJobs();
   
}
/**
 * SchedulerServerAdapter.getCurrentlyExectingJobs()
 */
