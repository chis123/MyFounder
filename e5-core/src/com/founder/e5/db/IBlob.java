package com.founder.e5.db;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Blob数据类型接口。 注意：一般IBlob只能输出一次，之后底层流就关闭了（BytesBlob实例可多次使用）
 * 
 * @version 1.0
 * @created 07-七月-2005 13:26:42
 */
public interface IBlob {

	/**
	 * 提供以流的方式取blob数据
	 * 
	 * @return
	 * @created 2005-7-20 16:10:47
	 * @deprecated replaced by getStream
	 */
	public InputStream toStream();

	/**
	 * 返回blob数据读取流
	 * 
	 * @return
	 */
	public InputStream getStream();

	/**
	 * 取出blob的数据并返回
	 * 
	 * @return
	 * @throws IOException
	 * @created 2005-7-20 16:10:49
	 */
	public byte[] toBytes() throws IOException;

	/**
	 * blob的数据输出到指定流。 <br>
	 * <br>
	 * 注意：传入流需要用户自己关闭
	 * 
	 * @param out
	 * @throws IOException
	 */
	public void writeTo( OutputStream out ) throws IOException;

	/**
	 * blob的数据输出到指定文件
	 * 
	 * @param file
	 * @throws IOException
	 */
	public void writeTo( File file ) throws IOException;

	/**
	 * 返回blob的字节数
	 */
	public long length();

}
