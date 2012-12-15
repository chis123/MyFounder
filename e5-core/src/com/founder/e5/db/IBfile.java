package com.founder.e5.db;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Bfile�������ͽӿڡ��ýӿ��ṩһ��ͳһ�ķ�ʽ����"������"bfile��oracle bfile����"α"bfile���ⲿ�ļ���<br>
 * <br>
 * ע�⣺�ýӿ�ͬʱ�������ڶ�ȡbfile�ķ���������д��bfile�ķ�������Ҫ��ʵ����ʵ�����з�����
 * ʵ���г�����ʵ��һ����ȡ��bfile��д����bfile�� <br>
 * <br>
 * ���ӿ��ṩ����ʹ��Bfile�ķ�ʽ��<br>
 * 1)��openFile() -> closeFile()<br>
 * 2)��writeTo()<br>
 * <br>
 * д�ӿڵ�getPlaceHolder()������������sqlƬ�Σ����oracle��˵��"bfilename(?, ?)"�����ⲿ�ļ�ʵ����˵��"?"��<br>
 * ���⣬����ض�ʵ����ֻϣ���޸�bfile���ݵ�"����"����"Ŀ¼-�ļ�"ֵ�ԣ���������ṩһ��store()�����Ŀ�ʵ��
 * 
 * @version 1.0
 * @created 07-����-2005 13:26:32
 */
public interface IBfile {

	// ---------------------------------------------------------- ͨ�ýӿ�

	/**
	 * ȡ��bfile��Ŀ¼����
	 * 
	 * @throws BfileException
	 */
	public String getDirectory() throws BfileException;

	/**
	 * ȡ��bfile���ļ�����
	 * 
	 * @throws BfileException
	 */
	public String getFile() throws BfileException;

	// ------------------------------------------------- ���ӿ�

	/**
	 * ���ļ���������������<br>
	 * <br>
	 * ע�⣺��ȡ������Ϻ�Ӧ���ȹر�����Ȼ�����closeFile()�ر��ļ�<br>
	 * ����ΪҪ����Oracle�ϣ����ﲻ�ò�����Oracle��BFILE��ʹ�÷�ʽ��
	 * 
	 * @return �ļ�������
	 * 
	 * @throws BfileException
	 * @created 2005-8-5 14:02:07
	 */
	public InputStream openFile() throws BfileException;

	/**
	 * �ر��ļ�
	 * 
	 * @throws BfileException
	 * @created 2005-8-5 14:02:09
	 */
	public void closeFile() throws BfileException;
	
	/**
	 * bfile����д��ָ���� <br>
	 * <br>
	 * ע�⣺�������Ҫ�û��Լ��ر�
	 * 
	 * @param out
	 * @throws IOException
	 * @throws BfileException
	 */
	public void writeTo( OutputStream out ) throws IOException, BfileException;

	/**
	 * bfile����д��ָ���ļ�
	 * 
	 * @param file
	 * @throws IOException
	 * @throws BfileException
	 */
	public void writeTo( File file ) throws IOException, BfileException;

	// ----------------------------------------------------------- д�ӿ�

	/**
	 * ����bfile�����ֶ���sql����е�ռλ��
	 * 
	 * @return
	 */
	public String getPlaceHolder();

	/**
	 * ��bfileֵ�󶨵���׼�������
	 * 
	 * @param pst jdbc��׼��������
	 * @param index bfile�����ֶ�����
	 * @return �������һ�����󶨲���������
	 * @throws SQLException
	 */
	public int setParameter( PreparedStatement pst, int index )
			throws SQLException;

	/**
	 * �洢bfile���ݵ�Ŀ�ĵ�
	 * 
	 * @throws Exception
	 */
	public void store() throws Exception;

}
