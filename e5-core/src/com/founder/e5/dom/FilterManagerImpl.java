package com.founder.e5.dom;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.founder.e5.context.BaseDAO;
import com.founder.e5.context.Context;
import com.founder.e5.context.DAOHelper;
import com.founder.e5.context.E5Exception;
import com.founder.e5.dom.facade.EUIDFacade;
import com.founder.e5.dom.util.DomUtils;

/**
 * @created 2005-7-18 13:31:29
 * @author Zhang Kaifeng
 */
/**
 * @created 2005-7-18 13:47:04
 * @author Zhang Kaifeng
 */
class FilterManagerImpl implements FilterManager {

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.FilterManager#create(com.founder.e5.dom.Filter)
     */
    public void create(Filter filter) throws E5Exception {

        int filterID = EUIDFacade.getID(EUIDFacade.IDTYPE_FILTER);
        this.create(filterID, filter);

    }

    /**
     * @param filterID
     * @param filter
     * @throws E5Exception
     */
    void create(int filterID, Filter filter) throws E5Exception {

        filter.setFilterID(filterID);
        BaseDAO dao = new BaseDAO();
        try{
            dao.save(filter);
        } catch (HibernateException e){
            throw new E5Exception(e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.FilterReader#getByTypeID(int)
     */
    public Filter[] getByTypeID(int docTypeID) throws E5Exception {
        
        List filterList = null;
        
        if (docTypeID > 0){
            filterList = DAOHelper.find("from Filter as filter where filter.docTypeID=:doctypeid order by filter.filterID", 
            		new Integer(docTypeID), Hibernate.INTEGER);
        }else{
            filterList = DAOHelper.find("from Filter as filter order by filter.filterID");
        }
        return (Filter[]) filterList.toArray(DomUtils.EMPTY_FILTER_ARAAY);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.FilterManager#delete(int)
     */
    public void delete(int filterID) throws E5Exception {
        
        DAOHelper.delete("delete from Filter as filter where filter.filterID=:fid",
                        new Integer(filterID), Hibernate.INTEGER);
        
        DAOHelper.delete("delete from FVFilters as filter where filter.filterID=:fid",
                         new Integer(filterID), Hibernate.INTEGER);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.FilterReader#get(int)
     */
    public Filter get(int filterID) throws E5Exception {

        BaseDAO dao = new BaseDAO();
        Filter filter = null;
        try{

            filter = (Filter) dao.get(Filter.class, new Integer(filterID));

        } catch (HibernateException e){
            throw new E5Exception(e);
        }

        return filter;

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.FilterManager#update(com.founder.e5.dom.Filter)
     */
    public void update(Filter filter) throws E5Exception {

        BaseDAO dao = new BaseDAO();
        Session session = null;
        Transaction t = null;
        
        try{
            session = dao.getSession();
            t = dao.beginTransaction(session);
            
            int filterID = filter.getFilterID();
            
            Filter oriFilter = (Filter) session.get(Filter.class, new Integer(filterID));
            oriFilter.setFilterName(filter.getFilterName());
            oriFilter.setDescription(filter.getDescription());
            oriFilter.setFormula(filter.getFormula());
            
            session.flush();
            dao.commitTransaction(t);

        } catch (HibernateException e){
            
            if(t != null)
                t.rollback();
            
            throw new E5Exception(e);
            
        }finally{
            dao.closeSession(session);
        }

    }

    /* (non-Javadoc)
     * @see com.founder.e5.dom.FilterManager#getFVFilters(int)
     */
    public FolderView[] getFolderViews(int filterID) throws E5Exception {
        
        FolderView[] ret = DomUtils.EMPTY_FV_ARRAY;
        
        List fvFilterList = DAOHelper.find("from FVFilters as fvFilters where fvFilters.filterID=:filterid",new Integer(filterID),Hibernate.INTEGER);
        if(fvFilterList.isEmpty())
            return ret;
        
        ret = new FolderView[fvFilterList.size()];
        
        FolderManager folderManager = (FolderManager) Context.getBean(FolderManager.class);
        int i = 0;
        for( Iterator iter = fvFilterList.iterator(); iter.hasNext();){
            FVFilters fvFilter = (FVFilters) iter.next();
            int fvID = fvFilter.getFvID();
            ret[i++] = folderManager.get(fvID);
        }
        
        return ret;
        
    }

}