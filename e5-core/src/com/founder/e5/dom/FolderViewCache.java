/**********************************************************************
 * Copyright (c) 2002,北大方正电子有限公司电子出版事业部集成系统开发部
 * All rights reserved.
 * 
 * 摘要：
 * 
 * 当前版本：1.0
 * 作者： 张凯峰   Zhang_KaiFeng@Founder.Com
 * 完成日期：2005-7-21  11:02:46
 *  
 *********************************************************************/
package com.founder.e5.dom;

import gnu.trove.TIntArrayList;
import gnu.trove.TIntIntHashMap;
import gnu.trove.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.founder.e5.context.Cache;
import com.founder.e5.context.Context;
import com.founder.e5.context.DAOHelper;
import com.founder.e5.context.E5Exception;
import com.founder.e5.dom.facade.CacheManagerFacade;
import com.founder.e5.dom.util.DomUtils;
import com.founder.e5.dom.util.TIntIntListHashMap;
import com.founder.e5.listpage.FVListPage;
import com.founder.e5.listpage.FVListPageManager;

public class FolderViewCache implements Cache {
    
    private static int order = 0;

    /**
     * key = folderID
     * value = TIntIntListHashMap
     *      key = groupid 0,1,2
     *      value = TIntArrayList。其中是排了顺序的过滤器ID
     */
    private TIntObjectHashMap fvID_filterIDs_Map = new TIntObjectHashMap();

    /**
     * key = folderid
     * value = ruleID list
     */
    private TIntIntListHashMap fvID_ruleIDs_Map = new TIntIntListHashMap();

    /**
     * key = fvid value = Folder
     */
    private TIntObjectHashMap fvID_fv_Map = new TIntObjectHashMap();

    /**
     * key = folder value = sons id 排了顺序的
     */
    private TIntIntListHashMap fvID_sonIDs_map = new TIntIntListHashMap();
    private TIntIntHashMap fvID_parentID = new TIntIntHashMap();

    //下面是文档库缓存的变量定义
    /**
     * key = docLibID
     * value = docLib
     */
    private TIntObjectHashMap libID_Lib_Map = new TIntObjectHashMap();

    /**
     * key = docTypeID
     * value = int[] docLibIDs
     */
    private TIntIntListHashMap typeID_libIDs_Map = new TIntIntListHashMap();;

    /**
     * key = libid
     * value = docLibAdds
     */
    private TIntObjectHashMap libID_libAdds_Map = new TIntObjectHashMap();;

    private FVListPage[] fvLists = null;
    public void refresh() throws E5Exception {

        List ruleList = this.getAllFVRules();
        List filterList = this.getAllFVFilters();
        List fvList = this.getAllFVs();
        // folderId_ruleIds_map
        this.fvID_ruleIDs_Map.clear();
        for (Iterator iter = ruleList.iterator(); iter.hasNext();) {
            FVRules r = (FVRules) iter.next();
            this.fvID_ruleIDs_Map.put(r.getFvID(), r.getRuleID());
        }

        // folderID_filterID_map
        this.fvID_filterIDs_Map.clear();
        for( Iterator iter = filterList.iterator(); iter.hasNext();){
            FVFilters ff = (FVFilters) iter.next();
            int fvID = ff.getFvID();
            
            TIntIntListHashMap map = (TIntIntListHashMap) this.fvID_filterIDs_Map.get(fvID);
            if(map == null){
                map = new TIntIntListHashMap();            
                this.fvID_filterIDs_Map.put(fvID,map);
            }
            map.put(ff.getDefaultFlag(),ff.getFilterID());            
        }
        
        //fvid_fv, fvid_sonid
        this.fvID_fv_Map.clear();
        this.fvID_parentID.clear();
        this.fvID_sonIDs_map.clear();
        
        for (Iterator iter = fvList.iterator(); iter.hasNext();) {
            
            FolderView fv = (FolderView) iter.next();
            
            this.fvID_fv_Map.put(fv.getFVID(), fv);
            this.fvID_parentID.put(fv.getFVID(),fv.getParentID());
            this.fvID_sonIDs_map.put(fv.getParentID(), fv.getFVID());
        }
        
        // 设置FolderView对象的resourceLevel和parents属性。
        this.setOtherProperties();
        
        //设置文件夹规则在整个树中排列的次序，按照深度。
        this.setOrderTogether();
        
        //文档库缓存
        List libList = this.getAllLibs();
        List libAddsList = this.getAllLibAdds();

        // libID_lib_map
        this.libID_Lib_Map.clear();
        this.typeID_libIDs_Map.clear();
        for (Iterator iter = libList.iterator(); iter.hasNext();) {
            DocLib lib = (DocLib) iter.next();
            this.libID_Lib_Map.put(lib.getDocLibID(), lib);
            this.typeID_libIDs_Map.put(lib.getDocTypeID(), lib.getDocLibID());
        }

        // libID_libAdds_map
        this.libID_libAdds_Map.clear();
        for (Iterator iter = libAddsList.iterator(); iter.hasNext();) {
            DocLibAdditionals a = (DocLibAdditionals) iter.next();
            this.libID_libAdds_Map.put(a.getDocLibID(), a);

        }
        
        //文件夹列表方式缓存
        try {
			FVListPageManager fvlpManager = (FVListPageManager)Context.getBean(FVListPageManager.class);
			fvLists = fvlpManager.getAll();
		} catch (Exception e) {
			//缓存刷新时不抛出异常，以兼容旧版本e5，无文件夹列表方式的情况
		}
    }
    public FVListPage[] getFVListPages() {
    	return fvLists;
    }

