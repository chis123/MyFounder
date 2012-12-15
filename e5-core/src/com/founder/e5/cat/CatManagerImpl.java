package com.founder.e5.cat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import com.founder.e5.commons.ResourceMgr;
import com.founder.e5.context.BaseDAO;
import com.founder.e5.context.Context;
import com.founder.e5.context.DAOHelper;
import com.founder.e5.context.E5Exception;
import com.founder.e5.context.EUID;
import com.founder.e5.db.DBSession;
import com.founder.e5.db.IResultSet;

/**
 * 分类管理器的实现类
 * @created 21-7-2005 16:28:32
 * @author Gong Lijie
 * @version 1.0
 */
class CatManagerImpl implements CatManager 
{
	private static String m_SelectLikeSQL = 
		"select * from CATEGORY_OTHER where WT_TYPE=? and ENTRY_NAME like ? and ENTRY_DELETE_FLAG=0";
	
	private static String m_SelectByNameSQL = 
		"select * from CATEGORY_OTHER where WT_TYPE=? and ENTRY_NAME=? and ENTRY_DELETE_FLAG=0";
	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatManager#getCatsByName(int, java.lang.String)
	 */
	public Category[] getCatsByName(int catType, String catName)
	throws E5Exception
	{
		DBSession db = Context.getDBSession();
		try
		{
			IResultSet rs = db.executeQuery(getSQL(catType, m_SelectLikeSQL),
					new Object[]{new Integer(catType), "%" + catName + "%"});
			List list = new ArrayList();
			while (rs.next())
				list.add(readRS(rs));
			rs.close();
			
			if (list.size() == 0) return null;
			return (Category[])list.toArray(new Category[0]);
		}
		catch (SQLException e)
		{
			throw new E5Exception(e);
		}
		finally
		{
			try{db.close();}catch(Exception e1){e1.printStackTrace();}
		}
	}
	
	private static String m_InsertSQL = "insert into CATEGORY_OTHER (WT_TYPE,PARENT_ID,ENTRY_CODE,ENTRY_NAME,ENTRY_LEVEL,ENTRY_DISP_ORDER,ENTRY_MEMO,LAST_MODI_MAN,LAST_MODI_DATE,IS_REFRENCE,REFRENCE_TYPE,REFRENCE_TABLE,REFRENCE_ID,ENTRY_LINKTYPE,ENTRY_LINKTABLE,ENTRY_LINKID,ENTRY_SAME,ENTRY_DELETE_FLAG,ENTRY_PUBLISH,ENTRY_PUB_LEVEL,CHILD_COUNT,ENTRY_ID) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatManager#createCat(com.founder.e5.cat.Category)
	 */
	public void createCat(Category cat) throws E5Exception
	{
		//新的ID
		int id = (int)EUID.getID("CategoryID");
		cat.setCatID(id);
		cat.setLastModified(new Date());
		cat.setDisplayOrder(id);
		
		//调用DBSession
		DBSession db = Context.getDBSession();
		try
		{
			db.beginTransaction();
			
			//1)设置catLevel = parentLevel + 1
			if(cat.getParentID() == 0)
				cat.setCatLevel(0);
			else
			{
				Category parent = this.getCat(cat.getCatType(),cat.getParentID(),null,db);
				cat.setCatLevel(parent.getCatLevel()+1);
			}
			
			//2)save
			String sql = getSQL(cat, m_InsertSQL);
			db.executeUpdate(sql, getValueArray(cat), getFieldTypes());
			
			//3)设置级联名称			
			upateCascade(cat,db);
			
			//4)如果有父节点更新childcount+1
			if(cat.getParentID()>0)
			{
				updateChildCount(cat.getCatType(),cat.getParentID(),1,db);
			}
			
			db.commitTransaction();
		}
		catch (Exception e)
		{
			try{db.rollbackTransaction();}catch(Exception ex){}
			throw new E5Exception("[CatCreate]", e);
		}
		finally
		{
			try{db.close();}catch(Exception e1){e1.printStackTrace();}
		}
	}
	
	private static String UPDATE_CHILDCOUNT = "UPDATE CATEGORY_OTHER set CHILD_COUNT=CHILD_COUNT+? Where ENTRY_DELETE_FLAG=0 AND ENTRY_ID=?";
	
	/**
	 * 更新分类节点孩子节点数量
	 * @param catType
	 * @param parentID
	 * @param value
	 * @param db
	 * @throws SQLException
	 * @throws E5Exception
	 */
	private void updateChildCount(int catType,int catID,int value,DBSession db)
		throws SQLException,E5Exception
	{		
		db.executeUpdate(getSQL(catType,UPDATE_CHILDCOUNT),new Object[]{new Integer(value),new Integer(catID)});
	}
	
	private static String SELECT_CHILD_COUNT = "select count(*) from CATEGORY_OTHER where PARENT_ID=? and ENTRY_DELETE_FLAG=0";
	private static String CALCULATE_CHILD_COUNT = "update CATEGORY_OTHER set CHILD_COUNT=? Where ENTRY_ID=?";
	
	//重新计算分类的child_count字段
	private void calculateChildCount(int catType,int catID,DBSession db)
	throws SQLException,E5Exception
	{	
		/**
		 * 不能使用
		 * UPDATE CATEGORY_FL set CHILD_COUNT=
		 * 		( select count(*) from CATEGORY_FL where parent_id=3 and ENTRY_DELETE_FLAG=0)
		 * 		Where ENTRY_ID=3
		 * 的格式，在MYSQL下报错
		 * java.sql.SQLException: You can't specify target table 'CATEGORY_FL' for update in FROM clause
		 * 可能在SQL SERVER下也不行
		 * 
		 * 改用2句SQL完成
		 */
		int count = 0;
		IResultSet rs = db.executeQuery(getSQL(catType,SELECT_CHILD_COUNT), new Object[]{new Integer(catID)});
		if (rs.next()) 
			count = rs.getInt(1);
		rs.close();
		
		db.executeUpdate(getSQL(catType,CALCULATE_CHILD_COUNT), new Object[]{new Integer(count), new Integer(catID)});
	}
	
