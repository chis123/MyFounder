package com.founder.e5.db;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * Clob�������ͽӿ�
 * 
 * @version 1.0
 * @created 07-����-2005 13:26:50
 */
public interface IClob {

	/**
	 * ת��Ϊjava.io.Reader
	 * 
	 * @deprecated replaced by getReader
	 */
	public Reader toReader();

	/**
	 * ����Clob���ݶ�ȡ��
	 * 
	 * @return
	 */
	public Reader getReader();

	/**
	 * ת��Ϊjava.io.InputStream
	 * 
	 * @deprecated
	 */
	public InputStream toStream();

	/**
	 * ת��Ϊ�ַ�������
	 */
	public String toString();

	/**
	 * д������Writer������Ĭ�ϱ��롣 <br>
	 * <br>
	 * ע�⣺��������Ҫ�û��Լ��ر�
	 * 
	 * @param out
	 * @throws IOException
	 */
	public void writeTo( Writer out ) throws IOException;

	/**
	 * д��ָ����������Ĭ�ϱ��롣 <br>
	 * <br>
	 * ע�⣺��������Ҫ�û��Լ��ر�
	 * 
	 * @param out
	 * @throws IOException
	 */
	public void writeTo( OutputStream out ) throws IOException;

	/**
	 * д��������������ָ�����롣 <br>
	 * <br>
	 * ע�⣺��������Ҫ�û��Լ��ر�
	 * 
	 * @param out
	 * @param encoding
	 * @throws IOException
	 */
	public void writeTo( OutputStream out, String encoding ) throws IOException;

	/**
	 * д��ָ���ļ�
	 * 
	 * @param file
	 * @throws IOException
	 */
	public void writeTo( File file ) throws IOException;

	/**
	 * �Ը�������д��ָ���ļ�
	 * 
	 * @param file
	 * @param encoding
	 * @throws IOException
	 */
	public void writeTo( File file, String encoding ) throws IOException;

	/**
	 * ����clob�������ַ���
	 */
	public int length();

}
