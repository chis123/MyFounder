/**********************************************************************
 * Copyright (c) 2002,�������������޹�˾���ӳ�����ҵ������ϵͳ������
 * All rights reserved.
 * 
 * ժҪ��
 * 
 * ��ǰ�汾��1.0
 * ���ߣ� �ſ���   Zhang_KaiFeng@Founder.Com
 * ������ڣ�2006-3-24  18:42:08
 *  
 *********************************************************************/
package com.founder.e5.rel.service;

import com.founder.e5.rel.dao.RelTableDocLibFieldsDAO;
import com.founder.e5.rel.model.RelTableDocLibFields;

public interface RelTableDocLibFieldsManager {
    
    public void setDao(RelTableDocLibFieldsDAO dao);
    
    public RelTableDocLibFields[] getRelTableDocLibFields(int docLibId,int catTypeId);
    
    
    /**
     * ����ָ���Ķ���
     * 2006-3-27 9:15:17
     * @author zhang_kaifeng
     * @param relTableDocLib
     */
    public void save(RelTableDocLibFields fields);
    
    /**
     * ɾ��ָ���Ķ���
     * 2006-3-27 9:16:15
     * @author zhang_kaifeng
     * @param docLibId
     * @param catTypeId
     */
    public void remove(int docLibId,int catTypeId);
    
    public void removeFields(RelTableDocLibFields fields);

}
