/**
 * $Id: e5new com.founder.e5.commons.web PaginationSupport.java 
 * created on 2006-4-9 1:07:09
 * by liyanhui
 */
package com.founder.e5.context;

import java.util.List;

/**
 * 保存列表页需要的数据，提供了对"查询->分页显示"逻辑的支持。<br>
 * <br>
 * 注：原代码来自javaeye上robbin的代码，在其基础上增加对pageNo的支持 <br>
 * <br>
 * 对分页逻辑的支持：<br>
 * getItems()：得到当前页需要使用的记录集<br>
 * getTotalCount()：得到结果集中总记录条数<br>
 * getPrevIndex()：得到上一页第一条记录的序号<br>
 * getNextIndex()：得到下一页第一条记录的序号<br>
 * hasPrevPage()：是否有上页<br>
 * hasNextPage()：是否有下页
 * 
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-4-9 1:07:09
 */
public class PageData {

	/**
	 * 默认每页显示记录的条数
	 */
	public final static int PAGESIZE = 25;

	/**
	 * 每页显示记录的条数
	 */
	private int pageSize = PAGESIZE;

	/**
	 * 当前页用到的记录
	 */
	private List items;

	/**
	 * 结果集总记录条数
	 */
	private int totalCount;

	/**
	 * 每页第一条记录在结果集中的序号（从0开始）
	 */
	private int[] indexes = new int[ 0 ];

	/**
	 * 当前页第一条记录在结果集中的序号（从0开始）
	 */
	private int startIndex = 0;

	/**
	 * 保存对表单查询条件的引用，以便在翻页时保持这些条件
	 */
	private Object formBean;

	/**
	 * @param items
	 * @param totalCount
	 */
	public PageData( List items, int totalCount ) {
		setPageSize( PAGESIZE );
		setTotalCount( totalCount );
		setItems( items );
		setStartIndex( 0 );
	}

	/**
	 * @param items
	 * @param totalCount
	 * @param startIndex
	 */
	public PageData( List items, int totalCount, int startIndex ) {
		setPageSize( PAGESIZE );
		setTotalCount( totalCount );
		setItems( items );
		setStartIndex( startIndex );
	}

	/**
	 * @param items
	 * @param totalCount
	 * @param pageSize
	 * @param startIndex
	 */
	public PageData( List items, int totalCount, int pageSize, int startIndex ) {
		setPageSize( pageSize );
		setTotalCount( totalCount );
		setItems( items );
		setStartIndex( startIndex );
	}

	/**
	 * 返回当前页用到的记录
	 * 
	 * @return
	 */
	public List getItems() {
		return items;
	}

	/**
	 * 设置当前页用到的记录
	 * 
	 * @param items
	 */
	public void setItems( List items ) {
		this.items = items;
	}

	/**
	 * 返回每页显示记录的条数
	 * 
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置每页显示记录的条数
	 * 
	 * @param pageSize
	 */
	public void setPageSize( int pageSize ) {
		if ( pageSize > 0 )
			this.pageSize = pageSize;
	}

	/**
	 * 返回结果集总记录条数
	 * 
	 * @return
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * 设置结果集总记录条数
	 * 
	 * @param totalCount
	 */
	public void setTotalCount( int totalCount ) {
		if ( totalCount > 0 ) {
			this.totalCount = totalCount;
			int count = totalCount / pageSize;
			if ( totalCount % pageSize > 0 )
				count++;
			indexes = new int[ count ];
			for ( int i = 0; i < count; i++ ) {
				indexes[i] = pageSize * i;
			}
		} else {
			this.totalCount = 0;
		}
	}

	/**
	 * 返回每页第一条记录在结果集中的序号数组
	 * 
	 * @return
	 */
	public int[] getIndexes() {
		return indexes;
	}

	/**
	 * 设置每页第一条记录在结果集中的序号数组
	 * 
	 * @param indexes
	 */
	public void setIndexes( int[] indexes ) {
		if ( indexes != null )
			this.indexes = indexes;
	}

	/**
	 * 返回当前页第一条记录在结果集中的序号
	 * 
	 * @return
	 */
	public int getStartIndex() {
		return startIndex;
	}

	/**
	 * 设置当前页第一条记录在结果集中的序号
	 * 
	 * @param startIndex
	 */
	public void setStartIndex( int startIndex ) {
		if ( totalCount <= 0 )
			this.startIndex = 0;

		else if ( startIndex >= totalCount )
			this.startIndex = getLastPageIndex();

		else if ( startIndex < 0 )
			this.startIndex = 0;

		else {
			this.startIndex = indexes[startIndex / pageSize];
		}
	}

	/**
	 * 返回下一页第一条记录在结果集中的序号
	 * 
	 * @return 若没有下页则返回当前页
	 */
	public int getNextIndex() {
		int nextIndex = getStartIndex() + pageSize;
		if ( nextIndex >= totalCount )
			return getStartIndex();
		else
			return nextIndex;
	}

	/**
	 * 返回上一页第一条记录在结果集中的序号
	 * 
	 * @return 若没有前页则返回0
	 */
	public int getPrevIndex() {
		int previousIndex = getStartIndex() - pageSize;
		if ( previousIndex < 0 )
			return 0;
		else
			return previousIndex;
	}

	/**
	 * 返回总页数
	 * 
	 * @return 若结果集总条数为0则返回0
	 */
	public int getTotalPages() {
		return indexes.length;
	}

	/**
	 * 获取最后一页第一条记录的序号
	 * 
	 * @return
	 */
	public int getLastPageIndex() {
		if ( indexes.length == 0 )
			return 0;

		return indexes[indexes.length - 1];
	}

	/**
	 * 返回当前页码（第一页的页码为1）
	 * 
	 * @return 若结果集总条数为0则返回0
	 */
	public int getPageNo() {
		for ( int i = 0; i < indexes.length; i++ ) {
			int index = indexes[i];
			if ( index == getStartIndex() )
				return ( i + 1 );
		}

		return 0;
	}

	/**
	 * 是否有上一页
	 * 
	 * @return
	 */
	public boolean hasPrevPage() {
		return ( getPageNo() > 1 );
	}

	/**
	 * 是否有下一页
	 * 
	 * @return
	 */
	public boolean hasNextPage() {
		int curPage = getPageNo();
		return ( curPage > 0 && curPage < getTotalPages() );
	}

	/**
	 * @return
	 */
	public Object getFormBean() {
		return formBean;
	}

	/**
	 * @param formBean
	 */
	public void setFormBean( Object formBean ) {
		this.formBean = formBean;
	}

}
