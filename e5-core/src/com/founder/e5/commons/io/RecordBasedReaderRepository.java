/**
 * $Id: e5new com.founder.e5.commons.io RecordBasedReaderRepository.java 
 * created on 2006-3-7 9:19:17
 * by liyanhui
 */
package com.founder.e5.commons.io;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

/**
 * 提供一个存储位置，在类空间范围内共享RecordBasedReader实例
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-3-7 9:19:17
 */
public class RecordBasedReaderRepository {

	private static Hashtable cache = new Hashtable();

	/**
	 * 获取关联到给定文件的RecordBasedReader实例
	 * 
	 * @param pathname
	 * @return
	 * @throws IOException
	 */
	public static RecordBasedReader getReader( String pathname )
			throws IOException {
		return getReader( new File( pathname ) );
	}

	/**
	 * 获取关联到给定文件的RecordBasedReader实例
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static RecordBasedReader getReader( File file ) throws IOException {
		RecordBasedReader reader = ( RecordBasedReader ) cache.get( file );
		if ( reader == null ) {
			reader = new LogReader( file );
			cache.put( file, reader );
		}
		return reader;
	}

}
