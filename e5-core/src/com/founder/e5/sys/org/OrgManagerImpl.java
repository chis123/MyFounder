package com.founder.e5.sys.org;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Hibernate;

import com.founder.e5.context.BaseDAO;
import com.founder.e5.context.DAOHelper;
import com.founder.e5.context.E5Exception;
import com.founder.e5.context.Context;
import com.founder.e5.context.EUID;
import com.founder.e5.commons.ResourceMgr;

/**
 * @version 1.0
 * @created 11-七月-2005 14:12:52
 */
class OrgManagerImpl implements OrgManager 
{
	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgManager#create(com.founder.e5.sys.org.Org)
	 */
	public void create(Org org) throws E5Exception {
		if (org.getParentID() < 0)
			throw new E5Exception("parent org not exist.");
		if (org.getName() == null || org.getName().trim().equals(""))
			throw new E5Exception("org name is null.");
		org.setName(org.getName().trim());
		Org[] orgs = getNextChildOrgs(org.getParentID());
		if (orgs != null)
			for (int i = 0; i < orgs.length; i++) 
			{
				if (orgs[i].getName().equals(org.getName()))
					throw new E5Exception("the same org name on the parent org.");
			}
		int orgID = 1;
		try {
			orgID = (int) EUID.getID("OrgID");
			org.setOrgID(orgID);
			org.setOrderID(getMaxOrderID(org.getParentID()) + 1);
			BaseDAO dao = new BaseDAO();
			dao.save(org);
		} catch (Exception e) {
			throw new E5Exception("create org error.", e);
		}
	}

