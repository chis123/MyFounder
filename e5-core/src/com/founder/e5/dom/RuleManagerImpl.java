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
 * @version 1.0
 * @created 11-ÆßÔÂ-2005 15:27:18
 */
class RuleManagerImpl implements RuleManager {

    public RuleManagerImpl() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.RuleManager#create(com.founder.e5.dom.Rule)
     */
    public void create(Rule rule) throws E5Exception {

        int ruleID = EUIDFacade.getID(EUIDFacade.IDTYPE_RULE);
        this.create(ruleID, rule);

    }

    /**
     * @param ruleID
     * @param rule
     * @throws
     */
    private void create(int ruleID, Rule rule) throws E5Exception {

        rule.setRuleID(ruleID);
        BaseDAO dao = new BaseDAO();

        try{
            dao.save(rule);
        } catch (HibernateException e){
            throw new E5Exception(e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.RuleReader#get(int)
     */
    public Rule get(int ruleID) throws E5Exception {

        BaseDAO dao = new BaseDAO();
        Rule rule = null;
        try{
            rule = (Rule) dao.get(Rule.class, new Integer(ruleID));

        } catch (HibernateException e){
            throw new E5Exception(e);
        }
        return rule;

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.RuleManager#delete(int)
     */
    public void delete(int ruleID) throws E5Exception {
        DAOHelper.delete("delete from Rule as rule where rule.ruleID=:rid",
                         new Integer(ruleID), Hibernate.INTEGER);
        
        DAOHelper.delete("delete from FVRules as rule where rule.ruleID=:rid",
                         new Integer(ruleID), Hibernate.INTEGER);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.RuleReader#getByDoctypeID(int)
     */
    public Rule[] getByDoctypeID(int docTypeID) throws E5Exception {
        
        List ruleList = null;
        
        if (docTypeID > 0){
            
            ruleList = DAOHelper.find("from Rule as rule where rule.docTypeID=:doctypeid order by rule.ruleID", 
            		new Integer(docTypeID), Hibernate.INTEGER);
            
        }else{
            ruleList = DAOHelper.find("from Rule as rule order by rule.ruleID");
        }
        
        return (Rule[]) ruleList.toArray(DomUtils.EMPTY_RULE_ARRAY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.RuleManager#update(com.founder.e5.dom.Rule)
     */
    public void update(Rule rule) throws E5Exception {

        BaseDAO dao = new BaseDAO();
        Session session = null;
        Transaction t = null;
        
        try{
            session = dao.getSession();
            t = session.beginTransaction();
            
            int ruleID = rule.getRuleID();
            Rule oriRule = (Rule) dao.get(Rule.class, new Integer(ruleID),session);
            
            oriRule.setRuleName(rule.getRuleName());
            oriRule.setDescription(rule.getDescription());
            oriRule.setRuleArguments(rule.getRuleArguments());
            oriRule.setRuleClassName(rule.getRuleClassName());
            oriRule.setRuleMethod(rule.getRuleMethod());
            
            session.flush();
            dao.commitTransaction(t);
            
        } catch (HibernateException e){
            if(null != t)
                t.rollback();
            
            throw new E5Exception(e);
        }finally{
            dao.closeSession(session);
        }

    }

    /* (non-Javadoc)
     * @see com.founder.e5.dom.RuleManager#getFolderViews(int)
     */
    public FolderView[] getFolderViews(int ruleID) throws E5Exception {
        
        FolderView[] ret = DomUtils.EMPTY_FV_ARRAY;
        
        List fvRulesList = DAOHelper.find("from FVRules as fvRules where fvRules.ruleID=:ruleid",
                                          new Integer(ruleID),Hibernate.INTEGER);
        if(fvRulesList.isEmpty())
            return ret;
        
        ret = new FolderView[fvRulesList.size()];
        
        FolderManager folderManager = (FolderManager) Context.getBean(FolderManager.class);
        int i = 0;
        for( Iterator iter = fvRulesList.iterator(); iter.hasNext();){
            FVRules fvRule = (FVRules) iter.next();
            int fvID = fvRule.getFvID();
            ret[i++] = folderManager.get(fvID);
        }
        
        return ret;
        
    }

}
