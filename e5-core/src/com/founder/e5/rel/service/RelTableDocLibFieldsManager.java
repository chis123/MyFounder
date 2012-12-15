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

import com.founder.e5.rel.dao.RelTableDocLibFieldsDAO;
import com.founder.e5.rel.model.RelTableDocLibFields;

public interface RelTableDocLibFieldsManager {
    
    public void setDao(RelTableDocLibFieldsDAO dao);
    
    public RelTableDocLibFields[] getRelTableDocLibFields(int docLibId,int catTypeId);
    
    
    /**
     * 保存指定的对象
     * 2006-3-27 9:15:17
     * @author zhang_kaifeng
     * @param relTableDocLib
     */
    public void save(RelTableDocLibFields fields);
    
    /**
     * 删除指定的对象
     * 2006-3-27 9:16:15
     * @author zhang_kaifeng
     * @param docLibId
     * @param catTypeId
     */
    public void remove(int docLibId,int catTypeId);
    
    public void removeFields(RelTableDocLibFields fields);

}
