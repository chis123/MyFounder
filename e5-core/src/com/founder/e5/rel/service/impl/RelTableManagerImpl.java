package com.founder.e5.rel.service.impl;

import java.lang.reflect.InvocationTargetException;

import com.founder.e5.context.Context;
import com.founder.e5.context.E5Exception;
import com.founder.e5.db.ColumnInfo;
import com.founder.e5.db.DBSession;
import com.founder.e5.db.DataType;
import com.founder.e5.dom.DocTypeField;
import com.founder.e5.dom.DocTypeManager;
import com.founder.e5.dom.facade.ContextFacade;
import com.founder.e5.rel.dao.RelTableDAO;
import com.founder.e5.rel.model.RelTable;
import com.founder.e5.rel.model.RelTableField;
import com.founder.e5.rel.model.RelTableVO;
import com.founder.e5.rel.service.RelTableDocLibManager;
import com.founder.e5.rel.service.RelTableManager;

public class RelTableManagerImpl implements RelTableManager {
    

    private RelTableDAO dao;

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.rel.service.RelTableManager#getRelTable(int)
     */
    public RelTable getRelTable(int tableId) {
        return dao.getRelTable(new Integer(tableId));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.rel.service.RelTableManager#getRelTables()
     */
    public RelTable[] getRelTables(int docTypeId) {        
        return  (RelTable[]) dao.getRelTables(docTypeId).toArray(new RelTable[0]);
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.rel.service.RelTableManager#removeRelTable(int)
     */
    public int removeRelTable(int tableId) {
        
        RelTableDocLibManager mgr = (RelTableDocLibManager) Context.getBean(RelTableDocLibManager.class);
        if(!mgr.isRelTableReferenced(tableId)){
            dao.removeReltable(new Integer(tableId));
            return 1;
        }
        return 0;
        
    }

    /**
     * 保存分类关联表记录 2006-3-21 16:21:54
     * 
     * @param table
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws E5Exception
     */
    public void saveRelTable(RelTable table) throws Exception {
        dao.saveRelTable(table);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.rel.service.RelTableManager#setRelTableDAO(com.founder.e5.rel.dao.RelTableDAO)
     */
    public void setDao(RelTableDAO dao) {
        this.dao = dao;

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.rel.service.RelTableManager#genCreateDDL(com.founder.e5.rel.model.RelTable)
     */
    public String genCreateDDL(RelTableVO table) throws E5Exception {

        int dsid = table.getDsID();
        DBSession dbsession = null;
        
        try
		{
			dbsession = Context.getDBSession(dsid,true);
			return this.genCreateDDL(table, dbsession);
		} finally
		{
			if (dbsession != null) dbsession.closeQuietly();
		}

    }

    /**
     * 2006-3-21 16:45:31
     *
     * @param dbsession
     * @param table
     * @return
     * @throws E5Exception
     */
    private String genCreateDDL(RelTableVO table, DBSession dbsession) throws E5Exception {

        StringBuffer ddlBuf = new StringBuffer();
        
        //generate SysFields DDL
        genSysFieldsDDL(table, dbsession,ddlBuf);
        
        //generate ExtFields DDL
        genExtFieldsDDL(table, dbsession, ddlBuf);
        
        //generate ClassFields DDL
        genClassFieldsDDL(dbsession,ddlBuf);

        ddlBuf.deleteCharAt(ddlBuf.length() - 1); // 删除最后一个逗号
        
        String tableName = table.getTableName();
        
        StringBuffer createSQLBuffer = new StringBuffer();
        createSQLBuffer.append("create table ");
        createSQLBuffer.append(tableName);
        createSQLBuffer.append("(").append(ddlBuf).append(")");

        return createSQLBuffer.toString();

    }

    /**
     * 生成十级分类的字段的DDL
     * 2006-3-23 9:48:25
     * @param dbsession
     * @param ddlBuf
     */
    private void genClassFieldsDDL(DBSession dbsession, StringBuffer ddlBuf) {
        
        for( int i = 1; i < 11; i++){
            
            ColumnInfo ci = new ColumnInfo();
            ci.setName("CLASS_"+i);
            ci.setE5TypeName(DataType.INTEGER);
            ci.setNullable(true);
            ci.setUnique(false);
            ci.setDataLength(38);

            String subDDL = dbsession.getDialect().getColumnDDL(ci);
            ddlBuf.append(subDDL).append(",");
            
        }        
    }

    /**
     * 生成扩展字段的DDL
     * 2006-3-23 9:44:36
     * @param table
     * @param dbsession
     * @param ddlBuf
     * @throws E5Exception
     */
    private void genExtFieldsDDL(RelTableVO table, DBSession dbsession, StringBuffer ddlBuf) throws E5Exception {
        
        DocTypeManager docTypeMgr = (DocTypeManager) ContextFacade.getBean(DocTypeManager.class);
        int[] fieldIds = table.getFiledIds();      
        for( int i = 0; i < fieldIds.length; i++){

            int fieldId = fieldIds[i];
            DocTypeField field = docTypeMgr.getField(fieldId);

            int isNull = field.getIsNull();
            boolean nullable = (isNull == 1);
            ColumnInfo ci = new ColumnInfo();
            ci.setName(field.getColumnCode());
            ci.setDisplayName(field.getColumnName());
            ci.setE5TypeName(field.getDataType());
            ci.setNullable(nullable);
            ci.setUnique(false);
            ci.setDataLength(field.getDataLength());
            ci.setDataPrecision(field.getScale());
            ci.setDefaultValue(field.getDefaultValue());

            String subDDL = dbsession.getDialect().getColumnDDL(ci);
            ddlBuf.append(subDDL).append(",");
        }
    }

    /**
     * 生成系统字段的DDL
     * 2006-3-23 9:43:36
     * @param table
     * @param dbsession
     * @param ddlBuf
     * @throws E5Exception
     */
    private void genSysFieldsDDL(RelTableVO table, DBSession dbsession, StringBuffer ddlBuf) throws E5Exception {
        DocTypeManager docTypeMgr = (DocTypeManager) ContextFacade.getBean(DocTypeManager.class);
        int docTypeId = table.getRefDocTypeID();
        DocTypeField[] sysFields = docTypeMgr.getSysFields(docTypeId);        
        for( int i = 0; i < sysFields.length; i++){
            ColumnInfo ci = new ColumnInfo();
            DocTypeField field = sysFields[i];
            
            int isNull = field.getIsNull();
            boolean nullable = (isNull == 1);

            ci.setName(field.getColumnCode());
            ci.setDisplayName(field.getColumnName());
            ci.setE5TypeName(field.getDataType());
            ci.setNullable(nullable);
            ci.setUnique(false);
            ci.setDataLength(field.getDataLength());
            ci.setDataPrecision(field.getScale());
            ci.setDefaultValue(field.getDefaultValue());

            String subDDL = dbsession.getDialect().getColumnDDL(ci);
            ddlBuf.append(subDDL).append(",");
            
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.rel.service.RelTableManager#createRelTable(java.lang.String,
     *      com.founder.e5.rel.model.RelTableVO)
     */
    public void createRelTable(String ddl, RelTable table) throws Exception {

        // create table
        int dsId = table.getDsID().intValue();
        DBSession dbsession = null; 
        try {
        	dbsession = Context.getDBSession(dsId, true);
        	dbsession.executeUpdate(ddl, null);
		} finally {
			if (dbsession != null) dbsession.closeQuietly();
		}

        this.saveRelTable(table);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.rel.service.RelTableManager#appendRelTableField(com.founder.e5.rel.model.RelTableField)
     */
    public void appendRelTableField(RelTableField field) throws Exception {

        int tableId = field.getRelTableId();

        RelTable table = this.getRelTable(tableId);
        String tableName = table.getTableName();

        int dsId = table.getDsID().intValue();
        DBSession dbsession = null;
        try
        {
	        dbsession = Context.getDBSession(dsId);
	
	        StringBuffer alterSQLBuf = new StringBuffer();
	        alterSQLBuf.append("alter table "); 
	        
	        alterSQLBuf.append(tableName).append(" add ");
	
	        ColumnInfo c = new ColumnInfo();
	
	        c.setName(field.getFieldName());
	        c.setDataLength(field.getDataLength());
	        c.setDataPrecision(field.getScale());
	        c.setE5TypeName(field.getDataType());
	        c.setDefaultValue(field.getDefaultValue());
	        c.setNullable((field.getIsNull() == 1));
	        String ddl = dbsession.getDialect().getColumnDDL(c);
	        alterSQLBuf.append(ddl);
	
	        dbsession.executeUpdate(alterSQLBuf.toString(), null);
		} finally {
			if (dbsession != null) dbsession.closeQuietly();
		}
    }

    /* (non-Javadoc)
     * @see com.founder.e5.rel.service.RelTableManager#getRelTable(java.lang.String)
     */
    public RelTable getRelTable(String tableSuffix) {
        return dao.getRelTable(tableSuffix);
    }



}
