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
	 * 取得当前应用程序范围内所有Log4j日志文件
	 * 
	 * @return 已排序的File对象的集合
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
	 * 获取关联到一个Log4j日志对象上的所有日志文件
	 * 
	 * @param logName 日志名称，如"e5.dom", "e5.flow"等
	 * @return File对象的集合
	 */
	public static List getLogFiles(String logName){
		return getLogFiles(Logger.getLogger(logName));
	}

	/**
	 * 获取关联到一个Log4j日志对象上的所有日志文件
	 * 
	 * @param logger 一个Log4j日志对象
	 * @return File对象的集合
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
	 * 若logfile=e5sys.log，则该方法找出同目录下所有符合"e5sys.log.yyyy-MM-dd"格式<br>
	 * 的文件，并返回其路径名的集合<br>
	 * 注意：路径名为字符串，并且以"/"作为路径分隔符
	 * 
	 * @param logfile 日志文件路径名
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

		// 按字符串倒序排序
		Collections.sort( list, new Comparator() {

			public int compare( Object o1, Object o2 ) {
				return -( ( String ) o1 ).compareTo( o2 );
			}
		} );
		
		return list;
	}

	/**
	 * 取得当前类空间内使用的所有Log4j Logger的列表<br>
	 * 注意：不包括根日志
	 * 
	 * @return [name - Logger]值对的集合
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
	 * 取得一个Logger对应的所有有效Appender（可能包括父类别的Appender）的集合.<br>
	 * 注意：返回List中的顺序代表了该Logger输出日志的顺序
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
	 * 取得一个Logger对应的所有有效Appender（可能包括父类别的Appender）名称的集合.<br>
	 * 注意：返回List中的顺序代表了该Logger输出日志的顺序
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

	// -------------------------------------- 供树状展示Loggers之用

	/**
	 * 生成所有logger的类别名按层次组织的xml,用于WebFXTree展示
	 * 
	 * @return xml内容
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
	 * 把一个具层次结构的类别名加到xml结构
	 * 
	 * @param path logger类别名,如org.hibernate.type.Type
	 * @param root 根元素
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
	 * 判断给定元素是否有一个名为"tree"的子元素，而且其"text"属性的值为给定值
	 * 
	 * @param element 当前元素对象
	 * @param textValue 子"tree"元素的"text"属性的值
	 * @return 若有这样的子元素,则返回之
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
