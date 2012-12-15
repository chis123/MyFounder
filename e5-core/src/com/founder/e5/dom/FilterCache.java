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
 * �ѱ��ϲ���DocTypeCache��
 * 
 * depends on context.jar
 * 
 * @version 1.0
 * @created 11-����-2005 15:56:22
 */
public class FilterCache implements Cache {

    private TIntIntListHashMap typeID_filterIDs_map = new TIntIntListHashMap();
    private TIntObjectHashMap filterID_filter_map = new TIntObjectHashMap();

    /**
     * ��ȡָ��id�Ĺ�����
     * 
     * @param filterID
     * @return ���û�У��򷵻�null
     */
    public Filter getFilter(int filterID) {
        return (Filter) this.filterID_filter_map.get(filterID);
    }

    /**
     * ��ȡָ���ĵ������µĹ�����
     * 
     * @param doctypeID
     * @return ���û�У��򷵻ؿ�����
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