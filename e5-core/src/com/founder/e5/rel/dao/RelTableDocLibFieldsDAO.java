/**********************************************************************
 * Copyright (c) 2002,�������������޹�˾���ӳ�����ҵ������ϵͳ������
 * All rights reserved.
 * 
 * ժҪ��
 * 
 * ��ǰ�汾��1.0
 * ���ߣ� �ſ���   Zhang_KaiFeng@Founder.Com
 * ������ڣ�2006-3-24  18:44:56
 *  
 *********************************************************************/
package com.founder.e5.rel.dao;

import com.founder.e5.rel.model.RelTableDocLibFields;

public interface RelTableDocLibFieldsDAO extends DAO {
    
    /**
     * ��ȡָ���Ķ���
     * 2006-3-27 9:12:22
     * @author zhang_kaifeng
     * @param docLibId
     * @param catTypeId
     * @return ���û���򷵻�null
     */
    public RelTableDocLibFields[] getRelTableDocLibFields(Integer docLibId,Integer catTypeId);
    
   
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
    public void remove(Integer docLibId,Integer catTypeId);
    
    public void removeFields(RelTableDocLibFields fields);
    

}
