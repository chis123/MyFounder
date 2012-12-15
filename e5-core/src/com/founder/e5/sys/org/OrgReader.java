package com.founder.e5.sys.org;

import com.founder.e5.context.E5Exception;

/**
 * @created 04-����-2005 14:24:50
 * @version 1.0
 * @updated 11-����-2005 13:08:36
 */
public interface OrgReader {

	/**
	 * @created 04-����-2005 14:35:37
	 * @version 1.0
	 */

	/**
	 * �õ����е���֯
	 */
	public Org[] get() throws E5Exception;

	/**
	 * �õ�����ָ������֯
	 * @param orgID    orgID
	 * 
	 */
	public Org get(int orgID) throws E5Exception;

	/**
	 * ���ָ����֯����������֯
	 * @param orgID    ��֯����ID
	 * 
	 */
	public Org[] getChildOrgs(int orgID) throws E5Exception;

	/**
	 * ���ָ����֯��ֱ������֯
	 * @param orgID    ��֯����ID
	 * 
	 */
	public Org[] getNextChildOrgs(int orgID) throws E5Exception;

	/**
	 * ���ָ����֯�ĸ���֯
	 * @param orgID    ��֯����
	 * 
	 */
	public Org getParentOrg(int orgID) throws E5Exception;

	/**
	 * ���ָ���û���ָ�����͵���֯
	 * @param userID    �û�ID
	 * @param typeName    ��֯���͵�����
	 * 
	 */
	public Org getOrgByType(int userID, String typeName) throws E5Exception;

	/**
	 * ���ָ���û���ָ�����͵���֯
	 * @param userCode    �û�����
	 * @param typeName    ��֯���͵�����
	 * 
	 */
	public Org getOrgByType(String userCode, String typeName)
			throws E5Exception;

	/**
	 * ���ָ����֯�µ����н�ɫ
	 * @param orgID    ��֯ID
	 * 
	 */
	public Role[] getRolesByOrg(int orgID) throws E5Exception;

	/**
	 * �õ�������֯�����еĽ�ɫ
	 * 
	 * @param orgID ��֯ID
	 * @return ��ɫ����
	 * @throws E5Exception
	 */
	public Role[] getRoles(int orgID) throws E5Exception;

	//���������ӣ�ʵ�ֻ�����ѯ 2006-3-8
	/**
	 * ���ݻ�������ȡ��Ӧ�Ļ���
	 * @param orgName
	 * @return
	 * @throws E5Exception
	 */
	public Org[] getOrgsByName(String orgName) throws E5Exception;

	//���������ӣ�ʵ�ֻ�����ѯ 2006-3-8
	/**
	 * ���ݻ������е�ĳЩ�ʻ�ȡ��Ӧ�Ļ���
	 * @param orgName
	 * @return
	 * @throws E5Exception
	 */
	public Org[] getOrgsIncludeName(String orgName) throws E5Exception;

	/**
	 * ���ָ������������ȱʡ�ļ���
	 * @param orgID ����ID
	 */
	public DefaultFolder[] getDefaultFolders(int orgID) throws E5Exception;

	/**
	 * ���ָ������ID��ָ���ĵ������µ�ȱʡ�ļ���
	 * @param orgID ����ID
	 * @param docTypeID �ĵ�����ID
	 */
	public DefaultFolder getDefaultFolder(int orgID, int docTypeID)
	throws E5Exception;

	/**
	 * �������ȱʡ�ļ���
	 */
	public DefaultFolder[] getDefaultFolders() throws E5Exception;
}