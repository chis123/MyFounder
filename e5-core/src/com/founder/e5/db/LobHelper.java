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
 * 供用户创建Lob类型实例的辅助类
 * 
 * @version 1.0
 * @created 08-七月-2005 10:41:42
 */
public class LobHelper {

	/**
	 * 创建一个以给定字符串为内容的IClob
	 * 
	 * @param content
	 */
	public static IClob createClob( String content ) {
		return new StringClob( content );
	}

	/**
	 * 创建一个以给定文件为内容的IClob
	 * 
	 * @param file
	 * @throws FileNotFoundException
	 */
	public static IClob createClob( File file ) throws FileNotFoundException {
		// 这里本应传入字符数，但字节数必定大于字符数，用于clob插入应该没问题
		return new ReaderClob( new FileReader( file ), ( int ) file.length() );
	}

	/**
	 * 读取文件内容，创建IClob对象
	 * 
	 * @param file
	 * @param encoding 文件编码
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
	 * 根据流和长度创建IClob对象<br>
	 * length取Integer.MAX_VALUE
	 * 
	 * @param reader
	 */
	public static IClob createClob( Reader reader ) {
		return new ReaderClob( reader );
	}

	/**
	 * 根据流和长度创建IClob对象<br>
	 * 注意：这个length是必要的，因为PreparedStatement.setCharacterStream需要该值
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
	 * 根据内存数据创建IBlob对象
	 * 
	 * @param bytes
	 */
	public static IBlob createBlob( byte[] bytes ) {
		return new BytesBlob( bytes );
	}

	/**
	 * 根据文件内容创建IBlob对象
	 * 
	 * @param file
	 * @throws FileNotFoundException
	 */
	public static IBlob createBlob( File file ) throws FileNotFoundException {
		return new StreamBlob( new FileInputStream( file ), file.length() );
	}

	/**
	 * 根据流数据创建IBlob对象
	 * 
	 * @param inputStream
	 */
	public static IBlob createBlob( InputStream inputStream ) {
		return new StreamBlob( inputStream );
	}

	// ----------------------------------------------------------------------

	/**
	 * 相当于createBfile( directory, file, null, BfileType.EXTFILE )
	 * 
	 * @param directory
	 * @param file
	 */
	public static IBfile createBfile( String directory, String file ) {
		return createBfile( directory, file, null, BfileType.EXTFILE );
	}

	/**
	 * 相当于createBfile( directory, file, stream, BfileType.EXTFILE )
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
	 * 根据用户指定的bfile类型创建IBfile实例
	 * 
	 * @param directory bfile目录
	 * @param file bfile文件
	 * @param stream bfile数据
	 * @param bfileType bfile类型：oracle型或外部文件型
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
	 * 读取java.sql.Clob的内容为字符串
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
	 * 读取java.sql.Clob的内容为字符串
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
	 * 读取java.sql.Blob的内容为字节数组
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
	 * 读取InputStream的内容为字节数组
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
	 * 从一个ResultSet中读取Clob字段，封装成IClob
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
			 * 这里捕捉UnsupportedOperationException方法
			 * 是因为Sybase中不支持getClob的读取。
			 * 为图方便，不再做单独的DBSession来实现Sybase下的功能，
			 * 而只是在BaseDBSession中增加一点判断。
			 * 若做单独的SybaseDBSession，则需要很多额外的处理，如注册，比较麻烦。
			 */
			Reader reader = rs.getCharacterStream( columnIndex );
			if ( reader != null )
				return createClob( readClob( reader ) );
		}
		return null;
	}
	/**
	 * 从一个ResultSet中读取Clob字段，封装成IClob
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
			 * 这里捕捉UnsupportedOperationException方法
			 * 是因为Sybase中不支持getClob的读取。
			 * 为图方便，不再做单独的DBSession来实现Sybase下的功能，
			 * 而只是在BaseDBSession中增加一点判断。
			 * 若做单独的SybaseDBSession，则需要很多额外的处理，如注册，比较麻烦。
			 */
			Reader reader = rs.getCharacterStream( columnName );
			if ( reader != null )
				return createClob( readClob( reader ) );
		}
		return null;
	}
	
	/**
	 * 从一个ResultSet中读取Blob字段，封装成IBlob
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
			 * 这里捕捉UnsupportedOperationException方法
			 * 是因为Sybase中不支持getBlob的读取。
			 * 为图方便，不再做单独的DBSession来实现Sybase下的功能，
			 * 而只是在BaseDBSession中增加一点判断。
			 * 若做单独的SybaseDBSession，则需要很多额外的处理，如注册，比较麻烦。
			 */
			InputStream is = rs.getBinaryStream( columnIndex );
			if ( is != null )
				return createBlob( readBlob( is ) );
		}
		return null;
	}

	/**
	 * 从一个ResultSet中读取Blob字段，封装成IBlob
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
			 * 这里捕捉UnsupportedOperationException方法
			 * 是因为Sybase中不支持getBlob的读取。
			 * 为图方便，不再做单独的DBSession来实现Sybase下的功能，
			 * 而只是在BaseDBSession中增加一点判断。
			 * 若做单独的SybaseDBSession，则需要很多额外的处理，如注册，比较麻烦。
			 */
			InputStream is = rs.getBinaryStream( columnName );
			if ( is != null )
				return createBlob( readBlob( is ) );
		}
		return null;
	}
}
