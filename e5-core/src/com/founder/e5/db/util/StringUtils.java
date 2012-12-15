package com.founder.e5.db.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * ͨ���ַ����������߷���
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-2-16 15:58:27
 */
public class StringUtils {

	public static final String[] EMPTY_STRING = new String[ 0 ];

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

}
