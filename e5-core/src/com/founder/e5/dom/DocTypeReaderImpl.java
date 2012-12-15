package com.founder.e5.dom;

import com.founder.e5.context.E5Exception;


/**
 * @created 2005-7-20 16:13:31
 * @author Zhang Kaifeng
 * @version
 */
class DocTypeReaderImpl implements DocTypeReader {
    
    /**
     * 
     */
    private ContextCacheManager cacheManager;
    
    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DocTypeReader#get(int)
     */
    public DocType get(int docTypeID) {
        return this.getCache().getDocType(docTypeID);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DocTypeReader#get(java.lang.String)
     */
    public DocType get(String doctypeName) {
        return this.getCache().getDocType(doctypeName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DocTypeReader#getAppID(int)
     */
    public int getAppID(int docTypeID) {
        return this.getCache().getAppID(docTypeID);
    }

    /**
     * 2006-1-10 19:10:46
     * @author zhang_kaifeng
     * @return
     */
    private DocTypeCache getCache() {
        return (DocTypeCache) cacheManager.find(DocTypeCache.class);
    }

    /* (non-Javadoc)
     * @see com.founder.e5.dom.DocTypeReader#getField(int)
     */
    public DocTypeField getField(int fieldID) {
        return this.getCache().getField(fieldID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DocTypeReader#getFieldName(int)
     */
    public String getFieldName(int fieldID) {
        return this.getCache().getFieldName(fieldID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DocTypeReader#getFields(int)
     */
    public DocTypeField[] getFields(int typeID) {
        return this.getCache().getFields(typeID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DocTypeReader#getFieldsExt(int)
     */
    public DocTypeField[] getFieldsExt(int doctypeID) {
        return this.getCache().getFieldsExt(doctypeID);
    }

	public DocTypeField[] getFieldsApp(int docTypeID) throws E5Exception
	{
       return this.getCache().getFields(docTypeID, DocTypeField.FIELD_APPLICATION);
	}

	public DocTypeField[] getFieldsUser(int docTypeID) throws E5Exception
	{
       return this.getCache().getFields(docTypeID, DocTypeField.FIELD_USER);
	}

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DocTypeReader#getIDs(int)
     */
    public int[] getIDs(int appID) {
        return this.getCache().getIDs(appID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DocTypeReader#getTypes(int)
     */
    public DocType[] getTypes(int appID) {
        return this.getCache().getTypes(appID);
    }

    /**
     * @param cacheMgr
     */
    public DocTypeReaderImpl(ContextCacheManager cacheMgr){
        this.cacheManager = cacheMgr;
    }

    /** (non-Javadoc)
     * @see com.founder.e5.dom.DocTypeReader#getFieldsOrderBy(int, java.lang.String, java.lang.String)
     * @deprecated 
     */
    public DocTypeField[] getFieldsOrderBy(int typeID, String fieldName, String order) throws E5Exception {
        // TODO Auto-generated method stub
        return null;
    }

    public DocTypeField[] getSysFields(int docTypeID) throws E5Exception {
        return this.getCache().getSysFields(docTypeID);
        }

    public DocTypeField getField(int docTypeID, String columnCode) throws E5Exception {
        return this.getCache().getField(docTypeID,columnCode);
    }

}