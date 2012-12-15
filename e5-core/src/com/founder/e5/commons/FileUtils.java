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
 * �ļ�����������<br>
 * ע������������ResourceMgr��apache commons-io
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2005-7-13 9:59:16
 */
public class FileUtils {

	/**
	 * Convert from a <code>URL</code> to a <code>File</code>.<br>
	 * ע��ԭ��������apache��ʹ���з����ļ�·�����к��ո�ʱ���ԣ���������url���빦�ܣ�����������
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
	// ��ȡ�ļ��Ĺ��ܽӿ�

	/**
	 * ��ȡ�ļ���д��ָ������Ȼ��ر������<br>
	 * �÷�����ʹ��BufferedOutputStream��װ�����
	 * 
	 * @param pathname �ļ�·����
	 * @param os �����
	 * @throws IOException
	 */
	public static void readFile( File pathname, OutputStream os )
			throws IOException {
		readFile( pathname, os, true );
	}

	/**
	 * ��ȡ�ļ���д��ָ����<br>
	 * �÷�����ʹ��BufferedOutputStream��װ�����
	 * 
	 * @param pathname �ļ�·����
	 * @param os �����
	 * @param close �Ƿ�ر������
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
	 * ����ϵͳĬ�ϱ����ȡ�ļ���д��ָ������Ȼ��ر������<br>
	 * �÷�����ʹ��BufferedWriter��װ�����
	 * 
	 * @param pathname �ļ�·����
	 * @param os �����
	 * @throws IOException
	 */
	public static void readFile( File pathname, Writer writer )
			throws IOException {
		readFile( pathname, null, writer, true );
	}

	/**
	 * �����û�ָ�������ȡ�ļ���д��ָ�������<br>
	 * �÷�����ʹ��BufferedWriter��װ�����
	 * 
	 * @param pathname �ļ�·����
	 * @param encoding �ļ����루��Ϊnull����ȡϵͳĬ�ϱ��룩
	 * @param writer �����
	 * @param close �Ƿ�ر������
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
	 * ��ȡ�ļ����������ֽ�����
	 * 
	 * @param pathname �ļ�·����
	 * @return �ļ�����
	 * @throws IOException
	 */
	public static byte[] readFile( File pathname ) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(
				( int ) pathname.length() );
		readFile( pathname, out );
		return out.toByteArray();
	}

	/**
	 * ��ȡ�ļ����������ֽ�����
	 * 
	 * @param pathname �ļ�·����
	 * @return �ļ�����
	 * @throws IOException
	 */
	public static byte[] readFile( String pathname ) throws IOException {
		return readFile( new File( pathname ) );
	}

	/**
	 * ����ϵͳĬ�ϱ����ȡ�ļ��������ļ�����
	 * 
	 * @param pathname �ļ�·����
	 * @return �ļ�����
	 * @throws IOException
	 */
	public static String readTextFile( File pathname ) throws IOException {
		StringWriter sw = new StringWriter();
		readFile( pathname, sw );
		return sw.getBuffer().toString();
	}

	/**
	 * ����ϵͳĬ�ϱ����ȡ�ļ��������ļ�����
	 * 
	 * @param pathname �ļ�·����
	 * @return �ļ�����
	 * @throws IOException
	 */
	public static String readTextFile( String pathname ) throws IOException {
		StringWriter sw = new StringWriter();
		readFile( new File( pathname ), sw );
		return sw.getBuffer().toString();
	}

	/**
	 * �����û�ָ�������ȡ�ļ��������ļ�����
	 * 
	 * @param pathname �ļ�·����
	 * @param encoding �ļ����루��Ϊnull����ȡϵͳĬ�ϱ��룩
	 * @return �ļ�����
	 * @throws IOException
	 */
	public static String readTextFile( File pathname, String encoding )
			throws IOException {
		StringWriter sw = new StringWriter();
		readFile( pathname, encoding, sw, true );
		return sw.getBuffer().toString();
	}

	/**
	 * �����û�ָ�������ȡ�ļ��������ļ�����
	 * 
	 * @param pathname �ļ�·����
	 * @param encoding �ļ����루��Ϊnull����ȡϵͳĬ�ϱ��룩
	 * @return �ļ�����
	 * @throws IOException
	 */
	public static String readTextFile( String pathname, String encoding )
			throws IOException {
		StringWriter sw = new StringWriter();
		readFile( new File( pathname ), encoding, sw, true );
		return sw.getBuffer().toString();
	}

	/**
	 * ��ȡ��·���µ���Դ�ļ����������ı�����
	 * 
	 * @param name ��Դ�������������·������
	 * @return �ļ�����
	 * @throws IOException
	 */
	public static String readClassPathFile( String name ) throws IOException {
		return readClassPathFile( name, null );
	}

	/**
	 * ��ȡ��·���µ���Դ�ļ����������ı�����
	 * 
	 * @param name ��Դ�������������·������
	 * @param encoding �ļ����루��Ϊnull��ȡϵͳĬ�ϱ��룩
	 * @return �ļ�����
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
	// д�ļ��Ĺ��ܽӿ�

	/**
	 * �����ж�ȡ���ݣ�д��ָ���ļ���Ȼ��ر���<br>
	 * �÷���ʵ��ʹ��BufferedInputStream��װ������
	 * 
	 * @param in ������
	 * @param pathname �ļ�·����
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
	 * д�����ݵ�ָ���ļ�
	 * 
	 * @param data �ļ��ֽ�����
	 * @param pathname �ļ�·����
	 * @throws IOException
	 */
	public static void writeFile( byte[] data, File pathname )
			throws IOException {
		ByteArrayInputStream bi = new ByteArrayInputStream( data );
		writeFile( bi, pathname );
	}

	/**
	 * �����ж�ȡ���ݣ����û�ָ������д��ָ���ļ���Ȼ��ر���<br>
	 * �÷���ʵ��ʹ��BufferedReader��װ������
	 * 
	 * @param reader ������
	 * @param pathname �ļ�·����
	 * @param encoding �ļ����루��Ϊnull����ȡϵͳĬ�ϱ��룩
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
	 * д�����ݵ��ļ�,�ļ������û�ָ������
	 * 
	 * @param content �ļ�����
	 * @param pathname �ļ�·����
	 * @param encoding �ļ����루��Ϊnull����ȡϵͳĬ�ϱ��룩
	 * @throws IOException
	 */
	public static void writeFile( String content, File pathname, String encoding )
			throws IOException {
		StringReader sr = new StringReader( content );
		writeFile( sr, pathname, encoding );
	}

	/**
	 * �����ж�ȡ���ݣ���ϵͳĬ�ϱ���д��ָ���ļ���Ȼ��ر���<br>
	 * �÷���ʵ��ʹ��BufferedReader��װ������
	 * 
	 * @param reader ������
	 * @param pathname �ļ�·����
	 * @throws IOException
	 */
	public static void writeFile( Reader reader, File pathname )
			throws IOException {
		writeFile( reader, pathname, null );
	}

	/**
	 * д�����ݵ��ļ����ļ�����ϵͳĬ�ϱ���
	 * 
	 * @param content �ļ�����
	 * @param pathname �ļ�·����
	 * @throws IOException
	 */
	public static void writeFile( String content, File pathname )
			throws IOException {
		StringReader sr = new StringReader( content );
		writeFile( sr, pathname );
	}

	// ----------------------------------------------------------------------

}
