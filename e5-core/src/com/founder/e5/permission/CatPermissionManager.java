package com.founder.e5.permission;

import com.founder.e5.context.E5Exception;

/**
 * @created 14-7-2005 16:23:34
 * @author Gong Lijie
 * @version 1.0
 */
public interface CatPermissionManager extends CatPermissionReader {

	/**
	 * Ȩ�޵���������
	 * �����Ϊ��������ʱ�������е�Ȩ���������
	 * 
	 * @param permission Ȩ��
	 * @param incremental �Ƿ���������
	 */
	public void save(CatPermission permission, boolean incremental) throws E5Exception;

	/**
	 * ���Ȩ�޵���������
	 * �����Ϊ��������ʱ�������е�Ȩ���������
	 * 
	 * @param permissionArray Ȩ������
	 * @param incremental �Ƿ���������
	 */
	public void save(CatPermission[] permissionArray, boolean incremental) throws E5Exception;

	/**
	 * ɾ��һ������ʱɾ�����ж���Ȩ��
	 * 
	 * @param catType ��������
	 * @param catID ����
	 */
	public void deleteCat(int catType, int catID) throws E5Exception;

	/**
	 * ɾ��һ����������ʱɾ�����ж���Ȩ��
	 * 
	 * @param catType ��������
	 */
	public void deleteCatType(int catType) throws E5Exception;
}