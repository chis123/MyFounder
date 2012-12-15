package com.founder.e5.dom;

import com.founder.e5.dom.facade.CacheManagerFacade;

/**
 * @created 2005-7-21 10:31:59
 * @author Zhang Kaifeng
 * @version
 */
class RuleReaderImpl implements RuleReader {

    private DocTypeCache getCache() {
        return (DocTypeCache) CacheManagerFacade.find(DocTypeCache.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.RuleReader#get(int)
     */
    public Rule get(int ruleID) {
        return this.getCache().getRule(ruleID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.RuleReader#getByDoctypeID(int)
     */
    public Rule[] getByDoctypeID(int doctypeId) {
        return this.getCache().getRules(doctypeId);
    }

}