package com.founder.e5.dom;

import com.founder.e5.context.E5Exception;
import com.founder.e5.dom.facade.CacheManagerFacade;

/**
 * @created 2005-7-20 10:43:41
 * @author Zhang Kaifeng
 * @version
 */
class DocLibReaderImpl implements DocLibReader {

    private FolderViewCache getCache() {
        return (FolderViewCache) CacheManagerFacade.find(FolderViewCache.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DocLibReader#getByTypeID(int)
     */
    public DocLib[] getByTypeID(int doctypeID) {
        return this.getCache().getDocLibsByTypeID(doctypeID);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DocLibReader#getByTypeName(java.lang.String)
     * 
     * @param doctypeName
     */
    public DocLib[] getByTypeName(String doctypeName) {
        return this.getCache().getDocLibsByTypeName(doctypeName);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DocLibReader#get(int)
     * 
     * @param docLibID
     */
    public DocLib get(int docLibID) {
        return this.getCache().getDocLib(docLibID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DocLibReader#getFlowTableInfo(int)
     */
    public DocLibAdditionals getFlowTableInfo(int docLibID) {
        return this.getCache().getFlowTableInfo(docLibID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DocLibReader#getIDsByTypeID(int)
     */
    public int[] getIDsByTypeID(int docTypeID) {
        return this.getCache().getDocLibIDs(docTypeID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DocLibReader#get(int[])
     */
    public DocLib[] get(int[] docLibIDs) {
        return this.getCache().getDocLibs(docLibIDs);
    }

    /* (non-Javadoc)
     * @see com.founder.e5.dom.DocLibReader#getDocTypeID(int)
     */
    public int getDocTypeID(int docLibId) throws E5Exception {
        return this.getCache().getDocTypeIDByLibID(docLibId);
    }

}