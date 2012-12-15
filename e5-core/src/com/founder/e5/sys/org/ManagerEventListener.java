package com.founder.e5.sys.org;

import com.founder.e5.context.E5Exception;

/**
 * <p>管理类事件的监听器</p>
 * <p>用于管理类增加、修改、删除事件的监听,用于对这些功能的扩展支持</p>
 * 需要每个Manager提供注册Listener的方法,并实现事件的调用
 * 
 * 事务的控制需要管理类调用Listener时实现
 * 
 * @author wanghc
 * @created 2008-7-22 下午02:32:12
 * @see com.founder.e5.sys.org.UserManager
 * @see com.founder.e5.sys.org.UserManagerImpl
 */
public interface ManagerEventListener
{
	/**
	 * 创建事件
	 */
	public static final int EVENT_CREATE = 1;
	/**
	 * 更新事件
	 */
	public static final int EVENT_UPDATE = 2;
	/**
	 * 删除事件
	 */
	public static final int EVENT_DELETE = 3;
	
	/**
	 * 处理事件
	 * 
	 * @param event - 见EVENT_XXX  常量
	 * @param object- 操作的对象
	 * @throws E5Exception
	 */
	void doEvent(int event,Object object) throws E5Exception;
}
