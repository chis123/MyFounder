/**
 * $Id: e5project_vss com.founder.e5.commons StringUtils.java created on
 * 2006-2-16 15:58:27 by liyanhui
 */
package com.founder.e5.commons;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * 通用字符串操作工具方法<br>
 * 注意：StringUtils提供普遍通用的方法，StringHelper提供在E5应用范围内通用的方法
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-2-16 15:58:27
 */
public class StringUtils {

	public static final String[] EMPTY_STRING = new String[ 0 ];

    /**
     * 判断当前输入参数是否可用于Java平台。<br/>
     * E5系统允许跨java和.net两个平台运行。<br/>
     * 因此对于需要区分平台特性的，如规则类名以及操作URL，
     * 可以加前缀"JAVA:"来明确表示为java环境下使用。
     * 前缀不区分大小写。<br/>
     * 
     * 缺省可以不加前缀，此时认为java平台可用。
     * 
     * 目前使用此跨平台前缀的有：规则、操作模块。
     * @return
     */
    public static boolean isForJava(String name)
    {
    	if (isBlank(name)) return false;
    	
    	String[] prefix = split(name, ":");
    	if (prefix == null) return false;
    	
    	if (prefix.length == 2 && !"java".equalsIgnoreCase(prefix[0]))
    		return false;
    	return true;
    }
    
	/**
	 * 根据指定分隔符分割字符串，返回结果数组。<br>
	 * 处理规则：<br>
	 * 若输入为null，则返回null；<br>
	 * 否则若输入为空字符串，则返回空数组；<br>
	 * 否则若分隔符为null或空字符串，则返回包含原字符串本身的数组；<br>
	 * 注意：返回结果中过滤掉空字符串
	 * 
	 * @param input 输入字符串
	 * @param separator 分隔符
	 * @return 结果数组（注意：不包括空字符串）
	 */
	public static String[] split( String input, String separator ) {
		if ( input == null )
			return null;
		if ( input.equals( "" ) )
			return EMPTY_STRING;
		if ( separator == null || "".equals( separator ) )
			return new String[] { input };

		int cursor = 0; // 游标
		int lastPos = 0; // 指向上一个分隔符后第一个字符
		ArrayList list = new ArrayList();

		while ( ( cursor = input.indexOf( separator, cursor ) ) != -1 ) {

			if ( cursor > lastPos ) {// 滤掉空字符串
				String token = input.substring( lastPos, cursor );
				list.add( token );
			}

			lastPos = cursor + separator.length();

			cursor = lastPos;
		}

		if ( lastPos < input.length() )
			list.add( input.substring( lastPos ) );

		return ( String[] ) list.toArray( new String[ list.size() ] );
	}

	/**
	 * 按照给定的分隔符分割字符串，包括字符串首、中、尾的空串<br>
	 * 如以"#"为分隔符，"#A"返回["", "A"]<br>
	 * "A#"返回["A", ""]<br>
	 * "A##"返回["A", "", ""]<br>
	 * 另外：input为null则返回null，为空串则返回空数组
	 * 
	 * @param input 输入字符串
	 * @param seperator 分隔符
	 * @return
	 */
	public static String[] splitStrictly( String input, String seperator ) {
		if ( input == null )
			return null;
		if ( input.equals( "" ) )
			return EMPTY_STRING;

		ArrayList list = new ArrayList();
		int cursor = 0;
		int beginPos = 0;

		while ( true ) {
			cursor = input.indexOf( seperator, beginPos );
			if ( cursor != -1 ) {
				list.add( input.substring( beginPos, cursor ) );
				beginPos = cursor + 1;
			} else {
				list.add( input.substring( beginPos ) );
				break;
			}
		}

		return ( String[] ) list.toArray( new String[ list.size() ] );
	}

