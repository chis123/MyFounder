package com.founder.e5.db;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * Clob数据类型接口
 * 
 * @version 1.0
 * @created 07-七月-2005 13:26:50
 */
public interface IClob {

	/**
	 * 转换为java.io.Reader
	 * 
	 * @deprecated replaced by getReader
	 */
	public Reader toReader();

	/**
	 * 返回Clob数据读取流
	 * 
	 * @return
	 */
	public Reader getReader();

	/**
	 * 转换为java.io.InputStream
	 * 
	 * @deprecated
	 */
	public InputStream toStream();

	/**
	 * 转换为字符串内容
	 */
	public String toString();

	/**
	 * 写到给定Writer，采用默认编码。 <br>
	 * <br>
	 * 注意：传入流需要用户自己关闭
	 * 
	 * @param out
	 * @throws IOException
	 */
	public void writeTo( Writer out ) throws IOException;

	/**
	 * 写到指定流，采用默认编码。 <br>
	 * <br>
	 * 注意：传入流需要用户自己关闭
	 * 
	 * @param out
	 * @throws IOException
	 */
	public void writeTo( OutputStream out ) throws IOException;

	/**
	 * 写到给定流，采用指定编码。 <br>
	 * <br>
	 * 注意：传入流需要用户自己关闭
	 * 
	 * @param out
	 * @param encoding
	 * @throws IOException
	 */
	public void writeTo( OutputStream out, String encoding ) throws IOException;

	/**
	 * 写到指定文件
	 * 
	 * @param file
	 * @throws IOException
	 */
	public void writeTo( File file ) throws IOException;

	/**
	 * 以给定编码写到指定文件
	 * 
	 * @param file
	 * @param encoding
	 * @throws IOException
	 */
	public void writeTo( File file, String encoding ) throws IOException;

	/**
	 * 返回clob包含的字符数
	 */
	public int length();

}
