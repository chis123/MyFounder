package com.founder.e5.db.lob;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.founder.e5.db.BfileException;
import com.founder.e5.db.IBfile;
import com.founder.e5.db.util.CloseHelper;

/**
 * 抽象的读取型bfile实现
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-4-21 9:44:21
 */
public abstract class AbstractBfileReader implements IBfile {

	/**
	 * @see com.founder.e5.db.IBfile#writeTo(java.io.File)
	 */
	public void writeTo( File file ) throws IOException, BfileException {
		FileOutputStream out = new FileOutputStream( file );
		try {
			writeTo( out );
		} finally {
			CloseHelper.closeQuietly( out );
		}
	}

	/**
	 * @see com.founder.e5.db.IBfile#getPlaceHolder()
	 */
	public String getPlaceHolder() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.founder.e5.db.IBfile#setParameter(java.sql.PreparedStatement,
	 *      int)
	 */
	public int setParameter( PreparedStatement pst, int index )
			throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.founder.e5.db.IBfile#store()
	 */
	public void store() throws Exception {
		throw new UnsupportedOperationException();
	}

}
