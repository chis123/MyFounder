/**********************************************************************
 * Copyright (c) 2002,北大方正电子有限公司电子出版事业部集成系统开发部
 * All rights reserved.
 * 
 * 摘要：
 * 
 * 当前版本：1.0
 * 作者： 张凯峰   Zhang_KaiFeng@Founder.Com
 * 完成日期：2006-3-24  18:22:15
 *  
 *********************************************************************/
package com.founder.e5.rel.model;



public class RelTableDocLib extends BaseObject {
    
	private static final long serialVersionUID = 5894569505250344838L;

	/**
     * 文档库ID
     */
    private Integer docLibId;
    
    /**
     * 分类类型ID
     */
    private Integer catTypeId;
    
    /**
     * 分类关联表ID
     */
    private Integer relTableId;
    
    /**
     * 用于拆分类的字段
     */
    private String categoryField;
    
    /**
     * 是否忽略拆分分类字段为空
     * 为1代表忽略（选择ignore的触发器模板），否则为0（选择zero的触发器模板）
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
