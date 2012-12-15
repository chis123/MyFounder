package com.founder.e5.dom;

import java.io.Serializable;

/**
 * @created 2005-7-18 15:39:14
 * @author Zhang Kaifeng
 */
public class FVRules implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8706226020407830868L;

	private int fvID;

    private int ruleID;

    private int subIndex;

    public int getFvID() {
        return fvID;
    }

    public int getRuleID() {
        return ruleID;
    }

    public int getSubIndex() {
        return subIndex;
    }

    public void setFvID(int fvID) {
        this.fvID = fvID;
    }

    public void setRuleID(int ruleID) {
        this.ruleID = ruleID;
    }

    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        return super.equals(obj);
    }

    public int hashCode() {
        // TODO Auto-generated method stub
        return super.hashCode();
    }

    public void setSubIndex(int subIndex) {
        this.subIndex = subIndex;
    }

    public FVRules() {

    }

    /**
     * @param fvID
     * @param ruleID
     * @param subIndex
     */
    public FVRules(int fvID, int ruleID, int subIndex) {
        super();
        this.fvID = fvID;
        this.ruleID = ruleID;
        this.subIndex = subIndex;
    }
}