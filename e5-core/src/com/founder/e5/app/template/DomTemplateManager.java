package com.founder.e5.app.template;

import gnu.trove.TIntArrayList;
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TObjectIntHashMap;

import java.io.File;
import java.lang.reflect.Field;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.hibernate.Session;

import com.founder.e5.cat.CatManager;
import com.founder.e5.commons.Log;
import com.founder.e5.context.BaseDAO;
import com.founder.e5.context.BaseField;
import com.founder.e5.context.Context;
import com.founder.e5.context.DSManager;
import com.founder.e5.context.E5DataSource;
import com.founder.e5.context.E5Exception;
import com.founder.e5.db.ColumnInfo;
import com.founder.e5.db.DBSession;
import com.founder.e5.db.IResultSet;
import com.founder.e5.dom.DocLib;
import com.founder.e5.dom.DocLibManager;
import com.founder.e5.dom.DocType;
import com.founder.e5.dom.DocTypeField;
import com.founder.e5.dom.DocTypeManager;
import com.founder.e5.dom.FVFilters;
import com.founder.e5.dom.FVRules;
import com.founder.e5.dom.Filter;
import com.founder.e5.dom.FilterManager;
import com.founder.e5.dom.Folder;
import com.founder.e5.dom.FolderManager;
import com.founder.e5.dom.FolderView;
import com.founder.e5.dom.Rule;
import com.founder.e5.dom.RuleManager;
import com.founder.e5.dom.View;
import com.founder.e5.dom.ViewManager;
import com.founder.e5.dom.facade.ContextFacade;
import com.founder.e5.dom.facade.EUIDFacade;
import com.founder.e5.dom.template.TriggerTemplateManager;
import com.founder.e5.dom.util.DomUtils;
import com.founder.e5.rel.model.RelTable;
import com.founder.e5.rel.model.RelTableDocLibFields;
import com.founder.e5.rel.model.RelTableDocLibVO;
import com.founder.e5.rel.model.RelTableField;
import com.founder.e5.rel.model.RelTableVO;
import com.founder.e5.rel.service.RelTableDocLibFieldsManager;
import com.founder.e5.rel.service.RelTableDocLibManager;
import com.founder.e5.rel.service.RelTableManager;

/**
 * 文档管理部分的模板管理器。
 * 包括文档类型、文档库、文件夹、规则、过滤器等的加载、卸载等。
 * @version 1.0
 * @created 11-七月-2005 09:47:53
 */
public class DomTemplateManager implements IAppTemplateManager {
    
    private static Log logger = Context.getLog("e5.sys");
    
    private DSManager dsMgr = Context.getDSManager();
    private DocTypeManager docTypeMgr = (DocTypeManager) Context.getBean(DocTypeManager.class);
    private RelTableManager relTableMgr = (RelTableManager) Context.getBean(RelTableManager.class);
    private FilterManager filterMgr = (FilterManager) Context.getBean(FilterManager.class);
    private DocLibManager libMgr = (DocLibManager) Context.getBean(DocLibManager.class);
    private RelTableDocLibManager relTableDocLibMgr = (RelTableDocLibManager) Context.getBean(RelTableDocLibManager.class);
    private FolderManager folderMgr = (FolderManager) Context.getBean(FolderManager.class);
    private ViewManager viewMgr = (ViewManager) Context.getBean(ViewManager.class);
    private RuleManager ruleMgr = (RuleManager) Context.getBean(RuleManager.class);
    private RelTableDocLibFieldsManager fieldsMgr = (RelTableDocLibFieldsManager) Context.getBean(RelTableDocLibFieldsManager.class);
    private CatManager catMgr = (CatManager) Context.getBean(CatManager.class);
    
    public DomTemplateManager(){

	}
	
