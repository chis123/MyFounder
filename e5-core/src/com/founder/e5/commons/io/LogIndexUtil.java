/**
 * $Id: e5new com.founder.e5.commons.io LogIndexUtil.java 
 * created on 2006-3-6 13:14:55
 * by liyanhui
 */
package com.founder.e5.commons.io;

import gnu.trove.TIntLongHashMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.founder.e5.commons.ResourceMgr;

/**
 * ��������Ϊlog4j���ɵ���־�ļ���������������
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-3-6 13:14:55
 */
public class LogIndexUtil {

	/**
	 * Ϊ������־�ļ��еļ�¼����������
	 * 
	 * @param file ��־�ļ�
	 * @param segmentLength �γ�
	 * @return TIntLongHashMap[int(��¼��) - long(ƫ����)]
	 * @throws IOException
	 */
	public static TIntLongHashMap createIndex( File file, int segmentLength )
			throws IOException {
		BufferedReader br = new BufferedReader( new FileReader( file ) );
		try {
			return createIndex( br, segmentLength );
		} finally {
			ResourceMgr.closeQuietly( br );
		}
	}

	/**
	 * Ϊ������־�ļ��еļ�¼����������
	 * 
	 * @param reader ��־�ļ�
	 * @param segmentLength �γ�
	 * @return TIntLongHashMap[int(��¼��) - long(ƫ����)]
	 * @throws IOException
	 */
	public static TIntLongHashMap createIndex( Reader reader, int segmentLength )
			throws IOException {
		TIntLongHashMap map = new TIntLongHashMap();

		PeekableReader fr = new PeekableReader( reader );
		try {
			int recordNum = 1;
			map.put( 1, 0 ); // ��һ����λ��0��

			for ( int i = fr.read(); i != -1; i = fr.read() ) {

				if ( isNewRecordBegin( fr ) ) {
					recordNum++;
					if ( recordNum % segmentLength == 0 ) {
						long pos = fr.position();
						map.put( recordNum, pos );
					}
				}

			}

			// �ܼ�¼����������Ϊ0
			map.put( 0, recordNum );
		} finally {
			ResourceMgr.closeQuietly( reader );
		}
		return map;
	}

	/**
	 * �Ӵ���PeekableReader��ǰ��ָ��λ�ÿ�ʼ����ȡlength����¼���أ���ʱ��ָ��λ��<br>
	 * ��һ����¼����ʼ��
	 * 
	 * @param pr
	 * @param length
	 * @return
	 * @throws IOException
	 */
	public static List readRecords( PeekableReader pr, int length )
			throws IOException {
		ArrayList list = new ArrayList();

		while ( length-- > 0 ) {
			StringBuffer sb = null;

			int i = 0;
			for ( i = pr.read(); i != -1; i = pr.read() ) {
				if ( sb == null )
					sb = new StringBuffer();
				sb.append( ( char ) i );

				if ( isNewRecordBegin( pr ) ) {
					list.add( sb.toString() );
					break;
				}
			}

			if ( i == -1 ) {
				if ( sb != null ) {
					list.add( sb.toString() );
				}
				break;
			}
		}

		return list;
	}

	/**
	 * �ж�FreeReader�е�ǰ��ָ������λ���Ƿ�һ����¼����ʼ��
	 * 
	 * @param fr
	 * @return
	 * @throws IOException
	 */
	private static boolean isNewRecordBegin( PeekableReader fr )
			throws IOException {
		if ( fr.isLineBegin() ) {
			String line = fr.peekLine();
			return ( line == null ) || isNewRecord( line );
		}
		return false;
	}

	/**
	 * �жϸ����Ƿ��¼�¼
	 * 
	 * @param line
	 * @return
	 */
	private static boolean isNewRecord( String line ) {
		if ( line.length() > 10 ) {
			return isDate( line.substring( 0, 10 ) );
		}
		return false;
	}

	/**
	 * �жϸ����ֽ��Ƿ�yyyy-mm-dd����ʽ�����ڴ�
	 */
	private static boolean isDate( String str ) {
		for ( int i = 0; i < str.length(); i++ ) {
			char c = str.charAt( i );
			switch ( i ) {
				case 0 :
				case 1 :
				case 2 :
				case 3 :
				case 5 :
				case 6 :
				case 8 :
				case 9 :
					if ( !Character.isDigit( c ) )
						return false;
					break;
				case 4 :
				case 7 :
					if ( c != '-' )
						return false;
					break;

				default :
					break;
			}
		}

		return true;
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main( String[] args ) throws IOException {
		File file = new File( "c:/hibernate.log" );
		TIntLongHashMap map = createIndex( file, 1 );

		int[] keys = map.keys();
		Arrays.sort( keys );
		for ( int i = 0; i < keys.length; i++ ) {
			int num = keys[i];
			long pos = map.get( num );

			if ( num != 0 )
				System.out.println( num + "=" + pos + "\t"
						+ readLine( file, pos ) );
		}

	}

	static String readLine( File file, long startPos ) throws IOException {
		FileInputStream fis = new FileInputStream( file );
		FileChannel chan = fis.getChannel();
		chan.position( startPos );

		BufferedReader br = new BufferedReader( new InputStreamReader( fis ) );
		String line = br.readLine();

		fis.close();
		return line;
	}

}
