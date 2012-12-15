package com.founder.e5.dom;

/**
 * @created 2005-7-18 13:24:43
 * @author Zhang Kaifeng
 */
public class Filter {

    private int filterID;

    private String filterName;

    private int docTypeID;

    private String formula;

    private String description;

    /**
     * 返回过滤器描述信息
     * @return 过滤器描述信息
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置过滤器描述
     * @param description 过滤器描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 返回文档类型ID 
     * @return 文档类型ID
     */
    public int getDocTypeID() {
        return docTypeID;
    }

    /**
     * 设置文档类型ID
     * @param doctypeID 文档类型ID
     */
    public void setDocTypeID(int doctypeID) {
        this.docTypeID = doctypeID;
    }

    /**
     * 返回过滤器ID
     * @return 过滤器ID
     */
    public int getFilterID() {
        return filterID;
    }

    void setFilterID(int filterID) {
        this.filterID = filterID;
    }

    /**
     * 返回过滤器名称 
     * @return 过滤器名称
     */
    public String getFilterName() {
        return filterName;
    }

    /**
     * 设置过滤器名称
     * @param filterName 过滤器名称
     */
    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    /**
     * 返回过滤器公式
     * @return 过滤器公式
     */
    public String getFormula() {
        return formula;
    }

    /**
     * 设置过滤器公式
     * @param formula 过滤器公式
     */
    public void setFormula(String formula) {
        this.formula = formula;
    }

    /**
     * 缺省构造函数
     */
    public Filter() {

    }

}