
package com.founder.e5.db;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

/**
 * ������󱣴���ִ��һ�����ݿ��ѯ������¡�ɾ�����������е�״̬��<br>
 * ���������DBSession.createQuery( sql )����
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-4-5 14:50:48
 */
public interface DBQuery {

	/**
	 * ����intֵ
	 * 
	 * @param position ����λ�ã���1��ʼ
	 * @param value ����ֵ
	 * @return �ö�������
	 */
	public DBQuery setInt( int position, int value );

	/**
	 * ����longֵ
	 * 
	 * @param position ����λ�ã���1��ʼ
	 * @param value ����ֵ
	 * @return �ö�������
	 */
	public DBQuery setLong( int position, long value );

	/**
	 * ����floatֵ
	 * 
	 * @param position ����λ�ã���1��ʼ
	 * @param value ����ֵ
	 * @return �ö�������
	 */
	public DBQuery setFloat( int position, float value );

	/**
	 * ����doubleֵ
	 * 
	 * @param position ����λ�ã���1��ʼ
	 * @param value ����ֵ
	 * @return �ö�������
	 */
	public DBQuery setDouble( int position, double value );

	/**
	 * �����ַ���ֵ
	 * 
	 * @param position ����λ�ã���1��ʼ
	 * @param value ����ֵ
	 * @return �ö�������
	 */
	public DBQuery setString( int position, String value );

	/**
	 * ��������ֵ
	 * 
	 * @param position ����λ�ã���1��ʼ
	 * @param value ����ֵ
	 * @return �ö�������
	 */
	public DBQuery setDate( int position, Date value );

	/**
	 * ����ʱ��ֵ
	 * 
	 * @param position ����λ�ã���1��ʼ
	 * @param value ����ֵ
	 * @return �ö�������
	 */
	public DBQuery setTime( int position, Time value );

	/**
	 * ����ʱ���ֵ
	 * 
	 * @param position ����λ�ã���1��ʼ
	 * @param value ����ֵ
	 * @return �ö�������
	 */
	public DBQuery setTimestamp( int position, Timestamp value );

	/**
	 * ���ö���ֵ
	 * 
	 * @param position ����λ�ã���1��ʼ
	 * @param value ����ֵ
	 * @return �ö�������
	 */
	public DBQuery setObject( int position, Object value );

	/**
	 * �󶨿ղ���
	 * 
	 * @param position ����λ�ã���1��ʼ
	 * @param sqlType ������,��java.sql.Types����
	 * @return �ö�������
	 */
	public DBQuery setNull( int position, int sqlType );

	/**
	 * ִ�в�ѯ��䣬���ؽ����
	 * 
	 * @return
	 * @throws SQLException
	 */
	public IResultSet executeQuery() throws SQLException;

	/**
	 * ִ�и�����䣬���ظı������
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int executeUpdate() throws SQLException;

	/**
	 * ִ�в�ѯ��䣬���ؽ����
	 * 
	 * @return List�����е�Ԫ������ΪList����һ����ֵ��
	 * @throws SQLException
	 */
	public List list() throws SQLException;

	/**
	 * ִ�в�ѯ��䣬���ؽ����
	 * 
	 * @return Iterator�����е�Ԫ������ΪList����һ����ֵ��
	 * @throws SQLException
	 */
	public Iterator iterator() throws SQLException;

}
