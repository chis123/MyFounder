/*
 * Created on 2005-2-25 16:23:40
 * 
 */
package com.founder.e5.commons;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.founder.e5.db.DBSession;
import com.founder.e5.db.IResultSet;

/**
 * 该类提供一些有关资源访问、释放的方法
 * 
 * @author liyanhui 2005-2-25 16:23:40
 */
public class ResourceMgr {

	private static Log log = LogFactory.getLog( "e5" );

	// --------------------------------------------------------------------------

	public static void closeQuietly( InputStream closable ) {
		if ( closable == null )
			return;
		try {
			closable.close();
		} catch ( IOException e ) {
			log.error( "", e );
		}
	}

	public static void closeQuietly( OutputStream closable ) {
		if ( closable == null )
			return;
		try {
			closable.close();
		} catch ( IOException e ) {
			log.error( "", e );
		}
	}

	public static void closeQuietly( Reader closable ) {
		if ( closable == null )
			return;
		try {
			closable.close();
		} catch ( IOException e ) {
			log.error( "", e );
		}
	}

	public static void closeQuietly( Writer closable ) {
		if ( closable == null )
			return;
		try {
			closable.close();
		} catch ( IOException e ) {
			log.error( "", e );
		}
	}

	public static void closeQuietly( Socket closable ) {
		if ( closable == null )
			return;
		try {
			closable.close();
		} catch ( IOException e ) {
			log.error( "", e );
		}
	}

	public static void closeQuietly( ServerSocket closable ) {
		if ( closable == null )
			return;
		try {
			closable.close();
		} catch ( IOException e ) {
			log.error( "", e );
		}
	}

	public static void closeQuietly( IResultSet closable ) {
		if ( closable == null )
			return;
		try {
			closable.close();
		} catch ( SQLException e ) {
			log.error( "", e );
		}
	}

	public static void closeQuietly( ResultSet closable ) {
		if ( closable == null )
			return;
		try {
			closable.close();
		} catch ( SQLException e ) {
			log.error( "", e );
		}
	}

	public static void closeQuietly( Statement closable ) {
		if ( closable == null )
			return;
		try {
			closable.close();
		} catch ( SQLException e ) {
			log.error( "", e );
		}
	}

	public static void closeQuietly( Connection closable ) {
		if ( closable == null )
			return;

		try {
			closable.close();
		} catch ( SQLException e ) {
			log.error( "", e );
		}
	}

	public static void closeQuietly( DBSession closable ) {
		if ( closable == null )
			return;

		try {
			closable.close();
		} catch ( Exception e ) {
			log.error( "", e );
		}
	}

	/**
	 * 通过反射调用给定对象的close方法（该方法无参数）
	 * 
	 * @param closable
	 */
	public static void closeQuietly( Object closable ) {
		invokeQuietly( closable, "close" );
	}

	// ----------------------------------------------------------------------

	public static void rollbackQuietly( Connection conn ) {
		if ( conn != null ) {
			try {
				conn.rollback();
			} catch ( SQLException e ) {
				log.error( "", e );
			}
		}
	}

	public static void rollbackQuietly( DBSession dbsession ) {
		if ( dbsession != null ) {
			try {
				dbsession.rollbackTransaction();
			} catch ( SQLException e ) {
				log.error( "", e );
			}
		}
	}

	public static void rollbackQuietly( Object resource ) {
		invokeQuietly( resource, "rollback" );
	}

	// ----------------------------------------------------------------------

	/**
	 * 调用给定对象的特定方法，若产生异常，则记录日志<br>
	 * <br>
	 * 注意：指定方法必须不带参数
	 * 
	 * @param object
	 * @param methodName
	 */
	static void invokeQuietly( Object object, String methodName ) {
		if ( object != null ) {
			Class clazz = object.getClass();

			try {
				Method method = clazz.getMethod( methodName, null );
				method.invoke( object, null );
			} catch ( Exception e ) {
				log.error( "error when invoke \"" + methodName + "\" on ["
						+ object + "]", e );
			}

		}
	}

	// ----------------------------------------------------------------------

	/**
	 * 返回一个输入流，用于读取类路径下指定的资源. <br>
	 * 注意：该方法使用当前线程的上下文类载入器在类路径下搜索资源
	 * 
	 * @param resourceName 资源相对于类路径的相对路径名
	 * @return 资源输入流
	 */
	public static InputStream getResourceAsStream( String resourceName ) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		return cl.getResourceAsStream( resourceName );
	}

	/**
	 * 从当前类路径下载入属性文件. <br>
	 * 注意：该方法使用当前线程的上下文类载入器在类路径下搜索资源
	 * 
	 * @param name 属性文件相对于类路径的相对路径名
	 * @return java.util.Properties
	 * @throws IOException 当指定资源找不到或读取失败时
	 */
	public static Properties loadProperties( String name ) throws IOException {
		return PropertyUtils.loadProperties( name );
	}

	/**
	 * 从当前类路径下按照指定编码载入属性文件.<br>
	 * 注意：该方法使用当前线程的上下文类载入器在类路径下搜索资源
	 * 
	 * @param name 属性文件相对于类路径的相对路径名 (not-null)
	 * @param name 资源相对于类路径的相对路径名
	 * @param encoding 属性文件的编码 (null-safe)
	 * @return java.util.Properties
	 * @throws IOException 当指定资源找不到或读取失败时
	 */
	public static Properties loadProperties( String name, String encoding )
			throws IOException {
		return PropertyUtils.loadProperties( name, encoding );
	}

	/**
	 * 查找位于类路径下的文件. <br>
	 * 注意：该方法使用当前线程的上下文类载入器在类路径下搜索资源
	 * 
	 * @param resourceName 文件相对于类路径的相对路径名
	 * @return java.io.File对象
	 */
	public static File findClassPathFile( String resourceName ) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		URL url = cl.getResource( resourceName );
		return ( url == null ? null : FileUtils.toFile( url ) );
	}

}
