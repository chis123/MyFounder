package com.founder.e5.db.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.founder.e5.db.DBSession;

/**
 * 该类提供安静地关闭各种资源对象的方法，若有异常，则记录日志<br>
 * <br>
 * 外部依赖：commons-logging
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-5-19 10:23:27
 */
public class CloseHelper {

	static Log log = LogFactory.getLog( "e5" );

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
		} catch ( SQLException e ) {
			log.error( "", e );
		}
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

	public static void rollbackQuietly( DBSession sess ) {
		if ( sess != null ) {
			try {
				sess.rollbackTransaction();
			} catch ( SQLException e ) {
				log.error( "", e );
			}
		}
	}

}
