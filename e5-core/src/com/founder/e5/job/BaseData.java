package com.founder.e5.job;

/**
 * ĳЩ����£�һ��ϵͳ����Ҫ�ں�̨һֱ����ĳ���������<br/>
 * �÷������ÿ��һ��ʱ���ظ�ִ��һ�Σ�ͨ�����ĳ��������Ҫִ�е�����<br/>
 * ÿ����ִ�е�������Ϊ���һ����¼�����ڡ�<br/>
 * ������ִ������ʱ���Ȱѱ��е�������һ����ִ�б�ǣ�ִ�����ɾ���ü�¼��<br/>
 * ������ִ�в��ɹ�����ѱ��е����������ɹ���ǡ�<br/>
 * <br/>
 * job������Ϊ��������������ĳ���<br/>
 * <br/>
 * BaseData��ʾ����������һ����¼��Ҫ���������������һ����Ψһ��ʶ���У�
 * �Լ�һ����ʾִ��״̬���С���BaseData����docID��status��ʾ��<br/>
 * �������չ����࣬�������������������ԡ�
 * 
 * Created on 2005-6-1
 * @author Gong Lijie
 */
public class BaseData
{
	/**����*/
	protected int docID;
	/**ִ��״̬*/
	protected int status;
	
	/**״̬��δִ��*/
	public static final int STATUS_WAITING = 0;
	/**״̬����ִ��*/
    public static final int STATUS_DOING = 1;
	/**״̬��ִ��ʧ��*/
    public static final int STATUS_FAILED = 2;

    public int getDocID()
    {
        return docID;
    }
    public void setDocID(int docid)
    {
        this.docID = docid;
    }
    public int getStatus()
    {
        return status;
    }
    public void setStatus(int nStatus)
    {
        this.status = nStatus;
    }
}
