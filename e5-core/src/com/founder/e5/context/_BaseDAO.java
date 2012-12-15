package com.founder.e5.context;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;

import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;
import org.hibernate.type.Type;

/**
 * Hibernate操作调用的基类
 * <BR>除了sessionFactory需要在继承类中按配置文件名获取后设置进来，其他基本的方法都有封装
 * 
 * <BR>该类的原型从Hibernate Sychronizer中得到
 * <BR>进行了较大的改动
 * <li>原来的用ThreadLocal保存session的方式有问题，不能进行session的交叉使用。因此
 * 		废弃这种方式，getSession时直接new出新的session
 * <li>配置文件初始化的方式有修改，在继承类中做处理
 * <li>每个继承类只对应一个Hibernate配置文件，
 * 		若增加一个配置文件，需要增加一个继承类，设置不同的configFile
 * <li>增加一些方法的包装
 * 
 * @Created on 2005-7-14
 * @author Gong Lijie
 * @version 2.0
 */
public abstract class _BaseDAO {

	protected static Map sessionFactoryMap = new HashMap();

	/**
	 * 用配置文件进行Hibernate的SessionFactory初始化
	 * <BR>该方法在每个继承类初始化时做一次即可
	 * <BR>多次调用不会增加负担，因为内部有判断
	 * @return Configuration 返回配置对象，子类中可能有用
	 */
	protected static synchronized Configuration init(String configFile)
	throws HibernateException 
	{		
		//若配置文件空
		if ((configFile == null) && (sessionFactoryMap.size() > 0)) 
			return null;
		else if (sessionFactoryMap.get(configFile) != null) 
			return null;
		else {
			Configuration cfg = new Configuration();
			if (configFile == null)
				cfg.configure();
			else
				cfg.configure(configFile);
			setSessionFactory(configFile, cfg.buildSessionFactory());
			return cfg;
		}
	}
	/**
	 * 用配置文件进行Hibernate的SessionFactory初始化
	 * <BR>该方法在每个继承类初始化时做一次即可
	 * <BR>多次调用不会增加负担，因为内部有判断
	 * @return Configuration 返回配置对象，子类中可能有用
	 */
	protected static synchronized Configuration init(File configFile)
	throws HibernateException 
	{		
		String fileName = configFile.getPath();
		
		//若配置文件空
		if ((configFile == null) && (sessionFactoryMap.size() > 0)) 
			return null;
		else if (sessionFactoryMap.get(fileName) != null) 
			return null;
		else {
			Configuration cfg = new Configuration();
			if (configFile == null)
				cfg.configure();
			else
				cfg.configure(configFile);
			setSessionFactory(fileName, cfg.buildSessionFactory());
			return cfg;
		}
	}
	
	/**
	 * 在使用_BaseDAO之前，要先做setSessionFactory
	 * Set the session factory
	 */
	public static void setSessionFactory (String configFileName, SessionFactory sessionFactory) {
		sessionFactoryMap.put(configFileName, sessionFactory);
	}

	/**
	 * Return the SessionFactory that is to be used by these DAOs.  Change this
	 * and implement your own strategy if you, for example, want to pull the SessionFactory
	 * from the JNDI tree.
	 * <BR>取SessionFactory
	 * <BR>按各自继承类实现的getConfigurationFileName，取不同的配置名对应的SessionFactory
	 */
	public SessionFactory getSessionFactory() throws HibernateException {
		return getSessionFactory (getConfigureFile());
	}

	private SessionFactory getSessionFactory(String configFile) throws HibernateException {
		//若只有一个，则不判断了，直接返回
		if (sessionFactoryMap.size() == 1) 
			return (SessionFactory) sessionFactoryMap.values().toArray()[0];
		else {
    		SessionFactory sessionFactory = (SessionFactory) sessionFactoryMap.get(configFile);
    		if (sessionFactory != null)
    			return sessionFactory;

    		if (configFile == null)
				throw new RuntimeException("The session factory has not been initialized.");
			else
				throw new RuntimeException("The session factory for '" + configFile + "' has not been initialized.");
		}
	}

	/**
	 * 获取一个session
	 * 注意用完关闭
	 * @return
	 * @throws HibernateException
	 */
	public Session getSession() throws HibernateException {
		return getSessionFactory(getConfigureFile()).openSession();
	}

