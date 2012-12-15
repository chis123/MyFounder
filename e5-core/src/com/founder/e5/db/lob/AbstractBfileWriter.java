package com.founder.e5.db.lob;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.founder.e5.db.BfileException;
import com.founder.e5.db.IBfile;

/**
 * 抽象的写入型bfile实现
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-4-21 9:47:09
 */
public abstract class AbstractBfileWriter implements IBfile {

	protected String directory;

	protected String file;

	protected InputStream input;

	/**
	 * @param directory
	 * @param file
	 * @param input
	 */
	public AbstractBfileWriter( String directory, String file, InputStream input ) {
		this.directory = directory;
		this.file = file;
		this.input = input;
	}

	public String getDirectory() throws BfileException {
		return directory;
	}

	public String getFile() throws BfileException {
		return file;
	}

	public InputStream getStream() {
		return input;
	}

	/**
	 * @see com.founder.e5.db.IBfile#openFile()
	 */
	public InputStream openFile() throws BfileException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.founder.e5.db.IBfile#closeFile()
	 */
	public void closeFile() throws BfileException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.founder.e5.db.IBfile#writeTo(java.io.OutputStream)
	 */
	public void writeTo( OutputStream out ) throws IOException, BfileException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.founder.e5.db.IBfile#writeTo(java.io.File)
	 */
	public void writeTo( File file ) throws IOException, BfileException {
		throw new UnsupportedOperationException();
	}

}
