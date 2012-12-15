package com.founder.e5.permission;

import com.founder.e5.context.E5Exception;

/**
 * �ĵ������ֵ�Ȩ�޶�ȡ����
 * �������Ȩ�޶�ȡ������Ȩ�޶�ȡ��
 * @created 2008-4-21
 * @author Gong Lijie
 * @version 1.0
 */
public interface DomPermissionReader {

	String TAG_PERMISSION_RULE = "RulePermission";
	String TAG_PERMISSION_FILTER = "FilterPermission";

	/**
	 * ȡĳ��ɫû��Ȩ�޵Ĺ�����ID���顣
	 * ������ȱʡ����Ȩ�޵ģ����Ȩ�ޱ��м�¼����û��Ȩ�޵Ĺ�����ID��
	 * @param roleID
	 * @return û��Ȩ�޵Ĺ�����ID����
	 * @throws E5Exception
	 */
	int[] getRightlessFilters(int roleID) throws E5Exception;
	/**
	 * �ж�һ����ɫ��ĳ�������Ƿ���Ȩ��
	 * @param roleID
	 * @param filterID
	 * @return
	 * @throws E5Exception
	 */
	boolean hasFilterRight(int roleID, int filterID) throws E5Exception;
	
	/**
	 * ȡĳ��ɫ��Ȩ�޵Ĺ���ID����
	 * @param roleID
	 * @return ��Ȩ�޵Ĺ���ID����
	 * @throws E5Exception
	 */
	int[] getRules(int roleID) throws E5Exception;
	
	/**
	 * �ж�һ����ɫ��ĳ�����Ƿ���Ȩ��
	 * @param roleID
	 * @param ruleID
	 * @return
	 * @throws E5Exception
	 */
	boolean hasRuleRight(int roleID, int ruleID) throws E5Exception;
}
