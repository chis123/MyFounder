package com.founder.e5.sys;
import java.io.InputStream;

import com.founder.e5.context.E5Exception;
import com.founder.e5.sys.StorageDevice;

/**
 * @created 04-����-2005 13:54:45
 * @version 1.0
 * @updated 11-����-2005 13:06:01
 */
public interface StorageDeviceReader {

	/**
	 * ������еĴ洢�豸
	 */
	public StorageDevice[] get() throws E5Exception;

	/**
	 * �����豸����ô洢�豸
	 * @param deviceName    �洢�豸��
	 * 
	 */
	public StorageDevice getByName(String deviceName) throws E5Exception;
	
	/**
	 * ָ���洢�豸��ָ�����·������һ�������浽�豸�ϡ�
	 * @param deviceName �洢�豸����
	 * @param rltPath �����ļ������·���������ļ���
	 * @param in	������
	 * @throws E5Exception
	 */
	public void write(String deviceName, String rltPath, InputStream in) throws E5Exception;
	
	/**
	 * ָ���洢�豸��ָ�����·������һ�������浽�豸�ϡ�
	 * @param device �洢�豸
	 * @param rltPath �����ļ������·���������ļ���
	 * @param in	������
	 * @throws E5Exception
	 */
	public void write(StorageDevice device, String rltPath, InputStream in) throws E5Exception;

	/**
	 * ��ָ���Ĵ洢�豸��ָ����·����ȡһ���ļ���
	 * @param deviceName �洢�豸����
	 * @param rltPath �����ļ������·���������ļ���
	 * @return	�����
	 * @throws E5Exception
	 */
	public InputStream read(String deviceName, String rltPath) throws E5Exception;
	
	/**
	 * ��ָ���Ĵ洢�豸��ָ����·����ȡһ���ļ���
	 * @param device �洢�豸
	 * @param rltPath �����ļ������·���������ļ���
	 * @return	�����
	 * @throws E5Exception
	 */
	public InputStream read(StorageDevice device, String rltPath) throws E5Exception;
	
	/**
	 * ��һ����д�뵽ָ���洢�豸��ָ��·����
	 * @param deviceName �洢�豸����
	 * @param rltPath �����ļ������·���������ļ���
	 * @param in	������
	 * @param deviceType ָ���洢�豸������
	 * @throws E5Exception
	 */
	public void write(String deviceName, String rltPath, InputStream in, int deviceType) throws E5Exception;
	
	/**
	 * ��һ����д�뵽ָ���洢�豸��ָ��·����
	 * @param device �洢�豸
	 * @param rltPath �����ļ������·���������ļ���
	 * @param in	������
	 * @param deviceType ָ���洢�豸������
	 * @throws E5Exception
	 */
	public void write(StorageDevice device, String rltPath, InputStream in, int deviceType) throws E5Exception;
	
	/**
	 * ��ָ���Ĵ洢�豸��ָ����·����ȡһ���ļ���
	 * @param deviceName �洢�豸����
	 * @param rltPath �����ļ������·���������ļ���
	 * @param deviceType ָ���洢�豸������
	 * @return �����
	 * @throws E5Exception
	 */
	public InputStream read(String deviceName, String rltPath, int deviceType) throws E5Exception;

	/**
	 * ��ָ���Ĵ洢�豸��ָ����·����ȡһ���ļ���
	 * @param device �洢�豸
	 * @param rltPath �����ļ������·���������ļ���
	 * @param deviceType ָ���洢�豸������
	 * @return �����
	 * @throws E5Exception
	 */
	public InputStream read(StorageDevice device, String rltPath, int deviceType) throws E5Exception;

	/**
	 * @param deviceName �洢�豸����
	 * @return
	 * @throws E5Exception
	 */
	public FtpReader getFtpReader(String deviceName)throws E5Exception;
}