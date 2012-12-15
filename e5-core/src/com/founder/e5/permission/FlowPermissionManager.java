package com.founder.e5.permission;
import com.founder.e5.context.E5Exception;
import com.founder.e5.flow.Proc;

/**
 * ϵͳ����������Ȩ�����úͶ�ȡ�Ĺ���ӿ� ȡ��ɫȨ��ʱ�����̳С�
 * @created 14-7-2005 16:24:03
 * @author Gong Lijie
 * @version 1.0
 */
public interface FlowPermissionManager extends FlowPermissionReader {

	/**
	 * ��������Ȩ�ޣ��������̽ڵ�Ȩ�޺ͷ����̲���Ȩ��
	 * 
	 * @param permission    permission
	 * @exception E5Exception
	 */
	public void save(FlowPermission permission)
	throws E5Exception;

	/**
	 * ��������Ȩ�ޣ��������̽ڵ�Ȩ�޺ͷ����̲���Ȩ��
	 * 
	 * @param permissionArray    permissionArray
	 * @exception E5Exception
	 */
	public void save(FlowPermission[] permissionArray)
	throws E5Exception;

	/**
	 * ȡ��ĳ���ĵ����������з����̲���������Ȩ��
	 * @param docTypeID
	 * @return
	 * @throws E5Exception
	 */
	public FlowPermission[] getUnflowPermissionsByDocType(int docTypeID)
	throws E5Exception;

	/**
	 * ȡ��ĳ���̵�����Ȩ�ޣ����������������̽ڵ��Ȩ��
	 * @param flowID
	 * @return
	 * @throws E5Exception
	 */
	public FlowPermission[] getPermissionsByFlow(int flowID)
	throws E5Exception;
	
	/**
	 * ȡ��ĳ���̽ڵ������Ȩ��
	 * @param flowID
	 * @param flowNodeID
	 * @return
	 * @throws E5Exception
	 */
	public FlowPermission[] getPermissionsByFlowNode(int flowID, int flowNodeID)
	throws E5Exception;

	/**
	 * ɾ���ĵ�����ʱ���ѷ�����Ȩ��ɾ��
	 * 
	 * @param docTypeID    �ĵ�����ID
	 * @exception E5Exception
	 */
	public void deleteDocType(int docTypeID)
	throws E5Exception;

	/**
	 * ɾ��һ������ʱ�����������̲���Ȩ��ɾ��
	 * 
	 * @param flowID     ����ID
	 * @exception E5Exception
	 */
	public void deleteFlow(int flowID)
	throws E5Exception;

	/**
	 * ɾ��һ�����̽ڵ�ʱ�������нڵ����Ȩ��ɾ��
	 * @param flowID ����ID
	 * @param flowNodeID    ���̽ڵ�ID
	 * @exception E5Exception
	 */
	public void deleteFlowNode(int flowID, int flowNodeID)
	throws E5Exception;

	/**
	 * ɾ��һ������ģ���ʱ�򣬰Ѷ�Ӧ��Ȩ��ɾ��
	 * 
	 * @param proc ����ģ�飬�������κη����̲��������̲���
	 * @exception E5Exception
	 */
	public void deleteProc(Proc proc)
	throws E5Exception;

	/**
	 * �÷����ѷϳ�
	 * ��Ϊɾ����ɫʱ����Ȩ�޶�ɾ������������Ȩ��
	 * Ӧ�õ���PermissionManager.deleteRole
	 * @deprecated
	 * @param roleID
	 * @throws E5Exception
	 */
	public void deleteRole(int roleID)
	throws E5Exception;
}