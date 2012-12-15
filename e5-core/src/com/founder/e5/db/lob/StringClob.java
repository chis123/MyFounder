package com.founder.e5.db.lob;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

import com.founder.e5.db.IClob;
import com.founder.e5.db.util.CloseHelper;

/**
 * String实现的IClob
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2005-7-21 18:28:02
 */
public class StringClob implements IClob {

	private String content;

	/**
	 * @param content
	 */
	public StringClob( String content ) {
		if ( content == null )
			//throw new NullPointerException(); 
			content = "";//2006-10-17 Gong Lijie
		this.content = content;
	}

	/**
	 * @see com.founder.e5.db.IClob#toReader()
	 * @deprecated
	 */
	public Reader toReader() {
		return new StringReader( content );
	}

	/**
	 * @see com.founder.e5.db.IClob#toStream()
	 * @deprecated
	 */
	public InputStream toStream() {
		return new ByteArrayInputStream( content.getBytes() );
	}

	/**
	 * @see com.founder.e5.db.IClob#writeTo(java.io.Writer)
	 */
	public void writeTo( Writer out ) throws IOException {
		out.write( content );
	}

	/**
	 * @see com.founder.e5.db.IClob#writeTo(java.io.OutputStream)
	 */
	public void writeTo( OutputStream out ) throws IOException {
		writeTo( new OutputStreamWriter( out ) );
	}

	/**
	 * @see com.founder.e5.db.IClob#writeTo(java.io.OutputStream,
	 *      java.lang.String)
	 */
	public void writeTo( OutputStream out, String encoding ) throws IOException {
		writeTo( new OutputStreamWriter( out, encoding ) );
	}

	/**
	 * @see com.founder.e5.db.IClob#writeTo(java.io.File)
	 */
	public void writeTo( File file ) throws IOException {
		FileWriter out = new FileWriter( file );
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
		FileOutputStream out = new FileOutputStream( file );
		try {
			writeTo( out, encoding );
		} finally {
			CloseHelper.closeQuietly( out );
		}
	}

	/**
	 * @see com.founder.e5.db.IClob#length()
	 */
	public int length() {
		return content.length();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return content;
	}

	/**
	 * @see com.founder.e5.db.IClob#getReader()
	 */
	public Reader getReader() {
		return new StringReader( content );
	}

	/**
	 * StringClob作用同String，因此比较string值
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals( Object obj ) {
		if ( obj != null )
			return content.equals( obj.toString() );
		return false;
	}

}
