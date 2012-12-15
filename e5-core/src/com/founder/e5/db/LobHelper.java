package com.founder.e5.db;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.io.CopyUtils;

import com.founder.e5.db.lob.BytesBlob;
import com.founder.e5.db.lob.ReaderClob;
import com.founder.e5.db.lob.StreamBlob;
import com.founder.e5.db.lob.StringClob;
import com.founder.e5.db.util.CloseHelper;

/**
 * ���û�����Lob����ʵ���ĸ�����
 * 
 * @version 1.0
 * @created 08-����-2005 10:41:42
 */
public class LobHelper {

	/**
	 * ����һ���Ը����ַ���Ϊ���ݵ�IClob
	 * 
	 * @param content
	 */
	public static IClob createClob( String content ) {
		return new StringClob( content );
	}

	/**
	 * ����һ���Ը����ļ�Ϊ���ݵ�IClob
	 * 
	 * @param file
	 * @throws FileNotFoundException
	 */
	public static IClob createClob( File file ) throws FileNotFoundException {
		// ���ﱾӦ�����ַ��������ֽ����ض������ַ���������clob����Ӧ��û����
		return new ReaderClob( new FileReader( file ), ( int ) file.length() );
	}

	/**
	 * ��ȡ�ļ����ݣ�����IClob����
	 * 
	 * @param file
	 * @param encoding �ļ�����
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static IClob createClob( File file, String encoding )
			throws UnsupportedEncodingException, FileNotFoundException {
		return new ReaderClob( new InputStreamReader(
				new FileInputStream( file ),
				encoding ), ( int ) file.length() );
	}

	/**
	 * �������ͳ��ȴ���IClob����<br>
	 * lengthȡInteger.MAX_VALUE
	 * 
	 * @param reader
	 */
	public static IClob createClob( Reader reader ) {
		return new ReaderClob( reader );
	}

	/**
	 * �������ͳ��ȴ���IClob����<br>
	 * ע�⣺���length�Ǳ�Ҫ�ģ���ΪPreparedStatement.setCharacterStream��Ҫ��ֵ
	 * 
	 * @param reader
	 * @param length
	 */
	public static IClob createClob( Reader reader, int length ) {
		return new ReaderClob( reader, length );
	}

	/**
	 * 
	 * @param inputStream
	 * @deprecated
	 */
	public static IClob createClob( InputStream inputStream ) {
		return new ReaderClob( new InputStreamReader( inputStream ), 0 );
	}

	/**
	 * �����ڴ����ݴ���IBlob����
	 * 
	 * @param bytes
	 */
	public static IBlob createBlob( byte[] bytes ) {
		return new BytesBlob( bytes );
	}

	/**
	 * �����ļ����ݴ���IBlob����
	 * 
	 * @param file
	 * @throws FileNotFoundException
	 */
	public static IBlob createBlob( File file ) throws FileNotFoundException {
		return new StreamBlob( new FileInputStream( file ), file.length() );
	}

	/**
	 * ���������ݴ���IBlob����
	 * 
	 * @param inputStream
	 */
	public static IBlob createBlob( InputStream inputStream ) {
		return new StreamBlob( inputStream );
	}

	// ----------------------------------------------------------------------

	/**
	 * �൱��createBfile( directory, file, null, BfileType.EXTFILE )
	 * 
	 * @param directory
	 * @param file
	 */
	public static IBfile createBfile( String directory, String file ) {
		return createBfile( directory, file, null, BfileType.EXTFILE );
	}

	/**
	 * �൱��createBfile( directory, file, stream, BfileType.EXTFILE )
	 * 
	 * @param directory
	 * @param file
	 * @param stream
	 */
	public static IBfile createBfile( String directory, String file,
			InputStream stream ) {
		return createBfile( directory, file, stream, BfileType.EXTFILE );
	}

	/**
	 * �����û�ָ����bfile���ʹ���IBfileʵ��
	 * 
	 * @param directory bfileĿ¼
	 * @param file bfile�ļ�
	 * @param stream bfile����
	 * @param bfileType bfile���ͣ�oracle�ͻ��ⲿ�ļ���
	 * @see com.founder.e5.db.BfileType#EXTFILE
	 * @see com.founder.e5.db.BfileType#ORACLE
	 */
	public static IBfile createBfile( String directory, String file,
			InputStream stream, int bfileType ) {
		return BfileFactoryManager.createBfile(
				directory,
				file,
				stream,
				bfileType );
	}

	// ----------------------------------------------------------------------

