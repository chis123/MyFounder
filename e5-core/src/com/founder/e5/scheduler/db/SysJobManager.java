package com.founder.e5.scheduler.db;

import com.founder.e5.context.E5Exception;


/**
 * ϵͳ�����ά���ӿ�
 */
public interface SysJobManager 
{
   
   /**
    * ����һ������
    * @param job
    * @roseuid 4487C7830167
    */
   public void createJob(SysJob job) throws E5Exception;
   
   /**
    * ����һ������
    * @param job
    * @roseuid 4487C789002E
    */
   public void updateJob(SysJob job);
   
   /**
    * ȡ��һ��job
    * @param jobID
    * @return com.founder.e5.scheduler.db.SysJob
    * @roseuid 4487C78E01C5
    */
   public SysJob getJob(int jobID);
   
   /**
    * ��ѯȫ����job�б�
    * @return com.founder.e5.scheduler.db.SysJob[]
    * @roseuid 4487C7B20148
    */
   public SysJob[] getJobs();
   
   /**
    * ���������״̬��ѯ�����б�
    * @param active
    * @return
    */
   public SysJob[] getJobsByActive(String active);
   
   /**
    * ��ѯ���õ����񣨰����ֹ��������Զ�����������
    * @return
    */
   public SysJob[] getAvailableJobs();
   
   /**
    * ɾ��������Ҫһͬɾ�������µĴ�����
    * @param jobID
    * @roseuid 4487C7C7005D
    */
   public void deleteJob(int jobID);
}
