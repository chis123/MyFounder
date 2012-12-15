package com.founder.e5.dom;

import gnu.trove.TIntArrayList;
import gnu.trove.TIntObjectHashMap;

import java.util.Iterator;
import java.util.List;

import com.founder.e5.context.Cache;
import com.founder.e5.context.DAOHelper;
import com.founder.e5.context.E5Exception;
import com.founder.e5.dom.facade.CacheManagerFacade;
import com.founder.e5.dom.util.DomUtils;
import com.founder.e5.dom.util.TIntIntListHashMap;

/**
 * @deprecated 2006-5-17
 * 已经被合并在FolderViewCache中
 * 
 * @created 2005-7-20 10:55:44
 * @author Zhang Kaifeng
 * @version
 */
public class DocLibCache implements Cache {

    /**
     * key = docLibID
     * value = docLib
     */
    private TIntObjectHashMap libID_Lib_Map = new TIntObjectHashMap();

    /**
     * key = docTypeID
     * value = int[] docLibIDs
     * 
     */
    private TIntIntListHashMap typeID_libIDs_Map = new TIntIntListHashMap();;

    /**
     * key = libid
     * value = docLibAdds
     * 
     */
    private TIntObjectHashMap libID_libAdds_Map = new TIntObjectHashMap();;

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

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.context.Cache#refresh()
     */
    public void refresh() throws E5Exception {

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

    }

    private List getAllLibs() throws E5Exception {
        return DAOHelper.find("from DocLib as d");
    }

    private List getAllLibAdds() throws E5Exception {
    	return DAOHelper.find("from DocLibAdditionals as d");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.context.Cache#reset()
     */
    public void reset() {
        this.libID_Lib_Map.clear();
        this.libID_libAdds_Map.clear();
        this.typeID_libIDs_Map.clear();
    }

    int getDocTypeIDByLibID(int docLibId) {
        DocLib lib = this.getDocLib(docLibId);
        if (lib != null){
            return lib.getDocTypeID();
        }
        return 0;
    }
}