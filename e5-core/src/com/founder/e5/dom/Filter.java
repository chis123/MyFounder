package com.founder.e5.dom;

/**
 * @created 2005-7-18 13:24:43
 * @author Zhang Kaifeng
 */
public class Filter {

    private int filterID;

    private String filterName;

    private int docTypeID;

    private String formula;

    private String description;

    /**
     * ���ع�����������Ϣ
     * @return ������������Ϣ
     */
    public String getDescription() {
        return description;
    }

    /**
     * ���ù���������
     * @param description ����������
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
     * ���ع�����ID
     * @return ������ID
     */
    public int getFilterID() {
        return filterID;
    }

    void setFilterID(int filterID) {
        this.filterID = filterID;
    }

    /**
     * ���ع��������� 
     * @return ����������
     */
    public String getFilterName() {
        return filterName;
    }

    /**
     * ���ù���������
     * @param filterName ����������
     */
    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    /**
     * ���ع�������ʽ
     * @return ��������ʽ
     */
    public String getFormula() {
        return formula;
    }

    /**
     * ���ù�������ʽ
     * @param formula ��������ʽ
     */
    public void setFormula(String formula) {
        this.formula = formula;
    }

    /**
     * ȱʡ���캯��
     */
    public Filter() {

    }

}