package com.founder.e5.sys.org;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Hibernate;
import org.hibernate.type.Type;

import com.founder.e5.commons.ResourceMgr;
import com.founder.e5.context.BaseDAO;
import com.founder.e5.context.DAOHelper;
import com.founder.e5.context.E5Exception;
import com.founder.e5.context.EUID;
import com.founder.e5.sys.org.RoleManager;

/**
 * @version 1.0
 * @created 11-七月-2005 10:31:16
 */
class RoleManagerImpl implements RoleManager {

	public RoleManagerImpl(){

	}

	
	/**
	 * 创建角色
	 * 
	 * @param orgID  组织ID
	 * @param roleName    角色名
	 */
//王朝阳修改，原来返回的是常数0，在增加时无法得到新的角色ID,现在改成返回角色ID
//2006-03-02	
	public int create(int orgID, String roleName) throws E5Exception{
	    BaseDAO dao = new BaseDAO();
	    Role role = new Role();
	    role.setOrgID(orgID);
	    role.setRoleName(roleName);
	    int roleID=0;
	    try
        {
            roleID = (int)EUID.getID("RoleID");
            role.setRoleID(roleID);
            dao.save(role);

        }
        catch (Exception e)
        {
            throw new E5Exception("create role error.", e);
        }
		return roleID;
	}

	/**
	 * 获得给定角色ID的角色
	 * 
	 * @param roleID    角色ID
	 */
	public Role get(int roleID) throws E5Exception{
        List list = DAOHelper.find("select role from com.founder.e5.sys.org.Role as role where role.RoleID = :roleID", 
        		new Integer(roleID), Hibernate.INTEGER);
        if(list.size() > 0)
            return (Role)list.get(0);
        else
            return null;
	}

	/**
	 * 王朝阳 2006-4-19
	 * 获取所有角色对象
	 * @return
	 * @throws E5Exception
	 */

	public Role [] getRoles() throws E5Exception
	{
	    Role[] roles;
        List list = DAOHelper.find("select role from com.founder.e5.sys.org.Role as role"); 
        roles = new Role[list.size()];
        list.toArray(roles);
        return roles;
		
	}	

	/**
	 * 更新角色
	 * 
	 * @param role    角色对象
	 */
	public void update(Role role) throws E5Exception{
	    BaseDAO dao = new BaseDAO();
	    try
        {
            dao.update(role);
        }
        catch (Exception e)
        {
            throw new E5Exception("update role error.", e);
        }
	}

	/**
	 * 删除给定的角色
	 * 
	 * @param roleID    角色ID
	 */
	public void delete(int roleID) throws E5Exception{
		DAOHelper.delete("delete from com.founder.e5.sys.org.Role as role where role.RoleID = :roleID", 
				new Integer(roleID), Hibernate.INTEGER);
	}

	/**
	 * 获得指定用户的角色
	 * 
	 * @param userID    用户ID
	 */
	public Role[] getRolesByUser(int userID) throws E5Exception{
	    Role[] roles;
        List list = DAOHelper.find("select role from com.founder.e5.sys.org.UserRole as userrole, com.founder.e5.sys.org.Role as role where userrole.RoleID = role.RoleID and userrole.UserID = :userID", 
        		new Integer(userID), Hibernate.INTEGER);
        roles = new Role[list.size()];
        list.toArray(roles);
        return roles;
	}

	/**
	 * 获得指定组织下的指定名称的角色对象
	 * 
	 * @param orgID  组织ID
	 * @param roleName    角色名
	 */
	public Role getRoleByName(int orgID, String roleName) throws E5Exception{
        List list = DAOHelper.find("select role from com.founder.e5.sys.org.Role as role where role.RoleName = :roleName and role.OrgID = :orgID",
        		new String[]{"roleName", "orgID"}, 
        		new Object[] {roleName, new Integer(orgID)}, 
        		new Type[] {Hibernate.STRING, Hibernate.INTEGER});
        if(list.size() > 0)
            return (Role)list.get(0);
        else
            return null;
	}

	/**
	 * 获得指定用户ID的用户角色对象
	 * 
	 * @param userID 用户ID
	 */
	public UserRole[] getUserRoles(int userID) throws E5Exception{
	    UserRole[] userRoles;
        List list = DAOHelper.find("select userrole from com.founder.e5.sys.org.UserRole as userrole where userrole.UserID = :userID", 
        		new Integer(userID), Hibernate.INTEGER);
        userRoles = new UserRole[list.size()];
        list.toArray(userRoles);
        return userRoles;
	}

