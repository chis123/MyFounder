package com.founder.e5.sys.org;
import com.founder.e5.context.E5Exception;
import com.founder.e5.sys.org.Org;
import com.founder.e5.sys.org.OrgReader;

/**
 * @created 04-����-2005 14:35:37
 * @version 1.0
 * @updated 11-����-2005 12:40:35
 */
public interface OrgManager extends OrgReader {

	/**
	 * ������֯
	 * @param org    ��֯����
	 * 
	 */
	public void create(Org org) throws E5Exception;

	/**
	 * �޸���֯����
	 * @param org    ��֯����
	 * 
	 */
	public void update(Org org) throws E5Exception;

	/**
	 * ɾ����֯��ͬ������������֯
	 * 
	 * @param orgID    ��֯�����ID
	 * @param cascade  �Ƿ�ɾ������֯ true ɾ���� false ��ɾ��
	 */
	public void delete(int orgID, boolean cascade) throws E5Exception;

	/**
	 * �ƶ���ǰ��֯��������֯��
	 * @param srcOrgID    Դ��֯����ID
	 * @param destOrgID    Ŀ����֯����ID
	 * @param cascade   �Ƿ��������֯��true  �������е�����֯��false ������
	 */
	public void move(int srcOrgID, int destOrgID, boolean cascade) throws E5Exception;

	/**
	 * ������ָ������֯��������
	 * 
	 * @param orgIDs    ���������֯ID
	 */
	public void sortOrg(int[] orgIDs) throws E5Exception;

	/**
	 * ����ȱʡ�ļ���
	 * @param defaultFolder    ����ȱʡ�ļ���
	 * @throws E5Exception
	 * 
	 */
	public void create(DefaultFolder defaultFolder) throws E5Exception;

	/**
	 * �޸�ȱʡ�ļ���
	 * @param defaultFolder    �޸�ȱʡ�ļ���
	 * 
	 */
	public void update(DefaultFolder defaultFolder) throws E5Exception;

	/**
	 * ɾ��ȱʡ�ļ���
	 * @param defaultFolder    ȱʡ�ļ��ж���
	 * 
	 */
	public void delete(DefaultFolder defaultFolder) throws E5Exception;

}