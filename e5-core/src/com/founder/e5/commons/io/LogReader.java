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
 * ��־�Ķ�����������ȡ��־����
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-3-6 18:29:17
 */
public class LogReader implements RecordBasedReader {

	protected File file;

	protected int totalRecords; // �ļ��ܼ�¼��

	private long length;

	private TIntLongHashMap index = null; // �ļ���������[int(��¼��) - long(ƫ����)]

	private int step = 20; // ��ַ���������step=100�����100�н���������200�н��������Դ�����

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
	 * ����ļ��Ƿ��ѱ��Ķ�,��Ķ����ؽ�����
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
	 * ���ļ�ָ����ָ����¼�е���ʼλ��
	 * 
	 * @param channel
	 * @param recordNum ָ����¼��
	 * @param pr
	 * @throws IOException
	 */
	private void position( FileChannel channel, int recordNum, PeekableReader pr )
			throws IOException {
		int segmentLength = step; // �γ���������
		int base = ( recordNum / segmentLength ) * segmentLength; // ��ַ����¼�ţ�
		int offset = recordNum % segmentLength; // ƫ��������¼����

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

		check(); // ����ļ��Ƿ��ѱ��Ķ�,��Ķ����ؽ�����

		FileInputStream fis = new FileInputStream( file );
		FileChannel channel = fis.getChannel();

		try {
			InputStreamReader isr = new InputStreamReader( fis );
			BufferedReader br = new BufferedReader( isr );
			PeekableReader pr = new PeekableReader( br );

			// ��ʼ�к�Ϊ1����ζ�Ŵ��ļ�ͷ��ʼ��
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
