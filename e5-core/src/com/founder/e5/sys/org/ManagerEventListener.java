package com.founder.e5.sys.org;

import com.founder.e5.context.E5Exception;

/**
 * <p>�������¼��ļ�����</p>
 * <p>���ڹ��������ӡ��޸ġ�ɾ���¼��ļ���,���ڶ���Щ���ܵ���չ֧��</p>
 * ��Ҫÿ��Manager�ṩע��Listener�ķ���,��ʵ���¼��ĵ���
 * 
 * ����Ŀ�����Ҫ���������Listenerʱʵ��
 * 
 * @author wanghc
 * @created 2008-7-22 ����02:32:12
 * @see com.founder.e5.sys.org.UserManager
 * @see com.founder.e5.sys.org.UserManagerImpl
 */
public interface ManagerEventListener
{
	/**
	 * �����¼�
	 */
	public static final int EVENT_CREATE = 1;
	/**
	 * �����¼�
	 */
	public static final int EVENT_UPDATE = 2;
	/**
	 * ɾ���¼�
	 */
	public static final int EVENT_DELETE = 3;
	
	/**
	 * �����¼�
	 * 
	 * @param event - ��EVENT_XXX  ����
	 * @param object- �����Ķ���
	 * @throws E5Exception
	 */
	void doEvent(int event,Object object) throws E5Exception;
}
