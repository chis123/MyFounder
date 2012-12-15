package com.founder.e5.scheduler;

/**
 * <p>
 * ������ȷ���������ӿڣ����Զ�������Ҳ����������web 
 * �����£�����{@link #schedulerAll()}���Ե���ȫ��������.
 * ��������Ŀǰ��֧�ּ�Ⱥ��û����Ӧ�ļ�Ⱥ�����ࡣ
 * </p>
 * <p>
 * ����ӿڿ����ж��ʵ���࣬����<code>SchedulerLocalServer</code>����������������������ȣ�
 * ����������Ⱥ�ĵ�����������ʵ����ͨ��url���Ƽ�Ⱥ��<code>SchedulerServerUrlClient</code>���
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
    * ����ȫ���������������񣬽�������뵽�������еȴ�ִ�У���ʱ�����״̬Ϊ"�ȴ�ִ��"
    * ��
    * ���ص��ȵ���������
    * @return int
    * @roseuid 4490FC4C0222
    */
   public int schedulerAll();
   
   /**
    * ����һ��ָ�������񣬽�������뵽�������еȴ�ִ�У���ʱ�����״̬Ϊ"�ȴ�ִ��"
    * �����Ƿ�ɹ�����ǰ��������ָ�����������û�����á�
    * @param jobID - jobID
    * @return boolean
    * @roseuid 4490FCC602BF
    */
   public boolean scheduler(int jobID);
   
   /**
    * ֹͣȫ��������ע�⣬��Щ����������ڴ�����ܲ�����������ϣ���Ҫ�����ҵ��job
    * ���ж�״̬�˳�
    * @return int
    * @roseuid 4490FDF80128
    */
   public int stopAll();
   
   /**
    * <p>ֹͣһ��ָ��������ע�⣬��Щ����������ڴ�����ܲ�����������ϣ���Ҫ�����ҵ��
    * job�Լ��ж�״̬�˳�
    * </p>
    * ���job��ֹͣ������ʵ�����Ƴ����������ٴε���shceduler��������ʱ�ᴴ���µ�ʵ��
    * @param jobID - jobID
    * @return boolean
    * @roseuid 4490FE410242
    */
   public boolean stop(int jobID);
   
   /**
    * �ر�������ȳ�������е������ͷ��̡߳�job��trigger
    * @roseuid 4490FF2102BF
    */
   public void shutdown();
   
   /**
    * ȡ�õ�ǰ����ִ�е�job,����job��������(job������=jobID_jobName)
    * @return String[]
    * @roseuid 44912AC30177
    */
   public String[] getCurrentlyExectingJobs();
   
   /**
    * ȡ�������Ѿ�������job����������ִ�к͵ȴ�ִ�еģ�,����job��������(job������=jobI
    * D_jobName)
    * @return String[]
    * @roseuid 44913075032C
    */
   public String[] getJobs();
}
