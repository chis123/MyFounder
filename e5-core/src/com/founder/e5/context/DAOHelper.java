package com.founder.e5.context;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

import com.founder.e5.db.DBSession;
import com.founder.e5.db.LaterDataTransferException;
import com.founder.e5.db.util.CloseHelper;

/**
 * ��BaseDAO�Ľ�һ����װ����̬�������������
 * @created on 2005-7-21
 * @author Gong Lijie
 */
public class DAOHelper
{
	/**
	 * ����������ɾ������
	 * @param sql
	 * @param paramValue
	 * @param paramType
	 * @throws E5Exception
	 */
	public static void delete(String sql) 
	throws E5Exception 
	{
		try
		{
			BaseDAO dao = new BaseDAO();
			dao.delete(sql);
		}
		catch (HibernateException e)
		{
			throw new E5Exception(e);
		}
	}

	/**
	 * ����������ɾ���������⴫��Session
	 * @param sql
	 * @param paramValue
	 * @param paramType
	 * @throws E5Exception
	 */
	public static void delete(String sql, Session s) 
	throws E5Exception 
	{
		try
		{
			BaseDAO dao = new BaseDAO();
			dao.delete(sql, s);
		}
		catch (HibernateException e)
		{
			throw new E5Exception(e);
		}
	}
	/**
	 * ��һ��������ɾ������
	 * @param sql
	 * @param paramValue
	 * @param paramType
	 * @throws E5Exception
	 */
	public static void delete(String sql, Object paramValue, Type paramType) 
	throws E5Exception 
	{
		try
		{
			BaseDAO dao = new BaseDAO();
			dao.delete(sql, paramValue, paramType);
		}
		catch (HibernateException e)
		{
			throw new E5Exception(e);
		}
	}

	/**
	 * ��һ��������ɾ���������⴫��Session
	 * @param sql
	 * @param paramValue
	 * @param paramType
	 * @param s
	 * @throws E5Exception
	 */
	public static void delete(String sql, Object paramValue, Type paramType, Session s) 
	throws E5Exception 
	{
		try
		{
			BaseDAO dao = new BaseDAO();
			dao.delete(sql, paramValue, paramType, s);
		}
		catch (HibernateException e)
		{
			throw new E5Exception(e);
		}
	}
	/**
	 * �����������ɾ������
	 * <p>
	 * ��Hibernate2.0������Hibernate3.0ʱ���������ȸĶ�
	 * <br/>��Ϊ3.0��ȥ���˴�����(Object[], Type[])�ķ������޷�ֱ�ӵ��� 
	 * <br/>����ʹ��Query.getNamedParameters����ȡ�������ƣ� 
	 * ��Ϊ�÷������ص������Ǵ�Map�еõ��ģ�˳�򲻹̶�
	 * </p>
	 * @param query HQLɾ�����
	 * @param paramName HQL����еĲ���������
	 * @param paramValue ������ֵ����
	 * @param paramType ��������������
	 * @throws E5Exception
	 */
	public static void delete(String sql, String[] paramName, Object[] paramValue, Type[] paramType) 
	throws E5Exception 
	{
		try
		{
			BaseDAO dao = new BaseDAO();
			dao.delete(sql, paramName, paramValue, paramType);
		}
		catch (HibernateException e)
		{
			throw new E5Exception(e);
		}
	}
	
