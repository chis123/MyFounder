package com.founder.e5.db;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

/**
 * BfileFactory��������<br>
 * <br>
 * �������һ��ģ��JDBC DriverManager��ע��Ͳ��һ��ƣ�ά��BfileFactory�Ķ���ʵ�֣����ݲ�ͬ������͸�����л���ͬ��ʵ�֡�<br>
 * ����������ṩһ��ResultSet.getObject���صĶ���Ҫ��ת��ΪIBfile����������һ��oracle
 * bfile���������ת����oracle
 * bfile��BfileFactoryʵ�֣�����һ���ⲿ�ļ���ʵ�ʶ���ΪString�����������ת�����ⲿ�ļ���BfileFactoryʵ�֣�
 * ��󷵻ظ�������һ����Ӧ��<code>IBfile</code>����
 * <br>
 * <br>
 * ���෽�����̰߳�ȫ�ġ�
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-4-20 20:44:47
 */
public final class BfileFactoryManager {

	/**
	 * ������������ע���BfileFactoryʵ��
	 */
	private final static Vector factories = new Vector();

	/**
	 * ע��BfileFactory
	 * 
	 * @param factory
	 */
	public static void registerFactory( BfileFactory factory ) {
		factories.add( factory );
	}

	// ----------------------------------------------------------------------

	/**
	 * �Ѵӽ������ȡ����bfile�������ݶ����ֵת��ΪIBfile����<br>
	 * <br>
	 * �÷�������ע��Ĺ����������������getBfile(ResultSet, int)������������ֵ�ǿգ������
	 * 
	 * @param obj ��jdbc�������ȡ���Ķ���rs.getObject()�ķ���ֵ��
	 * @return
	 * @throws SQLException
	 */
	public static IBfile convert( Object obj ) throws SQLException {
		for ( Iterator i = factories.iterator(); i.hasNext(); ) {

			BfileFactory factory = ( BfileFactory ) i.next();
			IBfile result = factory.convert( obj );
			if ( result != null )
				return result;
		}

		throw new RuntimeException( "no sutiable BfileFactory available." );
	}

	/**
	 * ������ע���BfileFactory����֧�ָ���Ŀ¼��������֮����IBfile
	 * 
	 * @param directory Ŀ¼��
	 * @param file �ļ��������������·������
	 * @return IBfile����
	 */
	public static IBfile createBfile( String directory, String file ) {
		for ( Iterator i = factories.iterator(); i.hasNext(); ) {

			BfileFactory factory = ( BfileFactory ) i.next();
			if ( factory.support( directory ) )
				return factory.createBfile( directory, file );
		}

		throw new RuntimeException(
				"no BfileFactory support specified directory[" + directory
						+ "] available." );
	}

	/**
	 * ����Ŀ¼�����ļ�������������������IBfile����
	 * 
	 * @param directory Ŀ¼��
	 * @param file �ļ��������������·������
	 * @param in ����������
	 * @return IBfile����
	 */
	public static IBfile createBfile( String directory, String file,
			InputStream in ) {
		for ( Iterator i = factories.iterator(); i.hasNext(); ) {

			BfileFactory factory = ( BfileFactory ) i.next();
			if ( factory.support( directory ) )
				return factory.createBfile( directory, file, in );
		}

		throw new RuntimeException(
				"no BfileFactory support specified directory[" + directory
						+ "] available." );
	}

	/**
	 * ����Ŀ¼�����ļ�����������������bfile���ʹ���IBfile����<br>
	 * <br>
	 * ע�⣺�÷�������BfileFactory�ķ�ʽ����createBfile������ͬ�����ﲻ����support
	 * �����жϣ�����ֱ�Ӽ����Ӧfactory�ķ���ֵ�Ƿ�Ϊ�գ�����Ϊ������ø�factory
	 * 
	 * @param directory Ŀ¼��
	 * @param file �ļ��������������·������
	 * @param in ����������
	 * @param bfileType bfile���ͣ�����������oracle bfile����ext bfile��
	 * @return IBfile����
	 */
	public static IBfile createBfile( String directory, String file,
			InputStream in, int bfileType ) {
		for ( Iterator i = factories.iterator(); i.hasNext(); ) {

			BfileFactory factory = ( BfileFactory ) i.next();
			IBfile bfile = factory.createBfile( directory, file, in, bfileType );
			if ( bfile != null )
				return bfile;
		}

		throw new RuntimeException( "no sutiable BfileFactory  available." );
	}

}
