package com.founder.e5.dom;

import com.founder.e5.dom.RuleParam;

/**
 * ����ʵ����Ľӿ�
 * <p>
 * ����ӿ�����ͳһ�淶�����ࡣ
 * ϵͳ�����ϸ��������еĹ����඼ʵ�ָýӿڣ�
 * ֻҪʵ�ִ�һ������ΪRuleParam�Ĳ����ķ������ɡ�
 * ��Ϊ"Rule"��������ʾ��������࣬�������������ϸļ�ǰ׺"I"
 * </p>
 * @created 2006-4-21
 * @author Gong Lijie
 * @version 1.0
 */
public interface IRule {
//	String ProcessRule(int DocTypeID, int DocLibID, int FVID, int bFV,
//				int UserID, int RoleID, int Level, String VarParam, int permissionCode, 
//				String viewFormula);
	String execute(RuleParam param);
}
