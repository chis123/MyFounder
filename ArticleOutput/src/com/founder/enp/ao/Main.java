/*
 * $Logfile$
 * $Revision$
 * $Date$
 * $Author$
 * $History$
 *
 * Copyright (c) 2006, 北大方正电子有限公司数字媒体开发部
 * All rights reserved.
 */
package com.founder.enp.ao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.sql.Clob;
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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class Main {
	
	private static Log log = LogFactory.getLog(Main.class);
	
    //数据库连接的信息
    private static String URL = "jdbc:oracle:thin:@128.0.168.15:1521:cms40";
    private static String USER = "cmsuser";
    private static String PWD = "xyz";
    private static String ENPROOT = "y:/";
    private static String OUTDIR = "g:/test";
    private static String INFO = "";
    private static String CONTENT = "";
    
    private Connection getConn() {
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(URL, USER, PWD);
        }
        catch(Exception ex) {
            log.error("数据库连接失败", ex);
        }
        return conn;
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Main main = new Main();
		String date = main.getToday();
		if(args.length == 5)
		{
			Main.URL = args[0];
			Main.USER = args[1];
			Main.PWD = args[2];
			Main.ENPROOT = args[3];
			Main.OUTDIR = args[4];
		}
		else if(args.length == 6)
		{
			Main.URL = args[0];
			Main.USER = args[1];
			Main.PWD = args[2];
			Main.ENPROOT = args[3];
			Main.OUTDIR = args[4];
			date = args[5];
		}
		else
		{
			log.info("传入的参数不足");
			System.exit(1);
		}
		
		INFO = OUTDIR + "/info/" + date;
		CONTENT = OUTDIR + "/content/" + date;
		File outfile = new File(INFO);
		outfile.mkdirs();
		outfile = new File(CONTENT);
		outfile.mkdirs();
		
		log.info("程序开始运行...");
		long begin = System.currentTimeMillis();
		main.run(date);
		long end = System.currentTimeMillis();
		log.info("程序运行结束，共用时[" + (end - begin) + "]");
	}
	
	private void run(String date)
	{
		log.info("指定操作的日期["+date+"]");
		Connection conn = null;
		
		try
		{
			conn = getConn();
			outArticle(conn, date);
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
	
	private static final String SQL_ARTICLE =
		"select articleid,introTitle,subTitle,abstract as articleAbstract," +
		"decode(attr,61,'图片新闻',62,'头条新闻',63,'普通新闻') as attrDesc,wordCount," +
		"importance,keyword,picCount," +
		"to_char(pubtime,'yyyy-mm-dd hh24:mi:ss') as nsdate," +
		"sourcename as source,author,title," +
		"f_nodepath_name(masterid) as nodeNamePath," +
		"nvl(url,f_webrule_article(articleid)) as url," +
		"to_char(expirationTime,'yyyy-mm-dd hh24:mi:ss') as expirationTime " +
		"from releaselib " +
		"where pubtime between to_date(?,'yyyy-mm-dd') " +
		"and to_date(?,'yyyy-mm-dd hh24:mi:ss')";
	
	private void outArticle(Connection conn, String date)
	{
		PreparedStatement pst = null;
		ResultSet rs = null;
		Article article = null;
		int count = 0;
		try
		{
			pst = conn.prepareStatement(SQL_ARTICLE);
			pst.setString(1, date);
			pst.setString(2, date + " 23:59:59");
			log.info("开始从数据库取指定日期的稿件...");
			rs = pst.executeQuery();
			while(rs.next())
			{
				count++;
				article = new Article();
				article.setArticleId(rs.getInt(1));
				article.setIntroTitle(rs.getString(2));
				article.setSubTitle(rs.getString(3));
				article.setArticleAbstract(rs.getString(4));
				article.setAttrDesc(rs.getString(5));
				//article.setWordCount(rs.getInt(17));
				article.setImportance(rs.getInt(7));
				article.setKeyword(rs.getString(8));
				article.setPicCount(rs.getInt(9));
				article.setNsdate(rs.getString(10));
				article.setSource(rs.getString(11));
				article.setAuthor(rs.getString(12));
				article.setTitle(rs.getString(13));
				log.info("取稿件["+rs.getInt(1)+"]["+rs.getString(13)+"]...");
				article.setNodeNamePath(rs.getString(14));
				article.setUrl(rs.getString(15));
				article.setExpirationTime(rs.getString(16));
				String content = getContent(conn, rs.getInt(1));
				//取正文
				article.setContent(content);
				article.setWordCount(content.length());
				//取附件
				article.setAttachments(getAttachmentList(conn, rs.getInt(1)));
				
				//写实体文件
				String file = CONTENT + "/" + article.getArticleId() + ".xml";
				File xmlfile = new File(file);
				
				log.info("[" + count + "]输出文件[" + file + "]");
				outputXml(article, xmlfile);
				
				//写标志文件
				file = INFO + "/" + article.getArticleId() + ".xml";
				xmlfile = new File(file);
				xmlfile.createNewFile();
			}
		}
		catch(Exception ex)
		{
			log.error("取稿件出现异常", ex);
		}
		finally
		{
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(pst);
		}
	}
	
	private String getContent(Connection conn, int articleId)
	{
		String table = "releaselib";
		String contentField = "content";
		String cond = "articleid=" + articleId;
		return readOracleClob(conn, table, cond, contentField);
	}
	
	private static final String SQL_ATTACHMENT =
		"select a.EXTRANAME,a.ATTPATH " +
		"from attachement a,articleatt b " +
		"where a.attid=b.attid and articleid=?";
	
	private List getAttachmentList(Connection conn, int articleId)
	{
		List ret = new ArrayList();
		PreparedStatement pst = null;
		ResultSet rs = null;
		Attachment attachment = null;
		try
		{
			pst = conn.prepareStatement(SQL_ATTACHMENT);
			pst.setInt(1, articleId);
			rs = pst.executeQuery();
			while(rs.next())
			{
				attachment = new Attachment();
				attachment.setType(rs.getString(1));
				attachment.setFilepath(rs.getString(2));
				//计算文件名
				attachment.setFilename(getFilename(rs.getString(2)));
				//取文件
				File file = getFile(rs.getString(2));
				//设置base64编码
				attachment.setFilecode(getFileBase64Code(file));
				ret.add(attachment);
			}
		}
		catch(Exception ex)
		{
			log.error("取稿件附件出现异常", ex);
		}
		finally
		{
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(pst);
		}
		return ret;
	}
	
	private String getFilename(String filepath)
	{
		int p = filepath.lastIndexOf("/");
		if(p > 0)
			return filepath.substring(p + 1);
		else
			return "";
	}
	
	private File getFile(String filepath)
	{
		if(!filepath.startsWith("/"))
			filepath = "/" + filepath;
		return new File(ENPROOT + filepath);
	}
	
	private String getFileBase64Code(File file)
	{
		byte[] filebyte = null;
		try
		{
			filebyte = FileUtils.readFileToByteArray(file);
		}
		catch(Exception ex)
		{
			log.error("读文件失败", ex);
		}
		String code = new String(Base64.encodeBase64(filebyte));
		return code;
	}
	
	/**
	 * 输出稿件到指定文件
	 * @param article
	 * @param file
	 */
	private void outputXml(Article article, File file)
	{
		Document doc = new Document();
		Element root = new Element("FounderEnpML");
		
		Element version = new Element("version");
		version.addContent("4.0.0.1");
		root.addContent(version);
		
		Element p = new Element("package");
		root.addContent(p);
		
		Element articleElement = new Element("article");
		setArticleElement(articleElement, article);
		root.addContent(articleElement);
		
		doc.setRootElement(root);
		outputXml(doc, file);
	}
	
	private void outputXml(Document doc, File file)
	{
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		Format format = Format.getPrettyFormat();
		XMLOutputter xmlout = null;
		
		try
		{
			fos = new FileOutputStream(file);
			osw = new OutputStreamWriter(fos, "UTF-8");
			bw = new BufferedWriter(osw);
			xmlout = new XMLOutputter(format);
			xmlout.output(doc, bw);
		}
		catch(Exception ex)
		{
			log.error("输出XML出现异常", ex);
		}
		finally
		{
			IOUtils.closeQuietly(bw);
			IOUtils.closeQuietly(osw);
			IOUtils.closeQuietly(fos);
		}
	}
	
	private void setArticleElement(Element articleElement, Article article)
	{
		CDATA cdata = null;
		
		Element articleIdElement = new Element("articleId");
		cdata = new CDATA("" + article.getArticleId());
		articleIdElement.addContent(cdata);
		articleElement.addContent(articleIdElement);
		
		Element introTitleElement = new Element("introTitle");
		cdata = new CDATA(article.getIntroTitle());
		introTitleElement.addContent(cdata);
		articleElement.addContent(introTitleElement);
		
		Element subTitleElement = new Element("subTitle");
		cdata = new CDATA(article.getSubTitle());
		subTitleElement.addContent(cdata);
		articleElement.addContent(subTitleElement);
		
		Element articleAbstractElement = new Element("articleAbstract");
		cdata = new CDATA(article.getArticleAbstract());
		articleAbstractElement.addContent(cdata);
		articleElement.addContent(articleAbstractElement);
		
		Element attrDescElement = new Element("attrDesc");
		cdata = new CDATA(article.getAttrDesc());
		attrDescElement.addContent(cdata);
		articleElement.addContent(attrDescElement);
		
		Element wordCountElement = new Element("wordCount");
		cdata = new CDATA("" + article.getWordCount());
		wordCountElement.addContent(cdata);
		articleElement.addContent(wordCountElement);
		
		Element importanceElement = new Element("importanceCount");
		cdata = new CDATA("" + article.getImportance());
		importanceElement.addContent(cdata);
		articleElement.addContent(importanceElement);
		
		Element keywordElement = new Element("keyword");
		cdata = new CDATA(article.getKeyword());
		keywordElement.addContent(cdata);
		articleElement.addContent(keywordElement);
		
		Element picCountElement = new Element("picCount");
		cdata = new CDATA("" + article.getPicCount());
		picCountElement.addContent(cdata);
		articleElement.addContent(picCountElement);
		
		Element nsdateElement = new Element("nsdate");
		cdata = new CDATA(article.getNsdate());
		nsdateElement.addContent(cdata);
		articleElement.addContent(nsdateElement);
		
		Element sourceElement = new Element("source");
		cdata = new CDATA(article.getSource());
		sourceElement.addContent(cdata);
		articleElement.addContent(sourceElement);
		
		Element authorElement = new Element("author");
		cdata = new CDATA(article.getAuthor());
		authorElement.addContent(cdata);
		articleElement.addContent(authorElement);
		
		Element titleElement = new Element("title");
		cdata = new CDATA(article.getTitle());
		titleElement.addContent(cdata);
		articleElement.addContent(titleElement);
		
		Element nodeNamePathElement = new Element("nodeNamePath");
		cdata = new CDATA(article.getNodeNamePath());
		nodeNamePathElement.addContent(cdata);
		articleElement.addContent(nodeNamePathElement);
		
		Element urlElement = new Element("url");
		cdata = new CDATA(article.getUrl());
		urlElement.addContent(cdata);
		articleElement.addContent(urlElement);
		
		Element contentElement = new Element("content");
		cdata = new CDATA(article.getContent());
		contentElement.addContent(cdata);
		articleElement.addContent(contentElement);
		
		Element expirationTimeElement = new Element("expirationTime");
		cdata = new CDATA(article.getExpirationTime());
		expirationTimeElement.addContent(cdata);
		articleElement.addContent(expirationTimeElement);
		
		setAttachmentElement(articleElement, article.getAttachments());
	}
	
	private void setAttachmentElement(Element articleElement, List atts)
	{
		Element attElement = new Element("attachement");
		for (Iterator iter = atts.iterator(); iter.hasNext();) {
			Attachment att = (Attachment) iter.next();
			Element fileElement = new Element("file");
			
			Element filenameElement = new Element("filename");
			filenameElement.setText(att.getFilename());
			fileElement.addContent(filenameElement);
			
			Element typeElement = new Element("type");
			typeElement.setText(att.getType());
			fileElement.addContent(typeElement);
			
			Element filecodeElement = new Element("filecode");
			CDATA cdata = new CDATA(att.getFilecode());
			filecodeElement.addContent(cdata);
			fileElement.addContent(filecodeElement);
			
			attElement.addContent(fileElement);
		}
		articleElement.addContent(attElement);
	}
	
    private String dateFormat(long date, String format)
    {
        return dateFormat(new Date(date), format);
    }
    
    /**
     * 按指定的格式对日期进行格式化
     *
     * @param date 日期
     * @param format 格式
     * @return 格式化后的日期
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
    
    private String readOracleClob(Connection conn, String table,
            String cond, String contentField) {
        StringBuffer selectSQL = new StringBuffer();
        selectSQL.append("select ").append(contentField);
        selectSQL.append(" from ").append(table);
        selectSQL.append(" where ").append(cond);

        Statement st = null;
        ResultSet rs = null;
        Clob clob = null;
        Reader reader = null;
        StringBuffer result = new StringBuffer(1024);

        try {
            st = conn.createStatement();
            rs = st.executeQuery(selectSQL.toString());
            if (rs.next()) {
                clob = rs.getClob(1);
                if (clob == null) {
                    return "";
                }
                reader = clob.getCharacterStream();
                char[] szContent = new char[8192];
                int iReaded = 0;
                while ((iReaded = reader.read(szContent)) > 0) {
                    result.append(szContent, 0, iReaded);
                }

            }
        } catch (Exception ex) {
            log.error("Can not read CLOB field. ", ex);
        } finally {
            IOUtils.closeQuietly(reader);
            reader = null;
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(st);
            rs = null;
            st = null;
        }

        return result.toString();
    }

}
