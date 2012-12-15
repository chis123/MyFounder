package com.founder.e5.db;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * postgreSQL resultset的处理
 * 
 * @author wanghc
 * @created 2009-3-23 上午10:43:17
 */
class PostgreSQLResultSet extends BaseResultSet implements IResultSet
{
	public PostgreSQLResultSet( ResultSet underlying ) {
		super( underlying );
	}
	
	public Clob getClob( int i ) throws SQLException {
		return new PostgreSQLClobAdapter(underlying.getCharacterStream(i));
	}
	
	public Clob getClob( String colName ) throws SQLException {
		return new PostgreSQLClobAdapter(underlying.getCharacterStream( colName ));
	}
	
	/* (non-Javadoc)
	 * @see com.founder.e5.db.ResultSetWrapper#getBlob(int)
	 */
	public Blob getBlob(int i) throws SQLException
	{
		return new PostgreSQLBlobAdapter(underlying.getBinaryStream(i));
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.db.ResultSetWrapper#getBlob(java.lang.String)
	 */
	public Blob getBlob(String colName) throws SQLException
	{
		return new PostgreSQLBlobAdapter(underlying.getBinaryStream(colName));
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.db.BaseResultSet#getBlob2(int)
	 */
	public IBlob getBlob2(int i) throws SQLException, IOException
	{
		return  LobHelper.createBlob(LobHelper.readBlob(underlying.getBinaryStream(i)));
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.db.BaseResultSet#getBlob2(java.lang.String)
	 */
	public IBlob getBlob2(String columnName) throws SQLException, IOException
	{
		return  LobHelper.createBlob(LobHelper.readBlob(underlying.getBinaryStream(columnName)));
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.db.BaseResultSet#getClob2(int)
	 */
	public IClob getClob2(int i) throws SQLException, IOException
	{
		return LobHelper.createClob(LobHelper.readClob(underlying.getCharacterStream(i)));	
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.db.BaseResultSet#getClob2(java.lang.String)
	 */
	public IClob getClob2(String columnName) throws SQLException, IOException
	{
		return LobHelper.createClob(LobHelper.readClob(underlying.getCharacterStream(columnName)));	
	}
}

class PostgreSQLClobAdapter implements Clob
{
	private Reader reader = null;
	
	PostgreSQLClobAdapter(Reader reader){
		this.reader = reader;
	}

	/* (non-Javadoc)
	 * @see java.sql.Clob#getCharacterStream()
	 */
	public Reader getCharacterStream() throws SQLException
	{
		return reader;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		char[] buff = new char[1024];
		int len = 0;
		try
		{
			while((len = reader.read(buff))>0)
			{
				sb.append(buff,0,len);
			}
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		return sb.toString();
	}
	
	/* (non-Javadoc)
	 * @see java.sql.Clob#getAsciiStream()
	 */
	public InputStream getAsciiStream() throws SQLException
	{
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see java.sql.Clob#getSubString(long, int)
	 */
	public String getSubString(long pos, int length) throws SQLException{
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Clob#length()
	 */
	public long length() throws SQLException{
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Clob#position(java.sql.Clob, long)
	 */
	public long position(Clob searchstr, long start) throws SQLException
	{
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Clob#position(java.lang.String, long)
	 */
	public long position(String searchstr, long start) throws SQLException
	{
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Clob#setAsciiStream(long)
	 */
	public OutputStream setAsciiStream(long pos) throws SQLException
	{
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Clob#setCharacterStream(long)
	 */
	public Writer setCharacterStream(long pos) throws SQLException
	{
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Clob#setString(long, java.lang.String, int, int)
	 */
	public int setString(long pos, String str, int offset, int len) throws SQLException
	{
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Clob#setString(long, java.lang.String)
	 */
	public int setString(long pos, String str) throws SQLException
	{
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Clob#truncate(long)
	 */
	public void truncate(long len) throws SQLException
	{
		throw new UnsupportedOperationException();
	}
}

class PostgreSQLBlobAdapter implements Blob
{
	private InputStream in = null;
	PostgreSQLBlobAdapter(InputStream in ){
		this.in = in;
	}
	
	/* (non-Javadoc)
	 * @see java.sql.Blob#getBinaryStream()
	 */
	public InputStream getBinaryStream() throws SQLException
	{
		return in;
	}

	/* (non-Javadoc)
	 * @see java.sql.Blob#getBytes(long, int)
	 */
	public byte[] getBytes(long pos, int length) throws SQLException
	{
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Blob#length()
	 */
	public long length() throws SQLException
	{
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Blob#position(java.sql.Blob, long)
	 */
	public long position(Blob pattern, long start) throws SQLException
	{
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Blob#position(byte[], long)
	 */
	public long position(byte[] pattern, long start) throws SQLException
	{
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Blob#setBinaryStream(long)
	 */
	public OutputStream setBinaryStream(long pos) throws SQLException
	{
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Blob#setBytes(long, byte[], int, int)
	 */
	public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException
	{
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Blob#setBytes(long, byte[])
	 */
	public int setBytes(long pos, byte[] bytes) throws SQLException
	{
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Blob#truncate(long)
	 */
	public void truncate(long len) throws SQLException
	{
		throw new UnsupportedOperationException();			
	}
}