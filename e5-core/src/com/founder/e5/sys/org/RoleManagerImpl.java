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
 * @created 11-����-2005 10:31:16
 */
class RoleManagerImpl implements RoleManager {

	public RoleManagerImpl(){

	}

	
	/**
	 * ������ɫ
	 * 
	 * @param orgID  ��֯ID
	 * @param roleName    ��ɫ��
	 */
//�������޸ģ�ԭ�����ص��ǳ���0��������ʱ�޷��õ��µĽ�ɫID,���ڸĳɷ��ؽ�ɫID
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
	 * ��ø�����ɫID�Ľ�ɫ
	 * 
	 * @param roleID    ��ɫID
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
	 * ������ 2006-4-19
	 * ��ȡ���н�ɫ����
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
	 * ���½�ɫ
	 * 
	 * @param role    ��ɫ����
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
	 * ɾ�������Ľ�ɫ
	 * 
	 * @param roleID    ��ɫID
	 */
	public void delete(int roleID) throws E5Exception{
		DAOHelper.delete("delete from com.founder.e5.sys.org.Role as role where role.RoleID = :roleID", 
				new Integer(roleID), Hibernate.INTEGER);
	}

	/**
	 * ���ָ���û��Ľ�ɫ
	 * 
	 * @param userID    �û�ID
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
	 * ���ָ����֯�µ�ָ�����ƵĽ�ɫ����
	 * 
	 * @param orgID  ��֯ID
	 * @param roleName    ��ɫ��
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
	 * ���ָ���û�ID���û���ɫ����
	 * 
	 * @param userID �û�ID
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
	 * �����û�ID�ͽ�ɫID����û���ɫ����
	 * 
	 * @param userID �û�ID
	 * @param roleID ��ɫID
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
	 * ����ɫIDָ���Ľ�ɫ�����û�
	 * 
	 * @param userID �û�ID
	 * @param userRole �û���ɫ����
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
	 * ����ɫIDָ���Ľ�ɫ���û�ȡ��
	 * 
	 * @param userID �û�ID
	 * @param roleID ��ɫID
	 */
	public void revokeRole(int userID, int roleID) throws E5Exception{
		DAOHelper.delete("delete from com.founder.e5.sys.org.UserRole as userrole where userrole.RoleID = :roleID  and userrole.UserID = :userID",
				new String[]{"roleID", "userID"}, 
				new Object[] {new Integer(roleID), new Integer(userID)}, 
				new Type[] {Hibernate.INTEGER, Hibernate.INTEGER});
	}

	
	/**
	 * ������ָ����˳��Խ�ɫ��������
	 * @param roleIDs    ��ɫID����
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
	 * ���ָ����ɫ�ĸ���֯
	 * 
	 * @param roleID    ��ɫID
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
	 * ���������� 2006-4-19
	 * ��������û���ɫ��ϵ
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
	//���������ӣ�ʵ�ֽ�ɫ��ѯ 2006-3-8
	/*
	 * ���ݽ�ɫ����ȡ��Ӧ�Ľ�ɫ
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

		//���������ӣ�ʵ�ֽ�ɫ��ѯ 2006-3-8
		/*
		 * ���ݽ�ɫ���е�ĳЩ�ʻ�ȡ��Ӧ�Ľ�ɫ
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