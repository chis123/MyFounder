package com.founder.e5.rel.service.impl;

import com.founder.e5.rel.dao.RelTableDocLibFieldsDAO;
import com.founder.e5.rel.model.RelTableDocLibFields;
import com.founder.e5.rel.service.RelTableDocLibFieldsManager;

/**
 * @created 2006-3-27 9:07:42
 * @author Zhang Kaifeng
 * @version 
 */
public class RelTableDocLibFieldsManagerImpl implements RelTableDocLibFieldsManager {

    private RelTableDocLibFieldsDAO dao;

    public void setDao(RelTableDocLibFieldsDAO dao) {
        this.dao = dao;
        
    }

    public RelTableDocLibFields[] getRelTableDocLibFields(int docLibId, int catTypeId) {
       return dao.getRelTableDocLibFields(new Integer(docLibId),new Integer(catTypeId));
    }

    public void save(RelTableDocLibFields fields) {
        dao.save(fields);
    }

    public void remove(int docLibId, int catTypeId) {
        dao.remove(new Integer(docLibId),new Integer(catTypeId));
    }

    public void removeFields(RelTableDocLibFields fields) {
        dao.removeFields(fields);
        
    }}
