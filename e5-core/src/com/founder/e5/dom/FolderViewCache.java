/**********************************************************************
 * Copyright (c) 2002,�������������޹�˾���ӳ�����ҵ������ϵͳ������
 * All rights reserved.
 * 
 * ժҪ��
 * 
 * ��ǰ�汾��1.0
 * ���ߣ� �ſ���   Zhang_KaiFeng@Founder.Com
 * ������ڣ�2005-7-21  11:02:46
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
     *      value = TIntArrayList������������˳��Ĺ�����ID
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
     * key = folder value = sons id ����˳���
     */
    private TIntIntListHashMap fvID_sonIDs_map = new TIntIntListHashMap();
    private TIntIntHashMap fvID_parentID = new TIntIntHashMap();

    //�������ĵ��⻺��ı�������
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
        
        // ����FolderView�����resourceLevel��parents���ԡ�
        this.setOtherProperties();
        
        //�����ļ��й����������������еĴ��򣬰�����ȡ�
        this.setOrderTogether();
        
        //�ĵ��⻺��
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
        
        //�ļ����б�ʽ����
        try {
			FVListPageManager fvlpManager = (FVListPageManager)Context.getBean(FVListPageManager.class);
			fvLists = fvlpManager.getAll();
		} catch (Exception e) {
			//����ˢ��ʱ���׳��쳣���Լ��ݾɰ汾e5�����ļ����б�ʽ�����
		}
    }
    public FVListPage[] getFVListPages() {
    	return fvLists;
    }

    /**
     * 2006-3-17 18:27:51
     */
    private void setOrderTogether() {
        
        // ���и��ļ���ID
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
     * ����fv���ļ�������������У����Ӹ��ڵ㿪ʼ���ڵ�����
     * ÿһ��Ľڵ��TreeOrder����˳�����򣬴�0��ʼ��������FolderManager��reArrange����ά����
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
     * ����FolderView�����resourceLevel��parents���ԡ�
     * parents����ڵ�ĸ�·��
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
     * ��ȡָ���ļ��еĸ�·��
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
     * ��ȡָ���ļ����µ����й�����ID
     * 
     * @param fvID
     * @return ���������Ԫ�ص��б�ÿ��Ԫ����int[]
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
     * ��ȡָ���ļ����µ����й�����
     * 
     * @param fvID
     * @return ���������Ԫ�ص��б�ÿ��Ԫ����Filter[]
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
     * ��ȡָ���ļ����µ����й���
     * 
     * @param fvID
     * @return ���û�У��򷵻ؿ�����
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
     * ��ȡָ��id��FV
     * 
     * @param fvID
     * @return ��������ڣ��򷵻�null
     */
    FolderView getFV(int fvID) {
        return (FolderView) this.fvID_fv_Map.get(fvID);
    }

    /**
     * ��ȡָ���ļ����µ������ļ��У��������ڵ��˳��
     * 
     * @param folderID
     * @return ���û�У��򷵻ؿ�����
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
     * ��ȡָ���ļ����µ�����fvid���������ڵ��˳��
     * 
     * @param folderID
     * @return ���û�У��򷵻ؿ�����
     */
    int[] getChildrenSubFVIDs(int folderID) {

        TIntArrayList idList = new TIntArrayList();
        return this.getChildrenFVSubIDs(idList, folderID);

    }

    /**
     * 2006-2-16 16:58:10
     * ��ȡ�ļ��������е�����ڵ��id���������Ĵ����ź�
     * @author zhang_kaifeng
     * @param idList
     * @param folderID
     * @return ���û�У��򷵻ؿ�����
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
     * ��ȡ���еĶ����ļ���
     * 2006-2-16 17:04:18
     * @author zhang_kaifeng
     * @param folderID
     * @return ���û�У��򷵻ؿ�����
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
     * ��ȡ���еĶ����ļ���id
     * 2006-2-16 17:17:17
     * @author zhang_kaifeng
     * @param folderID
     * @return ���û�У��򷵻ؿ�����
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
     * ��ȡ���еĶ����ļ�����ͼ
     * @author zhang_kaifeng
     * @param folderID
     * @return ���û�У��򷵻ؿ�����
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
     * �����ĵ���ID����ȡ���Ӧ���ļ��ж���
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
     * ��ȡָ��id���ļ�����ͼ��������
     * 2006-3-28 15:53:03
     * @author zhang_kaifeng
     * @param fvids ���Ϊnull���򷵻ؿ�����
     * @return ���ĳ��idû�ж�Ӧ�����򷵻�null
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
     * @return ��������ڣ��򷵻�null
     */
    DocLib getDocLib(int docLibID) {
        return (DocLib) this.libID_Lib_Map.get(docLibID);
    }

    /**
     * @param docLibIDs
     * @return ���ĳ���ĵ���id�����ڣ����Ӧ����λ�õ��ĵ���Ϊnull
     */
    DocLib[] getDocLibs(int[] docLibIDs) {

        DocLib[] libs = new DocLib[docLibIDs.length];
        for (int i = 0; i < libs.length; i++) {
            libs[i] = this.getDocLib(docLibIDs[i]);
        }

        return libs;
    }

    /**
     * ��ȡָ���ĵ������µ������ĵ���
     * 
     * @param doctypeID
     * @return ����ĵ����Ͳ����ڣ����������ĵ��⣬�򷵻ؿ����飻�������Ϊ0���򷵻����е��ĵ���
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
     * ��ȡָ���ĵ������µ������ĵ���
     * 
     * @param doctypeID
     * @return ����ĵ����Ͳ����ڣ����������ĵ��⣬�򷵻ؿ�����
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
     * ��ȡָ���ĵ�������̱���Ϣ
     * 
     * @param docLibID
     * @return ����ĵ��ⲻ���ڣ�����null
     */
    DocLibAdditionals getFlowTableInfo(int docLibID) {

        return (DocLibAdditionals) this.libID_libAdds_Map.get(docLibID);

    }

    /**
     * ��ȡָ���ĵ����͵��ĵ���id����
     * 
     * @param docTypeID
     * @return ����ĵ����������ĵ��⣬�򷵻ؿ����飻�������Ϊ0���򷵻����е��ĵ���id
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
     * ��ȡָ���ĵ���id��Ӧ������
     * 
     * @param docLibIDs
     * @return ���ĳһ�ĵ��ⲻ���ڣ����Ӧ����λ�õ�����Ϊnull
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
