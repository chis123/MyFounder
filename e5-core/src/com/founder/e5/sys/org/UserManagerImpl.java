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

/**
 * @version 1.0
 * @created 11-七月-2005 13:13:40
 */
class UserManagerImpl implements UserManager {

	private ManagerEventListener listener = null;
	
	public UserManagerImpl(){

	}

	/**
	 * 添加用户
	 * 
	 * @param user  要创建的用户对象
	 */
	public void create(User user) throws E5Exception{
	    if(user.getUserCode() == null || user.getUserCode().trim().equals(""))
	        throw new E5Exception("User Code is Null.");
	    if(user.getUserName() == null || user.getUserName().trim().equals(""))
	        throw new E5Exception("User Name is Null.");
	    if(getUserByCode(user.getUserCode()) != null)
	        throw new E5Exception("The Same User Code exist.");
	    
	    BaseDAO dao = new BaseDAO();
	    Session s   = null;
	    Transaction t = null;
   	    String plainText = user.getUserPassword();
	    try
        {
	        int orderID = getMaxOrderID(user.getOrgID());
	        user.setOrderID(orderID + 1);
	        int userID = (int)EUID.getID("UserID");
		    
            user.setUserID(userID);
            
            s = dao.getSession();
            t = dao.beginTransaction(s);
            
            //数据库中存储的是密文
            dao.save(user,s);
                        
            //create event(需要传递明文密码)
            this.doEvent(ManagerEventListener.EVENT_CREATE, user);
            
            //加密password
            user.setUserPassword(UserEncrypt.getInstance().encrypt(plainText));
            
            //commit 
            t.commit();
        }
        catch (Exception e)
        {
        	if(t != null) t.rollback();
        	
        	if(e instanceof E5Exception) throw (E5Exception)e;
        	
            throw new E5Exception("Create User Error.", e);
        }
        finally{
            //user对应继续存储明文
            user.setUserPassword(plainText);
            
        	dao.closeSession(s);
        }
	}
	
	
	/**
	 * 获取指定组织ID下所有用户中最大的排序ID
	 * @param orgID 组织ID
	 * @return 最大的排序ID
	 * @throws E5Exception
	 */
	private int getMaxOrderID(int orgID) throws E5Exception
	{
	    int maxOrderID = 1;
        List list = DAOHelper.find("select max(user.OrderID) from com.founder.e5.sys.org.User as user where user.OrgID = :orgID", 
        		new Integer(orgID), Hibernate.INTEGER);
        if(list.size() == 1)
        {
        	try
        	{
        		maxOrderID = ((Integer)list.get(0)).intValue();
        	}
        	catch(Exception ex)
        	{
//如果数据库记录为空，查询存有返回值，则转换会失败。
//王朝阳修改2006-2-24        		
        	}
        }
          return maxOrderID;
	}

//王朝阳增加，为了判断一个机构节点下是否有用户节点 2006-3-10
	public int getUserCountByOrg(int orgID) throws E5Exception
	{
	    int count = 0;
	    List list = DAOHelper.find("select count(*) from com.founder.e5.sys.org.User as user where user.OrgID = :orgID", 
	    		new Integer(orgID), Hibernate.INTEGER);
	    if(list.size() == 1)
	    {
	    	try
	    	{
	    		count = ((Integer)list.get(0)).intValue();
	    	}
	    	catch(Exception ex)
	    	{
	    	}
	    }
	      return count;
	}
	/**
	 * 获得所有的用户
	 * @return User对象的数组
	 */
	public User[] getUsers() throws E5Exception{
        try
        {
            BaseDAO dao = new BaseDAO();
            List list = dao.find("select user from com.founder.e5.sys.org.User as user order by user.OrgID,user.OrderID");
    		return getUsers(list);
        }
        catch (Exception e)
        {
            throw new E5Exception("", e);
        }
	}
	/**
	 * 获得用户ID指定的用户
	 * 
	 * @param userID    用户ID
	 */
	public User getUserByID(int userID) throws E5Exception{
        List list = DAOHelper.find("select a from com.founder.e5.sys.org.User as a where a.UserID = :userID", 
        		new Integer(userID), Hibernate.INTEGER);
        if(list.size() > 0)
            return this.decrypt(list.get(0));
        else
            return null;
	}

	/**
	 * 更新用户信息
	 * 
	 * @param user    用户对象
	 */
	public void update(User user) throws E5Exception{
	    User user1 = getUserByID(user.getUserID());
	    if( user1 == null)
	        throw new E5Exception("the updated user not exist.");
	    if(user1.getUserID() != user.getUserID())
	        throw new E5Exception("cannt update user code.");
	    
	    BaseDAO dao = new BaseDAO();
	    Session s   = null;
	    Transaction t = null;
        String plainText = user.getUserPassword();
	    try
        {            
            s = dao.getSession();
            t = dao.beginTransaction(s);
            
            //数据库中存储的是密文
            dao.update(user,s);
                      
            //update event（需要密码明文）
            this.doEvent(ManagerEventListener.EVENT_UPDATE, user);
            
            //加密password
            user.setUserPassword(UserEncrypt.getInstance().encrypt(plainText));
            
            t.commit();
        }
        catch (Exception e)
        {
        	if(t != null ) t.rollback();
        	
        	if(e instanceof E5Exception ) throw (E5Exception)e;
        	
            throw new E5Exception("update user error.", e);
        }
        finally
        {
            //user对应继续存储明文
            user.setUserPassword(plainText);
            
        	dao.closeSession(s);
        }
	}

