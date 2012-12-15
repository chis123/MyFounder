package com.founder.e5.dom;

import com.founder.e5.dom.facade.CacheManagerFacade;

/**
 * @version 1.0
 * @created 11-ÆßÔÂ-2005 15:29:43
 */
class FilterReaderImpl implements FilterReader {

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.FilterReader#getByTypeID(int)
     */
    public Filter[] getByTypeID(int doctypeID) {
        return this.getCache().getFilters(doctypeID);
    }

    private DocTypeCache getCache() {
        return (DocTypeCache) CacheManagerFacade.find(DocTypeCache.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.FilterReader#get(int)
     */
    public Filter get(int filterID) {
        return this.getCache().getFilter(filterID);
    }

}