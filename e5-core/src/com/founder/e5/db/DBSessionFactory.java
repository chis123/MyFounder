package com.founder.e5.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.founder.e5.db.dialect.Dialect;

/**
 * DBSession�����������ض��ھ������ݿ��DBSessionʵ�����ʵ��<br>
 * <br>
 * Ŀǰ�Ĳ����ǣ��Ȳ����û���ע���DBSessionʵ�֣����Ҳ����������Ĭ��ʵ�֣�BaseDBSession��
 * 
 * @version 1.0
 * @created 07-����-2005 15:20:19
 */
public class DBSessionFactory {

	static Log log = LogFactory.getLog( "e5" );

	/**
	 * ���ݿ����Ͷ��壨���ݿ�������--ʵ���ࣩ����
	 */
	private static HashMap dbdef = new HashMap();
	
	static
	{
		try
		{
			//�Զ�ע�������dbsession�����������ļ����滻
			registerDB(DBType.POSTGRESQL, "com.founder.e5.db.PostgreSQLDBSession");
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * ע�����ݿ����ͼ���DBSessionʵ����
	 * 
	 * @param dbname ���ݿ�������
	 * @param implementation ʵ������
	 * @throws ClassNotFoundException
	 */
	public static void registerDB( String dbname, String implementation )
			throws ClassNotFoundException {
		Class clazz = Class.forName( implementation );
		registerDB( dbname, clazz );
	}

	/**
	 * ע�����ݿ����ͼ���DBSessionʵ����
	 * 
	 * @param dbname ���ݿ�������
	 * @param implemention ʵ����
	 */
	public static void registerDB( String dbname, Class implemention ) {
		dbdef.put( dbname, implemention );
	}

	/**
	 * �������ݿ���������ö�Ӧ��ʵ��������Ȼ�󷵻ظ���ʵ��<br>
	 * <br>
	 * ע�⣺�÷������ص�DBSessionʵ����δע��Connection���û�ʹ��ǰ���ֶ�ע��
	 * 
	 * @param dbtype ���ݿ����������������ֲ�ͬ���͵����ݿ⣩
	 */
	public static DBSession getDBSession( String dbtype ) {

		// �Ȳ����Ƿ������ע��ʵ���࣬���У������
		Class clazz = ( Class ) dbdef.get( dbtype );
		if ( clazz != null ) {
			try {
				return ( DBSession ) clazz.newInstance();
			} catch ( Exception e ) {
				log.error( "newInstance error! " + clazz, e );
			}
		}

		// �Ҳ�����ʵ����ʧ�ܣ������Ĭ��ʵ��
		Dialect dialect = Dialect.getDialect( dbtype );
		if ( dialect == null )
			throw new IllegalArgumentException( "unsupported dbtype: " + dbtype );

		BaseDBSession dbsession = new BaseDBSession( dialect );
		return dbsession;
	}

	/**
	 * ����ע���Connection����²����ݿ����ͣ�Ȼ�󴴽���Ӧ��DBSessionʵ��
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException ����ȡ���ݿ�Ԫ���ݳ������׳��쳣
	 */
	public static DBSession getDBSession( Connection conn ) throws SQLException {

		String dbtype = DBType.guessDBType( conn );

		// �Ȳ����Ƿ������ע��ʵ���࣬���У������
		Class clazz = ( Class ) dbdef.get( dbtype );
		if ( clazz != null ) {
			try {
				DBSession result = ( DBSession ) clazz.newInstance();
				result.setConnection( conn );
				return result;
			} catch ( Exception e ) {
				log.error( "newInstance error! " + clazz, e );
			}
		}

		// �Ҳ�����ʵ����ʧ�ܣ������Ĭ��ʵ��
		Dialect dialect = Dialect.getDialect( dbtype );
		if ( dialect == null )
			throw new IllegalArgumentException( "unsupported dbtype: " + dbtype );

		BaseDBSession dbsession = new BaseDBSession( dialect );
		return dbsession;
	}

}
