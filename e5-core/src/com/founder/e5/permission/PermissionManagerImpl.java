package com.founder.e5.permission;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.type.Type;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

import com.founder.e5.context.BaseDAO;
import com.founder.e5.context.Context;
import com.founder.e5.context.DAOHelper;
import com.founder.e5.context.E5Exception;
import com.founder.e5.db.DBSession;

/**
 * @created 14-7-2005 16:25:20
 * @author Gong Lijie
 * @version 1.0
 */
class PermissionManagerImpl implements PermissionManager {

	PermissionManagerImpl(){
	}
	/**
	 * ȡ��ɫȨ��
	 * @param roleID ��ɫID
	 * @param resourceType ��Դ����
	 * @param resource ��Դ��
	 */
	public Permission get(int roleID, String resourceType, String resource)
		throws E5Exception
	{
		Permission perm = new Permission(roleID, resourceType, resource);
		try
		{
			BaseDAO dao = new BaseDAO();
			return (Permission)dao.get(Permission.class, perm);
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[GetPermission]", e);
		}
	}

	/**
	 * ȡ��ɫ������Ȩ��
	 * @param roleID ��ɫID
	 */
	public Permission[] getPermissions(int roleID) 
	throws E5Exception 
	{
		return getArrayFromList(DAOHelper.find("from Permission p where p.roleID=:role",
				new Integer(roleID), Hibernate.INTEGER));
	}

	/**
	 * ȡ��ɫ��ĳ��Դ���͵�Ȩ��
	 * @param roleID ��ɫID
	 * @param resourceType ��Դ����
	 */
	public Permission[] getPermissions(int roleID, String resourceType)
	throws E5Exception
	{
		return getArrayFromList(DAOHelper.find("from Permission p where p.roleID=:role and p.resourceType=:rt",
				new String[]{"role", "rt"},
				new Object[]{new Integer(roleID), resourceType},
				new Type[]{Hibernate.INTEGER, Hibernate.STRING}));
	}

	/**
	 * ȡĳ��Դ���͵�����Ȩ��
	 * @param resourceType ��Դ����
	 */
	public Permission[] getPermissions(String resourceType)
	throws E5Exception
	{
		return getArrayFromList(DAOHelper.find("from Permission p where p.resourceType=:r",
				resourceType, Hibernate.STRING));
	}
	/**
	 * ȡ�����е�Ȩ�޼�¼
	 * @return Ȩ�޼�¼����
	 * @throws E5Exception
	 */
	public Permission[] getAll() 
	throws E5Exception
	{
		BaseDAO dao = new BaseDAO();
		try
		{
			Session s = null;
			try
			{
				s = dao.getSession();
				List list = s.createCriteria(Permission.class)
					.addOrder(Order.asc("roleID"))
					.addOrder(Order.asc("resourceType"))
					.addOrder(Order.asc("resource"))
					.list();
				if (list.size() == 0) return null;
				return (Permission[])list.toArray(new Permission[0]);
			}
			finally
			{
				dao.closeSession(s);
			}
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[GetAllPermission]", e);
		}
	}

	/**
	 * ȡĳ��Դ������Ȩ��
	 * @param resourceType ��Դ����
	 * @param resource ��Դ��
	 */
	public Permission[] getPermissions(String resourceType, String resource)
	throws E5Exception
	{
		return getArrayFromList(DAOHelper.find("from Permission p where p.resourceType=:rt and p.resource=:r",
				new String[]{"rt", "r"},
				new Object[]{resourceType, resource},
				new Type[]{Hibernate.STRING, Hibernate.STRING}));
	}

	private Permission[] getArrayFromList(List pList)
	{
		if ((pList == null) || (pList.size() == 0)) return null;
		return (Permission[])pList.toArray(new Permission[0]);
	}

