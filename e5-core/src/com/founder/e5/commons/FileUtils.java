/*
 * Created on 2005-7-13 9:59:16
 *
 */
package com.founder.e5.commons;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLDecoder;

import org.apache.commons.io.CopyUtils;

/**
 * 文件操作工具类<br>
 * 注：该类依赖于ResourceMgr、apache commons-io
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2005-7-13 9:59:16
 */
public class FileUtils {

	/**
	 * Convert from a <code>URL</code> to a <code>File</code>.<br>
	 * 注：原代码来自apache，使用中发现文件路径名中含空格时不对；后来增加url解码功能，修正该问题
	 * 
	 * @author liyanhui
	 * @param url File URL.
	 * @return The equivalent <code>File</code> object, or <code>null</code>
	 *         if the URL's protocol is not <code>file</code>
	 */
	public static File toFile( URL url ) {
		if ( !"file".equals( url.getProtocol() ) ) {
			return null;
		} else {
			String pathname = url.getFile().replace( '/', File.separatorChar );
			if ( pathname.indexOf( "%" ) != -1 ) {
				String encoding = System.getProperty( "file.encoding", "GBK" );
				try {
					pathname = URLDecoder.decode( pathname, encoding );
				} catch ( IOException e ) {
				}
			}
			return new File( pathname );
		}
	}

	// -------------------------------------------------------------------------
	// 读取文件的功能接口

	/**
	 * 读取文件，写入指定流，然后关闭输出流<br>
	 * 该方法中使用BufferedOutputStream包装输出流
	 * 
	 * @param pathname 文件路径名
	 * @param os 输出流
	 * @throws IOException
	 */
	public static void readFile( File pathname, OutputStream os )
			throws IOException {
		readFile( pathname, os, true );
	}

	/**
	 * 读取文件，写入指定流<br>
	 * 该方法中使用BufferedOutputStream包装输出流
	 * 
	 * @param pathname 文件路径名
	 * @param os 输出流
	 * @param close 是否关闭输出流
	 * @throws IOException
	 */
	public static void readFile( File pathname, OutputStream os, boolean close )
			throws IOException {
		InputStream in = new BufferedInputStream(
				new FileInputStream( pathname ) );
		OutputStream out = new BufferedOutputStream( os );
		try {
			CopyUtils.copy( in, out );
		} finally {
			ResourceMgr.closeQuietly( in );
			if ( close )
				ResourceMgr.closeQuietly( out );
		}
	}

	/**
	 * 根据系统默认编码读取文件，写入指定流，然后关闭输出流<br>
	 * 该方法中使用BufferedWriter包装输出流
	 * 
	 * @param pathname 文件路径名
	 * @param os 输出流
	 * @throws IOException
	 */
	public static void readFile( File pathname, Writer writer )
			throws IOException {
		readFile( pathname, null, writer, true );
	}

	/**
	 * 根据用户指定编码读取文件，写入指定输出流<br>
	 * 该方法中使用BufferedWriter包装输出流
	 * 
	 * @param pathname 文件路径名
	 * @param encoding 文件编码（如为null，则取系统默认编码）
	 * @param writer 输出流
	 * @param close 是否关闭输出流
	 * @throws IOException
	 */
	public static void readFile( File pathname, String encoding, Writer writer,
			boolean close ) throws IOException {
		String enc = ( encoding == null ? System.getProperty( "file.encoding" )
				: encoding );

		Reader reader = new BufferedReader( new InputStreamReader(
				new FileInputStream( pathname ),
				enc ) );
		Writer _writer = new BufferedWriter( writer );
		try {
			CopyUtils.copy( reader, _writer );
		} finally {
			ResourceMgr.closeQuietly( reader );
			if ( close )
				ResourceMgr.closeQuietly( _writer );
		}
	}

