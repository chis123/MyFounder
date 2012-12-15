package com.founder.e5.sys;

import java.io.InputStream;

import com.founder.e5.db.BfileFactoryManager;
import com.founder.e5.db.lob.ExtBfileFactory;

/**
 * ���ڴ洢�豸������ⲿ�ļ�����bfile����ʵ��
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-4-24 10:31:24
 */
public class SDBasedExtBfileFactory extends ExtBfileFactory {

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
	 * @see com.founder.e5.db.lob.ExtBfileFactory#read(java.lang.String,
	 *      java.lang.String)
	 */
	public InputStream read( String directory, String file ) throws Exception {
		return reader.read( directory, file );
	}

	/**
	 * @see com.founder.e5.db.lob.ExtBfileFactory#store(java.lang.String,
	 *      java.lang.String, java.io.InputStream)
	 */
	public void store( String directory, String file, InputStream in )
			throws Exception {
		if ( in != null )
			reader.write( directory, file, in );
	}

}