	/**
	 * ȡĳ��ɫ��ĳȨ�޵�������Դ
	 * @param roleID ��ɫID
	 * @param resourceType ��Դ����
	 * @param maskArray Ȩ�ޱ��
	 */
	public String[] getResourcesOfRole(int roleID, String resourceType, int[] maskArray)
	throws E5Exception
	{
		Permission[] pArr = getPermissions(roleID, resourceType);
		if (pArr == null) return null;
		if (maskArray == null) {
			String[] resArr = new String[pArr.length];
			for (int i = 0; i < pArr.length; i++) {
				resArr[i] = pArr[i].getResource();
			}
			return resArr;
		}

		List list = new ArrayList(pArr.length);
		for (int i = 0; i < pArr.length; i++) {
			if (PermissionHelper.hasPermission(pArr[i], maskArray))
				list.add(pArr[i].getResource());
		}
		if (list.size() == 0) 
			return null;
		else
			return (String[])(list.toArray(new String[0]));
	}

	/**
	 * ȡ��ĳ��Դ��ĳ��Ȩ�޵����н�ɫ
	 * @param resourceType ��Դ����
	 * @param resource ��Դ��
	 * @param maskArray Ȩ�ޱ��
	 */
	public int[] getRolesOfResources(String resourceType, String resource, int[] maskArray)
	throws E5Exception
	{
		Permission[] pArr = getPermissions(resourceType, resource);
		if (pArr == null) return null;

		if (maskArray == null) {
			int[] roleArr = new int[pArr.length];
			for (int i = 0; i < pArr.length; i++) {
				roleArr[i] = pArr[i].getRoleID();
			}
			return roleArr;
		}
		return PermissionHelper.getRoleByMask(pArr, maskArray);
	}

	/**
	 * ����ɫɾ������Ȩ��
	 * @param roleID ��ɫID
	 */
	public void deleteRole(int roleID)
	throws E5Exception{
		DAOHelper.delete("delete from Permission p where p.roleID=:role", 
				new Integer(roleID), Hibernate.INTEGER);
	}

	/**
	 * ����Դ����ɾ������Ȩ��
	 * ��������Դ����
	 * 
	 * @param resourceType
	 */
	public void deleteResourceType(String resourceType) 
	throws E5Exception{
		DAOHelper.delete("delete from Permission p where p.resourceType=:rt", 
				resourceType,  Hibernate.STRING);
	}

	/**
	 * ����Դ��ɾ������Ȩ��
	 * ��������Դ���ͣ���Դ��
	 * 
	 * @param resourceType
	 * @param resource
	 */
	public void deleteResource(String resourceType, String resource) 
	throws E5Exception{
		DAOHelper.delete("delete from Permission p where p.resourceType=:rt and p.resource=:r",
				new String[]{"rt", "r"},
				new Object[]{resourceType,resource},
				new Type[]{Hibernate.STRING, Hibernate.STRING});
	}

	/**
	 * Ȩ�ޱ���
	 * Ȩ��Ϊ0ʱ��ɾ�����е�Ȩ�޼�¼
	 * 
	 * @param permission
	 */
	public void save(Permission permission) 
	throws E5Exception 
	{
		save(permission, false);
	}

