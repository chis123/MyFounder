package com.founder.e5.db.lob;

import java.io.InputStream;
import java.sql.SQLException;

import com.founder.e5.db.BfileFactory;
import com.founder.e5.db.BfileType;
import com.founder.e5.db.IBfile;

/**
 * @author liyanhui
 * @version 1.0
 * @created 2006-4-28 10:37:51
 */
public abstract class ExtBfileFactory implements BfileFactory {

	/**
	 * @see com.founder.e5.db.BfileFactory#support(java.lang.String)
	 */
	public boolean support( String directory ) {
		return true;
	}

	/**
	 * @see com.founder.e5.db.BfileFactory#convert(java.lang.Object)
	 */
	public IBfile convert( Object obj ) throws SQLException {
		if ( obj instanceof String ) {
			String fileInfo = ( String ) obj;
			int comma = fileInfo.indexOf( ',' );

			if ( comma != -1 && ( comma != fileInfo.length() - 1 ) ) {
				String dir = fileInfo.substring( 0, comma );
				String file = fileInfo.substring( comma + 1 );

				ExtBfile bfile = new ExtBfile( dir, file, this );
				return bfile;
			}
		}

		return null;
	}

	/**
	 * @see com.founder.e5.db.BfileFactory#createBfile(java.lang.String,
	 *      java.lang.String)
	 */
	public IBfile createBfile( String directory, String file ) {
		return createBfile( directory, file, null );
	}

	/**
	 * @see com.founder.e5.db.BfileFactory#createBfile(java.lang.String,
	 *      java.lang.String, java.io.InputStream)
	 */
	public IBfile createBfile( String directory, String file, InputStream in ) {
		return ( new ExtBfile( directory, file, in, this ) );
	}

	/**
	 * @see com.founder.e5.db.BfileFactory#createBfile(java.lang.String,
	 *      java.lang.String, java.io.InputStream, int)
	 */
	public IBfile createBfile( String directory, String file, InputStream in,
			int bfileType ) {
		if ( bfileType == BfileType.EXTFILE )
			return createBfile( directory, file, in );
		return null;
	}

	// ---------------------------------------------------------------------
	// ��չ�ӿ�

	/**
	 * ���ض���Դ��ȡ����<br>
	 * <br>
	 * ����Դ��Ϣ��ʵ�������л��
	 * 
	 * @param directory
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public abstract InputStream read( String directory, String file )
			throws Exception;

	/**
	 * �������ݵ�Ŀ�ĵ�<br>
	 * <br>
	 * Ŀ�ĵ���Ϣ��ʵ�������л�ã�<br>
	 * ���⣬ʵ������Ҫ������Ϊ�յ���������͵Ĵ���ʽ����Ϊ�յĻ�ʲô��������
	 * 
	 * @param directory
	 * @param file
	 * @param in
	 * @throws Exception
	 */
	public abstract void store( String directory, String file, InputStream in )
			throws Exception;

}
