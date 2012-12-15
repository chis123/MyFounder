package com.founder.e5.sys.org;

import java.util.List;

import com.founder.e5.context.BaseDAO;
import com.founder.e5.context.DAOHelper;
import com.founder.e5.context.E5Exception;

class DefaultFolderHelper {

	private static DefaultFolder[] getDefaultFolders(String sql, Object[] params)
	throws E5Exception
	{
        List list = DAOHelper.find(sql,params);
        DefaultFolder[] folders = new DefaultFolder[list.size()];
        list.toArray(folders);
        return folders;
	}
	
	public static DefaultFolder[] getDefaultFolders(int id, int type) throws E5Exception
	{
		String sql = "from DefaultFolder as df where df.id=? and df.idType=?";
		Object[] params = new Object[]{new Integer(id), new Integer(type)};
		
		return getDefaultFolders(sql, params);
	}

	public static DefaultFolder[] getDefaultFolders(int type) throws E5Exception{
		String sql = "from DefaultFolder as df where df.idType=?";
		Object[] params = new Object[]{new Integer(type)};
		
		return getDefaultFolders(sql, params);
	}

	public static DefaultFolder getDefaultFolder(int id, int type, int docTypeID)
	throws E5Exception
	{
		String sql = "from DefaultFolder as df where df.id=? and df.idType=? and df.docTypeID=?";
		Object[] params = new Object[]{new Integer(id), new Integer(type), new Integer(docTypeID)};
		
        List list = DAOHelper.find(sql,params);
        if (list == null || list.size() == 0) return null;
        return (DefaultFolder)list.get(0);
	}

	public static void create(DefaultFolder defaultFolder) throws E5Exception{
	    try {
            BaseDAO dao = new BaseDAO();
            dao.save(defaultFolder);
        } catch (Exception e) {
            throw new E5Exception("create default folder error.", e);
        }
	}

	public static void update(DefaultFolder defaultFolder) throws E5Exception{
	    try {
            BaseDAO dao = new BaseDAO();
            dao.update(defaultFolder);
        } catch (Exception e) {
            throw new E5Exception("update default folder error.", e);
        }
	}

	public static void delete(DefaultFolder defaultFolder) throws E5Exception{
	    try {
            BaseDAO dao = new BaseDAO();
            dao.delete(defaultFolder);
        } catch (Exception e) {
            throw new E5Exception("delete default folder error.", e);
        }
	}
}
