package com.founder.e5.context;
import java.util.List;


import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
/**
 * E5数据源管理类
 * @created 11-7-2005 15:46:27
 * @author Gong Lijie
 * @version 1.0
 */
class DSManagerImpl implements DSManager {
	private static DSManagerImpl manager;
	
	/**
	 * Singleton
	 */
	private DSManagerImpl() {
	}
	
	/**
	 * 获得Manager实例，单例
	 * @return DSManagerImpl
	 */
	public static DSManagerImpl getInstance(){
		if (manager == null)
			manager = new DSManagerImpl();
		return manager;
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.context.DSManager#create(com.founder.e5.context.E5DataSource)
	 */
	public void create(E5DataSource ds) 
	throws E5Exception
	{
		int nDSID;
		try
		{
			nDSID = (int)EUID.getID("DataSourceID");
		}
		catch (Exception e1)
		{
			throw new E5Exception("[DSCreate]getID", e1);
		}
		create(ds, nDSID);
	}
	void create(E5DataSource ds, int nDSID) 
	throws E5Exception
	{
		try
		{
			BaseDAO dao = new BaseDAO();
			ds.setDsID(nDSID);
			dao.save(ds);
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[DSCreate]", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.context.DSManager#update(com.founder.e5.context.E5DataSource)
	 */
	public void update(E5DataSource ds) 
	throws E5Exception
	{
		try
		{
			BaseDAO dao = new BaseDAO();
			dao.update(ds);
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[DSUpdate]", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.context.DSManager#delete(int)
	 */
	public void delete(int dsID) 
	throws E5Exception
	{
		DAOHelper.delete("delete from E5DataSource as ds where ds.dsID=:ds",
				new Integer(dsID),
				Hibernate.INTEGER);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.context.DSReader#get(int)
	 */
	public E5DataSource get(int dsID) 
	throws E5Exception 
	{
		try
		{
			BaseDAO dao = new BaseDAO();
			return (E5DataSource)dao.get(E5DataSource.class, new Integer(dsID));
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[DSGet]", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.context.DSManager#getAll()
	 */
	public E5DataSource[] getAll() 
	throws E5Exception 
	{
		List list = DAOHelper.find("from E5DataSource as ds");
		if (list.size() == 0)
			return null;
		return (E5DataSource[])(list.toArray(new E5DataSource[0]));
	}

}