	/**
	 * �����������ɾ������������Session
	 * <p>
	 * ��Hibernate2.0������Hibernate3.0ʱ���������ȸĶ� 
	 * <br/>��Ϊ3.0��ȥ���˴�����(Object[], Type[])�ķ������޷�ֱ�ӵ���
	 * <br/>����ʹ��Query.getNamedParameters����ȡ�������ƣ�
	 * ��Ϊ�÷������ص������Ǵ�Map�еõ��ģ�˳�򲻹̶�
	 * </p>
	 * @param query HQLɾ�����
	 * @param paramName HQL����еĲ���������
	 * @param paramValue ������ֵ����
	 * @param paramType ��������������
	 * @param s
	 * @throws E5Exception
	 */
	public static void delete(String sql, String[] paramName, Object[] paramValue, Type[] paramType, Session s) 
	throws E5Exception 
	{
		try
		{
			BaseDAO dao = new BaseDAO();
			dao.delete(sql, paramName, paramValue, paramType, s);
		}
		catch (HibernateException e)
		{
			throw new E5Exception(e);
		}
	}
	/**
	 * �����������ɾ������
	 * @param query HQLɾ�����
	 * @param paramValue ������ֵ����
	 * @throws E5Exception
	 */
	public static void delete(String sql, Object[] paramValue) 
	throws E5Exception 
	{
		try
		{
			BaseDAO dao = new BaseDAO();
			dao.delete(sql, paramValue);
		}
		catch (HibernateException e)
		{
			throw new E5Exception(e);
		}
	}
	
	/**
	 * �����������ɾ������������Session
	 * @param query HQLɾ�����
	 * @param paramValue ������ֵ����
	 * @param s
	 * @throws E5Exception
	 */
	public static void delete(String sql, Object[] paramValue, Session s) 
	throws E5Exception 
	{
		try
		{
			BaseDAO dao = new BaseDAO();
			dao.delete(sql, paramValue, s);
		}
		catch (HibernateException e)
		{
			throw new E5Exception(e);
		}
	}
	/**
	 * ���������Ĳ��Ҷ���
	 * @param sql
	 * @param paramValue
	 * @param paramType
	 * @return
	 * @throws E5Exception
	 */
	public static List find(String sql)
	throws E5Exception
	{
		try
		{
			BaseDAO dao = new BaseDAO();
			return dao.find(sql);
		}
		catch (HibernateException e)
		{
			throw new E5Exception(e);
		}
	}

	/**
	 * ���������Ĳ��Ҷ���
	 * @param sql
	 * @param paramValue
	 * @param paramType
	 * @return
	 * @throws E5Exception
	 */
	public static List find(String sql, Session s)
	throws E5Exception
	{
		try
		{
			BaseDAO dao = new BaseDAO();
			return dao.find(sql, s);
		}
		catch (HibernateException e)
		{
			throw new E5Exception(e);
		}
	}

	/**
	 * ��һ�������Ĳ��Ҷ���
	 * @param sql
	 * @param paramValue
	 * @param paramType
	 * @return
	 * @throws E5Exception
	 */
	public static List find(String sql, Object paramValue, Type paramType)
	throws E5Exception
	{
		try
		{
			BaseDAO dao = new BaseDAO();
			return dao.find(sql, paramValue, paramType);
		}
		catch (HibernateException e)
		{
			throw new E5Exception(e);
		}
	}

	/**
	 * ��һ�������Ĳ��Ҷ���
	 * @param sql
	 * @param paramValue
	 * @param paramType
	 * @return
	 * @throws E5Exception
	 */
	public static List find(String sql, Object paramValue, Type paramType, Session s)
	throws E5Exception
	{
		try
		{
			BaseDAO dao = new BaseDAO();
			return dao.find(sql, paramValue, paramType, s);
		}
		catch (HibernateException e)
		{
			throw new E5Exception(e);
		}
	}

	/**
	 * ����������Ĳ��Ҷ���
	 * <p>��Hibernate2.0������Hibernate3.0ʱ���������ȸĶ�</p> 
	 * @param query HQL�������
	 * @param paramName HQL����еĲ���������
	 * @param paramValue ������ֵ����
	 * @param paramType ��������������
	 * @return
	 * @throws E5Exception
	 */
	public static List find(String sql, String[] paramName, Object[] paramValue, Type[] paramType)
	throws E5Exception
	{
		try
		{
			BaseDAO dao = new BaseDAO();
			return dao.find(sql, paramName, paramValue, paramType);			
		}
		catch (HibernateException e)
		{
			throw new E5Exception(e);
		}
	}
	
