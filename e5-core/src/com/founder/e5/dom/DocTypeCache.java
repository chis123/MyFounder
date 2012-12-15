package com.founder.e5.dom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gnu.trove.TIntArrayList;
import gnu.trove.TIntIntHashMap;
import gnu.trove.TIntIntIterator;
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TObjectIntHashMap;

import com.founder.e5.context.Cache;
import com.founder.e5.context.Context;
import com.founder.e5.context.DAOHelper;
import com.founder.e5.context.E5Exception;
import com.founder.e5.db.DBSession;
import com.founder.e5.dom.util.DomUtils;
import com.founder.e5.dom.util.TIntIntListHashMap;

/**
 * depends on context.jar
 * 
 * @version 1.0
 * @created 11-七月-2005 15:54:52
 */
public class DocTypeCache implements Cache {

    /**
     * key = fieldID
     * value = Field
     */
    //private TIntObjectHashMap fieldID_Field_Map = new TIntObjectHashMap();
    //private TIntIntListHashMap typeID_fieldIDs_Map = new TIntIntListHashMap();
    private DocTypeField[] typeFieldArr = null;

    /**
     * key = typeID
     * value = appID
     */
    private TIntIntHashMap typeID_AppID_Map = new TIntIntHashMap();

    /**
     * key = typeID
     * value = type
     */
    private TIntObjectHashMap typeID_Type_Map = new TIntObjectHashMap();

    /**
     * key = typeName
     * value = typeID
     */
    private TObjectIntHashMap typeName_TypeID_Map = new TObjectIntHashMap();
    
    //规则
    private TIntObjectHashMap ruleID_Rule_map = new TIntObjectHashMap();
    private TIntIntListHashMap typeID_RuleID_map = new TIntIntListHashMap();

    //过滤器
    private TIntIntListHashMap typeID_filterIDs_map = new TIntIntListHashMap();
    private TIntObjectHashMap filterID_filter_map = new TIntObjectHashMap();
    
    /**
     * 获取指定id的doctype
     * 
     * @param doctypeID
     * @return 如果不存在，则返回null
     */
    DocType getDocType(int doctypeID) {

        return (DocType) this.typeID_Type_Map.get(doctypeID);
    }

    /**
     * 获取指定name的doctype
     * 
     * @param doctypeName
     * @return 如果不存在，则返回null
     */
    DocType getDocType(String doctypeName) {

        int id = this.typeName_TypeID_Map.get(doctypeName);
        if (0 == id)
            return null;
        return this.getDocType(id);
    }

    private List getAllFields() throws E5Exception {
    	/**
    	 * 2006-11-16 Gong Lijie 
    	 * 提前把几个字段设成0:READONLY,EDITTYPE,ISINDEXED
    	 * 因为这些整数字段原来在数据库中都空，Hibernate读取时会报错，所以提前处理一下
    	 */
    	DBSession db = Context.getDBSession();
        try{
        	db.executeUpdate("update DOM_DOCTYPEFIELDS set READONLY=0 where READONLY is null", null);
        	db.executeUpdate("update DOM_DOCTYPEFIELDS set EDITTYPE=0 where EDITTYPE is null", null);
        	db.executeUpdate("update DOM_DOCTYPEFIELDS set ISINDEXED=0 where ISINDEXED is null", null);
        	
        } catch (Exception e){
        } finally {
        	db.closeQuietly();
        }
    	return DAOHelper.find("from DocTypeField as f where f.status='P' order by f.docTypeID, f.fieldID");
    }

    private List getAllTypeApps() throws E5Exception {
        return DAOHelper.find("from DocTypeApps as a");
    }

    private DocType[] getAllTypes() throws E5Exception {
    	DocTypeManager docTypeManager = (DocTypeManager)Context.getBean(DocTypeManager.class);
    	return docTypeManager.getTypes(0);
    }

    /**
     * 获取指定type的appid
     * 
     * @param docTypeID
     * @return 如果不存在，则返回0
     */
    int getAppID(int docTypeID) {

        return this.typeID_AppID_Map.get(docTypeID);

    }

    /**
     * 获取指定id的doctypeField
     * 
     * @param fieldID
     * @return 如果field不存在，则返回null
     */
    DocTypeField getField(int fieldID) {
    	DocTypeField f = findField(fieldID);
    	if (f == null) return null;
    	
    	return (DocTypeField)f.clone();
    }
    private DocTypeField findField(int fieldID) {
    	if (typeFieldArr == null) return null;
    	
    	for (int i = 0; i < typeFieldArr.length; i++) {
			if (typeFieldArr[i].getFieldID() == fieldID)
				return typeFieldArr[i];
		}
    	return null;
    }

