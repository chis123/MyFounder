package com.founder.e5.dom;

/**
 * @version 1.0
 * @created 11-七月-2005 15:36:12
 */
public class View extends FolderView {

    private String viewFormula;

    /**
     * 返回视图公式
     * @return 视图公式
     */
    public String getViewFormula() {
        return viewFormula;
    }

    /**
     * 设置视图公式
     * @param viewFormula 视图公式
     */
    public void setViewFormula(String viewFormula) {
        this.viewFormula = viewFormula;
    }

    /* (non-Javadoc)
     * @see com.founder.e5.dom.FolderView#isFolder()
     */
    public boolean isFolder() {
       return false;
    }
    
    
}