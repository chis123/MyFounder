/**********************************************************************
 * Copyright (c) 2002,�������������޹�˾���ӳ�����ҵ������ϵͳ������
 * All rights reserved.
 * 
 * ժҪ��
 * 
 * ��ǰ�汾��1.0
 * ���ߣ� �ſ���   Zhang_KaiFeng@Founder.Com
 * ������ڣ�2006-3-24  18:22:15
 *  
 *********************************************************************/
package com.founder.e5.rel.model;



public class RelTableDocLib extends BaseObject {
    
	private static final long serialVersionUID = 5894569505250344838L;

	/**
     * �ĵ���ID
     */
    private Integer docLibId;
    
    /**
     * ��������ID
     */
    private Integer catTypeId;
    
    /**
     * ���������ID
     */
    private Integer relTableId;
    
    /**
     * ���ڲ������ֶ�
     */
    private String categoryField;
    
    /**
     * �Ƿ���Բ�ַ����ֶ�Ϊ��
     * Ϊ1������ԣ�ѡ��ignore�Ĵ�����ģ�壩������Ϊ0��ѡ��zero�Ĵ�����ģ�壩
     */
    private Integer ignoreFlag;
    
    /**
     * @return Returns the categoryField.
     */
    public String getCategoryField() {
        return categoryField;
    }

    /**
     * @param categoryField The categoryField to set.
     */
    public void setCategoryField(String categoryField) {
        this.categoryField = categoryField;
    }

    public RelTableDocLib() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @return Returns the catTypeId.
     */
    public Integer getCatTypeId() {
        return catTypeId;
    }

    /**
     * @param catTypeId The catTypeId to set.
     */
    public void setCatTypeId(Integer catTypeId) {
        this.catTypeId = catTypeId;
    }

    /**
     * @return Returns the docLibId.
     */
    public Integer getDocLibId() {
        return docLibId;
    }

    /**
     * @param docLibId The docLibId to set.
     */
    public void setDocLibId(Integer docLibId) {
        this.docLibId = docLibId;
    }
    /**
     * @return Returns the relTableId.
     */
    public Integer getRelTableId() {
        return relTableId;
    }

    /**
     * @param relTableId The relTableId to set.
     */
    public void setRelTableId(Integer relTableId) {
        this.relTableId = relTableId;
    }

    public RelTableDocLib(RelTableDocLibVO vo) {
        this.docLibId = new Integer(vo.getDocLibId());
        this.catTypeId = new Integer(vo.getCatTypeId());
        this.relTableId = new Integer(vo.getRelTableId());
        
        this.categoryField = vo.getCategoryField();
        this.ignoreFlag = new Integer(vo.getIgnoreFlag());
        
    }

    /**
     * @return Returns the ignoreFlag.
     */
    public Integer getIgnoreFlag() {
        return ignoreFlag;
    }

    /**
     * @param ignoreFlag The ignoreFlag to set.
     */
    public void setIgnoreFlag(Integer ignoreFlag) {
        this.ignoreFlag = ignoreFlag;
    }

}