    /**
     * 2006-3-17 18:27:51
     */
    private void setOrderTogether() {
        
        // 所有根文件夹ID
        TIntArrayList rootIDList = this.fvID_sonIDs_map.getIntList(0);
        if(rootIDList == null)
            return ;
        for(int i=0;i<rootIDList.size();i++){
            
            int rootID = rootIDList.get(i);
            FolderView root = (FolderView) this.fvID_fv_Map.get(rootID);
            order =1;
            root.setOrderTogether(order);
            order++;            
            setChildrenOrderTogether(rootID);
            
        }
        
    }

    /**
     * 设置fv在文件夹树中深度排行，即从根节点开始到节点的深度
     * 每一层的节点的TreeOrder按照顺序排序，从0开始（可以由FolderManager的reArrange方法维护）
     * 2006-3-17 18:11:17
     * @author zhang_kaifeng
     * @param parentID
     * @param order
     */
    private void setChildrenOrderTogether(int parentID) {
        
        TIntArrayList sonIDList = this.fvID_sonIDs_map.getIntList(parentID);
        
        if(sonIDList != null){
            for( int j = 0; j < sonIDList.size(); j++){
                int sonID = sonIDList.get(j);
                FolderView son = (FolderView) this.fvID_fv_Map.get(sonID);
                son.setOrderTogether(order);
                order++;
                setChildrenOrderTogether(sonID);
            }
        }
    }

    /**
     * 设置FolderView对象的resourceLevel和parents属性。
     * parents代表节点的父路径
     * 2006-3-17 18:29:14
     * @author zhang_kaifeng
     */
    private void setOtherProperties() {
        
        int[] fvids = this.fvID_fv_Map.keys();
        for( int i = 0; i < fvids.length; i++){
            int fvid = fvids[i];
            FolderView fv  = (FolderView) this.fvID_fv_Map.get(fvid);
            
            //parents
            int[] parents = this.getFVParents(fvid);
            fv.setParents(parents);
            
        }
        
    }
    
    /**
     * 获取指定文件夹的父路径
     * 2006-3-17 15:41:27
     * @author zhang_kaifeng
     * @param fvid
     * @return
     */
    private int[] getFVParents(int fvid) {
        
        TIntArrayList l = new TIntArrayList();
        
        int parentID = this.fvID_parentID.get(fvid);
        while(parentID != 0){
            l.add(parentID);
            parentID = this.fvID_parentID.get(parentID);
        }
        
        l.reverse();
        
        return l.toNativeArray();
    }

    public void reset() {
        
        this.fvID_filterIDs_Map.clear();
        this.fvID_ruleIDs_Map.clear();

        this.fvID_fv_Map.clear();
        this.fvID_sonIDs_map.clear();

        this.libID_Lib_Map.clear();
        this.libID_libAdds_Map.clear();
        this.typeID_libIDs_Map.clear();
    }

