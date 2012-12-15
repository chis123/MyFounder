package com.founder.e5.permission;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.founder.e5.context.BaseDAO;
import com.founder.e5.context.Context;
import com.founder.e5.context.DAOHelper;
import com.founder.e5.context.E5Exception;
import com.founder.e5.db.DBSession;

/**
 * @created 14-7-2005 16:23:14
 * @author Gong Lijie
 * @version 1.0
 */
class FVPermissionManagerImpl implements FVPermissionManager {

	FVPermissionManagerImpl()
	{
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FVPermissionManager#save(com.founder.e5.permission.FVPermission)
	 */
	public void save(FVPermission permission) throws E5Exception
	{
		BaseDAO dao = new BaseDAO();
		Transaction t = null;
		Session s = null;
		try
		{
			try {
				s = dao.getSession();
				t = dao.beginTransaction(s);
				save(permission, s);
				dao.commitTransaction(t);
			}
			catch (HibernateException e) {
				if (null != t) t.rollback();
	            throw e;
			}
			finally {
				dao.closeSession(s);
			}
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[SaveFVPermission]", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FVPermissionManager#save(com.founder.e5.permission.FVPermission[])
	 */
	public void save(FVPermission[] permissionArray) throws E5Exception
	{
		if (permissionArray == null) return;

		BaseDAO dao = new BaseDAO();
		Transaction t = null;
		Session s = null;
		try
		{
			try {
				s = dao.getSession();
				t = dao.beginTransaction(s);
				for (int i = 0; i < permissionArray.length; i++)
					save(permissionArray[i], s);
				dao.commitTransaction(t);
			}
			catch (HibernateException e) {
				if (null != t) t.rollback();
	            throw e;
			}
			finally {
				dao.closeSession(s);
			}
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[SaveFVPermissions]", e);
		}
	}

	/**
	 * ˽�з���
	 * ������ݿ����Ƿ��Ѿ���ĳȨ�޼�¼��
	 * ��û�У������session.save��������
	 * ���Ѿ����ڣ�����Ȩ���������棬���û򷽷��޸�Ȩ�ޣ�����ֱ���޸�Ȩ��
	 * @param permission Ȩ��ʵ��
	 * @param s Session Hibernate�Ự
	 * @param incremental �Ƿ���������
	 * @throws HibernateException
	 */
	private void save(FVPermission permission, Session s) throws HibernateException {
		//������Ȩ��Ϊ0ʱ,ֱ��ɾ��
		if (permission.getPermission() == 0) {
			/**
			 * ��MySQL�µ�Hibernate����������,ֱ����s.deleteһ�������ڵļ�¼�ᱨ��
			 * ���ֻ����ȡ���������Ƿ�������Ȩ�� 
			 */
			//��ȡ�����ݿ��еľ�Ȩ�޼�¼
			FVPermission perm = new FVPermission(permission.getRoleID(), permission.getFvID());
			perm = (FVPermission)s.get(FVPermission.class, perm);
			//�����ڣ���ɾ��
			if (perm != null) s.delete(perm);
			return;
		}
		//��ȡ�����ݿ��еľ�Ȩ�޼�¼
		FVPermission perm = new FVPermission(permission.getRoleID(), permission.getFvID());
		perm = (FVPermission)s.get(FVPermission.class, perm);
		//�޸�Ȩ��
		if (perm == null)
			s.save(permission);
		else 
			perm.setPermission(permission.getPermission());
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FVPermissionManager#deleteFV(int)
	 */
	public void deleteFolder(int fvID) throws E5Exception
	{
		DAOHelper.delete("delete from FVPermission where fvID=:fv", 
					new Integer(fvID), Hibernate.INTEGER);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FVPermissionReader#get(int, int)
	 */
	public int get(int roleID, int fvID) throws E5Exception
	{
		FVPermission perm = new FVPermission(roleID, fvID);
		try
		{
			BaseDAO dao = new BaseDAO();
			dao.get(FVPermission.class, perm);
			if (perm != null)
				return perm.getPermission();
			else
				return 0;
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[GetFVPermission]", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FVPermissionReader#getPermissions(int)
	 */
	public FVPermission[] getPermissionsByRole(int roleID) throws E5Exception
	{
		return getArrayFromList(DAOHelper.find("from FVPermission p where p.roleID=:role",
				new Integer(roleID), Hibernate.INTEGER));
	}

	private FVPermission[] getArrayFromList(List pList)
	{
		if ((pList == null) || (pList.size() == 0)) return null;
		return (FVPermission[])pList.toArray(new FVPermission[0]);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FVPermissionReader#getFoldersOfRole(int, int[])
	 */
	public int[] getFoldersOfRole(int roleID, int[] maskArray) throws E5Exception
	{
		FVPermission[] pArr = getPermissionsByRole(roleID);
		if (pArr == null) return null;
		if (maskArray == null) {
			int[] resArr = new int[pArr.length];
			for (int i = 0; i < pArr.length; i++) {
				resArr[i] = pArr[i].getFvID();
			}
			return resArr;
		}

		List list = new ArrayList(pArr.length);
		for (int i = 0; i < pArr.length; i++) {
			if (PermissionHelper.hasPermission(pArr[i].getPermission(), maskArray))
				list.add(new Integer(pArr[i].getFvID()));
		}
		
		int size = list.size();
		if (size == 0) return null;
		int[] fvArr = new int[size];
		for (int i = 0; i < size; i++)
			fvArr[i] = ((Integer)list.get(i)).intValue();
		return fvArr;
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FVPermissionReader#getRolesOfFolder(int, int[])
	 */
	public int[] getRolesOfFolder(int fvID, int[] maskArray) throws E5Exception
	{
		FVPermission[] pArr = getPermissionsByFolder(fvID);
		if (pArr == null) return null;

		if (maskArray == null) {
			int[] arr = new int[pArr.length];
			for (int i = 0; i < pArr.length; i++) {
				arr[i] = pArr[i].getRoleID();
			}
			return arr;
		}
		//�Ƚ�Ȩ�ޣ��õ����ʵĽ�ɫ
		List list = new ArrayList(pArr.length);
		for (int i = 0; i < pArr.length; i++) {
			if (PermissionHelper.hasPermission(pArr[i].getPermission(), maskArray))
				list.add(new Integer(pArr[i].getRoleID()));
		}
		
		int size = list.size();
		if (size == 0) return null;
		int[] arr = new int[size];
		for (int i = 0; i < size; i++)
			arr[i] = ((Integer)list.get(i)).intValue();
		return arr;
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FVPermissionManager#getAll()
	 */
	public FVPermission[] getAll() throws E5Exception
	{
		BaseDAO dao = new BaseDAO();
		try
		{
			Session s = null;
			try
			{
				s = dao.getSession();
				List list = s.createCriteria(FVPermission.class)
					.addOrder(Order.asc("roleID"))
					.addOrder(Order.asc("fvID"))
					.list();
				return (FVPermission[])list.toArray(new FVPermission[0]);
			}
			finally
			{
				dao.closeSession(s);
			}
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[GetAllFVPermission]", e);
		}
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FVPermissionReader#getPermissionsByFolder(int)
	 */
	public FVPermission[] getPermissionsByFolder(int fvID) throws E5Exception
	{
		return getArrayFromList(DAOHelper.find("from FVPermission p where p.fvID=:fvID",
				new Integer(fvID), Hibernate.INTEGER));
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FVPermissionManager#deleteRole(int)
	 */
	public void deleteRole(int roleID) throws E5Exception
	{
		DAOHelper.delete("delete from FVPermission p where p.roleID=:role", 
				new Integer(roleID), Hibernate.INTEGER);
	}

	//Ȩ�޸���ʱʹ�õ����ݿ����
	private final String COPY_SQL = "insert into DOM_FVUSERS(GROUPID,FVID,PERMISSIONCODE,ATYPE) select @ROLEID@,FVID,PERMISSIONCODE,ATYPE from DOM_FVUSERS where GROUPID=?";
	
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FVPermissionManager#copy(int, int)
	 */
	public void copy(int srcRoleID, int destRoleID) throws E5Exception {
		deleteRole(destRoleID);//�Ȱ�ԭ����Ȩ��ȫ�����
		
		DBSession db = Context.getDBSession();
		try
		{
			String sql = COPY_SQL.replaceFirst("@ROLEID@", String.valueOf(destRoleID));
			db.executeUpdate(sql, new Object[]{new Integer(srcRoleID)});
		}
		catch (Exception e)
		{
			throw new E5Exception("[FVPermissionManager.copy]srcRoleID=" + srcRoleID +", destRoleID=" + destRoleID, e);
		}
		finally
		{
			try{db.close();}catch(Exception e1){e1.printStackTrace();}
		}
	}

}