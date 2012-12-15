package com.founder.e5.sys;

import java.io.InputStream;

import com.founder.e5.db.BfileFactoryManager;
import com.founder.e5.db.lob.ExtBfileFactory;

/**
 * 基于存储设备管理的外部文件类型bfile工厂实现
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
	 * 该方法在IoC容器注入reader实现后调用，把自身对象注册入BfileFactoryManager
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