    private List getAllFVs() throws E5Exception {
    	return DAOHelper.find("from FolderView f order by f.rootID, f.parentID,f.treeOrder");
    }

    private List getAllFVFilters() throws E5Exception {
    	return DAOHelper.find("from FVFilters as f order by f.fvID,f.defaultFlag, f.index");
    }

    private List getAllFVRules() throws E5Exception {
    	return DAOHelper.find("from FVRules r order by r.fvID, r.subIndex");
	}

    /**
     * 获取指定文件夹下的所有过滤器ID
     * 
     * @param fvID
     * @return 最多有三个元素的列表，每个元素是int[]
     */
    List getFVFilterIDs(int fvID) {

        TIntIntListHashMap map = (TIntIntListHashMap) this.fvID_filterIDs_Map.get(fvID);
        List ret = new ArrayList();
        if(map == null)
            return ret;
        
        int[] groups = map.keys();
        Arrays.sort(groups);
        for( int i = 0; i < groups.length; i++){
            int group = groups[i];
            int[] fileterIDs = map.getIntList(group).toNativeArray();
            ret.add(fileterIDs);            
        }
        return ret;

    }

    /**
     * 获取指定文件夹下的所有过滤器
     * 
     * @param fvID
     * @return 最多有三个元素的列表，每个元素是Filter[]
     */
    List getFVFilters(int fvID) {
        
        TIntIntListHashMap map = (TIntIntListHashMap) this.fvID_filterIDs_Map.get(fvID);
        List ret = new ArrayList();
        if(map == null)
            return ret;
        
        DocTypeCache c = (DocTypeCache) CacheManagerFacade.find(DocTypeCache.class);
        
        int[] groups = map.keys();
        Arrays.sort(groups);
        for( int i = 0; i < groups.length; i++){
            int group = groups[i];
            int[] filterIDs = map.getIntList(group).toNativeArray();
            Filter[] filters = new Filter[filterIDs.length];
            for( int j = 0; j < filters.length; j++){
                filters[j] = c.getFilter(filterIDs[j]);
            }
            ret.add(filters);
        }
        return ret;
    }

    int[] getFVRuleIDs(int fvID) {

        TIntArrayList idList = this.fvID_ruleIDs_Map.getIntList(fvID);
        if (null == idList)
            return DomUtils.EMPTY_INT_ARRAY;
        return idList.toNativeArray();
    }

    /**
     * 获取指定文件夹下的所有规则
     * 
     * @param fvID
     * @return 如果没有，则返回空数组
     */
    Rule[] getFVRules(int fvID) {

        TIntArrayList idList = this.fvID_ruleIDs_Map.getIntList(fvID);
        if (null == idList)
            return DomUtils.EMPTY_RULE_ARRAY;

        DocTypeCache c = (DocTypeCache) CacheManagerFacade.find(DocTypeCache.class);
        Rule[] rs = new Rule[idList.size()];
        for (int i = 0; i < idList.size(); i++) {
            rs[i] = c.getRule(idList.getQuick(i));
        }

        return rs;
    }

    /**
     * 获取指定id的FV
     * 
     * @param fvID
     * @return 如果不存在，则返回null
     */
    FolderView getFV(int fvID) {
        return (FolderView) this.fvID_fv_Map.get(fvID);
    }

    /**
     * 获取指定文件夹下的所有文件夹，按照树节点的顺序
     * 
     * @param folderID
     * @return 如果没有，则返回空数组
     */
    FolderView[] getChildrenFVs(int folderID) {

        int[] ids = this.getChildrenSubFVIDs(folderID);
        
        FolderView[] fvs = new FolderView[ids.length];
        for (int i = 0; i < ids.length; i++) {
            fvs[i] = (FolderView) this.fvID_fv_Map.get(ids[i]);
        }

        return fvs;
    }

    /**
     * 获取指定文件夹下的所有fvid，按照树节点的顺序
     * 
     * @param folderID
     * @return 如果没有，则返回空数组
     */
    int[] getChildrenSubFVIDs(int folderID) {

        TIntArrayList idList = new TIntArrayList();
        return this.getChildrenFVSubIDs(idList, folderID);

    }

