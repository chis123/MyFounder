/**
 * $Id: e5new com.founder.e5.commons.io IndexBuilder.java 
 * created on 2006-3-6 21:05:49
 * by liyanhui
 */
package com.founder.e5.commons.io;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

/**
 * 对java.io.Reader的封装，提供判断行首，获取读指针位置，预读当前行等功能
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-3-6 21:05:49
 */
public class PeekableReader {

	private long pos; // 读指针位置

	private PushbackReader reader;

	private static final int BUFFER_SIZE = 256;

	private int bufferSize = BUFFER_SIZE;

	private int lastChar; // 上一次读取的字符

	/**
	 * @param reader
	 */
	public PeekableReader( Reader reader ) {
		this.reader = new PushbackReader( reader, bufferSize );
	}

	/**
	 * @param reader
	 * @param bufferSize
	 */
	public PeekableReader( Reader reader, int bufferSize ) {
		if ( bufferSize < 1 )
			throw new IllegalArgumentException( "bufferSize too small" );

		this.reader = new PushbackReader( reader, bufferSize );
		this.bufferSize = bufferSize;
	}

	/**
	 * 读取一个字符，并前移读指针
	 * 
	 * @return
	 * @throws IOException
	 */
	public int read() throws IOException {
		lastChar = reader.read();
		if ( lastChar != -1 ) {
			pos++;
			return lastChar;
		}

		return -1;
	}

	/**
	 * 判断当前读指针所处位置是否行首。<br>
	 * 其中，'\n'后认为行首、'\r\n'后认为行首、'\r'后也认为行首<br>
	 * 注意：若读指针处于'\r'与'\n'之间，则读指针下移一位，并返回true
	 * 
	 * @return
	 * @throws IOException
	 */
	public boolean isLineBegin() throws IOException {
		return isLineBegin( true );
	}

	/**
	 * 判断当前读指针所处位置是否行首。<br>
	 * 其中，'\n'后认为行首、'\r\n'后认为行首、'\r'后也认为行首<br>
	 * 
	 * @param allowChangeCursorPos 是否允许改变读指针位置<br>
	 *            注意：若允许，则当读指针处于'\r'与'\n'之间，读指针下移一位后返回true
	 * @return
	 * @throws IOException
	 */
	public boolean isLineBegin( boolean allowChangeCursorPos )
			throws IOException {
		if ( lastChar == '\n' )
			return true;

		if ( lastChar == '\r' ) {
			int next = reader.read();

			if ( next == -1 ) {
				return true;
			}

			if ( next == '\n' && allowChangeCursorPos ) {
				pos++;
				return true;
			}

			// 单纯'\r'或者虽然下一个字符为'\n'但不允许改变读指针位置
			reader.unread( next );
			return true;
		}

		return false;
	}

	/**
	 * 察看从当前读指针所处位置开始至行尾的字符串，但并不改变读指针位置<br>
	 * 注意：最多察看bufferSize个字符
	 * 
	 * @return
	 * @throws IOException
	 */
	public String peekLine() throws IOException {
		char[] buf = new char[ bufferSize ];

		for ( int i = 0; i < bufferSize; i++ ) {
			int c = reader.read();
			buf[i] = ( char ) c;

			if ( c == -1 ) {
				reader.unread( buf, 0, i );
				return new String( buf, 0, i ); // 丢掉最后一个字符（-1）
			}

			else if ( c == '\n' || c == '\r' ) {
				reader.unread( buf, 0, i + 1 );
				return new String( buf, 0, i ); // 丢掉最后一个字符（\r或\n）
			}
		}

		reader.unread( buf );
		return new String( buf );
	}

	/**
	 * 返回当前读指针位置
	 */
	public long position() {
		return pos;
	}

	public void close() throws IOException {
		reader.close();
	}

}
