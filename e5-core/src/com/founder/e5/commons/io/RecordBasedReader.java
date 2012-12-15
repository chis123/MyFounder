/**
 * $Id: e5new com.founder.e5.commons.io RecordBasedReader.java 
 * created on 2006-3-6 16:58:34
 * by liyanhui
 */
package com.founder.e5.commons.io;

import java.io.IOException;
import java.util.List;

/**
 * ���ڼ�¼���ļ����ʽӿ�
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-3-6 16:58:34
 */
public interface RecordBasedReader {

	/**
	 * ��ָ����ŵļ�¼��ʼ������ȡ���������򵽴��ļ�ĩβ�󷵻�
	 * 
	 * @param recordNo ��ʼ��¼��
	 * @param length ��ȡ����
	 * @return ��ָ����ż�¼��ʼ��length����¼����
	 * @throws IOException
	 */
	public List read( int recordNo, int length ) throws IOException;

	/**
	 * �����ļ��а������ܼ�¼��
	 * 
	 * @return �ļ��а������ܼ�¼��
	 * @throws IOException
	 */
	public int totalRecords() throws IOException;

}