	/**
	 * 配置文件名
	 * 继承类需要重新实现此方法
	 */
	protected abstract String getConfigureFile () ;

	/**
	 * Close the session
	 */
	public void closeSession (Session s) throws HibernateException {
		if (s != null) s.close();
	}

	/**
	 * Begin the transaction related to the session
	 */
	public Transaction beginTransaction(Session s) throws HibernateException {
		return s.beginTransaction();
	}

	/**
	 * Commit the given transaction
	 */
	public void commitTransaction(Transaction t) throws HibernateException {
		t.commit();
	}

	/**
	 * Execute a query. 
	 * @param query a query expressed in Hibernate's query language
	 * @return a distinct list of instances (or arrays of instances)
	 */
	public java.util.List find(String query) throws HibernateException {
		Session s = null;
		try {
			s = getSession();
			return find(query, s);
		} finally {
			closeSession(s);
		}
	}

	/**
	 * Perform a find but use the session given instead of creating a new one.
	 * @param query a query expressed in Hibernate's query language
	 * @s the Session to use
	 */
	public java.util.List find(String query, Session s) throws HibernateException {
		return s.createQuery(query).list();
		//return s.find(query);
	}

	/**
	 * Execute a query.
	 * @param query a query expressed in Hibernate's query language
	 * @param paramValue the parameter value
	 * @param paramType the parameter Type
	 * @return a distinct list of instances (or arrays of instances)
	 */
	public java.util.List find(String query, Object obj, Type type) throws HibernateException {
		Session s = null;
		try {
			s = getSession();
			return find(query, obj, type, s);
		}
		finally {
			closeSession(s);
		}
	}

	/**
	 * Perform a find but use the session given instead of creating a new one.
	 * @param query a query expressed in Hibernate's query language
	 * @param paramValue the parameter value
	 * @param paramType the parameter Type
	 * @param s the Session to use
	 */
	public java.util.List find(String query, Object paramValue, Type paramType, Session s) throws HibernateException {
		Query q = s.createQuery(query);
		String[] p = q.getNamedParameters();
		return q.setParameter(p[0], paramValue, paramType)
			.list();
		//return s.find(query, obj, type);
	}

	/**
	 * Execute a query.
	 * 
	 * 从Hibernate2.0升级到Hibernate3.0时本方法被迫改动 
	 * @param query HQL查找语句
	 * @param paramName HQL语句中的参数名数组
	 * @param paramValue 参数的值数组
	 * @param paramType 参数的类型数组
	 * @return a distinct list of instances (or arrays of instances)
	 */
	public java.util.List find(String query, String[] paramName, Object[] paramValue, Type[] paramType) throws HibernateException {
		Session s = null;
		try {
			s = getSession();
			return find(query, paramName, paramValue, paramType, s);
		}
		finally {
			closeSession(s);
		}
	}

	/**
	 * Perform a find but use the session given instead of creating a new one.
	 * 
	 * 从Hibernate2.0升级到Hibernate3.0时本方法被迫改动 
	 * 因为3.0后去掉了带参数(Object[], Type[])的方法，无法直接调用
	 * 不能使用Query.getNamedParameters来获取参数名称，
	 * 因为该方法返回的数组是从Map中得到的，顺序不固定
	 * @param query HQL查找语句
	 * @param paramName HQL语句中的参数名数组
	 * @param paramValue 参数的值数组
	 * @param paramType 参数的类型数组
	 * @param s the Session to use
	 */
	public List find(String query, String[] paramName, Object[] paramValue, Type[] paramType, Session s) throws HibernateException {
		Query q = s.createQuery(query);
		for (int i = 0; i < paramName.length; i++)
			q.setParameter(paramName[i], paramValue[i], paramType[i]);
		return q.list();
	}
	/**
	 * 根据给定的HQL和参数值，执行查询
	 * <br/>HQL中的参数不能命名，只能是?的形式
	 * @param query	HQL查询语句
	 * @param paramValue 参数值 顺序与在HQL中的显示顺序一致
	 * @return
	 * @throws HibernateException
	 */
	public List find(String query, Object[] paramValue) throws HibernateException {
		Session s = null;
		try {
			s = getSession();
			return find(query, paramValue, s);
		}
		finally {
			closeSession(s);
		}
	}
	/**
	 * 给定session的查询，参见find(String, Object[])
	 * @param query
	 * @param paramValue
	 * @param s
	 * @return
	 * @throws HibernateException
	 */
	public List find(String query, Object[] paramValue, Session s) throws HibernateException {
		Query q = s.createQuery(query);
		for (int i = 0; i < paramValue.length; i++)
			q.setParameter(i, paramValue[i]);
		return q.list();
	}
	/**
	 * 取某类的所有记录
	 * @param clazz 查询的类，该类必须有映射文件
	 * @param orderProperty 升序的排序列，可为null
	 * @return 查询结果列表，size有可能为0
	 * @throws HibernateException
	 */
	public java.util.List findAll (Class clazz, String orderProperty) throws HibernateException {
		Session s = null;
		try {
			s = getSession();
			return findAll(clazz, orderProperty, s);
		}
		finally {
			closeSession(s);
		}
	}