    /**
     * 获取指定id的field的name
     * 
     * @param fieldID
     * @return 如果field不存在，则返回null
     */
    String getFieldName(int fieldID) {
        DocTypeField field = findField(fieldID);
        if (null != field)
            return field.getColumnName();
        return null;
    }

    /**
     * 获取指定文档类型的所有字段，默认按照正序排列
     * 
     * @param typeID
     * @return 如果没有，则返回空数组
     */
    DocTypeField[] getFields(int typeID) {
    	/*
        TIntArrayList l = this.typeID_fieldIDs_Map.getIntList(typeID);
        if (null == l)
            return DomUtils.EMPTY_DOCTYPEFIELD_ARRAY;
        l.sort();
        int[] ids = l.toNativeArray();
        DocTypeField[] fields = new DocTypeField[ids.length];
        for ( int i = 0 ; i < ids.length ; i++ ){
            fields[i] = (DocTypeField)findField(ids[i]).clone();
        }
        */
    	if (typeFieldArr == null) return null;
    	
    	List list = new ArrayList(150);
    	for (int i = 0; i < typeFieldArr.length; i++) {
			if (typeFieldArr[i].getDocTypeID() == typeID)
				list.add(typeFieldArr[i].clone());
			else if (typeFieldArr[i].getDocTypeID() > typeID)
				break;
		}
        return (DocTypeField[])list.toArray(DomUtils.EMPTY_DOCTYPEFIELD_ARRAY);
    }

    /**
     * 获取指定文档类型下的所有扩展字段，默认按照正序排列
     * 
     * @param typeID
     * @return 如果文档类型不存在，或不存在扩展字段，则返回空数组
     */
    DocTypeField[] getFieldsExt(int typeID) {
    	if (typeFieldArr == null) return null;
    	
    	List list = new ArrayList(150);
    	for (int i = 0; i < typeFieldArr.length; i++) {
			if (typeFieldArr[i].getDocTypeID() == typeID
					&& typeFieldArr[i].getAttribute() != DocTypeField.FIELD_SYSTEM)
				list.add(typeFieldArr[i].clone());
			else if (typeFieldArr[i].getDocTypeID() > typeID)
				break;
		}
        return (DocTypeField[])list.toArray(DomUtils.EMPTY_DOCTYPEFIELD_ARRAY);
    }

    DocTypeField[] getFields(int typeID, int attribute) {
    	if (typeFieldArr == null) return null;
    	
    	List list = new ArrayList(150);
    	for (int i = 0; i < typeFieldArr.length; i++) {
			if (typeFieldArr[i].getDocTypeID() == typeID
					&& typeFieldArr[i].getAttribute() == attribute)
				list.add(typeFieldArr[i].clone());
			else if (typeFieldArr[i].getDocTypeID() > typeID)
				break;
		}
        return (DocTypeField[])list.toArray(DomUtils.EMPTY_DOCTYPEFIELD_ARRAY);
    }
   /**
     * 获取指定子应用下文档类型id,默认按照正序排列
     * 
     * @param appID
     * @return 如果子应用不存在，或其下无文档类型，则返回空数组；如果参数为0，则返回所有的文档类型id
     */
    int[] getIDs(int appID) {
        
        if(appID == 0){
            int[] ret = typeID_Type_Map.keys();
            Arrays.sort(ret);
            return ret;
        }

        TIntArrayList idList = new TIntArrayList();
        TIntIntIterator iterator = typeID_AppID_Map.iterator();
        
        for ( int i = typeID_AppID_Map.size() ; i-- > 0 ; ){
            iterator.advance();
            if (iterator.value() == appID)
                idList.add(iterator.key());
        }
        
        idList.sort();

        return idList.toNativeArray();

    }

