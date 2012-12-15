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

import java.sql.SQLException;

import com.founder.e5.context.E5Exception;
import com.founder.e5.rel.dao.RelTableDocLibDAO;
import com.founder.e5.rel.model.RelTableDocLibVO;

public interface RelTableDocLibManager {
    
    public void setDao(RelTableDocLibDAO dao);
    
    /**
     * ��ȡ���е����еĹ�������ĵ���Ķ�Ӧ
     * 2006-3-24 18:43:06
     * @author zhang_kaifeng
     * @throws
     * @return ���û�У��򷵻ؿ�����
     */
    public RelTableDocLibVO[] getRelTableDocLibs() throws E5Exception;
    
    /**
     * ��ȡָ���Ķ�Ӧ
     * 2006-3-24 18:46:04
     * @author zhang_kaifeng
     * @param docLibId
     * @param catTypeId
     * @return ���û�У��򷵻�null
     * @throws E5Exception
     */
    public RelTableDocLibVO getRelTableDocLib(int docLibId,int catTypeId)throws E5Exception;
    
    /**
     * ɾ��ָ���Ķ�Ӧ
     * 2006-3-24 18:46:50
     * @author zhang_kaifeng
     * @param docLibId
     * @param catTypeId
     * @throws E5Exception
     * @throws SQLException 
     * @throws Exception 
     */
    public void removeRelTableDocLib(int docLibId,int catTypeId) throws Exception;
    
    /**
     * �����Ӧ��ϵ
     * 2006-3-24 18:52:06
     * @author zhang_kaifeng
     * @param relTableDocLib
     * @throws E5Exception
     */
    public void saveRelTableDocLib(RelTableDocLibVO relTableDocLibVO) throws E5Exception;
    
    /**
     * ȷ�Ϸ���������Ƿ���ĳ�ĵ����Ӧ��
     * 2006-3-27 9:53:33
     * @author zhang_kaifeng
     * @param relTableId
     * @return
     */
    public boolean isRelTableReferenced(int relTableId);

}