    /**
     * 2006-2-16 16:58:10
     * 获取文件夹下所有的子孙节点的id，按照树的次序排好
     * @author zhang_kaifeng
     * @param idList
     * @param folderID
     * @return 如果没有，则返回空数组
     */
    private int[] getChildrenFVSubIDs(TIntArrayList idList, int folderID) {

        TIntArrayList l = this.fvID_sonIDs_map.getIntList(folderID);
        
        if(l == null)
            return new int[0];
        
        for (int i = 0; i < l.size(); i++) {
            int id = l.getQuick(i);
            idList.add(id);
            getChildrenFVSubIDs(idList, id);
        }
        return idList.toNativeArray();
    }

    /**
     * 获取所有的儿子文件夹
     * 2006-2-16 17:04:18
     * @author zhang_kaifeng
     * @param folderID
     * @return 如果没有，则返回空数组
     */
    Folder[] getSubFolders(int folderID) {
        
        TIntArrayList l = this.fvID_sonIDs_map.getIntList(folderID);
        ArrayList ret = new ArrayList();
        
        if(l == null)
        	return new Folder[0];
        
        for(int i=0;i<l.size();i++){
            int fvid = l.getQuick(i);
            FolderView fv = (FolderView) this.fvID_fv_Map.get(fvid);
            if(fv.getClass().getName().equalsIgnoreCase("com.founder.e5.dom.Folder"))
                ret.add(fv);                
        }
        return (Folder[]) ret.toArray(DomUtils.EMPTY_FOLDER_ARRAY);
    }

    /**
     * 获取所有的儿子文件夹id
     * 2006-2-16 17:17:17
     * @author zhang_kaifeng
     * @param folderID
     * @return 如果没有，则返回空数组
     */
    int[] getSubFoldersID(int folderID) {
        
        TIntArrayList l = this.fvID_sonIDs_map.getIntList(folderID);
        TIntArrayList ret = new TIntArrayList();
        
        if(l != null)
            for (int i = 0; i < l.size(); i++)
			{
				int fvid = l.getQuick(i);
				FolderView fv = (FolderView) this.fvID_fv_Map.get(fvid);
				if (fv.getClass().getName().equalsIgnoreCase(
						"com.founder.e5.dom.Folder"))
					ret.add(fvid);
			}
        
        return ret.toNativeArray();
    }

    /**
     * 2006-2-16 17:19:44
     * 获取所有的儿子文件夹视图
     * @author zhang_kaifeng
     * @param folderID
     * @return 如果没有，则返回空数组
     */
    FolderView[] getSubFVs(int folderID) {
        
        TIntArrayList list = this.fvID_sonIDs_map.getIntList(folderID);
        ArrayList ret = new ArrayList();
        
        if (list != null)
	        for (int i = 0; i < list.size(); i++)
			{
				int fvid = list.getQuick(i);
				FolderView fv = (FolderView) this.fvID_fv_Map.get(fvid);
				ret.add(fv);
			}
        return (FolderView[]) ret.toArray(DomUtils.EMPTY_FV_ARRAY);
    }

    /**
     * 根据文档库ID，获取其对应的文件夹对象
     * 2006-2-23 10:00:54
     * @author zhang_kaifeng
     * @param docLibID
     * @return
     */
    Folder getFVRoot(int docLibID) {
        
        Object[] fvs = this.fvID_fv_Map.getValues();
        
        int rootID = -1;
        
        for( int i = 0; i < fvs.length; i++){
            FolderView fv= (FolderView) fvs[i];
            if(fv.getDocLibID() == docLibID){
                rootID = fv.getRootID();
                break;
            }
        }
        
        return (Folder) this.getFV(rootID);
        
        
    }

    /**
     * 获取指定id的文件夹视图对象数组
     * 2006-3-28 15:53:03
     * @author zhang_kaifeng
     * @param fvids 如果为null，则返回空数组
     * @return 如果某个id没有对应对象，则返回null
     */
    FolderView[] getFVs(int[] fvids) {
        
        FolderView[] ret = DomUtils.EMPTY_FV_ARRAY;
        if(fvids == null)
            return ret;
        
        ret = new FolderView[fvids.length];
        for( int i = 0; i < ret.length; i++){
            ret[i] = this.getFV(fvids[i]);
        }
        return ret;
    }
    /**
     * @param docLibID
     * @return 如果不存在，则返回null
     */
    DocLib getDocLib(int docLibID) {
        return (DocLib) this.libID_Lib_Map.get(docLibID);
    }