	private static String m_UpdateSQL = "update CATEGORY_OTHER set WT_TYPE=?,PARENT_ID=?, ENTRY_CODE=?, ENTRY_NAME=?, ENTRY_LEVEL=?, ENTRY_DISP_ORDER=?, ENTRY_MEMO=?, LAST_MODI_MAN=?, LAST_MODI_DATE=?, IS_REFRENCE=?, REFRENCE_TYPE=?, REFRENCE_TABLE=?, REFRENCE_ID=?, ENTRY_LINKTYPE=?, ENTRY_LINKTABLE=?, ENTRY_LINKID=?, ENTRY_SAME=?, ENTRY_DELETE_FLAG=?, ENTRY_PUBLISH=?, ENTRY_PUB_LEVEL=?,CHILD_COUNT=? where ENTRY_ID=?";
	//private static String m_UpdateLevelSQL = "update CATEGORY_OTHER set ENTRY_LEVEL=(ENTRY_LEVEL@LEVEL@) where PARENT_ID in (@PARENTID@)";
	
	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatManager#update(com.founder.e5.cat.Category)
	 */
	public void updateCat(Category cat) throws E5Exception
	{
		//1.取出旧的分类，待比较
		Category oldCat = getCat(cat.getCatType(), cat.getCatID(), null);		
	
		//调用DBSession
		DBSession db = Context.getDBSession();
		try
		{
			db.beginTransaction();
			CatType catType = getType(cat.getCatType());
			//2.修改本分类
			//TODO:用oldCat进行保存，保存前先把可改变的值赋上。注意不能改变catLevel
			db.executeUpdate(getSQL(catType, m_UpdateSQL), getValueArray(cat), getFieldTypes());
			
			//3.检查名称是否发生变化，若有变化则需要修改所有子分类
			if (!cat.getCatName().equals(oldCat.getCatName()))
				updateChildCascadeName(cat, db);
			
			//5.事务提交
			db.commitTransaction();
		}
		catch (SQLException e)
		{
			try{db.rollbackTransaction();}catch(Exception e1){e1.printStackTrace();}
			throw new E5Exception("[CatCreate]", e);
		}
		finally
		{
			try{db.close();}catch(Exception e1){e1.printStackTrace();}
		}
	}
	
	/**
	 * 移动节点时，修改级联名称(自己和所有下级分类)
	 * @author wanghc
	 * @param catType 分类类型
	 * @param catID   当前分类ID
	 * @param destCatID 目的分类 =0 根，>1分类ID
	 * 父分类的名称或者层次发生了变化，则需要变化子分类的级联名称
	 * 
	 */
	private void updateChildCascade(Category cat, int destCatID,int dlevel,DBSession db)
		throws E5Exception
	{	
		int catType = cat.getCatType();
		int catID   = cat.getCatID();
		
		//1) 设置当前分类的级联名称、级联ID
		//再去分类时在mssqlserver上有死锁
		//Category cat = getCat(catType,catID);
		cat.setParentID(destCatID);
		
		String   oname= cat.getCascadeName();  //当前分类历史级联名称
		String   oid  = cat.getCascadeID();    //当前分类历史级联ID
		
		upateCascade(cat,db);
		
		String   name = cat.getCascadeName();   //当前分类的级联名称
		String   id   = cat.getCascadeID();   //当前分类的级联ID
		
		//2) 取得所有子分类，设置他们的名称和ID
		Category[] subs = getAllChildrenCatsByCascadeID(catType,oid,true,db);

		if(subs!=null && subs.length>0)
		{
			int onlen = oname.length();
			int oilen = oid.length();
			
			int[]    catIDs      = new int[subs.length];
			String[] cascadeID   = new String[subs.length];
			String[] cascadeName = new String[subs.length];
			
			for(int i=0;i<subs.length;i++)
			{
				catIDs[i]      = subs[i].getCatID();
				cascadeID[i]   = id+subs[i].getCascadeID().substring(oilen,subs[i].getCascadeID().length());
				cascadeName[i] = name+subs[i].getCascadeName().substring(onlen,subs[i].getCascadeName().length());			
			}
			
		    //3) 构造成数组 批量更新		
			updateCascade(catType,catIDs,cascadeName,cascadeID,dlevel,db);			
			
		}
		//4) 调用更新别名(在无子的情况下更新本身)
		CatExtCascadeHelper.updateExtChildCascade(catType,catID,destCatID,db.getConnection());
	}
	
	private static String GET_ALL_CHILDREN_BY_CASCADEID = "select * from CATEGORY_OTHER where WT_TYPE=? and ENTRY_CASCADE_ID like ? order by ENTRY_LEVEL, ENTRY_DISP_ORDER ";
	private static String GET_ALL_NORMAL_CHILDREN_BY_CASCADEID = "select * from CATEGORY_OTHER where WT_TYPE=? and ENTRY_CASCADE_ID like ? And ENTRY_DELETE_FLAG=0 order by ENTRY_LEVEL, ENTRY_DISP_ORDER ";
	
	/**
	 * 更据级联ID查找所有子类
	 * @param catType
	 * @param cascadeID 为null表示查询全部节点
	 * @param deleted true/查询包括带删除的节点,false/查询不包括删除的节点
	 * @author wanghc
	 * @return Category[] 所有子孙节点
	 */
	private Category[] getAllChildrenCatsByCascadeID(int catType,String cascadeID,boolean deleted,DBSession db)
		throws E5Exception
	{
		//取出分类类型
		CatType type = getType(catType);		
		
		StringBuffer cascadeCond = new StringBuffer();
		cascadeCond.append(cascadeID).append(Category.separator).append("%");
		
		boolean owner = false;
		if (db == null)
		{
			db = Context.getDBSession();
			owner=true;
		}
		try
		{
			IResultSet rs = null;
			if(deleted)
			{
				rs = db.executeQuery(
						getSQL(catType, GET_ALL_CHILDREN_BY_CASCADEID), 
						new Object[]{new Integer(type.getCatType()),
							cascadeCond.toString()}
						);
			}
			else
			{
				rs = db.executeQuery(
						getSQL(catType, GET_ALL_NORMAL_CHILDREN_BY_CASCADEID), 
						new Object[]{new Integer(type.getCatType()),
							cascadeCond.toString()}
						);	
			}
			List list = new ArrayList();
			while (rs.next())
				list.add(readRS(rs));
			rs.close();
			if (list.size() == 0) return null;
			
			return (Category[])list.toArray(new Category[0]);
		}
		catch (SQLException e)
		{
			throw new E5Exception(e);
		}
		finally
		{
			if(owner)
				try{db.close();}catch(Exception e){}
		}
	}
	/**
	 * 修改分类名称时，修改所有子节点级联名称
	 * @param cat
	 * @param db
	 * @author wanghc
	 * @throws E5Exception
	 */
	private void updateChildCascadeName(Category cat,DBSession db)
	throws E5Exception
	{	
		//当前分类历史级联名称
		String   oname    = cat.getCascadeName();  
		String ocascadeID = cat.getCascadeID();
		
		upateCascade(cat,db);
		
		//2) 取得所有子分类，设置他们的名称和ID
		Category[] subs = getAllChildrenCatsByCascadeID(cat.getCatType(),ocascadeID,true,db);
	
		if(subs!=null && subs.length>0)
		{
			int onlen = oname.length();
		
			int[]    catIDs      = new int[subs.length];
			String[] cascadeName = new String[subs.length];
			
			for(int i=0;i<subs.length;i++)
			{
				catIDs[i]      = subs[i].getCatID();
				cascadeName[i] = cat.getCascadeName()+subs[i].getCascadeName().substring(onlen,subs[i].getCascadeName().length());			
			}
			
		    //3) 构造成数组 批量更新		
			updateCascade(cat.getCatType(),catIDs,cascadeName,null,0,db);			
		}
	
	}
	