	/**
	 * 判断给定字符串是否空白串。<br>
	 * 空白串是指由空格、制表符、回车符、换行符组成的字符串<br>
	 * 注意：若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isBlank( String input ) {
		if ( input == null || "".equals( input ) )
			return true;
		for ( int i = 0; i < input.length(); i++ ) {
			char c = input.charAt( i );
			if ( c != ' ' && c != '\t' && c != '\r' && c != '\n' )
				return false;
		}
		return true;
	}

	/**
	 * 清除字符串末尾的特定字符<br>
	 * 若字符串末尾并非给定字符，则什么都不做<br>
	 * 注意：该方法改变了传入的StringBuffer参数的值
	 * 
	 * @param sb 字符串缓存
	 * @param tail 用户给定字符
	 * @return 字符串缓存对象的字符串表示
	 */
	public static String trimTail( StringBuffer sb, char tail ) {
		if ( sb.length() > 0 && sb.charAt( sb.length() - 1 ) == tail )
			sb.deleteCharAt( sb.length() - 1 );
		return sb.toString();
	}

	/**
	 * 将模板中的占位符替换为给定字符串（替换一次）
	 * 
	 * @param template 模板字符串
	 * @param placeholder 占位符，被替换字符串
	 * @param replacement 替换字符串
	 * @return 模板被替换后的字符串
	 */
	public static String replaceOnce( String template, String placeholder,
			String replacement ) {
		int pos = template.indexOf( placeholder );
		if ( pos < 0 ) {
			return template;
		} else {
			return new StringBuffer( template.substring( 0, pos ) ).append(
					replacement ).append(
					template.substring( pos + placeholder.length() ) ).toString();
		}
	}

	/**
	 * 若输入为null，则返回空字符串；否则返回字符串自身
	 */
	public static String getNotNull( String input ) {
		return ( input == null ? "" : input );
	}

	/**
	 * 修剪字符串，使得该字符串按照给定编码方式编码后的字节数在指定长度内<br>
	 * 该方法主要用于插入字符串到数据库时截短超长的字符串
	 * 
	 * @param str 待修剪字符串
	 * @param encoding 目标编码方式
	 * @param maxLength 编码后的字节流的最大长度
	 * @return 修剪后的字符串
	 * @throws UnsupportedEncodingException
	 */
	public static String pruneToFit( String str, String encoding, int maxLength )
			throws UnsupportedEncodingException {
		int nowLength = str.getBytes( encoding ).length;
		if ( nowLength <= maxLength )
			return str;

		int pruneCnt = ( nowLength - maxLength ) / 3 + 1;
		String s = str.substring( 0, ( str.length() - pruneCnt ) );

		if ( s.getBytes( encoding ).length <= maxLength )
			return s;
		else
			return pruneToFit( s, encoding, maxLength );
	}

	/**
	 * 把一个字符串转换成long数组 字符串是以逗号分隔的长整数
	 * 
	 * @param docIDs
	 * @return
	 */
	public static long[] getLongArray( String docIDs ) {
		return getLongArray( docIDs, "," );
	}

	/**
	 * 把一个字符串转换成int数组 字符串是以逗号分隔的整数
	 * 
	 * @param docIDs
	 * @return
	 */
	public static int[] getIntArray( String docIDs ) {
		return getIntArray( docIDs, "," );
	}

	/**
	 * 把一个字符串转换成long数组
	 * 
	 * @param docIDs
	 * @param splitor
	 * @return
	 */
	public static long[] getLongArray( String docIDs, String splitor ) {
		if ( docIDs == null || "".equals( docIDs ) )
			return null;

		String[] tmp = docIDs.split( splitor );
		if ( tmp == null )
			return null;

		long[] docs = new long[ tmp.length ];
		for ( int i = 0; i < tmp.length; i++ )
			docs[i] = Long.parseLong( tmp[i] );
		return docs;
	}

	/**
	 * 把一个字符串转换成int数组
	 * 
	 * @param docIDs
	 * @param splitor
	 * @return
	 */
	public static int[] getIntArray( String docIDs, String splitor ) {
		if ( docIDs == null || "".equals( docIDs ) )
			return null;

		String[] tmp = docIDs.split( splitor );
		if ( tmp == null )
			return null;

		int[] docs = new int[ tmp.length ];
		for ( int i = 0; i < tmp.length; i++ )
			docs[i] = Integer.parseInt( tmp[i] );
		return docs;
	}

	/**
	 * 把给定数组中的数字用给定分隔符连接起来<br>
	 * <br>
	 * 若数组为null，则返回空字符串；若分隔符为null，则直接连接
	 * 
	 * @param array 数字数组
	 * @param separator 分隔符
	 * @return
	 */
	public static String join( int[] array, String separator ) {
		if ( array != null ) {
			if ( separator == null )
				separator = "";

			StringBuffer sb = new StringBuffer();
			for ( int i = 0; i < array.length; i++ ) {
				sb.append( array[i] );
				if ( i != array.length - 1 )
					sb.append( separator );
			}

			return sb.toString();
		}

		return "";
	}

