package com.founder.e5.dom;

import gnu.trove.TIntObjectHashMap;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

import com.founder.e5.commons.Log;
import com.founder.e5.context.BaseDAO;
import com.founder.e5.context.BaseField;
import com.founder.e5.context.Context;
import com.founder.e5.context.DAOHelper;
import com.founder.e5.context.E5Exception;
import com.founder.e5.db.ColumnInfo;
import com.founder.e5.dom.facade.ContextFacade;
import com.founder.e5.dom.facade.EUIDFacade;
import com.founder.e5.dom.util.DomUtils;

/**
 * @created 05-七月-2005 9:02:26
 * @author kaifeng zhang
 * @version 1.0
 */
class DocTypeManagerImpl implements DocTypeManager {

    private Log logger = Context.getLog("e5.dom");
    
    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DocTypeManager#create(com.founder.e5.dom.DocType)
     */
    public int create(DocType docType) throws E5Exception {

        // check if one doctype whith the same name already exist.
        String name = docType.getDocTypeName();
        if (name == null)
            throw new E5Exception("the docType1 has no name");

        DocType dType = null;
        dType = this.get(name);
        if (dType != null)
            throw new E5Exception(
                    "docType1 with the same name already existed!");

        // gen doctypeID
        int docTypeID = EUIDFacade.getID(EUIDFacade.IDTYPE_DOCTYPE);
        docType.setDocTypeID(docTypeID);

        BaseDAO dao = new BaseDAO();
        Session session = null;
        try {
            session = dao.getSession();
        } catch (HibernateException e3) {
            throw new E5Exception("get session from BaseDao excetion", e3);
        }

        // begin Transaction
        Transaction tx = null;
        try {
            tx = dao.beginTransaction(session);

            // save DocType record
            dao.save(docType, session);

            // save DocTypeApps record
            DocTypeApps docTypeApps = new DocTypeApps();
            docTypeApps.setAppID(docType.getAppID());
            docTypeApps.setDocTypeID(docTypeID);
            dao.save(docTypeApps, session);

            // save sys-DocTypeField records
            Field[] fields = BaseField.class.getFields();

            for (int i = 0; i < fields.length; i++) {

                Field f = fields[i];
                ColumnInfo ci = (ColumnInfo) f.get(f);
                DocTypeField docTypeField = new DocTypeField(ci);
                docTypeField.setFieldID(EUIDFacade
                        .getID(EUIDFacade.IDTYPE_DOCTYPEFIELD));
                docTypeField.setDocTypeID(docTypeID);
                docTypeField.setAttribute(1);// 1代表系统平台字段，2代表应用系统字段，3代码用户扩展字段
                docTypeField.setStatus("P");
                dao.save(docTypeField, session);

            }
            dao.commitTransaction(tx);
            
            return docTypeID;

        } catch (HibernateException e) {
            if (tx != null)
                try {
                    tx.rollback();
                } catch (HibernateException e4) {
                    e4.printStackTrace();
                }
            throw new E5Exception(e);
        } catch (IllegalArgumentException e){
            throw new E5Exception(e);
        } catch (IllegalAccessException e){
            throw new E5Exception(e);
        } finally {
            dao.closeSession(session);
            
        }

    }