	/**
	 * 更新一个分类的级联名称
	 * @param cat
	 * @throws E5Exception
	 */
	private void upateCascade(Category cat,DBSession db) throws E5Exception
	{
		//1) 设置当前分类的级联名称、级联ID
		String   name = "";   //当前分类的级联名称
		String   id   = "";   //当前分类的级联ID
		
		
		//根
		if(cat.getParentID()<= 0)
		{
			id   = cat.getCatID() + "";
			name = cat.getCatName();
		}
		else
		{
			Category parent = getCat(cat.getCatType(),cat.getParentID(),null,db);
			id   = parent.getCascadeID() + Category.separator + cat.getCatID();
			name = parent.getCascadeName() + Category.separator + cat.getCatName();
		}		
		cat.setCascadeID(id);
		cat.setCascadeName(name);
		
		//更新当前类,不更新level，level由move方法更新过了,level=0表示不更新
		updateCascade(cat.getCatType(),new int[]{cat.getCatID()},new String[]{name},new String[]{id},0,db);
	}
	
	private static String UPDATE_CASCADE_ALL_SQL  = "UPDATE CATEGORY_OTHER set ENTRY_CASCADE_NAME=?,ENTRY_CASCADE_ID=?,ENTRY_LEVEL=ENTRY_LEVEL+? WHERE ENTRY_ID=?";
	private static String UPDATE_CASCADE_NAME_SQL = "UPDATE CATEGORY_OTHER set ENTRY_CASCADE_NAME=? WHERE ENTRY_ID=?";
	
	/**
	 * 批量更新分类的级联名称、及联ID,数组中为设置好的对应关系及其取值。 
	 * @param catID
	 * @param cascadeName
	 * @param cascadeID
	 * @param level - 层差
	 */
	private void updateCascade(int catType,int catID[],String cascadeName[],String cascadeID[],int level,DBSession db)
		throws E5Exception
	{
		Connection conn  = db.getConnection();
		PreparedStatement pstmt = null;
		try
		{
			
			//支持只更新cascadeName
			if(cascadeID == null)
				pstmt = conn.prepareStatement(getSQL(catType,UPDATE_CASCADE_NAME_SQL));
			else
				pstmt = conn.prepareStatement(getSQL(catType,UPDATE_CASCADE_ALL_SQL));
			
			for(int i=0;i<cascadeName.length;i++)
			{
				pstmt.setString(1,cascadeName[i]);
				if(cascadeID == null)
					pstmt.setInt(2,catID[i]);
				else
				{
					pstmt.setString(2,cascadeID[i]);
					pstmt.setInt(3,level);
					pstmt.setInt(4,catID[i]);	
				}
				
				pstmt.addBatch();
			}
			
			pstmt.executeBatch();
		}
		catch(Exception e)
		{
			throw new E5Exception("[Update Cascade]"+e.getMessage());
		}
		finally
		{
			try{pstmt.close();}catch(Exception e){};
			pstmt=null;
		}
	}
	/**
	 * 修改所有子分类的层次，用程序的方式递归调用修改
	 * 注意该方法中对每一层分类只做一次查找，而不是每个分类查一次子分类，
	 * 当层次比较深时，传递的int[]可能比较大
	 */
//	private void updateChildrenLevel(CatType catType, String strSQL, int[] catID, 
//			DBSession db) 
//	throws SQLException, E5Exception
//	{
//		//修改当前层次
//		String sql = strSQL.replaceAll("@PARENTID@", CatHelper.join(catID, ','));
//		db.executeUpdate(sql, null);	
//		
//		//找所有子类,若没有子类了，则返回
//		Category[] catArr = getBareSubCats(catType, catID, db,false);
//		
//		if (catArr == null) return;
//
//		//把子类ID抽取出来
//		int[] childrenID = new int[catArr.length];
//		for (int i = 0; i < catArr.length; i++)
//			childrenID[i] = catArr[i].getCatID();
//			
//		//递归：修改所有子类
//		updateChildrenLevel(catType, strSQL, childrenID, db);
//	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatManager#updateSubCats(com.founder.e5.cat.Category, java.lang.String[])
	 */
	public void updateTransfer(Category cat, int[] fields) throws E5Exception
	{
		int size = fields.length;
		Object[] params = new Object[size];
		int[] types = new int[size];

		//取分类类型
		CatType catType = getType(cat.getCatType());

		//准备sql语句，以及参数，和参数类型
		String sql = prepaire(catType, cat, fields, params, types);
		
		DBSession db = Context.getDBSession();
		try
		{
			db.beginTransaction();
			updateTransfer(catType, sql, new int[]{cat.getCatID()}, params, types, db);
			db.commitTransaction();
		}
		catch (SQLException e)
		{
			try{db.rollbackTransaction();}catch(Exception e1){e1.printStackTrace();}
			throw new E5Exception("[CatUpdateTransfer]", e);
		}
		finally
		{
			try{db.close();}catch(Exception e1){e1.printStackTrace();}
		}
	}
	
