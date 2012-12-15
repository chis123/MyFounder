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
 * �����ṩһЩ�й���Դ���ʡ��ͷŵķ���
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
	 * ͨ��������ø��������close�������÷����޲�����
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
	 * ���ø���������ض��������������쳣�����¼��־<br>
	 * <br>
	 * ע�⣺ָ���������벻������
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
	 * ����һ�������������ڶ�ȡ��·����ָ������Դ. <br>
	 * ע�⣺�÷���ʹ�õ�ǰ�̵߳�������������������·����������Դ
	 * 
	 * @param resourceName ��Դ�������·�������·����
	 * @return ��Դ������
	 */
	public static InputStream getResourceAsStream( String resourceName ) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		return cl.getResourceAsStream( resourceName );
	}

	/**
	 * �ӵ�ǰ��·�������������ļ�. <br>
	 * ע�⣺�÷���ʹ�õ�ǰ�̵߳�������������������·����������Դ
	 * 
	 * @param name �����ļ��������·�������·����
	 * @return java.util.Properties
	 * @throws IOException ��ָ����Դ�Ҳ������ȡʧ��ʱ
	 */
	public static Properties loadProperties( String name ) throws IOException {
		return PropertyUtils.loadProperties( name );
	}

	/**
	 * �ӵ�ǰ��·���°���ָ���������������ļ�.<br>
	 * ע�⣺�÷���ʹ�õ�ǰ�̵߳�������������������·����������Դ
	 * 
	 * @param name �����ļ��������·�������·���� (not-null)
	 * @param name ��Դ�������·�������·����
	 * @param encoding �����ļ��ı��� (null-safe)
	 * @return java.util.Properties
	 * @throws IOException ��ָ����Դ�Ҳ������ȡʧ��ʱ
	 */
	public static Properties loadProperties( String name, String encoding )
			throws IOException {
		return PropertyUtils.loadProperties( name, encoding );
	}

	/**
	 * ����λ����·���µ��ļ�. <br>
	 * ע�⣺�÷���ʹ�õ�ǰ�̵߳�������������������·����������Դ
	 * 
	 * @param resourceName �ļ��������·�������·����
	 * @return java.io.File����
	 */
	public static File findClassPathFile( String resourceName ) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		URL url = cl.getResource( resourceName );
		return ( url == null ? null : FileUtils.toFile( url ) );
	}

}
