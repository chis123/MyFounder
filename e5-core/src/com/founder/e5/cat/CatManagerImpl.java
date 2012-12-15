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
 * �����������ʵ����
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
		//�µ�ID
		int id = (int)EUID.getID("CategoryID");
		cat.setCatID(id);
		cat.setLastModified(new Date());
		cat.setDisplayOrder(id);
		
		//����DBSession
		DBSession db = Context.getDBSession();
		try
		{
			db.beginTransaction();
			
			//1)����catLevel = parentLevel + 1
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
			
			//3)���ü�������			
			upateCascade(cat,db);
			
			//4)����и��ڵ����childcount+1
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
	 * ���·���ڵ㺢�ӽڵ�����
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
	
	//���¼�������child_count�ֶ�
	private void calculateChildCount(int catType,int catID,DBSession db)
	throws SQLException,E5Exception
	{	
		/**
		 * ����ʹ��
		 * UPDATE CATEGORY_FL set CHILD_COUNT=
		 * 		( select count(*) from CATEGORY_FL where parent_id=3 and ENTRY_DELETE_FLAG=0)
		 * 		Where ENTRY_ID=3
		 * �ĸ�ʽ����MYSQL�±���
		 * java.sql.SQLException: You can't specify target table 'CATEGORY_FL' for update in FROM clause
		 * ������SQL SERVER��Ҳ����
		 * 
		 * ����2��SQL���
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
		//1.ȡ���ɵķ��࣬���Ƚ�
		Category oldCat = getCat(cat.getCatType(), cat.getCatID(), null);		
	
		//����DBSession
		DBSession db = Context.getDBSession();
		try
		{
			db.beginTransaction();
			CatType catType = getType(cat.getCatType());
			//2.�޸ı�����
			//TODO:��oldCat���б��棬����ǰ�Ȱѿɸı��ֵ���ϡ�ע�ⲻ�ܸı�catLevel
			db.executeUpdate(getSQL(catType, m_UpdateSQL), getValueArray(cat), getFieldTypes());
			
			//3.��������Ƿ����仯�����б仯����Ҫ�޸������ӷ���
			if (!cat.getCatName().equals(oldCat.getCatName()))
				updateChildCascadeName(cat, db);
			
			//5.�����ύ
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
	 * �ƶ��ڵ�ʱ���޸ļ�������(�Լ��������¼�����)
	 * @author wanghc
	 * @param catType ��������
	 * @param catID   ��ǰ����ID
	 * @param destCatID Ŀ�ķ��� =0 ����>1����ID
	 * ����������ƻ��߲�η����˱仯������Ҫ�仯�ӷ���ļ�������
	 * 
	 */
	private void updateChildCascade(Category cat, int destCatID,int dlevel,DBSession db)
		throws E5Exception
	{	
		int catType = cat.getCatType();
		int catID   = cat.getCatID();
		
		//1) ���õ�ǰ����ļ������ơ�����ID
		//��ȥ����ʱ��mssqlserver��������
		//Category cat = getCat(catType,catID);
		cat.setParentID(destCatID);
		
		String   oname= cat.getCascadeName();  //��ǰ������ʷ��������
		String   oid  = cat.getCascadeID();    //��ǰ������ʷ����ID
		
		upateCascade(cat,db);
		
		String   name = cat.getCascadeName();   //��ǰ����ļ�������
		String   id   = cat.getCascadeID();   //��ǰ����ļ���ID
		
		//2) ȡ�������ӷ��࣬�������ǵ����ƺ�ID
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
			
		    //3) ��������� ��������		
			updateCascade(catType,catIDs,cascadeName,cascadeID,dlevel,db);			
			
		}
		//4) ���ø��±���(�����ӵ�����¸��±���)
		CatExtCascadeHelper.updateExtChildCascade(catType,catID,destCatID,db.getConnection());
	}
	
	private static String GET_ALL_CHILDREN_BY_CASCADEID = "select * from CATEGORY_OTHER where WT_TYPE=? and ENTRY_CASCADE_ID like ? order by ENTRY_LEVEL, ENTRY_DISP_ORDER ";
	private static String GET_ALL_NORMAL_CHILDREN_BY_CASCADEID = "select * from CATEGORY_OTHER where WT_TYPE=? and ENTRY_CASCADE_ID like ? And ENTRY_DELETE_FLAG=0 order by ENTRY_LEVEL, ENTRY_DISP_ORDER ";
	
	/**
	 * ���ݼ���ID������������
	 * @param catType
	 * @param cascadeID Ϊnull��ʾ��ѯȫ���ڵ�
	 * @param deleted true/��ѯ������ɾ���Ľڵ�,false/��ѯ������ɾ���Ľڵ�
	 * @author wanghc
	 * @return Category[] ��������ڵ�
	 */
	private Category[] getAllChildrenCatsByCascadeID(int catType,String cascadeID,boolean deleted,DBSession db)
		throws E5Exception
	{
		//ȡ����������
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
	 * �޸ķ�������ʱ���޸������ӽڵ㼶������
	 * @param cat
	 * @param db
	 * @author wanghc
	 * @throws E5Exception
	 */
	private void updateChildCascadeName(Category cat,DBSession db)
	throws E5Exception
	{	
		//��ǰ������ʷ��������
		String   oname    = cat.getCascadeName();  
		String ocascadeID = cat.getCascadeID();
		
		upateCascade(cat,db);
		
		//2) ȡ�������ӷ��࣬�������ǵ����ƺ�ID
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
			
		    //3) ��������� ��������		
			updateCascade(cat.getCatType(),catIDs,cascadeName,null,0,db);			
		}
	
	}
	
	/**
	 * ����һ������ļ�������
	 * @param cat
	 * @throws E5Exception
	 */
	private void upateCascade(Category cat,DBSession db) throws E5Exception
	{
		//1) ���õ�ǰ����ļ������ơ�����ID
		String   name = "";   //��ǰ����ļ�������
		String   id   = "";   //��ǰ����ļ���ID
		
		
		//��
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
		
		//���µ�ǰ��,������level��level��move�������¹���,level=0��ʾ������
		updateCascade(cat.getCatType(),new int[]{cat.getCatID()},new String[]{name},new String[]{id},0,db);
	}
	
	private static String UPDATE_CASCADE_ALL_SQL  = "UPDATE CATEGORY_OTHER set ENTRY_CASCADE_NAME=?,ENTRY_CASCADE_ID=?,ENTRY_LEVEL=ENTRY_LEVEL+? WHERE ENTRY_ID=?";
	private static String UPDATE_CASCADE_NAME_SQL = "UPDATE CATEGORY_OTHER set ENTRY_CASCADE_NAME=? WHERE ENTRY_ID=?";
	
	/**
	 * �������·���ļ������ơ�����ID,������Ϊ���úõĶ�Ӧ��ϵ����ȡֵ�� 
	 * @param catID
	 * @param cascadeName
	 * @param cascadeID
	 * @param level - ���
	 */
	private void updateCascade(int catType,int catID[],String cascadeName[],String cascadeID[],int level,DBSession db)
		throws E5Exception
	{
		Connection conn  = db.getConnection();
		PreparedStatement pstmt = null;
		try
		{
			
			//֧��ֻ����cascadeName
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
	 * �޸������ӷ���Ĳ�Σ��ó���ķ�ʽ�ݹ�����޸�
	 * ע��÷����ж�ÿһ�����ֻ��һ�β��ң�������ÿ�������һ���ӷ��࣬
	 * ����αȽ���ʱ�����ݵ�int[]���ܱȽϴ�
	 */
//	private void updateChildrenLevel(CatType catType, String strSQL, int[] catID, 
//			DBSession db) 
//	throws SQLException, E5Exception
//	{
//		//�޸ĵ�ǰ���
//		String sql = strSQL.replaceAll("@PARENTID@", CatHelper.join(catID, ','));
//		db.executeUpdate(sql, null);	
//		
//		//����������,��û�������ˣ��򷵻�
//		Category[] catArr = getBareSubCats(catType, catID, db,false);
//		
//		if (catArr == null) return;
//
//		//������ID��ȡ����
//		int[] childrenID = new int[catArr.length];
//		for (int i = 0; i < catArr.length; i++)
//			childrenID[i] = catArr[i].getCatID();
//			
//		//�ݹ飺�޸���������
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

		//ȡ��������
		CatType catType = getType(cat.getCatType());

		//׼��sql��䣬�Լ��������Ͳ�������
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
	 * ׼�������޸ĵ�SQL��䣬�Լ�SQL����еĲ����Ͳ�������
	 * @param catType
	 * @param cat
	 * @param fields ���޸Ĵ��ݵ����Ա�ʶ
	 * @param params SQL����еĲ���
	 * @param types SQL����еĲ�������
	 * @return
	 */
	private String prepaire(CatType catType, Category cat, int[] fields, 
			Object[] params, int[] types)
	{
		String strTableName = catType.getTableName();
		if (strTableName == null)
			strTableName = Category.DEFAULT_TABLENAME;
		//ƴ�޸����
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
	 * �޸������ӷ���Ĳ�Σ��ó���ķ�ʽ�ݹ�����޸�
	 * ע��÷����ж�ÿһ�����ֻ��һ�β��ң�������ÿ�������һ���ӷ��࣬
	 * ����αȽ���ʱ�����ݵ�int[]���ܱȽϴ�
	 */
	private void updateTransfer(CatType catType, String strSQL, int[] catID,
			Object[] params, int[] types, DBSession db) 
	throws SQLException, E5Exception
	{
		//�޸ĵ�ǰ���
		String sql = strSQL.replaceAll("@PARENTID@", CatHelper.join(catID, ','));
		db.executeUpdate(sql, params, types);
		
		//����������,��û�������ˣ��򷵻�
		Category[] catArr = getBareSubCats(catType, catID, db,false);
		
		if (catArr == null) return;

		//������ID��ȡ����
		int[] childrenID = new int[catArr.length];
		for (int i = 0; i < catArr.length; i++)
			childrenID[i] = catArr[i].getCatID();
		
		//�ݹ飺�޸���������
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
	 * ִ�з����ɾ���ͻָ�����
	 * ��Ϊ������ɾ���ͻָ�ʱ����Ҫͬʱ�޸��ӷ��࣬���ԱȽϸ��ӡ�
	 * @param catType
	 * @param catID
	 * @param delete
	 * @throws E5Exception
	 */
	private void catDelete(int catType, int catID, int delete) throws E5Exception
	{
		//����DBSession
		DBSession db = Context.getDBSession();
		try
		{
			CatType type = getType(catType);

			//1.ȡ�÷�����Ϣ����Ҫ�Ǽ���ID����
			Category cat = getBareALLCat(catType,catID,db);	
			//����ǰȡҪ�������б�
			Category[] cats = null;
			if(delete != 1)
				cats = getAllChildrenCatsByCascadeID(catType,cat.getCascadeID(),true,db);
			
			//2.���ݼ���ID��ƴ���޸��ӷ����DELETEFLAG��SQL			
			String strSQL = getSQL(type, m_DeleteSQL);		
			StringBuffer cascadeCond = new StringBuffer();
			cascadeCond = cascadeCond.append(cat.getCascadeID()).append(Category.separator).append("%");

			db.beginTransaction();//����

			//ɾ��(��ָ�)�ӷ���:���ݼ���ID			
			db.executeUpdate(strSQL, new Object[]{new Integer(delete), cascadeCond.toString() });
			//ɾ������ָ����������
			strSQL = getSQL(type, m_DeleteSelfSQL);		
			db.executeUpdate(strSQL, new Object[]{new Integer(delete), new Integer(catID)});
			
			//3.ά��child_count�ֶ�
			//���㸸
			if(cat.getParentID()!=0)
				calculateChildCount(catType, cat.getParentID(), db);

			//�����Լ�
			calculateChildCount(catType, catID, db);
			//����ǻָ���������������
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
		//������
		Category cat = getBareCat(catType, catID, db);
		if ((cat == null) || (extType == null)) return cat;

		//��չ����
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
			//�õ������������
			catArray = getBareCats(catType, db);
			if ((catArray == null) || (extType == null)) return catArray;

			//�õ��������չ����
			extArray = CatHelper.getExtManager().getAllExts(catType, extType);
			if (extArray == null) return catArray;
		}
		finally
		{
			try{db.close();}catch(Exception e1){e1.printStackTrace();}
		}

		//��ÿ���������չ���ԣ�����չ�����޸ķ������ͼ�����			
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
	 * ����չ������������һ���������չ����
	 */
	private CatExt getExt(int catID, CatExt[] extArray)
	{
		return getExt(catID, extArray, 0, extArray.length - 1);
	}

	/**
	 * ���ַ���λһ���������չ����
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
			//�õ������������
			catArray = getBareSubCats(catType, catID, db);
			if ((catArray == null) || (extType == null)) return catArray;

			//�õ��������չ����
			extArray = CatHelper.getExtManager().getSubExt(catType, catID, extType);
			if (extArray == null) return catArray;
		}
		finally
		{
			try{db.close();}catch(Exception e1){e1.printStackTrace();}
		}

		//��ÿ���������չ���ԣ�����չ�����޸ķ������ͼ�����			
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
				//ע���������͵�ת��
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
	 * ���ݷ���õ����ݿ����
	 */
	private String getSQL(Category cat, String templateSQL) throws E5Exception
	{
		return getSQL(cat.getCatType(), templateSQL);
	}
	
	/**
	 * ���ݷ�������ID�õ����ݿ����
	 */
	private String getSQL(int catType, String templateSQL) throws E5Exception
	{
		return getSQL(getType(catType), templateSQL);
	}

	/**
	 * ���ݷ������͵õ����ݿ����
	 */
	private String getSQL(CatType catType, String templateSQL) throws E5Exception
	{
		if (catType == null)
			throw new E5Exception("No Category Type!");
		return getSQL(catType.getTableName(), templateSQL);
	}

	/**
	 * ���ݷ������Ͷ�Ӧ�ı����õ����ݿ����
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
	 * ȡһ�������������
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
	 * ȡһ�������������(ɾ����δɾ����)
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
	 * ȡĳ�������͵����з��ֻ࣬ȡ������
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
	 * ȡĳ�����ֱ���ӷ���
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
	 * �Ѷ���������ֱ���ӷ���һ����ȡ��
	 * ���ڷ�������´����޸�
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
			//����ʾɾ����
			if(!deleted)
			{
				rs = db.executeQuery(
						getSQL(catType, m_SelectMultiSubSQL)
						.replaceAll("@PARENTID@", CatHelper.join(catID, ',')), 
						new Object[]{new Integer(catType.getCatType())}
						);
			}
			//��ʾȫ����
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
	 * ����ID��������������
	 */
	void create(CatType catType, int id) 
	throws E5Exception
	{
		try
		{
			BaseDAO dao = new BaseDAO();
			catType.setCatType(id);
			//�������͵�˳�򣬴���ʱʹ��ID��ͬ
			//��Ϊ���IDһ�������еķ������������ģ����Կ��Ա�֤�½��������������
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
		//������ɾ���ķ���
		return getChildrenCats(catType, catID, extType,false);
	}
	
	/* (non-Javadoc)
	 * @see com.founder.e5.cat.CatManager#getChildrenCatsIncludeDeleted(int, int, int)
	 */	
	public Category[] getChildrenCatsIncludeDeleted(int catType,int catID,int extType)
	throws E5Exception
	{
		//����ɾ���ķ���
		return getChildrenCats(catType, catID, extType,true);
	}
	
	private Category[] getChildrenCats(int catType, int catID, int extType,boolean deleted) throws E5Exception
	{
		//ȡ����������		
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
			
			//1) ȡ�������������(����������)
			cats = getAllChildrenCatsByCascadeID(catType,cascadeID,deleted,db);
			if(cats == null) return cats;
			
			//2) ȡ�����б���,ƥ�䵽����name,cascadeName��
			if(extType>0)
			{
				CatExt catExts[] = CatHelper.getExtManager().getAllExts(catType,extType);
				//��ÿ���������չ���ԣ�����չ�����޸ķ������ͼ�����
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
//	 * ȡһ������������ӷ���ʱ�ĵݹ��㷨
//	 * ȡ����ǰ����ӷ��࣬����ͳһ��List�У�Ȼ�����´���
//	 * һ��һ������ӷ��࣬������ͬһ��List�У���󷵻�
//	 * ��ΪҪ��ȡ��չ���ԣ�����������һЩ���Ӷ�
//	 * 
//	 * ע��÷����ж�ÿһ�����ֻ��һ�β��ң�������ÿ�������һ���ӷ��࣬
//	 * ����αȽ���ʱ�����ݵ�int[]���ܱȽϴ�
//	 * @param deleted true/����ȫ���ķ���,false/ֻ����δɾ���ķ���
//	 */
//	private void getChildrenCats(CatType type, int[] catIDs, int extType, 
//			DBSession db, List childrenList,boolean deleted)
//	throws E5Exception
//	{
//		//1.���ҵ���ǰ��ε������ӷ���
//		Category[] catArray = null;
//		CatExt[] extArray = null;
//
//		//�õ������ӷ����������
//		catArray = getBareSubCats(type, catIDs, db,deleted);
//		if (catArray == null) return;
//
//		//�õ��������չ���� 
//		if (extType > 0)
//			extArray = CatHelper.getExtManager().getSubExt(type.getCatType(), catIDs, extType);
//
//		//��ÿ���������չ���ԣ�����չ�����޸ķ������ͼ�����
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
//		//2.������ID��ȡ����
//		//3.�Ѳ鵽�������ӷ�����뵽�б���
//		int[] childrenID = new int[catArray.length];
//		for (int i = 0; i < catArray.length; i++)
//		{
//			childrenID[i] = catArray[i].getCatID();
//			childrenList.add(catArray[i]);
//		}
//		//4.�ݹ���ã����ӷ���������ӷ���
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
		
		//Ŀ��IDΪ0����ʾ�ŵ�����
		if (destCatID == 0) 
		{
			cat = getCat(catTypeID, srcCatID, null);
			
			//2.��¼���
			dLevel = 0 - cat.getCatLevel();
			
			//�����ƶ���Ĳ�Ρ����򡢸�ID
			cat.setCatLevel(0);//���Ϊ���������һ��
			srcParentID = cat.getParentID();
			
		}
		else
		{
			//1.ȡ���������࣬���Ƚ�
			Category parentCat = getCat(catTypeID, destCatID, null);
			cat = getCat(catTypeID, srcCatID, null);
			srcParentID = cat.getParentID();
			
			//2.��¼���
			dLevel = parentCat.getCatLevel() - cat.getCatLevel() + 1;
	
			//�����ƶ���Ĳ�Ρ����򡢸�ID
			cat.setCatLevel(parentCat.getCatLevel() + 1);//���Ϊ���������һ��
		}
		
		cat.setDisplayOrder(nextDispOrder(cat.getCatType(),destCatID));
		cat.setParentID(destCatID);//���ø�����

		CatType catType = getType(catTypeID);

		//����DBSession
		DBSession db = Context.getDBSession();
		try
		{
			db.beginTransaction();
			//3.�޸ı�����
			db.executeUpdate(getSQL(catType, m_UpdateSQL), getValueArray(cat), getFieldTypes());
			
			//4.������Ƿ�仯�����б仯����Ҫ�޸������ӷ���
			//Ч�ʽϵ��������ϴ�ʱ����ORA-01795: �б��е������ʽ��Ϊ 1000
			//�ڸ��¼���ʱ˳�������level,wanghc
			
//			if (dLevel != 0)//����0����ʾֻ���ƶ�����ԭ���������ͬ������
//			{
//				String strSQL = getSQL(catType, m_UpdateLevelSQL);
//				if (dLevel > 0)
//					strSQL = strSQL.replaceAll("@LEVEL@", "+" + dLevel);
//				else
//					strSQL = strSQL.replaceAll("@LEVEL@", "" + dLevel);
//				updateChildrenLevel(catType, strSQL, new int[]{cat.getCatID()}, db);
//			}
			
			//4.�������/����Ƿ����仯�����б仯����Ҫ�޸������ӷ���			
			//�����ƶ�����¼��� wanghc
			updateChildCascade(cat,destCatID, dLevel,db);
			
			//5.�ƶ�һ������ʱ��Դ������childcount-1��Ŀ�길����childcount+1			
			updateChildCount(catTypeID,srcParentID,-1,db);
			updateChildCount(catTypeID,destCatID,1,db);
			
			//6.�����ύ
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
	 * ȡ�÷���order 
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
	 * ����ȫ�����ӷ���
	 * @param catType
	 * @param catID
	 * @param extType
	 * @return
	 * @throws E5Exception
	 */
//	private Category[] getAllChildrenCats(int catType, int catID, int extType) 
//	throws E5Exception
//	{
//		//ȡ����������
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