	/**
	 * 准备传递修改的SQL语句，以及SQL语句中的参数和参数类型
	 * @param catType
	 * @param cat
	 * @param fields 待修改传递的属性标识
	 * @param params SQL语句中的参数
	 * @param types SQL语句中的参数类型
	 * @return
	 */
	private String prepaire(CatType catType, Category cat, int[] fields, 
			Object[] params, int[] types)
	{
		String strTableName = catType.getTableName();
		if (strTableName == null)
			strTableName = Category.DEFAULT_TABLENAME;
		//拼修改语句
		StringBuffer sb = new StringBuffer(500);
		sb.append("update ").append(strTableName).append(" set ");

		int size = fields.length;
		for (int i = 0; i < size; i++)
		{
			switch (fields[i])
			{
				case Category.TRANS_CATCODE:
					sb.append(Category.FIELD_CATCODE).append("=?,");
					params[i] = cat.getCatCode();
					types[i] = java.sql.Types.VARCHAR;
					break;
				case Category.TRANS_LINKID:
					sb.append(Category.FIELD_LINKID).append("=?,");
					params[i] = new Integer(cat.getLinkID());
					types[i] = java.sql.Types.INTEGER;
					break;
				case Category.TRANS_LINKTABLE:
					sb.append(Category.FIELD_LINKTABLE).append("=?,");
					params[i] = cat.getLinkTable();
					types[i] = java.sql.Types.VARCHAR;
					break;
				case Category.TRANS_LINKTYPE:
					sb.append(Category.FIELD_LINKTYPE).append("=?,");
					params[i] = new Integer(cat.getLinkType());
					types[i] = java.sql.Types.INTEGER;
					break;
				case Category.TRANS_PUBLEVEL:
					sb.append(Category.FIELD_PUBLEVEL).append("=?,");
					params[i] = new Integer(cat.getPubLevel());
					types[i] = java.sql.Types.INTEGER;
					break;
				case Category.TRANS_PUBLISH:
					sb.append(Category.FIELD_PUBLISH).append("=?,");
					params[i] = (cat.isPublished()?new Integer(1):new Integer(0));
					types[i] = java.sql.Types.INTEGER;
					break;
				default:
					break;
			}
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" where PARENT_ID in (@PARENTID@)");
		
		return sb.toString();
	}
 	/**
	 * 修改所有子分类的层次，用程序的方式递归调用修改
	 * 注意该方法中对每一层分类只做一次查找，而不是每个分类查一次子分类，
	 * 当层次比较深时，传递的int[]可能比较大
	 */
	private void updateTransfer(CatType catType, String strSQL, int[] catID,
			Object[] params, int[] types, DBSession db) 
	throws SQLException, E5Exception
	{
		//修改当前层次
		String sql = strSQL.replaceAll("@PARENTID@", CatHelper.join(catID, ','));
		db.executeUpdate(sql, params, types);
		
		//找所有子类,若没有子类了，则返回
		Category[] catArr = getBareSubCats(catType, catID, db,false);
		
		if (catArr == null) return;

		//把子类ID抽取出来
		int[] childrenID = new int[catArr.length];
		for (int i = 0; i < catArr.length; i++)
			childrenID[i] = catArr[i].getCatID();
		
		//递归：修改所有子类
		updateTransfer(catType, strSQL, childrenID, params, types, db);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatManager#delete(java.lang.String, int)
	 */
	public void deleteCat(String catTypeName, int catID) throws E5Exception
	{
		deleteCat(getType(catTypeName).getCatType(), catID);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatManager#deleteCat(int, int)
	 */
	public void deleteCat(int catType, int catID) throws E5Exception
	{
		catDelete(catType, catID, 1);
	}
	
	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatManager#restoreCat(int, int)
	 */
	public void restoreCat(int catType, int catID) throws E5Exception 
	{
		catDelete(catType, catID, 0);
	}

	//private static String m_DeleteSQL = "delete from CATEGORY_OTHER where ENTRY_ID in $ENTRY_ID$";
	private static String m_DeleteSQL = "update CATEGORY_OTHER set ENTRY_DELETE_FLAG=? where ENTRY_CASCADE_ID like ?";
	private static String m_DeleteSelfSQL = "update CATEGORY_OTHER set ENTRY_DELETE_FLAG=? where ENTRY_ID=?";
	/**
	 * 执行分类的删除和恢复动作
	 * 因为分类在删除和恢复时都需要同时修改子分类，所以比较复杂。
	 * @param catType
	 * @param catID
	 * @param delete
	 * @throws E5Exception
	 */
	private void catDelete(int catType, int catID, int delete) throws E5Exception
	{
		//调用DBSession
		DBSession db = Context.getDBSession();
		try
		{
			CatType type = getType(catType);

			//1.取得分类信息，主要是级联ID有用
			Category cat = getBareALLCat(catType,catID,db);	
			//操作前取要操作的列表
			Category[] cats = null;
			if(delete != 1)
				cats = getAllChildrenCatsByCascadeID(catType,cat.getCascadeID(),true,db);
			
			//2.根据级联ID，拼出修改子分类的DELETEFLAG的SQL			
			String strSQL = getSQL(type, m_DeleteSQL);		
			StringBuffer cascadeCond = new StringBuffer();
			cascadeCond = cascadeCond.append(cat.getCascadeID()).append(Category.separator).append("%");

			db.beginTransaction();//事务

			//删除(或恢复)子分类:根据级联ID			
			db.executeUpdate(strSQL, new Object[]{new Integer(delete), cascadeCond.toString() });
			//删除（或恢复）本身分类
			strSQL = getSQL(type, m_DeleteSelfSQL);		
			db.executeUpdate(strSQL, new Object[]{new Integer(delete), new Integer(catID)});
			
			//3.维护child_count字段
			//计算父
			if(cat.getParentID()!=0)
				calculateChildCount(catType, cat.getParentID(), db);

			//计算自己
			calculateChildCount(catType, catID, db);
			//如果是恢复操作，计算子孙
			for (int i = 0; cats != null && i < cats.length; i++)
				calculateChildCount(catType, cats[i].getCatID(), db);							
			
			db.commitTransaction();
		}
		catch (Exception e)
		{
			try{db.rollbackTransaction();} catch(Exception e1) {e1.printStackTrace();}
			throw new E5Exception("[CatDelete]", e);
		}
		finally
		{
			try{db.close();}catch(Exception e1){e1.printStackTrace();}
		}
	}


	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatReader#getCat(java.lang.String, int, java.lang.String)
	 */
	public Category getCat(String catTypeName, int catID, String extType)
	throws E5Exception
	{
		CatType type = getType(catTypeName);
		return getCat(type.getCatType(), catID, extType);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatReader#getCat(int, int, java.lang.String)
	 */
	public Category getCat(int catType, int catID, String extType)
	throws E5Exception
	{
		DBSession db = Context.getDBSession();
		try
		{
			return getCat(catType,catID,extType,db);
		}
		finally
		{
			try{db.close();}catch(Exception e1){e1.printStackTrace();}
		}
	}
	
	private Category getCat(int catType, int catID, String extType,DBSession db)
	throws E5Exception
	{
		//主属性
		Category cat = getBareCat(catType, catID, db);
		if ((cat == null) || (extType == null)) return cat;

		//扩展属性
		CatExt ext = CatHelper.getExtManager().getExt(catType, catID, extType);
		if (ext == null) return cat;

		cat.setCascadeName(ext.getCascadeName());
		cat.setCatName(ext.getExtName());
		return cat;
	}
	
	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatReader#getCats(java.lang.String, java.lang.String)
	 */
	public Category[] getCats(String catTypeName, String extType)
	throws E5Exception
	{
		CatType type = getType(catTypeName);
		return getCats(type.getCatType(), extType);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatReader#getCats(int, java.lang.String)
	 */
	public Category[] getCats(int catType, String extType)
	throws E5Exception
	{
		DBSession db = Context.getDBSession();
		Category[] catArray = null;
		CatExt[] extArray = null;
		try
		{
			//得到分类的主属性
			catArray = getBareCats(catType, db);
			if ((catArray == null) || (extType == null)) return catArray;

			//得到分类的扩展属性
			extArray = CatHelper.getExtManager().getAllExts(catType, extType);
			if (extArray == null) return catArray;
		}
		finally
		{
			try{db.close();}catch(Exception e1){e1.printStackTrace();}
		}

		//找每个分类的扩展属性，用扩展属性修改分类名和级联名			
		for (int i = 0; i < catArray.length; i++)
		{
			CatExt ext = getExt(catArray[i].getCatID(), extArray);
			if (ext != null)
			{
				catArray[i].setCascadeName(ext.getCascadeName());
				catArray[i].setCatName(ext.getExtName());
			}
		}
		return catArray;
	}

	/**
	 * 从扩展属性数组中找一个分类的扩展属性
	 */
	private CatExt getExt(int catID, CatExt[] extArray)
	{
		return getExt(catID, extArray, 0, extArray.length - 1);
	}

	/**
	 * 二分法定位一个分类的扩展属性
	 */
	private CatExt getExt(int catID, CatExt[] extArray, int from, int to)
	{
		if (from > to) return null;
		
		int pos = (from + to) / 2;
		if (extArray[pos].getCatID() == catID)
			return extArray[pos];
		if (from == to)
			return null;
		
		if (extArray[pos].getCatID() < catID)
			return getExt(catID, extArray, pos + 1, to);
		else
			return getExt(catID, extArray, from, pos - 1);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatReader#getSubCats(java.lang.String, int, java.lang.String)
	 */
	public Category[] getSubCats(String catTypeName, int catID, String extType)
	throws E5Exception
	{
		return getSubCats(getType(catTypeName).getCatType(), catID, extType);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatReader#getSubCats(int, int, java.lang.String)
	 */
	public Category[] getSubCats(int catType, int catID, String extType)
	throws E5Exception
	{
		DBSession db = Context.getDBSession();
		Category[] catArray = null;
		CatExt[] extArray = null;
		try
		{
			//得到分类的主属性
			catArray = getBareSubCats(catType, catID, db);
			if ((catArray == null) || (extType == null)) return catArray;

			//得到分类的扩展属性
			extArray = CatHelper.getExtManager().getSubExt(catType, catID, extType);
			if (extArray == null) return catArray;
		}
		finally
		{
			try{db.close();}catch(Exception e1){e1.printStackTrace();}
		}

		//找每个分类的扩展属性，用扩展属性修改分类名和级联名			
		for (int i = 0; i < catArray.length; i++)
		{
			CatExt ext = getExt(catArray[i].getCatID(), extArray);
			if (ext != null)
			{
				catArray[i].setCascadeName(ext.getCascadeName());
				catArray[i].setCatName(ext.getExtName());
			}
		}
		return catArray;
	}

	private Object[] getValueArray(Category cat)
	{
		Object[] objArr = new Object[]{
				new Integer(cat.getCatType()),
				new Integer(cat.getParentID()),
				cat.getCatCode(),
				cat.getCatName(),
				new Integer(cat.getCatLevel()),
				new Integer(cat.getDisplayOrder()),
				cat.getMemo(),
				cat.getUserName(),
				//注意日期类型的转换
				new java.sql.Timestamp(cat.getLastModified().getTime()),
				new Integer(cat.isRef()?1:0),
				new Integer(cat.getRefType()),
				cat.getRefTable(),
				new Integer(cat.getRefID()),
				new Integer(cat.getLinkType()),
				cat.getLinkTable(),
				new Integer(cat.getLinkID()),
				new Integer(cat.getSameGroup()),
				new Integer(cat.isDeleted()?1:0),
				new Integer(cat.isPublished()?1:0),
				new Integer(cat.getPubLevel()),
				new Integer(cat.getChildCount()),
				new Integer(cat.getCatID()),
		};
		return objArr;
		
	}

	private static int[] m_TypeArr = new int[]{
			Types.INTEGER,/*getCatType*/
			Types.INTEGER,/*getParentID*/
			Types.VARCHAR,/*getCatCode*/
			Types.VARCHAR,/*getCatName*/
			Types.INTEGER,/*getCatLevel*/
			Types.INTEGER,/*getDisplayOrder*/
			Types.VARCHAR,/*getMemo*/
			Types.VARCHAR,/*getUserName*/
			Types.TIMESTAMP,/*getLastModified*/
			Types.INTEGER,/*getIsRef*/
			Types.INTEGER,/*getRefType*/
			Types.VARCHAR,/*getRefTable*/
			Types.INTEGER,/*getRefID*/
			Types.INTEGER,/*getLinkType*/
			Types.VARCHAR,/*getLinkTable*/
			Types.INTEGER,/*getLinkID*/
			Types.INTEGER,/*getIsSame*/
			Types.INTEGER,/*getIsDeleted*/
			Types.INTEGER,/*getIsPublished*/
			Types.INTEGER,/*getPubLevel*/
			Types.INTEGER,/*getChildCount*/
			Types.INTEGER,/*getCatID*/
		};

	private int[] getFieldTypes()
	{
		return m_TypeArr;
	}
	
	/**
	 * 根据分类得到数据库语句
	 */
	private String getSQL(Category cat, String templateSQL) throws E5Exception
	{
		return getSQL(cat.getCatType(), templateSQL);
	}
	
	/**
	 * 根据分类类型ID得到数据库语句
	 */
	private String getSQL(int catType, String templateSQL) throws E5Exception
	{
		return getSQL(getType(catType), templateSQL);
	}

	/**
	 * 根据分类类型得到数据库语句
	 */
	private String getSQL(CatType catType, String templateSQL) throws E5Exception
	{
		if (catType == null)
			throw new E5Exception("No Category Type!");
		return getSQL(catType.getTableName(), templateSQL);
	}

	/**
	 * 根据分类类型对应的表名得到数据库语句
	 */
	private String getSQL(String catTable, String templateSQL) throws E5Exception
	{
		String sql;
		if (catTable == null || "".equals(catTable))
			sql = templateSQL;
		else
			sql = templateSQL.replaceAll(Category.DEFAULT_TABLENAME, catTable);
		return sql;		
	}
	private static String m_SelectSQL = "select * from CATEGORY_OTHER where ENTRY_ID=? and ENTRY_DELETE_FLAG=0";
	//private static String m_SelectSQL = "select * from CATEGORY_OTHER where ENTRY_ID=?";
	
	/**
	 * 取一个分类的主属性
	 * @param catType
	 * @param catID
	 * @param db
	 * @return
	 * @throws E5Exception
	 */
	private Category getBareCat(int catType, int catID, DBSession db)
	throws E5Exception
	{
		try
		{
			IResultSet rs = db.executeQuery(getSQL(catType, m_SelectSQL), new Object[]{new Integer(catID)});
			Category cat = null;
			if (rs.next())
				cat = readRS(rs);
			rs.close();
			return cat;
		}
		catch (SQLException e)
		{
			throw new E5Exception(e);
		}
	}
	private static String m_SelectSQL2 = "select * from CATEGORY_OTHER where ENTRY_ID=?";
	
	/**
	 * 取一个分类的主属性(删除的未删除的)
	 * @param catType
	 * @param catID
	 * @param db
	 * @return
	 * @throws E5Exception
	 */
	private Category getBareALLCat(int catType, int catID, DBSession db)
	throws E5Exception
	{
		try
		{
			IResultSet rs = db.executeQuery(getSQL(catType, m_SelectSQL2), new Object[]{new Integer(catID)});
			Category cat = null;
			if (rs.next())
				cat = readRS(rs);
			rs.close();
			return cat;
		}
		catch (SQLException e)
		{
			throw new E5Exception(e);
		}
	}	
	private Category readRS(IResultSet rs)
	throws E5Exception
	{
		try
		{
			Category cat =new Category();
			cat.setCatType(rs.getInt("WT_TYPE"));
			cat.setCatID(rs.getInt("ENTRY_ID"));
			cat.setParentID(rs.getInt("PARENT_ID"));
			cat.setCatCode(rs.getString("ENTRY_CODE"));
			cat.setCatName(rs.getString("ENTRY_NAME"));
			cat.setCascadeName(rs.getString("ENTRY_CASCADE_NAME"));
			cat.setCascadeID(rs.getString("ENTRY_CASCADE_ID"));
			cat.setCatLevel(rs.getInt("ENTRY_LEVEL"));
			cat.setDisplayOrder(rs.getInt("ENTRY_DISP_ORDER"));
			cat.setMemo(rs.getString("ENTRY_MEMO"));

			cat.setUserName(rs.getString("LAST_MODI_MAN"));
			cat.setLastModified(rs.getTimestamp("LAST_MODI_DATE"));
			
			cat.setRef(rs.getInt("IS_REFRENCE")==1?true:false);
			cat.setRefType(rs.getInt("REFRENCE_TYPE"));
			cat.setRefTable(rs.getString("REFRENCE_TABLE"));
			cat.setRefID(rs.getInt("REFRENCE_ID"));

			cat.setLinkType(rs.getInt("ENTRY_LINKTYPE"));
			cat.setLinkTable(rs.getString("ENTRY_LINKTABLE"));
			cat.setLinkID(rs.getInt("ENTRY_LINKID"));

			cat.setSameGroup(rs.getInt("ENTRY_SAME"));
			cat.setDeleted(rs.getInt("ENTRY_DELETE_FLAG")==1?true:false);
			cat.setPublished(rs.getInt("ENTRY_PUBLISH")==1?true:false);
			cat.setPubLevel(rs.getInt("ENTRY_PUB_LEVEL"));
			cat.setChildCount(rs.getInt("CHILD_COUNT"));
			return cat;
		}
		catch (SQLException e)
		{
			throw new E5Exception(e);
		}
	}

	private static String m_SelectAllSQL = 
		"select * from CATEGORY_OTHER where WT_TYPE=? and ENTRY_DELETE_FLAG=0 order by ENTRY_LEVEL, ENTRY_DISP_ORDER";
	/**
	 * 取某分类类型的所有分类，只取主属性
	 * @param catType
	 * @param db
	 * @return
	 * @throws E5Exception
	 */
	private Category[] getBareCats(int catType, DBSession db)
	throws E5Exception
	{
		try
		{
			IResultSet rs = db.executeQuery(getSQL(catType, m_SelectAllSQL), 
					new Object[]{new Integer(catType)});
			List list = new ArrayList();
			while (rs.next())
				list.add(readRS(rs));
			rs.close();
			if (list.size() == 0) return null;
			
			return (Category[])list.toArray(new Category[0]);
		}
		catch (SQLException e)
		{
			throw new E5Exception(e);
		}
	}

	private static String m_SelectSubSQL = 
		"select * from CATEGORY_OTHER where PARENT_ID=? and WT_TYPE=? and ENTRY_DELETE_FLAG=0 order by ENTRY_DISP_ORDER";
	/**
	 * 取某分类的直接子分类
	 * @param catType
	 * @param catID
	 * @param db
	 * @return
	 * @throws E5Exception
	 */
	private Category[] getBareSubCats(int catType, int catID, DBSession db)
	throws E5Exception
	{
		try
		{
			IResultSet rs = db.executeQuery(getSQL(catType, m_SelectSubSQL), 
					new Object[]{new Integer(catID), new Integer(catType)});
			List list = new ArrayList();
			while (rs.next())
				list.add(readRS(rs));
			rs.close();
			if (list.size() == 0) return null;
			
			return (Category[])list.toArray(new Category[0]);
		}
		catch (SQLException e)
		{
			throw new E5Exception(e);
		}
	}
	private static String m_SelectMultiSubSQL = 
		"select * from CATEGORY_OTHER where WT_TYPE=? and PARENT_ID in (@PARENTID@) and ENTRY_DELETE_FLAG=0 order by ENTRY_LEVEL, ENTRY_DISP_ORDER";
	private static String m_SelectALLMultiSubSQL = 
		"select * from CATEGORY_OTHER where WT_TYPE=? and PARENT_ID in (@PARENTID@) order by ENTRY_LEVEL, ENTRY_DISP_ORDER";	
	/**
	 * 把多个父分类的直接子分类一次性取出
	 * 用于分类的向下传递修改
	 * @param catType
	 * @param catID
	 * @param db
	 * @return
	 * @throws E5Exception
	 */
	private Category[] getBareSubCats(CatType catType, int[] catID, DBSession db,boolean deleted)
	throws E5Exception
	{
		try
		{
			IResultSet rs = null;
			//不显示删除的
			if(!deleted)
			{
				rs = db.executeQuery(
						getSQL(catType, m_SelectMultiSubSQL)
						.replaceAll("@PARENTID@", CatHelper.join(catID, ',')), 
						new Object[]{new Integer(catType.getCatType())}
						);
			}
			//显示全部的
			else				
			{
				rs = db.executeQuery(
						getSQL(catType, m_SelectALLMultiSubSQL)
						.replaceAll("@PARENTID@", CatHelper.join(catID, ',')), 
						new Object[]{new Integer(catType.getCatType())}
						);
			}
			List list = new ArrayList();
			while (rs.next())
				list.add(readRS(rs));
			rs.close();
			if (list.size() == 0) return null;
			
			return (Category[])list.toArray(new Category[0]);
		}
		catch (SQLException e)
		{
			throw new E5Exception(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatManager#createType(com.founder.e5.cat.CatType)
	 */
	public void createType(CatType catType) throws E5Exception
	{
		int nDSID;
		try
		{
			nDSID = (int)EUID.getID("CatTypeID");
		}
		catch (Exception e1)
		{
			throw new E5Exception("[CatTypeCreate]GetID", e1);
		}
		create(catType, nDSID);
	}

	/**
	 * 给出ID，创建分类类型
	 */
	void create(CatType catType, int id) 
	throws E5Exception
	{
		try
		{
			BaseDAO dao = new BaseDAO();
			catType.setCatType(id);
			//分类类型的顺序，创建时使与ID相同
			//因为这个ID一定是已有的分类类型中最大的，所以可以保证新建的类型排序最大
			catType.setOrder(id);	
			dao.save(catType);
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[CatTypeCreate]", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatManager#update(com.founder.e5.cat.CatType)
	 */
	public void updateType(CatType catType) throws E5Exception
	{
		try
		{
			BaseDAO dao = new BaseDAO();
			dao.update(catType);
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[CatTypeUpdate]", e);
		}
	}

	private static String m_DeleteTypeSQL = "delete from CATEGORY_OTHER where WT_TYPE=?";
	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatManager#delete(int)
	 */
	public void deleteType(int catType) throws E5Exception
	{
		CatType type = getType(catType);
		if (type == null) return;
		
		DAOHelper.delete("delete from CatType as cattype where cattype.catType=:ct",
				new Integer(catType),
				Hibernate.INTEGER);
		DBSession db = Context.getDBSession();
		try
		{
			db.beginTransaction();
			
			db.executeUpdate(getSQL(type, m_DeleteTypeSQL), new Object[]{new Integer(type.getCatType())});
			db.commitTransaction();
		}
		catch (SQLException e)
		{
			try{db.rollbackTransaction();} catch(Exception e1) {e1.printStackTrace();}
			throw new E5Exception("[CatDelete]", e);
		}
		finally
		{
			try{db.close();}catch(Exception e1){e1.printStackTrace();}
		}
		
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatManager#getCatTypes()
	 */
	public CatType[] getTypes() throws E5Exception
	{
		BaseDAO dao = new BaseDAO();
		try
		{
			Session s = null;
			try
			{
				s = dao.getSession();
				List list = s.createCriteria(CatType.class)
					.addOrder(Order.asc("order"))
					.addOrder(Order.asc("catType"))
					.list();
				if (list.size() == 0) return null;
				return (CatType[])list.toArray(new CatType[0]);
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
	 * @see com.founder.e5.cat.CatTypeReader#get(int)
	 */
	public CatType getType(int id) throws E5Exception
	{
		try
		{
			BaseDAO dao = new BaseDAO();
			return (CatType) dao.get(CatType.class, new Integer(id));
		}
		catch (Exception e)
		{
			throw new E5Exception("[GetCatType]", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatTypeReader#getCatTypeByName(java.lang.String)
	 */
	public CatType getType(String name) throws E5Exception
	{
		List list = DAOHelper.find("from CatType as cattype where cattype.name=:name", name,
				Hibernate.STRING);
		if (list.size() == 0) return null;
		return (CatType) list.get(0);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatReader#getChildrenCats(int, int, int)
	 */
	public Category[] getChildrenCats(int catType, int catID, int extType) 
	throws E5Exception
	{
		//不包含删除的分类
		return getChildrenCats(catType, catID, extType,false);
	}
	
	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatManager#getChildrenCatsIncludeDeleted(int, int, int)
	 */	
	public Category[] getChildrenCatsIncludeDeleted(int catType,int catID,int extType)
	throws E5Exception
	{
		//包含删除的分类
		return getChildrenCats(catType, catID, extType,true);
	}
	
	private Category[] getChildrenCats(int catType, int catID, int extType,boolean deleted) throws E5Exception
	{
		//取出分类类型		
		String cascadeID = null;
		
		if(catID>0)
		{
			Category cat = getCat(catType,catID);
			if(cat == null)
				return null;
			cascadeID = cat.getCascadeID();
		}		
		
		Category cats[] = null;
		DBSession db = Context.getDBSession();
		try
		{
			//getChildrenCats(type, new int[]{catID}, extType, db, list,false);
			
			//1) 取得所有子孙分类(不包括本身)
			cats = getAllChildrenCatsByCascadeID(catType,cascadeID,deleted,db);
			if(cats == null) return cats;
			
			//2) 取得所有别名,匹配到分类name,cascadeName上
			if(extType>0)
			{
				CatExt catExts[] = CatHelper.getExtManager().getAllExts(catType,extType);
				//找每个分类的扩展属性，用扩展属性修改分类名和级联名
				if (catExts != null)
				{
					for (int i = 0; i < cats.length; i++)
					{
						CatExt ext = getExt(cats[i].getCatID(), catExts);
						if (ext != null)
						{
							cats[i].setCascadeName(ext.getCascadeName());
							cats[i].setCatName(ext.getExtName());							
						}
					}
				}				
			}
			
		}
		finally
		{
			try{db.close();}catch(Exception e1){e1.printStackTrace();}
		}
		
		return cats;
	}

//	/**
//	 * 取一个分类的所有子分类时的递归算法
//	 * 取出当前层的子分类，放入统一的List中，然后向下传递
//	 * 一层一层读出子分类，都放在同一个List中，最后返回
//	 * 因为要读取扩展属性，所以增加了一些复杂度
//	 * 
//	 * 注意该方法中对每一层分类只做一次查找，而不是每个分类查一次子分类，
//	 * 当层次比较深时，传递的int[]可能比较大
//	 * @param deleted true/查找全部的分类,false/只查找未删除的分类
//	 */
//	private void getChildrenCats(CatType type, int[] catIDs, int extType, 
//			DBSession db, List childrenList,boolean deleted)
//	throws E5Exception
//	{
//		//1.先找到当前层次的所有子分类
//		Category[] catArray = null;
//		CatExt[] extArray = null;
//
//		//得到所有子分类的主属性
//		catArray = getBareSubCats(type, catIDs, db,deleted);
//		if (catArray == null) return;
//
//		//得到分类的扩展属性 
//		if (extType > 0)
//			extArray = CatHelper.getExtManager().getSubExt(type.getCatType(), catIDs, extType);
//
//		//找每个分类的扩展属性，用扩展属性修改分类名和级联名
//		if (extArray != null)
//		{
//			for (int i = 0; i < catArray.length; i++)
//			{
//				CatExt ext = getExt(catArray[i].getCatID(), extArray);
//				if (ext != null)
//				{
//					catArray[i].setCascadeName(ext.getCascadeName());
//					catArray[i].setCatName(ext.getExtName());
//				}
//			}
//		}
//		
//		//2.把子类ID抽取出来
//		//3.把查到的所有子分类加入到列表中
//		int[] childrenID = new int[catArray.length];
//		for (int i = 0; i < catArray.length; i++)
//		{
//			childrenID[i] = catArray[i].getCatID();
//			childrenList.add(catArray[i]);
//		}
//		//4.递归调用，找子分类的所有子分类
//		getChildrenCats(type, childrenID, extType, db, childrenList,deleted);
//	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatReader#getChildrenCats(int, int, java.lang.String)
	 */
	public Category[] getChildrenCats(int catType, int catID, String extType) throws E5Exception
	{
		int nTypeID = 0;
		if (extType != null)
		{
			CatExtType  type = CatHelper.getExtManager().getExtType(extType);
			if (type != null)
				nTypeID = type.getType();
		}
		return getChildrenCats(catType, catID, nTypeID);
	}
	
	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatManager#move(com.founder.e5.cat.Category, int)
	 */
	public void move(int catTypeID, int srcCatID, int destCatID) throws E5Exception
	{
		Category cat = null;
		int dLevel;
		int srcParentID = 0;
		
		//目标ID为0，表示放到根下
		if (destCatID == 0) 
		{
			cat = getCat(catTypeID, srcCatID, null);
			
			//2.记录层差
			dLevel = 0 - cat.getCatLevel();
			
			//设置移动后的层次、排序、父ID
			cat.setCatLevel(0);//层次为父分类的下一层
			srcParentID = cat.getParentID();
			
		}
		else
		{
			//1.取出两个分类，待比较
			Category parentCat = getCat(catTypeID, destCatID, null);
			cat = getCat(catTypeID, srcCatID, null);
			srcParentID = cat.getParentID();
			
			//2.记录层差
			dLevel = parentCat.getCatLevel() - cat.getCatLevel() + 1;
	
			//设置移动后的层次、排序、父ID
			cat.setCatLevel(parentCat.getCatLevel() + 1);//层次为父分类的下一层
		}
		
		cat.setDisplayOrder(nextDispOrder(cat.getCatType(),destCatID));
		cat.setParentID(destCatID);//设置父分类

		CatType catType = getType(catTypeID);

		//调用DBSession
		DBSession db = Context.getDBSession();
		try
		{
			db.beginTransaction();
			//3.修改本分类
			db.executeUpdate(getSQL(catType, m_UpdateSQL), getValueArray(cat), getFieldTypes());
			
			//4.检查层次是否变化，若有变化则需要修改所有子分类
			//效率较低数据量较大时报错：ORA-01795: 列表中的最大表达式数为 1000
			//在跟新级联时顺便更新了level,wanghc
			
//			if (dLevel != 0)//若是0，表示只是移动到其原来父分类的同级分类
//			{
//				String strSQL = getSQL(catType, m_UpdateLevelSQL);
//				if (dLevel > 0)
//					strSQL = strSQL.replaceAll("@LEVEL@", "+" + dLevel);
//				else
//					strSQL = strSQL.replaceAll("@LEVEL@", "" + dLevel);
//				updateChildrenLevel(catType, strSQL, new int[]{cat.getCatID()}, db);
//			}
			
			//4.检查名称/层次是否发生变化，若有变化则需要修改所有子分类			
			//发生移动便更新级联 wanghc
			updateChildCascade(cat,destCatID, dLevel,db);
			
			//5.移动一个分类时，源父分类childcount-1，目标父分类childcount+1			
			updateChildCount(catTypeID,srcParentID,-1,db);
			updateChildCount(catTypeID,destCatID,1,db);
			
			//6.事务提交
			db.commitTransaction();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			try{db.rollbackTransaction();}catch(Exception e1){e1.printStackTrace();}
			throw new E5Exception("[CatMove]", e);
		}
		finally
		{
			try{db.close();}catch(Exception e1){e1.printStackTrace();}
		}
	}
	
	private static String NEXT_DISPORDEDR = "select max(entry_disp_order) disp_order from CATEGORY_OTHER where parent_id=?"; 
	/**
	 * 取得分类order 
	 * @param catType
	 * @param catID
	 * @return
	 * @throws E5Exception
	 */
	private int nextDispOrder(int catType,int catID) throws E5Exception {
		DBSession db = Context.getDBSession();
		int order = 0;
		IResultSet rs = null;
		try
		{
			 rs = db.executeQuery(getSQL(catType, NEXT_DISPORDEDR), 
					new Object[]{new Integer(catID)}
					);
			
			if(rs.next())
				order = rs.getInt("disp_order") + 1;
		}
		catch (SQLException e)
		{
			throw new E5Exception(e);
		}
		finally
		{
			ResourceMgr.closeQuietly(rs);
			ResourceMgr.closeQuietly(db);
		}
		return order;
	}
	private static String m_SelectDeletedSQL = 
		"select * from CATEGORY_OTHER where WT_TYPE=? and ENTRY_DELETE_FLAG=1 order by ENTRY_LEVEL, ENTRY_DISP_ORDER";
	
	
	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatManager#getAllDeleted(int)
	 */
	public Category[] getAllDeleted(int catType) throws E5Exception {
		DBSession db = Context.getDBSession();
		try
		{
			IResultSet rs = db.executeQuery(
					getSQL(catType, m_SelectDeletedSQL), 
					new Object[]{new Integer(catType)}
					);
			List list = new ArrayList();
			while (rs.next())
				list.add(readRS(rs));
			rs.close();
			if (list.size() == 0) return null;
			
			return (Category[])list.toArray(new Category[0]);
		}
		catch (SQLException e)
		{
			throw new E5Exception(e);
		} finally {
			ResourceMgr.closeQuietly(db);
		}
	}
	public Category getCat(String catTypeName, int catID) 
	throws E5Exception 
	{
		return getCat(catTypeName, catID, null);
	}

	public Category getCat(int catType, int catID) throws E5Exception 
	{
		return getCat(catType, catID, null);
	}

	public Category[] getCats(String catTypeName) throws E5Exception 
	{
		return getCats(catTypeName, null);
	}

	public Category[] getCats(int catType) throws E5Exception {
		return getCats(catType, null);
	}

	public Category[] getChildrenCats(int catType, int catID) 
	throws E5Exception 
	{
		return getChildrenCats(catType, catID, 0);
	}

	public Category[] getSubCats(String catTypeName, int catID) 
	throws E5Exception 
	{
		return getSubCats(catTypeName, catID, null);
	}

	public Category[] getSubCats(int catType, int catID) 
	throws E5Exception 
	{
		return getSubCats(catType, catID, null);
	}
	/**
	 * 查找全部的子分类
	 * @param catType
	 * @param catID
	 * @param extType
	 * @return
	 * @throws E5Exception
	 */
//	private Category[] getAllChildrenCats(int catType, int catID, int extType) 
//	throws E5Exception
//	{
//		//取出分类类型
//		CatType type = getType(catType);
//		DBSession db = Context.getDBSession();
//
//		try
//		{
//			List list = new ArrayList();
//			//getChildrenCats(type, new int[]{catID}, extType, db, list,true);
//			list = getAllChildrenCatsByCascadeID(catType,cat.getCascadeID(),true,db);
//			if (list.size() == 0) return null;
//			return (Category[])list.toArray(new Category[0]);
//		}
//		finally
//		{
//			try{db.close();}catch(Exception e1){e1.printStackTrace();}
//		}
//	}

	private static final String GET_CATEGORY_BY_CATIDS = "select * from CATEGORY_OTHER where WT_TYPE=? and ENTRY_ID in (@ENTRY_ID@) order by ENTRY_LEVEL, ENTRY_DISP_ORDER";
	
	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatReader#getCat(int[])
	 */
	public Category[] getCat(int catType,String catIDs) throws E5Exception
	{
		if(catIDs == null || catIDs.equals(""))
			return null;
		
		DBSession db = Context.getDBSession();
		try
		{
			String temp = GET_CATEGORY_BY_CATIDS.replaceAll("@ENTRY_ID@",catIDs);
			IResultSet rs = db.executeQuery(
					getSQL(catType, temp), 
					new Object[]{new Integer(catType)});
			List list = new ArrayList();
			while (rs.next())
				list.add(readRS(rs));
			rs.close();
			if (list.size() == 0) return null;
			
			return (Category[])list.toArray(new Category[0]);
		}
		catch (SQLException e)
		{
			throw new E5Exception(e);
		} finally {
			ResourceMgr.closeQuietly(db);
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatReader#getCatByName(int, java.lang.String)
	 */
	public Category getCatByName(int catType, String catName) throws E5Exception
	{
		DBSession db = Context.getDBSession();
		Category cat = null;
		try
		{
			IResultSet rs = db.executeQuery(getSQL(catType, m_SelectByNameSQL),
					new Object[]{new Integer(catType), catName});
			if (rs.next())
				cat = readRS(rs);
			rs.close();

			return cat;
		}
		catch (SQLException e)
		{
			throw new E5Exception(e);
		}
		finally
		{
			db.closeQuietly();
		}
	}

}