	/**
	 * 删除用户
	 * 
	 * @param userID    用户ID
	 */
	public void delete(int userID) throws E5Exception{
		User user = this.getUserByID(userID);
		
		DAOHelper.delete("delete from com.founder.e5.sys.org.User as user where user.UserID = :userID", 
        		   new Integer(userID), Hibernate.INTEGER);
		
        //delete event
        this.doEvent(ManagerEventListener.EVENT_DELETE, user);
	}

	/**
	 * 获得用户代码指定的用户
	 * 
	 * @param userCode    用户编码
	 */
	public User getUserByCode(String userCode) throws E5Exception{
        List list = DAOHelper.find("select a from com.founder.e5.sys.org.User as a where a.UserCode = :userCode", 
        		userCode, Hibernate.STRING);
        if(list.size() > 0)
            return decrypt(list.get(0));
        else
            return null;
	}

	/**
	 * 创建用户文件夹
	 * 
	 * @param userFolder    创建用户文件夹
	 */
	public void create(UserFolder userFolder) throws E5Exception{
	    try
        {
            BaseDAO dao = new BaseDAO();
            dao.save(userFolder);
        }
        catch (Exception e)
        {
            throw new E5Exception("create user folder error.", e);
        }
	}

//	王朝阳修改把users初始化成null
	/**
	 * 获得组织下的所有用户
	 * @param orgID    组织ID
	 */
	public User[] getUsersByOrg(int orgID) throws E5Exception{
        List list = DAOHelper.find("select user from com.founder.e5.sys.org.User as user where user.OrgID = :orgID order by user.OrderID", 
        		new Integer(orgID), Hibernate.INTEGER);
		return getUsers(list);
	}

	/**
	 * 获得角色下的所有用户
	 * 
	 * @param roleID    角色ID
	 */
	public User[] getUsersByRole(int roleID) throws E5Exception{
        List list = DAOHelper.find("select user from com.founder.e5.sys.org.User as user, com.founder.e5.sys.org.UserRole as userrole where user.UserID = userrole.UserID and userrole.RoleID = :roleID", 
        		new Integer(roleID), Hibernate.INTEGER);
		return getUsers(list);
	}

	/**
	 * 修改用户文件夹
	 * 
	 * @param userFolder    修改用户文件夹
	 */
	public void update(UserFolder userFolder) throws E5Exception{
	    try
        {
            BaseDAO dao = new BaseDAO();
            dao.update(userFolder);
        }
        catch (Exception e)
        {
            throw new E5Exception("update user folder error.", e);
        }
	}

	/**
	 * 删除用户文件夹
	 * 
	 * @param userFolder    用户文件夹对象
	 */
	public void delete(UserFolder userFolder) throws E5Exception{
	    try
        {
            BaseDAO dao = new BaseDAO();
            dao.delete(userFolder);
        }
        catch (Exception e)
        {
            throw new E5Exception("delete user folder error.", e);
        }
	}

	/**
	 * 获得指定用户的所有用户文件夹
	 * 
	 * @param userID    用户ID
	 */
	public UserFolder[] getUserFolders(int userID) throws E5Exception{
        List list = DAOHelper.find("from com.founder.e5.sys.org.UserFolder as uf where uf.UserID = :userID", 
        		new Integer(userID), Hibernate.INTEGER);
        UserFolder[] userFolders = new UserFolder[list.size()];
        list.toArray(userFolders);
        return userFolders;
	}

	/**
	 * 获得指定用户ID，指定文档类型下的用户文件夹
	 * 
	 * @param docTypeID    文档类型ID
	 * @param userID    用户ID
	 */
	public UserFolder[] getUserFolders(int userID, int docTypeID) throws E5Exception{
        List list = DAOHelper.find("from com.founder.e5.sys.org.UserFolder as uf where uf.UserID = :userID and uf.DocTypeID = :docTypeID",
        		new String[]{"userID", "docTypeID"},
        		new Object[] {new Integer(userID), new Integer(docTypeID)}, 
        		new Type[] {Hibernate.INTEGER, Hibernate.INTEGER});
        UserFolder[] userFolders = new UserFolder[list.size()];
        list.toArray(userFolders);
        return userFolders;
	}

	/**
	 * 王朝阳增加 2006-4-19
	 * 获得所有用户文件夹
	 * 
	 */
	public UserFolder[] getUserFolders() throws E5Exception
	{
        List list = DAOHelper.find("from com.founder.e5.sys.org.UserFolder as uf");
        UserFolder[] userFolders = new UserFolder[list.size()];
        list.toArray(userFolders);
        return userFolders;
		
	}