    /* (non-Javadoc)
     * @see com.founder.e5.dom.DocTypeManager#create(com.founder.e5.dom.DocTypeApps)
     */
    public void create(DocTypeApps docTypeApps) throws E5Exception {
        new BaseDAO().saveOrUpdate(docTypeApps);
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DocTypeManager#create(com.founder.e5.dom.DocTypeField)
     */
    public int create(DocTypeField doctypeField) throws E5Exception {

        // 获取文档类型字段ID
        int fieldID = EUIDFacade.getID(EUIDFacade.IDTYPE_DOCTYPEFIELD);
        doctypeField.setFieldID(fieldID);

        // 创建DAO，及Session
        BaseDAO dao = new BaseDAO();
        Session session = null;

        // 开始事务
        Transaction tx = null;
        
        try {
            
            session = dao.getSession();
            tx = dao.beginTransaction(session);

            // 保存字段记录
            dao.save(doctypeField, session);

            // 根据文档类型应用到的文档库，创建该新增字段的更新记录
            int docTypeID = doctypeField.getDocTypeID();
            int appid = this.getAppID(docTypeID);
            DocLibManager docLibMgr = (DocLibManager) ContextFacade.getBean(DocLibManager.class);
            
            int[] docLibIDs = docLibMgr.getIDsByTypeID(docTypeID);
            for (int i = 0; i < docLibIDs.length; i++) {
                int doclibid = docLibIDs[i];
                DocTypeFieldApps fieldApps = new DocTypeFieldApps(appid,doclibid, docTypeID, fieldID, 0, 0);
                dao.save(fieldApps, session);

            }
            dao.commitTransaction(tx);
        } catch (HibernateException e) {
            if (tx != null)
                try {
                    tx.rollback();
                } catch (HibernateException e4) {
                    e4.printStackTrace();
                }
            throw new E5Exception(e);
        } finally {
            try {
                dao.closeSession(session);
            } catch (HibernateException e4) {
                e4.printStackTrace();
            }
        }
        
        return fieldID;

    }

    /* (non-Javadoc)
     * @see com.founder.e5.dom.DocTypeManager#create(java.lang.String, int)
     */
    public int create(String docTypeName, int appID) throws E5Exception {
        
        DocType docType = new DocType();
        docType.setAppID(appID);
        docType.setDocTypeName(docTypeName);
        
        return this.create(docType);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DocTypeManager#delete(int)
     */
    public void delete(int doctypeID) throws E5Exception {
        BaseDAO dao = new BaseDAO();
        Session ss = null;
        Transaction tx = null;

        try {
            ss = dao.getSession();
            tx = dao.beginTransaction(ss);
            DAOHelper.delete("delete from DocTypeField as docTypeField where docTypeField.docTypeID = :docTypeId", 
            		new Integer(doctypeID), Hibernate.INTEGER, ss);
            
            DAOHelper.delete("delete from DocTypeApps as docTypeApps where docTypeApps.docTypeID = :docTypeId", 
            		new Integer(doctypeID), Hibernate.INTEGER, ss);
            
            DAOHelper.delete("delete from DocType as docType1 where docType1.docTypeID = :docTypeId", 
            		new Integer(doctypeID), Hibernate.INTEGER, ss);
            
            //delete doclib
            DocLibManager docLibManager = (DocLibManager) Context.getBean(DocLibManager.class);
            DocLib[] libs = docLibManager.getByTypeID(doctypeID);
            for( int i = 0; i < libs.length; i++){
                DocLib lib = libs[i];
                docLibManager.delete(lib.getDocLibID());
            }
            
            //delete rule
            RuleManager ruleManager = (RuleManager) Context.getBean(RuleManager.class);
            Rule[] rules = ruleManager.getByDoctypeID(doctypeID);
            for( int i = 0; i < rules.length; i++){
                Rule rule = rules[i];
                ruleManager.delete(rule.getRuleID());
            }
            
            //delete filter
            FilterManager filterManager = (FilterManager) Context.getBean(FilterManager.class);
            Filter[] filters = filterManager.getByTypeID(doctypeID);
            for( int i = 0; i < filters.length; i++){
                Filter filter = filters[i];
                filterManager.delete(filter.getFilterID());
            }
            
            dao.commitTransaction(tx);
        } catch (HibernateException e) {
            if (tx != null)
                try {
                    tx.rollback();
                } catch (HibernateException e1) {
                    e1.printStackTrace();
                }
            throw new E5Exception(e);
        } finally {
            try {
                dao.closeSession(ss);
            } catch (HibernateException e1) {
                e1.printStackTrace();
            }
        }
    }

    /* (non-Javadoc)
     * @see com.founder.e5.dom.DocTypeManager#delete(int, int)
     */
    public void delete(int appID, int docTypeID) throws E5Exception {
        
        new BaseDAO().delete("delete from DocTypeApps as dta where dta.appID="+appID+" and dta.docTypeID="+docTypeID);
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DocTypeManager#deleteField(int)
     */
    public void deleteField(int fieldID) throws E5Exception {

        BaseDAO dao = new BaseDAO();
        Session session = null;
        Transaction tx = null;

        try {
            
            session = dao.getSession();
            tx = dao.beginTransaction(session);

            //置删除标记
            DocTypeField field = this.getField(fieldID,session);
            if (field == null)
                throw new E5Exception("DocTypeField not exist, fieldID=" + fieldID);
            field.setStatus("D");

            //这里应该判断对应的字段记录是否存在，存在则置isupdated为－1
            List docTypeFieldAppsList = DAOHelper.find("from DocTypeFieldApps as d where d.fieldID=:fieldid",
            		new Integer(fieldID), Hibernate.INTEGER, session);
            
            //判断apps记录是否存在
            if(docTypeFieldAppsList.isEmpty()){
                
                int docTypeID = field.getDocTypeID();
                int appid = this.getAppID(docTypeID);
                DocLibManager docLibMgr = (DocLibManager) ContextFacade.getBean(DocLibManager.class);
                
                int[] docLibIDs = docLibMgr.getIDsByTypeID(appid);
                for (int i = 0; i < docLibIDs.length; i++) {
                    int doclibid = docLibIDs[i];
                    DocTypeFieldApps fieldApps = new DocTypeFieldApps(appid,doclibid, docTypeID, fieldID, 0, -1);
                    dao.save(fieldApps, session);

                }
            }else
                for (Iterator iter = docTypeFieldAppsList.iterator(); iter.hasNext();) {
                    
                    DocTypeFieldApps fieldApps = (DocTypeFieldApps) iter.next();
                    fieldApps.setIsDBUpdated(-1);
                }
            
            session.flush();
            
            dao.commitTransaction(tx);
            
        } catch (HibernateException e) {
            if (tx != null)
                try {
                    tx.rollback();
                } catch (HibernateException e1) {
                    e1.printStackTrace();
                }
            throw new E5Exception(e);
        } finally {
            try {
                dao.closeSession(session);
            } catch (HibernateException e1) {

                e1.printStackTrace();
            }
        }
    }

    public void exportDocType(Element rootElement, DocType type) {
        
        int typeID = type.getDocTypeID();
        String typeName = type.getDocTypeName();
        
        Element typeElement = rootElement.addElement("DocumentType");
        
        //properties
        exprotDocTypeProperties(type, typeName, typeElement);        
        //fields           
        exportFields( typeID, typeElement);        
        //filters
        exportFilters(rootElement, typeID, typeName);          
        //rules
        exportRules(rootElement, typeID, typeName);
    }

    public String exportDocType(int docTypeID) throws E5Exception {

        Element rootElement = DocumentHelper.createElement("DocumentTypes");
        Document document = DocumentHelper.createDocument(rootElement);
        DocType type = this.get(docTypeID);
        this.exportDocType(rootElement,type);
        
        OutputFormat format = OutputFormat.createPrettyPrint();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        String ret = null;
        try{
            XMLWriter writer = new XMLWriter(os,format);
            writer.write(document);
            ret = os.toString("utf-8");
        } catch (Exception e){
            throw new E5Exception(e);
        }
        
        return ret;
    }

    private void exportFields(int typeID, Element typeElement) {
        
        DocTypeField[] fields = DomUtils.EMPTY_DOCTYPEFIELD_ARRAY;
        DocTypeManager typeManager = (DocTypeManager) Context.getBean(DocTypeManager.class);
        
        try{
            fields = typeManager.getFieldsExt(typeID);
        } catch (E5Exception e){
            
            logger.warn("",e);
        }
        
        Element fieldsElement = typeElement.addElement("Fields");
        
        for( int j = 0; j < fields.length; j++){
            DocTypeField field = fields[j];
            
            Element fieldElement = fieldsElement.addElement("Field");
            
            fieldElement.addElement("ColumnCode").addText(field.getColumnCode());
            fieldElement.addElement("ColumnName").addText(field.getColumnName());
            fieldElement.addElement("DataType").addText(field.getDataType());
            fieldElement.addElement("Length").addText(""+field.getDataLength());
            
            fieldElement.addElement("IsNull").addText(""+field.getIsNull());
            fieldElement.addElement("Status").addText(field.getStatus());
            fieldElement.addElement("Attribute").addText(String.valueOf(field.getAttribute()));
            //缺省值
            String defaultValue = field.getDefaultValue();
            if (defaultValue == null) defaultValue = "";
            fieldElement.addElement("DefaultValue").addText(defaultValue);
            //填写方式
            fieldElement.addElement("EditType").addText(String.valueOf(field.getEditType()));
            //枚举值
            if (field.getEditType() == DocTypeField.EDITTYPE_ENUM 
            	|| field.getEditType() == DocTypeField.EDITTYPE_COMPLEX)
            	fieldElement.addElement("Options").addText(String.valueOf(field.getOptions()));
            //只读?
            fieldElement.addElement("Readonly").addText(String.valueOf(field.getReadonly()));
            
        }
    }

    private void exportFilters(Element typesElement, int typeID, String typeName) {
        
        FilterManager filterManager = (FilterManager) Context.getBean(FilterManager.class);
        
        Filter[] filters = DomUtils.EMPTY_FILTER_ARAAY;
        
        try{
            filters = filterManager.getByTypeID(typeID);
        } catch (E5Exception e){
            logger.warn("",e);
        }
        
        Element filtersElement = typesElement.addElement("FILTERS");
        Element filterTypeElement = filtersElement.addElement("DOCTYPE").addAttribute("NAME",typeName);
        
        for( int j = 0; j < filters.length; j++){
            Filter filter = filters[j];
            
            filterTypeElement.addElement("FILTER").addAttribute("NAME",filter.getFilterName())
                                                  .addAttribute("FORMULA",filter.getFormula())
                                                  .addAttribute("DESCRIPTION",filter.getDescription());               
            
        }
    }

    private void exportRules(Element typesElement, int typeID, String typeName) {
        
        RuleManager ruleManger = (RuleManager) Context.getBean(RuleManager.class);
        Rule[] rules = DomUtils.EMPTY_RULE_ARRAY;
        
        try{
            rules = ruleManger.getByDoctypeID(typeID);
            
        } catch (E5Exception e){
            logger.warn("",e);
        }
        
        Element rulesElement = typesElement.addElement("RULES");
        Element ruleTypeElement = rulesElement.addElement("DOCTYPE").addAttribute("NAME",typeName);
        
        for( int j = 0; j < rules.length; j++){
            Rule rule = rules[j];
            
            ruleTypeElement.addElement("RULE").addAttribute("NAME",rule.getRuleName())
                                              .addAttribute("CLASSNAME",rule.getRuleClassName())
                                              .addAttribute("VARPARAM",rule.getRuleArguments())
                                              .addAttribute("METHOD",rule.getRuleMethod())
                                              .addAttribute("DESCRIPTION",rule.getDescription());
        }
    }

    private void exprotDocTypeProperties(DocType type, String typeName, Element typeElement) {
        
        typeElement.addElement("Name").addText(typeName);
        typeElement.addElement("DispName").addText(typeName);
        typeElement.addElement("Attribute").addText("0");
        typeElement.addElement("Description")
                    .addText((type.getDescInfo() == null)? "": type.getDescInfo());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DocTypeReader#get(int)
     */
    public DocType get(int docTypeID) throws E5Exception {

        BaseDAO dao = new BaseDAO();
        DocType docType = null;
        try {
            docType = (DocType) dao.get(DocType.class, new Integer(docTypeID));
        } catch (HibernateException e) {
            throw new E5Exception("根据ID获取文档类型对象时，发生数据库操作异常", e);
        }
        return docType;

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DocTypeReader#get(java.lang.String)
     */
    public DocType get(String doctypeName) throws E5Exception {
    	List list = DAOHelper.find("from DocType as docType1 where docType1.docTypeName=:typename",
        		doctypeName, Hibernate.STRING);
    	if (list.size() == 0) return null;
        return (DocType) list.get(0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DocTypeReader#getAppID(int)
     */
    public int getAppID(int docTypeID) throws E5Exception {
        List docTypeApps = DAOHelper.find("from DocTypeApps as docTypeApps where docTypeApps.docTypeID=:doctypeid",
                            new Integer(docTypeID), Hibernate.INTEGER);
        if (docTypeApps.isEmpty())
            return 0;
        return ((DocTypeApps) docTypeApps.get(0)).getAppID();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DocTypeManager#getDocLibsToBeUpdated()
     */
    public DocLib[] getDocLibsToBeUpdated(int docTypeID) throws E5Exception {
        
        DocLib[] ret = DomUtils.EMPTY_DOCLIB_ARRAY;
        
        DocLibManager libManager = (DocLibManager) Context.getBean(DocLibManager.class);

        List fieldAppsList = DAOHelper.find("from DocTypeFieldApps as d where d.isDBAdded = 0 and d.isDBUpdated = 0 and d.docTypeID = :doctypeid",
                                            new Integer(docTypeID),Hibernate.INTEGER);

        if (!fieldAppsList.isEmpty()) {
            
            TIntObjectHashMap libMap = new TIntObjectHashMap();
            
            for (Iterator iter = fieldAppsList.iterator(); iter.hasNext();) {
                DocTypeFieldApps fieldApps = (DocTypeFieldApps) iter.next();
                int docLibID = fieldApps.getDocLibID();
                DocLib lib = libManager.get(docLibID);
                
                if(!libMap.containsKey(docLibID))
                    libMap.put(docLibID,lib);
                
            }
            
            Object[] os = libMap.getValues();
            ret = new DocLib[os.length];
            for( int i = 0; i < os.length; i++){
                Object object = os[i];
                ret[i] = (DocLib) object;
                
            }
            
        }
        return ret;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DocTypeReader#getField(int)
     */
    public DocTypeField getField(int fieldID) throws E5Exception {

        BaseDAO dao = new BaseDAO();
        DocTypeField field = null;
        try {
            field = (DocTypeField) dao.get(DocTypeField.class, new Integer(
                    fieldID));
        } catch (HibernateException e) {
            throw new E5Exception(e);
        }
        return field;
    }

    private DocTypeField getField(int fieldID, Session session) {
        return (DocTypeField) session.get(DocTypeField.class, new Integer(fieldID));
    }

    /* (non-Javadoc)
     * @see com.founder.e5.dom.DocTypeReader#getField(int, java.lang.String)
     */
    public DocTypeField getField(int docTypeID, String columnCode) throws E5Exception {
        
        List fieldList = DAOHelper.find("from DocTypeField as field where field.columnCode=:columnCode and field.docTypeID=:docTypeId",
                                        new String[]{"docTypeId","columnCode"},
                                        new Object[]{new Integer(docTypeID),columnCode},
                                        new Type[]{Hibernate.INTEGER,Hibernate.STRING});
        
        if(fieldList.isEmpty())
            return null;
        
        return (DocTypeField) fieldList.get(0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DocTypeReader#getFieldName(int)
     */
    public String getFieldName(int fieldID) throws E5Exception {

        BaseDAO dao = new BaseDAO();
        DocTypeField field = null;
        try {
            field = (DocTypeField) dao.get(DocTypeField.class, new Integer(
                    fieldID));
        } catch (HibernateException e) {
            throw new E5Exception(e);
        }
        return field.getColumnName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DocTypeReader#getFields(int)
     */
    public DocTypeField[] getFields(int typeID) throws E5Exception {
        
        return this.getFieldsOrderBy(typeID, "fieldID", "");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DocTypeReader#getFieldsExt(int)
     */
    public DocTypeField[] getFieldsExt(int docTypeID) throws E5Exception {
        
        DocTypeField[] fields = DomUtils.EMPTY_DOCTYPEFIELD_ARRAY;
        List fieldList = DAOHelper.find("from DocTypeField as field where field.docTypeID=:doctype and field.attribute > 1 and field.status='P' order by field.fieldID",
                                        new Integer(docTypeID), Hibernate.INTEGER);
        if (!fieldList.isEmpty()) {
            fields = (DocTypeField[]) fieldList.toArray(DomUtils.EMPTY_DOCTYPEFIELD_ARRAY);
        }
        return fields;
    }

	private DocTypeField[] getFields(int docTypeID, int attribute) throws E5Exception
	{
        DocTypeField[] fields = DomUtils.EMPTY_DOCTYPEFIELD_ARRAY;
        List fieldList = DAOHelper.find("from DocTypeField as field where field.docTypeID=? and field.attribute=? and field.status='P' order by field.fieldID",
        				new Object[]{new Integer(docTypeID), new Integer(attribute)});
        if (!fieldList.isEmpty()) {
            fields = (DocTypeField[]) fieldList.toArray(DomUtils.EMPTY_DOCTYPEFIELD_ARRAY);
        }
        return fields;
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.dom.DocTypeReader#getFieldsApp(int)
	 */
	public DocTypeField[] getFieldsApp(int docTypeID) throws E5Exception
	{
        return getFields(docTypeID, DocTypeField.FIELD_APPLICATION);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.dom.DocTypeReader#getFieldsUser(int)
	 */
	public DocTypeField[] getFieldsUser(int docTypeID) throws E5Exception
	{
	       return getFields(docTypeID, DocTypeField.FIELD_USER);
	}

    /* (non-Javadoc)
     * @see com.founder.e5.dom.DocTypeReader#getFieldsOrderBy(int, java.lang.String, java.lang.String)
     */
    public DocTypeField[] getFieldsOrderBy(int typeID, String fieldName, String order) throws E5Exception {
        
        DocTypeField[] fields = DomUtils.EMPTY_DOCTYPEFIELD_ARRAY;
        List fieldList = DAOHelper.find("from DocTypeField as field where field.docTypeID=:doctypeid and field.status='P' " +
                                        "order by field." + fieldName + " " + order, 
                                        new Integer(typeID),
                                        Hibernate.INTEGER);
        
        if (!fieldList.isEmpty()) {
            fields = (DocTypeField[]) fieldList.toArray(DomUtils.EMPTY_DOCTYPEFIELD_ARRAY);
        }
        
        return fields;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DocTypeReader#getIDs(int)
     */
    public int[] getIDs(int appID) throws E5Exception {
        
        DocType[] types = this.getTypes(appID);
        
        int[] typeIDs = new int[types.length];
        
        for ( int i = 0 ; i < typeIDs.length ; i++ ){
            typeIDs[i] = types[i].getDocTypeID();            
        }
        
        return typeIDs;
        
    }

    /* (non-Javadoc)
     * @see com.founder.e5.dom.DocTypeReader#getSysFields(int)
     */
    public DocTypeField[] getSysFields(int docTypeID) throws E5Exception {
        
        DocTypeField[] fields = DomUtils.EMPTY_DOCTYPEFIELD_ARRAY;
        List fieldList = DAOHelper.find("from DocTypeField as field where field.attribute=1 and field.docTypeID=:doctype and field.status='P' order by field.fieldID",
                                        new Integer(docTypeID), Hibernate.INTEGER);
        if (!fieldList.isEmpty()) {
            fields = (DocTypeField[]) fieldList.toArray(DomUtils.EMPTY_DOCTYPEFIELD_ARRAY);
        }
        return fields;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DocTypeReader#getTypes(int)
     */
    public DocType[] getTypes(int appID) throws E5Exception {
        
        String sql = null;
        
        List typeList = new ArrayList();
        
        if(appID == 0){
            sql = "from DocType as dt order by dt.docTypeID";
            typeList = DAOHelper.find(sql);
            
        }else{            
            sql = "from DocTypeApps as dta where dta.appID=:appid order by dta.docTypeID";
            List typeAppsList = DAOHelper.find(sql, new Integer(appID), Hibernate.INTEGER);
            
            if (!typeAppsList.isEmpty()) {                
                for (Iterator iter = typeAppsList.iterator(); iter.hasNext();) {
                    DocTypeApps typeApps = (DocTypeApps) iter.next();
                    typeList.add(this.get(typeApps.getDocTypeID()));
                }
            }}
        
        return (DocType[]) typeList.toArray(DomUtils.EMPTY_DOCTYPE_ARRAY);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DocTypeManager#saveTemplateXMLByAppID(int)
     */
    public String saveTemplateXMLByAppID(int appID) {
        return "";
        // TODO:还未完成的工作，2005-7-14 9:27:12
    }

    /**
     * 
     * @param appID
     * @param xmlFileName
     *            xmlFileName
     */
    public String saveTemplateXMLByAppID(int appID, String xmlFileName) {
        return "";
        // TODO:还未完成的工作，2005-7-14 9:27:22
    }

    /**
     * 
     * @param docTypeID
     *            docTypeID
     */
    public String saveTemplateXMLByDocTypeID(int docTypeID) {
        return "";
        // TODO:还未完成的工作，2005-7-14 9:27:27

    }

    /**
     * 
     * @param docTypeID
     * @param xmlFileName
     *            xmlFileName
     */
    public String saveTemplateXMLByDocTypeID(int docTypeID, String xmlFileName) {
        return "";
        // TODO:还未完成的工作，2005-7-14 9:27:32
    }

    /* (non-Javadoc)
     * @see com.founder.e5.dom.DocTypeManager#setDefaultFlow(int, int)
     */
    public void setDefaultFlow(int docTypeID, int flowID) throws E5Exception {
        
        DocType type = this.get(docTypeID);
        
        BaseDAO dao = new BaseDAO();
        Session session = null;
        
        try{
            session = dao.getSession();
            type.setDefaultFlow(flowID);
            session.update(type);
            
            session.flush();
            
        } catch (HibernateException e){
            throw new E5Exception(e);
        }
        finally{
            dao.closeSession(session);
        }
        
    }

    /* (non-Javadoc)
     * @see com.founder.e5.dom.DocTypeManager#update(com.founder.e5.dom.DocTypeField)
     */
    public void update(DocTypeField docTypeField) throws E5Exception {

        int fieldID = docTypeField.getFieldID();
        BaseDAO dao = new BaseDAO();
        Session session = null;
        Transaction tx = null;

        try {
            
            DocTypeField oriField = this.getField(fieldID);
            oriField.setColumnName(docTypeField.getColumnName());
            oriField.setReadonly(docTypeField.getReadonly());
            oriField.setEditType(docTypeField.getEditType());
            oriField.setOptions(docTypeField.getOptions());
            
            session = dao.getSession();
            tx = dao.beginTransaction(session);
            session.update(oriField);
            session.flush();
            
            //暂时去掉下面的逻辑，因为更新文档类型字段时只更新其显示名columnName

            // 下面的逻辑应该是往DocTypeFieldApps表中插入新记录
//            int docTypeID = oriField.getDocTypeID();
//            int appid = this.getAppID(docTypeID);
//            
//            DocLibManager docLibMgr = (DocLibManager) ContextFacade.getBean(DocLibManager.class);
//            
//            int[] docLibIDs = docLibMgr.getIDsByTypeID(appid);
//            
//            for (int i = 0; i < docLibIDs.length; i++) {
//                int doclibid = docLibIDs[i];
//                DocTypeFieldApps fieldApps = new DocTypeFieldApps(appid, doclibid, docTypeID, fieldID, 0, 0);
//                session.save(fieldApps);
//            }

            dao.commitTransaction(tx);
            
        } catch (HibernateException e) {
            if (tx != null)
                try {
                    tx.rollback();
                } catch (HibernateException e1) {
                    e1.printStackTrace();
                }
            throw new E5Exception(e);
        } finally {
            try {
                dao.closeSession(session);
            } catch (HibernateException e1) {
                e1.printStackTrace();
            }
        }
        return;
    }

    /* (non-Javadoc)
     * @see com.founder.e5.dom.DocTypeManager#update(com.founder.e5.dom.DocType)
     */
    public void updateTypeRelation(int typeid, String relatedIDs) throws E5Exception {
        
        BaseDAO dao = new BaseDAO();
        Session session = null;
        Transaction tx = null;
        
        try{
            session = dao.getSession();
            tx = dao.beginTransaction(session);
            
            DocType oriType = (DocType) session.get(DocType.class, new Integer(typeid));
            oriType.setDocTypeRelated(relatedIDs);
            
            session.flush();
            dao.commitTransaction(tx);

        } catch (HibernateException e){
            throw new E5Exception(e);
        }finally{
            dao.closeSession(session);
        }
        
    }

    public DocTypeManagerImpl() {

    }
}