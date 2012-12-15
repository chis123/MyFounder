package com.founder.e5.cat;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.type.Type;

import com.founder.e5.context.BaseDAO;
import com.founder.e5.context.DAOHelper;
import com.founder.e5.context.E5Exception;
import com.founder.e5.context.EUID;

/**
 * @created 21-7-2005 16:20:12
 * @author Gong Lijie
 * @version 1.0
 */
class CatExtManagerImpl implements CatExtManager {

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatExtReader#getExts(int, int)
	 */
	public CatExt[] getExts(int catType, int catID) 
	throws E5Exception
	{
		List list = DAOHelper.find("from CatExt as catext where catext.catType=:catType and catext.catID=:catID",
				new String[]{"catType", "catID"},
				new Object[]{new Integer(catType), new Integer(catID)},
				new Type[]{Hibernate.INTEGER, Hibernate.INTEGER});
		if (list.size() == 0) return null;
		return (CatExt[])list.toArray(new CatExt[0]);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatExtManager#save(com.founder.e5.cat.CatExt[])
	 */
	public void saveExt(int catType, int catID, CatExt[] catExtArray)
	throws E5Exception
	{
		//历史记录
		CatExt old[] = getExts(catType,catID);		
		//是否新建		
		boolean isNew= old == null?true:false;
		
		deleteCat(catType, catID);
		
		if (catExtArray == null)
			return;
		
		BaseDAO dao = new BaseDAO();
		Session s = null;
		Transaction t = null;
		try
		{
			s = dao.getSession();
			t = dao.beginTransaction(s);
			for (int i = 0; i < catExtArray.length; i++)
			{
				if(isNew)				
					CatExtCascadeHelper.createExtCascade(catExtArray[i]);				
				else							
					CatExtCascadeHelper.updateExtCascade(catExtArray[i],old,s.connection());
				
				s.save(catExtArray[i]);
			}
			t.commit();
		}
		catch (HibernateException e)
		{
			try{t.rollback();}catch(Exception e1){e1.printStackTrace();}
			throw new E5Exception("[CatTypeCreate]", e);
		}
		finally
		{
			try{dao.closeSession(s);}catch(Exception e1){e1.printStackTrace();}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatExtManager#deleteCat(int, int)
	 */
	public void deleteCat(int catType, int catID)
	throws E5Exception
	{
		DAOHelper.delete("delete from CatExt as catext where catext.catType=:catType and catext.catID=:catID",
				new String[]{"catType", "catID"},
				new Object[]{new Integer(catType), new Integer(catID)},
				new Type[]{Hibernate.INTEGER, Hibernate.INTEGER});
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatExtReader#get(int, int, java.lang.String)
	 */
	public CatExt getExt(int catType, int catID, String extType) 
	throws E5Exception
	{
		int extTypeID = getExtTypeID(extType);
		return getExt(catType, catID, extTypeID);
	}
	
	private int getExtTypeID(String extType) throws E5Exception
	{
		CatExtType type = getExtType(extType);
		if (type == null) return 0;
		
		return type.getType();
	}
	
	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatExtReader#getExts(int, java.lang.String)
	 */
	public CatExt[] getAllExts(int catType, String extType) throws E5Exception
	{
		int extTypeID = getExtTypeID(extType);
		return getAllExts(catType, extTypeID);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatExtReader#getSub(int, int, java.lang.String)
	 */
	public CatExt[] getSubExt(int catType, int catID, String extType) throws E5Exception
	{
		int extTypeID = getExtTypeID(extType);
		return getSubExt(catType, catID, extTypeID);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatExtManager#create(com.founder.e5.cat.CatExtType)
	 */
	public void createExtType(CatExtType extType) throws E5Exception
	{
		Session s = null;
		Transaction t = null;
		BaseDAO dao = new BaseDAO();
		try
		{			
			
			int id = (int)EUID.getID("CatExtType");
			extType.setType(id);		
			
			s = dao.getSession();
			t = dao.beginTransaction(s);
			//1) 保存扩展类型			
			dao.save(extType, s);				

			
			//2) 如果分类已存在，初始化分类别名级联			
			CatExtCascadeHelper.createExtType(extType,s.connection());
			
			dao.commitTransaction(t);
		}
		catch (HibernateException e)
		{
			if (null != t) t.rollback();
			throw new E5Exception("[CatTypeCreate]", e);
		}
		finally {
			dao.closeSession(s);
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatExtManager#delete(java.lang.String)
	 */
	public void deleteExtType(int extType) throws E5Exception
	{
		//先删除扩展属性类型
		DAOHelper.delete("delete from CatExtType as cet where cet.type=:type", 
				new Integer(extType), Hibernate.INTEGER);
		//再删除扩展属性。这个操作不成功也没有关系，因此不做事务处理
		DAOHelper.delete("delete from CatExt as cet where cet.extType=:type", 
				new Integer(extType), Hibernate.INTEGER);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatExtManager#update(com.founder.e5.cat.CatExtType)
	 */
	public void updateExtType(CatExtType extType) throws E5Exception
	{
		try
		{
			BaseDAO dao = new BaseDAO();
			dao.update(extType);
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[CatTypeCreate]", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatExtManager#get(java.lang.String)
	 */
	public CatExtType getExtType(String extType) throws E5Exception
	{
		List list = DAOHelper.find("from CatExtType as cet where cet.typeName=:name", 
				extType, Hibernate.STRING);
		if (list.size() == 0) 
			return null;
		else
			return (CatExtType)list.get(0);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatExtManager#getExtTypes()
	 */
	public CatExtType[] getExtTypes() throws E5Exception
	{
		BaseDAO dao = new BaseDAO();
		try
		{
			Session s = null;
			try
			{
				s = dao.getSession();
				List list = s.createCriteria(CatExtType.class)
					.addOrder(Order.asc("catType"))
					.addOrder(Order.asc("order"))					
					.list();
				if (list.size() == 0) return null;
				return (CatExtType[])list.toArray(new CatExtType[0]);
			}
			finally
			{
				dao.closeSession(s);
			}
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[GetCatTypes]", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatExtManager#getExtTypes(int)
	 */
	public CatExtType[] getExtTypes(int catType) throws E5Exception
	{
		//扩展属性类型中，没有指定分类类型的，以及指定了分类类型=catType的，都检索出来		
		List list = DAOHelper.find("from CatExtType as et where (et.catType=0) or (et.catType=:ctype)", 
				new Integer(catType), Hibernate.INTEGER);
		if (list.size() == 0) return null;
		return (CatExtType[])list.toArray(new CatExtType[0]);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatExtManager#get(int)
	 */
	public CatExtType getExtType(int extType) throws E5Exception
	{
		BaseDAO dao = new BaseDAO();
		try
		{
			return (CatExtType)dao.get(CatExtType.class, new Integer(extType));
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[GetCatExtType]", e);
		}
			
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatExtReader#getSubExt(int, int[], java.lang.String)
	 */
	public CatExt[] getSubExt(int catType, int[] catIDs, String extType) throws E5Exception
	{
		int extTypeID = getExtTypeID(extType);
		return getSubExt(catType, catIDs, extTypeID);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatExtReader#getExt(int, int, int)
	 */
	public CatExt getExt(int catType, int catID, int extType) throws E5Exception
	{
		if (extType < 1) return null;
		
		BaseDAO dao = new BaseDAO();
		try
		{
			CatExt ext = new CatExt(extType, catType, catID);
			return (CatExt)dao.get(CatExt.class, ext);
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[GetCatExt]", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatExtReader#getAllExts(int, int)
	 */
	public CatExt[] getAllExts(int catType, int extType) throws E5Exception
	{
		if (extType < 1) return null;
		List list = DAOHelper.find("from CatExt as catext where catext.extType=:et and catext.catType=:ct order by catext.catID",
				new String[]{"et", "ct"}, 
				new Object[]{new Integer(extType), new Integer(catType)},
				new Type[]{Hibernate.INTEGER, Hibernate.INTEGER});
		if (list.size() == 0) return null;
		return (CatExt[])list.toArray(new CatExt[0]);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatExtReader#getSubExt(int, int, int)
	 */
	public CatExt[] getSubExt(int catType, int catID, int extType) throws E5Exception
	{
		if (extType < 1) return null;
		List list = DAOHelper.find("from CatExt as catext where catext.extType=:et and catext.catType=:ct and catext.parentID=:pid order by catext.catID", 
				new String[]{"et", "ct", "pid"}, 
				new Object[]{new Integer(extType), new Integer(catType), new Integer(catID)},
				new Type[]{Hibernate.INTEGER, Hibernate.INTEGER, Hibernate.INTEGER});
		if (list.size() == 0) return null;
		return (CatExt[])list.toArray(new CatExt[0]);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatExtReader#getSubExt(int, int[], int)
	 */
	public CatExt[] getSubExt(int catType, int[] catIDs, int extType) throws E5Exception
	{
		if (extType < 1) return null;

		List list = null;
		List idList = new ArrayList();
		if (catIDs != null)
			for (int i = 0; i < catIDs.length; i++)
				idList.add(new Integer(catIDs[i]));
		String query = "from CatExt as catext where catext.extType=:et and catext.catType=:ct and catext.parentID in (:pid) order by catext.catID";
		BaseDAO dao = new BaseDAO();
		Session s = null;
		try
		{
			//因为要使用setParameterList,所以不调用DAOHelper.find
			s = dao.getSession();
			list = s.createQuery(query)
				.setInteger("et", extType)
				.setInteger("ct", catType)
				.setParameterList("pid", idList)
				.list();
		}
		catch (HibernateException e)
		{
			throw new E5Exception(e);
		}
		finally {
			dao.closeSession(s);
		}
		
		if (list == null || list.size() == 0) return null;
		return (CatExt[])list.toArray(new CatExt[0]);
	}
}