package com.founder.e5.permission;

import com.founder.e5.context.E5Exception;
import com.founder.e5.flow.Proc;

/**
 * ����Ȩ�޵Ķ�ȡ��
 * @created 14-7-2005 16:24:08
 * @author Gong Lijie
 * @version 1.0
 */
public interface FlowPermissionReader {

	/**
	 * ȡһ����ɫ��ĳ���̽ڵ�Ȩ�ޡ�
	 * �����ڹ���ƽ̨�С�
	 * 
	 * @param roleID    ��ɫID
	 * @param flowNodeID    �ڵ�ID
	 * @exception E5Exception E5Exception
	 */
	public int get(int roleID, int flowID, int flowNodeID)
	throws E5Exception;

	/**
	 * ȡһ����ɫ��ĳ�ĵ����͵ķ����̲���Ȩ��
	 * @param roleID    ��ɫID
	 * @param docTypeID    �ĵ�����ID
	 * @exception E5Exception E5Exception
	 */
	public int getUnflowPermission(int roleID, int docTypeID)
	throws E5Exception;

	/**
	 * ȡһ����ɫ�����з����̲�����Ȩ��
	 * 
	 * @param roleID    ��ɫID
	 * @exception E5Exception E5Exception
	 */
	public FlowPermission[] getUnflowPermissionsByRole(int roleID)
	throws E5Exception;

	/**
	 * ȡһ����ɫ�д���Ȩ�޵���������
	 * <br/>�����ý�ɫ�ڴ����ĵ�ʱ������ѡ����ĵ����������
	 * @param roleID    ��ɫID
	 * <br/>�磺ȡ��ɫ5���½�Ȩ�޵��������� getFlowsNewable(5)
	 * @exception E5Exception E5Exception
	 */
	public int[] getFlowsNewable(int roleID)
	throws E5Exception;

	/**
	 * ȡ��ĳ���̲���Ȩ�޵����н�ɫ
	 * @param flowID ����ID
	 * @param flowNodeID    ���̽ڵ�ID
	 * @param permissionArray    Ȩ�ޱ�� 
	 * <br/>Ȩ�ޱ����һ�����飬ֻҪӵ�и��������г�������һ��Ȩ�ޱ�ǣ�����Ϊƥ��ɹ������Է��ظý�ɫID��
	 * <br/>���������Ϊ��ʱ���κ�Ȩ�޶��ɡ�
	 * <br/>�磺ȡ��"�ύ"Ȩ�޵����н�ɫ getRolesOfFlow(5,{1})
	 * @exception E5Exception E5Exception
	 */
	public int[] getRolesOfFlowNode(int flowID, int flowNodeID, int[] maskArray)
	throws E5Exception;

	/**
	 * ȡһ����ɫ��һ�����̵�����Ȩ��
	 * 
	 * @param roleID    ��ɫID
	 * @param flowID    ����ID
	 * @exception E5Exception E5Exception
	 */
	public FlowPermission[] getPermissionsByRole(int roleID, int flowID)
	throws E5Exception;

	/**
	 * ȡһ����ɫ����������Ȩ��
	 * 
	 * @param roleID    ��ɫID
	 * @exception E5Exception E5Exception
	 */
	public FlowPermission[] getPermissionsByRole(int roleID)
	throws E5Exception;
	
	/**
	 * �жϽ�ɫ��ĳ���̲����Ƿ���Ȩ��
	 * 
	 * @param roleID    ��ɫID
	 * @param flowID	����ID
	 * @param flowNodeID    ���̽ڵ�ID
	 * @param procType    ��������
	 * @param procID    ����ID 
	 * @return boolean
	 * @throws E5Exception
	 */
	public boolean hasPermission(int roleID, int flowID, int flowNodeID, 
			int procType, int procID)
	throws E5Exception;

	/**
	 * �жϽ�ɫ��ĳ���̲����Ƿ���Ȩ��
	 * 
	 * @param roleID    ��ɫID
	 * @param flowID	����ID
	 * @param flowNodeID    ���̽ڵ�ID
	 * @param procName    ������ 
	 * @return boolean
	 * @throws E5Exception
	 */
	public boolean hasPermission(int roleID, int flowID, int flowNodeID, String procName)
	throws E5Exception;
	
	/**
	 * �жϽ�ɫ��ĳ���̲����Ƿ���Ȩ��
	 * @param roleID ��ɫID
	 * @param proc   ���̲�������
	 * @return boolean
	 * @throws E5Exception
	 */
	public boolean hasPermission(int roleID, Proc proc)
	throws E5Exception;
	/**
	 * �жϽ�ɫ��ĳ�����̲����Ƿ���Ȩ��
	 * 
	 * @param roleID    ��ɫID
	 * @param procID    ����ID 
	 * @return boolean
	 * @throws E5Exception
	 */
	public boolean hasUnflowPermission(int roleID, int procID)
	throws E5Exception;

	/**
	 * �жϽ�ɫ��ĳ�����̲����Ƿ���Ȩ��
	 * 
	 * @param roleID    ��ɫID
	 * @param docTypeID    �ĵ�����ID
	 * @param procName    ������  
	 * @return boolean
	 * @throws E5Exception
	 */
	public boolean hasUnflowPermission(int roleID, int docTypeID, String procName)
	throws E5Exception;
	
	/**
	 * �жϽ�ɫ��ĳ�����̲����Ƿ���Ȩ��
	 * @param roleID ��ɫID
	 * @param proc   �����̲�������
	 * @return boolean
	 * @throws E5Exception
	 */
	public boolean hasUnflowPermission(int roleID, Proc proc)
	throws E5Exception;
}