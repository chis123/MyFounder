package com.founder.e5.sys.org;

import com.founder.e5.context.E5Exception;

/**
 * @version 1.0
 * @created 11-ÆßÔÂ-2005 13:15:26
 */
class OrgReaderImpl implements OrgReader {

	private OrgManager orgManager = OrgRoleUserHelper.getOrgManager();

	/**
	 * @param orgID    orgID
	 * @throws E5Exception
	 */
	public Org getOrg(int orgID) throws E5Exception {
		OrgRoleUserCache cache = OrgRoleUserHelper.getCache();
		if (cache != null) {
			return cache.getOrg(orgID);
		}
		return orgManager.get(orgID);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgReader#getParentOrg(int)
	 */
	public Org getParentOrg(int orgID) throws E5Exception {

		OrgRoleUserCache cache = OrgRoleUserHelper.getCache();
		if (cache != null) {
			return cache.getParentOrg(orgID);
		}

		return orgManager.getParentOrg(orgID);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgReader#getOrgByType(int, java.lang.String)
	 */
	public Org getOrgByType(int userID, String typeName) throws E5Exception {
		return orgManager.getOrgByType(userID, typeName);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgReader#getOrgByType(java.lang.String, java.lang.String)
	 */
	public Org getOrgByType(String userCode, String typeName)
			throws E5Exception {
		return orgManager.getOrgByType(userCode, typeName);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgReader#getNextChildOrgs(int)
	 */
	public Org[] getNextChildOrgs(int orgID) throws E5Exception {
		OrgRoleUserCache cache = OrgRoleUserHelper.getCache();
		if (cache != null) {
			return cache.getNextChildOrgs(orgID);
		}

		return orgManager.getNextChildOrgs(orgID);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgReader#get()
	 */
	public Org[] get() throws E5Exception {
		OrgRoleUserCache cache = OrgRoleUserHelper.getCache();
		if (cache != null) {
			return cache.getOrgs();
		}

		return orgManager.get();
	}

	/* £¨·Ç Javadoc£©
	 * @see com.founder.e5.sys.org.OrgReader#get(int)
	 */
	public Org get(int orgID) throws E5Exception {
		return getOrg(orgID);
	}

	/* £¨·Ç Javadoc£©
	 * @see com.founder.e5.sys.org.OrgReader#getChildOrgs(int)
	 */
	public Org[] getChildOrgs(int orgID) throws E5Exception {
		OrgRoleUserCache cache = OrgRoleUserHelper.getCache();
		if (cache != null) {
			return cache.getChildOrgs(orgID);
		}
		return orgManager.getChildOrgs(orgID);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgReader#getRolesByOrg(int)
	 */
	public Role[] getRolesByOrg(int orgID) throws E5Exception {
		OrgRoleUserCache cache = OrgRoleUserHelper.getCache();
		if (cache != null) {
			return cache.getRolesByOrg(orgID);
		}
		return orgManager.getRolesByOrg(orgID);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgReader#getRoles(int)
	 */
	public Role[] getRoles(int orgID) throws E5Exception {
		OrgRoleUserCache cache = OrgRoleUserHelper.getCache();
		if (cache != null) {
			return cache.getRoles(orgID);
		}
		return orgManager.getRoles(orgID);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgReader#getOrgsByName(java.lang.String)
	 */
	public Org[] getOrgsByName(String orgName) throws E5Exception {
		return orgManager.getOrgsByName(orgName);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgReader#getOrgsIncludeName(java.lang.String)
	 */
	public Org[] getOrgsIncludeName(String orgName) throws E5Exception {
		return orgManager.getOrgsIncludeName(orgName);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgReader#getDefaultFolders(int)
	 */
	public DefaultFolder[] getDefaultFolders(int orgID) throws E5Exception {
		OrgRoleUserCache cache = OrgRoleUserHelper.getCache();
		if (cache != null) {
			return cache.getDefaultFolders(orgID);
		}
		return orgManager.getDefaultFolders(orgID);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgReader#getDefaultFolder(int, int)
	 */
	public DefaultFolder getDefaultFolder(int orgID, int docTypeID)
			throws E5Exception {
		OrgRoleUserCache cache = OrgRoleUserHelper.getCache();
		if (cache != null) {
			return cache.getDefaultFolder(orgID, docTypeID);
		}
		return orgManager.getDefaultFolder(orgID, docTypeID);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.OrgReader#getDefaultFolders()
	 */
	public DefaultFolder[] getDefaultFolders() throws E5Exception {
		OrgRoleUserCache cache = OrgRoleUserHelper.getCache();
		if (cache != null) {
			return cache.getDefaultFolders();
		}
		return orgManager.getDefaultFolders();
	}
}