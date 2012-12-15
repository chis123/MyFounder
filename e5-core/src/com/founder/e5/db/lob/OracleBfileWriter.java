package com.founder.e5.db.lob;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 针对oracle bfile的写入型bfile实现
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-4-21 0:00:02
 */
public class OracleBfileWriter extends AbstractBfileWriter {

	private static Log log = LogFactory.getLog( OracleBfileWriter.class );

	/**
	 * 创建本实例的bfile工厂（被OracleBfileWriter用来委托store请求）
	 */
	private OracleBfileFactory factory;

	/**
	 * @param directory
	 * @param file
	 * @param input
	 * @param factory 创建本实例的bfile工厂
	 */
	public OracleBfileWriter( String directory, String file, InputStream input,
			OracleBfileFactory factory ) {
		super( directory, file, input );
		this.factory = factory;
	}

	/**
	 * @see com.founder.e5.db.IBfile#getPlaceHolder()
	 */
	public String getPlaceHolder() {
		return "bfilename(?, ?)";
	}

	/**
	 * @see com.founder.e5.db.IBfile#setParameter(java.sql.PreparedStatement,
	 *      int)
	 */
	public int setParameter( PreparedStatement pst, int index )
			throws SQLException {
		pst.setString( index, directory );

		if ( log.isDebugEnabled() )
			log.debug( "binding parameter #" + index + " : " + directory );

		pst.setString( index + 1, file );

		if ( log.isDebugEnabled() )
			log.debug( "binding parameter #" + ( index + 1 ) + " : " + file );

		return index + 2;
	}

	/**
	 * @see com.founder.e5.db.IBfile#store()
	 */
	public void store() throws Exception {
		// 默认实现把数据存储的工作委托给创建本实例的bfile工厂
		factory.store( directory, file, input );
	}

}
