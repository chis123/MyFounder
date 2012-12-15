package com.founder.e5.sso;
import com.founder.e5.context.E5Exception;
import com.founder.e5.sys.org.Role;

/**
 * E5ϵͳ�û���֤����
 * @author LuZuowei
 */
public interface SSO 
{
	/**
	 * ��֤�û�������Ĺ���
	 * @param usercode
	 * @param password
	 * @return 0:�û���������ȷ��-1�û������ڣ�-2��ʾ�û����벻��ȷ
	 */
	public int verifyUserPassword(String usercode,String password) throws E5Exception;
	
	/**
	 * ���ص�ǰ�û�������Ч��ɫ
	 * @param usercode
	 * @return ȷ������Ч�Ľ�ɫ�����ʱ�䷶Χ��
	 */
	public Role[] getValidRole(String usercode) throws E5Exception;
	
	/**
	 * �Ǽ�ע��Ĺ���
	 * @param usercode
	 * @param roleID
	 * @param hostname
	 * @param servername
	 * @return ��������һ���Ƕ�ά��
	 * 	[0]:��loginid��ֵ��-1��ʾʧ�ܣ�>0��ʾ�ɹ�
	 *  [1]:"samemachine"��ʾ��ͳһ�����ϵ�¼����������ǵ�¼����������
	 */
	public String[] login(String usercode,int roleID,String hostname,String servername,boolean concurrent) throws E5Exception;
	
	/**
	 * ȷ��������ʱ��
	 * @param loginid
	 * @return �����Ϣ��0����ʾ�ɹ���-1:��ʾ��ɫ������Чʱ���ڣ�-2:��ʾϵͳ����Ա������
	 */
	public int accessLast(int loginid,int userid,int roleid) throws E5Exception; 
	
	/**
	 * �����˳��Ĺ���
	 * @param loginid
	 */
	public void logout(int loginid) throws E5Exception;
}
