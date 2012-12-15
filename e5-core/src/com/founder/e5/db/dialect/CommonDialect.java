/**
 * $Id: e5new com.founder.e5.db.dialect CommonDialect.java 
 * created on 2006-4-12 19:32:27
 * by liyanhui
 */
package com.founder.e5.db.dialect;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.founder.e5.db.IBfile;
import com.founder.e5.db.IBlob;
import com.founder.e5.db.IClob;
import com.founder.e5.db.LobHelper;

/**
 * @author liyanhui
 * @version 1.0
 * @created 2006-4-12 19:32:27
 */
public abstract class CommonDialect extends Dialect {

	/**
	 * @see com.founder.e5.db.dialect.Dialect#writeBlob(long, java.lang.String,
	 *      java.lang.String, java.lang.String, com.founder.e5.db.IBlob)
	 */
	public void writeBlob( long id, String tablename, String idColumnName,
			String lobColumnName, IBlob blob ) throws SQLException, IOException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.founder.e5.db.dialect.Dialect#writeClob(long, java.lang.String,
	 *      java.lang.String, java.lang.String, com.founder.e5.db.IClob)
	 */
	public void writeClob( long id, String tablename, String idColumnName,
			String lobColumnName, IClob clob ) throws SQLException, IOException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @throws IOException
	 * @see com.founder.e5.db.dialect.Dialect#getClob(java.sql.ResultSet,
	 *      java.lang.String)
	 */
	public IClob getClob( ResultSet rs, String name ) throws SQLException,
			IOException {
		Clob clob = rs.getClob( name );
		if ( clob != null ) {
			String content = LobHelper.readClob( clob );
			return LobHelper.createClob( content );
		}
		return null;
	}

	/**
	 * @throws SQLException
	 * @throws IOException
	 * @see com.founder.e5.db.dialect.Dialect#getBlob(java.sql.ResultSet,
	 *      java.lang.String)
	 */
	public IBlob getBlob( ResultSet rs, String name ) throws SQLException,
			IOException {
		Blob blob = rs.getBlob( name );
		if ( blob != null ) {
			byte[] content = LobHelper.readBlob( blob );
			return LobHelper.createBlob( content );
		}
		return null;
	}

	/**
	 * @see com.founder.e5.db.dialect.Dialect#getBfile(java.sql.ResultSet,
	 *      java.lang.String)
	 */
	public IBfile getBfile( ResultSet rs, String name ) throws SQLException,
			IOException {

		if ( !supportBfile() ) {
			String bfileInfo = rs.getString( name );
			if ( bfileInfo != null ) {
				int pos = bfileInfo.indexOf( ',' );
				if ( pos != -1 && pos != bfileInfo.length() - 1 ) {
					String dir = bfileInfo.substring( 0, pos );
					String file = bfileInfo.substring( pos + 1 );
					return LobHelper.createBfile( dir, file );
				}
			}
		}

		return null;
	}

}
