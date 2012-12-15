package com.founder.e5.dom;

import com.founder.e5.commons.StringUtils;

/**
 * @created 2005-7-18 13:11:54
 * @author Zhang Kaifeng
 */
public class Rule {

    private int ruleID;

    private String ruleName;

    private int docTypeID;

    private String ruleClassName;

    private String ruleArguments;

    private String ruleMethod;

    private String description;

    /**
     * 缺省构造函数
     */
    public Rule() {

    }
    
    /**
     * 判断当前规则是否是一个Java平台使用的规则。<br/>
     * E5系统允许跨java和.net两个平台运行。<br/>
     * 规则类名可以加前缀"JAVA:"来明确表示为java环境下使用的规则，前缀不区分大小写。<br/>
     * 缺省可以不加前缀，当作java规则处理
     * @return
     */
    public boolean isJavaRule()
    {
    	return StringUtils.isForJava(this.ruleClassName);
    }
    
    public String toString()
    {
		return (new StringBuffer()
				.append("[ruleID:").append(ruleID)
				.append(",ruleName:").append(ruleName)
				.append(",docTypeID:").append(docTypeID)
				.append(",ruleClassName:").append(ruleClassName)
				.append(",ruleArguments:").append(ruleArguments)
				.append(",ruleMethod:").append(ruleMethod)
				.append(",description:").append(description)
				.append("]")
				).toString();
    	
    }
    /**
     * 返回规则描述
     * @return 规则描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置规则描述
     * @param description 规则描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 返回文档类型ID
     * @return 文档类型ID
     */
    public int getDocTypeID() {
        return docTypeID;
    }

    /**
     * 设置文档类型ID
     * @param doctypeID 文档类型ID
     */
    public void setDocTypeID(int doctypeID) {
        this.docTypeID = doctypeID;
    }

    /**
     * 返回规则参数
     * @return 规则参数
     */
    public String getRuleArguments() {
        return ruleArguments;
    }

    /**
     * 设置规则参数
     * @param ruleArguments 规则参数
     */
    public void setRuleArguments(String ruleArguments) {
        this.ruleArguments = ruleArguments;
    }

    /**
     * 返回规则类名
     * @return 规则类名
     */
    public String getRuleClassName() {
        return ruleClassName;
    }

    /**
     * 设置规则类名
     * @param ruleClassName 规则类名
     */
    public void setRuleClassName(String ruleClassName) {
        this.ruleClassName = ruleClassName;
    }

    /**
     * 返回规则ID
     * @return 规则ID
     */
    public int getRuleID() {
        return ruleID;
    }

    void setRuleID(int ruleID) {
        this.ruleID = ruleID;
    }

    /**
     * 返回规则方法
     * @return 规则方法
     */
    public String getRuleMethod() {
        return ruleMethod;
    }

    /**
     * 设置规则方法名
     * @param ruleMethod 规则方法名
     */
    public void setRuleMethod(String ruleMethod) {
        this.ruleMethod = ruleMethod;
    }

    /**
     * 返回规则名称
     * @return 规则名称
     */
    public String getRuleName() {
        return ruleName;
    }

    /**
     * 设置规则名称
     * @param ruleName 规则名称
     */
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }
}