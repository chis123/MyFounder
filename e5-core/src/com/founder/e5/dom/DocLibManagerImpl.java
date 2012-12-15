package com.founder.e5.dom;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.founder.e5.commons.Log;
import com.founder.e5.context.BaseDAO;
import com.founder.e5.context.Context;
import com.founder.e5.context.DAOHelper;
import com.founder.e5.context.DSManager;
import com.founder.e5.context.E5DataSource;
import com.founder.e5.context.E5Exception;
import com.founder.e5.db.ColumnInfo;
import com.founder.e5.db.DBSession;
import com.founder.e5.dom.facade.ContextFacade;
import com.founder.e5.dom.facade.EUIDFacade;
import com.founder.e5.dom.util.DomUtils;
import com.founder.e5.dom.util.FlowField;

/**
 * 文档库管理器的实现类
 *
 * @created 2005-7-15 9:27:16
 * @author Zhang Kaifeng
 */
/**
 * @created 2006-1-24 10:11:23
 * @author Zhang Kaifeng
 * @version
 */
class DocLibManagerImpl implements DocLibManager {
    
    private static Log logger = Context.getLog("e5.dom");

    /*
     * (non-Javadoc)
     *
     * @see com.founder.e5.dom.DocLibManager#alterDocLib(int)
     */
    public void alterDocLib(int docLibID) throws E5Exception {

        DocTypeFieldApps[] addedFields = this.getAddedFields(docLibID);

        DocLib lib = this.get(docLibID);
        int dsid = lib.getDsID();
        DBSession dbss = null;
        try
        {
	        dbss = ContextFacade.getDBSession(dsid);
	        
	        DocTypeManager m = (DocTypeManager) ContextFacade.getBean(DocTypeManager.class);
	
	        ColumnInfo cArray[] = new ColumnInfo[addedFields.length];
	        for (int i = 0; i < addedFields.length; i++) {
	        	ColumnInfo c = new ColumnInfo();
	            DocTypeField f = m.getField(addedFields[i].getFieldID());
	            c.setName(f.getColumnCode());
	            c.setDisplayName(f.getColumnName());
	            c.setDataLength(f.getDataLength());
	            c.setDataPrecision(f.getScale());
	            c.setE5TypeName(f.getDataType());
	            c.setDefaultValue(f.getDefaultValue());
	            c.setNullable((f.getIsNull() == 1) ? true : false);
	            cArray[i] = c;
	        }
	        
	        String alterSQL = dbss.getDialect().getAddColumnDDL(cArray, lib.getDocLibTable());
	        logger.info("alter sql = "+alterSQL);

            dbss.executeUpdate(alterSQL, null);
		} catch (SQLException e) {
            throw new E5Exception(e);

        } finally {
			if (dbss != null) dbss.closeQuietly();
		}

//      this.update(addedFields);

        this.setDBAdded(docLibID);

    }

//    private void update(DocTypeFieldApps[] addedFields) {
//        
//        BaseDAO dao = new BaseDAO();
//        Session session = null;
//        Transaction tx = null;
//        
//        session = dao.getSession();
//        tx = session.beginTransaction();
//        
//        for( int i = 0; i < addedFields.length; i++){
//            DocTypeFieldApps apps = addedFields[i];
//            apps.setIsDBAdded(1);
//            apps.setIsDBUpdated(0);
//            dao.update(apps,session);
//        }
//        
//        dao.commitTransaction(tx);
//        
//        
//    }

