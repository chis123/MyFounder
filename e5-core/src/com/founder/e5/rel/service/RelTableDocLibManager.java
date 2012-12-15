/**********************************************************************
 * Copyright (c) 2002,北大方正电子有限公司电子出版事业部集成系统开发部
 * All rights reserved.
 * 
 * 摘要：
 * 
 * 当前版本：1.0
 * 作者： 张凯峰   Zhang_KaiFeng@Founder.Com
 * 完成日期：2006-3-24  18:42:08
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
     * 获取所有的已有的关联表和文档库的对应
     * 2006-3-24 18:43:06
     * @author zhang_kaifeng
     * @throws
     * @return 如果没有，则返回空疏组
     */
    public RelTableDocLibVO[] getRelTableDocLibs() throws E5Exception;
    
    /**
     * 获取指定的对应
     * 2006-3-24 18:46:04
     * @author zhang_kaifeng
     * @param docLibId
     * @param catTypeId
     * @return 如果没有，则返回null
     * @throws E5Exception
     */
    public RelTableDocLibVO getRelTableDocLib(int docLibId,int catTypeId)throws E5Exception;
    
    /**
     * 删除指定的对应
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
     * 保存对应关系
     * 2006-3-24 18:52:06
     * @author zhang_kaifeng
     * @param relTableDocLib
     * @throws E5Exception
     */
    public void saveRelTableDocLib(RelTableDocLibVO relTableDocLibVO) throws E5Exception;
    
    /**
     * 确认分类关联表是否与某文档库对应。
     * 2006-3-27 9:53:33
     * @author zhang_kaifeng
     * @param relTableId
     * @return
     */
    public boolean isRelTableReferenced(int relTableId);

}
