
package com.founder.e5.db.lob;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import org.apache.commons.io.CopyUtils;

import com.founder.e5.db.IClob;
import com.founder.e5.db.util.CloseHelper;

/**
 * ReaderÊµÏÖµÄIClob
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2005-7-21 18:31:57
 */
public class ReaderClob implements IClob {

	private Reader reader;

	private int length;

	private boolean closed;

	/**
	 * @param reader
	 */
	public ReaderClob( Reader reader ) {
		if ( reader == null )
			throw new NullPointerException();

		this.reader = reader;
		this.length = Integer.MAX_VALUE;
	}

	/**
	 * @param reader
	 * @param length
	 */
	public ReaderClob( Reader reader, int length ) {
		if ( reader == null )
			throw new NullPointerException();
		this.reader = reader;
		this.length = length;
	}

	private void _check() {
		if ( closed ) {
			throw new IllegalStateException( "Reader already closed!" );
		}
	}

	/**
	 * @see com.founder.e5.db.IClob#toReader()
	 * @deprecated
	 */
	public Reader toReader() {
		_check();
		return reader;
	}

	/**
	 * @see com.founder.e5.db.IClob#toStream()
	 * @deprecated
	 */
	public InputStream toStream() {
		_check();
		return new InputStream() {

			public int read() throws IOException {
				return reader.read();
			}
		};
	}

	/**
	 * @see com.founder.e5.db.IClob#writeTo(java.io.Writer)
	 */
	public void writeTo( Writer out ) throws IOException {
		_check();

		try {
			CopyUtils.copy( reader, out );
		} finally {
			CloseHelper.closeQuietly( reader );
			reader = null;
			closed = true;
		}
	}

	/**
	 * @see com.founder.e5.db.IClob#writeTo(java.io.OutputStream)
	 */
	public void writeTo( OutputStream out ) throws IOException {
		_check();
		writeTo( new OutputStreamWriter( out ) );
	}

	/**
	 * @see com.founder.e5.db.IClob#writeTo(java.io.OutputStream,
	 *      java.lang.String)
	 */
	public void writeTo( OutputStream out, String encoding ) throws IOException {
		_check();
		writeTo( new OutputStreamWriter( out, encoding ) );
	}

	/**
	 * @see com.founder.e5.db.IClob#writeTo(java.io.File)
	 */
	public void writeTo( File file ) throws IOException {
		_check();

		FileOutputStream out = new FileOutputStream( file );
		try {
			writeTo( out );
		} finally {
			CloseHelper.closeQuietly( out );
		}
	}

	/**
	 * @see com.founder.e5.db.IClob#writeTo(java.io.File, java.lang.String)
	 */
	public void writeTo( File file, String encoding ) throws IOException {
		_check();

		FileOutputStream out = new FileOutputStream( file );
		try {
			writeTo( new FileOutputStream( file ), encoding );
		} finally {
			CloseHelper.closeQuietly( out );
		}
	}

	/**
	 * @see com.founder.e5.db.IClob#length()
	 */
	public int length() {
		return length;
	}

	/**
	 * @see com.founder.e5.db.IClob#getReader()
	 */
	public Reader getReader() {
		_check();
		return reader;
	}

}
