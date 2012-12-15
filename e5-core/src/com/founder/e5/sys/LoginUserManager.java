package com.founder.e5.sys;
import com.founder.e5.context.E5Exception;
import com.founder.e5.sys.LoginUser;
/**
 * @created 04-����-2005 16:01:14
 * @version 1.0
 * @updated 11-����-2005 13:04:29
 * Lu Zuowei modified 2006/4/4
 */
public interface LoginUserManager 
{
	/**
	 * ��¼LoginUser��¼��Fsys_LoginUser
	 * @param loginUser    ��¼�û�����ϢloginUser
	 */
	public void add(LoginUser login) throws E5Exception;
	
	/**
	 * ɾ��LoginUser��¼��Fsys_LoginUser	 * 
	 * ����ת�뵽�鵵���û���־����
	 * @param normal �Ƿ������˳�
	 */
	public void remove(int loginid,boolean normal) throws E5Exception;
	
	/**
	 * ɾ��LoginUser��¼��Fsys_LoginUserת�Ƶ�Fsys_LoginArchive��
	 * �Զ���Ϊ�������˳���ɾ������Ϊ����date��������Ĳ���
	 * @param date: ���ڸ�ʽ�������ϸ�Ϊ:2000-01-01 15:30:40.000
	 * @throws E5Exception
	 */
	public void remove(String date) throws E5Exception;
	/**
	 * ˢ��ʱ��Ĺ���
	 * @param loginid
	 * @throws E5Exception
	 * @return Ӱ������
	 */
	public int access(int loginid) throws E5Exception;
	
	/**
	 * ��õ�ǰ�ĵ�¼�û�
	 */
	public LoginUser[] get() throws E5Exception;

	/**
	 * ���ݵ�¼ID��ȡ��ǰ��¼�û���Ϣ
	 * @param loginid
	 * @return
	 * @throws E5Exception
	 */
	public LoginUser getById(int loginid) throws E5Exception;
	
	/**
	 * �õ����е�ϵͳ��¼�û���Ϣ,����һ�����ֶν�������
	 * ���������ӣ����û����Խ�������ĵ�¼�û����� 2006-03-06
	 */
	public LoginUser[] getBySort(String sortField) throws E5Exception;

	/**
	 * �õ����е�ϵͳ��¼�û���Ϣ,����һ�����ֶν�������
	 * ���������ӣ����û����Խ�������ĵ�¼�û����� 2006-03-06
	 */
	public LoginUser[] getBySort(String sortField,String sortBy) throws E5Exception;
	
	/**
	 * ���ָ���û�����ĵ�¼�û�
	 * @param userCode    �û���¼��
	 */
	public LoginUser[] get(String userCode) throws E5Exception;

	/**
	 * ���ָ���û��ĵ�ǰ��¼�û�
	 * @param userID    ��¼�û���ID
	 * 
	 */
	public LoginUser[] get(int userID) throws E5Exception;
	
	/**
	 * ��ȡָ���û��ĵ�ǰ��¼��
	 * @param userCode
	 * @param roleID
	 * @return
	 * @throws E5Exception
	 */
	public LoginUser get(String userCode, int roleID) throws E5Exception;
	/**
	 * ����ָ���û�����ɫȷ�ϵ�ǰ��¼״̬
	 * @param userCode
	 * @param roleID
	 * @param hostName
	 * @return
	 * @throws E5Exception
	 */
	public LoginUser get(String userCode, int roleID, String hostName) throws E5Exception;
}