	/**
	 * 把给定数组中的数字用给定分隔符连接起来<br>
	 * <br>
	 * 若数组为null，则返回空字符串；若分隔符为null，则直接连接
	 * 
	 * @param array 数字数组
	 * @param separator 分隔符
	 * @return
	 */
	public static String join( long[] array, String separator ) {
		if ( array != null ) {
			if ( separator == null )
				separator = "";

			StringBuffer sb = new StringBuffer();
			for ( int i = 0; i < array.length; i++ ) {
				sb.append( array[i] );
				if ( i != array.length - 1 )
					sb.append( separator );
			}

			return sb.toString();
		}

		return "";
	}

	/**
	 * 把给定数组中的对象的字符串值用给定分隔符连接起来<br>
	 * <br>
	 * 若数组为null，则返回空字符串；若分隔符为null，则直接连接
	 * 
	 * @param array 数字数组
	 * @param separator 分隔符
	 * @return
	 */
	public static String join( Object[] array, String separator ) {
		if ( array != null ) {
			if ( separator == null )
				separator = "";

			StringBuffer sb = new StringBuffer();
			for ( int i = 0; i < array.length; i++ ) {
				sb.append( array[i] );
				if ( i != array.length - 1 )
					sb.append( separator );
			}

			return sb.toString();
		}

		return "";
	}

	/**
	 * 把给定数组中的数字用逗号连接起来
	 * 
	 * @param array
	 * @return
	 */
	public static String join( long[] array ) {
		return join( array, "," );
	}

	/**
	 * 把给定数组中的对象的字符串值用逗号连接起来
	 * 
	 * @param array
	 * @return
	 */
	public static String join( Object[] array ) {
		return join( array, "," );
	}

	private static final String[] htmlCode = new String[ 256 ];
	static {
		for ( int i = 0; i < 10; i++ )
			htmlCode[i] = "&#00" + i + ";";

		for ( int i = 10; i < 32; i++ )
			htmlCode[i] = "&#0" + i + ";";

		for ( int i = 32; i < 128; i++ )
			htmlCode[i] = String.valueOf( ( char ) i );

		// Special characters
		htmlCode['\n'] = "<br/>\n";
		htmlCode['\"'] = "&quot;"; // double quote
		htmlCode['\''] = "&apos;"; // quote
		htmlCode['&'] = "&amp;"; // ampersand
		htmlCode['<'] = "&lt;"; // lower than
		htmlCode['>'] = "&gt;"; // greater than
		for ( int i = 128; i < 256; i++ ) {
			htmlCode[i] = "&#" + i + ";";
		}
	}

	/**
	 * <p>
	 * Encode the given text into html.
	 * </p>
	 * 
	 * @param string the text to encode
	 * @return the encoded string
	 */
	public static String encode( String string ) {
		if (string == null) return null;
		
		int n = string.length();
		char character;
		StringBuffer buffer = new StringBuffer();
		// loop over all the characters of the String.
		for ( int i = 0; i < n; i++ ) {
			character = string.charAt( i );
			// the Htmlcode of these characters are added to a StringBuffer one
			// by one
			try {
				buffer.append( htmlCode[character] );
			} catch ( ArrayIndexOutOfBoundsException aioobe ) {
				buffer.append( character );
			}
		}
		return buffer.toString();
	}

	/**
	 * 输出给定异常对象的堆栈信息为字符串
	 * 
	 * @param t
	 * @return 异常对象的堆栈信息
	 */
	public static String printStackTrace( Throwable t ) {
		StringWriter sw = new StringWriter();
		t.printStackTrace( new PrintWriter( sw ) );
		return sw.getBuffer().toString();
	}

	/**
	 * 把一个整数数组用逗号（,）拼成字符串
	 * @param values
	 * @return
	 */
	public static String toString(int[] values) {
		if (values == null) return "";
		
		StringBuffer result = new StringBuffer(20);
		for (int i = 0; i < values.length; i++) {
			if (i > 0) result.append(",");
			result.append(values[i]);
		}
		return result.toString();
	}
}