	/**
	 * 获取指定组织ID下所有组织中最大的排序ID
	 * @param orgID 组织ID
	 * @return 最大的排序ID
	 * @throws E5Exception
	 */
	private int getMaxOrderID(int orgID) throws E5Exception {
		int maxOrderID = 0;
		List list = DAOHelper.find("select max(org.OrderID) from com.founder.e5.sys.org.Org as org where org.ParentID = :pID ",
						new Integer(orgID), Hibernate.INTEGER);
		if (list != null)
			if (list.size() == 1)
				if (list.get(0) != null)
					maxOrderID = ((Integer) list.get(0)).intValue();
		return maxOrderID;
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgReader#get()
	 */
	public Org[] get() throws E5Exception {
		Org[] orgs = null;
		List list = DAOHelper.find("from com.founder.e5.sys.org.Org as org order by org.OrderID");
		if (list.size() > 0) {
			orgs = new Org[list.size()];
			list.toArray(orgs);
		}
		return orgs;
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgReader#get(int)
	 */
	public Org get(int orgID) throws E5Exception {
		List list = DAOHelper.find("select org from com.founder.e5.sys.org.Org as org where org.OrgID = :orgID",
						new Integer(orgID), Hibernate.INTEGER);
		if (list.size() > 0)
			return (Org) list.get(0);
		else
			return null;
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgManager#update(com.founder.e5.sys.org.Org)
	 */
	public void update(Org org) throws E5Exception {
		if (org.getParentID() < 0)
			throw new E5Exception("parent org not exist.");
		if (org.getName() == null || org.getName().trim().equals(""))
			throw new E5Exception("org name is null.");
		org.setName(org.getName().trim());
		Org[] orgs = getNextChildOrgs(org.getParentID());
		if (orgs != null)
			for (int i = 0; i < orgs.length; i++) {
				//王朝阳修改,当修改自己的类型时,名字名可以不变
				if (orgs[i].getOrgID() == org.getOrgID()) 
					continue;
				if (orgs[i].getName().equals(org.getName()))
					throw new E5Exception("the same org name on the parent org.");
			}
		BaseDAO dao = new BaseDAO();
		try {
			dao.update(org);
		} catch (Exception e) {
			throw new E5Exception(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgManager#delete(int, boolean)
	 */
	public void delete(int orgID, boolean cascade) throws E5Exception {
		BaseDAO dao = new BaseDAO();
		Session session = null;
		Transaction tx = null;

		try {
			session = dao.getSession();
			tx = session.beginTransaction();
			if (cascade) 
			{
				delete(orgID, session);
				DAOHelper.delete("delete from com.founder.e5.sys.org.Org as org where org.OrgID = :orgID",
								new Integer(orgID), Hibernate.INTEGER, session);
				//	 2006-05-08 删除用户和角色
				DAOHelper.delete("delete from com.founder.e5.sys.org.User as user where user.OrgID = :orgID",
								new Integer(orgID), Hibernate.INTEGER, session);
				DAOHelper.delete("delete from com.founder.e5.sys.org.Role as role where role.OrgID = :orgID",
								new Integer(orgID), Hibernate.INTEGER, session);
			} 
			else 
			{
				int parentID = ((Integer) (DAOHelper.find("select org.ParentID from com.founder.e5.sys.org.Org as org where org.OrgID = :orgID",
								new Integer(orgID), Hibernate.INTEGER, session)
						.get(0))).intValue();
				List list = DAOHelper.find("select org from com.founder.e5.sys.org.Org as org where org.ParentID = :orgID",
								new Integer(orgID), Hibernate.INTEGER, session);
				for (int i = 0; i < list.size(); i++) {
					Org org = (Org) list.get(i);
					org.setParentID(parentID);
					session.update(org);
				}
				//王朝阳增加 2006-05-09 把角色和用户的机构设成府机构                
				Role[] roles = getRolesByOrg(orgID);
				for (int i = 0; i < roles.length; i++) {
					roles[i].setOrgID(parentID);
					session.update(roles[i]);
				}

				UserManager userMgr = OrgImplFactory.getUserManager();
				User[] users = userMgr.getUsersByOrg(orgID);
				for (int i = 0; i < users.length; i++) {
					users[i].setOrgID(parentID);
					session.update(users[i]);
				}

				DAOHelper.delete("delete from com.founder.e5.sys.org.Org as org where org.OrgID = :orgID",
								new Integer(orgID), Hibernate.INTEGER, session);
			}
			tx.commit();
		} catch (Exception e) {
			ResourceMgr.rollbackQuietly(tx);
			throw new E5Exception("delete with childrens error.", e);
		} finally {
			ResourceMgr.closeQuietly(session);
		}
	}

	/**
	 * 递归删除组织
	 * @param orgID 组织ID
	 * @param session hibernate
	 * @throws E5Exception
	 */
	private void delete(int orgID, Session session) throws E5Exception {
		try {
			List list = DAOHelper.find("from com.founder.e5.sys.org.Org as org where org.ParentID = :pID",
							new Integer(orgID), Hibernate.INTEGER, session);
			for (int i = 0; i < list.size(); i++) {
				Org org = (Org) list.get(i);
				delete(org.getOrgID(), session);
				DAOHelper.delete("delete from com.founder.e5.sys.org.Org as org where org.OrgID = :orgID",
								new Integer(org.getOrgID()), Hibernate.INTEGER,
								session);
				//2006-05-08 删除用户和角色
				DAOHelper.delete("delete from com.founder.e5.sys.org.User as user where user.OrgID = :orgID",
								new Integer(org.getOrgID()), Hibernate.INTEGER,
								session);
				DAOHelper.delete("delete from com.founder.e5.sys.org.Role as role where role.OrgID = :orgID",
								new Integer(org.getOrgID()), Hibernate.INTEGER,
								session);

			}
		} catch (Exception e) {
			throw new E5Exception("delete with childrens error.", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgManager#move(int, int, boolean)
	 */
	public void move(int srcOrgID, int destOrgID, boolean cascade)
			throws E5Exception {
		BaseDAO dao = new BaseDAO();
		Session session = null;
		Transaction tx = null;
		try {
			session = dao.getSession();
			tx = session.beginTransaction();
			if (cascade) {
				Org org = (Org) dao.find("from com.founder.e5.sys.org.Org as org where org.OrgID = :orgID",
								new Integer(srcOrgID), Hibernate.INTEGER,
								session).get(0);
				org.setParentID(destOrgID);
				session.update(org);
			} else {
				List list = dao.find("from com.founder.e5.sys.org.Org as org where org.OrgID = :orgID",
								new Integer(srcOrgID), Hibernate.INTEGER,
								session);
				if (list.size() == 1) {
					Org srcOrg = (Org) list.get(0);
					int parentID = srcOrg.getParentID();
					list = dao.find("from com.founder.e5.sys.org.Org as org where org.ParentID = :pID",
									new Integer(srcOrgID), Hibernate.INTEGER,
									session);
					for (int i = 0; i < list.size(); i++) {
						Org org = (Org) list.get(i);
						org.setParentID(parentID);
						session.update(org);
					}
					srcOrg.setParentID(destOrgID);
					session.update(srcOrg);
				}
			}
			tx.commit();
		} catch (Exception e) {
			ResourceMgr.rollbackQuietly(tx);
			throw new E5Exception("move error.", e);
		} finally {
			ResourceMgr.closeQuietly(session);
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgReader#getChildOrgs(int)
	 */
	public Org[] getChildOrgs(int orgID) throws E5Exception {
		List orgs = new ArrayList();
		getChildOrgs(orgID, orgs);
		Org[] rtnOrgs = null;
		if (orgs.size() > 0) {
			rtnOrgs = new Org[orgs.size()];
			orgs.toArray(rtnOrgs);
		}
		return rtnOrgs;
	}

	/**
	 * 获得orgID指定组织的所有下级组织，在orgs中保存
	 * @param orgID 组织ID
	 * @param orgs  组织集合
	 * @throws E5Exception
	 */
	private void getChildOrgs(int orgID, List orgs) throws E5Exception {
		Org[] org = getNextChildOrgs(orgID);
		if (org != null)
			for (int i = 0; i < org.length; i++) {
				orgs.add(org[i]);
				getChildOrgs(org[i].getOrgID(), orgs);
			}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgReader#getRolesByOrg(int)
	 */
	public Role[] getRolesByOrg(int orgID) throws E5Exception {
		Role[] roles = null;
		List list = DAOHelper.find("select role from com.founder.e5.sys.org.Role as role where role.OrgID = :orgID order by role.OrderID",
						new Integer(orgID), Hibernate.INTEGER);
		if (list.size() > 0) {
			roles = new Role[list.size()];
			list.toArray(roles);
		}
		return roles;
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgReader#getRoles(int)
	 */
	public Role[] getRoles(int orgID) throws E5Exception {
		List roles = new ArrayList();
		List orgs = new ArrayList();
		getChildOrgs(orgID, orgs);
		if (orgID == 0) {
			Org org = new Org();
			org.setOrgID(0);
			orgs.add(org);
		} else
			orgs.add(get(orgID));

		BaseDAO dao = new BaseDAO();
		Session session = null;
		Transaction tx = null;
		try {
			session = dao.getSession();
			tx = session.beginTransaction();

			for (int i = 0; i < orgs.size(); i++) {
				Org org = (Org) orgs.get(i);
				List list = dao.find("from com.founder.e5.sys.org.Role as role where role.OrgID = :orgID order by role.OrderID",
								new Integer(org.getOrgID()), Hibernate.INTEGER,
								session);
				if (list.size() > 0)
					roles.addAll(list);
			}
			tx.commit();
		} catch (Exception e) {
			ResourceMgr.rollbackQuietly(tx);
			throw new E5Exception("[getRoles(int)]", e);
		} finally {
			dao.closeSession(session);
		}
		Role[] role = null;
		if (roles.size() > 0) {
			role = new Role[roles.size()];
			roles.toArray(role);
		}
		return role;
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgReader#getNextChildOrgs(int)
	 */
	public Org[] getNextChildOrgs(int orgID) throws E5Exception {
		Org[] orgs = null;
		//王朝阳增加对取出节点进行排序2006-02-28	    
		List list = DAOHelper.find("from com.founder.e5.sys.org.Org as org where org.ParentID = :pID order by org.OrderID",
						new Integer(orgID), Hibernate.INTEGER);
		if (list.size() > 0) {
			orgs = new Org[list.size()];
			list.toArray(orgs);
		}
		return orgs;
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgReader#getParentOrg(int)
	 */
	public Org getParentOrg(int orgID) throws E5Exception {
		BaseDAO dao = new BaseDAO();
		Org org = null;
		List list = dao.find("select org from com.founder.e5.sys.org.Org as org where org.OrgID = :orgID",
						new Integer(orgID), Hibernate.INTEGER);
		if (list.size() == 1) {
			org = (Org) list.get(0);
			if (org.getParentID() == 0)
				return null;

			list = dao.find("select org from com.founder.e5.sys.org.Org as org where org.OrgID = :orgID",
							new Integer(org.getParentID()), Hibernate.INTEGER);
			if (list.size() == 1)
				return (Org) list.get(0);
			else
				return null;
		} else
			throw new E5Exception("the org not exist.");
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgManager#sortOrg(int[])
	 */
	public void sortOrg(int[] orgIDs) throws E5Exception {
		BaseDAO dao = new BaseDAO();
		Session session = null;
		Transaction tx = null;
		try {
			session = dao.getSession();
			tx = session.beginTransaction();
			if (orgIDs != null)
				for (int i = 0; i < orgIDs.length; i++) {
					Org org = (Org) DAOHelper.find("select org from com.founder.e5.sys.org.Org as org where org.OrgID = :orgID",
									new Integer(orgIDs[i]), Hibernate.INTEGER,
									session).get(0);
					org.setOrderID(i + 1);
					session.update(org);
				}
			tx.commit();
		} catch (Exception e) {
			ResourceMgr.rollbackQuietly(tx);
			throw new E5Exception("order orgs error.", e);
		} finally {
			ResourceMgr.closeQuietly(session);
		}
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgReader#getOrgByType(int, java.lang.String)
	 */
	public Org getOrgByType(int userID, String typeName) throws E5Exception {
		OrgTypeReader reader = (OrgTypeReader) Context.getBean(OrgTypeReader.class);
		String typeID = reader.getTypeID(typeName);
		Org org = OrgImplFactory.getUserManager().getParentOrg(userID);
		return getOrgByType(org.getOrgID(), Integer.parseInt(typeID));
	}

	/**
	 * 获得指定组织的指定类型的父组织
	 * @param orgID  组织ID
	 * @param typeID 组织类型ID
	 * @return   组织
	 * @throws E5Exception
	 */
	private Org getOrgByType(int orgID, int typeID) throws E5Exception {
		Org org = getParentOrg(orgID);
		if (org != null)
			if (org.getType() == typeID)
				return org;
			else
				return getOrgByType(org.getOrgID(), typeID);
		return null;
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgReader#getOrgByType(java.lang.String, java.lang.String)
	 */
	public Org getOrgByType(String userCode, String typeName)
			throws E5Exception {
		OrgTypeReader reader = (OrgTypeReader) Context.getBean(OrgTypeReader.class);
		String typeID = reader.getTypeID(typeName);
		Org org = OrgImplFactory.getUserManager().getParentOrg(userCode);
		return getOrgByType(org.getOrgID(), Integer.parseInt(typeID));
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgReader#getOrgsByName(java.lang.String)
	 */
	public Org[] getOrgsByName(String orgName) throws E5Exception {
		Org[] orgs = null;
		List list = DAOHelper.find("from com.founder.e5.sys.org.Org as org where org.Name = :orgName",
						orgName, Hibernate.STRING);
		orgs = new Org[list.size()];
		list.toArray(orgs);

		return orgs;

	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgReader#getOrgsIncludeName(java.lang.String)
	 */
	public Org[] getOrgsIncludeName(String orgName) throws E5Exception {
		Org[] orgs = null;
		String wrapOrgName = "%" + orgName + "%";
		List list = DAOHelper.find("from com.founder.e5.sys.org.Org as org where org.Name like :wrapOrgName",
						wrapOrgName, Hibernate.STRING);
		orgs = new Org[list.size()];
		list.toArray(orgs);

		return orgs;
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgReader#getDefaultFolders(int)
	 */
	public DefaultFolder[] getDefaultFolders(int orgID) throws E5Exception {
		return DefaultFolderHelper.getDefaultFolders(orgID, 0);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgReader#getDefaultFolder(int, int)
	 */
	public DefaultFolder getDefaultFolder(int orgID, int docTypeID)
	throws E5Exception {
		return DefaultFolderHelper.getDefaultFolder(orgID, 0, docTypeID);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgReader#getDefaultFolders()
	 */
	public DefaultFolder[] getDefaultFolders() throws E5Exception {
		return DefaultFolderHelper.getDefaultFolders(0);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgManager#create(com.founder.e5.sys.org.DefaultFolder)
	 */
	public void create(DefaultFolder defaultFolder) throws E5Exception {
		DefaultFolderHelper.create(defaultFolder);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgManager#update(com.founder.e5.sys.org.DefaultFolder)
	 */
	public void update(DefaultFolder defaultFolder) throws E5Exception {
		DefaultFolderHelper.update(defaultFolder);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgManager#delete(com.founder.e5.sys.org.DefaultFolder)
	 */
	public void delete(DefaultFolder defaultFolder) throws E5Exception {
		DefaultFolderHelper.delete(defaultFolder);
	}
}