    /**
     * @param docLibIDs
     * @return 如果某个文档库id不存在，则对应索引位置的文档库为null
     */
    DocLib[] getDocLibs(int[] docLibIDs) {

        DocLib[] libs = new DocLib[docLibIDs.length];
        for (int i = 0; i < libs.length; i++) {
            libs[i] = this.getDocLib(docLibIDs[i]);
        }

        return libs;
    }

    /**
     * 获取指定文档类型下的所有文档库
     * 
     * @param doctypeID
     * @return 如果文档类型不存在，或其下无文档库，则返回空数组；如果参数为0，则返回所有的文档库
     */
    DocLib[] getDocLibsByTypeID(int doctypeID) {
        
        TIntArrayList l = null;
        
        if(doctypeID == 0){
            int[] typeIds = this.typeID_libIDs_Map.keys();
            l = new TIntArrayList();
            for( int i = 0; i < typeIds.length; i++){
                int typeId = typeIds[i];
                l.add(this.typeID_libIDs_Map.getIntList(typeId).toNativeArray());
            }
        }
        
        else
            l = (TIntArrayList) this.typeID_libIDs_Map.get(doctypeID);

        if (null == l)
            return DomUtils.EMPTY_DOCLIB_ARRAY;

        DocLib[] libs = new DocLib[l.size()];
        for (int i = 0; i < libs.length; i++) {
            libs[i] = (DocLib) this.libID_Lib_Map.get(l.getQuick(i));
        }

        return libs;

    }

    /**
     * 获取指定文档类型下的所有文档库
     * 
     * @param doctypeID
     * @return 如果文档类型不存在，或其下无文档库，则返回空数组
     */
    DocLib[] getDocLibsByTypeName(String doctypeName) {

        DocTypeCache c = (DocTypeCache) CacheManagerFacade
                .find(DocTypeCache.class);
        DocType t = c.getDocType(doctypeName);
        if (null == t)
            return DomUtils.EMPTY_DOCLIB_ARRAY;

        int docTypeID = t.getDocTypeID();
        return this.getDocLibsByTypeID(docTypeID);

    }

    /**
     * 获取指定文档库的流程表信息
     * 
     * @param docLibID
     * @return 如果文档库不存在，返回null
     */
    DocLibAdditionals getFlowTableInfo(int docLibID) {

        return (DocLibAdditionals) this.libID_libAdds_Map.get(docLibID);

    }

    /**
     * 获取指定文档类型的文档库id数组
     * 
     * @param docTypeID
     * @return 如果文档类型下无文档库，则返回空数组；如果参数为0，则返回所有的文档库id
     */
    int[] getDocLibIDs(int docTypeID) {
        
        TIntArrayList l = null;
        
        if (docTypeID == 0){
            Object[] ls = this.typeID_libIDs_Map.getValues();
            l = new TIntArrayList();

            for( int i = 0; i < ls.length; i++){
                TIntArrayList temp = (TIntArrayList) ls[i];
                l.add(temp.toNativeArray());
            }
        }

        else
            l = (TIntArrayList) this.typeID_libIDs_Map.get(docTypeID);

        if (null == l)
            return new int[0];
        return l.toNativeArray();

    }

    /**
     * 获取指定文档库id对应的名字
     * 
     * @param docLibIDs
     * @return 如果某一文档库不存在，则对应索引位置的名称为null
     */
    String[] getDocLibNames(int[] docLibIDs) {

        String[] names = new String[docLibIDs.length];

        for (int i = 0; i < docLibIDs.length; i++) {
            int id = docLibIDs[i];
            DocLib lib = this.getDocLib(id);
            names[i] = (lib == null) ? null : lib.getDocLibName();
        }
        return names;
    }
    private List getAllLibs() throws E5Exception {
        return DAOHelper.find("from DocLib as d");
    }

    private List getAllLibAdds() throws E5Exception {
    	return DAOHelper.find("from DocLibAdditionals as d");
    }
    int getDocTypeIDByLibID(int docLibId) {
        DocLib lib = this.getDocLib(docLibId);
        if (lib != null){
            return lib.getDocTypeID();
        }
        return 0;
    }

}
