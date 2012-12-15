/*
 * $Logfile$
 * $Revision$
 * $Date$
 * $Author$
 * $History$
 *
 * Copyright (c) 2006, �������������޹�˾����ý�忪����
 * All rights reserved.
 */
package com.founder.enp.aa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * �����ں�̨��������������ظ��
 * 1���Զ�ȡʱ�����µ�5����ظ��
 * 2��ֻ�е����û����ظ����ʱ���ȡ
 * 3��������Ĺؼ���ȡ��ظ���ı��������㣬������û�йؼ��־Ͳ�����
 * ��������������4.0
 * @author Liudong
 * Date:2007-8-10
 */
public class Main {
	
	private static Log log = LogFactory.getLog(Main.class);
	
    //���ݿ����ӵ���Ϣ
    private static String URL = "jdbc:oracle:thin:@128.0.168.15:1521:cms40";
    private static String USER = "cmsuser";
    private static String PWD = "xyz";
    //ȡ��ظ����Ĭ������
    private static final int COUNT = 5;
    
    private Connection getConn() {
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(URL, USER, PWD);
        }
        catch(Exception ex) {
            log.error("���ݿ�����ʧ��", ex);
        }
        return conn;
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Main main = new Main();
		String date = main.getToday();
		if(args.length == 3)
		{
			Main.URL = args[0];
			Main.USER = args[1];
			Main.PWD = args[2];
		}
		else if(args.length == 4)
		{
			Main.URL = args[0];
			Main.USER = args[1];
			Main.PWD = args[2];
			date = args[4];
		}
		else
		{
			log.info("����Ĳ�������");
			System.exit(1);
		}
		log.info("����ʼ����...");
		long begin = System.currentTimeMillis();
		main.run(date);
		long end = System.currentTimeMillis();
		log.info("�������н���������ʱ[" + (end - begin) + "]");
	}
	
	/**
	 * Ҫ���������
	 * @param date
	 */
	private void run(String date)
	{
		log.info("ָ������������["+date+"]");
		Connection conn = null;
		int count = 0;
		try
		{
			conn = getConn();
			log.info("1-��ʼȡָ�����ڵĸ���б�...");
			List articleList = getArticleList(conn, date);
			log.info("2-ȡ����б����,������ĵĸ����[" + articleList.size() + "],��ʼ�����б�");
			for (Iterator iterator = articleList.iterator(); iterator.hasNext();) {
				count++;
				Article article = (Article) iterator.next();
				boolean hasAss = hasAssociate(conn, article);
				if(hasAss)
				{
					log.info("���[" + count + "]���["+article.getArticleId()+"]["+article.getTitle()+"]������ظ����������������");
				}
				else
				{
					log.info("���[" + count + "]������ظ��["+article.getArticleId()+"]["+article.getTitle()+"]...");
					long begin = System.currentTimeMillis();
					List assList = getAssociate(conn, parseKeyword(article.getKeyword()));
					setAssociate(conn, article.getArticleId(), assList);
					long end = System.currentTimeMillis();
					log.info("�ɹ�����ʱ[" + (end - begin) + "]");
				}
			}
		}
		catch(Exception ex)
		{
			log.error(ex);
		}
		finally
		{
			DbUtils.closeQuietly(conn);
			conn = null;
		}
	}
	
	private List getArticleList(Connection conn, String date)
	{
		List ret = new ArrayList();
		String sql = "select articleid,title,keyword from releaselib " +
				"where pubtime between to_date('"+date+"','yyyy-mm-dd') and to_date('"+date+" 23:59:59','yyyy-mm-dd hh24:mi:ss')" +
				"order by articleid";
		Statement st = null;
		ResultSet rs = null;
		Article article = null;
		
		try
		{
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next())
			{
				article = new Article();
				article.setArticleId(rs.getInt(1));
				article.setTitle(rs.getString(2));
				article.setKeyword(rs.getString(3));
				ret.add(article);
			}
		}
		catch(Exception ex)
		{
			log.error("ȡ����б�����쳣", ex);
			log.debug(sql);
		}
		finally
		{
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(st);
		}
		return ret;
	}
	
	private static final String SQL_ASSOCIATE =
		"select count(*) from associate where nsid=?";
	
	private boolean hasAssociate(Connection conn, Article article)
	{
		boolean ret = false;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try
		{
			pst = conn.prepareStatement(SQL_ASSOCIATE);
			pst.setInt(1, article.getArticleId());
			rs = pst.executeQuery();
			if(rs.next())
			{
				if(rs.getInt(1) > 0)
					ret = true;
			}
		}
		catch(Exception ex)
		{
			log.error("�������е���ظ����Ϣ�����쳣", ex);
		}
		finally
		{
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(pst);
		}
		return ret;
	}
	
	private String parseKeyword(String keywords)
	{
		log.debug("ԭ�еĹؼ����б�" + keywords);
		if(keywords == null || "".equals(keywords))
			return "";
		int p = keywords.indexOf(" ");
		if(p < 0)
			return "";
		String ret = keywords.substring(0, p);
		return ret;
	}
	
	private static final String SQL_GET_ASSOCIATE =
		"select * from (" +
		"select articleid,title from releaselib " +
		"where contains(title,?)>0 " +
		"order by pubtime desc" +
		") where rownum<6";
	
	private List getAssociate(Connection conn, String keyword)
	{
		log.debug("keyword[" + keyword + "]");
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		String assId = "";
		String title = "";
		List ret = new ArrayList(COUNT);
		if(keyword == null || "".equals(keyword))
			return ret;
		try
		{
			pst = conn.prepareStatement(SQL_GET_ASSOCIATE);
			pst.setString(1, keyword);
			rs = pst.executeQuery();
			while(rs.next())
			{
				assId = rs.getString(1);
				title = rs.getString(2);
				log.debug("��ѯ������ظ��ID[" + assId + "],����[" + title + "]");
				ret.add(assId);
			}
		}
		catch(Exception ex)
		{
			log.error("��ȫ������ȡ��ظ�������쳣", ex);
		}
		finally
		{
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(pst);
		}
		return ret;
	}
	
	private static final String SQL_SET_ASSOCIATE =
		"insert into ASSOCIATE (NSID,ASSNSID,NSTYPE,DISPLAYORDER,AUTOTYPE,BACKUPED) " +
		"values (?,?,4,?,1,0)";
	
	private void setAssociate(Connection conn, int articleId, List articleIds)
	{
		PreparedStatement pst = null;
		int num = 0;
		for (Iterator iterator = articleIds.iterator(); iterator.hasNext();) {
			String id = (String) iterator.next();
			num++;
			try
			{
				pst = conn.prepareStatement(SQL_SET_ASSOCIATE);
				pst.setInt(1, articleId);
				pst.setString(2, id);
				pst.setInt(3, num);
				pst.executeUpdate();
			}
			catch(Exception ex)
			{
				log.error("������ظ�������쳣", ex);
			}
			finally
			{
				DbUtils.closeQuietly(pst);
			}
		}
	}
	
    private String dateFormat(long date, String format)
    {
        return dateFormat(new Date(date), format);
    }
    
    /**
     * ��ָ���ĸ�ʽ�����ڽ��и�ʽ��
     *
     * @param date ����
     * @param format ��ʽ
     * @return ��ʽ���������
     */
    private String dateFormat(Date date, String format)
    {
        SimpleDateFormat formatter = null;
        formatter = new SimpleDateFormat(format, Locale.getDefault());
        return formatter.format(date);
    }
    
    private String getToday()
    {
    	long now = System.currentTimeMillis();
    	String today = dateFormat(now, "yyyy-MM-dd");
    	return today;
    }

}