	/**
	 * 取某类的所有记录
	 * @param clazz 查询的类，该类必须有映射文件
	 * @param orderProperty 升序的排序列，可为null
	 * @param s the Session
	 * @return 查询结果列表，size有可能为0
	 * @throws HibernateException
	 */
	public java.util.List findAll (Class clazz, String orderProperty, Session s) throws HibernateException {
		Criteria crit = createCriteria(s, clazz);
		if (null != orderProperty) 
			crit.addOrder(Order.asc(orderProperty));
		return crit.list();
	}

	/**
	 * 取某类的所有记录，带多个排序列
	 * @param clazz 查询的类，该类必须有映射文件
	 * @param orderProperty 排序列数组，可null
	 * @param order 排序数组。
	 * 该数组用来指示orderProperty的排序方式，必须不小于orderProperty数组的长度。
	 * order数组中，0表示升序，其他数字表示降序
	 * @return
	 * @throws HibernateException
	 */
	public java.util.List findAll (Class clazz, String[] orderProperty, int[] order) throws HibernateException {
		Session s = null;
		try {
			s = getSession();
			return findAll(clazz, orderProperty, order, s);
		}
		finally {
			closeSession(s);
		}
	}

	/**
	 * 取某类的所有记录，带多个排序列
	 * @param clazz 查询的类，该类必须有映射文件
	 * @param orderProperty 排序列数组，可null
	 * @param order 排序数组。
	 * 该数组用来指示orderProperty的排序方式，必须不小于orderProperty数组的长度。
	 * order数组中，0表示升序，其他数字表示降序
	 * @param s the Session
	 * @return
	 * @throws HibernateException
	 */
	public java.util.List findAll (Class clazz, String[] orderProperty, int[] order, Session s) throws HibernateException {
		Criteria crit = createCriteria(s, clazz);
		if (null != orderProperty) 
		{
			for (int i = 0; i < orderProperty.length; i++)
			{
				if (order[i] == 0)
					crit.addOrder(Order.asc(orderProperty[i]));
				else
					crit.addOrder(Order.desc(orderProperty[i]));
			}
		}
		return crit.list();
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Load object matching the given key and return it.
	 */
	public Object get(Class refClass, Serializable key) throws HibernateException {
		Session s = null;
		try {
			s = getSession();
			return get(refClass, key, s);
		} finally {
			closeSession(s);
		}
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Load object matching the given key and return it.
	 */
	public Object get(Class refClass, Serializable key, Session s) throws HibernateException {
		return s.get(refClass, key);
	}

	/**
	 * Obtain an instance of Query for a named query string defined in the mapping file.
	 * @param name the name of a query defined externally 
	 * @return Query
	 */
	public java.util.List getNamedQuery(String name) throws HibernateException {
		Session s = null;
		try {
			s = getSession();
			return getNamedQuery(name, s);
		} finally {
			closeSession(s);
		}
	}

	/**
	 * Obtain an instance of Query for a named query string defined in the mapping file.
	 * Use the session given.
	 * @param name the name of a query defined externally 
	 * @param s the Session
	 * @return Query
	 */
	public java.util.List getNamedQuery(String name, Session s) throws HibernateException {
		Query q = s.getNamedQuery(name);
		return q.list();
	}

	/**
	 * Obtain an instance of Query for a named query string defined in the mapping file.
	 * Use the parameters given.
	 * @param name the name of a query defined externally 
	 * @param params the parameter array
	 * @return Query
	 */
	public java.util.List getNamedQuery(String name, Serializable[] params)
		throws HibernateException {
		Session s = null;
		try {
			s = getSession();
			return getNamedQuery(name, params, s);
		} finally {
			closeSession(s);
		}
	}

	/**
	 * Obtain an instance of Query for a named query string defined in the mapping file.
	 * Use the parameters given and the Session given.
	 * @param name the name of a query defined externally 
	 * @param params the parameter array
	 * @s the Session
	 * @return Query
	 */
	public java.util.List getNamedQuery(String name, Serializable[] params, Session s)
		throws HibernateException {
		Query q = s.getNamedQuery(name);
		if (null != params) {
			for (int i = 0; i < params.length; i++) {
				setParameterValue(q, i, params[i]);
			}
		}
		return q.list();
	}

	/**
	 * Obtain an instance of Query for a named query string defined in the mapping file.
	 * Use the parameters given.
	 * @param name the name of a query defined externally 
	 * @param params the parameter Map
	 * @return Query
	 */
	public java.util.List getNamedQuery(String name, Map params)
		throws HibernateException {
		Session s = null;
		try {
			s = getSession();
			return getNamedQuery(name, params, s);
		} finally {
			closeSession(s);
		}
	}

	/**
	 * Obtain an instance of Query for a named query string defined in the mapping file.
	 * Use the parameters given and the Session given.
	 * @param name the name of a query defined externally 
	 * @param params the parameter Map
	 * @s the Session
	 * @return Query
	 */
	public java.util.List getNamedQuery(String name, Map params, Session s)
		throws HibernateException {
		Query q = s.getNamedQuery(name);
		if (null != params) {
			for (Iterator i=params.entrySet().iterator(); i.hasNext(); ) {
				Map.Entry entry = (Map.Entry) i.next();
				setParameterValue(q, (String) entry.getKey(), entry.getValue());
			}
		}
		return q.list();
	}

	/**
	 * Return a Criteria object that relates to the DAO's table
	 */
	public Criteria createCriteria (Session s, Class clazz) throws HibernateException {
	 	return s.createCriteria(clazz);
	 }

	/**
	 * Convenience method to set paramers in the query given based on the actual object type in passed in as the value.
	 * You may need to add more functionaly to this as desired (or not use this at all).
	 * @param query the Query to set
	 * @param position the ordinal position of the current parameter within the query
	 * @param value the object to set as the parameter
	 */
	public void setParameterValue(Query query, int position, Object value) {
		if (null == value) {
			return;
		} else if (value instanceof Boolean) {
			query.setBoolean(position, ((Boolean) value).booleanValue());
		} else if (value instanceof String) {
			query.setString(position, (String) value);
		} else if (value instanceof Integer) {
			query.setInteger(position, ((Integer) value).intValue());
		} else if (value instanceof Long) {
			query.setLong(position, ((Long) value).longValue());
		} else if (value instanceof Float) {
			query.setFloat(position, ((Float) value).floatValue());
		} else if (value instanceof Double) {
			query.setDouble(position, ((Double) value).doubleValue());
		} else if (value instanceof BigDecimal) {
			query.setBigDecimal(position, (BigDecimal) value);
		} else if (value instanceof Byte) {
			query.setByte(position, ((Byte) value).byteValue());
		} else if (value instanceof Calendar) {
			query.setCalendar(position, (Calendar) value);
		} else if (value instanceof Character) {
			query.setCharacter(position, ((Character) value).charValue());
		} else if (value instanceof Timestamp) {
			query.setTimestamp(position, (Timestamp) value);
		} else if (value instanceof Date) {
			query.setDate(position, (Date) value);
		} else if (value instanceof Short) {
			query.setShort(position, ((Short) value).shortValue());
		}
	}

	/**
	 * Convenience method to set paramers in the query given based on the actual object type in passed in as the value.
	 * You may need to add more functionaly to this as desired (or not use this at all).
	 * @param query the Query to set
	 * @param key the key name
	 * @param value the object to set as the parameter
	 */
	public void setParameterValue(Query query, String key, Object value) {
		if (null == key || null == value) {
			return;
		} else if (value instanceof Boolean) {
			query.setBoolean(key, ((Boolean) value).booleanValue());
		} else if (value instanceof String) {
			query.setString(key, (String) value);
		} else if (value instanceof Integer) {
			query.setInteger(key, ((Integer) value).intValue());
		} else if (value instanceof Long) {
			query.setLong(key, ((Long) value).longValue());
		} else if (value instanceof Float) {
			query.setFloat(key, ((Float) value).floatValue());
		} else if (value instanceof Double) {
			query.setDouble(key, ((Double) value).doubleValue());
		} else if (value instanceof BigDecimal) {
			query.setBigDecimal(key, (BigDecimal) value);
		} else if (value instanceof Byte) {
			query.setByte(key, ((Byte) value).byteValue());
		} else if (value instanceof Calendar) {
			query.setCalendar(key, (Calendar) value);
		} else if (value instanceof Character) {
			query.setCharacter(key, ((Character) value).charValue());
		} else if (value instanceof Timestamp) {
			query.setTimestamp(key, (Timestamp) value);
		} else if (value instanceof Date) {
			query.setDate(key, (Date) value);
		} else if (value instanceof Short) {
			query.setShort(key, ((Short) value).shortValue());
		}
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Persist the given transient instance, first assigning a generated identifier. 
	 * (Or using the current value of the identifier property if the assigned generator is used.) 
	 */
	public Serializable save(Object obj) throws HibernateException {
		Transaction t = null;
		Session s = null;
		try {
			s = getSession();
			t = beginTransaction(s);
			Serializable rtn = save(obj, s);
			commitTransaction(t);
			return rtn;
		}
		catch (HibernateException e) {
			if (null != t) t.rollback();
            throw e;
		}
		finally {
			closeSession(s);
		}
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Persist the given transient instance, first assigning a generated identifier. 
	 * (Or using the current value of the identifier property if the assigned generator is used.) 
	 */
	public Serializable save(Object obj, Session s) throws HibernateException {
		return s.save(obj);
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Either save() or update() the given instance, depending upon the value of its
	 * identifier property.
	 */
	public void saveOrUpdate(Object obj) throws HibernateException {
		Transaction t = null;
		Session s = null;
		try {
			s = getSession();
			t = beginTransaction(s);
			saveOrUpdate(obj, s);
			commitTransaction(t);
		}
		catch (HibernateException e) {
			if (null != t) t.rollback();
            throw e;
		}
		finally {
			closeSession(s);
		}
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Either save() or update() the given instance, depending upon the value of its
	 * identifier property.
	 */
	public void saveOrUpdate(Object obj, Session s) throws HibernateException {
		s.saveOrUpdate(obj);
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * @param obj a transient instance containing updated state
	 */
	public void update(Object obj) throws HibernateException {
		Transaction t = null;
		Session s = null;
		try {
			s = getSession();
			t = beginTransaction(s);
			update(obj, s);
			commitTransaction(t);
		}
		catch (HibernateException e) {
			if (null != t) t.rollback();
            throw e;
		}
		finally {
			closeSession(s);
		}
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * @param obj a transient instance containing updated state
	 * @param s the Session
	 */
	public void update(Object obj, Session s) throws HibernateException {
		s.update(obj);
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 */
	public int delete(Object obj) throws HibernateException {
		Transaction t = null;
		Session s = null;
		try {
			s = getSession();
			t = beginTransaction(s);
			int result = delete(obj, s);
			commitTransaction(t);
			return result;
		}
		catch (HibernateException e) {
			if (null != t) t.rollback();
            throw e;
		}
		finally {
			closeSession(s);
		}
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 */
	public int delete(Object obj, Session s) throws HibernateException {
		s.delete(obj);
		return 1;
	}

	/**
	 * 不带参数的删除
	 * @param query
	 * @return
	 * @throws HibernateException
	 */
	public int delete(String query) throws HibernateException {
		Transaction t = null;
		Session s = null;
		try {
			s = getSession();
			t = beginTransaction(s);
			int result = delete(query, s);
			commitTransaction(t);
			return result;
		}
		catch (HibernateException e) {
			if (null != t) t.rollback();
            throw e;
		}
		finally {
			closeSession(s);
		}
	}
	/**
	 * 不带参数的删除
	 * @param query
	 * @param s
	 * @return
	 * @throws HibernateException
	 */
	public int delete(String query, Session s) throws HibernateException {
		return s.createQuery(query)
			.executeUpdate();
	}
	
	/**
	 * 带一个参数的删除
	 * @param query
	 * @param paramValue 参数值
	 * @param paramType 参数类型
	 * @return
	 * @throws HibernateException
	 */
	public int delete (String query, Object paramValue, Type paramType) throws HibernateException {
		Transaction t = null;
		Session s = null;
		try {
			s = getSession();
			t = beginTransaction(s);
			int result = delete(query, paramValue, paramType, s);
			commitTransaction(t);
			return result;
		}
		catch (HibernateException e) {
			if (null != t) t.rollback();
            throw e;
		}
		finally {
			closeSession(s);
		}
	}
	/**
	 * 带一个参数的删除
	 * @param query
	 * @param paramValue
	 * @param paramType
	 * @param s
	 * @return
	 * @throws HibernateException
	 */
	public int delete (String query, Object paramValue, Type paramType, Session s) throws HibernateException {
		Query q = s.createQuery(query);
		String[] p = q.getNamedParameters();
		return q.setParameter(p[0], paramValue, paramType)
			.executeUpdate();
	}

	/**
	 * 带多个参数的删除。HQL语句中的参数是带命名的格式。
	 * 从Hibernate2.0升级到Hibernate3.0时本方法被迫改动
	 * 因为3.0后去掉了带参数(Object[], Type[])的方法，无法直接调用
	 * @param query HQL删除语句
	 * @param paramName HQL语句中的参数名数组
	 * @param paramValue 参数的值数组
	 * @param paramType 参数的类型数组
	 * @throws HibernateException
	 */
	public int delete (String query, String[] paramName, Object[] paramValue, Type[] paramType) throws HibernateException {
		Transaction t = null;
		Session s = null;
		try {
			s = getSession();
			t = beginTransaction(s);
			int result = delete(query, paramName, paramValue, paramType, s);
			commitTransaction(t);
			return result;
		}
		catch (HibernateException e) {
			if (null != t) t.rollback();
            throw e;
		}
		finally {
			closeSession(s);
		}
	}
	/**
	 * 带多个参数的SQL删除
	 * 
	 * 从Hibernate2.0升级到Hibernate3.0时本方法被迫改动 
	 * 因为3.0后去掉了带参数(Object[], Type[])的方法，无法直接调用
	 * 不能使用Query.getNamedParameters来获取参数名称，
	 * 因为该方法返回的数组是从Map中得到的，顺序不固定
	 * @param query HQL删除语句
	 * @param paramName HQL语句中的参数名数组
	 * @param paramValue 参数的值数组
	 * @param paramType 参数的类型数组
	 * @param s
	 * @throws HibernateException
	 */
	public int delete (String query, String[] paramName, Object[] paramValue, Type[] paramType, Session s) throws HibernateException {
		Query q = s.createQuery(query);
		for (int i = 0; i < paramName.length; i++)
			q.setParameter(paramName[i], paramValue[i], paramType[i]);
		return q.executeUpdate();
	}
	
	/**
	 * 带多个参数的删除。HQL中的参数都是不带名称的?格式
	 * @param query
	 * @param paramValue
	 * @return
	 * @throws HibernateException
	 */
	public int delete (String query, Object[] paramValue) throws HibernateException {
		Transaction t = null;
		Session s = null;
		try {
			s = getSession();
			t = beginTransaction(s);
			int result = delete(query, paramValue, s);
			commitTransaction(t);
			return result;
		}
		catch (HibernateException e) {
			if (null != t) t.rollback();
            throw e;
		}
		finally {
			closeSession(s);
		}
	}
	/**
	 * 带多个参数的删除。HQL中的参数都是不带名称的?格式
	 * @param query
	 * @param paramValue
	 * @param s
	 * @return
	 * @throws HibernateException
	 */
	public int delete (String query, Object[] paramValue, Session s) throws HibernateException {
		Query q = s.createQuery(query);
		for (int i = 0; i < paramValue.length; i++)
			q.setParameter(i, paramValue[i]);
		return q.executeUpdate();
	}
	/**
	 * Used by the base DAO classes but here for your modification
	 * Re-read the state of the given instance from the underlying database. It is inadvisable to use this to implement
	 * long-running sessions that span many business tasks. This method is, however, useful in certain special circumstances.
	 */
	public void refresh(Object obj, Session s) throws HibernateException {
		s.refresh(obj);
	}
}