	/**
	 * ����������Ĳ��Ҷ���
	 * <p>
	 * ��Hibernate2.0������Hibernate3.0ʱ���������ȸĶ� 
	 * <br/>��Ϊ3.0��ȥ���˴�����(Object[], Type[])�ķ������޷�ֱ�ӵ���
	 * <br/>����ʹ��Query.getNamedParameters����ȡ�������ƣ�
	 * ��Ϊ�÷������ص������Ǵ�Map�еõ��ģ�˳�򲻹̶�
	 * </p>
	 * @param query HQL�������
	 * @param paramName HQL����еĲ���������
	 * @param paramValue ������ֵ����
	 * @param paramType ��������������
	 * @param s
	 * @return
	 * @throws E5Exception
	 */
	public static List find(String sql, String[] paramName, Object[] paramValue, Type[] paramType, Session s)
	throws E5Exception
	{
		try
		{
			BaseDAO dao = new BaseDAO();
			return dao.find(sql, paramName, paramValue, paramType, s);			
		}
		catch (HibernateException e)
		{
			throw new E5Exception(e);
		}
	}
	
	/**
	 * ����������Ĳ��Ҷ���
	 * @param query HQL�������
	 * @param paramValue ������ֵ����
	 * @return
	 * @throws E5Exception
	 */
	public static List find(String sql, Object[] paramValue)
	throws E5Exception
	{
		try
		{
			BaseDAO dao = new BaseDAO();
			return dao.find(sql, paramValue);			
		}
		catch (HibernateException e)
		{
			throw new E5Exception(e);
		}
	}
	
	/**
	 * ����������Ĳ��Ҷ���
	 * @param query HQL�������
	 * @param paramValue ������ֵ����
	 * @param s
	 * @return
	 * @throws E5Exception
	 */
	public static List find(String sql, Object[] paramValue, Session s)
	throws E5Exception
	{
		try
		{
			BaseDAO dao = new BaseDAO();
			return dao.find(sql, paramValue, s);			
		}
		catch (HibernateException e)
		{
			throw new E5Exception(e);
		}
	}
	
	/**
	 * ��������
	 * @param t
	 */
	public static void rollback(Transaction t)
	{
		try{
			t.rollback();
		}catch (HibernateException e1){e1.printStackTrace();}
	}

	/**
	 * �ر�һ���Ự
	 * @param s
	 */
	public static void close(Session s)
	{
		try{
			s.close();
		}catch (HibernateException e1){e1.printStackTrace();}
	}
	
	//----------------------------------------------------------
	//---��DBSession��MyPersister���ܵķ�װ---------
	/**
	 * ʹ��DBSession������BaseDAO��һ�����ݿ������д�����
	 * Ҫ����ǰ�Ѿ���DBContext���������Ժ����ݿ���ֶε�ӳ�䡣
	 * @param obj ʵ��bean����
	 * @param tableName ����
	 * @return �ɹ��򷵻�1
	 * @throws E5Exception
	 */
	public static int create(Object obj, String tableName)
	throws E5Exception
	{
		DBSession ss = null;
		try {
			ss = Context.getDBSession();
			ss.store(tableName, obj );
			return 1;
		} catch (LaterDataTransferException e) {
			throw new E5Exception(e);
		} catch (SQLException e) {
			throw new E5Exception(e);
		} finally {
			CloseHelper.closeQuietly( ss );
		}
	}
	/**
	 * ʹ��DBSession������BaseDAO��һ�����ݿ�������ɾ����
	 * Ҫ����ǰ�Ѿ���DBContext���������Ժ����ݿ���ֶε�ӳ�䡣
	 * @param queryParams ���ݿ���ֶε���ֵ��
	 * @param tableName ����
	 * @return ɾ���ɹ��ĸ���
	 * @throws E5Exception
	 */
	public static int delete(Map queryParams, String tableName)
	throws E5Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append( "delete from " ).append(tableName).append( " where " );

