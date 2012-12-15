package com.founder.e5.dom;

import com.founder.e5.context.E5Exception;

/**
 * @version 1.0
 * @created 11-七月-2005 15:30:44
 */
public interface FilterManager extends FilterReader {

    /**
     * 创建Filter记录
     * 
     * @param filter
     *            必须是新创建的Filter对象，否则抛出异常
     * @throws E5Exception
     */
    public void create(Filter filter) throws E5Exception;

    /**
     * 删除某一过滤器
     * 
     * @param filterID
     *            如果该过滤器不存在，则什么也不发生
     * @throws E5Exception
     */
    public void delete(int filterID) throws E5Exception;

    /**
     * 更新某一过滤器
     * 
     * @param filter
     * @throws E5Exception
     */
    public void update(Filter filter) throws E5Exception;
    
    /**
     * 查看过滤器挂在了哪些文件夹上
     * 2006-3-2 9:52:36
     * @author zhang_kaifeng
     * @param filterID
     * @return
     * @throws E5Exception
     */
    public FolderView[] getFolderViews(int filterID) throws E5Exception;

}