    /*
     * (non-Javadoc)
     *
     * @see com.founder.e5.dom.DocLibManager#alterDocLib(int, java.lang.String)
     */
    public void alterDocLib(int docLibID, String DDLStr) throws E5Exception {

        DocLib docLib = this.get(docLibID);
        int dsID = docLib.getDsID();
        DBSession dbSession = ContextFacade.getDBSession(dsID);

        try {
            dbSession.executeUpdate(DDLStr, null);
        } catch (SQLException e) {
            throw new E5Exception(e);

        } finally {
        	dbSession.closeQuietly();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.founder.e5.dom.DocLibManager#create(com.founder.e5.dom.DocLib)
     */
    public void create(String DDL, DocLib docLib) throws E5Exception {

        // 获取文档库ID
        int docLibID = docLib.getDocLibID();
        docLib.setDocLibTable(this.genDocLibName(docLibID));

        // 获取DBSession
        int dsID = docLib.getDsID();
        DBSession dbSession = ContextFacade.getDBSession(dsID);

        // 开始创建
        try {

            // 创建文档库表
            dbSession.executeUpdate(DDL, null);
            // 创建流程记录表
            this.createFlowTable(docLib, dbSession);

            // 创建文档库相关的三条记录
            createDocLibRelatedRecords(docLib, dbSession);

        } catch (Exception e) {

            this.deleteDocLibRelatedTables(docLib, dbSession);
            throw new E5Exception(e);

        } finally {
        	dbSession.closeQuietly();
        }
    }

    /**
     * @param docLib
     * @param session
     * @throws E5Exception
     */
    private void createDocLibRelatedDocLib(DocLib docLib, Session session) throws
            E5Exception {
        try {
            session.save(docLib);
        } catch (HibernateException e) {
            throw new E5Exception(e);
        }
    }

    /**
     * @param docLib
     * @param ds
     * @param ss
     * @throws HibernateException
     * @throws E5Exception
     */
    private void createDocLibRelatedDocLibAdds(DocLib docLib, E5DataSource ds,
                                               Session ss) throws
            HibernateException, E5Exception {

        DocLibAdditionals docLibAdds = new DocLibAdditionals();
        docLibAdds.setLibServer(ds.getDbServer());
        docLibAdds.setLibDB(ds.getDb());
        docLibAdds.setId(EUIDFacade.getID(EUIDFacade.IDTYPE_DOCLIBADDITIONALS));
        docLibAdds.setDocLibID(docLib.getDocLibID());
        docLibAdds.setLibTable(this.genFlowTBName(docLib.getDocLibID()));
        docLibAdds.setLibTypes(1); // //TODO:还未完成的任务，2005-7-14 13:24:25
        ss.save(docLibAdds);

    }

    /**
     * @param docLib
     * @param ss
     * @throws E5Exception
     */
    private void createDocLibRelatedFolder(DocLib docLib, Session ss) throws
            E5Exception {

        int folderID = EUIDFacade.getID(EUIDFacade.IDTYPE_FOLDERVIEW);
        docLib.setFolderID(folderID);

        Folder folder = new Folder();
        folder.setFVID(folderID);
        folder.setDocLibID(docLib.getDocLibID());
        folder.setDocTypeID(docLib.getDocTypeID());
        folder.setFVName(docLib.getDocLibName());
        folder.setParentID(0);
        folder.setRootID(folderID);
        folder.setTreeOrder(0);//同级文件夹从0开始
        folder.setTreeLevel(0);//从根文件夹开始，treelevel为0
        folder.setKeepDay(15);
        folder.setDefaultLayoutID(1); // TODO:还未完成的任务，2005-7-14 12:51:55

        FolderManager folderMgr = (FolderManager) ContextFacade.getBean(
                FolderManager.class);

        folderMgr.create(folder, ss);

    }

    /**
     * @param docLib
     * @param dbss
     */
    private void createDocLibRelatedRecords(DocLib docLib, DBSession dbss) {

        BaseDAO dao = new BaseDAO();
        Session ss = null;
        Transaction tx = null;

        try {
            ss = dao.getSession();
            tx = dao.beginTransaction(ss);

            /** 创建文档库对应的文件夹 */
            createDocLibRelatedFolder(docLib, ss);

            /** 创建文档库记录 */
            this.createDocLibRelatedDocLib(docLib, ss);

            /** 创建DocLibAdditionals记录 */
            DSManager dsMgr = ContextFacade.getDSManager();
            E5DataSource ds = dsMgr.get(docLib.getDsID());
            this.createDocLibRelatedDocLibAdds(docLib, ds, ss);

            dao.commitTransaction(tx);

        } catch (Exception e) {
            if (tx != null)
                try {
                    tx.rollback();
                } catch (HibernateException e1) {
                    e1.printStackTrace();
                }
            this.deleteDocLibRelatedTables(docLib, dbss);

        } finally {
            try {
                dao.closeSession(ss);
            } catch (HibernateException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 创建文档库对应的流程记录表，可能在非中心数据库上
     *
     * @param docLib
     * @param dbss
     * @throws E5Exception
     */
    private void createFlowTable(DocLib docLib, DBSession dbss) throws
            E5Exception {

        try {

            /** 创建流程表 */
            int docLibID = docLib.getDocLibID();
            String flowTBName = this.genFlowTBName(docLibID);

            Field[] flowFields = FlowField.class.getDeclaredFields();
            StringBuffer flowTBDDLBuf = new StringBuffer();
            for (int i = 0; i < flowFields.length; i++) {
                Field field = flowFields[i];
                ColumnInfo ci = (ColumnInfo) field.get(field);
                String ddl = dbss.getDialect().getColumnDDL(ci);
                flowTBDDLBuf.append(ddl).append(",");
            }
            flowTBDDLBuf.deleteCharAt(flowTBDDLBuf.length() - 1);

            StringBuffer flowTBSQLBuf = new StringBuffer();
            flowTBSQLBuf.append("create table ").append(flowTBName);
            flowTBSQLBuf.append("(").append(flowTBDDLBuf);
            flowTBSQLBuf.append(")");
            String flowSql = flowTBSQLBuf.toString();

            dbss.executeUpdate(flowSql, null);

        } catch (Exception e) {
            throw new E5Exception(e);
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see com.founder.e5.dom.DocLibManager#delete(int)
     */
    public void delete(int docLibID) throws E5Exception {

        DocLib lib = this.get(docLibID);
        if (null == lib)
            return;

        BaseDAO dao = new BaseDAO();
        Session ss = null;

        Transaction tx = null;

        try {
            ss = dao.getSession();
            tx = ss.beginTransaction();
            dao.delete("delete from DocLibAdditionals as docLibAdds where docLibAdds.docLibID = :doclib", new Integer(docLibID), Hibernate.INTEGER, ss);

//            FolderManager folderMgr = (FolderManager) ContextFacade.getBean(FolderManager.class);
//            folderMgr.delete(folderID, ss);
            dao.delete("delete from FolderView as fv where fv.docLibID = :doclibid", new Integer(docLibID),Hibernate.INTEGER,ss);
            
            dao.delete("delete from DocLib as docLib where docLib.docLibID = :libid", new Integer(docLibID), Hibernate.INTEGER, ss);
            tx.commit();

        } catch (HibernateException e) {

            if (tx != null)
                tx.rollback();
            throw new E5Exception(e);

        } finally {
            try {
                dao.closeSession(ss);

            } catch (HibernateException e1) {
                throw new E5Exception(e1);
            }
        }

    }

    /**
     * 删除文档库相关的两张表
     *
     * @param docLib
     * @param dbss
     */
    private void deleteDocLibRelatedTables(DocLib docLib, DBSession dbss) {

        try {
            int docLibID = docLib.getDocLibID();

            String sql1 = new StringBuffer().append("drop table ").append(
                    docLib.getDocLibTable()).toString();

            String sql2 = new StringBuffer().append("drop table ").append(
                    this.genFlowTBName(docLibID)).toString();

            dbss.executeUpdate(sql1, null);
            dbss.executeUpdate(sql2, null);

        } catch (Exception e) {

        }

    }

    /**
     * 根据doclibid返回文档库表名
     *
     * @param docLibID
     * @return
     */
    private String genDocLibName(int docLibID) {
        return new StringBuffer().append("DOM_").append(docLibID).append(
                "_DOCLIB").toString();
    }

    /**
     * 根据doclibid返回流程表名
     *
     * @param docLibID
     * @return
     */
    private String genFlowTBName(int docLibID) {
        return new StringBuffer().append("DOM_").append(docLibID).append(
                "_FLOWRECORDS").toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.founder.e5.dom.DocLibReader#get(int)
     */
    public DocLib get(int docLibID) throws E5Exception {

        BaseDAO dao = new BaseDAO();
        DocLib docLib = null;
        try {
            docLib = (DocLib) dao.get(DocLib.class, new Integer(docLibID));
        } catch (HibernateException e) {
            throw new E5Exception(e);
        }

        return docLib;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.founder.e5.dom.DocLibReader#get(int[])
     */
    public DocLib[] get(int[] docLibIDs) throws E5Exception {

        List libList = new ArrayList();
        for (int i = 0; i < docLibIDs.length; i++) {

            int libID = docLibIDs[i];
            DocLib lib = this.get(libID);

            libList.add(lib);
        }

        return (DocLib[]) libList.toArray(DomUtils.EMPTY_DOCLIB_ARRAY);

    }

    /**
     * 根据文档库id，查找更新了的（增加了的）字段id数组
     *
     * 该方法查询DocTypeFieldApps表，找出符合指定文档库id的，并且isUpdated和isAdded为0的字段
     *
     * @param docLibID
     * @return
     * @throws E5Exception
     */
    private DocTypeFieldApps[] getAddedFields(int docLibID) throws E5Exception {
        
        DocTypeFieldApps[] ret = DomUtils.EMPTY_DOCTYPEFIELDAPPS_ARRAY;
        
        List appsList = DAOHelper.find("from DocTypeFieldApps as d where d.isDBAdded = 0  and d.isDBUpdated = 0 and d.docLibID=:libid",
                                       new Integer(docLibID), Hibernate.INTEGER);
        
        if(appsList.isEmpty())
            return ret;
        
        return (DocTypeFieldApps[]) appsList.toArray(DomUtils.EMPTY_DOCTYPEFIELDAPPS_ARRAY);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.founder.e5.dom.DocLibReader#getByTypeID(int)
     */
    public DocLib[] getByTypeID(int doctypeID) throws E5Exception {

        DocLib[] docLibs = DomUtils.EMPTY_DOCLIB_ARRAY;
        List docLibList = null;

        if (doctypeID > 0)
            docLibList = DAOHelper.find(
                    "from DocLib as docLib where docLib.docTypeID=:doctypeid order by docLib.docLibID",
                    new Integer(doctypeID), Hibernate.INTEGER);
        else
            docLibList = DAOHelper.find("from DocLib as docLib order by docLib.docLibID");

        if (!docLibList.isEmpty())
            docLibs = (DocLib[]) docLibList
                      .toArray(DomUtils.EMPTY_DOCLIB_ARRAY);
        return docLibs;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.founder.e5.dom.DocLibReader#getByTypeName(java.lang.String)
     */
    public DocLib[] getByTypeName(String doctypeName) throws E5Exception {

        DocType docType = ((DocTypeManager) ContextFacade
                           .getBean(DocTypeManager.class)).get(doctypeName);

        if (null == docType)
            return DomUtils.EMPTY_DOCLIB_ARRAY;

        int docTypeID = docType.getDocTypeID();
        return this.getByTypeID(docTypeID);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.founder.e5.dom.DocLibReader#getFlowTableInfo(int)
     */
    public DocLibAdditionals getFlowTableInfo(int docLibID) throws E5Exception {
        DocLibAdditionals docLibAdds = null;
        List list = DAOHelper.find("from DocLibAdditionals as docLibAdds where docLibAdds.libTypes=1 and docLibAdds.docLibID=:libid",
                                   new Integer(docLibID), Hibernate.INTEGER);
        if (list.size() > 0)
            docLibAdds = (DocLibAdditionals) list.get(0);
        return docLibAdds;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.founder.e5.dom.DocLibReader#getIDsByTypeID(int)
     */
    public int[] getIDsByTypeID(int docTypeID) throws E5Exception {

        int[] docLibs = DomUtils.EMPTY_INT_ARRAY;
        List docLibList = null;

        if (docTypeID > 0)
            docLibList = DAOHelper.find(
                    "from DocLib as docLib where docLib.docTypeID=:doctypeid",
                    new Integer(docTypeID), Hibernate.INTEGER);
        else
            docLibList = DAOHelper.find("from DocLib as docLib");

        if (!docLibList.isEmpty()) {
            docLibs = new int[docLibList.size()];
            int i = 0;
            for (Iterator iter = docLibList.iterator(); iter.hasNext(); ) {
                DocLib docLib = (DocLib) iter.next();
                docLibs[i++] = docLib.getDocLibID();
            }
        }
        return docLibs;
    }

    private void setDBAdded(int docLibID) throws E5Exception {

        BaseDAO dao = new BaseDAO();
        Session ss = null;
        Transaction tx = null;

        try {
            ss = dao.getSession();
            tx = ss.beginTransaction();
            
            List appsList = DAOHelper.find("from DocTypeFieldApps as d where d.isDBAdded=0 and d.isDBUpdated=0 and d.docLibID=:libid",
                                           new Integer(docLibID),
                                           Hibernate.INTEGER, ss);
            for (Iterator iter = appsList.iterator(); iter.hasNext(); ) {
                DocTypeFieldApps d = (DocTypeFieldApps) iter.next();
                d.setIsDBAdded(1);
                ss.saveOrUpdate(d);
            }
            tx.commit();
            
        } catch (HibernateException e) {
            throw new E5Exception(e);
        } finally {
            try {
                dao.closeSession(ss);
            } catch (HibernateException e1) {
                e1.printStackTrace();
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.founder.e5.dom.DocLibManager#update(com.founder.e5.dom.DocLib)
     */
    public void update(DocLib docLib) throws E5Exception {

        BaseDAO dao = new BaseDAO();
        Session session = null;
        Transaction tx = null;

        try {
            session = dao.getSession();
            tx = dao.beginTransaction(session);

            int libid = docLib.getDocLibID();
            DocLib oriLib = (DocLib) session.get(DocLib.class,
                                                 new Integer(libid));

            oriLib.setDescription(docLib.getDescription());
            oriLib.setDocLibName(docLib.getDocLibName());
            
            //更新文档库对应文件夹的名称。
            FolderManager folderManager = (FolderManager) Context.getBean(FolderManager.class);
            folderManager.updateFolderName(libid,docLib.getDocLibName());
            
            session.update(oriLib);
            dao.commitTransaction(tx);

        } catch (HibernateException e) {

            throw new E5Exception(e);

        } finally {
            dao.closeSession(session);
        }

    }

    /* (non-Javadoc)
     * @see com.founder.e5.dom.DocLibManager#generateDDL(com.founder.e5.dom.DocLib)
     */
    public String generateDDL(DocLib doclib) throws E5Exception {

        // 获取文档库ID
        int docLibID = EUIDFacade.getID(EUIDFacade.IDTYPE_DOCLIB);

        doclib.setDocLibID(docLibID);
        doclib.setDocLibTable(this.genDocLibName(docLibID));

        // 获取DBSession
        int dsID = doclib.getDsID();
        DBSession dbss = ContextFacade.getDBSession(dsID);

        try {
			return this.generateDDL(doclib, dbss);
		} finally {
			dbss.closeQuietly();
		}
    }

    /**
     * 2006-1-24 10:13:22
     * @author zhang_kaifeng
     * @param doclib
     * @param dbss
     * @return
     * @throws E5Exception
     */
    private String generateDDL(DocLib doclib, DBSession dbss) throws
            E5Exception {

        /** 创建文档库表 */
        int typeID = doclib.getDocTypeID();

        DocTypeManager docTypeMgr = (DocTypeManager) ContextFacade.getBean(
                DocTypeManager.class);

        DocTypeField[] fields = docTypeMgr.getFields(typeID);

        StringBuffer docLibDDLBuf = new StringBuffer();
        ColumnInfo ci = new ColumnInfo();

        for (int i = 0; i < fields.length; i++) {

            DocTypeField field = fields[i];
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

            String subDDL = dbss.getDialect().getColumnDDL(ci);
            logger.debug(ToStringBuilder.reflectionToString(field));
            logger.info(field.getColumnCode()+" 's DDL = "+subDDL);
            
            docLibDDLBuf.append(subDDL).append(",");
        }

        docLibDDLBuf.deleteCharAt(docLibDDLBuf.length() - 1); // 删除最后一个逗号

        StringBuffer createSQLBuffer = new StringBuffer();
        createSQLBuffer.append("create table ");
        createSQLBuffer.append(doclib.getDocLibTable());
        createSQLBuffer.append("(").append(docLibDDLBuf).append(")");

        return createSQLBuffer.toString();

    }

    /* (non-Javadoc)
     * @see com.founder.e5.dom.DocLibManager#hasUsedForDocLib(int)
     */
    public boolean hasUsedForDocLib(int dsID) throws E5Exception {
        
        List ret = DAOHelper.find("from DocLib as docLib where docLib.dsID=:dsid",
                                  new Integer(dsID), Hibernate.INTEGER);
        return !(ret.isEmpty());
    }

    /* (non-Javadoc)
     * @see com.founder.e5.dom.DocLibReader#getDocTypeId(int)
     */
    public int getDocTypeID(int docLibId) throws E5Exception {
        DocLib lib = this.get(docLibId);
        if (lib != null)
            return lib.getDocTypeID();
        return 0;
    }

}
