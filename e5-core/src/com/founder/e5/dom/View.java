package com.founder.e5.dom;

/**
 * @version 1.0
 * @created 11-����-2005 15:36:12
 */
public class View extends FolderView {

    private String viewFormula;

    /**
     * ������ͼ��ʽ
     * @return ��ͼ��ʽ
     */
    public String getViewFormula() {
        return viewFormula;
    }

    /**
     * ������ͼ��ʽ
     * @param viewFormula ��ͼ��ʽ
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