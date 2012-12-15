/**********************************************************************
 * Copyright (c) 2002,�������������޹�˾���ӳ�����ҵ������ϵͳ������
 * All rights reserved.
 * 
 * ժҪ��
 * 
 * ��ǰ�汾��1.0
 * ���ߣ� �ſ���   Zhang_KaiFeng@Founder.Com
 * ������ڣ�2006-3-27  9:07:42
 *  
 *********************************************************************/
package com.founder.e5.rel.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Hibernate;

import com.founder.e5.context.BaseDAO;
import com.founder.e5.context.Context;
import com.founder.e5.context.DSManager;
import com.founder.e5.context.E5DataSource;
import com.founder.e5.context.E5Exception;
import com.founder.e5.db.DBSession;
import com.founder.e5.dom.template.TriggerTemplateManager;
import com.founder.e5.rel.dao.RelTableDocLibDAO;
import com.founder.e5.rel.model.RelTableDocLib;
import com.founder.e5.rel.model.RelTableDocLibVO;
import com.founder.e5.rel.service.RelTableDocLibManager;

/**
 * @created 2006-3-27 9:07:42
 * @author Zhang Kaifeng
 * @version 
 */
public class RelTableDocLibManagerImpl implements RelTableDocLibManager {
    
    private RelTableDocLibDAO dao;
    
    /* (non-Javadoc)
     * @see com.founder.e5.rel.service.RelTableDocLibManager#getRelTableDocLibs()
     */
    public RelTableDocLibVO[] getRelTableDocLibs() throws E5Exception {
        
        RelTableDocLibVO[] vos = new RelTableDocLibVO[0];
        
        List l = dao.getRelTableDocLibs();
        
        if(l.isEmpty())
            return vos;
        
        List ret = new ArrayList();
        for( Iterator iter = l.iterator(); iter.hasNext();){
            RelTableDocLib po = (RelTableDocLib) iter.next();
            RelTableDocLibVO vo = null;
            try{
                vo = new RelTableDocLibVO(po);
            } catch (Exception e){
                throw new E5Exception(e);
            }
            ret.add(vo);
        }
        return (RelTableDocLibVO[]) ret.toArray(vos);
        
    }

    /* (non-Javadoc)
     * @see com.founder.e5.rel.service.RelTableDocLibManager#getRelTableDocLib(int, int, int)
     */
    public RelTableDocLibVO getRelTableDocLib(int docLibId,
            int catTypeId) throws E5Exception {
        
        
        RelTableDocLib po = dao.getRelTableDocLib(new Integer(docLibId),new Integer(catTypeId));
        
        if(po == null)
            return null;
        
        RelTableDocLibVO vo = null;
        try{
            vo = new RelTableDocLibVO(po);
            
        } catch (Exception e){
            throw new E5Exception(e);
        }
        return vo;
    }

    /* (non-Javadoc)
     * @see com.founder.e5.rel.service.RelTableDocLibManager#removeRelTableDocLib(int, int, int)
     */
    public void removeRelTableDocLib(int docLibID, int catTypeId) throws Exception 
    {
    	//ȡ������������Ӧ��Ϣ
        RelTableDocLibVO vo = this.getRelTableDocLib(docLibID, catTypeId);
        int dsID = vo.getDocLib().getDsID();
        String relTableName = vo.getRelTable().getTableName();
        
        //�õ��ĵ������ڵ����ݿ�����
        DSManager dsManager = (DSManager)Context.getBean(DSManager.class);
        E5DataSource ds = dsManager.get(dsID);
        String dbType = ds.getDbType();
        
        //���ݲ�ͬ�����ݿ����ͣ��õ��ĵ����ϵĴ���������
        TriggerTemplateManager temlateManager = TriggerTemplateManager.getTemplateManager(dbType);
        String[] triggers = temlateManager.getTriggers(relTableName, docLibID);
        
        //ɾ��������
        DBSession dbSession = null;
        try{
            dbSession = Context.getDBSession(dsID,true);
            for (int i = 0; i < triggers.length; i++) {
            	dbSession.executeUpdate("drop trigger " + triggers[i], null);
			}
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        	if (dbSession != null) dbSession.closeQuietly();
        }
        dao.remove(new Integer(docLibID), new Integer(catTypeId));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.rel.service.RelTableDocLibManager#saveRelTableDocLib(com.founder.e5.rel.model.RelTableDocLibVO)
     */
    public void saveRelTableDocLib(RelTableDocLibVO vo) {
        
        dao.save(new RelTableDocLib(vo));
    }

    public boolean isRelTableReferenced(int relTableId) {
        
        BaseDAO dao = new BaseDAO();
        List l = dao.find("from RelTableDocLib as r where r.relTableId = :relTableId",
                 new Integer(relTableId),
                 Hibernate.INTEGER);
        
        if(l.isEmpty())
            return false;
        return true;

    }

    /* (non-Javadoc)
     * @see com.founder.e5.rel.service.RelTableDocLibManager#setDao(com.founder.e5.rel.dao.RelTableDocLibDAO)
     */
    public void setDao(RelTableDocLibDAO dao) {
        this.dao = dao;
    }

}
