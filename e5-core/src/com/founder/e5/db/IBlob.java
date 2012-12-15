package com.founder.e5.db;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Blob�������ͽӿڡ� ע�⣺һ��IBlobֻ�����һ�Σ�֮��ײ����͹ر��ˣ�BytesBlobʵ���ɶ��ʹ�ã�
 * 
 * @version 1.0
 * @created 07-����-2005 13:26:42
 */
public interface IBlob {

	/**
	 * �ṩ�����ķ�ʽȡblob����
	 * 
	 * @return
	 * @created 2005-7-20 16:10:47
	 * @deprecated replaced by getStream
	 */
	public InputStream toStream();

	/**
	 * ����blob���ݶ�ȡ��
	 * 
	 * @return
	 */
	public InputStream getStream();

	/**
	 * ȡ��blob�����ݲ�����
	 * 
	 * @return
	 * @throws IOException
	 * @created 2005-7-20 16:10:49
	 */
	public byte[] toBytes() throws IOException;

	/**
	 * blob�����������ָ������ <br>
	 * <br>
	 * ע�⣺��������Ҫ�û��Լ��ر�
	 * 
	 * @param out
	 * @throws IOException
	 */
	public void writeTo( OutputStream out ) throws IOException;

	/**
	 * blob�����������ָ���ļ�
	 * 
	 * @param file
	 * @throws IOException
	 */
	public void writeTo( File file ) throws IOException;

	/**
	 * ����blob���ֽ���
	 */
	public long length();

}
