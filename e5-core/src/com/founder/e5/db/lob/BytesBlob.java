package com.founder.e5.db.lob;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.founder.e5.db.IBlob;
import com.founder.e5.db.util.CloseHelper;

/**
 * 字节数组实现的IBlob
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2005-7-20 15:58:43
 */
public class BytesBlob implements IBlob {

	private byte[] bytes;

	public BytesBlob( byte[] bytes ) {
		if ( bytes == null )
			//throw new NullPointerException(); 
			bytes = new byte[0];//2006-10-17 Gong Lijie
		this.bytes = bytes;
	}

	/**
	 * @see com.founder.e5.db.IBlob#toStream()
	 * @deprecated
	 */
	public InputStream toStream() {
		return new ByteArrayInputStream( bytes );
	}

	/**
	 * @see com.founder.e5.db.IBlob#toBytes()
	 */
	public byte[] toBytes() throws IOException {
		return bytes;
	}

	/**
	 * @see com.founder.e5.db.IBlob#writeTo(java.io.OutputStream)
	 */
	public void writeTo( OutputStream out ) throws IOException {
		out.write( bytes );
	}

	/**
	 * @throws IOException
	 * @see com.founder.e5.db.IBlob#writeTo(java.io.File)
	 */
	public void writeTo( File file ) throws IOException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream( file );
			writeTo( out );
		} finally {
			CloseHelper.closeQuietly( out );
		}
	}

	/**
	 * @see com.founder.e5.db.IBlob#length()
	 */
	public long length() {
		return bytes.length;
	}

	/**
	 * @see com.founder.e5.db.IBlob#getStream()
	 */
	public InputStream getStream() {
		return new ByteArrayInputStream( bytes );
	}

}
