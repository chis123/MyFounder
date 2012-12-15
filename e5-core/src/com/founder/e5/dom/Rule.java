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
     * ȱʡ���캯��
     */
    public Rule() {

    }
    
    /**
     * �жϵ�ǰ�����Ƿ���һ��Javaƽ̨ʹ�õĹ���<br/>
     * E5ϵͳ�����java��.net����ƽ̨���С�<br/>
     * �����������Լ�ǰ׺"JAVA:"����ȷ��ʾΪjava������ʹ�õĹ���ǰ׺�����ִ�Сд��<br/>
     * ȱʡ���Բ���ǰ׺������java������
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
     * ���ع�������
     * @return ��������
     */
    public String getDescription() {
        return description;
    }

    /**
     * ���ù�������
     * @param description ��������
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * �����ĵ�����ID
     * @return �ĵ�����ID
     */
    public int getDocTypeID() {
        return docTypeID;
    }

    /**
     * �����ĵ�����ID
     * @param doctypeID �ĵ�����ID
     */
    public void setDocTypeID(int doctypeID) {
        this.docTypeID = doctypeID;
    }

    /**
     * ���ع������
     * @return �������
     */
    public String getRuleArguments() {
        return ruleArguments;
    }

    /**
     * ���ù������
     * @param ruleArguments �������
     */
    public void setRuleArguments(String ruleArguments) {
        this.ruleArguments = ruleArguments;
    }

    /**
     * ���ع�������
     * @return ��������
     */
    public String getRuleClassName() {
        return ruleClassName;
    }

    /**
     * ���ù�������
     * @param ruleClassName ��������
     */
    public void setRuleClassName(String ruleClassName) {
        this.ruleClassName = ruleClassName;
    }

    /**
     * ���ع���ID
     * @return ����ID
     */
    public int getRuleID() {
        return ruleID;
    }

    void setRuleID(int ruleID) {
        this.ruleID = ruleID;
    }

    /**
     * ���ع��򷽷�
     * @return ���򷽷�
     */
    public String getRuleMethod() {
        return ruleMethod;
    }

    /**
     * ���ù��򷽷���
     * @param ruleMethod ���򷽷���
     */
    public void setRuleMethod(String ruleMethod) {
        this.ruleMethod = ruleMethod;
    }

    /**
     * ���ع�������
     * @return ��������
     */
    public String getRuleName() {
        return ruleName;
    }

    /**
     * ���ù�������
     * @param ruleName ��������
     */
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }
}