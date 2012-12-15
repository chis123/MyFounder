package com.founder.e5.db;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

/**
 * BfileFactory管理器。<br>
 * <br>
 * 该类采用一种模仿JDBC DriverManager的注册和查找机制，维护BfileFactory的多种实现，根据不同的请求透明地切换不同的实现。<br>
 * 比如调用者提供一个ResultSet.getObject返回的对象，要求转化为IBfile对象：若这是一个oracle
 * bfile，则把请求转发给oracle
 * bfile型BfileFactory实现；若是一个外部文件（实际对象为String），则把请求转发给外部文件型BfileFactory实现；
 * 最后返回给调用者一个相应的<code>IBfile</code>对象。
 * <br>
 * <br>
 * 该类方法是线程安全的。
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-4-20 20:44:47
 */
public final class BfileFactoryManager {

	/**
	 * 此容器保存已注册的BfileFactory实例
	 */
	private final static Vector factories = new Vector();

	/**
	 * 注册BfileFactory
	 * 
	 * @param factory
	 */
	public static void registerFactory( BfileFactory factory ) {
		factories.add( factory );
	}

	// ----------------------------------------------------------------------

	/**
	 * 把从结果集中取出的bfile类型数据对象的值转化为IBfile对象。<br>
	 * <br>
	 * 该方法在已注册的工厂对象上逐个调用getBfile(ResultSet, int)方法，若返回值非空，则采用
	 * 
	 * @param obj 从jdbc结果集中取出的对象（rs.getObject()的返回值）
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
	 * 查找已注册的BfileFactory，若支持给定目录名，则用之创建IBfile
	 * 
	 * @param directory 目录名
	 * @param file 文件名（可以是相对路径名）
	 * @return IBfile对象
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
	 * 根据目录名、文件名、数据输入流创建IBfile对象
	 * 
	 * @param directory 目录名
	 * @param file 文件名（可以是相对路径名）
	 * @param in 数据输入流
	 * @return IBfile对象
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
	 * 根据目录名、文件名、数据输入流、bfile类型创建IBfile对象。<br>
	 * <br>
	 * 注意：该方法查找BfileFactory的方式与别的createBfile方法不同：这里不采用support
	 * 方法判断，而是直接检查相应factory的返回值是否为空，若不为空则采用该factory
	 * 
	 * @param directory 目录名
	 * @param file 文件名（可以是相对路径名）
	 * @param in 数据输入流
	 * @param bfileType bfile类型（用来区分是oracle bfile还是ext bfile）
	 * @return IBfile对象
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
