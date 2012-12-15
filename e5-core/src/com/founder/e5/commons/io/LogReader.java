/**
 * $Id: e5new com.founder.e5.commons.io RecordBasedReaderImpl.java 
 * created on 2006-3-6 18:29:17
 * by liyanhui
 */
package com.founder.e5.commons.io;

import gnu.trove.TIntLongHashMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.List;

import com.founder.e5.commons.ResourceMgr;

/**
 * 日志阅读器，按条读取日志数据
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-3-6 18:29:17
 */
public class LogReader implements RecordBasedReader {

	protected File file;

	protected int totalRecords; // 文件总记录数

	private long length;

	private TIntLongHashMap index = null; // 文件索引数据[int(记录号) - long(偏移量)]

	private int step = 20; // 基址间隔，如若step=100，则第100行建索引，第200行建索引，以此类推

	/**
	 * @param file
	 * @throws IOException
	 */
	public LogReader( File file ) throws IOException {
		this.file = file;
		this.length = file.length();
		buildIndex();
	}

	private void buildIndex() throws IOException {
		TIntLongHashMap map = LogIndexUtil.createIndex( file, step );
		this.totalRecords = ( int ) map.remove( 0 );
		this.index = map;
	}

	/**
	 * 检查文件是否已被改动,如改动则重建索引
	 * 
	 * @throws IOException
	 */
	private void check() throws IOException {
		long l = file.length();
		if ( l != length ) {
			buildIndex();
			length = l;
		}
	}

	/**
	 * 置文件指针于指定记录行的起始位置
	 * 
	 * @param channel
	 * @param recordNum 指定记录号
	 * @param pr
	 * @throws IOException
	 */
	private void position( FileChannel channel, int recordNum, PeekableReader pr )
			throws IOException {
		int segmentLength = step; // 段长（行数）
		int base = ( recordNum / segmentLength ) * segmentLength; // 基址（记录号）
		int offset = recordNum % segmentLength; // 偏移量（记录数）

		if ( base != 0 ) {
			long basePosition = index.get( base );
			channel.position( basePosition );
		} else {
			channel.position( 0 );
		}

		if ( offset != 0 )
			LogIndexUtil.readRecords( pr, offset );
	}

	/**
	 * @see com.founder.e5.commons.io.RecordBasedReader#read(int, int)
	 */
	public List read( int recordNum, int length ) throws IOException {
		if ( length < 0 )
			throw new IllegalArgumentException(
					"length must be an unsigned integer." );
		if ( length == 0 )
			return null;

		check(); // 检查文件是否已被改动,如改动则重建索引

		FileInputStream fis = new FileInputStream( file );
		FileChannel channel = fis.getChannel();

		try {
			InputStreamReader isr = new InputStreamReader( fis );
			BufferedReader br = new BufferedReader( isr );
			PeekableReader pr = new PeekableReader( br );

			// 开始行号为1，意味着从文件头开始读
			if ( recordNum > 1 ) {
				position( channel, recordNum, pr );
			}

			List result = LogIndexUtil.readRecords( pr, length );
			return result;

		} finally {
			ResourceMgr.closeQuietly( fis );
		}

	}

	/**
	 * @see com.founder.e5.commons.io.RecordBasedReader#totalRecords()
	 */
	public int totalRecords() throws IOException {
		return totalRecords;
	}

	public static void main( String[] args ) throws IOException {
		String s = "c:/e5cxt.log";
		File file = new File( s );
		LogReader reader = new LogReader( file );

		int cnt = reader.totalRecords();
		for ( int i = 0; i < cnt; i++ ) {
			System.out.println( "#" + ( i + 1 ) );
			System.out.println( reader.read( i + 1, 1 ) );
		}
	}
}
