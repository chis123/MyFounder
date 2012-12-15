package com.founder.e5.dom;

import java.util.List;

import com.founder.e5.context.E5Exception;

/**
 * @created 2005-7-21 14:19:15
 * @author Zhang Kaifeng
 * @version
 */
public interface ViewReader {

    /**
     * 获取指定ID的视图
     * 
     * @param viewID 视图ID
     * @return 如果视图不存在，则返回null
     * @throws E5Exception
     */
    public View get(int viewID) throws E5Exception;

    /**
     * 获取指定视图下的所有规则，排了顺序的
     * 
     * @param viewID 视图ID
     * @return 如果无规则，则返回空数组
     * @throws E5Exception
     */
    public Rule[] getRules(int viewID) throws E5Exception;

    /**
     * 获取指定视图下的过滤器，排了顺序的
     * 
     * @param viewID 视图ID
     * @return 如果视图下无过滤器，则返空控数组
     * @throws E5Exception
     */
    public List getFilters(int viewID) throws E5Exception;

    /**
     * 获取指定视图下的所有过滤器id，排了顺序的
     * 
     * @param viewID，视图ID
     * @return 如果视图下无过滤器，则返回空数组
     * @throws E5Exception
     */
    public List getFilterIDs(int viewID) throws E5Exception;

    /**
     * 获取指定视图下的所有规则id，排了顺序的
     * 
     * @param viewID 视图ID
     * @return 如果视图下无规则，则返回空数组
     * @throws E5Exception
     */
    public int[] getRuleIDs(int viewID) throws E5Exception;

}