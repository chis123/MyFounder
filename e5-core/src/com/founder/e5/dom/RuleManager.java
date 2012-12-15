package com.founder.e5.dom;

import com.founder.e5.context.E5Exception;

/**
 * @version 1.0
 * @created 11-七月-2005 15:27:13
 */
public interface RuleManager extends RuleReader {

    /**
     * 创建Rule对象
     * 
     * @param rule
     *            约束：必须是一个实际的Rule对象，否则会抛出异常；id无需设置；必须设置rule名称，所属的文档类型ID；
     * @throws E5Exception
     */
    public void create(Rule rule) throws E5Exception;

    /**
     * 删除一个rule对象
     * 
     * @param ruleID
     * @throws E5Exception
     */
    public void delete(int ruleID) throws E5Exception;

    /**
     * 更新一个rule对象
     * 
     * @param rule
     *            rule参数必须指定已经存在的id，才能做更新操作
     * @throws E5Exception
     */
    public void update(Rule rule) throws E5Exception;
    
    /**
     * 获取指定的规则挂在了哪些文件夹下
     * 2006-3-2 13:38:17
     * @author zhang_kaifeng
     * @param ruleID
     * @return
     * @throws E5Exception
     */
    public FolderView[] getFolderViews(int ruleID) throws E5Exception;

}