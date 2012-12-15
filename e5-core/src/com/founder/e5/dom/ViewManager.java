package com.founder.e5.dom;

import org.hibernate.Session;

import com.founder.e5.context.E5Exception;

/**
 * @version 1.0
 * @created 11-七月-2005 15:36:35
 */
public interface ViewManager extends ViewReader {

    /**
     * 创建视图
     * 
     * @param view
     *            id将在方法内设置
     * @throws E5Exception
     */
    public int create(View view,FVRules[] fvRules,FVFilters[] fvFilters) throws E5Exception;

    /**
     * 删除视图，如果视图不存在，则什么也不做
     * 
     * @param viewID
     * @throws E5Exception
     */
    public void delete(int viewID) throws E5Exception;

    public void create(View view,Session session) throws E5Exception;
    
    void delete(int viewID, Session ss) throws E5Exception;
    
    public void update(View view,FVRules[] fvRules,FVFilters[] fvFilters) throws E5Exception;
    
}