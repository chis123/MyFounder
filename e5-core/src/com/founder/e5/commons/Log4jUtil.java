package com.founder.e5.commons;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Appender;
import org.apache.log4j.Category;
import org.apache.log4j.FileAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

/**
 * @author liyanhui
 * @version 1.0
 * @created 2006-2-24 17:18:11
 */
public class Log4jUtil {

	public static void main( String[] args ) {
		Logger.getLogger( "test1" );
		Logger.getLogger( "test1.test2" );
		Logger.getLogger( Log4jUtil.class );

		// System.out.println( buildNameHierarchyXml() );

		System.out.println( getAvailableDates( "c:/e5sys.log" ) );
	}

	/**
	 * ȡ�õ�ǰӦ�ó���Χ������Log4j��־�ļ�
	 * 
	 * @return �������File����ļ���
	 */
	public static List getAllLogFile() {
		HashSet set = new HashSet();

		Logger root = LogManager.getRootLogger();
		set.addAll( getLogFiles( root ) );

		Enumeration e = LogManager.getCurrentLoggers();
		while ( e.hasMoreElements() ) {
			Logger logger = ( Logger ) e.nextElement();
			set.addAll( getLogFiles( logger ) );
		}

		ArrayList list = new ArrayList( set );
		Collections.sort( list );
		return list;
	}

	/**
	 * ��ȡ������һ��Log4j��־�����ϵ�������־�ļ�
	 * 
	 * @param logName ��־���ƣ���"e5.dom", "e5.flow"��
	 * @return File����ļ���
	 */
	public static List getLogFiles(String logName){
		return getLogFiles(Logger.getLogger(logName));
	}

	/**
	 * ��ȡ������һ��Log4j��־�����ϵ�������־�ļ�
	 * 
	 * @param logger һ��Log4j��־����
	 * @return File����ļ���
	 */
	private static List getLogFiles( Logger logger ) {
		ArrayList list = new ArrayList( 5 );
		Enumeration e = logger.getAllAppenders();
		while ( e.hasMoreElements() ) {
			Appender a = ( Appender ) e.nextElement();

			if ( a instanceof FileAppender ) {
				FileAppender fa = ( FileAppender ) a;
				String file = fa.getFile();
				list.add( new File( file ) );
			}
		}
		return list;
	}

	/**
	 * ��logfile=e5sys.log����÷����ҳ�ͬĿ¼�����з���"e5sys.log.yyyy-MM-dd"��ʽ<br>
	 * ���ļ�����������·�����ļ���<br>
	 * ע�⣺·����Ϊ�ַ�����������"/"��Ϊ·���ָ���
	 * 
	 * @param logfile ��־�ļ�·����
	 * @return
	 */
	public static List getAvailableDates( String logfile ) {
		ArrayList list = new ArrayList();
		File file = new File( logfile );
		String filename = file.getName();
		String prefix = filename + ".";

		String[] ss = file.getParentFile().list();
		for ( int i = 0; i < ss.length; i++ ) {
			String fname = ss[i];
			if ( fname.startsWith( prefix ) ) {
				String suffix = fname.substring( prefix.length() );
				if ( suffix.matches( "\\d{4}-\\d{2}-\\d{2}" ) ) {
					list.add( suffix );
				}
			}
		}

		// ���ַ�����������
		Collections.sort( list, new Comparator() {

			public int compare( Object o1, Object o2 ) {
				return -( ( String ) o1 ).compareTo( o2 );
			}
		} );
		
		return list;
	}

	/**
	 * ȡ�õ�ǰ��ռ���ʹ�õ�����Log4j Logger���б�<br>
	 * ע�⣺����������־
	 * 
	 * @return [name - Logger]ֵ�Եļ���
	 */
	public static Map getCurrentLoggers() {
		TreeMap map = new TreeMap();

		Enumeration e = LogManager.getCurrentLoggers();
		while ( e.hasMoreElements() ) {
			Logger logger = ( Logger ) e.nextElement();
			String name = logger.getName();
			map.put( name, logger );
		}
		return map;
	}

	/**
	 * ȡ��һ��Logger��Ӧ��������ЧAppender�����ܰ���������Appender���ļ���.<br>
	 * ע�⣺����List�е�˳������˸�Logger�����־��˳��
	 * 
	 * @param logger
	 * @return
	 */
	public static List getEffectiveAppenders( Logger logger ) {
		ArrayList list = new ArrayList();
		for ( Category c = logger; c != null; c = c.getParent() ) {
			Enumeration e = c.getAllAppenders();
			while ( e != null && e.hasMoreElements() ) {
				list.add( e.nextElement() );
			}

			if ( !c.getAdditivity() )
				break;
		}
		return list;
	}

	/**
	 * ȡ��һ��Logger��Ӧ��������ЧAppender�����ܰ���������Appender�����Ƶļ���.<br>
	 * ע�⣺����List�е�˳������˸�Logger�����־��˳��
	 * 
	 * @param logger
	 * @return
	 */
	public static List getEffectiveAppenderNames( Logger logger ) {
		ArrayList list = new ArrayList();
		for ( Category c = logger; c != null; c = c.getParent() ) {
			Enumeration e = c.getAllAppenders();
			while ( e != null && e.hasMoreElements() ) {
				Appender a = ( Appender ) e.nextElement();
				list.add( a.getName() );
			}

			if ( !c.getAdditivity() )
				break;
		}
		return list;
	}

	// -------------------------------------- ����״չʾLoggers֮��

	/**
	 * ��������logger��������������֯��xml,����WebFXTreeչʾ
	 * 
	 * @return xml����
	 */
	public static String buildNameHierarchyXml() {
		ArrayList list = new ArrayList();

		Enumeration e = LogManager.getCurrentLoggers();
		while ( e.hasMoreElements() ) {
			Logger logger = ( Logger ) e.nextElement();
			list.add( logger.getName() );
		}

		Collections.sort( list );

		Document doc = DocumentHelper.createDocument();
		DefaultElement root = new DefaultElement( "tree" );
		doc.setRootElement( root );

		for ( int i = 0; i < list.size(); i++ ) {
			addToTree( ( String ) list.get( i ), root );
		}

		return doc.asXML();
	}

	/**
	 * ��һ���߲�νṹ��������ӵ�xml�ṹ
	 * 
	 * @param path logger�����,��org.hibernate.type.Type
	 * @param root ��Ԫ��
	 */
	private static void addToTree( String path, Element root ) {
		int pos = path.indexOf( '.' );
		if ( pos == -1 ) {
			if ( getChild( root, path ) == null )
				root.addElement( "tree" ).addAttribute( "text", path );
		} else {
			String p1 = path.substring( 0, pos );
			String p2 = path.substring( pos + 1 );

			Element sub = getChild( root, p1 );
			if ( sub == null ) {
				sub = root.addElement( "tree" ).addAttribute( "text", p1 );
			}

			addToTree( p2, sub );
		}
	}

	/**
	 * �жϸ���Ԫ���Ƿ���һ����Ϊ"tree"����Ԫ�أ�������"text"���Ե�ֵΪ����ֵ
	 * 
	 * @param element ��ǰԪ�ض���
	 * @param textValue ��"tree"Ԫ�ص�"text"���Ե�ֵ
	 * @return ������������Ԫ��,�򷵻�֮
	 */
	private static Element getChild( Element element, String textValue ) {
		Iterator i = element.elementIterator( "tree" );
		while ( i.hasNext() ) {
			Element child = ( Element ) i.next();
			if ( textValue.equals( child.attributeValue( "text" ) ) )
				return child;
		}
		return null;
	}

}