	/* (non-Javadoc)
	 * @see com.founder.e5.app.template.IAppTemplateManager#load(int, java.lang.String)
	 */
	public int load(int appID, String templateFile) 
	throws E5Exception
	{
	    SAXReader reader = new SAXReader();
	    Document doc = null;
        try {
			doc = reader.read(new File(templateFile));
		} catch (DocumentException e) {
			throw new E5Exception("Invalid Template File", e);
		}
		return load(appID, doc);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.app.template.IAppTemplateManager#load(int, org.dom4j.Document)
	 */
	public int load(int appID, Document doc) {
        
        try{
            //加载文档类型
            loadDocTypes(appID,doc);
            
            //加载过滤器
            loadFilters(doc);
            
            //加载规则
            loadRules(doc);
            
            //加载数据源
            loadDataSources(doc);
            
            //加载文档库
            loadDocLibs(doc);
            
            //加载文件夹视图
            loadFolderViews(doc);
            
            //加载分类关联表
            loadRelTables(doc);
            
            //加载分类关联表对应
            loadRelTableDocLibs(doc);

        } catch (Exception e){
            logger.error("",e);
        }
        
        return 0;
	}
    
    private int getFVID(String fvName, int doclibID) throws E5Exception{
        FolderView[] fvs = folderMgr.getChildrenFVsByDocLib(doclibID);
        
        for( int i = 0; i < fvs.length; i++){
            FolderView fv = fvs[i];
            if(fv.getFVName().equals(fvName))
                return fv.getFVID();
        }
        return 0;
    }

    private void loadFolderViews(Document doc) throws E5Exception {

        List fvs = doc.selectNodes("//FolderViews/FolderView");
        if (fvs == null || fvs.size() == 0) return; 

        //缓存所有的文件夹，包括根文件夹，不包括视图，用来导入某个文件夹时，根据父文件夹名称获得父文件夹ID
        TObjectIntHashMap fvName_fvID_Map = new TObjectIntHashMap();
        
        for( Iterator iter = fvs.iterator(); iter.hasNext();){
        
            Element fvElement = (Element) iter.next();
            //判断是否根节点
            String treeLevel = fvElement.attributeValue("TreeLevel");
            String parentName = fvElement.attributeValue("Parent");
            String folderName = fvElement.attributeValue("Name");
            String doclibName = fvElement.attributeValue("DocLib");   
            DocLib docLib = getDocLib(doclibName);
            int doclibID = docLib.getDocLibID();
            int docTypeID = docLib.getDocTypeID();

            //规则            
            FVRules[] fvRules = getFVRules(docTypeID, fvElement);
            //过滤器
            FVFilters[] fvFilters = getFVFilters(docTypeID, fvElement);
            
            //如果是根节点，则对应文件夹已经存在，只需要把根文件夹放入缓存以备子孙文件夹的创建，并导入根文件夹的过滤器规则
            if(treeLevel.equals("0"))
                if(parentName == null || parentName.length()==0){
                    
                    //把根文件夹放入缓存，但不真的创建根文件夹，因为导入文档库时根文件夹已经创建
                    int rootID = getFVID(folderName, doclibID);
                    fvName_fvID_Map.put(folderName,rootID);
                    
                    //导入根文件夹的过滤器规则
                    loadRootFiltersAndRules(fvRules, fvFilters, rootID);
                    continue;
                }
            
            //如果是非根节点，则创建文件夹视图            
            String treeOrder = fvElement.attributeValue("TreeOrder");
            String keepDay = fvElement.attributeValue("KeeyDay");
            
            int parentID = fvName_fvID_Map.get(parentName);
            
            String type = fvElement.attributeValue("Type");
            if(type.equals("1")){
                
                //创建文件夹                
                Folder folder = newFolder(treeLevel, folderName, treeOrder, keepDay, parentID);
                int folderID = folderMgr.create(folder,fvRules,fvFilters);
                
                fvName_fvID_Map.put(folderName,folderID);
                
            }else{
                //创建视图
                String viewFormula = fvElement.attributeValue("ViewFormula");
                
                View view = newView(treeLevel, folderName, treeOrder, keepDay, parentID, viewFormula);
                viewMgr.create(view,fvRules,fvFilters);
            }
        }
    }

    /**
     * @param fvRules
     * @param fvFilters
     * @param rootID
     */
    private void loadRootFiltersAndRules(FVRules[] fvRules, FVFilters[] fvFilters, int rootID) {
        
        BaseDAO dao = new BaseDAO();                    
        
        for( int i = 0; i < fvFilters.length; i++){
            FVFilters fvFilter = fvFilters[i];
            fvFilter.setFvID(rootID);
            dao.save(fvFilter);
        }
        
        for( int i = 0; i < fvRules.length; i++){
            FVRules fvRule = fvRules[i];
            fvRule.setFvID(rootID);
            dao.save(fvRule);
        }
    }

    /**
     * @param treeLevel
     * @param folderName
     * @param treeOrder
     * @param keepDay
     * @param parentID
     * @param parentFolder
     * @param docTypeID
     * @param doclibID
     * @param viewFormula
     * @return
     * @throws E5Exception 
     */
    private View newView(String treeLevel, String folderName, String treeOrder, String keepDay, int parentID, String viewFormula) throws E5Exception {
        
        Folder parentFolder = (Folder) folderMgr.get(parentID);                
        int docTypeID = parentFolder.getDocTypeID();
        int doclibID = parentFolder.getDocLibID();
        
        View view = new View();
        view.setParentID(parentID);        
        view.setDocTypeID(docTypeID);
        view.setDocLibID(doclibID);
        
        view.setFVName(folderName);        
        view.setKeepDay(Integer.parseInt(keepDay));
        view.setDefaultLayoutID(parentFolder.getDefaultLayoutID());
        
        view.setRootID(parentFolder.getRootID());
        view.setTreeLevel(Integer.parseInt(treeLevel));
        view.setTreeOrder(Integer.parseInt(treeOrder));        
        view.setViewFormula(viewFormula);
        return view;
    }

    /**
     * @param treeLevel
     * @param folderName
     * @param treeOrder
     * @param keepDay
     * @param parentID
     * @param parentFolder
     * @param docTypeID
     * @param doclibID
     * @return
     * @throws E5Exception 
     */
    private Folder newFolder(String treeLevel, String folderName, String treeOrder, String keepDay, int parentID) throws E5Exception {
        
        Folder parentFolder = (Folder) folderMgr.get(parentID);                
        int docTypeID = parentFolder.getDocTypeID();
        int doclibID = parentFolder.getDocLibID();
        
        Folder folder = new Folder();
        
        folder.setParentID(parentID);        
        folder.setDocTypeID(docTypeID);
        folder.setDocLibID(doclibID);
        
        folder.setFVName(folderName);        
        folder.setKeepDay(Integer.parseInt(keepDay));
        folder.setDefaultLayoutID(parentFolder.getDefaultLayoutID());
        
        folder.setRootID(parentFolder.getRootID());
        folder.setTreeLevel(Integer.parseInt(treeLevel));
        folder.setTreeOrder(Integer.parseInt(treeOrder));
        
        return folder;
    }

    private FVFilters[] getFVFilters(int docTypeID, Element fvElement) throws E5Exception {
        
        List filterElements = fvElement.selectNodes("Filters/Filter");
        List fvFilterList = new ArrayList();
        
        for( Iterator iter = filterElements.iterator(); iter.hasNext();){
            Element filterElement = (Element) iter.next();
            
            String filterName = filterElement.attributeValue("Name");
            Filter filter = getFilter(docTypeID, filterName);
            String index = filterElement.attributeValue("index");
            String group = filterElement.attributeValue("group");
            
            FVFilters fvFilter = new FVFilters();
            fvFilter.setDefaultFlag(Integer.parseInt(group));
            fvFilter.setDocTypeID(filter.getDocTypeID());
            fvFilter.setFilterID(filter.getFilterID());
            fvFilter.setIndex(Integer.parseInt(index));
            
            fvFilterList.add(fvFilter);
        }
        return (FVFilters[]) fvFilterList.toArray(new FVFilters[0]);
    }

    private Filter getFilter(int docTypeID, String filterName) throws E5Exception {
        Filter[] filters = filterMgr.getByTypeID(0);
        for( int i = 0; i < filters.length; i++){
            Filter filter = filters[i];
            if(filter.getDocTypeID() == docTypeID 
            		&& filter.getFilterName().equals(filterName)){
                return filter;
            }
        }
        return null;
    }

    private FVRules[] getFVRules(int docTypeID, Element fvElement) throws E5Exception {
        
        List ruleElements = fvElement.selectNodes("Rules/Rule");
        List ruleList = new ArrayList();
        
        for( Iterator iterator = ruleElements.iterator(); iterator.hasNext();){
            Element ruleElement = (Element) iterator.next();
            
            String ruleName = ruleElement.attributeValue("Name");
            int ruleID = getRuleID(docTypeID, ruleName);
            String subIndex = ruleElement.attributeValue("subIndex");
            
            FVRules fvRule = new FVRules();
            fvRule.setRuleID(ruleID);
            fvRule.setSubIndex(Integer.parseInt(subIndex));
            ruleList.add(fvRule);
        }
        return (FVRules[]) ruleList.toArray(new FVRules[0]);
    }

    private int getRuleID(int docTypeID, String ruleName) throws E5Exception 
    {
        Rule[] rules = ruleMgr.getByDoctypeID(0);
        for( int i = 0; i < rules.length; i++)
        {
            if(rules[i].getDocTypeID() == docTypeID 
            		&& rules[i].getRuleName().equals(ruleName))
            {
                return rules[i].getRuleID();
            }
        }
        return 0;
    }

    private void loadRelTables(Document doc) throws Exception {
        
        List relTableElements = doc.selectNodes("//RelTables/RelTable");
        if (relTableElements == null || relTableElements.size() == 0) return; 
        
        for( Iterator iter = relTableElements.iterator(); iter.hasNext();){
            Element relTableElement = (Element) iter.next();
            
            String docTypeName = relTableElement.attributeValue("DocType");
            int docTypeID = getDocTypeID(docTypeName);
            if (docTypeID == 0)
            {
            	logger.warn("[DomTempate Load RelTables]Unavailable doctype, ignore:" + docTypeName);
            	continue;
            }
            
            String dsName = relTableElement.attributeValue("DataSource");
            int dsID = getDSID(dsName);
            
            String suffixName = relTableElement.attributeValue("SuffixTableName");
            String name = relTableElement.attributeValue("Name");
            
            //用于创建分类关联表的扩展字段，不包括Class字段和平台字段
            int[] fieldIDs = getFieldIDs(relTableElement, docTypeID);
            RelTableField[] appendFields = getAppendFields(relTableElement, docTypeID);
            //创建分类关联表
            RelTable relTable = createRelTable(docTypeID, dsID, suffixName, name, fieldIDs, appendFields);
            
            List indexSqlList = relTableElement.elements("IndexSQL");
            createRelIndexSql(relTable, indexSqlList);
        }
        
    }
    private void createRelIndexSql(RelTable relTable,List indexSql) throws E5Exception
    {
    	if(indexSql.isEmpty()) return;
    	
        int dsID = relTable.getDsID().intValue();
        DBSession dbss = null;   
        try
        {
        	dbss = ContextFacade.getDBSession(dsID);
	    	for (int i = 0; i < indexSql.size(); i++)
	    	{
    			try {
		    		Element e = (Element)indexSql.get(i);
		    		String index = e.getText();
		    		if (index != null && !"".equals(index.trim())) {
		    			index = index.replaceAll("[$]DOCLIB[$]", relTable.getTableName());
						logger.info("rel table index sql:" + index);
						dbss.executeUpdate(index, null);
		    		}
    			} catch(Exception ex) {
    	        	logger.error(ex);
    	        }
	    	}
        } catch(Exception ex) {
        	logger.error(ex);
        } finally {
        	if (dbss != null) dbss.closeQuietly();
        }
    }

    /**
     * @param docTypeID
     * @param dsID
     * @param suffixName
     * @param name
     * @param fieldIDs
     * @throws E5Exception
     * @throws Exception
     */
    private RelTable createRelTable(int docTypeID, int dsID, String suffixName, 
    		String name, int[] fieldIDs, RelTableField[] appendFields) throws E5Exception , Exception {
        
        RelTableVO vo = new RelTableVO();
        vo.setDsID(dsID);
        vo.setRefDocTypeID(docTypeID);
        vo.setTableName("DOM_REL_"+suffixName);
        vo.setName(name);
        vo.setFiledIds(fieldIDs);
        String ddl = this.relTableMgr.genCreateDDL(vo);
        
        RelTable table = new RelTable();
        table.setDsID(new Integer(dsID));
        table.setName(name);
        table.setRefDocTypeID(new Integer(docTypeID));
        table.setTableName("DOM_REL_"+suffixName);
        
        int id = EUIDFacade.getID(EUIDFacade.IDTYPE_REL_TABLE);
        table.setId(new Integer(id));

        relTableMgr.createRelTable(ddl,table);
        
        appendRelTable(table, appendFields);
        
        return table;
    }
    
    private void appendRelTable(RelTable table, RelTableField[] fields) throws Exception 
    {
        String tableName = table.getTableName();

        int dsId = table.getDsID().intValue();
        DBSession dbsession = null;
        try
        {
	        dbsession = Context.getDBSession(dsId);
	
	        for (int i = 0; i < fields.length; i++) {
	        	RelTableField field = fields[i];

		        ColumnInfo c = new ColumnInfo();
		        c.setName(field.getFieldName());
		        c.setDataLength(field.getDataLength());
		        c.setDataPrecision(field.getScale());
		        c.setE5TypeName(field.getDataType());
		        c.setDefaultValue(field.getDefaultValue());
		        c.setNullable((field.getIsNull() == 1));
		        
		        String ddl = dbsession.getDialect().getColumnDDL(c);
		        
	        	StringBuffer alterSQLBuf = new StringBuffer();
		        alterSQLBuf.append("alter table ").append(tableName)
		        	.append(" add ");
		        alterSQLBuf.append(ddl);
		
		        dbsession.executeUpdate(alterSQLBuf.toString(), null);
			}
		} finally {
			if (dbsession != null) dbsession.closeQuietly();
		}
    }

    /**
     * @param relTableElement
     * @param docTypeID
     * @return 
     * @throws E5Exception
     */
    private int[] getFieldIDs(Element relTableElement, int docTypeID) throws E5Exception {
        
        List fieldElements = relTableElement.selectNodes("Fields/Field");
        TIntArrayList fieldIDList = new TIntArrayList();
        
        DocTypeField[] fields = docTypeMgr.getFields(docTypeID);
        
        for( Iterator iterator = fieldElements.iterator(); iterator.hasNext();){
            Element fieldElement = (Element) iterator.next();

            //若一个字段重新定义了类型，则单独加载
            String type = fieldElement.attributeValue("Type");
            if (!com.founder.e5.commons.StringUtils.isBlank(type)) 
            	continue;
            
           int fieldID = getFieldID(fields, fieldElement.attributeValue("Name"));
            if (fieldID > 0)
            	fieldIDList.add(fieldID);
        }
        
        return fieldIDList.toNativeArray();
    }

    private int getFieldID(DocTypeField[] fields, String name)
    {
    	for (int i = 0; i < fields.length; i++)
		{
			if (name.equalsIgnoreCase(fields[i].getColumnCode()))
				return fields[i].getFieldID();
		}
    	return 0;
    }
    
    //分类关联表加载时，可以加载自定义的字段。
    private RelTableField[] getAppendFields(Element relTableElement, int docTypeID) throws E5Exception {
        List fieldIDList = new ArrayList(10);
        
        List fieldElements = relTableElement.selectNodes("Fields/Field");
        
        for( int i = 0; i < fieldElements.size(); i++){
            Element fieldElement = (Element)fieldElements.get(i);

            String type = fieldElement.attributeValue("Type");
            
            //若一个字段重新定义了类型，则单独加载
            if (!com.founder.e5.commons.StringUtils.isBlank(type)) {
                RelTableField field = new RelTableField();
            	field.setDataType(type);
            	field.setFieldName(fieldElement.attributeValue("Name"));
            	field.setDefaultValue(fieldElement.attributeValue("DefaultValue"));
            	
            	if ("false".equals(fieldElement.attributeValue("Nullable")))
            		field.setIsNull(0); 
            	else
            		field.setIsNull(1);//1: nullable

            	String value = fieldElement.attributeValue("Length");
            	if (!com.founder.e5.commons.StringUtils.isBlank(value))
            		field.setDataLength(Integer.parseInt(value));
            	
            	value = fieldElement.attributeValue("Scale");
            	if (!com.founder.e5.commons.StringUtils.isBlank(value))
            		field.setScale(Integer.parseInt(value));
            	
            	fieldIDList.add(field);
            }
        }
        return (RelTableField[])fieldIDList.toArray(new RelTableField[0]);
    }

    private void loadRelTableDocLibs(Document doc) throws Exception {
        
        List relTableDocLibElements = doc.selectNodes("//RelTableDocLibs/RelTableDocLib");
        if (relTableDocLibElements == null || relTableDocLibElements.size() == 0) return; 

        for( Iterator iter = relTableDocLibElements.iterator(); iter.hasNext();){
            Element relTableDocLibElement = (Element) iter.next();
            
            String relTableName = relTableDocLibElement.attributeValue("RelTableName");
            String doclibName = relTableDocLibElement.attributeValue("DocLibName");
            String catTypeName = relTableDocLibElement.attributeValue("CatType");
            String categoryField = relTableDocLibElement.attributeValue("CategoryField");
            String ignoreFlag = relTableDocLibElement.attributeValue("IgnoreFlag");
            
            String[] fields = getRelatedFields(relTableDocLibElement);
            
            int doclibID = getDocLibID(doclibName);
            int relTableID = getRelTableID(relTableName);
            int catTypeID = getCatTypeId(catTypeName);
            
            int dsId = libMgr.get(doclibID).getDsID();
            E5DataSource ds = dsMgr.get(dsId);
            String dbType = ds.getDbType();
            
            TriggerTemplateManager templateMgr = TriggerTemplateManager.getTemplateManager(dbType);
            //获取分类关联表的名称后缀
            String suffix = relTableName.substring(8);
            String triggerContent = templateMgr.getTriggerContent(doclibID, suffix, categoryField, fields,Integer.parseInt(ignoreFlag));
            
            // 替换触发器文件中的参数
            logger.info("The trigger to load : "+triggerContent);
            
            //获取指定类型的DBSession，创建触发器
            // 调用liyh的单条执行接口
            DBSession dbSession = Context.getDBSession(ds);
            try
            {
            	dbSession.executeDDL(triggerContent);
            }
            finally{
            	dbSession.closeQuietly();
            }
            
            //保存记录
            RelTableDocLibVO vo = new RelTableDocLibVO(relTableID,doclibID,catTypeID,categoryField,Integer.parseInt(ignoreFlag));
            this.fieldsMgr.remove( doclibID,catTypeID);
            
            List fieldElements = relTableDocLibElement.selectNodes("RelatedFields/RelatedField");
            for( Iterator iterator = fieldElements.iterator(); iterator.hasNext();){
                Element fieldElement = (Element) iterator.next();
                
                String tableField = fieldElement.attributeValue("RelTableField");
                String libField = fieldElement.attributeValue("DocLibField");
                RelTableDocLibFields relatedFields = new RelTableDocLibFields(new Integer(doclibID),
                		new Integer(catTypeID),
                		tableField,
                		libField);
                this.fieldsMgr.save(relatedFields);
                
            }
            
            relTableDocLibMgr.saveRelTableDocLib(vo);
        }
        
        
    }

    private int getCatTypeId(String catTypeName) throws E5Exception {
        return catMgr.getType(catTypeName).getCatType();
    }

    private int getRelTableID(String relTableName) {
        return relTableMgr.getRelTable(relTableName.substring(8)).getId().intValue();
    }

    private DocLib getDocLib(String doclibName) throws E5Exception {
        DocLib[] libs = libMgr.getByTypeID(0);
        for( int i = 0; i < libs.length; i++){
            DocLib lib = libs[i];
            if(lib.getDocLibName().equals(doclibName))
                return lib;
        }
        return null;
    }
    private int getDocLibID(String doclibName) throws E5Exception {
        
        DocLib lib = getDocLib(doclibName);
        if (lib != null) return lib.getDocLibID();
        return 0;
    }

    /**
     * @param relTableDocLibElement
     */
    private String[] getRelatedFields(Element relTableDocLibElement) {
        
        List fieldElements = relTableDocLibElement.selectNodes("RelatedFields/RelatedField");
        
        StringBuffer relTableFields = new StringBuffer();
        StringBuffer docLibFields = new StringBuffer();
        
        for( Iterator iterator = fieldElements.iterator(); iterator.hasNext();){
            Element fieldElement = (Element) iterator.next();
            
            relTableFields.append(fieldElement.attributeValue("RelTableField")).append(",");
            docLibFields.append(fieldElement.attributeValue("DocLibField")).append(",");            
        }
        
        if(relTableFields.length() > 0 )
            relTableFields.deleteCharAt(relTableFields.length()-1);
        if(docLibFields.length() >0)
            docLibFields.deleteCharAt(docLibFields.length()-1);
        
        return new String[]{relTableFields.toString(),docLibFields.toString()};
    }
    
    private int getDSID(String dsName) throws E5Exception{
        
        E5DataSource[] dz = dsMgr.getAll();
        for( int i = 0; i < dz.length; i++){
            E5DataSource ds = dz[i];
            if(ds.getName().equals(dsName))
                return ds.getDsID();            
        }
        return 0;
    }
    
    private int getDocTypeID(String docTypeName) throws E5Exception{
        DocType docType = docTypeMgr.get(docTypeName);
        return (docType == null)? 0: docType.getDocTypeID();
    }
    
    private void loadDocLibs(Document doc) throws E5Exception {
        
        List libElements = doc.selectNodes("//DocLibs/DocLib");
        if (libElements == null || libElements.size() == 0) return; 

        TObjectIntHashMap dsName_dsID_Map = new TObjectIntHashMap();
        TObjectIntHashMap docTypeName_docTypeID_Map = new TObjectIntHashMap();      
        
        for( Iterator iter = libElements.iterator(); iter.hasNext();){
            Element libElement = (Element) iter.next();
            
            String name = libElement.attributeValue("Name");
            String dsName = libElement.attributeValue("DataSource");
            String docTypeName = libElement.attributeValue("DocType");
            String keeyDay = libElement.attributeValue("KeeyDay");
            //扩展：支持为文档库指定表空间和索引SQL
            String extendSQL = null;
            Element extendSqlElement = libElement.element("ExtendSQL");
            if(extendSqlElement!=null) extendSQL = extendSqlElement.getText();
            List indexSqlList = libElement.elements("IndexSQL");
            
            DocLib lib = new DocLib();
            lib.setDocLibName(name);
            lib.setKeepDay((keeyDay == null || keeyDay.length() == 0)? 1 : Integer.parseInt(keeyDay));
            
            int dsID = dsName_dsID_Map.get(dsName);
            if(dsID <= 0){
                dsID = getDSID(dsName);
                dsName_dsID_Map.put(dsName,dsID);
            }
            lib.setDsID(dsID);
            
            int docTypeID = docTypeName_docTypeID_Map.get(docTypeName);
            if(docTypeID <=0){
                docTypeID = getDocTypeID(docTypeName);
                docTypeName_docTypeID_Map.put(docTypeName,docTypeID);
            }
            lib.setDocTypeID(docTypeID);

            //create
            String ddl = libMgr.generateDDL(lib);
            
            //extendSQL
            if(extendSQL!=null) ddl = ddl + extendSQL;
            if(ddl.endsWith(";")) ddl = ddl.substring(0, ddl.length()-1);
            
            logger.info("create doclib sql="+ddl);
            
            libMgr.create(ddl,lib);
            
            //create doclib index
            createIndexSql(lib,indexSqlList);
        }
        
    }
    
    private void createIndexSql(DocLib doclib,List indexSql) throws E5Exception
    {
    	if(indexSql.isEmpty()) return;
    	
        int dsID = doclib.getDsID();
        DBSession dbss = null;   
        try
        {
        	dbss = ContextFacade.getDBSession(dsID);
	    	for(int i=0;i<indexSql.size();i++)
	    	{
    	        try {
		    		Element e = (Element)indexSql.get(i);
		    		String index = e.getText();
		    		if (index != null && !"".equals(index.trim())) {
		    			index = index.replaceAll("[$]DOCLIB[$]", doclib.getDocLibTable());
		    			logger.info("index sql:" + index);
						dbss.executeDDL(index);
		    		}
    	        } catch(Exception ex) {
    	        	logger.error(ex);
    	        }
	    	}
        }
        catch(Exception ex)
        {
        	logger.error(ex);
        }
        finally
        {
        	if(dbss!=null)
        		dbss.closeQuietly();
        }
    }
    
    private void loadDataSources(Document doc) throws E5Exception {
        
        List dsElements = doc.selectNodes("//DataSources/DataSource");
        if (dsElements == null || dsElements.size() == 0) return; 
        
        for( Iterator iter = dsElements.iterator(); iter.hasNext();){
            
            Element dsElement = (Element) iter.next();
            
            String name = dsElement.attributeValue("Name");
            String dataSource = dsElement.elementText("Datasource");
            String dbServer = dsElement.elementText("DBServer");
            String db = dsElement.elementText("DB");
            String dbUser = dsElement.elementText("DBUser");
            String dbPassword = dsElement.elementText("DBPassword");
            String dbType = dsElement.elementText("DBType");
            
            E5DataSource ds = new E5DataSource(name,dataSource,dbType,dbServer,db,dbUser,dbPassword);
            try {
				dsMgr.create(ds);
			} catch (E5Exception e) {
				logger.error("Datasource create error!", e);
			}
        }
        
    }

    /**
     * 2008-3-28 Gong Lijie
     * 修改为不依赖于DocumentType节点，可单独加载
     	<FILTERS>
			<DOCTYPE NAME="待编稿件">
				<FILTER NAME="时间方式" FORMULA="" DESCRIPTION=""/>
				<FILTER NAME="最近一天" FORMULA="DOC_SOURCETYPEID_EQ_10" DESCRIPTION="查看一天内的数据"/>
			</DOCTYPE>
			<DOCTYPE NAME="资料稿件">
				<FILTER NAME="来源方式" FORMULA="" DESCRIPTION=""/>
				<FILTER NAME="外电" FORMULA="DOC_SOURCETYPEID_EQ_29" DESCRIPTION="查看外电数据"/>
			</DOCTYPE>
		</FILTERS>
     	<FILTERS>
			<DOCTYPE NAME="待编稿件2">
				<FILTER NAME="时间方式" FORMULA="" DESCRIPTION=""/>
				<FILTER NAME="最近一天" FORMULA="DOC_SOURCETYPEID_EQ_10" DESCRIPTION="查看一天内的数据"/>
			</DOCTYPE>
			<DOCTYPE NAME="资料稿件2">
				<FILTER NAME="来源方式" FORMULA="" DESCRIPTION=""/>
				<FILTER NAME="外电" FORMULA="DOC_SOURCETYPEID_EQ_29" DESCRIPTION="查看外电数据"/>
			</DOCTYPE>
		</FILTERS>
     * <br/>
     * 2006-3-16 10:13:44
     * @author zhang_kaifeng
     * @param docType 
     * @param doc
     * @throws E5Exception 
     */
    private void loadFilters(Document doc) throws E5Exception {
        //List filterNodes = doc.selectNodes("//FILTERS/DOCTYPE[@NAME='"+ docTypeName + "']/FILTER");
        List filters = doc.selectNodes("//FILTERS");
        if (filters == null || filters.size() == 0) return; 

        //可以有多个FILTERS节点
        for( Iterator iter = filters.iterator(); iter.hasNext();){
            Element filterDocType = (Element) iter.next();

            List docTypes = filterDocType.selectNodes("DOCTYPE");
            if (docTypes == null || docTypes.size() == 0) continue;
            
            //每个FILTERS节点下，可以注册多个文档类型的过滤器
            for (int i = 0; i < docTypes.size(); i++) {
				Element docTypeNode = (Element)docTypes.get(i);
				
				String docTypeName = docTypeNode.attributeValue("NAME");
				DocType docType = getDocType(docTypeName);
				if (docType == null)
				{
					logger.warn("DocType cannnot be found:" + docTypeName);
					continue;
				}
				//每个文档类型有多个过滤器
				List docType_filters = docTypeNode.selectNodes("FILTER");
	            if (docType_filters == null || docType_filters.size() == 0) continue;
	            
	            for (int j = 0; j < docType_filters.size(); j++) {
	            	Element filter = (Element)docType_filters.get(j);
	            	loadFilter(docType.getDocTypeID(), filter);
				}
			}
        }
    }
    private void loadFilter(int docTypID, Element filterE) throws E5Exception {
        Filter filter = new Filter();

        filter.setDocTypeID(docTypID);
        
        String name = filterE.attributeValue("NAME");
        filter.setFilterName(name);
        
        String formula = filterE.attributeValue("FORMULA");
        filter.setFormula(formula);
        
        String description = filterE.attributeValue("DESCRIPTION");
        filter.setDescription(description);
        
        filterMgr.create(filter);
    }
    private DocType getDocType(String docTypeName)
    {
    	try {
			return docTypeMgr.get(docTypeName);
		} catch (E5Exception e) {
			return null;
		}
    }

    /**
     * 2008-3-28 Gong Lijie
     * 修改为不依赖于DocumentType节点，可单独加载
		<RULES>
			<DOCTYPE NAME="...">
				<RULE NAME="..." CLASSNAME="..." METHOD="..."/>
				<RULE NAME="..." CLASSNAME="..." METHOD="..."/>
			</DOCTYPE>
			<DOCTYPE NAME="...">
				<RULE NAME="..." CLASSNAME="..." METHOD="..."/>
				<RULE NAME="..." CLASSNAME="..." METHOD="..."/>
			</DOCTYPE>
		</RULES>
		<RULES>
			<DOCTYPE NAME="...">
				<RULE NAME="..." CLASSNAME="..." METHOD="..."/>
			</DOCTYPE>
		</RULES>

     * 2006-3-16 10:12:58
     * 
     * @author zhang_kaifeng
     * @param docType
     * @param doc
     * @throws E5Exception 
     */
    private void loadRules(Document doc) throws E5Exception {
        //List ruleNodes = doc.selectNodes("//RULES/DOCTYPE[@NAME='"+ docTypeName + "']/RULE");
        
        List rules = doc.selectNodes("//RULES");
        if (rules == null || rules.size() == 0) return; 

        //可以有多个RULES节点
        for( Iterator iter = rules.iterator(); iter.hasNext();){
            Element ruleDocType = (Element) iter.next();

            List docTypes = ruleDocType.selectNodes("DOCTYPE");
            if (docTypes == null || docTypes.size() == 0) continue;
            
            //每个RULES节点下，可以注册多个文档类型的规则
            for (int i = 0; i < docTypes.size(); i++) {
				Element docTypeNode = (Element)docTypes.get(i);
				
				String docTypeName = docTypeNode.attributeValue("NAME");
				DocType docType = getDocType(docTypeName);
				if (docType == null)
				{
					logger.warn("[Load Rule]DocType cannnot be found:" + docTypeName);
					continue;
				}
				//每个文档类型有多个规则
				List docType_rules = docTypeNode.selectNodes("RULE");
	            if (docType_rules == null || docType_rules.size() == 0) continue;
	            
	            for (int j = 0; j < docType_rules.size(); j++) {
	            	Element rule = (Element)docType_rules.get(j);
	            	loadRule(docType.getDocTypeID(), rule);
				}
			}
        }
    }
    private void loadRule(int docTypeID, Element ruleE) throws E5Exception {
        Rule rule = new Rule();
        rule.setDocTypeID(docTypeID);
        
        String name = ruleE.attributeValue("NAME");
        rule.setRuleName(name);
        
        String className = ruleE.attributeValue("CLASSNAME");
        rule.setRuleClassName(className);
        
        String description = ruleE.attributeValue("DESCRIPTION");
        rule.setDescription(description);
        
        String method = ruleE.attributeValue("METHOD");
        rule.setRuleMethod(method);
        
        String params = ruleE.attributeValue("VARPARAM");
        rule.setRuleArguments(params);
        
        ruleMgr.create(rule);
    }

    /**
     * 加载模板中的文档类型，包括基本属性，字段，过滤器，规则
     * 2006-3-16 10:11:50
     * @author zhang_kaifeng
     * @param appID 
     * @param doc
     */
    private void loadDocTypes(int appID, Document doc) {
        
        List docTypeNodes = doc.selectNodes("//DocumentType");
        
        if (null == docTypeNodes || docTypeNodes.isEmpty()) {
            logger.info("No document type.");
            return ;
        }
        
        for( Iterator iter = docTypeNodes.iterator(); iter.hasNext();){
            
            Element _docType = (Element) iter.next();
            
            DocType docType = null;
            
            try{
                //load properties
                docType = this.loadDocTypeProperties(appID,_docType);
                //load fields
                this.loadDocTypeFields(docType,_docType);
           } catch (Exception e){
                uninstallOnError(docType);
                logger.warn("occur Exception when load DocType: "+docType.getDocTypeName(),e);
            }
        }
    }

    /**
     * 如果加载某个文档类型时异常，则卸载所有刚刚加载的内容。
     * 2006-3-16 13:01:26
     * @author zhang_kaifeng
     * @param docType
     * @throws E5Exception
     */
    private void uninstallOnError(DocType docType){
        
        if(docType!= null ){
            int docTypeID = docType.getDocTypeID();
            
            if(docTypeID >0){
                
                try{
                    docTypeMgr.delete(docTypeID);
                } catch (E5Exception e){
                    logger.warn("uninstall DocType Exception",e);
                }
            }
        }
    }

	/**
     * 加载文档类型基本属性
	 * 2006-3-16 10:22:18
	 * @author zhang_kaifeng
	 * @param docType 
	 * @param _type
	 * @throws E5Exception 
	 */
	private void loadDocTypeFields(DocType docType, Element _type) throws E5Exception {
        
        int docTypeID = docType.getDocTypeID();
        
        Element fields = _type.element("Fields");
        List fieldList = fields.elements("Field");
        String tmpValue;
        for( Iterator iter = fieldList.iterator(); iter.hasNext();)
        {
            Element field = (Element) iter.next();

			DocTypeField typeField = new DocTypeField();
			typeField.setDocTypeID(docTypeID);

			tmpValue = field.elementText("ColumnCode");
			typeField.setColumnCode(tmpValue);

			tmpValue = field.elementText("ColumnName");
			typeField.setColumnName(tmpValue);

			tmpValue = field.elementText("DataType");
			typeField.setDataType(tmpValue);

			tmpValue = field.elementText("Length");
			typeField.setDataLength((tmpValue != null) ? Integer
					.parseInt(tmpValue) : 0);
			
			tmpValue = field.elementText("Scale");
			typeField.setScale((tmpValue != null) ? Integer.parseInt(tmpValue) : 0);
			
			tmpValue = field.elementText("IsNull");
			typeField.setIsNull((tmpValue != null) ? Integer.parseInt(tmpValue)
					: 0);

			tmpValue = field.elementText("Status");
			typeField.setStatus(tmpValue);
            
            // 子系统模板加载进来的字段都是应用级字段，2
            typeField.setAttribute(2);
//			tmpValue = field.elementText("Attribute");
//			typeField.setAttribute((tmpValue != null)?Integer.parseInt(tmpValue) :0);
            // 缺省值
			tmpValue = field.elementText("DefaultValue");
			if (tmpValue != null)
				typeField.setDefaultValue(tmpValue);

			// 填写方式
			tmpValue = field.elementText("EditType");
			if (!StringUtils.isBlank(tmpValue))
				typeField.setEditType(Integer.parseInt(tmpValue));
			// 枚举值
			tmpValue = field.elementText("Options");
			if (tmpValue != null)
				typeField.setOptions(tmpValue);
			// 只读?
			tmpValue = field.elementText("Readonly");
			if (!StringUtils.isBlank(tmpValue))
				typeField.setReadonly(Integer.parseInt(tmpValue));

			docTypeMgr.create(typeField);
        }
        
    }

    /**
	 * 加载文档类型字段 2006-3-16 10:22:38
	 * 
	 * @author zhang_kaifeng
	 * @param appID
	 * @param type
	 * @throws E5Exception
	 */
    private DocType loadDocTypeProperties(int appID, Element type) throws Exception {
        
        String typeName = type.elementText("Name");
        String description = type.elementText("Description");
        
        DocType docType = new DocType(typeName,description);
        docType.setAppID(appID);
        
        docTypeMgr.create(docType);
        return docType;
        
        
    }

/* (non-Javadoc)
     * @see com.founder.e5.app.template.IAppTemplateManager#exportTemplate(int)
     */
    public String exportTemplate(int appID)
    {
        //先取得子系统的文档类型
        DocType[] types = null;
        try {
            types = docTypeMgr.getTypes(appID);
        } catch (E5Exception e){ }
        if (types == null) return "";
        
        StringBuffer ret = new StringBuffer();
        try{
            // 导出文档类型
        	DomTemplateExport dtExport = new DomTemplateExport();
			String docTypesXML = dtExport.exportDocTypes(types);
			ret.append(docTypesXML);

			// 提取出文档类型的ID数组，传给接下来的导出方法
            int[] docTypeIDs = new int[types.length];
            for (int i = 0; i < docTypeIDs.length; i++) {
            	docTypeIDs[i] = types[i].getDocTypeID();
			}
            
            //导出数据源
            String dataSourcesXML = exportDataSources();
            ret.append(dataSourcesXML);
            
            //导出文档库
            String doclibsXML = exportDocLibs(docTypeIDs);
            if (doclibsXML != null)
            	ret.append(doclibsXML);
            
            // 导出文件夹视图
			String fvsXML = exportFolderViews(docTypeIDs);
			if (fvsXML != null)
				ret.append(fvsXML);
            
            // 导出分类关联表
			String relTablesXML = exportRelTables();
			ret.append(relTablesXML);

			// 导出分类关联表对应
			String relTableDocLibXML = exportRelTableDocLibs();
			ret.append(relTableDocLibXML);
            
        } catch (Exception e){
            logger.error(e);
        }
        
        return ret.toString();

    }

    private String exportRelTableDocLibs() throws E5Exception {
        
        RelTableDocLibVO[] vos = relTableDocLibMgr.getRelTableDocLibs();
        
        if(vos.length == 0)
            return "";
        
        Element relTableDocLibsElement = DocumentHelper.createElement("RelTableDocLibs");
        Document doc = DocumentHelper.createDocument(relTableDocLibsElement);
        
        for( int i = 0; i < vos.length; i++){
            RelTableDocLibVO vo = vos[i];
            
            Element relTableDocLibElement = relTableDocLibsElement.addElement("RelTableDocLib");
            
            relTableDocLibElement.addAttribute("RelTableName",vo.getRelTable().getTableName());
            relTableDocLibElement.addAttribute("DocLibName",vo.getDocLib().getDocLibName());
            relTableDocLibElement.addAttribute("CatType",vo.getCatType().getName());
            
            relTableDocLibElement.addAttribute("CategoryField",vo.getCategoryField());
            relTableDocLibElement.addAttribute("IgnoreFlag",String.valueOf(vo.getIgnoreFlag()));
            
            Element fieldsElement = relTableDocLibElement.addElement("RelatedFields");
            
            int doclibID = vo.getDocLibId();
            int catTypeID = vo.getCatTypeId();
            
            RelTableDocLibFields[] fields = fieldsMgr.getRelTableDocLibFields(doclibID,catTypeID);
            
            for( int j = 0; j < fields.length; j++){
                RelTableDocLibFields field = fields[j];
                
                Element fieldElement = fieldsElement.addElement("RelatedField");
                fieldElement.addAttribute("RelTableField",field.getTableField());
                fieldElement.addAttribute("DocLibField",field.getLibField());
            }
        }
        
        return doc.getRootElement().asXML();
    }

    private String exportRelTables() throws Exception {
        
        RelTable[] tables = relTableMgr.getRelTables(0);
        if(tables.length == 0)
            return "";
        
        Element tablesElement = DocumentHelper.createElement("RelTables");
        Document doc = DocumentHelper.createDocument(tablesElement);
        
        for( int i = 0; i < tables.length; i++){
            RelTable table = tables[i];
            int dsID = table.getDsID().intValue();
            String tableName = table.getTableName();
            
            Element tableElement = tablesElement.addElement("RelTable");
            tableElement.addAttribute("Name",table.getName());
            tableElement.addAttribute("SuffixTableName",tableName.substring(8));
            tableElement.addAttribute("DocType",docTypeMgr.get(table.getRefDocTypeID().intValue()).getDocTypeName());
            tableElement.addAttribute("DataSource",dsMgr.get(dsID).getName());
            
            Element fieldsElement = tableElement.addElement("Fields");
            appendFieldElements(fieldsElement,dsID,tableName);
            
        }
        
        return doc.getRootElement().asXML();
    }

    
    private void appendFieldElements(Element fieldsElement, int dsID, String tableName) throws Exception{
        
        List tmp = new ArrayList();
        
        //获取所有的系统字段
        Field[] fields = BaseField.class.getFields();
        for (int i = 0; i < fields.length; i++) {

            Field f = fields[i];
            ColumnInfo ci = (ColumnInfo) f.get(f);
            tmp.add(ci.getName());
        }
        //获取所有的Class字段
        for( int i = 1; i < 11; i++){
            tmp.add("CLASS_"+i);
        }
        
        String[] nonExtFields = (String[]) tmp.toArray(new String[0]);
        
        String sql = "select * from "+tableName;
        DBSession sess = Context.getDBSession(dsID);
        try
        {
	        IResultSet rst = sess.executeQuery(sql);
	        
	        ResultSetMetaData md = rst.getMetaData();
	        int colCount = md.getColumnCount();
	        
	        for( int i = 1; i <= colCount; i++){
	            String colName = md.getColumnName(i);
	        
	            if(!ArrayUtils.contains(nonExtFields,colName)){
	                Element fieldElement = fieldsElement.addElement("Field");
	                fieldElement.addAttribute("Name",colName);
	            }
	        }
	        rst.close();
        } finally {
        	sess.closeQuietly();
        }
    }

    /**
     * 导出指定的子系统的文档类型下的所有库文件夹
     * @return
     */
    private String exportFolderViews(int[] docTypeIDs) throws E5Exception{
        
        //获取所有的文档库
        DocLib[] libs = libMgr.getByTypeID(0);
        
        if(libs.length == 0)
            return "";
        
        Element fvsElement = DocumentHelper.createElement("FolderViews");
        Document doc = DocumentHelper.createDocument(fvsElement);
        
        for( int i = 0; i < libs.length; i++){
        	if (!isValid(libs[i].getDocTypeID(), docTypeIDs)) continue;

        	DocLib lib = libs[i];
            int libID = lib.getDocLibID();            
            String libName = lib.getDocLibName();
            
            //根据文档库，获得所有其下的文件夹，按照深度排列
            FolderView[] fvs = folderMgr.getChildrenFVsByDocLib(libID);
            
            TIntObjectHashMap fvID_fvName = new TIntObjectHashMap();
            
            for( int j = 0; j < fvs.length; j++){
                
                FolderView fv = fvs[j];
                int fvID = fv.getFVID();
                String fvName = fv.getFVName();
                int level = fv.getTreeLevel();
                int parentID = fv.getParentID();
                boolean isFolder = fv.isFolder();
                
                fvID_fvName.put(fvID,fvName);//cache
                
                
                Element fvElement = fvsElement.addElement("FolderView");
                
                fvElement.addAttribute("Name",fvName);
                fvElement.addAttribute("Type",isFolder? "1":"2");
                fvElement.addAttribute("DocLib",libName);
                
                fvElement.addAttribute("Parent",(level == 0)?"":String.valueOf(fvID_fvName.get(parentID)));
                fvElement.addAttribute("Root",String.valueOf(fv.getRootID()));
                fvElement.addAttribute("KeeyDay",String.valueOf(fv.getKeepDay()));
                
                fvElement.addAttribute("DefaultLayoutID","1");
                fvElement.addAttribute("TreeOrder",String.valueOf(fv.getTreeOrder()));
                fvElement.addAttribute("TreeLevel",String.valueOf(level));
                
                fvElement.addAttribute("ViewFormula",isFolder?"":((View)fv).getViewFormula());
                
                //导出文件夹上的规则
                exportFVRules(fvID, fvElement);
                
                //导出文件夹上的过滤器
                exportFVFilters(fvID, fvElement);
                
            }
            
            
        }
        
        
        return doc.getRootElement().asXML();
    }

    /**
     * @param folderMgr
     * @param fvID
     * @param fvElement
     * @throws E5Exception
     */
    private void exportFVFilters(int fvID, Element fvElement) throws E5Exception {
        
        Element filtersElement = fvElement.addElement("Filters");
        List filterList = folderMgr.getFilters(fvID);
        
        for( int i = 1; i <= filterList.size(); i++){            
            Filter[] filters = (Filter[]) filterList.get(i-1);
            
            for( int j = 0; j < filters.length; j++){
                Filter filter = filters[j];
                Element filterElement = filtersElement.addElement("Filter");
                
                filterElement.addAttribute("group",String.valueOf(i - 1));//2006-10-19 Gong Lijie
                filterElement.addAttribute("Name",filter.getFilterName());
                filterElement.addAttribute("index",String.valueOf(j));
            }
        }
    }

    /**
     * @param folderMgr
     * @param fvID
     * @param fvElement
     * @throws E5Exception
     */
    private void exportFVRules(int fvID, Element fvElement) throws E5Exception {
        
        Element rulesElement = fvElement.addElement("Rules");
        Rule[] rules = folderMgr.getRules(fvID);
        for( int k = 0; k < rules.length; k++){
            Rule rule = rules[k];
            Element ruleElement = rulesElement.addElement("Rule");
            ruleElement.addAttribute("Name",rule.getRuleName());
            ruleElement.addAttribute("subIndex",String.valueOf(k));
        }
    }

    //只导出指定的子系统下的文档类型的文档库
    private String exportDocLibs(int[] docTypeIDs) throws E5Exception {
        
        DocLib[] libs = null;        
        try{
            libs = libMgr.getByTypeID(0);
            
        } catch (E5Exception e){
            logger.error("获取所有文档库异常",e);
            return null;
        }
        
        Element libsElement = DocumentHelper.createElement("DocLibs");
        Document doc = DocumentHelper.createDocument(libsElement);
        
        for( int i = 0; i < libs.length; i++){
        	if (!isValid(libs[i].getDocTypeID(), docTypeIDs)) continue;
            DocLib lib = libs[i];
            
            Element libElement = libsElement.addElement("DocLib");
            libElement.addAttribute("Name",lib.getDocLibName());
            
            String dsName = dsMgr.get(lib.getDsID()).getName();
            libElement.addAttribute("DataSource",dsName);
            
            String docTypeName = docTypeMgr.get(lib.getDocTypeID()).getDocTypeName();
            libElement.addAttribute("DocType",docTypeName);
            
            libElement.addAttribute("KeeyDay",String.valueOf(lib.getKeepDay()));
            
        }
        
        return doc.getRootElement().asXML();
    }
    private boolean isValid(int id, int[] ids)
    {
    	for (int i = 0; i < ids.length; i++) {
			if (ids[i] == id) return true;
		}
    	return false;
    }
    
    private String exportDataSources() throws E5Exception{
        
        E5DataSource[] dz = null;
        
        try{
            dz = dsMgr.getAll();
            
        } catch (E5Exception e){
            logger.error("获取数据源异常",e);
            throw new E5Exception("获取数据源异常");
        }
        
        Element dzElement = DocumentHelper.createElement("DataSources");
        Document doc = DocumentHelper.createDocument(dzElement);
        
        for( int i = 0; i < dz.length; i++){
            E5DataSource ds = dz[i];
            
            Element dsElement = dzElement.addElement("DataSource");
            
            dsElement.addAttribute("Name",ds.getName());
            dsElement.addElement("Datasource").addText(ds.getDataSource());
            dsElement.addElement("DBServer").addText(ds.getDbServer());
            dsElement.addElement("DB").addText(ds.getDb());
            dsElement.addElement("DBUser").addText(ds.getUser());
            dsElement.addElement("DBPassword").addText(ds.getPassword());
            dsElement.addElement("DBType").addText(ds.getDbType());
            dsElement.addElement("UserType").addText("0");
        }
        
        return doc.getRootElement().asXML();
    }

    /* (non-Javadoc)
	 * @see com.founder.e5.app.template.IAppTemplateManager#unload(int)
	 */
	public void unload(int appID){
        
        DocType[] types = DomUtils.EMPTY_DOCTYPE_ARRAY;;
        try{
            types = docTypeMgr.getTypes(appID);
            
        } catch (E5Exception e){
            logger.warn("getDocTypesByAppID Exception ",e);
        }
        
        for( int i = 0; i < types.length; i++){
            
            DocType type = types[i];
            int typeID = type.getDocTypeID();
            
            try{
                //delete all related objects, doclib,folderview,filter,rule,etc.
                docTypeMgr.delete(typeID);
                
            } catch (Exception e){
                logger.warn("unload Exception when unload DocType: "+typeID,e);
            }
            
        }
	}

	public void setSession(Session session)
    {
    	return;
    }
}