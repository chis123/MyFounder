package com.founder.e5.db.lob;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import com.founder.e5.db.BfileFactory;
import com.founder.e5.db.BfileType;
import com.founder.e5.db.IBfile;

/**
 * 针对oracle bfile的BfileFactory实现
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-4-20 21:10:15
 */
public abstract class OracleBfileFactory implements BfileFactory {

	private ArrayList supportedDirs = new ArrayList();

	public void setSupportedDirs( ArrayList supportedDirs ) {
		if ( supportedDirs != null )
			this.supportedDirs = supportedDirs;
	}

	/**
	 * @see com.founder.e5.db.BfileFactory#support(java.lang.String)
	 */
	public boolean support( String directory ) {
		return supportedDirs.contains( directory );
	}

	/**
	 * @see com.founder.e5.db.BfileFactory#convert(java.lang.Object)
	 */
	public IBfile convert( Object obj ) throws SQLException {
		if ( obj instanceof oracle.sql.BFILE ) {
			oracle.sql.BFILE bfile = ( oracle.sql.BFILE ) obj;
			return ( new OracleBfileReader( bfile ) );
		}

		return null;
	}

	/**
	 * @see com.founder.e5.db.BfileFactory#createBfile(java.lang.String,
	 *      java.lang.String)
	 */
	public IBfile createBfile( String directory, String file ) {
		return ( createBfile( directory, file, null ) );
	}

	/**
	 * @see com.founder.e5.db.BfileFactory#createBfile(java.lang.String,
	 *      java.lang.String, java.io.InputStream)
	 */
	public IBfile createBfile( String directory, String file, InputStream in ) {
		return ( new OracleBfileWriter( directory, file, in, this ) );
	}

	/**
	 * @see com.founder.e5.db.BfileFactory#createBfile(java.lang.String,
	 *      java.lang.String, java.io.InputStream, int)
	 */
	public IBfile createBfile( String directory, String file, InputStream in,
			int bfileType ) {
		if ( bfileType == BfileType.ORACLE )
			return createBfile( directory, file, in );
		return null;
	}

	// ------------------------------------------------------------------------
	// 扩展接口

	/**
	 * 传输数据到目的地<br>
	 * <br>
	 * 目的地信息由实现类自行获得；<br>
	 * 另外，实现者需要处理流为空的情况（典型的处理方式是流为空的话什么都不做）
	 * 
	 * @param directory 目录
	 * @param file 文件
	 * @param in 数据输入流
	 * @throws Exception
	 */
	public abstract void store( String directory, String file, InputStream in )
			throws Exception;

}