	/**
	 * 根据用户ID和角色ID获得用户角色对象
	 * 
	 * @param userID 用户ID
	 * @param roleID 角色ID
	 */
	public UserRole getUserRole(int userID, int roleID) throws E5Exception{
        List list = DAOHelper.find("select userrole from com.founder.e5.sys.org.UserRole as userrole where userrole.RoleID = :roleID and userrole.UserID = :userID",
        		new String[]{"roleID", "userID"}, 
        		new Object[] {new Integer(roleID),new Integer(userID)}, 
        		new Type[] {Hibernate.INTEGER, Hibernate.INTEGER});
        if(list.size() > 0)
            return (UserRole)list.get(0);
        else
            return null;
	}

	/**
	 * 将角色ID指定的角色授予用户
	 * 
	 * @param userID 用户ID
	 * @param userRole 用户角色对象
	 */
	public void grantRole(int userID, UserRole userRole) throws E5Exception{
	    BaseDAO dao = new BaseDAO();
	    try
        {
            dao.save(userRole);
        }
        catch (Exception e)
        {
            throw new E5Exception("grant role to user error.", e);
        }
	}

	/**
	 * 将角色ID指定的角色从用户取消
	 * 
	 * @param userID 用户ID
	 * @param roleID 角色ID
	 */
	public void revokeRole(int userID, int roleID) throws E5Exception{
		DAOHelper.delete("delete from com.founder.e5.sys.org.UserRole as userrole where userrole.RoleID = :roleID  and userrole.UserID = :userID",
				new String[]{"roleID", "userID"}, 
				new Object[] {new Integer(roleID), new Integer(userID)}, 
				new Type[] {Hibernate.INTEGER, Hibernate.INTEGER});
	}

	
	/**
	 * 按参数指定的顺序对角色进行排序
	 * @param roleIDs    角色ID数组
	 */
	public void sortRoles(int[] roleIDs) throws E5Exception{
	    BaseDAO dao = new BaseDAO();
	    Session session = null;
	    Transaction t = null;
	    try
        {
	        session = dao.getSession();
	        t = dao.beginTransaction(session);
            if(roleIDs != null)
                for(int i = 0; i < roleIDs.length; i++)
                {
                    Role role = (Role)dao.find("select role from com.founder.e5.sys.org.Role as role where role.RoleID = :roleID ", 
                    		new Integer(roleIDs[i]), Hibernate.INTEGER, session).get(0);
                    role.setOrderID(i + 1);
                    dao.update(role, session);
                }
            t.commit();
        }
        catch (Exception e)
        {
            ResourceMgr.rollbackQuietly(t);
            throw new E5Exception("order roles error.", e);
        }
        finally
        {
            ResourceMgr.closeQuietly(session);
        }
	}

	/**
	 * 获得指定角色的父组织
	 * 
	 * @param roleID    角色ID
	 */
	public Org getParentOrgByRole(int roleID) throws E5Exception{
        List list = DAOHelper.find("select org from com.founder.e5.sys.org.Org as org, com.founder.e5.sys.org.Role as role where role.OrgID = org.OrgID and role.RoleID = :roleID", 
        		new Integer(roleID), Hibernate.INTEGER);
        if(list.size() == 1)
            return (Org)list.get(0);
        else
            return null;
	}
	
	/**
	 * 王朝阳增加 2006-4-19
	 * 获得所有用户角色关系
	 * @return
	 * @throws E5Exception
	 */
	public UserRole [] getUserRoles() throws E5Exception
	{
	    UserRole[] userRoles;
        List list = DAOHelper.find("select userrole from com.founder.e5.sys.org.UserRole as userrole");
        userRoles = new UserRole[list.size()];
        list.toArray(userRoles);
        return userRoles;
		
	}	
	//王朝阳增加，实现角色查询 2006-3-8
	/*
	 * 根据角色名获取对应的角色
	 * 
	 * 
	 */
	 public Role [] getRolesByName(String roleName) throws E5Exception
	 {
		    Role[] roles = null;
	        List list = DAOHelper.find("from com.founder.e5.sys.org.Role as role where role.RoleName = :roleName", 
	        		roleName, Hibernate.STRING);
            roles = new Role[list.size()];
            list.toArray(roles );

        return roles ;
		 
	 }

		//王朝阳增加，实现角色查询 2006-3-8
		/*
		 * 根据角色名中的某些词获取对应的角色
		 * 
		 * 
		 */
	 public Role [] getRolesIncludeName(String roleName) throws E5Exception
	 {
		    Role[] roles = null;
		    String wrapRoleName="%"+roleName+"%";
	        List list = DAOHelper.find("from com.founder.e5.sys.org.Role as role where role.RoleName like :wrapRoleName", 
	        		wrapRoleName, Hibernate.STRING);
            roles = new Role[list.size()];
            list.toArray(roles );

        return roles ;
		 
	 }
	
}