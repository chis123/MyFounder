package com.founder.e5.sys;

import java.io.InputStream;

import com.founder.e5.db.BfileFactoryManager;
import com.founder.e5.db.lob.OracleBfileFactory;

/**
 * ���ڴ洢�豸�����oracle���bfile����ʵ��<br>
 * <br>
 * �Ӹù���������bfile�����ݴ����߼��ɴ洢�豸����ʵ��
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-4-24 10:32:33
 */
public class SDBasedOraBfileFactory extends OracleBfileFactory {

	private StorageDeviceReader reader;

	public void setReader( StorageDeviceReader reader ) {
		this.reader = reader;
	}

	/**
	 * �÷�����IoC����ע��readerʵ�ֺ���ã����������ע����BfileFactoryManager
	 */
	public void initialize() {
		BfileFactoryManager.registerFactory( this );
	}

	/**
	 * @see com.founder.e5.db.lob.OracleBfileFactory#store(java.lang.String,
	 *      java.lang.String, java.io.InputStream)
	 */
	public void store( String directory, String file, InputStream in )
			throws Exception {
		if ( in != null )
			reader.write( directory, file, in );
	}
}