	/**
	 * 对用户ID数组指定的用户进行排序
	 * 
	 * @param userID 用户ID数组
	 */
	public void sortUsers(int[] userID) throws E5Exception{
	    BaseDAO dao = new BaseDAO();
	    Session session = null;
	    Transaction t = null;
	    try
        {
	        session = dao.getSession();
	        t = dao.beginTransaction(session);
            if(userID != null)
                for(int i = 0; i < userID.length; i++)
                {
                    User user = (User)dao.find("select user from com.founder.e5.sys.org.User as user where user.UserID = :userID", 
                    		new Integer(userID[i]), Hibernate.INTEGER, session).get(0);
                    user.setOrderID(i + 1);
                    dao.update(user, session);
                }
            t.commit();
        }
        catch (Exception e)
        {
            ResourceMgr.rollbackQuietly(t);
            throw new E5Exception("order users error.", e);
        }
        finally
        {
            ResourceMgr.closeQuietly(session);
        }
	}

	/**
	 * 获得指定用户ID，指定文档库下的用户文件夹
	 * 
	 * @param userID    用户ID
	 * @param libID    文档库ID
	 */
	public UserFolder getUserFolder(int userID, int libID) throws E5Exception{
        List list = DAOHelper.find("from com.founder.e5.sys.org.UserFolder as uf where uf.UserID = :userID and uf.LibID = :libID",
        		new String[]{"userID", "libID"},
        		new Object[] {new Integer(userID), new Integer(libID)}, 
        		new Type[] {Hibernate.INTEGER, Hibernate.INTEGER});
        if(list.size() == 1)
            return (UserFolder)list.get(0);
        else if(list.size()>1)
        {
            throw new E5Exception("user folder in a lib have to manay.");
        }
        else
            return null;
	}
	
	/**
	 * 获得指定用户的父组织
	 * 
	 * @param userID    用户ID
	 */
	public Org getParentOrg(int userID) throws E5Exception{
        List list = DAOHelper.find("select org from com.founder.e5.sys.org.Org as org, com.founder.e5.sys.org.User as user where user.OrgID = org.OrgID and user.UserID = :userID", 
        		new Integer(userID), Hibernate.INTEGER);
        if(list.size() == 1)
            return (Org)list.get(0);
        else
            return null;
	}
	
	/**
	 * 获得指定用户的父组织
	 * @param userCode    用户代码
	 * 
	 */
	public Org getParentOrg(String userCode) throws E5Exception
	{
        List list = DAOHelper.find("select org from com.founder.e5.sys.org.Org as org, com.founder.e5.sys.org.User as user where user.OrgID = org.OrgID and user.UserCode = :userCode", 
        		userCode, Hibernate.STRING);
        if(list.size() == 1)
            return (Org)list.get(0);
        else
            return null;
	}

//	王朝阳增加，实现用户查询 2006-3-8
	/*
	 * 根据用户名获取对应的用户
	 * 
	 * 
	 */
	 public User [] getUsersByName(String userName) throws E5Exception
	 {
	        List list = DAOHelper.find("from com.founder.e5.sys.org.User as user where user.UserName=:userName or user.UserCode=:userCode", 
		    		new String[]{"userName", "userCode"},
		    		new Object[] {userName, userName}, 
		    		new Type[] {Hibernate.STRING, Hibernate.STRING});

    		return getUsers(list);
	 }

	// 王朝阳增加，实现机构角色 2006-3-8
	/*
	 * 根据机构名中的某些词获取对应的角色
	 */
	 public User[] getUsersIncludeName(String userName) throws E5Exception {
		String wrapUserName = "%" + userName + "%";
		List list = DAOHelper.find("from com.founder.e5.sys.org.User as user where user.UserName like :wrapUserName or user.UserCode like :wrapUserCode",
	    		new String[]{"wrapUserName", "wrapUserCode"},
	    		new Object[] {wrapUserName, wrapUserName}, 
	    		new Type[] {Hibernate.STRING, Hibernate.STRING});
		return getUsers(list);
	}
	private User decrypt(Object _user){
		User user = (User)_user;
		if(UserEncrypt.getInstance().isDecryptText(user.getUserPassword()))
			user.setUserPassword(UserEncrypt.getInstance().decrypt(user.getUserPassword()));
		return user;
	}
	private User[] getUsers(List list)
	{
		if (list == null) return null;
		
		User[] users = new User[list.size()];
		list.toArray(users);
		
	    for (int i = 0; i < users.length; i++) {
			decrypt(users[i]);
		}
        
		return users;
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.sys.org.UserManager#setListener(com.founder.e5.sys.org.UserManagerListener)
	 */
	public void setListener(ManagerEventListener listener){
		this.listener = listener;
	}
	
	/**
	 * 处理用户管理的事件,设置了listener时有效
	 * 可用于与其他系统用户同步
	 */
	private void doEvent(int event,Object object) 
	throws E5Exception
	{
		if(this.listener != null){
			this.listener.doEvent(event, object);
		}
	}
}