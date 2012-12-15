package com.founder.e5.job;


import java.util.List;

import com.founder.e5.context.E5Exception;
import com.founder.e5.scheduler.BaseJob;

/**
 * ĳЩ����£�һ��ϵͳ����Ҫ�ں�̨һֱ����ĳ���������<br/>
 * �÷������ÿ��һ��ʱ���ظ�ִ��һ�Σ�ͨ�����ĳ��������Ҫִ�е�����<br/>
 * ÿ����ִ�е�������Ϊ���һ����¼�����ڡ�<br/>
 * ������ִ������ʱ���Ȱѱ��е�������һ����ִ�б�ǣ�ִ�����ɾ���ü�¼��<br/>
 * ������ִ�в��ɹ�����ѱ��е����������ɹ���ǡ�<br/>
 * <br/>
 * job������Ϊ��������������ĳ���<br/>
 * <br/>
 * 
 * BaseService�Ƿ���E5ϵͳ��������Ƚӿڵķ�������ࡣ<br/>
 * 
 * ���̳���BaseJob������BaseManager����ʵ�ʵĲ�����<br/>
 * ����������Ҫ������ֻҪ�̳б����༴�ɣ���������κη���ʵ�֣����˸�manager������ֵ��<br/>
 * ��ʹ��manager����־���м�¼��<br/>
 * @created 2006-7-27
 * @author Gong Lijie
 * @version 1.0
 */
public abstract class BaseService extends BaseJob
{
	protected BaseManager manager;
	
	protected void execute() throws E5Exception
	{
		List datas = manager.getAllData(BaseData.STATUS_WAITING);		
        if (datas == null) return;

        for (int i = 0; i < datas.size(); i++)
        {
        	if (isInterrupt()) break;//���������жϱ�־���������˳���
        	BaseData data = (BaseData)datas.get(i);
        	
            //�����ڴ����־�������ɹ���������Ǳ����������޸��ˡ���������
        	try {
        		manager.setStatus(data, BaseData.STATUS_DOING);
			} catch (Exception e) {
                continue;
			}
			//��ʼ�������ҵ����
        	try {
        		manager.handleData(data);
			} catch (Exception e) {
				//ʹ��manager����־���м�¼
				manager.getLog().error("[Service Running]", e);
                try {
                	//�����쳣ʱ����ʧ�ܱ�־
                	manager.setStatus(data, BaseData.STATUS_FAILED);
				} catch (Exception e1) {
				}
				continue;
			}
			//������Ϻ�ɾ��������еļ�¼
            try {
            	manager.delete(data.getDocID());
			} catch (Exception e1) {
			}
        }
	}
}
