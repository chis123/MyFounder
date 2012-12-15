package com.founder.e5.db;

import java.io.InputStream;
import java.sql.SQLException;

/**
 * IBfile���󹤳���<br>
 * <br>
 * �ýӿ��ṩ���ֹ��ܣ�<br>
 * 1)����ȡʱ��ResultSet��ȡ��һ��IBfile�����ⲿ�ֹ�����convert�����ṩ��<br>
 * 2)��д��ʱ�����û��ṩ��dir��file����stream������һ��IBfile��������ֵ���󴫸�
 * DBSession.executeUpdate���ⲿ�ֹ�����createBfile�����ṩ�� <br>
 * <br>
 * ʵ������Ҫ������ʵ�֣�oracle bfile��ʵ�ֺ��ⲿ�ļ���ʵ�֡�<br>
 * ͬһӦ���п����ж���ʵ�ֹ��棬ÿ��ʵ�ֶ�Ӧһ��bfileʵ�ֻ��ƣ�Ҫ��ʵ���߼���Ƿ�������
 * ֧�ֵ�bfile���ƣ�������ǣ�ֱ�ӷ���null���⽫����BfileFactoryManager���Լ���֧������bfile��
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-4-20 20:42:56
 * @see BfileFactoryManager
 */
public interface BfileFactory {

	/**
	 * �Ƿ�֧��ָ��Ŀ¼
	 * 
	 * @param directory Ŀ¼����������OracleĿ¼��Ҳ�����Ǵ洢�豸����
	 * @return boolean
	 */
	public boolean support( String directory );

	/**
	 * �Ѵӽ������ȡ����bfile�������ݶ����ֵת��ΪIBfile����<br>
	 * <br>
	 * ע�⣺���ù�������֧�ְѽ�����еĶ���ת��ΪIBfile���󣬿ɷ���null��<br>
	 * IBfile��������������ע��Ĺ���������������ø÷�����������ֵ�ǿգ�����øù�������
	 * 
	 * @param obj ��jdbc�������ȡ���Ķ���rs.getObject()�ķ���ֵ��
	 * @return IBfile
	 * @throws SQLException
	 */
	public IBfile convert( Object obj ) throws SQLException;

	/**
	 * ����Ŀ¼�����ļ�������IBfile����
	 * 
	 * @param directory Ŀ¼��
	 * @param file �ļ��������������·������
	 * @return IBfile����
	 */
	public IBfile createBfile( String directory, String file );

	/**
	 * ����Ŀ¼�����ļ�������������������IBfile����
	 * 
	 * @param directory Ŀ¼��
	 * @param file �ļ��������������·������
	 * @param in ����������
	 * @return IBfile����
	 */
	public IBfile createBfile( String directory, String file, InputStream in );

	/**
	 * ����Ŀ¼�����ļ�����������������bfile���ʹ���IBfile����
	 * 
	 * @param directory Ŀ¼��
	 * @param file �ļ��������������·������
	 * @param in ����������
	 * @param bfileType bfile���ͣ�����������oracle bfile����ext bfile��
	 * @return IBfile����
	 */
	public IBfile createBfile( String directory, String file, InputStream in,
			int bfileType );

}
