/*
 * Created on 2005-6-23 10:16:39
 * 
 */
package com.founder.e5.doc;

import com.founder.e5.context.E5Exception;

/**
 * DocAssociationʵ������ӿڣ���������ɾ�Ĳ顣 <br>
 * ����e5context
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2005-6-23 10:16:39
 */
public interface DocAssociationManager {

	/**
	 * ����������ϵ
	 * 
	 * @param obj �������������Ϣ��������¼ID
	 * @return �����˼�¼ID��Ķ���
	 * @throws E5Exception
	 */
	public DocAssociation create( DocAssociation obj ) throws E5Exception;

	/**
	 * ɾ��������ϵ
	 * 
	 * @param recordID ������ϵ��¼ID
	 * @throws E5Exception
	 */
	public void delete( long recordID ) throws E5Exception;

	/**
	 * ɾ��ָ��Դ������й�����ϵ
	 * @param srcLibID
	 * @param srcID
	 * @throws E5Exception
	 */
	public void deleteBySrc( int srcLibID, long srcID ) throws E5Exception;

	/**
	 * ɾ��ָ��Ŀ�ĸ�����й�����ϵ
	 * @param destLibID
	 * @param destID
	 * @throws E5Exception
	 */
	public void deleteByDest( int destLibID, long destID ) throws E5Exception;

	/**
	 * ��ȡ������ϵ
	 * 
	 * @param recordID ������ϵ��¼ID
	 * @throws E5Exception
	 */
	public DocAssociation get( long recordID ) throws E5Exception;
	
	/**
	 * ����Դ����Ϣ��ѯ������ϵ
	 * 
	 * @param srcLibID ��ID
	 * @param srcID ���ID
	 * @param associationCode ������
	 * @return
	 * @throws E5Exception
	 */
	public DocAssociation[] getBySrc( int srcLibID, long srcID,
			int associationCode ) throws E5Exception;

	/**
	 * ����Ŀ�ĸ���Ϣ��ѯ������ϵ
	 * 
	 * @param destLibID ��ID
	 * @param destID ���ID
	 * @param associationCode ������
	 * @return
	 * @throws E5Exception
	 */
	public DocAssociation[] getByDest( int destLibID, long destID,
			int associationCode ) throws E5Exception;

	/**
	 * ���ݸ���Ϣ��ѯ������ϵ
	 * 
	 * @param rootLibID ��ID
	 * @param rootID ���ID
	 * @param associationCode ������
	 * @return
	 * @throws E5Exception
	 */
	public DocAssociation[] getByRoot( int rootLibID, long rootID,
			int associationCode ) throws E5Exception;

	// -------------------------------------------------------------------------

	/**
	 * ���������ĵ���Ĺ�����ϵ
	 * 
	 * @param srcLibID Դ�ĵ���ID
	 * @param srcID Դ�ĵ�ID
	 * @param destLibID Ŀ���ĵ���ID
	 * @param destID Ŀ���ĵ�ID
	 * @param associationCode ������
	 * @param order Ŀ���ĵ����
	 * @throws E5Exception
	 * @deprecated
	 */
	public void addAssociation( int srcLibID, long srcID, int destLibID,
			long destID, int associationCode, int order ) throws E5Exception;

	/**
	 * ���������ĵ���Ĺ�����ϵ
	 * 
	 * @param src Դ�ĵ�
	 * @param dest Ŀ���ĵ�
	 * @param associationCode ������
	 * @param order Ŀ���ĵ����
	 * @throws E5Exception
	 * @deprecated
	 */
	public void addAssociation( Document src, Document dest,
			int associationCode, int order ) throws E5Exception;

	/**
	 * ɾ��������ϵ
	 * 
	 * @param srcLibID
	 * @param srcID
	 * @param destLibID
	 * @param destID
	 * @param associationCode
	 * @return
	 * @deprecated
	 */
	public DocAssociation delAssociation( int srcLibID, long srcID,
			int destLibID, long destID, int associationCode );

	/**
	 * ȡ��һ��������еĹ�����ϵ
	 * 
	 * @param srcLibID
	 * @param srcID
	 * @param associationCode
	 * @return
	 * @throws E5Exception
	 * @deprecated
	 */
	public DocAssociation[] getAllAssociations( int srcLibID, long srcID,
			int associationCode ) throws E5Exception;

	// -------------------------------------------------------------------------

	/**
	 * �ڹ�����ϵ�������½ڵ�(�ĵ�)�滻�ɽڵ�
	 * 
	 * @param oldDocLibID ���ĵ���ID
	 * @param oldDocID ���ĵ�ID
	 * @param newDocLibID ���ĵ���ID
	 * @param newDocID ���ĵ�ID
	 * @throws E5Exception
	 * @created 2005-7-25 10:12:18
	 */
	public void replaceNode( int oldDocLibID, long oldDocID, int newDocLibID,
			long newDocID ) throws E5Exception;

}
