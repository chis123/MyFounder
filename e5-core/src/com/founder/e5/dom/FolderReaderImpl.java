package com.founder.e5.dom;

import java.util.List;

import com.founder.e5.context.E5Exception;
import com.founder.e5.dom.facade.CacheManagerFacade;

/**
 * @created 2005-7-21 9:49:36
 * @author Zhang Kaifeng
 * @version
 */
class FolderReaderImpl implements FolderReader {

    private FolderViewCache getCache() {
        return (FolderViewCache) CacheManagerFacade.find(FolderViewCache.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.FolderReader#get(int)
     */
    public FolderView get(int fvID) {
        return (FolderView) this.getCache().getFV(fvID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.FolderReader#getFolders(int)
     */
    public FolderView[] getChildrenFVsByDocLib(int docLibID) throws E5Exception {

    	FolderViewCache c = (FolderViewCache) CacheManagerFacade.find(FolderViewCache.class);
        DocLib lib = c.getDocLib(docLibID);
        if (null == lib)
            throw new E5Exception("doclib not exist.");
        int folderID = lib.getFolderID();

        return c.getChildrenFVs(folderID);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.FolderReader#getSubs(int)
     */
    public FolderView[] getChildrenFVs(int folderID) {
        return this.getCache().getChildrenFVs(folderID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.FolderReader#getSubIDs(int)
     */
    public int[] getChildrenFVIDs(int folderID) {
        return this.getCache().getChildrenSubFVIDs(folderID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.FolderReader#getRules(int)
     */
    public Rule[] getRules(int folderID) {

        return getCache().getFVRules(folderID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.FolderReader#getRuleIDs(int)
     */
    public int[] getRuleIDs(int folderID) {
        return this.getCache().getFVRuleIDs(folderID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.FolderReader#getFilters(int)
     */
    public List getFilters(int folderID) {
        return this.getCache().getFVFilters(folderID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.FolderReader#getFilterIDs(int)
     */
    public List getFilterIDs(int folderID) {
        return this.getCache().getFVFilterIDs(folderID);
    }

    /* (non-Javadoc)
     * @see com.founder.e5.dom.FolderReader#getSubFolders(int)
     */
    public Folder[] getSubFolders(int folderID) {
        return this.getCache().getSubFolders(folderID);
    }

    /* (non-Javadoc)
     * @see com.founder.e5.dom.FolderReader#getSubFoldersID(int)
     */
    public int[] getSubFoldersID(int folderID) {
        return this.getCache().getSubFoldersID(folderID); 
    }

    /* (non-Javadoc)
     * @see com.founder.e5.dom.FolderReader#getSubFVs(int)
     */
    public FolderView[] getSubFVs(int folderID) {
        return this.getCache().getSubFVs(folderID);
    }

    /* (non-Javadoc)
     * @see com.founder.e5.dom.FolderReader#getRoot(int)
     */
    public Folder getRoot(int docLibID) throws E5Exception {
        return this.getCache().getFVRoot(docLibID);

    }

    /* (non-Javadoc)
     * @see com.founder.e5.dom.FolderReader#getFVs(int[])
     */
    public FolderView[] getFVs(int[] fvids) throws E5Exception {
        return this.getCache().getFVs(fvids);
    }

}