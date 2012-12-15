package com.founder.e5.sys;
import java.io.InputStream;

import com.founder.e5.context.E5Exception;
import com.founder.e5.sys.StorageDevice;

/**
 * @created 04-七月-2005 13:54:45
 * @version 1.0
 * @updated 11-七月-2005 13:06:01
 */
public interface StorageDeviceReader {

	/**
	 * 获得所有的存储设备
	 */
	public StorageDevice[] get() throws E5Exception;

	/**
	 * 根据设备名获得存储设备
	 * @param deviceName    存储设备名
	 * 
	 */
	public StorageDevice getByName(String deviceName) throws E5Exception;
	
	/**
	 * 指定存储设备、指定存放路径，把一个流保存到设备上。
	 * @param deviceName 存储设备名称
	 * @param rltPath 保存文件的相对路径，包括文件名
	 * @param in	输入流
	 * @throws E5Exception
	 */
	public void write(String deviceName, String rltPath, InputStream in) throws E5Exception;
	
	/**
	 * 指定存储设备、指定存放路径，把一个流保存到设备上。
	 * @param device 存储设备
	 * @param rltPath 保存文件的相对路径，包括文件名
	 * @param in	输入流
	 * @throws E5Exception
	 */
	public void write(StorageDevice device, String rltPath, InputStream in) throws E5Exception;

	/**
	 * 从指定的存储设备、指定的路径获取一个文件流
	 * @param deviceName 存储设备名称
	 * @param rltPath 保存文件的相对路径，包括文件名
	 * @return	输出流
	 * @throws E5Exception
	 */
	public InputStream read(String deviceName, String rltPath) throws E5Exception;
	
	/**
	 * 从指定的存储设备、指定的路径获取一个文件流
	 * @param device 存储设备
	 * @param rltPath 保存文件的相对路径，包括文件名
	 * @return	输出流
	 * @throws E5Exception
	 */
	public InputStream read(StorageDevice device, String rltPath) throws E5Exception;
	
	/**
	 * 把一个流写入到指定存储设备的指定路径下
	 * @param deviceName 存储设备名称
	 * @param rltPath 保存文件的相对路径，包括文件名
	 * @param in	输入流
	 * @param deviceType 指定存储设备的类型
	 * @throws E5Exception
	 */
	public void write(String deviceName, String rltPath, InputStream in, int deviceType) throws E5Exception;
	
	/**
	 * 把一个流写入到指定存储设备的指定路径下
	 * @param device 存储设备
	 * @param rltPath 保存文件的相对路径，包括文件名
	 * @param in	输入流
	 * @param deviceType 指定存储设备的类型
	 * @throws E5Exception
	 */
	public void write(StorageDevice device, String rltPath, InputStream in, int deviceType) throws E5Exception;
	
	/**
	 * 从指定的存储设备、指定的路径获取一个文件流
	 * @param deviceName 存储设备名称
	 * @param rltPath 保存文件的相对路径，包括文件名
	 * @param deviceType 指定存储设备的类型
	 * @return 输出流
	 * @throws E5Exception
	 */
	public InputStream read(String deviceName, String rltPath, int deviceType) throws E5Exception;

	/**
	 * 从指定的存储设备、指定的路径获取一个文件流
	 * @param device 存储设备
	 * @param rltPath 保存文件的相对路径，包括文件名
	 * @param deviceType 指定存储设备的类型
	 * @return 输出流
	 * @throws E5Exception
	 */
	public InputStream read(StorageDevice device, String rltPath, int deviceType) throws E5Exception;

	/**
	 * @param deviceName 存储设备名称
	 * @return
	 * @throws E5Exception
	 */
	public FtpReader getFtpReader(String deviceName)throws E5Exception;
}