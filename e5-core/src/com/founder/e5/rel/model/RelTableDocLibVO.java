/**********************************************************************
 * Copyright (c) 2002,�������������޹�˾���ӳ�����ҵ������ϵͳ������
 * All rights reserved.
 * 
 * ժҪ��
 * 
 * ��ǰ�汾��1.0
 * ���ߣ� �ſ���   Zhang_KaiFeng@Founder.Com
 * ������ڣ�2006-3-24  18:57:30
 *  
 *********************************************************************/
package com.founder.e5.rel.model;

import com.founder.e5.cat.CatManager;
import com.founder.e5.cat.CatType;
import com.founder.e5.context.Context;
import com.founder.e5.context.E5Exception;
import com.founder.e5.dom.DocLib;
import com.founder.e5.dom.DocLibManager;
import com.founder.e5.rel.service.RelTableManager;

public class RelTableDocLibVO extends BaseObject {
    
	private static final long serialVersionUID = -8042800891446735546L;

    private String relatedFields = "";
    
    /**
     * �ĵ���ID
     */
    private int docLibId;
    
    /**
     * ��������ID
     */
    private int catTypeId;
    
    /**
     * ���������ID
     */
    private int relTableId;
    
    /**
     * �ĵ������
     */
    private DocLib docLib;
    
    /**
     * �������Ͷ���
     */
    private CatType catType;
    
    /**
     * ������������
     */
    private RelTable relTable;
    
    /**
     * ���ڲ������ֶ�
     */
    private String categoryField;

    /**
     * �Ƿ���Բ�ַ����ֶ�Ϊ��
     * Ϊ1������ԣ�ѡ��ignore�Ĵ�����ģ�壩������Ϊ0��ѡ��zero�Ĵ�����ģ�壩
     *
     */
    private int ignoreFlag;

    /**
     * @return Returns the catType.
     */
    public CatType getCatType() {
        return catType;
    }

    /**
     * @param catType The catType to set.
     */
    public void setCatType(CatType catType) {
        this.catType = catType;
    }

    /**
     * @return Returns the catTypeId.
     */
    public int getCatTypeId() {
        return catTypeId;
    }

    /**
     * @param catTypeId The catTypeId to set.
     */
    public void setCatTypeId(int catTypeId) {
        this.catTypeId = catTypeId;
    }

    /**
     * @return Returns the docLib.
     */
    public DocLib getDocLib() {
        return docLib;
    }

    /**
     * @param docLib The docLib to set.
     */
    public void setDocLib(DocLib docLib) {
        this.docLib = docLib;
    }

    /**
     * @return Returns the docLibId.
     */
    public int getDocLibId() {
        return docLibId;
    }

    /**
     * @param docLibId The docLibId to set.
     */
    public void setDocLibId(int docLibId) {
        this.docLibId = docLibId;
    }

    /**
     * @return Returns the relTable.
     */
    public RelTable getRelTable() {
        return relTable;
    }

    /**
     * @param relTable The relTable to set.
     */
    public void setRelTable(RelTable relTable) {
        this.relTable = relTable;
    }

    /**
     * @return Returns the relTableId.
     */
    public int getRelTableId() {
        return relTableId;
    }

    /**
     * @param relTableId The relTableId to set.
     */
    public void setRelTableId(int relTableId) {
        this.relTableId = relTableId;
    }

    public RelTableDocLibVO(int relTableId,int docLibId, int catTypeId,String categoryField,int ignoreFlag) {
        this.relTableId = relTableId;
        this.docLibId = docLibId;
        this.catTypeId = catTypeId;
        this.categoryField = categoryField;
        this.ignoreFlag = ignoreFlag;

    }
    
    public RelTableDocLibVO(RelTableDocLib po) {
        
        this.docLibId = po.getDocLibId().intValue();
        this.catTypeId = po.getCatTypeId().intValue();
        this.relTableId = po.getRelTableId().intValue();
        this.categoryField = po.getCategoryField();
        this.ignoreFlag = po.getIgnoreFlag().intValue();
        
        this.docLib = getDocLib(docLibId);
        this.catType = getCatType(catTypeId);
        this.relTable = getRelTable(relTableId);
       
    }

    public void refresh(){
        
        this.docLib = getDocLib(docLibId);
        this.catType = getCatType(catTypeId);
        this.relTable = getRelTable(relTableId);
    }

    private RelTable getRelTable(int relTableId){
        
        RelTableManager mgr = (RelTableManager) Context.getBean(RelTableManager.class);
        
        try {
			return mgr.getRelTable(relTableId);
		} catch (E5Exception e) {
			e.printStackTrace();
			return null;
		}
    }

    private CatType getCatType(int catTypeId) {
        
        CatManager catMgr = (CatManager) Context.getBean(CatManager.class);
        
        try{
            return catMgr.getType(catTypeId);
        } catch (E5Exception e){
        	e.printStackTrace();
            return null;
        }
    }

    private DocLib getDocLib(int docLibId) {
        
        DocLibManager libManager = (DocLibManager) Context.getBean(DocLibManager.class);
        try{
            return libManager.get(docLibId);
        } catch (E5Exception e){
        	e.printStackTrace();
            return null;
        }
    }

    public String getCategoryField() {
        return this.categoryField;
    }

    /**
     * @param categoryField The categoryField to set.
     */
    public void setCategoryField(String categoryField) {
        this.categoryField = categoryField;
    }

    /**
     * @return Returns the ignoreFlag.
     */
    public int getIgnoreFlag() {
        return ignoreFlag;
    }

    /**
     * @param ignoreFlag The ignoreFlag to set.
     */
    public void setIgnoreFlag(int ignoreFlag) {
        this.ignoreFlag = ignoreFlag;
    }

    /**
     * @return Returns the relatedFields.
     */
    public String getRelatedFields() {
        return relatedFields;
    }

    /**
     * @param relatedFields The relatedFields to set.
     */
    public void setRelatedFields(String relatedFields) {
        this.relatedFields = relatedFields;
    }
    
    

}
