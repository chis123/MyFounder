package com.founder.e5.dom;

import java.util.List;

import com.founder.e5.dom.facade.CacheManagerFacade;

/**
 * @version 1.0
 * @created 11-ÆßÔÂ-2005 15:36:29
 */
class ViewReaderImpl implements ViewReader {

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.ViewReader#get(int)
     */
    public View get(int viewID) {
        return (View) this.getCache().getFV(viewID);
    }

    private FolderViewCache getCache() {

        return (FolderViewCache) CacheManagerFacade.find(FolderViewCache.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.ViewReader#getFilterIDs(int)
     */
    public List getFilterIDs(int viewID) {
        return this.getCache().getFVFilterIDs(viewID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.ViewReader#getFilters(int)
     */
    public List getFilters(int viewID) {
        return this.getCache().getFVFilters(viewID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.ViewReader#getRuleIDs(int)
     */
    public int[] getRuleIDs(int viewID) {
        return this.getCache().getFVRuleIDs(viewID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.ViewReader#getRules(int)
     */
    public Rule[] getRules(int viewID) {
        return this.getCache().getFVRules(viewID);
    }

}