    /**
     * 获取指定子应用下的文档类型
     * 
     * @param appID
     * @return 如果子应用不存在，或其下无文档类型，则返回空数组；如果参数为0，则返回所有的文档类型
     */
    DocType[] getTypes(int appID) {

        int[] typeIDs = this.getIDs(appID);
        
        DocType[] types = new DocType[typeIDs.length];

        for ( int i = 0 ; i < types.length ; i++ ){
            types[i] = this.getDocType(typeIDs[i]);
        }

        return types;

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.context.Cache#refresh()
     */
    public void refresh() throws E5Exception {

        DocType[] typeArr = this.getAllTypes();
        List fieldList = this.getAllFields();
        typeFieldArr = (DocTypeField[])fieldList.toArray(new DocTypeField[0]);
        
        List typeAppsList = this.getAllTypeApps();

        // 装载typeIDTypeMap,typeName_TypeID_Map
        this.typeID_Type_Map.clear();
        this.typeName_TypeID_Map.clear();
        for (int i = 0; i < typeArr.length; i++) {
            DocType t = typeArr[i];
            this.typeID_Type_Map.put(t.getDocTypeID(), t);
            this.typeName_TypeID_Map.put(t.getDocTypeName(), t.getDocTypeID());
        }

        // 装载typeIDAppIDMap
        this.typeID_AppID_Map.clear();
        for (int i = 0; i < typeAppsList.size(); i++ ){
            DocTypeApps a = (DocTypeApps) typeAppsList.get(i);
            this.typeID_AppID_Map.put(a.getDocTypeID(), a.getAppID());
        }

        //规则
        Rule[] ruleList = this.getAllRules();
        this.ruleID_Rule_map.clear();
        this.typeID_RuleID_map.clear();
        for (int i = 0; i < ruleList.length; i++) {
            Rule r = ruleList[i];
            this.ruleID_Rule_map.put(r.getRuleID(), r);
            this.typeID_RuleID_map.put(r.getDocTypeID(), r.getRuleID());
        }
        //过滤器
        Filter[] filterList = this.getAllFilters();

        this.filterID_filter_map.clear();
        this.typeID_filterIDs_map.clear();

        for (int i = 0; i < filterList.length; i++) {
            Filter f = filterList[i];
            this.filterID_filter_map.put(f.getFilterID(), f);
            this.typeID_filterIDs_map.put(f.getDocTypeID(), f.getFilterID());
        }
    }

    /*
     * (non-Javadoc)
     * @see com.founder.e5.context.Cache#reset()
     */
    public void reset() {
        this.typeID_AppID_Map.clear();
        this.typeID_Type_Map.clear();
        this.typeName_TypeID_Map.clear();

        this.ruleID_Rule_map.clear();
        this.typeID_RuleID_map.clear();

        this.filterID_filter_map.clear();
        this.typeID_filterIDs_map.clear();
   }

    DocTypeField[] getSysFields(int docTypeID) {
    	return getFields(docTypeID, DocTypeField.FIELD_SYSTEM);
    }

    DocTypeField getField(int docTypeID, String columnCode) {
    	if (typeFieldArr == null) return null;
    	
    	for (int i = 0; i < typeFieldArr.length; i++) {
			if (typeFieldArr[i].getDocTypeID() == docTypeID
					&& columnCode.equalsIgnoreCase(typeFieldArr[i].getColumnCode()))
               return (DocTypeField)typeFieldArr[i].clone();
			else if (typeFieldArr[i].getDocTypeID() > docTypeID)
				break;
        }
        return null;
    }
    /**
     * 获取指定id的规则
     * 
     * @param ruleID
     * @return 如果没有则返回null
     */
    Rule getRule(int ruleID) {
        return (Rule) this.ruleID_Rule_map.get(ruleID);
    }

    /**
     * 获取指定文档类型下的所有规则
     * 
     * @param doctypeID
     * @return 如果没有，则返回空数组
     */
    Rule[] getRules(int doctypeID) {
        TIntArrayList l = this.typeID_RuleID_map.getIntList(doctypeID);
        if (null == l)
            return DomUtils.EMPTY_RULE_ARRAY;

        final int size = l.size();
        Rule[] rules = new Rule[size];
        for (int i = 0; i < size; i++) {
            rules[i] = this.getRule(l.getQuick(i));
        }
        return rules;
    }
    private Rule[] getAllRules() throws E5Exception {
    	RuleManager ruleManager = (RuleManager)Context.getBean(RuleManager.class);
    	return ruleManager.getByDoctypeID(0);
    }

    /**
     * 获取指定id的过滤器
     * 
     * @param filterID
     * @return 如果没有，则返回null
     */
    Filter getFilter(int filterID) {
        return (Filter) this.filterID_filter_map.get(filterID);
    }

    /**
     * 获取指定文档类型下的过滤器
     * 
     * @param doctypeID
     * @return 如果没有，则返回空数组
     */
    Filter[] getFilters(int doctypeID) {

        TIntArrayList idList = this.typeID_filterIDs_map.getIntList(doctypeID);
        if (null == idList)
            return DomUtils.EMPTY_FILTER_ARAAY;

        int[] ids = idList.toNativeArray();
        Filter[] fs = new Filter[ids.length];
        for (int i = 0; i < ids.length; i++) {
            fs[i] = this.getFilter(ids[i]);
        }

        return fs;

    }
    private Filter[] getAllFilters() throws E5Exception {
    	FilterManager filterManager = (FilterManager)Context.getBean(FilterManager.class);
    	return filterManager.getByTypeID(0);
    }
}