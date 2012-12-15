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
	 * 取角色权限
	 * @param roleID 角色ID
	 * @param resourceType 资源类型
	 * @param resource 资源名
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
	 * 取角色的所有权限
	 * @param roleID 角色ID
	 */
	public Permission[] getPermissions(int roleID) 
	throws E5Exception 
	{
		return getArrayFromList(DAOHelper.find("from Permission p where p.roleID=:role",
				new Integer(roleID), Hibernate.INTEGER));
	}

	/**
	 * 取角色的某资源类型的权限
	 * @param roleID 角色ID
	 * @param resourceType 资源类型
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
	 * 取某资源类型的所有权限
	 * @param resourceType 资源类型
	 */
	public Permission[] getPermissions(String resourceType)
	throws E5Exception
	{
		return getArrayFromList(DAOHelper.find("from Permission p where p.resourceType=:r",
				resourceType, Hibernate.STRING));
	}
	/**
	 * 取出所有的权限记录
	 * @return 权限记录数组
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
	 * 取某资源的所有权限
	 * @param resourceType 资源类型
	 * @param resource 资源名
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
	 * 取某角色有某权限的所有资源
	 * @param roleID 角色ID
	 * @param resourceType 资源类型
	 * @param maskArray 权限标记
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
	 * 取对某资源有某种权限的所有角色
	 * @param resourceType 资源类型
	 * @param resource 资源名
	 * @param maskArray 权限标记
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
	 * 按角色删除所有权限
	 * @param roleID 角色ID
	 */
	public void deleteRole(int roleID)
	throws E5Exception{
		DAOHelper.delete("delete from Permission p where p.roleID=:role", 
				new Integer(roleID), Hibernate.INTEGER);
	}

	/**
	 * 按资源类型删除所有权限
	 * 参数：资源类型
	 * 
	 * @param resourceType
	 */
	public void deleteResourceType(String resourceType) 
	throws E5Exception{
		DAOHelper.delete("delete from Permission p where p.resourceType=:rt", 
				resourceType,  Hibernate.STRING);
	}

	/**
	 * 按资源名删除所有权限
	 * 参数：资源类型，资源名
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
	 * 权限保存
	 * 权限为0时，删除表中的权限记录
	 * 
	 * @param permission
	 */
	public void save(Permission permission) 
	throws E5Exception 
	{
		save(permission, false);
	}

	/**
	 * 权限的增量保存
	 * 当标记为增量保存时，与已有的权限做或操作
	 * 权限为0时，删除表中的权限记录
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
	 * 私有方法
	 * 检查数据库中是否已经有某权限记录，
	 * 若没有，则调用session.save方法增加
	 * 若已经存在，若是权限增量保存，则用或方法修改权限；否则直接修改权限
	 * @param permission 权限实例
	 * @param s Session Hibernate会话
	 * @param incremental 是否增量保存
	 * @throws HibernateException
	 */
	private void save(Permission permission, Session s, boolean incremental) 
	throws HibernateException 
	{
		//当保存权限为0时的处理:若不是增量保存，则直接删除；否则不改变。
		if (permission.getPermission() == 0) {
			/**
			 * 在MySQL下的Hibernate处理有问题,直接用s.delete一个不存在的记录会报错
			 * 因此只能先取出来，看是否存在这个权限 
			 */
			if (!incremental)
			{
				//先取出数据库中的旧权限记录
				Permission perm = new Permission(permission.getRoleID(), permission.getResourceType(),
						permission.getResource());
				perm = (Permission)s.get(Permission.class, perm);
				//若存在，则删除
				if (perm != null) s.delete(perm);
			}
			return;
		}
		//先取出数据库中的旧权限记录
		Permission perm = new Permission(permission.getRoleID(), permission.getResourceType(),
				permission.getResource());
		perm = (Permission)s.get(Permission.class, perm);
		//修改权限
		if (perm == null)
			s.save(permission);
		else if (incremental)
			perm.setPermission(perm.getPermission() | permission.getPermission());
		else 
			perm.setPermission(permission.getPermission());
	}

	/**
	 * 多个权限保存
	 * 权限为0时，删除表中的权限记录
	 * 
	 * @param permissionArray
	 */
	public void save(Permission[] permissionArray)
	throws E5Exception
	{
		save(permissionArray, false);
	}

	/**
	 * 多个权限的增量保存
	 * 当标记为增量保存时，与已有的权限做或操作
	 * 权限为0时，删除表中的权限记录
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

	//权限复制时使用的数据库语句
	private final String COPY_SQL = "insert into FSYS_PERMISSION(NID,NRESOURCEID,NRESOURCETYPE,NPERMISSION,NTYPE,NAPPID) select @ROLEID@,NRESOURCEID,NRESOURCETYPE,NPERMISSION,NTYPE,NAPPID from FSYS_PERMISSION where NID=?";
	
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.PermissionManager#copy(int, int)
	 */
	public void copy(int srcRoleID, int destRoleID) throws E5Exception {
		deleteRole(destRoleID);//先把原来的权限全部清除
		
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