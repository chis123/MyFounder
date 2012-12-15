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
import com.founder.e5.rel.dao.RelTableDocLibFieldsDAO;
import com.founder.e5.rel.model.RelTableDocLibFields;

/**
 * @created 2006-3-27 9:16:59
 * @author Zhang Kaifeng
 * @version 
 */
public class RelTableDocLibFieldsDAOHibernate implements RelTableDocLibFieldsDAO {

    public RelTableDocLibFields[] getRelTableDocLibFields(Integer docLibId, Integer catTypeId) {
        BaseDAO dao = new BaseDAO();
        List l = dao.find("from RelTableDocLibFields as fields where fields.docLibID=:docLibID and fields.catTypeID=:catTypeID",
                 new String[]{"docLibID","catTypeID"},
                 new Object[]{docLibId,catTypeId},
                 new Type[]{Hibernate.INTEGER,Hibernate.INTEGER});
        
        return (RelTableDocLibFields[]) l.toArray(new RelTableDocLibFields[0]);
    }

    public void save(RelTableDocLibFields fields) {
        new BaseDAO().saveOrUpdate(fields);        
    }

    public void remove(Integer docLibId, Integer catTypeId) {
        RelTableDocLibFields[] fieldz = this.getRelTableDocLibFields(docLibId,catTypeId);
        BaseDAO dao = new BaseDAO();
        for( int i = 0; i < fieldz.length; i++){
            RelTableDocLibFields fields = fieldz[i];
            dao.delete(fields);
        }
    }

    public void removeFields(RelTableDocLibFields fields) {
       new BaseDAO().delete(fields);
        
    }}