	/**
	 * Ȩ�޵���������
	 * �����Ϊ��������ʱ�������е�Ȩ���������
	 * Ȩ��Ϊ0ʱ��ɾ�����е�Ȩ�޼�¼
	 * 
	 * @param permission
	 * @param incremental
	 */
	public void save(Permission permission, boolean incremental)
	throws E5Exception 
	{
		BaseDAO dao = new BaseDAO();
		Transaction t = null;
		Session s = null;
		try
		{
			try {
				s = dao.getSession();
				t = dao.beginTransaction(s);
				save(permission, s, incremental);
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
			throw new E5Exception("[SavePermission]", e);
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
	private void save(Permission permission, Session s, boolean incremental) 
	throws HibernateException 
	{
		//������Ȩ��Ϊ0ʱ�Ĵ���:�������������棬��ֱ��ɾ�������򲻸ı䡣
		if (permission.getPermission() == 0) {
			/**
			 * ��MySQL�µ�Hibernate����������,ֱ����s.deleteһ�������ڵļ�¼�ᱨ��
			 * ���ֻ����ȡ���������Ƿ�������Ȩ�� 
			 */
			if (!incremental)
			{
				//��ȡ�����ݿ��еľ�Ȩ�޼�¼
				Permission perm = new Permission(permission.getRoleID(), permission.getResourceType(),
						permission.getResource());
				perm = (Permission)s.get(Permission.class, perm);
				//�����ڣ���ɾ��
				if (perm != null) s.delete(perm);
			}
			return;
		}
		//��ȡ�����ݿ��еľ�Ȩ�޼�¼
		Permission perm = new Permission(permission.getRoleID(), permission.getResourceType(),
				permission.getResource());
		perm = (Permission)s.get(Permission.class, perm);
		//�޸�Ȩ��
		if (perm == null)
			s.save(permission);
		else if (incremental)
			perm.setPermission(perm.getPermission() | permission.getPermission());
		else 
			perm.setPermission(permission.getPermission());
	}

	/**
	 * ���Ȩ�ޱ���
	 * Ȩ��Ϊ0ʱ��ɾ�����е�Ȩ�޼�¼
	 * 
	 * @param permissionArray
	 */
	public void save(Permission[] permissionArray)
	throws E5Exception
	{
		save(permissionArray, false);
	}

	/**
	 * ���Ȩ�޵���������
	 * �����Ϊ��������ʱ�������е�Ȩ���������
	 * Ȩ��Ϊ0ʱ��ɾ�����е�Ȩ�޼�¼
	 * @param permissionArray
	 * @param incremental
	 * @throws E5Exception
	 */
	public void save(Permission[] permissionArray, boolean incremental)
	throws E5Exception
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
					save(permissionArray[i], s, incremental);
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
			throw new E5Exception("[SavePermissions]", e);
		}
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.PermissionManager#find(int, java.lang.String)
	 */
	public Permission[] find(int roleID, String resourceTypePattern) 
	throws E5Exception
	{
		BaseDAO dao = new BaseDAO();
		try
		{
			Session s = null;
			try
			{
				s = dao.getSession();
				List list = s.createCriteria(Permission.class)
					.add(Expression.eq("roleID", new Integer(roleID)))
					.add(Expression.like("resourceType", resourceTypePattern))
					.addOrder(Order.asc("resourceType"))
					.addOrder(Order.asc("resource"))
					.list();
				if (list.size() == 0) 
					return null;
				else
					return (Permission[])list.toArray(new Permission[0]);
			}
			finally
			{
				dao.closeSession(s);
			}
		}
		catch (HibernateException e)
		{
			throw new E5Exception("[FindPermission]", e);
		}
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.PermissionManager#delete(int, java.lang.String)
	 */
	public void delete(int roleID, String resourceType) throws E5Exception {
		DAOHelper.delete("delete from Permission p where p.roleID=:role and p.resourceType=:resType", 
				new String[]{"role", "resType"},
				new Object[]{new Integer(roleID), resourceType},
				new Type[]{Hibernate.INTEGER, Hibernate.STRING});
	}

	//Ȩ�޸���ʱʹ�õ����ݿ����
	private final String COPY_SQL = "insert into FSYS_PERMISSION(NID,NRESOURCEID,NRESOURCETYPE,NPERMISSION,NTYPE,NAPPID) select @ROLEID@,NRESOURCEID,NRESOURCETYPE,NPERMISSION,NTYPE,NAPPID from FSYS_PERMISSION where NID=?";
	
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.PermissionManager#copy(int, int)
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
			throw new E5Exception("[PermissionManager.copy]srcRoleID=" + srcRoleID +", destRoleID=" + destRoleID, e);
		}
		finally
		{
			try{db.close();}catch(Exception e1){e1.printStackTrace();}
		}
	}
}