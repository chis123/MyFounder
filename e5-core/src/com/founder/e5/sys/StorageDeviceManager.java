package com.founder.e5.sys;

import com.founder.e5.context.E5Exception;
import com.founder.e5.sys.StorageDevice;

/**
 * @created 04-����-2005 13:54:45
 * @version 1.0
 * @updated 11-����-2005 13:06:05
 */
public interface StorageDeviceManager extends StorageDeviceReader {

	/**
	 * �����洢�豸
	 * @param storageDivice    �洢�豸����
	 * 
	 */
	public void create(StorageDevice storageDivice) throws E5Exception;

	/**
	 * ���´洢�豸
	 * @param storageDevice    �洢�豸
	 * 
	 */
	public void update(StorageDevice storageDevice) throws E5Exception;

	/**
	 * ɾ���洢�豸
	 * @param deviceName    �洢�豸��
	 * 
	 */
	public void delete(String deviceName) throws E5Exception;

}