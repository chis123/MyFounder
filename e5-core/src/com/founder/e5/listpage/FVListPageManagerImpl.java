package com.founder.e5.listpage;

import java.util.ArrayList;
import java.util.List;

import com.founder.e5.commons.ResourceMgr;
import com.founder.e5.commons.StringUtils;
import com.founder.e5.context.Context;
import com.founder.e5.context.E5Exception;
import com.founder.e5.db.DBSession;
import com.founder.e5.db.IResultSet;

/**
 * 按文件夹设置列表方式的管理接口的实现类
 * @created 2009-8-25
 * @author Gong Lijie
 * @version 1.0
 */
public class FVListPageManagerImpl implements FVListPageManager {
	private static final String SQL_SELECT_ALL = "select * from DOM_FVLISTPAGE";
	private static final String SQL_SELECT = "select * from DOM_FVLISTPAGE where FVID=?";
	private static final String SQL_DELETE = "delete from DOM_FVLISTPAGE where FVID=?";
	private static final String SQL_INSERT = "insert into DOM_FVLISTPAGE(FVID,LISTID) values(?,?)";

	public ListPage[] get(int fvID) throws E5Exception {
		int[] ids = getIDs(fvID);
		return getPages(ids);
	}
	private ListPage[] getPages(int[] listArr) throws E5Exception {
		if (listArr == null) return null;

		ListPageManager listManager = (ListPageManager)Context.getBean(ListPageManager.class);
		ListPage[] pageList = new ListPage[listArr.length];
		for (int i = 0; i < listArr.length; i++) {
			pageList[i] = listManager.getPageList(listArr[i]);
		}
		return pageList;
	}
	public FVListPage[] getAll() throws E5Exception
	{
		List list = new ArrayList();

		DBSession conn = null;
		IResultSet rs = null;
		//从库中读出所有的文件夹和列表ID
		try {
			conn = Context.getDBSession();
			rs = conn.executeQuery(SQL_SELECT_ALL);

			while (rs.next()) {
				FVListPage page = new FVListPage(rs.getInt("FVID"), rs.getString("LISTID"), null);
				list.add(page);
			}
		} catch (Exception e) {
			throw new E5Exception("Error when get FVListpage.", e);
		} finally {
			ResourceMgr.closeQuietly(rs);
			ResourceMgr.closeQuietly(conn);
		}
		
		//根据ID得到列表对象
		for (int i = 0; i < list.size(); i++) {
			FVListPage page = (FVListPage)list.get(i);
			int[] pageIDArr = StringUtils.getIntArray(page.getPages());
			
			page.setPageList(getPages(pageIDArr));
		}
		
		return (FVListPage[])list.toArray(new FVListPage[0]);
	}
	
	public int[] getIDs(int fvID) throws E5Exception {
		DBSession conn = null;
		IResultSet rs = null;
		String lists = null;
		try {
			conn = Context.getDBSession();
			rs = conn.executeQuery(SQL_SELECT, new Object[]{new Integer(fvID)});

			if (rs.next())
				lists = rs.getString("LISTID");
		} catch (Exception e) {
			throw new E5Exception("Error when get FVListpage. fvID=" + fvID, e);
		} finally {
			ResourceMgr.closeQuietly(rs);
			ResourceMgr.closeQuietly(conn);
		}
		return StringUtils.getIntArray(lists);
	}

	public void delete(int fvID) throws E5Exception {
		DBSession conn = null;
		try {
			conn = Context.getDBSession();
			conn.executeUpdate(SQL_DELETE, new Integer[]{new Integer(fvID)});
		} catch (Exception e) {
			throw new E5Exception("Error when delete FVListpage. fvID=" + fvID, e);
		} finally {
			ResourceMgr.closeQuietly(conn);
		}
	}

	public void save(int fvID, int[] values) throws E5Exception {
		//先删除原来的
		delete(fvID);
		if (values == null) return;
		
		DBSession conn = null;
		try {
			conn = Context.getDBSession();
			String value = StringUtils.join(values, ",");
			conn.executeUpdate(SQL_INSERT, 
					new Object[]{new Integer(fvID), value});
		} catch (Exception e) {
			throw new E5Exception("Error when save FVListpage. fvID=" + fvID, e);
		} finally {
			ResourceMgr.closeQuietly(conn);
		}
	}
}
