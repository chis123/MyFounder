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
 * Hibernate�������õĻ���
 * <BR>����sessionFactory��Ҫ�ڼ̳����а������ļ�����ȡ�����ý��������������ķ������з�װ
 * 
 * <BR>�����ԭ�ʹ�Hibernate Sychronizer�еõ�
 * <BR>�����˽ϴ�ĸĶ�
 * <li>ԭ������ThreadLocal����session�ķ�ʽ�����⣬���ܽ���session�Ľ���ʹ�á����
 * 		�������ַ�ʽ��getSessionʱֱ��new���µ�session
 * <li>�����ļ���ʼ���ķ�ʽ���޸ģ��ڼ̳�����������
 * <li>ÿ���̳���ֻ��Ӧһ��Hibernate�����ļ���
 * 		������һ�������ļ�����Ҫ����һ���̳��࣬���ò�ͬ��configFile
 * <li>����һЩ�����İ�װ
 * 
 * @Created on 2005-7-14
 * @author Gong Lijie
 * @version 2.0
 */
public abstract class _BaseDAO {

	protected static Map sessionFactoryMap = new HashMap();

	/**
	 * �������ļ�����Hibernate��SessionFactory��ʼ��
	 * <BR>�÷�����ÿ���̳����ʼ��ʱ��һ�μ���
	 * <BR>��ε��ò������Ӹ�������Ϊ�ڲ����ж�
	 * @return Configuration �������ö��������п�������
	 */
	protected static synchronized Configuration init(String configFile)
	throws HibernateException 
	{		
		//�������ļ���
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
	 * �������ļ�����Hibernate��SessionFactory��ʼ��
	 * <BR>�÷�����ÿ���̳����ʼ��ʱ��һ�μ���
	 * <BR>��ε��ò������Ӹ�������Ϊ�ڲ����ж�
	 * @return Configuration �������ö��������п�������
	 */
	protected static synchronized Configuration init(File configFile)
	throws HibernateException 
	{		
		String fileName = configFile.getPath();
		
		//�������ļ���
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
	 * ��ʹ��_BaseDAO֮ǰ��Ҫ����setSessionFactory
	 * Set the session factory
	 */
	public static void setSessionFactory (String configFileName, SessionFactory sessionFactory) {
		sessionFactoryMap.put(configFileName, sessionFactory);
	}

	/**
	 * Return the SessionFactory that is to be used by these DAOs.  Change this
	 * and implement your own strategy if you, for example, want to pull the SessionFactory
	 * from the JNDI tree.
	 * <BR>ȡSessionFactory
	 * <BR>�����Լ̳���ʵ�ֵ�getConfigurationFileName��ȡ��ͬ����������Ӧ��SessionFactory
	 */
	public SessionFactory getSessionFactory() throws HibernateException {
		return getSessionFactory (getConfigureFile());
	}

	private SessionFactory getSessionFactory(String configFile) throws HibernateException {
		//��ֻ��һ�������ж��ˣ�ֱ�ӷ���
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
	 * ��ȡһ��session
	 * ע������ر�
	 * @return
	 * @throws HibernateException
	 */
	public Session getSession() throws HibernateException {
		return getSessionFactory(getConfigureFile()).openSession();
	}

	/**
	 * �����ļ���
	 * �̳�����Ҫ����ʵ�ִ˷���
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
	 * ��Hibernate2.0������Hibernate3.0ʱ���������ȸĶ� 
	 * @param query HQL�������
	 * @param paramName HQL����еĲ���������
	 * @param paramValue ������ֵ����
	 * @param paramType ��������������
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
	 * ��Hibernate2.0������Hibernate3.0ʱ���������ȸĶ� 
	 * ��Ϊ3.0��ȥ���˴�����(Object[], Type[])�ķ������޷�ֱ�ӵ���
	 * ����ʹ��Query.getNamedParameters����ȡ�������ƣ�
	 * ��Ϊ�÷������ص������Ǵ�Map�еõ��ģ�˳�򲻹̶�
	 * @param query HQL�������
	 * @param paramName HQL����еĲ���������
	 * @param paramValue ������ֵ����
	 * @param paramType ��������������
	 * @param s the Session to use
	 */
	public List find(String query, String[] paramName, Object[] paramValue, Type[] paramType, Session s) throws HibernateException {
		Query q = s.createQuery(query);
		for (int i = 0; i < paramName.length; i++)
			q.setParameter(paramName[i], paramValue[i], paramType[i]);
		return q.list();
	}
	/**
	 * ���ݸ�����HQL�Ͳ���ֵ��ִ�в�ѯ
	 * <br/>HQL�еĲ�������������ֻ����?����ʽ
	 * @param query	HQL��ѯ���
	 * @param paramValue ����ֵ ˳������HQL�е���ʾ˳��һ��
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
	 * ����session�Ĳ�ѯ���μ�find(String, Object[])
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
	 * ȡĳ������м�¼
	 * @param clazz ��ѯ���࣬���������ӳ���ļ�
	 * @param orderProperty ����������У���Ϊnull
	 * @return ��ѯ����б�size�п���Ϊ0
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
	 * ȡĳ������м�¼
	 * @param clazz ��ѯ���࣬���������ӳ���ļ�
	 * @param orderProperty ����������У���Ϊnull
	 * @param s the Session
	 * @return ��ѯ����б�size�п���Ϊ0
	 * @throws HibernateException
	 */
	public java.util.List findAll (Class clazz, String orderProperty, Session s) throws HibernateException {
		Criteria crit = createCriteria(s, clazz);
		if (null != orderProperty) 
			crit.addOrder(Order.asc(orderProperty));
		return crit.list();
	}

	/**
	 * ȡĳ������м�¼�������������
	 * @param clazz ��ѯ���࣬���������ӳ���ļ�
	 * @param orderProperty ���������飬��null
	 * @param order �������顣
	 * ����������ָʾorderProperty������ʽ�����벻С��orderProperty����ĳ��ȡ�
	 * order�����У�0��ʾ�����������ֱ�ʾ����
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
	 * ȡĳ������м�¼�������������
	 * @param clazz ��ѯ���࣬���������ӳ���ļ�
	 * @param orderProperty ���������飬��null
	 * @param order �������顣
	 * ����������ָʾorderProperty������ʽ�����벻С��orderProperty����ĳ��ȡ�
	 * order�����У�0��ʾ�����������ֱ�ʾ����
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
	 * ����������ɾ��
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
	 * ����������ɾ��
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
	 * ��һ��������ɾ��
	 * @param query
	 * @param paramValue ����ֵ
	 * @param paramType ��������
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
	 * ��һ��������ɾ��
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
	 * �����������ɾ����HQL����еĲ����Ǵ������ĸ�ʽ��
	 * ��Hibernate2.0������Hibernate3.0ʱ���������ȸĶ�
	 * ��Ϊ3.0��ȥ���˴�����(Object[], Type[])�ķ������޷�ֱ�ӵ���
	 * @param query HQLɾ�����
	 * @param paramName HQL����еĲ���������
	 * @param paramValue ������ֵ����
	 * @param paramType ��������������
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
	 * �����������SQLɾ��
	 * 
	 * ��Hibernate2.0������Hibernate3.0ʱ���������ȸĶ� 
	 * ��Ϊ3.0��ȥ���˴�����(Object[], Type[])�ķ������޷�ֱ�ӵ���
	 * ����ʹ��Query.getNamedParameters����ȡ�������ƣ�
	 * ��Ϊ�÷������ص������Ǵ�Map�еõ��ģ�˳�򲻹̶�
	 * @param query HQLɾ�����
	 * @param paramName HQL����еĲ���������
	 * @param paramValue ������ֵ����
	 * @param paramType ��������������
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
	 * �����������ɾ����HQL�еĲ������ǲ������Ƶ�?��ʽ
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
	 * �����������ɾ����HQL�еĲ������ǲ������Ƶ�?��ʽ
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