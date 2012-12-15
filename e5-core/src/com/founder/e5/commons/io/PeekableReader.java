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
 * ��java.io.Reader�ķ�װ���ṩ�ж����ף���ȡ��ָ��λ�ã�Ԥ����ǰ�еȹ���
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-3-6 21:05:49
 */
public class PeekableReader {

	private long pos; // ��ָ��λ��

	private PushbackReader reader;

	private static final int BUFFER_SIZE = 256;

	private int bufferSize = BUFFER_SIZE;

	private int lastChar; // ��һ�ζ�ȡ���ַ�

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
	 * ��ȡһ���ַ�����ǰ�ƶ�ָ��
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
	 * �жϵ�ǰ��ָ������λ���Ƿ����ס�<br>
	 * ���У�'\n'����Ϊ���ס�'\r\n'����Ϊ���ס�'\r'��Ҳ��Ϊ����<br>
	 * ע�⣺����ָ�봦��'\r'��'\n'֮�䣬���ָ������һλ��������true
	 * 
	 * @return
	 * @throws IOException
	 */
	public boolean isLineBegin() throws IOException {
		return isLineBegin( true );
	}

	/**
	 * �жϵ�ǰ��ָ������λ���Ƿ����ס�<br>
	 * ���У�'\n'����Ϊ���ס�'\r\n'����Ϊ���ס�'\r'��Ҳ��Ϊ����<br>
	 * 
	 * @param allowChangeCursorPos �Ƿ�����ı��ָ��λ��<br>
	 *            ע�⣺�������򵱶�ָ�봦��'\r'��'\n'֮�䣬��ָ������һλ�󷵻�true
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

			// ����'\r'������Ȼ��һ���ַ�Ϊ'\n'��������ı��ָ��λ��
			reader.unread( next );
			return true;
		}

		return false;
	}

	/**
	 * �쿴�ӵ�ǰ��ָ������λ�ÿ�ʼ����β���ַ������������ı��ָ��λ��<br>
	 * ע�⣺���쿴bufferSize���ַ�
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
				return new String( buf, 0, i ); // �������һ���ַ���-1��
			}

			else if ( c == '\n' || c == '\r' ) {
				reader.unread( buf, 0, i + 1 );
				return new String( buf, 0, i ); // �������һ���ַ���\r��\n��
			}
		}

		reader.unread( buf );
		return new String( buf );
	}

	/**
	 * ���ص�ǰ��ָ��λ��
	 */
	public long position() {
		return pos;
	}

	public void close() throws IOException {
		reader.close();
	}

}
