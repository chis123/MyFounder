/*
 * Created on 2004-8-5 15:43:04 by liyanhui
 *
 */
package com.founder.e5.commons;

/**
 * 该类提供显示时间的工具方法
 * 
 * @author liyanhui 2004-8-5 15:43:04
 */
public class TimeUtils {

	public static final long SECONDS_PER_MINUTE = 60;

	public static final long SECONDS_PER_HOUR = 60 * SECONDS_PER_MINUTE;

	public static final long SECONDS_PER_DAY = 24 * SECONDS_PER_HOUR;

	public static final long SECONDS_PER_MONTH = 30 * SECONDS_PER_DAY;

	public static final long SECONDS_PER_YEAR = 12 * SECONDS_PER_MONTH;

	/**
	 * 把以毫秒计的时间转换为易于看懂的时间
	 * 
	 * @param millis 毫秒数
	 * @return
	 * @throws IllegalArgumentException if millis is negative
	 */
	public static String toManFriendTime( long millis ) {
		if ( millis < 0 )
			throw new IllegalArgumentException( "negative number" );

		long years = millis / ( SECONDS_PER_YEAR * 1000 );
		millis -= years * SECONDS_PER_YEAR * 1000;
		long months = millis / ( SECONDS_PER_MONTH * 1000 );
		millis -= months * SECONDS_PER_MONTH * 1000;
		long days = millis / ( SECONDS_PER_DAY * 1000 );
		millis -= days * SECONDS_PER_DAY * 1000;
		long hours = millis / ( SECONDS_PER_HOUR * 1000 );
		millis -= hours * SECONDS_PER_HOUR * 1000;
		long minutes = millis / ( SECONDS_PER_MINUTE * 1000 );
		millis -= minutes * SECONDS_PER_MINUTE * 1000;
		long seconds = millis / 1000;
		millis -= seconds * 1000;

		StringBuffer bf = new StringBuffer();
		if ( years != 0 )
			bf.append( years ).append( " years " );
		if ( months != 0 )
			bf.append( months ).append( " months " );
		if ( days != 0 )
			bf.append( days ).append( " days " );
		if ( hours != 0 )
			bf.append( hours ).append( "h " );
		if ( minutes != 0 )
			bf.append( minutes ).append( "m " );
		if ( seconds != 0 )
			bf.append( seconds ).append( "s " );

		if ( millis > 0 || ( millis == 0 && bf.length() == 0 ) )
			bf.append( millis ).append( "ms" );
		return bf.toString();
	}

	public static void main( String[] args ) {
		System.out.println( toManFriendTime( 48 + 1000 * ( 1 * SECONDS_PER_YEAR + 5
				* SECONDS_PER_MONTH + 3 * SECONDS_PER_DAY + 5 * SECONDS_PER_HOUR + 13
				* SECONDS_PER_MINUTE + 7 ) ) );
	}
}
