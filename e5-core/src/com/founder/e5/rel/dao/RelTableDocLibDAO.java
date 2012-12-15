/**********************************************************************
 * Copyright (c) 2002,北大方正电子有限公司电子出版事业部集成系统开发部
 * All rights reserved.
 * 
 * 摘要：
 * 
 * 当前版本：1.0
 * 作者： 张凯峰   Zhang_KaiFeng@Founder.Com
 * 完成日期：2006-3-24  18:44:56
 *  
 *********************************************************************/
package com.founder.e5.rel.dao;

import java.util.List;

import com.founder.e5.context.E5Exception;
import com.founder.e5.rel.model.RelTableDocLib;

public interface RelTableDocLibDAO extends DAO {
    
    /**
     * 获取指定的对象
     * 2006-3-27 9:12:22
     * @author zhang_kaifeng
     * @param docLibId
     * @param catTypeId
     * @return 如果没有则返回null
     */
    public RelTableDocLib getRelTableDocLib(Integer docLibId,Integer catTypeId);
    
    /**
     * 获取所有的对象
     * 2006-3-27 9:13:15
     * @author zhang_kaifeng
     * @return 如果没有则返回空列表
     * @throws E5Exception 
     */
    public List getRelTableDocLibs() throws E5Exception;
    
    /**
     * 保存指定的对象
     * 2006-3-27 9:15:17
     * @author zhang_kaifeng
     * @param relTableDocLib
     */
    public void save(RelTableDocLib relTableDocLib);
    
    /**
     * 删除指定的对象
     * 2006-3-27 9:16:15
     * @author zhang_kaifeng
     * @param docLibId
     * @param catTypeId
     */
    public void remove(Integer docLibId,Integer catTypeId);
    
    

}
