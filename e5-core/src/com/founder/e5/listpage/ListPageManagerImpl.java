package com.founder.e5.listpage;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

import com.founder.e5.context.BaseDAO;
import com.founder.e5.context.Context;
import com.founder.e5.context.DAOHelper;
import com.founder.e5.context.E5Exception;
import com.founder.e5.context.EUID;
import com.founder.e5.dom.DocType;
import com.founder.e5.dom.DocTypeManager;

public class ListPageManagerImpl implements ListPageManager 
{
	public ListPageManagerImpl() 
	{
		super();
	}

	/*
	 *  (non-Javadoc)
	 * @see com.founder.e5.listpage.ListMananger#create(com.founder.e5.listpage.PageList, org.hibernate.Session)
	 */
	public boolean create(ListPage page, Session session) throws E5Exception 
	{
		boolean bRet = true;
		int listID = 1;
		Transaction t = null;
	    try
        {
		    listID = (int)EUID.getID("ListPageID");
		    t = session.beginTransaction();
		    page.setListID(listID);		    
		    session.save(page);
		    t.commit();
        }
        catch (Exception e)
        {
        	if (t != null) t.rollback();
        	bRet = false;
            throw new E5Exception("ListManagerImpl::create has error!", e);
        }
        return bRet;
    }

	/*
	 *  (non-Javadoc)
	 * @see com.founder.e5.listpage.ListMananger#update(com.founder.e5.listpage.PageList, org.hibernate.Session)
	 */
	public boolean update(ListPage page, Session session) throws E5Exception 
	{
		boolean bRet = true;
		Transaction t = null;
		try
		{
			t = session.beginTransaction();
			session.saveOrUpdate(page);
			t.commit();
		}
		catch(Exception e)
		{
			if (t != null)
				t.rollback();
			bRet = false;
			throw new E5Exception("ListManagerImpl::update has error!",e);
		}
		return bRet;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.founder.e5.listpage.ListMananger#delete(com.founder.e5.listpage.PageList, org.hibernate.Session)
	 */
	public boolean delete(ListPage page, Session session) throws E5Exception 
	{
		boolean bRet = true;
		Transaction t = null;
		try		
		{
			t = session.beginTransaction();
			session.delete(page);
			t.commit();
		}
		catch(Exception e)
		{
			if (t != null)
				t.rollback();
			bRet = false;
			throw new E5Exception("ListManagerImpl::update has error!",e);
		}
		return bRet;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.founder.e5.listpage.ListMananger#delete(int, org.hibernate.Session)
	 */
	public boolean delete(int doctypeid, Session session) throws E5Exception 
	{
		boolean bRet = true;
		try
		{
			DAOHelper.delete("delete from ListPage as listpage where listpage.docTypeID = :ID", 
					new Integer(doctypeid), 
					Hibernate.INTEGER,session);
		}
		catch(Exception e)
		{
			bRet = false;
			throw new E5Exception("ListManagerImpl::delete has error!",e);
		}
		return bRet;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.founder.e5.listpage.ListMananger#delete(int, int, org.hibernate.Session)
	 */
	public boolean delete(int doctypeid, int listid, Session session)
			throws E5Exception 
	{
		boolean bRet = true;
		try
		{
			DAOHelper.delete("delete from ListPage as listpage where listpage.docTypeID=:DocTypeID and listpage.listID=:ListID", 
	        		new Object[] {new Integer(doctypeid), new Integer(listid)}, session); 
		}
		catch(Exception e)
		{
			bRet = false;
			throw new E5Exception("ListManagerImpl::delete has error!",e);
		}
		return bRet;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.founder.e5.listpage.ListMananger#get(int,Session)
	 */
	public ListPage[] get(int doctypeid) throws E5Exception 
	{
		ListPage[] ret = null;
		try
		{
	        List list = DAOHelper.find("from ListPage where docTypeID = :ID order by listID asc", 
	        		new Integer(doctypeid), 
	        		Hibernate.INTEGER);
	        if (list != null && list.size() >0 )
	        {
	        	ret = new ListPage[list.size()];
	        	list.toArray(ret);
	        }
	        list.clear();
	        list = null;
		}
		catch(Exception e)
		{
			throw new E5Exception("ListManagerImpl::get has error!",e);			
		}
		return ret;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.founder.e5.listpage.ListMananger#get(int, int,Session)
	 */
	public ListPage get(int doctypeid, int listid) throws E5Exception 
	{
		ListPage page = null;
		try
		{
	        List list = DAOHelper.find("from ListPage where docTypeID = :DocTypeID and listID = :ListID",
	        		new String[]{"DocTypeID","ListID"},
	        		new Object[]{new Integer(doctypeid),new Integer(listid)}, 
	        		new Type[]{Hibernate.INTEGER,Hibernate.INTEGER});
	        if (list != null && list.size() > 0)
	        	page = (ListPage)list.get(0);
		}
		catch(Exception e)
		{
			throw new E5Exception("ListManagerImpl::get has error!",e);
		}
		return page;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.founder.e5.pagelist.ListManager#getPageList(int, org.hibernate.Session)
	 */
	public ListPage getPageList(int listid) throws E5Exception 
	{
		ListPage page = null;
		try
		{
	        List list = DAOHelper.find("from ListPage where listID = :ListID",
	        		new Integer(listid), 
	        		Hibernate.INTEGER);
	        if (list != null && list.size() > 0)
	        	page = (ListPage)list.get(0);
		}
		catch(Exception e)
		{
			throw new E5Exception("ListManagerImpl::getPageList has error!",e);
		}
		return page;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.founder.e5.listpage.ListPageReader#get()
	 */
	public ListPage[] get() throws E5Exception 
	{
		ListPage[] pages = null;
		try
		{
	        List list = DAOHelper.find("from ListPage order by listID asc");
	        if (list != null && list.size() > 0)
	        {
	        	pages = new ListPage[list.size()];
	        	list.toArray(pages);
	        }
		}
		catch(Exception e)
		{
			throw new E5Exception("ListManagerImpl::getPageList has error!",e);
		}
		return pages;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.founder.e5.listpage.ListPageManager#imports(java.lang.String)
	 */
	public void imports(String xml) throws E5Exception 
	{
		XMLParser parser = new XMLParser();
		Session session = null;
		try
		{
			BaseDAO dao = new BaseDAO();
			session = dao.getSession();
		}
		catch(Exception e)
		{
			throw new E5Exception("do not find center db!",e);
		}		
		try
		{
			DocTypeManager manager = (DocTypeManager)Context.getBean(DocTypeManager.class);
			parser.load(xml);
			int typecount = parser.getChildsCount("//ListPages/DocType");
			int k = 0;
			for (int i = 0; i < typecount; i++)
			{
				k = i + 1;
				String typename = parser.getField("//ListPages/DocType["+k+"]/name");
				int typeid = CheckDocType(manager,typename);
				if (typeid > 0)
				{
					int pagecount = parser.getChildsCount("//ListPages/DocType["+k+"]/ListPage");
					int kk = 0;
					for (int j = 0; j < pagecount; j++)
					{
						kk = j + 1;
						ListPage page = new ListPage();
						page.setDocTypeID(typeid);
						page.setListName(parser.getField("//ListPages/DocType["+k+"]/ListPage["+kk+"]/name"));
						page.setListBuilderName(parser.getField("//ListPages/DocType["+k+"]/ListPage["+kk+"]/builder"));
						page.setPathXSL(parser.getField("//ListPages/DocType["+k+"]/ListPage["+kk+"]/pathxsl"));
						page.setPathJS(parser.getField("//ListPages/DocType["+k+"]/ListPage["+kk+"]/pathjs"));
						page.setPathCSS(parser.getField("//ListPages/DocType["+k+"]/ListPage["+kk+"]/pathcss"));
						page.setListXML(parser.getChildXML("//ListPages/DocType["+k+"]/ListPage["+kk+"]/listxml/root"));
						page.setTemplateSlice(parser.getChildXML("//ListPages/DocType["+k+"]/ListPage["+kk+"]/slicexml/custom"));						
						create(page,session);
					}
				}
			}
		}
		catch(Exception e)
		{
			throw new E5Exception("parser xml has error["+xml+"]!",e);
		}
		finally
		{
			if (session != null)
				session.close();
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see com.founder.e5.listpage.ListPageManager#exports()
	 */
	public String exports() throws E5Exception 
	{
		StringBuffer buf = new StringBuffer(200);
		DocTypeManager manager = (DocTypeManager)Context.getBean(DocTypeManager.class);
		DocType[] types = manager.getTypes(0);
		//(1)需要加入
		buf.append("<ListPages>\n");
		if (types != null)
		{
			for (int i = 0; i < types.length; i++)
			{
				ListPage[] pages = get(types[i].getDocTypeID());
				if (pages != null)
				{
					buf.append("<DocType>\n");
					buf.append("<name><![CDATA[").append(types[i].getDocTypeName()).append("]]></name>\n");
					for (int j = 0; j < pages.length; j++)
					{
						buf.append("<ListPage>\n");
						buf.append("<name><![CDATA[").append(pages[j].getListName()).append("]]></name>\n");
						buf.append("<builder>").append(pages[j].getListBuilderName()).append("</builder>\n");
						buf.append("<pathxsl><![CDATA[").append(pages[j].getPathXSL()).append("]]></pathxsl>\n");
						buf.append("<pathjs><![CDATA[").append(pages[j].getPathJS()).append("]]></pathjs>\n");
						buf.append("<pathcss><![CDATA[").append(pages[j].getPathCSS()).append("]]></pathcss>\n");
						buf.append("<listxml>").append(pages[j].getListXML()).append("</listxml>\n");
						buf.append("<slicexml>").append(pages[j].getTemplateSlice()).append("</slicexml>\n");
						buf.append("</ListPage>\n");
					}
					buf.append("</DocType>\n");					
				}
			}
		}
		buf.append("</ListPages>\n");
		return buf.toString();
	}
	
	private int CheckDocType(DocTypeManager manager, String typename)
	{
		int typeid = 0;
		try
		{
			if (manager != null)
			{
				DocType type = manager.get(typename);
				if (type != null)
					typeid = type.getDocTypeID();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return typeid;
	}
}
