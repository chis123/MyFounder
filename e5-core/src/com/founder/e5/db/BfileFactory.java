package com.founder.e5.db;

import java.io.InputStream;
import java.sql.SQLException;

/**
 * IBfile对象工厂。<br>
 * <br>
 * 该接口提供两种功能：<br>
 * 1)、读取时从ResultSet中取出一个IBfile对象。这部分功能由convert方法提供。<br>
 * 2)、写入时根据用户提供的dir、file、（stream）创建一个IBfile对象，用作值对象传给
 * DBSession.executeUpdate。这部分功能由createBfile方法提供。 <br>
 * <br>
 * 实际中主要有两种实现：oracle bfile型实现和外部文件型实现。<br>
 * 同一应用中可能有多种实现共存，每种实现对应一种bfile实现机制，要求实现者检查是否自身所
 * 支持的bfile机制，如果不是，直接返回null，这将告诉BfileFactoryManager，自己不支持这种bfile。
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-4-20 20:42:56
 * @see BfileFactoryManager
 */
public interface BfileFactory {

	/**
	 * 是否支持指定目录
	 * 
	 * @param directory 目录名（可能是Oracle目录，也可能是存储设备名）
	 * @return boolean
	 */
	public boolean support( String directory );

	/**
	 * 把从结果集中取出的bfile类型数据对象的值转化为IBfile对象。<br>
	 * <br>
	 * 注意：若该工厂对象不支持把结果集中的对象转化为IBfile对象，可返回null；<br>
	 * IBfile工厂管理器在已注册的工厂对象上逐个调用该方法，若返回值非空，则采用该工厂对象。
	 * 
	 * @param obj 从jdbc结果集中取出的对象（rs.getObject()的返回值）
	 * @return IBfile
	 * @throws SQLException
	 */
	public IBfile convert( Object obj ) throws SQLException;

	/**
	 * 根据目录名、文件名创建IBfile对象
	 * 
	 * @param directory 目录名
	 * @param file 文件名（可以是相对路径名）
	 * @return IBfile对象
	 */
	public IBfile createBfile( String directory, String file );

	/**
	 * 根据目录名、文件名、数据输入流创建IBfile对象
	 * 
	 * @param directory 目录名
	 * @param file 文件名（可以是相对路径名）
	 * @param in 数据输入流
	 * @return IBfile对象
	 */
	public IBfile createBfile( String directory, String file, InputStream in );

	/**
	 * 根据目录名、文件名、数据输入流、bfile类型创建IBfile对象
	 * 
	 * @param directory 目录名
	 * @param file 文件名（可以是相对路径名）
	 * @param in 数据输入流
	 * @param bfileType bfile类型（用来区分是oracle bfile还是ext bfile）
	 * @return IBfile对象
	 */
	public IBfile createBfile( String directory, String file, InputStream in,
			int bfileType );

}
