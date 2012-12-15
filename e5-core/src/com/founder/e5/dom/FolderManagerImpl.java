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
import com.founder.e5.dom.util.DomUtils;

/**
 * @created 2005-7-18 14:03:34
 * @author Zhang Kaifeng
 */
class FolderManagerImpl implements FolderManager {
    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.FolderManager#create(com.founder.e5.dom.Folder,
     *      com.founder.e5.dom.FVRules[], com.founder.e5.dom.FVFilters[])
     */
    public int create(Folder folder, FVRules[] fvRules, FVFilters[] fvFilters)
            throws E5Exception {

        int folderID = EUIDFacade.getID(EUIDFacade.IDTYPE_FOLDERVIEW);
        folder.setFVID(folderID);

        BaseDAO dao = new BaseDAO();
        Session session = null;
        Transaction t = null;

        try{
            session = dao.getSession();
            t = dao.beginTransaction(session);

            // create Folder
            this.create(folder, session);

            // add rules
            if (fvRules != null)
                for( int i = 0; i < fvRules.length; i++){
                    FVRules fvRule = fvRules[i];
                    fvRule.setFvID(folderID);                    
                    addRule(fvRule, session);
                }

            // add filters
            if (fvFilters != null)
                for( int i = 0; i < fvFilters.length; i++){
                    FVFilters fvFilter = fvFilters[i];
                    fvFilter.setFvID(folderID);
                    addFilter(fvFilter, session);

                }

            dao.commitTransaction(t);
            
            return folderID;

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

    /**
     * 2006-1-17 10:14:57
     * 
     * @author zhang_kaifeng
     * @param fvRule
     * @param session
     */
    private void addRule(FVRules fvRule, Session session) {
        session.save(fvRule);
    }

    /**
     * @param folderID
     * @param session
     * @param folder
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.FolderManager#create(org.hibernate.Session,
     *      com.founder.e5.dom.Folder)
     */
    public void create(Folder folder, Session session) throws E5Exception {

        try{
            session.save(folder);

        } catch (HibernateException e){
            throw new E5Exception(e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.FolderManager#delete(int)
     */
    public void delete(int folderID) throws E5Exception {

        BaseDAO dao = new BaseDAO();
        Session session = null;
        Transaction tx = null;

        try{
            
            FolderView fv = this.get(folderID);
            int parentID = fv.getParentID();
            
            session = dao.getSession();
            tx = dao.beginTransaction(session);

            this.delete(folderID, session);
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
                dao.closeSession(session);

            } catch (HibernateException e1){
                e1.printStackTrace();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.FolderManager#delete(int, org.hibernate.Session)
     */
    public void delete(int folderID, Session ss) throws E5Exception {

        deleteChindrenFolders(folderID, ss);
    }

    /**
     * 递归删除文件夹 2006-1-16 16:42:13
     * 
     * @author zhang_kaifeng
     * @param folderID
     * @param session
     * @throws E5Exception
     */
    private void deleteChindrenFolders(int folderID, Session session)
            throws E5Exception {

        // 先删除自己
        this.removeRules(folderID, session);
        this.removeFilters(folderID, session);

        DAOHelper
                .delete(
                        "delete from FolderView as folder where folder.FVID =:fvid",
                        new Integer(folderID), Hibernate.INTEGER, session);

        FolderView[] fvs = this.getSubFVs(folderID,session);

        if (fvs == null)
            return;

        for ( int i = 0 ; i < fvs.length ; i++ ){
            int fid = fvs[i].getFVID();
            deleteChindrenFolders(fid, session);
        }

    }

    private FolderView[] getSubFVs(int folderID, Session session) throws E5Exception {
        
        List fvList = DAOHelper.find("from FolderView as fv where fv.parentID=:parentid order by fv.treeOrder",
                                         new Integer(folderID),
                                         Hibernate.INTEGER,session);
        
        if (fvList.isEmpty())
            return null;
        return (FolderView[]) fvList.toArray(DomUtils.EMPTY_FV_ARRAY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.FolderReader#get(int)
     */
    public FolderView get(int fvID) throws E5Exception {

        BaseDAO dao = new BaseDAO();
        Session session = null;
        FolderView fv = null;

        try{
            session = dao.getSession();
            fv = this.get(fvID, session);

        } catch (HibernateException e){
            throw new E5Exception(e);
        } finally{
            dao.closeSession(session);
        }

        return fv;

    }

    private FolderView get(int fvID, Session session) {
        return (FolderView) session.get(FolderView.class, new Integer(fvID));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.FolderReader#getFilterIDs(int)
     */
    public List getFilterIDs(int folderID) throws E5Exception {

        List ret = new ArrayList();
        //1
        List filterList = DAOHelper.find("from FVFilters as fvFilters where fvFilters.fvID=:fvid and fvFilters.defaultFlag=0 order by fvFilters.index",
                                         new Integer(folderID), Hibernate.INTEGER);
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
                                         new Integer(folderID), Hibernate.INTEGER);
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
                                    new Integer(folderID), Hibernate.INTEGER);
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
     * @see com.founder.e5.dom.FolderReader#getFilters(int)
     */
    public List getFilters(int folderID) throws E5Exception {

        List ids = this.getFilterIDs(folderID);
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
     * @see com.founder.e5.dom.FolderReader#getFolders(int)
     */
    public FolderView[] getChildrenFVsByDocLib(int docLibID) throws E5Exception {

        Folder rootFolder = this.getRoot(docLibID);
        
        if (rootFolder == null){
            return DomUtils.EMPTY_FV_ARRAY;
        }

        int rootID = rootFolder.getFVID();
        FolderView[] fvs = this.getAllFVByRootID(rootID);
        
        List list = new ArrayList();
        list.add(rootFolder);

        getSubTree(rootID, fvs, list);

        return (FolderView[]) list.toArray(DomUtils.EMPTY_FV_ARRAY);
    }

    /**
     * 递归遍历数组
     * 2006-2-16 16:51:16
     * @author zhang_kaifeng
     * @param parentID
     * @param fvs
     * @param list
     */
    private void getSubTree(int parentID, FolderView[] fvs, List list)
    {
        for (int i = 0; i < fvs.length; i++)
            if(fvs[i].getParentID() == parentID)
            {
                list.add(fvs[i]);
                getSubTree(fvs[i].getFVID(), fvs, list);        
            }
    }

    /**
     * 根据rootid，获取所有的FV 2006-2-16 16:43:27
     * 
     * @author zhang_kaifeng
     * @param fvid
     * @return
     * @throws E5Exception
     */
    private FolderView[] getAllFVByRootID(int fvid) throws E5Exception {

        List folderList = DAOHelper.find("from FolderView as fv where fv.rootID =:pid order by fv.treeLevel, fv.treeOrder",
                                         new Integer(fvid), Hibernate.INTEGER);
        
        return (FolderView[]) folderList.toArray(DomUtils.EMPTY_FV_ARRAY); 

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.FolderReader#getRuleIDs(int)
     */
    public int[] getRuleIDs(int folderID) throws E5Exception {
        int[] ids = new int[0];

        List fvRuleList = DAOHelper
                .find(
                      "from FVRules as f where f.fvID=:fvid order by f.subIndex",
                      new Integer(folderID), Hibernate.INTEGER);
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
     * @see com.founder.e5.dom.FolderReader#getRules(int)
     */
    public Rule[] getRules(int folderID) throws E5Exception {

        int[] ruleIDs = this.getRuleIDs(folderID);

        Rule[] rules = new Rule[ruleIDs.length];

        RuleManager ruleMgr = (RuleManager) ContextFacade
                .getBean(RuleManager.class);
        for ( int i = 0 ; i < rules.length ; i++ ){
            rules[i] = ruleMgr.get(ruleIDs[i]);
        }

        return rules;

    }

    /* (non-Javadoc)
     * @see com.founder.e5.dom.FolderManager#getSubFolders(int)
     */
    public Folder[] getSubFolders(int folderID) throws E5Exception {

        List folderList = DAOHelper.find("from Folder as folder where folder.parentID=:parentid order by folder.treeOrder",
                                         new Integer(folderID),
                                         Hibernate.INTEGER);
        
        if (folderList.isEmpty())
            return null;
        return (Folder[]) folderList.toArray(new Folder[0]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.FolderManager#getSonIDs(int)
     */
    public int[] getSubFoldersID(int foldID) throws E5Exception {

        Folder[] folders = this.getSubFolders(foldID);
        if (folders == null)
            return null;

        int[] ret = new int[folders.length];
        for ( int i = 0 ; i < ret.length ; i++ ){
            ret[i] = folders[i].getFVID();
        }

        return ret;

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.FolderReader#getSubIDs(int)
     */
    public int[] getChildrenFVIDs(int folderID) throws E5Exception {

        FolderView[] folders = this.getChildrenFVs(folderID);
        
        int[] ids = new int[folders.length];
        for ( int i = 0 ; i < ids.length ; i++ ){
            ids[i] = folders[i].getFVID();
        }

        return ids;

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.FolderReader#getSubs(int)
     */
    public FolderView[] getChildrenFVs(int folderID) throws E5Exception {

        ArrayList list = new ArrayList();
        this.getSubs(list, folderID);
        return (FolderView[]) list.toArray(new FolderView[0]);
    }

    /**
     * @param list
     * @param folderID
     * @throws E5Exception
     */
    private void getSubs(ArrayList list, int folderID) throws E5Exception {

        List folderList = DAOHelper
                .find(
                      "from FolderView as fv where fv.parentID =:pid order by fv.treeOrder",
                      new Integer(folderID), Hibernate.INTEGER);

        for ( Iterator iter = folderList.iterator() ; iter.hasNext() ; ){
            FolderView folder = (FolderView) iter.next();            
            list.add(folder);
            getSubs(list, folder.getFVID());
        }
    }

    /**
     * @param folderID
     * @param ss
     * @throws E5Exception
     */
    private void removeFilters(int folderID, Session ss) throws E5Exception {

        DAOHelper
                .delete(
                        "delete from FVFilters as fvFilters where fvFilters.fvID=:fvid",
                        new Integer(folderID), Hibernate.INTEGER, ss);
    }

    /**
     * @param folderID
     * @param ss
     * @throws E5Exception
     */
    private void removeRules(int folderID, Session ss) throws E5Exception {

        DAOHelper
                .delete(
                        "delete from FVRules as fvRules where fvRules.fvID=:fvid",
                        new Integer(folderID), Hibernate.INTEGER, ss);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.FolderManager#update(com.founder.e5.dom.Folder)
     */
    public void update(Folder folder, FVRules[] fvRules, FVFilters[] fvFilters)
            throws E5Exception {

        BaseDAO dao = new BaseDAO();
        Session session = null;
        Transaction tx = null;

        try{
            session = dao.getSession();

            tx = session.beginTransaction();

            int folderID = folder.getFVID();

            // update folder
            FolderView oriFolder = this.get(folderID, session);
            oriFolder.setFVName(folder.getFVName());
            oriFolder.setKeepDay(folder.getKeepDay());//保留天数 2009-6-12
            
            this.updateFolder(oriFolder, session);

            // update rules
            this.updateRules(folderID, fvRules, session);

            // update filters
            this.updateFilters(folderID, fvFilters, session);

            dao.commitTransaction(tx);

        } catch (HibernateException e){
            if (tx != null)
                tx.rollback();
            throw new E5Exception(e);

        } finally{
            dao.closeSession(session);
        }

    }

    private void updateFilters(int folderID, FVFilters[] fvFilters,
            Session session) throws E5Exception {
        
        this.removeFilters(folderID, session);

        for ( int i = 0 ; i < fvFilters.length ; i++ ){
            FVFilters fvFilter = fvFilters[i];
            this.addFilter(fvFilter, session);
        }
    }

    private void addFilter(FVFilters fvFilter, Session session) {
        session.save(fvFilter);

    }

    private void updateRules(int folderID, FVRules[] fvRules, Session session)
            throws E5Exception {

        this.removeRules(folderID, session);

        for ( int i = 0 ; i < fvRules.length ; i++ ){
            FVRules fvRule = fvRules[i];
            this.addRule(fvRule, session);
        }

    }

    /**
     * 2006-1-17 10:11:25
     * 
     * @author zhang_kaifeng
     * @param oriFolder
     * @param session
     */
    private void updateFolder(FolderView oriFolder, Session session) {
        session.save(oriFolder);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.FolderManager#changeParentFolder(com.founder.e5.dom.Folder)
     */
    public void drag(Folder folder) throws E5Exception {

        int folderId = folder.getFVID();
        int newParentID = folder.getParentID();   
        
        BaseDAO dao = new BaseDAO();
        try{
            
            //取得旧父节点的id，留做重新排序子节点用
            FolderView draggerFolder = this.get(folderId);
            int oriParentID = draggerFolder.getParentID();
            
            // 先将新父节点下所有子节点重新排序            
            this.reArrangeSubFVs(newParentID);            
            
            // 更新文件夹的父id，treelevel，treeorder后保存
            draggerFolder.setParentID(newParentID);
            
            FolderView newParent = this.get(newParentID);            
            draggerFolder.setTreeLevel(newParent.getTreeLevel()+1);
            
            FolderView[] newSiblings = this.getSubFVs(newParentID);            
            draggerFolder.setTreeOrder((newSiblings == null)? 0 :newSiblings.length );
            
            dao.update(draggerFolder);
            
            this.reArrangeSubFVs(oriParentID);
            
            
            
        } catch (HibernateException e){
            throw new E5Exception(e);

        } finally{
        }

    }

    /**
     * 将父节点下，从指定节点往后的所有节点的order自行加一
     * 2006-1-18 9:57:47
     * @author zhang_kaifeng
     * @param parentID
     * @param order
     * @param session
     * @throws E5Exception 
     */
    /**
    private void reOrder(int parentID, int order, Session session) throws E5Exception {
        
        Folder[] folders = this.getSubFolders(parentID);
        if(folders == null)
            return;
        
        for ( int i = 0 ; i < folders.length ; i++ ){
            Folder folder = folders[i];
            int oriOrder = folder.getTreeOrder();
            if(oriOrder >= order){
                folder.setTreeOrder(oriOrder+1);
                session.saveOrUpdate(folder);
            }
        }
    }
	**/
    /* (non-Javadoc)
     * @see com.founder.e5.dom.FolderReader#getSubFVs(int)
     */
    public FolderView[] getSubFVs(int folderID) throws E5Exception {
        
        List fvList = DAOHelper.find("from FolderView as fv where fv.parentID=:parentid order by fv.treeOrder",
                                         new Integer(folderID),
                                         Hibernate.INTEGER);
        
        if (fvList.isEmpty())
            return DomUtils.EMPTY_FV_ARRAY;
        return (FolderView[]) fvList.toArray(DomUtils.EMPTY_FV_ARRAY);
    }

    /* (non-Javadoc)
     * @see com.founder.e5.dom.FolderReader#getRoot(int)
     */
    public Folder getRoot(int docLibID) throws E5Exception {
        
        List folderList = DAOHelper.find("from Folder as f where f.treeLevel = 0 and f.docLibID = :libid",new Integer(docLibID),Hibernate.INTEGER);
        if(folderList.isEmpty())
            return null;
        
        Folder folder = (Folder) folderList.get(0);
        
        return folder;

    }

    /* (non-Javadoc)
     * @see com.founder.e5.dom.FolderManager#updateFolderName(int, java.lang.String)
     */
    public void updateFolderName(int docLibID, String newName) throws E5Exception {
        
        Folder root = this.getRoot(docLibID);
        root.setFVName(newName);
        
        BaseDAO dao = new BaseDAO();
        dao.update(root);
        
    }

    /* (non-Javadoc)
     * @see com.founder.e5.dom.FolderManager#reArrangeSubFVs(int)
     */
    public void reArrangeSubFVs(int parentID) throws E5Exception {
        
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

    /* (non-Javadoc)
     * @see com.founder.e5.dom.FolderReader#getFVs(int[])
     */
    public FolderView[] getFVs(int[] fvids) throws E5Exception {
        
        FolderView[] ret = DomUtils.EMPTY_FV_ARRAY;
        if(fvids == null)
            return ret;
        
        ret = new FolderView[fvids.length];
        for( int i = 0; i < ret.length; i++){
            ret[i] = this.get(fvids[i]);
        }
        
        return ret;
        
       
    }

    /* (non-Javadoc)
     * @see com.founder.e5.dom.FolderManager#reArrangeSubFVs(int, int[])
     */
    public void reArrangeSubFVs(int[] fvids) throws E5Exception {
        for( int i = 0; i < fvids.length; i++){
            int fvid = fvids[i];
            FolderView fv = this.get(fvid);
            fv.setTreeOrder(i);
            this.updateFolder(fv);
        }        
    }

    private void updateFolder(FolderView fv) {
        new BaseDAO().saveOrUpdate(fv);
    }
}