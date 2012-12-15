package com.founder.e5.scheduler;

/**
 * �������adapter����
 * ��Ϊ������spring�г�ʼ��(��Ҫ�õ�ConfigReader),������ʹ��ʱ��ʼ�����ʵ��
 * @author wanghc
 *
 */
public class SchedulerServerAdapterFactory
{
	private static SchedulerServerAdapter adapter = null;
	
	static public synchronized SchedulerServerAdapter getSchedulerServerAdapter()
	{
		if(adapter==null)
		{
			adapter = new SchedulerServerAdapterImpl();
		}
		return adapter;
	}
}
