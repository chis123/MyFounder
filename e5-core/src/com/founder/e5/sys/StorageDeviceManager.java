package com.founder.e5.sys;

import com.founder.e5.context.E5Exception;
import com.founder.e5.sys.StorageDevice;

/**
 * @created 04-七月-2005 13:54:45
 * @version 1.0
 * @updated 11-七月-2005 13:06:05
 */
public interface StorageDeviceManager extends StorageDeviceReader {

	/**
	 * 创建存储设备
	 * @param storageDivice    存储设备对象
	 * 
	 */
	public void create(StorageDevice storageDivice) throws E5Exception;

	/**
	 * 更新存储设备
	 * @param storageDevice    存储设备
	 * 
	 */
	public void update(StorageDevice storageDevice) throws E5Exception;

	/**
	 * 删除存储设备
	 * @param deviceName    存储设备名
	 * 
	 */
	public void delete(String deviceName) throws E5Exception;

}