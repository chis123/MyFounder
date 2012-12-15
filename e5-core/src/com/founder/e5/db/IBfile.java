package com.founder.e5.db;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Bfile数据类型接口。该接口提供一种统一的方式看待"真正的"bfile（oracle bfile）和"伪"bfile（外部文件）<br>
 * <br>
 * 注意：该接口同时包含用于读取bfile的方法和用于写入bfile的方法，不要求实现者实现所有方法；
 * 实际中常常是实现一个读取型bfile或写入型bfile。 <br>
 * <br>
 * 读接口提供两种使用Bfile的方式：<br>
 * 1)、openFile() -> closeFile()<br>
 * 2)、writeTo()<br>
 * <br>
 * 写接口的getPlaceHolder()方法用于生成sql片段，如对oracle来说是"bfilename(?, ?)"，对外部文件实现来说是"?"；<br>
 * 另外，如果特定实现者只希望修改bfile内容的"链接"（即"目录-文件"值对），则可以提供一个store()方法的空实现
 * 
 * @version 1.0
 * @created 07-七月-2005 13:26:32
 */
public interface IBfile {

	// ---------------------------------------------------------- 通用接口

	/**
	 * 取得bfile的目录部分
	 * 
	 * @throws BfileException
	 */
	public String getDirectory() throws BfileException;

	/**
	 * 取得bfile的文件部分
	 * 
	 * @throws BfileException
	 */
	public String getFile() throws BfileException;

	// ------------------------------------------------- 读接口

	/**
	 * 打开文件，并返回输入流<br>
	 * <br>
	 * 注意：读取数据完毕后应该先关闭流，然后调用closeFile()关闭文件<br>
	 * （因为要用在Oracle上，这里不得不遵守Oracle中BFILE的使用方式）
	 * 
	 * @return 文件输入流
	 * 
	 * @throws BfileException
	 * @created 2005-8-5 14:02:07
	 */
	public InputStream openFile() throws BfileException;

	/**
	 * 关闭文件
	 * 
	 * @throws BfileException
	 * @created 2005-8-5 14:02:09
	 */
	public void closeFile() throws BfileException;
	
	/**
	 * bfile数据写到指定流 <br>
	 * <br>
	 * 注意：输出流需要用户自己关闭
	 * 
	 * @param out
	 * @throws IOException
	 * @throws BfileException
	 */
	public void writeTo( OutputStream out ) throws IOException, BfileException;

	/**
	 * bfile数据写到指定文件
	 * 
	 * @param file
	 * @throws IOException
	 * @throws BfileException
	 */
	public void writeTo( File file ) throws IOException, BfileException;

	// ----------------------------------------------------------- 写接口

	/**
	 * 返回bfile类型字段在sql语句中的占位符
	 * 
	 * @return
	 */
	public String getPlaceHolder();

	/**
	 * 把bfile值绑定到已准备语句中
	 * 
	 * @param pst jdbc已准备语句对象
	 * @param index bfile类型字段索引
	 * @return 语句中下一个待绑定参数的索引
	 * @throws SQLException
	 */
	public int setParameter( PreparedStatement pst, int index )
			throws SQLException;

	/**
	 * 存储bfile数据到目的地
	 * 
	 * @throws Exception
	 */
	public void store() throws Exception;

}
