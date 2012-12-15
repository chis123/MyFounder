package com.founder.e5.dom;

import gnu.trove.TIntArrayList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.founder.e5.context.BaseDAO;
import com.founder.e5.context.DAOHelper;
import com.founder.e5.context.E5Exception;
import com.founder.e5.dom.facade.ContextFacade;
import com.founder.e5.dom.facade.EUIDFacade;

/**
 * @created 2005-7-19 13:06:45
 * @author Zhang Kaifeng
 * @version
 */
class ViewManagerImpl implements ViewManager {

    /**
     * @param viewID
     * @param s
     * @param view
     * @throws E5Exception
     */
    public void create(View view ,Session s) throws E5Exception {

        try{
            s.save(view);

        } catch (HibernateException e){
            throw new E5Exception(e);
        }

    }


    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.ViewManager#delete(int)
     */
    public void delete(int viewID) throws E5Exception {

        BaseDAO dao = new BaseDAO();
        Session ss = null;
        Transaction tx = null;

        try{
            FolderView fv = this.get(viewID);
            int parentID = fv.getParentID();
            
            ss = dao.getSession();
            tx = dao.beginTransaction(ss);

            this.removeRules(viewID, ss);
            this.removeFilters(viewID, ss);

            DAOHelper.delete("delete from View as v where v.FVID=:fvid",
                             new Integer(viewID), Hibernate.INTEGER, ss);
            dao.commitTransaction(tx);
            
            if(parentID != 0)
                this.reArrangeSubFVs(parentID);

        } catch (HibernateException e){
            if (tx != null)
                try{
                    tx.rollback();
                } catch (HibernateException e1){
                    e1.printStackTrace();
                }

            throw new E5Exception(e);
        } finally{
            try{
                dao.closeSession(ss);
            } catch (HibernateException e1){
                e1.printStackTrace();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.ViewReader#get(int)
     */
    public View get(int viewID) throws E5Exception {

        BaseDAO dao = new BaseDAO();
        View view = null;

        try{
            view = (View) dao.get(View.class, new Integer(viewID));

        } catch (HibernateException e){
            throw new E5Exception(e);
        }

        return view;

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.ViewReader#getFilterIDs(int)
     */
    public List getFilterIDs(int viewID) throws E5Exception {

        List ret = new ArrayList();
        //1
        List filterList = DAOHelper.find("from FVFilters as fvFilters where fvFilters.fvID=:fvid and fvFilters.defaultFlag=0 order by fvFilters.index",
                                         new Integer(viewID), Hibernate.INTEGER);
        if (filterList.isEmpty())
            return ret;
        TIntArrayList ids = new TIntArrayList();
        for ( Iterator iter = filterList.iterator() ; iter.hasNext() ; ){
            FVFilters ff = (FVFilters) iter.next();
            ids.add(ff.getFilterID());
        }
        ret.add(ids.toNativeArray());
        //2
        filterList = DAOHelper.find("from FVFilters as fvFilters where fvFilters.fvID=:fvid and fvFilters.defaultFlag=1 order by fvFilters.index",
                                         new Integer(viewID), Hibernate.INTEGER);
        if (filterList.isEmpty())
            return ret;
        ids = new TIntArrayList();
        for ( Iterator iter = filterList.iterator() ; iter.hasNext() ; ){
            FVFilters ff = (FVFilters) iter.next();
            ids.add(ff.getFilterID());
        }
        ret.add(ids.toNativeArray());
        //3
        filterList = DAOHelper.find("from FVFilters as fvFilters where fvFilters.fvID=:fvid and fvFilters.defaultFlag=2 order by fvFilters.index",
                                    new Integer(viewID), Hibernate.INTEGER);
        if (filterList.isEmpty())
            return ret;
        ids = new TIntArrayList();
        for( Iterator iter = filterList.iterator(); iter.hasNext();){
            FVFilters ff = (FVFilters) iter.next();
            ids.add(ff.getFilterID());
        }
        ret.add(ids.toNativeArray());
        
        return ret;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.ViewReader#getFilters(int)
     */
    public List getFilters(int viewID) throws E5Exception {

        List ids = this.getFilterIDs(viewID);
        List ret = new ArrayList();
        FilterManager filterMgr = (FilterManager) ContextFacade.getBean(FilterManager.class);
        for( Iterator iter = ids.iterator(); iter.hasNext();){
            int[] id = (int[]) iter.next();
            Filter[] filters = new Filter[id.length]; 
            for ( int i = 0 ; i < filters.length ; i++ ){
                filters[i] = filterMgr.get(id[i]);
            }
            
            ret.add(filters);
            
        }
        return ret;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.ViewReader#getRuleIDs(int)
     */
    public int[] getRuleIDs(int viewID) throws E5Exception {
        int[] ids = new int[0];
        List fvRuleList = DAOHelper
                .find(
                      "from FVRules as f where f.fvID=:fvid order by f.subIndex",
                      new Integer(viewID), Hibernate.INTEGER);

        if (fvRuleList.isEmpty())
            return ids;

        ids = new int[fvRuleList.size()];
        int i = 0;
        for ( Iterator iter = fvRuleList.iterator() ; iter.hasNext() ; ){
            FVRules f = (FVRules) iter.next();
            int ruleID = f.getRuleID();
            ids[i++] = ruleID;
        }
        return ids;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.ViewReader#getRules(int)
     */
    public Rule[] getRules(int viewID) throws E5Exception {

        int[] ruleIDs = this.getRuleIDs(viewID);

        Rule[] rules = new Rule[ruleIDs.length];

        RuleManager ruleMgr = (RuleManager) ContextFacade
                .getBean(RuleManager.class);
        for ( int i = 0 ; i < rules.length ; i++ ){
            rules[i] = ruleMgr.get(ruleIDs[i]);
        }

        return rules;

    }

    /**
     * @param viewID
     * @param ss
     * @throws E5Exception
     */
    private void removeFilters(int viewID, Session ss) throws E5Exception {
        DAOHelper
                .delete(
                        "delete from FVFilters as fvFilters where fvFilters.fvID=:fvid",
                        new Integer(viewID), Hibernate.INTEGER, ss);
    }

    private void removeRules(int viewID, Session ss) throws E5Exception {
        DAOHelper
                .delete(
                        "delete from FVRules as fvRules where fvRules.fvID=:fvid",
                        new Integer(viewID), Hibernate.INTEGER, ss);
    }

   
    /**
     * 2006-3-24 11:51:58
     * @author zhang_kaifeng
     * @param parentID
     * @throws E5Exception
     */
    private void reArrangeSubFVs(int parentID) throws E5Exception {
        
        List fvList = DAOHelper.find("from FolderView as fv where fv.parentID=:parentid order by fv.treeOrder",
                                         new Integer(parentID),
                                         Hibernate.INTEGER);

        int i = 0 ;
        BaseDAO dao = new BaseDAO();
        for( Iterator iter = fvList.iterator(); iter.hasNext();){
            FolderView fv = (FolderView) iter.next();
            fv.setTreeOrder(i++);
            dao.saveOrUpdate(fv);            
        }
        
    }

    public int create(View view, FVRules[] fvRules, FVFilters[] fvFilters) throws E5Exception {

        int viewID = EUIDFacade.getID(EUIDFacade.IDTYPE_FOLDERVIEW);
        view.setFVID(viewID);

        BaseDAO dao = new BaseDAO();
        Session session = null;
        Transaction t = null;

        try{
            session = dao.getSession();
            t = dao.beginTransaction(session);

            // create Folder
            this.create(view, session);

            // add rules
            if (fvRules != null)
                for( int i = 0; i < fvRules.length; i++){
                    FVRules fvRule = fvRules[i];
                    fvRule.setFvID(viewID);                    
                    addRule(fvRule, session);
                }

            // add filters
            if (fvFilters != null)
                for( int i = 0; i < fvFilters.length; i++){
                    FVFilters fvFilter = fvFilters[i];
                    fvFilter.setFvID(viewID);
                    addFilter(fvFilter, session);

                }

            dao.commitTransaction(t);
            
            return viewID;

        } catch (HibernateException e){
            if (null != t)
                try{
                    t.rollback();
                } catch (HibernateException e1){
                    e1.printStackTrace();
                }
            throw new E5Exception(e);

        } finally{
            try{
                dao.closeSession(session);
            } catch (HibernateException e1){
                e1.printStackTrace();
            }
        }

    }

    private void addFilter(FVFilters fvFilter, Session session) {
        session.save(fvFilter);

    }

    private void addRule(FVRules fvRule, Session session) {
        session.save(fvRule);
    }

    public void delete(int viewID, Session ss) throws E5Exception {
        // TODO Auto-generated method stub
        
    }

    public void update(View view, FVRules[] fvRules, FVFilters[] fvFilters) throws E5Exception {

        BaseDAO dao = new BaseDAO();
        Session session = null;
        Transaction tx = null;

        try{
            session = dao.getSession();

            tx = session.beginTransaction();

            int viewID = view.getFVID();

            // update folder
            View oriView = this.get(viewID, session);
            oriView.setFVName(view.getFVName());
            oriView.setViewFormula(view.getViewFormula());
            this.updateView(oriView, session);

            // update rules
            this.updateRules(viewID, fvRules, session);

            // update filters
            this.updateFilters(viewID, fvFilters, session);

            dao.commitTransaction(tx);

        } catch (HibernateException e){
            if (tx != null)
                tx.rollback();
            throw new E5Exception(e);

        } finally{
            dao.closeSession(session);
        }

    }
    
    private void updateFilters(int viewID, FVFilters[] fvFilters, Session session) throws E5Exception {
        this.removeFilters(viewID, session);

        for ( int i = 0 ; i < fvFilters.length ; i++ ){
            FVFilters fvFilter = fvFilters[i];
            this.addFilter(fvFilter, session);
        }
    }


    private void updateRules(int viewID, FVRules[] fvRules, Session session) throws E5Exception {

        this.removeRules(viewID, session);

        for ( int i = 0 ; i < fvRules.length ; i++ ){
            FVRules fvRule = fvRules[i];
            this.addRule(fvRule, session);
        }

    }


    private void updateView(View oriView, Session session) {
        session.save(oriView);
    }


    private View get(int fvID, Session session) {
        return (View) session.get(View.class, new Integer(fvID));
    }
}