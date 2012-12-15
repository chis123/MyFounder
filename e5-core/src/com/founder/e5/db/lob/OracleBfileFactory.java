package com.founder.e5.db.lob;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import com.founder.e5.db.BfileFactory;
import com.founder.e5.db.BfileType;
import com.founder.e5.db.IBfile;

/**
 * ���oracle bfile��BfileFactoryʵ��
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
	// ��չ�ӿ�

	/**
	 * �������ݵ�Ŀ�ĵ�<br>
	 * <br>
	 * Ŀ�ĵ���Ϣ��ʵ�������л�ã�<br>
	 * ���⣬ʵ������Ҫ������Ϊ�յ���������͵Ĵ���ʽ����Ϊ�յĻ�ʲô��������
	 * 
	 * @param directory Ŀ¼
	 * @param file �ļ�
	 * @param in ����������
	 * @throws Exception
	 */
	public abstract void store( String directory, String file, InputStream in )
			throws Exception;

}
