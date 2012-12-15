package com.founder.e5.dom;

import java.io.Serializable;

/**
 * @created 2005-7-18 16:06:19
 * @author Zhang Kaifeng
 */
public class FVFilters implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3009112174409961156L;

	private int defaultFlag;

    private int docTypeID;

    private int filterID;

    private int fvID;

    private int index;

    public int getDefaultFlag() {
        return defaultFlag;
    }

    public int getDocTypeID() {
        return docTypeID;
    }

    public int getFilterID() {
        return filterID;
    }

    public int getFvID() {
        return fvID;
    }

    public int getIndex() {
        return index;
    }

    public void setDefaultFlag(int defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    public void setDocTypeID(int docTypeID) {
        this.docTypeID = docTypeID;
    }

    public void setFilterID(int filterID) {
        this.filterID = filterID;
    }

    public void setFvID(int fvID) {
        this.fvID = fvID;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public FVFilters() {

    }

    /**
     * @param fvID
     * @param filterID
     * @param docTypeID
     * @param defaultFlag
     * @param index
     */
    public FVFilters(int fvID, int filterID, int docTypeID, int defaultFlag,
            int index) {
        super();
        this.fvID = fvID;
        this.filterID = filterID;
        this.docTypeID = docTypeID;
        this.defaultFlag = defaultFlag;
        this.index = index;
    }

    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        return super.equals(obj);
    }

    public int hashCode() {
        // TODO Auto-generated method stub
        return super.hashCode();
    }

}