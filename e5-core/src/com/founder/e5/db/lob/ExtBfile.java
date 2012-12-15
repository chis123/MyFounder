package com.founder.e5.db.lob;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.io.CopyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.founder.e5.db.BfileException;
import com.founder.e5.db.IBfile;
import com.founder.e5.db.util.CloseHelper;

/**
 * 外部文件风格bfile实现。<br>
 * <br>
 * 该类与ExtBfileFactory联合使用，完成数据上传下载
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-4-28 10:46:45
 */
public class ExtBfile implements IBfile {

	protected static Log log = LogFactory.getLog( "e5" );

	protected String directory;

	protected String file;

	protected InputStream input; // 此流仅用于写入时

	/**
	 * 创建本实例的bfile工厂（被ExtBfile用来委托read、store请求）
	 */
	protected ExtBfileFactory factory;

	/**
	 * 该构造函数用于构造一个读取型bfile实现
	 * 
	 * @param directory
	 * @param file
	 * @param factory 创建本实例的bfile工厂
	 */
	public ExtBfile( String directory, String file, ExtBfileFactory factory ) {
		this.directory = directory;
		this.file = file;
		this.factory = factory;
	}

	/**
	 * 该构造函数用于构造一个写入型bfile实现
	 * 
	 * @param directory
	 * @param file
	 * @param input
	 * @param factory 创建本实例的bfile工厂
	 */
	public ExtBfile( String directory, String file, InputStream input,
			ExtBfileFactory factory ) {
		this.directory = directory;
		this.file = file;
		this.input = input;
		this.factory = factory;
	}

	/**
	 * @see com.founder.e5.db.IBfile#getDirectory()
	 */
	public String getDirectory() throws BfileException {
		return directory;
	}

	/**
	 * @see com.founder.e5.db.IBfile#getFile()
	 */
	public String getFile() throws BfileException {
		return file;
	}

	/**
	 * @see com.founder.e5.db.IBfile#openFile()
	 */
	public InputStream openFile() throws BfileException {
		try {
			return factory.read( directory, file );
		} catch ( Exception e ) {
			throw new BfileException( e );
		}
	}

	/**
	 * @see com.founder.e5.db.IBfile#closeFile()
	 */
	public void closeFile() throws BfileException {
	}

	/**
	 * @see com.founder.e5.db.IBfile#writeTo(java.io.OutputStream)
	 */
	public void writeTo( OutputStream out ) throws IOException, BfileException {
		InputStream in = null;
		try {
			in = openFile();
			if (in == null) throw new BfileException("BFile is null!");
			
			CopyUtils.copy( in, out );
		} finally {
			CloseHelper.closeQuietly( in );

			try {
				closeFile();
			} catch ( Exception e ) {
				log.error( "", e );
			}
		}
	}

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
		return "?";
	}

	/**
	 * @see com.founder.e5.db.IBfile#setParameter(java.sql.PreparedStatement,
	 *      int)
	 */
	public int setParameter( PreparedStatement pst, int index )
			throws SQLException {
		String value = directory + "," + file;
		pst.setString( index, value );

		if ( log.isDebugEnabled() )
			log.debug( "binding parameter #" + index + " : " + value );

		return index + 1;
	}

	/**
	 * @see com.founder.e5.db.IBfile#store()
	 */
	public void store() throws Exception {

		// 默认实现把数据存储的工作转发给工厂对象
		factory.store( directory, file, input );
	}

}
