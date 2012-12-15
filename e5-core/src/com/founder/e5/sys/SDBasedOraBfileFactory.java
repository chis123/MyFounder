package com.founder.e5.sys;

import java.io.InputStream;

import com.founder.e5.db.BfileFactoryManager;
import com.founder.e5.db.lob.OracleBfileFactory;

/**
 * 基于存储设备管理的oracle风格bfile工厂实现<br>
 * <br>
 * 从该工厂创建的bfile的数据传输逻辑由存储设备管理实现
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
	 * 该方法在IoC容器注入reader实现后调用，把自身对象注册入BfileFactoryManager
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
