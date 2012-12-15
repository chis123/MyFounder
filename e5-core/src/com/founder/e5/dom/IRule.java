package com.founder.e5.dom;

import com.founder.e5.dom.RuleParam;

/**
 * 规则实现类的接口
 * <p>
 * 这个接口用于统一规范规则类。
 * 系统并不严格限制所有的规则类都实现该接口，
 * 只要实现带一个类型为RuleParam的参数的方法即可。
 * 因为"Rule"已用来表示规则管理类，所以这里命名上改加前缀"I"
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
