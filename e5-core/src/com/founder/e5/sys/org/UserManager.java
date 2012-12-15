package com.founder.e5.sys.org;


import com.founder.e5.context.E5Exception;
import com.founder.e5.sys.org.User;
import com.founder.e5.sys.org.UserFolder;
import com.founder.e5.sys.org.UserReader;

/**
 * @created 04-����-2005 14:52:30
 * @version 1.0
 * @updated 11-����-2005 12:42:13
 */
public interface UserManager extends UserReader {

	/**
	 * ����û�
	 * @param user    �û�����
	 * 
	 */
	public void create(User user) throws E5Exception;

	/**
	 * �����û���Ϣ
	 * @param user    �û�����
	 * @throws HibernateException
	 * 
	 */
	public void update(User user) throws E5Exception;

	/**
	 * ɾ���û�
	 * @param userID    �û�ID
	 * @throws HibernateException
	 * 
	 */
	public void delete(int userID) throws E5Exception;

	/**
	 * �����û��ļ���
	 * @param userFolder    �����û��ļ���
	 * @throws HibernateException
	 * 
	 */
	public void create(UserFolder userFolder) throws E5Exception;

	/**
	 * �޸��û��ļ���
	 * @param userFolder    �޸��û��ļ���
	 * 
	 */
	public void update(UserFolder userFolder) throws E5Exception;

	/**
	 * ɾ���û��ļ���
	 * @param userFolder    �û��ļ��ж���
	 * 
	 */
	public void delete(UserFolder userFolder) throws E5Exception;



	/**
	 * �Բ������û�ID����ָ���û���������
	 * 
	 * @param userID �û�ID����
	 */
	public void sortUsers(int[] userID) throws E5Exception;

	/**
	 * �����û����������
	 * @param listener
	 */
	public void setListener(ManagerEventListener listener);
}