	/**
	 * ��ȡjava.sql.Clob������Ϊ�ַ���
	 * 
	 * @param clob
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public static String readClob( Clob clob ) throws SQLException, IOException {
		Reader reader = clob.getCharacterStream();
		return readClob( reader );
	}

	/**
	 * ��ȡjava.sql.Clob������Ϊ�ַ���
	 * 
	 * @param clob
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public static String readClob( Reader reader ) throws SQLException, IOException {
		StringWriter sw = new StringWriter();
		try {
			CopyUtils.copy( reader, sw );
		} finally {
			CloseHelper.closeQuietly( reader );
		}
		return sw.getBuffer().toString();
	}
	/**
	 * ��ȡjava.sql.Blob������Ϊ�ֽ�����
	 * 
	 * @param blob
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public static byte[] readBlob( Blob blob ) throws SQLException, IOException {
		InputStream in = blob.getBinaryStream();
		return readBlob( in, (int)blob.length() );
	}
	
	/**
	 * ��ȡInputStream������Ϊ�ֽ�����
	 * @param in
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public static byte[] readBlob( InputStream in ) throws SQLException, IOException {
		return readBlob( in, in.available() );
	}
	
	private static byte[] readBlob( InputStream in, int initialSize ) throws SQLException, IOException {
		ByteArrayOutputStream bo = new ByteArrayOutputStream( initialSize );
		try {
			CopyUtils.copy( in, bo );
		} finally {
			CloseHelper.closeQuietly( in );
		}
		return bo.toByteArray();
	}
	
	/**
	 * ��һ��ResultSet�ж�ȡClob�ֶΣ���װ��IClob
	 * @param rs
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public static IClob getClob(ResultSet rs, int columnIndex) throws SQLException, IOException
	{
		try {
			Clob clob = rs.getClob( columnIndex );
			if ( clob != null )
				return createClob( readClob( clob ) );
		} catch (UnsupportedOperationException e) {
			/*
			 * ���ﲶ׽UnsupportedOperationException����
			 * ����ΪSybase�в�֧��getClob�Ķ�ȡ��
			 * Ϊͼ���㣬������������DBSession��ʵ��Sybase�µĹ��ܣ�
			 * ��ֻ����BaseDBSession������һ���жϡ�
			 * ����������SybaseDBSession������Ҫ�ܶ����Ĵ�����ע�ᣬ�Ƚ��鷳��
			 */
			Reader reader = rs.getCharacterStream( columnIndex );
			if ( reader != null )
				return createClob( readClob( reader ) );
		}
		return null;
	}
	/**
	 * ��һ��ResultSet�ж�ȡClob�ֶΣ���װ��IClob
	 * @param rs
	 * @param columnName
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public static IClob getClob(ResultSet rs, String columnName) throws SQLException, IOException
	{
		try {
			Clob clob = rs.getClob( columnName );
			if ( clob != null )
				return createClob( readClob( clob ) );
		} catch (UnsupportedOperationException e) {
			/*
			 * ���ﲶ׽UnsupportedOperationException����
			 * ����ΪSybase�в�֧��getClob�Ķ�ȡ��
			 * Ϊͼ���㣬������������DBSession��ʵ��Sybase�µĹ��ܣ�
			 * ��ֻ����BaseDBSession������һ���жϡ�
			 * ����������SybaseDBSession������Ҫ�ܶ����Ĵ�����ע�ᣬ�Ƚ��鷳��
			 */
			Reader reader = rs.getCharacterStream( columnName );
			if ( reader != null )
				return createClob( readClob( reader ) );
		}
		return null;
	}
	
	/**
	 * ��һ��ResultSet�ж�ȡBlob�ֶΣ���װ��IBlob
	 * @param rs
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public static IBlob getBlob(ResultSet rs, int columnIndex) throws SQLException, IOException
	{
		try {
			Blob blob = rs.getBlob( columnIndex );
			if ( blob != null )
				return createBlob( readBlob( blob ) );
		} catch (UnsupportedOperationException e) {
			/*
			 * ���ﲶ׽UnsupportedOperationException����
			 * ����ΪSybase�в�֧��getBlob�Ķ�ȡ��
			 * Ϊͼ���㣬������������DBSession��ʵ��Sybase�µĹ��ܣ�
			 * ��ֻ����BaseDBSession������һ���жϡ�
			 * ����������SybaseDBSession������Ҫ�ܶ����Ĵ�����ע�ᣬ�Ƚ��鷳��
			 */
			InputStream is = rs.getBinaryStream( columnIndex );
			if ( is != null )
				return createBlob( readBlob( is ) );
		}
		return null;
	}

	/**
	 * ��һ��ResultSet�ж�ȡBlob�ֶΣ���װ��IBlob
	 * @param rs
	 * @param columnName
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public static IBlob getBlob(ResultSet rs, String columnName) throws SQLException, IOException
	{
		try {
			Blob blob = rs.getBlob( columnName );
			if ( blob != null )
				return createBlob( readBlob( blob ) );
		} catch (UnsupportedOperationException e) {
			/*
			 * ���ﲶ׽UnsupportedOperationException����
			 * ����ΪSybase�в�֧��getBlob�Ķ�ȡ��
			 * Ϊͼ���㣬������������DBSession��ʵ��Sybase�µĹ��ܣ�
			 * ��ֻ����BaseDBSession������һ���жϡ�
			 * ����������SybaseDBSession������Ҫ�ܶ����Ĵ�����ע�ᣬ�Ƚ��鷳��
			 */
			InputStream is = rs.getBinaryStream( columnName );
			if ( is != null )
				return createBlob( readBlob( is ) );
		}
		return null;
	}
}
