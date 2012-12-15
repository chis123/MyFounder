package com.founder.e5.rel.dao.hibernate;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.orm.ObjectRetrievalFailureException;

import com.founder.e5.context.BaseDAO;
import com.founder.e5.rel.dao.RelTableDAO;
import com.founder.e5.rel.model.RelTable;


/**
 * This class interacts with Spring and Hibernate to save and
 * retrieve User objects.
 *
 */
public class RelTableDAOHibernate implements RelTableDAO {
    
    /* (non-Javadoc)
     * @see com.founder.e5.rel.dao.RelTableDAO#getRelTable(java.lang.Integer)
     */
    public RelTable getRelTable(Integer tableId) {
        
        BaseDAO dao = new BaseDAO();        
        RelTable table = (RelTable) dao.get(RelTable.class,tableId);
        if(table == null)
            throw new ObjectRetrievalFailureException(RelTable.class,tableId);
        
        return table;
    }

    /* (non-Javadoc)
     * @see com.founder.e5.rel.dao.RelTableDAO#getRelTables()
     */
    public List getRelTables(int docTypeId) {
        
        if(docTypeId == 0)
            return new BaseDAO().find("from RelTable as t");
        
        return new BaseDAO().find("from RelTable as t where t.refDocTypeID="+docTypeId);
    }

    /* (non-Javadoc)
     * @see com.founder.e5.rel.dao.RelTableDAO#removeReltable(java.lang.Integer)
     */
    public void removeReltable(Integer tableId) {
        new BaseDAO().delete(getRelTable(tableId));
        
    }

    /* (non-Javadoc)
     * @see com.founder.e5.rel.dao.RelTableDAO#saveRelTable(com.founder.e5.rel.model.RelTable)
     */
    public void saveRelTable(RelTable table) {
        //save table record
        new BaseDAO().saveOrUpdate(table);
        
    }

    /* (non-Javadoc)
     * @see com.founder.e5.rel.dao.RelTableDAO#getRelTable(java.lang.String)
     */
    public RelTable getRelTable(String tableSuffix) {
        BaseDAO dao = new BaseDAO();
        List l = dao.find("from RelTable as t where t.tableName = :tableName",
                 "DOM_REL_"+tableSuffix.toUpperCase(),
                 Hibernate.STRING);
        if(!l.isEmpty())
            return (RelTable) l.get(0);
        return null;
    }

}
