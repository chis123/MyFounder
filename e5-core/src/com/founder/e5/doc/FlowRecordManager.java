/*
 * Created on 2005-6-21 17:01:39
 *
 */
package com.founder.e5.doc;

import com.founder.e5.context.E5Exception;

/**
 * ���̼�¼ʵ�����ӿڣ��ṩ�����ݿ��б����ɾ�Ĳ��������
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2005-6-21 17:01:39
 */
public interface FlowRecordManager {

	/**
	 * �����ݿ��д���һ�����̼�¼�����ݼ�¼
	 * 
	 * @param ���̼�¼���������ĵ�
	 * @param FlowRecordʵ��(ע��:��Ҫ��IDֵ,�ᱻ����)
	 * @return FlowRecordʵ�������������ɵ�IDֵ��
	 * @throws E5Exception
	 * @created 2005-7-25 9:37:14
	 */
	public FlowRecord createFlowRecord( Document doc, FlowRecord fr )
			throws E5Exception;

	/**
	 * �����ݿ��д���һ�����̼�¼�����ݼ�¼
	 * 
	 * @param ���̼�¼���������ĵ��Ŀ�ID
	 * @param ���̼�¼���������ĵ���ID
	 * @param FlowRecordʵ��(ע��:��Ҫ��IDֵ,�ᱻ����)
	 * @return FlowRecordʵ�������������ɵ�IDֵ��
	 * @throws E5Exception
	 * @created 2005-7-18 10:06:56
	 */
	public FlowRecord createFlowRecord( int docLibID, long docID, FlowRecord fr )
			throws E5Exception;

	/**
	 * �����ĵ���ID�����̼�¼IDȡ���̼�¼ʵ��
	 * 
	 * @param docLibID �ĵ���ID
	 * @param frID ���̼�¼ID
	 * @return FlowRecordʵ��
	 * @throws E5Exception
	 */
	public FlowRecord get( int docLibID, long frID ) throws E5Exception;

	/**
	 * ȡ�ù�����ָ���ĵ����������̼�¼��ID<br>
	 * <br>
	 * ���̼�¼ID�ļ�����������
	 * 
	 * @param docLibID �ĵ���ID
	 * @param docID �ĵ�ID
	 * @return ���̼�¼ID����
	 * @throws E5Exception
	 * @created 2005-7-24 14:26:38
	 */
	public long[] getAssociatedFRIDs( int docLibID, long docID )
			throws E5Exception;

	/**
	 * ȡ�ù�����ָ���ĵ����������̼�¼<br>
	 * <br>
	 * ���̼�¼���ϰ�ID��������
	 * 
	 * @param docLibID �ĵ���ID
	 * @param docID �ĵ�ID
	 * @return ���̼�¼����
	 * @throws E5Exception
	 * @created 2005-7-20 10:10:26
	 */
	public FlowRecord[] getAssociatedFRs( int docLibID, long docID )
			throws E5Exception;

	/**
	 * ȡ�ù�����ָ���ĵ����������̼�¼<br>
	 * <br>
	 * ���̼�¼���ϰ�ָ����˳���������
	 * 
	 * @param docLibID �ĵ���ID
	 * @param docID �ĵ�ID
	 * @param asc ������� true��ʾ���������� false��ʾ��������
	 * @return ���̼�¼����
	 * @throws E5Exception
	 */
	public FlowRecord[] getAssociatedFRs( int docLibID, long docID, boolean asc )
			throws E5Exception;
	/**
	 * �����ݿ���ɾ��������ĵ���������������̼�¼<br>
	 * 
	 * @param docLibID �ĵ���ID
	 * @param docID �ĵ�ID
	 * @return ��ɾ�������̼�¼����
	 * @throws E5Exception
	 */
	public int deleteAssociatedFRs( int docLibID, long docID )
			throws E5Exception;

	/**
	 * �����ĵ���ID�����̼�¼IDɾ�����̼�¼
	 * 
	 * @param docLibID �ĵ���ID
	 * @param frID ���̼�¼ID
	 * @throws E5Exception
	 */
	public void delete( int docLibID, long frID ) throws E5Exception;

	/**
	 * ����һ���ĵ�,�ƶ���ص����̼�¼�����ĵ���
	 * 
	 * @param docLibID Ŀǰ���ڵ��ĵ���ID
	 * @param docID �ĵ�ID
	 * @param newDocLibID ���ĵ���ID
	 * @return ������̼�¼������
	 * @throws E5Exception
	 * @created 2005-7-20 9:24:49
	 */
	public int moveAssociatedFRs( int docLibID, long docID, int newDocLibID )
			throws E5Exception;

}
