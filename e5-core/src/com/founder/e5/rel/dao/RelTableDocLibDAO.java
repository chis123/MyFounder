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

import java.util.List;

import com.founder.e5.context.E5Exception;
import com.founder.e5.rel.model.RelTableDocLib;

public interface RelTableDocLibDAO extends DAO {
    
    /**
     * ��ȡָ���Ķ���
     * 2006-3-27 9:12:22
     * @author zhang_kaifeng
     * @param docLibId
     * @param catTypeId
     * @return ���û���򷵻�null
     */
    public RelTableDocLib getRelTableDocLib(Integer docLibId,Integer catTypeId);
    
    /**
     * ��ȡ���еĶ���
     * 2006-3-27 9:13:15
     * @author zhang_kaifeng
     * @return ���û���򷵻ؿ��б�
     * @throws E5Exception 
     */
    public List getRelTableDocLibs() throws E5Exception;
    
    /**
     * ����ָ���Ķ���
     * 2006-3-27 9:15:17
     * @author zhang_kaifeng
     * @param relTableDocLib
     */
    public void save(RelTableDocLib relTableDocLib);
    
    /**
     * ɾ��ָ���Ķ���
     * 2006-3-27 9:16:15
     * @author zhang_kaifeng
     * @param docLibId
     * @param catTypeId
     */
    public void remove(Integer docLibId,Integer catTypeId);
    
    

}
