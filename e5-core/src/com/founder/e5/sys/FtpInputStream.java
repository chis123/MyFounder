package com.founder.e5.sys;

import java.io.IOException;
import java.io.InputStream;

public class FtpInputStream extends InputStream
{
	private InputStream readInputStream = null;

	private FtpReader ftpReader = null;

	private FtpInputStream()
	{
		super();
	}

	public FtpInputStream(FtpReader ftpReader, String path, String fileName)
	{
		this.ftpReader = ftpReader;
		try
		{
			ftpReader.connect();
			readInputStream = ftpReader.read(path, fileName);
		} catch (Exception ex) {
			readInputStream = null;
		}
	}

	public int read() throws IOException
	{
		if (readInputStream == null)
		{
			return 0;
		}
		return readInputStream.read();
	}

	public int read(byte b[]) throws IOException
	{
		if (readInputStream == null)
		{
			return 0;
		}
		return readInputStream.read(b);
	}

	public int read(byte b[], int off, int len) throws IOException
	{
		if (readInputStream == null)
		{
			return 0;
		}
		return readInputStream.read(b, off, len);
	}

	public long skip(long n) throws IOException
	{
		if (readInputStream == null)
		{
			return 0;
		}
		return readInputStream.skip(n);
	}

	public int available() throws IOException
	{
		if (readInputStream == null)
		{
			return 0;
		}
		return readInputStream.available();
	}
	/**
	 * 当内部流为空时，返回true
	 * @return
	 */
	public boolean isNull()
	{
		return (readInputStream == null);
	}

	public void close() throws IOException
	{
		if (readInputStream != null)
		{
			readInputStream.close();
			try
			{
				ftpReader.disconnect();
				readInputStream = null;
			} catch (Exception ex)
			{

			}
		}
	}

	public synchronized void mark(int readlimit)
	{
		if (readInputStream == null)
		{
			return;
		}
		readInputStream.mark(readlimit);
	}

	public synchronized void reset() throws IOException
	{
		if (readInputStream == null)
		{
			return;
		}
		readInputStream.reset();
	}

	public boolean markSupported()
	{
		return readInputStream.markSupported();
	}

}
