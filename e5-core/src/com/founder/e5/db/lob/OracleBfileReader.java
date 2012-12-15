package com.founder.e5.db.lob;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import oracle.sql.BFILE;

import org.apache.commons.io.CopyUtils;

import com.founder.e5.db.BfileException;
import com.founder.e5.db.util.CloseHelper;

/**
 * 针对oracle bfile的读取型bfile实现
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2005-8-5 13:34:20
 */
public class OracleBfileReader extends AbstractBfileReader {

	private oracle.sql.BFILE bfile;

	/**
	 * @param bfile
	 */
	public OracleBfileReader( BFILE bfile ) {
		if ( bfile == null )
			throw new NullPointerException();
		this.bfile = bfile;
	}

	/**
	 * @see com.founder.e5.db.IBfile#openFile()
	 */
	public InputStream openFile() throws BfileException {
		try {
			bfile.openFile();
			return bfile.getBinaryStream();

		} catch ( SQLException e ) {
			throw new BfileException( e );
		}
	}

	/**
	 * @see com.founder.e5.db.IBfile#closeFile()
	 */
	public void closeFile() throws BfileException {
		try {
			bfile.closeFile();
		} catch ( SQLException e ) {
			throw new BfileException( e );
		}
	}

	/**
	 * @see com.founder.e5.db.IBfile#writeTo(java.io.OutputStream)
	 */
	public void writeTo( OutputStream out ) throws IOException, BfileException {
		try {
			try {
				bfile.openFile();

				InputStream in = bfile.getBinaryStream();
				try {
					CopyUtils.copy( in, out );
				} finally {
					CloseHelper.closeQuietly( in );
				}

			} finally {
				bfile.closeFile();
			}

		} catch ( SQLException e ) {
			throw new BfileException( e );
		} catch ( IOException e ) {
			throw new BfileException( e );
		}
	}

	/**
	 * @see com.founder.e5.db.IBfile#writeTo(java.io.File)
	 */
	public void writeTo( File file ) throws IOException, BfileException {
		OutputStream out = null;
		try {
			out = new FileOutputStream( file );
			writeTo( out );
		} finally {
			CloseHelper.closeQuietly( out );
		}
	}

	/**
	 * @see com.founder.e5.db.IBfile#getDirectory()
	 */
	public String getDirectory() throws BfileException {
		try {
			return bfile.getDirAlias();
		} catch ( SQLException e ) {
			throw new BfileException( e );
		}
	}

	/**
	 * @see com.founder.e5.db.IBfile#getFile()
	 */
	public String getFile() throws BfileException {
		try {
			return bfile.getName();
		} catch ( SQLException e ) {
			throw new BfileException( e );
		}
	}

}
