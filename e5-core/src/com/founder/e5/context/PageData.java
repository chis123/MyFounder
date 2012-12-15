/**
 * $Id: e5new com.founder.e5.commons.web PaginationSupport.java 
 * created on 2006-4-9 1:07:09
 * by liyanhui
 */
package com.founder.e5.context;

import java.util.List;

/**
 * �����б�ҳ��Ҫ�����ݣ��ṩ�˶�"��ѯ->��ҳ��ʾ"�߼���֧�֡�<br>
 * <br>
 * ע��ԭ��������javaeye��robbin�Ĵ��룬������������Ӷ�pageNo��֧�� <br>
 * <br>
 * �Է�ҳ�߼���֧�֣�<br>
 * getItems()���õ���ǰҳ��Ҫʹ�õļ�¼��<br>
 * getTotalCount()���õ���������ܼ�¼����<br>
 * getPrevIndex()���õ���һҳ��һ����¼�����<br>
 * getNextIndex()���õ���һҳ��һ����¼�����<br>
 * hasPrevPage()���Ƿ�����ҳ<br>
 * hasNextPage()���Ƿ�����ҳ
 * 
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-4-9 1:07:09
 */
public class PageData {

	/**
	 * Ĭ��ÿҳ��ʾ��¼������
	 */
	public final static int PAGESIZE = 25;

	/**
	 * ÿҳ��ʾ��¼������
	 */
	private int pageSize = PAGESIZE;

	/**
	 * ��ǰҳ�õ��ļ�¼
	 */
	private List items;

	/**
	 * ������ܼ�¼����
	 */
	private int totalCount;

	/**
	 * ÿҳ��һ����¼�ڽ�����е���ţ���0��ʼ��
	 */
	private int[] indexes = new int[ 0 ];

	/**
	 * ��ǰҳ��һ����¼�ڽ�����е���ţ���0��ʼ��
	 */
	private int startIndex = 0;

	/**
	 * ����Ա���ѯ���������ã��Ա��ڷ�ҳʱ������Щ����
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
	 * ���ص�ǰҳ�õ��ļ�¼
	 * 
	 * @return
	 */
	public List getItems() {
		return items;
	}

	/**
	 * ���õ�ǰҳ�õ��ļ�¼
	 * 
	 * @param items
	 */
	public void setItems( List items ) {
		this.items = items;
	}

	/**
	 * ����ÿҳ��ʾ��¼������
	 * 
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * ����ÿҳ��ʾ��¼������
	 * 
	 * @param pageSize
	 */
	public void setPageSize( int pageSize ) {
		if ( pageSize > 0 )
			this.pageSize = pageSize;
	}

	/**
	 * ���ؽ�����ܼ�¼����
	 * 
	 * @return
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * ���ý�����ܼ�¼����
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
	 * ����ÿҳ��һ����¼�ڽ�����е��������
	 * 
	 * @return
	 */
	public int[] getIndexes() {
		return indexes;
	}

	/**
	 * ����ÿҳ��һ����¼�ڽ�����е��������
	 * 
	 * @param indexes
	 */
	public void setIndexes( int[] indexes ) {
		if ( indexes != null )
			this.indexes = indexes;
	}

	/**
	 * ���ص�ǰҳ��һ����¼�ڽ�����е����
	 * 
	 * @return
	 */
	public int getStartIndex() {
		return startIndex;
	}

	/**
	 * ���õ�ǰҳ��һ����¼�ڽ�����е����
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
	 * ������һҳ��һ����¼�ڽ�����е����
	 * 
	 * @return ��û����ҳ�򷵻ص�ǰҳ
	 */
	public int getNextIndex() {
		int nextIndex = getStartIndex() + pageSize;
		if ( nextIndex >= totalCount )
			return getStartIndex();
		else
			return nextIndex;
	}

	/**
	 * ������һҳ��һ����¼�ڽ�����е����
	 * 
	 * @return ��û��ǰҳ�򷵻�0
	 */
	public int getPrevIndex() {
		int previousIndex = getStartIndex() - pageSize;
		if ( previousIndex < 0 )
			return 0;
		else
			return previousIndex;
	}

	/**
	 * ������ҳ��
	 * 
	 * @return �������������Ϊ0�򷵻�0
	 */
	public int getTotalPages() {
		return indexes.length;
	}

	/**
	 * ��ȡ���һҳ��һ����¼�����
	 * 
	 * @return
	 */
	public int getLastPageIndex() {
		if ( indexes.length == 0 )
			return 0;

		return indexes[indexes.length - 1];
	}

	/**
	 * ���ص�ǰҳ�루��һҳ��ҳ��Ϊ1��
	 * 
	 * @return �������������Ϊ0�򷵻�0
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
	 * �Ƿ�����һҳ
	 * 
	 * @return
	 */
	public boolean hasPrevPage() {
		return ( getPageNo() > 1 );
	}

	/**
	 * �Ƿ�����һҳ
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
