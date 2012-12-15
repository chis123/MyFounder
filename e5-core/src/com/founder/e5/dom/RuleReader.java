package com.founder.e5.dom;

import com.founder.e5.context.E5Exception;

/**
 * @version 1.0
 * @created 11-七月-2005 15:27:01
 */
public interface RuleReader {

    /**
     * 根据ruleid获取Rule对象
     * 
     * @param ruleID 规则ID
     * @return 如果没有对应的Rule，则返回null
     * @throws E5Exception
     */
    public Rule get(int ruleID) throws E5Exception;

    /**
     * 依据文档类型id，获取其下所有的rule对象
     * 
     * @param docTypeID
     * @return 如果没有，则返回空数组
     * @throws E5Exception
     */
    public Rule[] getByDoctypeID(int docTypeID) throws E5Exception;

}