/**********************************************************************
 * Copyright (c) 2002,北大方正电子有限公司电子出版事业部集成系统开发部
 * All rights reserved.
 * 
 * 摘要：
 * 
 * 当前版本：1.0
 * 作者： 张凯峰   Zhang_KaiFeng@Founder.Com
 * 完成日期：2006-3-27  9:16:59
 *  
 *********************************************************************/
package com.founder.e5.rel.dao.hibernate;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.type.Type;

import com.founder.e5.context.BaseDAO;
import com.founder.e5.context.DAOHelper;
import com.founder.e5.context.E5Exception;
import com.founder.e5.rel.dao.RelTableDocLibDAO;
import com.founder.e5.rel.model.RelTableDocLib;

/**
 * @created 2006-3-27 9:16:59
 * @author Zhang Kaifeng
 * @version 
 */
public class RelTableDocLibDAOHibernate implements RelTableDocLibDAO {

    /* (non-Javadoc)
     * @see com.founder.e5.rel.dao.RelTableDocLibDAO#getRelTableDocLib(java.lang.Integer, java.lang.Integer, java.lang.Integer)
     */
    public RelTableDocLib getRelTableDocLib(Integer docLibId,
            Integer catTypeId) {
        
        BaseDAO dao = new BaseDAO();
        List l  = dao.find("from RelTableDocLib as r where r.docLibId=:docLibId and r.catTypeId=:catTypeId",
                 new String[]{"docLibId","catTypeId"},
                 new Object[]{docLibId,catTypeId},
                 new Type[]{Hibernate.INTEGER,Hibernate.INTEGER}
                 );
        
        if(l.isEmpty())
            return null;
        return (RelTableDocLib) l.get(0);
    }

    /* (non-Javadoc)
     * @see com.founder.e5.rel.dao.RelTableDocLibDAO#getRelTableDocLibs()
     */
    public List getRelTableDocLibs() throws E5Exception {
        
        return DAOHelper.find("from RelTableDocLib as r order by r.relTableId");
    }

    /* (non-Javadoc)
     * @see com.founder.e5.rel.dao.RelTableDocLibDAO#save(com.founder.e5.rel.model.RelTableDocLib)
     */
    public void save(RelTableDocLib relTableDocLib) {
        
        BaseDAO dao = new BaseDAO();
        dao.saveOrUpdate(relTableDocLib);

    }

    /* (non-Javadoc)
     * @see com.founder.e5.rel.dao.RelTableDocLibDAO#remove(java.lang.Integer, java.lang.Integer, java.lang.Integer)
     */
    public void remove(Integer docLibId, Integer catTypeId) {
        
        BaseDAO dao = new BaseDAO();
        dao.delete("delete from RelTableDocLib as r where r.docLibId=:docLibId and r.catTypeId=:catTypeId",
                 new String[]{"docLibId","catTypeId"},
                 new Object[]{docLibId,catTypeId},
                 new Type[]{Hibernate.INTEGER,Hibernate.INTEGER}
                 );
    }

}