		for ( Iterator i = queryParams.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry me = ( Map.Entry ) i.next();
			sb.append( me.getKey() ).append( "=?" );

			if ( i.hasNext() )
				sb.append( " and " );
		}

		DBSession ss = null;
		try
		{
			ss = Context.getDBSession();
			return ss.executeUpdate( sb.toString(), queryParams.values().toArray() );
		} catch (LaterDataTransferException e) {
			throw new E5Exception(e);
		} catch (SQLException e) {
			throw new E5Exception(e);
		} finally {
			CloseHelper.closeQuietly( ss );
		}
	}
	/**
	 * �������ݿ��и�������Щbean���ԡ�
	 * ʹ��DBSession������BaseDAO��һ�����ݿ��������޸ġ�
	 * Ҫ����ǰ�Ѿ���DBContext���������Ժ����ݿ���ֶε�ӳ�䡣
	 * @param bean
	 * @param tableName ����
	 * @param dirtyFields �Զ��ŷָ���bean�����б�
	 * @throws E5Exception
	 */
	public static int update(Object bean, String tableName, String dirtyFields)
	throws E5Exception
	{
		String[] dirty = dirtyFields.split( "," );
		DBSession ss = null;
		try {
			ss = Context.getDBSession();
			ss.update(tableName, bean, dirty );
			return 1;
		} catch (LaterDataTransferException e) {
			throw new E5Exception(e);
		} catch (SQLException e) {
			throw new E5Exception(e);
		} finally {
			CloseHelper.closeQuietly( ss );
		}
	}
	/**
	 * ���ݸ�����ѯ������ѯָ��������ʵ������б�<br>
	 * ����ñ�δ����ӳ���࣬��Ĭ��ӳ�䵽java.util.Map��<br>
	 * ���δ������������������ӳ�������Ĭ�ϲ���ͬ��ӳ��
	 * 
	 * @param queryParams ��ѯ���� {���� - ��ֵ} ע�⣺���ܺ�nullֵ
	 * @param tablename ����
	 * @return bean�ļ��ϣ����˱����mappingClass����Ϊ����ʵ��������ΪMapʵ����
	 * @throws E5Exception
	 */
	public static List query( Map queryParams, String tableName)
	throws E5Exception
	{
		DBSession ss = null;
		try {
			ss = Context.getDBSession();
			return ss.query(tableName, queryParams );
			
		} catch (LaterDataTransferException e) {
			throw new E5Exception(e);
		} catch (SQLException e) {
			throw new E5Exception(e);
		} finally {
			CloseHelper.closeQuietly( ss );
		}
	}
	/**
	 * ���ݱ�����id�����ݿ�������һ����¼��װ��Ϊʵ����󷵻ء�<br>
	 * ����ñ�δ����ӳ���࣬��Ĭ��ӳ�䵽java.util.Map��<br>
	 * ���δ������������������ӳ�������Ĭ�ϲ���ͬ��ӳ�䣻<br>
	 * ע�⣺��ı�ʶ�ֶ����û�Ԥ�����ã�Ҳ�ɶ�̬�޸ġ�
	 * 
	 * @param tablename ����
	 * @param id ��ʶ�ֶε�ֵ
	 * @return ʵ�����POJO��Map��
	 * @throws E5Exception
	 */
	public static Object get(String tableName, Object id)
	throws E5Exception
	{
		DBSession ss = null;
		try {
			ss = Context.getDBSession();
			return ss.load(tableName, id);
		} catch (LaterDataTransferException e) {
			throw new E5Exception(e);
		} catch (SQLException e) {
			throw new E5Exception(e);
		} finally {
			CloseHelper.closeQuietly( ss );
		}
	}
}
