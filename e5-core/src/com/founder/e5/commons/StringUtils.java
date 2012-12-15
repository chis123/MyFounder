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
 * ͨ���ַ����������߷���<br>
 * ע�⣺StringUtils�ṩ�ձ�ͨ�õķ�����StringHelper�ṩ��E5Ӧ�÷�Χ��ͨ�õķ���
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-2-16 15:58:27
 */
public class StringUtils {

	public static final String[] EMPTY_STRING = new String[ 0 ];

    /**
     * �жϵ�ǰ��������Ƿ������Javaƽ̨��<br/>
     * E5ϵͳ�����java��.net����ƽ̨���С�<br/>
     * ��˶�����Ҫ����ƽ̨���Եģ�����������Լ�����URL��
     * ���Լ�ǰ׺"JAVA:"����ȷ��ʾΪjava������ʹ�á�
     * ǰ׺�����ִ�Сд��<br/>
     * 
     * ȱʡ���Բ���ǰ׺����ʱ��Ϊjavaƽ̨���á�
     * 
     * Ŀǰʹ�ô˿�ƽ̨ǰ׺���У����򡢲���ģ�顣
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
	 * ����ָ���ָ����ָ��ַ��������ؽ�����顣<br>
	 * �������<br>
	 * ������Ϊnull���򷵻�null��<br>
	 * ����������Ϊ���ַ������򷵻ؿ����飻<br>
	 * �������ָ���Ϊnull����ַ������򷵻ذ���ԭ�ַ�����������飻<br>
	 * ע�⣺���ؽ���й��˵����ַ���
	 * 
	 * @param input �����ַ���
	 * @param separator �ָ���
	 * @return ������飨ע�⣺���������ַ�����
	 */
	public static String[] split( String input, String separator ) {
		if ( input == null )
			return null;
		if ( input.equals( "" ) )
			return EMPTY_STRING;
		if ( separator == null || "".equals( separator ) )
			return new String[] { input };

		int cursor = 0; // �α�
		int lastPos = 0; // ָ����һ���ָ������һ���ַ�
		ArrayList list = new ArrayList();

		while ( ( cursor = input.indexOf( separator, cursor ) ) != -1 ) {

			if ( cursor > lastPos ) {// �˵����ַ���
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
	 * ���ո����ķָ����ָ��ַ����������ַ����ס��С�β�Ŀմ�<br>
	 * ����"#"Ϊ�ָ�����"#A"����["", "A"]<br>
	 * "A#"����["A", ""]<br>
	 * "A##"����["A", "", ""]<br>
	 * ���⣺inputΪnull�򷵻�null��Ϊ�մ��򷵻ؿ�����
	 * 
	 * @param input �����ַ���
	 * @param seperator �ָ���
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
	 * �жϸ����ַ����Ƿ�հ״���<br>
	 * �հ״���ָ�ɿո��Ʊ�����س��������з���ɵ��ַ���<br>
	 * ע�⣺�������ַ���Ϊnull����ַ���������true
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
	 * ����ַ���ĩβ���ض��ַ�<br>
	 * ���ַ���ĩβ���Ǹ����ַ�����ʲô������<br>
	 * ע�⣺�÷����ı��˴����StringBuffer������ֵ
	 * 
	 * @param sb �ַ�������
	 * @param tail �û������ַ�
	 * @return �ַ������������ַ�����ʾ
	 */
	public static String trimTail( StringBuffer sb, char tail ) {
		if ( sb.length() > 0 && sb.charAt( sb.length() - 1 ) == tail )
			sb.deleteCharAt( sb.length() - 1 );
		return sb.toString();
	}

	/**
	 * ��ģ���е�ռλ���滻Ϊ�����ַ������滻һ�Σ�
	 * 
	 * @param template ģ���ַ���
	 * @param placeholder ռλ�������滻�ַ���
	 * @param replacement �滻�ַ���
	 * @return ģ�屻�滻����ַ���
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
	 * ������Ϊnull���򷵻ؿ��ַ��������򷵻��ַ�������
	 */
	public static String getNotNull( String input ) {
		return ( input == null ? "" : input );
	}

	/**
	 * �޼��ַ�����ʹ�ø��ַ������ո������뷽ʽ�������ֽ�����ָ��������<br>
	 * �÷�����Ҫ���ڲ����ַ��������ݿ�ʱ�ض̳������ַ���
	 * 
	 * @param str ���޼��ַ���
	 * @param encoding Ŀ����뷽ʽ
	 * @param maxLength �������ֽ�������󳤶�
	 * @return �޼�����ַ���
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
	 * ��һ���ַ���ת����long���� �ַ������Զ��ŷָ��ĳ�����
	 * 
	 * @param docIDs
	 * @return
	 */
	public static long[] getLongArray( String docIDs ) {
		return getLongArray( docIDs, "," );
	}

	/**
	 * ��һ���ַ���ת����int���� �ַ������Զ��ŷָ�������
	 * 
	 * @param docIDs
	 * @return
	 */
	public static int[] getIntArray( String docIDs ) {
		return getIntArray( docIDs, "," );
	}

	/**
	 * ��һ���ַ���ת����long����
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
	 * ��һ���ַ���ת����int����
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
	 * �Ѹ��������е������ø����ָ�����������<br>
	 * <br>
	 * ������Ϊnull���򷵻ؿ��ַ��������ָ���Ϊnull����ֱ������
	 * 
	 * @param array ��������
	 * @param separator �ָ���
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
	 * �Ѹ��������е������ø����ָ�����������<br>
	 * <br>
	 * ������Ϊnull���򷵻ؿ��ַ��������ָ���Ϊnull����ֱ������
	 * 
	 * @param array ��������
	 * @param separator �ָ���
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
	 * �Ѹ��������еĶ�����ַ���ֵ�ø����ָ�����������<br>
	 * <br>
	 * ������Ϊnull���򷵻ؿ��ַ��������ָ���Ϊnull����ֱ������
	 * 
	 * @param array ��������
	 * @param separator �ָ���
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
	 * �Ѹ��������е������ö�����������
	 * 
	 * @param array
	 * @return
	 */
	public static String join( long[] array ) {
		return join( array, "," );
	}

	/**
	 * �Ѹ��������еĶ�����ַ���ֵ�ö�����������
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
	 * ��������쳣����Ķ�ջ��ϢΪ�ַ���
	 * 
	 * @param t
	 * @return �쳣����Ķ�ջ��Ϣ
	 */
	public static String printStackTrace( Throwable t ) {
		StringWriter sw = new StringWriter();
		t.printStackTrace( new PrintWriter( sw ) );
		return sw.getBuffer().toString();
	}

	/**
	 * ��һ�����������ö��ţ�,��ƴ���ַ���
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
