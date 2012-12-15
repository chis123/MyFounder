/**
 * $Id: e5new com.founder.e5.commons.io RecordBasedReader.java 
 * created on 2006-3-6 16:58:34
 * by liyanhui
 */
package com.founder.e5.commons.io;

import java.io.IOException;
import java.util.List;

/**
 * 基于记录的文件访问接口
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-3-6 16:58:34
 */
public interface RecordBasedReader {

	/**
	 * 从指定序号的记录开始读，读取给定条数或到达文件末尾后返回
	 * 
	 * @param recordNo 开始记录号
	 * @param length 读取条数
	 * @return 从指定序号记录开始的length条记录内容
	 * @throws IOException
	 */
	public List read( int recordNo, int length ) throws IOException;

	/**
	 * 返回文件中包含的总记录数
	 * 
	 * @return 文件中包含的总记录数
	 * @throws IOException
	 */
	public int totalRecords() throws IOException;

}