	/**
	 * 读取文件，返回其字节数据
	 * 
	 * @param pathname 文件路径名
	 * @return 文件数据
	 * @throws IOException
	 */
	public static byte[] readFile( File pathname ) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(
				( int ) pathname.length() );
		readFile( pathname, out );
		return out.toByteArray();
	}

	/**
	 * 读取文件，返回其字节数据
	 * 
	 * @param pathname 文件路径名
	 * @return 文件数据
	 * @throws IOException
	 */
	public static byte[] readFile( String pathname ) throws IOException {
		return readFile( new File( pathname ) );
	}

	/**
	 * 根据系统默认编码读取文件，返回文件内容
	 * 
	 * @param pathname 文件路径名
	 * @return 文件内容
	 * @throws IOException
	 */
	public static String readTextFile( File pathname ) throws IOException {
		StringWriter sw = new StringWriter();
		readFile( pathname, sw );
		return sw.getBuffer().toString();
	}

	/**
	 * 根据系统默认编码读取文件，返回文件内容
	 * 
	 * @param pathname 文件路径名
	 * @return 文件内容
	 * @throws IOException
	 */
	public static String readTextFile( String pathname ) throws IOException {
		StringWriter sw = new StringWriter();
		readFile( new File( pathname ), sw );
		return sw.getBuffer().toString();
	}

	/**
	 * 根据用户指定编码读取文件，返回文件内容
	 * 
	 * @param pathname 文件路径名
	 * @param encoding 文件编码（如为null，则取系统默认编码）
	 * @return 文件内容
	 * @throws IOException
	 */
	public static String readTextFile( File pathname, String encoding )
			throws IOException {
		StringWriter sw = new StringWriter();
		readFile( pathname, encoding, sw, true );
		return sw.getBuffer().toString();
	}

	/**
	 * 根据用户指定编码读取文件，返回文件内容
	 * 
	 * @param pathname 文件路径名
	 * @param encoding 文件编码（如为null，则取系统默认编码）
	 * @return 文件内容
	 * @throws IOException
	 */
	public static String readTextFile( String pathname, String encoding )
			throws IOException {
		StringWriter sw = new StringWriter();
		readFile( new File( pathname ), encoding, sw, true );
		return sw.getBuffer().toString();
	}

	/**
	 * 读取类路径下的资源文件，返回其文本内容
	 * 
	 * @param name 资源名（可以是相对路径名）
	 * @return 文件内容
	 * @throws IOException
	 */
	public static String readClassPathFile( String name ) throws IOException {
		return readClassPathFile( name, null );
	}

	/**
	 * 读取类路径下的资源文件，返回其文本内容
	 * 
	 * @param name 资源名（可以是相对路径名）
	 * @param encoding 文件编码（如为null则取系统默认编码）
	 * @return 文件内容
	 * @throws IOException
	 */
	public static String readClassPathFile( String name, String encoding )
			throws IOException {

		InputStream in = ResourceMgr.getResourceAsStream( name );
		if ( in == null )
			return null;

		String enc = ( encoding == null ? System.getProperty( "file.encoding" )
				: encoding );
		BufferedReader br = new BufferedReader( new InputStreamReader( in, enc ) );
		StringWriter sw = new StringWriter();
		try {
			CopyUtils.copy( br, sw );
		} finally {
			ResourceMgr.closeQuietly( br );
		}

		return sw.toString();
	}

	// -------------------------------------------------------------------------
	// 写文件的功能接口

	/**
	 * 从流中读取数据，写入指定文件，然后关闭流<br>
	 * 该方法实现使用BufferedInputStream包装输入流
	 * 
	 * @param in 输入流
	 * @param pathname 文件路径名
	 * @throws IOException
	 */
	public static void writeFile( InputStream in, File pathname )
			throws IOException {
		BufferedInputStream bi = new BufferedInputStream( in );
		BufferedOutputStream bo = new BufferedOutputStream(
				new FileOutputStream( pathname ) );
		try {
			CopyUtils.copy( bi, bo );
		} finally {
			ResourceMgr.closeQuietly( bi );
			ResourceMgr.closeQuietly( bo );
		}
	}

	/**
	 * 写入数据到指定文件
	 * 
	 * @param data 文件字节数据
	 * @param pathname 文件路径名
	 * @throws IOException
	 */
	public static void writeFile( byte[] data, File pathname )
			throws IOException {
		ByteArrayInputStream bi = new ByteArrayInputStream( data );
		writeFile( bi, pathname );
	}

	/**
	 * 从流中读取数据，按用户指定编码写入指定文件，然后关闭流<br>
	 * 该方法实现使用BufferedReader包装输入流
	 * 
	 * @param reader 输入流
	 * @param pathname 文件路径名
	 * @param encoding 文件编码（如为null，则取系统默认编码）
	 * @throws IOException
	 */
	public static void writeFile( Reader reader, File pathname, String encoding )
			throws IOException {
		String enc = ( encoding == null ? System.getProperty( "file.encoding" )
				: encoding );

		BufferedReader br = new BufferedReader( reader );
		BufferedWriter bw = new BufferedWriter( new OutputStreamWriter(
				new FileOutputStream( pathname ),
				enc ) );
		try {
			CopyUtils.copy( br, bw );
		} finally {
			ResourceMgr.closeQuietly( br );
			ResourceMgr.closeQuietly( bw );
		}
	}

	/**
	 * 写入数据到文件,文件采用用户指定编码
	 * 
	 * @param content 文件内容
	 * @param pathname 文件路径名
	 * @param encoding 文件编码（如为null，则取系统默认编码）
	 * @throws IOException
	 */
	public static void writeFile( String content, File pathname, String encoding )
			throws IOException {
		StringReader sr = new StringReader( content );
		writeFile( sr, pathname, encoding );
	}

	/**
	 * 从流中读取数据，按系统默认编码写入指定文件，然后关闭流<br>
	 * 该方法实现使用BufferedReader包装输入流
	 * 
	 * @param reader 输入流
	 * @param pathname 文件路径名
	 * @throws IOException
	 */
	public static void writeFile( Reader reader, File pathname )
			throws IOException {
		writeFile( reader, pathname, null );
	}

	/**
	 * 写入数据到文件，文件采用系统默认编码
	 * 
	 * @param content 文件内容
	 * @param pathname 文件路径名
	 * @throws IOException
	 */
	public static void writeFile( String content, File pathname )
			throws IOException {
		StringReader sr = new StringReader( content );
		writeFile( sr, pathname );
	}

	// ----------------------------------------------------------------------

}
