package com.founder.e5.dom;

import gnu.trove.TIntArrayList;
import gnu.trove.TIntObjectHashMap;

import com.founder.e5.context.Cache;
import com.founder.e5.context.Context;
import com.founder.e5.context.E5Exception;
import com.founder.e5.dom.util.DomUtils;
import com.founder.e5.dom.util.TIntIntListHashMap;

/**
 * @deprecated 2006-5-17
 * 已被合并在DocTypeCache中
 * 
 * depends on context.jar
 * 
 * @version 1.0
 * @created 11-七月-2005 15:56:22
 */
public class FilterCache implements Cache {

    private TIntIntListHashMap typeID_filterIDs_map = new TIntIntListHashMap();
    private TIntObjectHashMap filterID_filter_map = new TIntObjectHashMap();

    /**
     * 获取指定id的过滤器
     * 
     * @param filterID
     * @return 如果没有，则返回null
     */
    public Filter getFilter(int filterID) {
        return (Filter) this.filterID_filter_map.get(filterID);
    }

    /**
     * 获取指定文档类型下的过滤器
     * 
     * @param doctypeID
     * @return 如果没有，则返回空数组
     */
    public Filter[] getFilters(int doctypeID) {

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

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.context.Cache#refresh()
     */
    public void refresh() throws E5Exception {

        Filter[] filterList = this.getAllFilters();

        this.filterID_filter_map.clear();
        this.typeID_filterIDs_map.clear();

        for (int i = 0; i < filterList.length; i++) {
            Filter f = filterList[i];
            this.filterID_filter_map.put(f.getFilterID(), f);
            this.typeID_filterIDs_map.put(f.getDocTypeID(), f.getFilterID());
        }

    }

    private Filter[] getAllFilters() throws E5Exception {
    	FilterManager filterManager = (FilterManager)Context.getBean(FilterManager.class);
    	return filterManager.getByTypeID(0);
    }
    /*
     * (non-Javadoc)
     * @see com.founder.e5.context.Cache#reset()
     */
    public void reset() {
        this.filterID_filter_map.clear();
        this.typeID_filterIDs_map.clear();
    }

}