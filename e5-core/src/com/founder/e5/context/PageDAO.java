/**
 * $Id: e5new com.founder.e5.commons.web AbstractManager.java created on
 * 2006-4-9 1:19:38 by liyanhui
 */
package com.founder.e5.context;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;


/**
 * 提供分页显示支持的DAO<br>
 * <br>
 * 注：原代码来自javaeye上robbin的代码，增加了findPageByCriteria对排序的支持 <br>
 * <br>
 * 注意：从网友反映的情况看，DetachedCriteria不能多次反复使用，会有问题；“一个DetachedCriteria
 * 对象反复做分页查询，第一次查询时调用的setMaxResults方法和setFirstResult方法后，这个
 * 状态保存在DetachedCriteria上了，会影响下一次count操作，因此每次查询必需new一个DetachedCriteria”。
 * 另外，“如果关联其它表，或者createAlias，返回的items就会有问题”
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-4-9 1:19:38
 */
public class PageDAO extends BaseDAO {
	/**
	 * @param detachedCriteria
	 * @return
	 */
	public PageData findPageByCriteria(
			final MyDetachedCriteria detachedCriteria ) {
		return findPageByCriteria( detachedCriteria, 0, PageData.PAGESIZE );
	}

	/**
	 * @param detachedCriteria
	 * @param startIndex
	 * @return
	 */
	public PageData findPageByCriteria(
			final MyDetachedCriteria detachedCriteria, final int startIndex ) {
		return findPageByCriteria(
				detachedCriteria,
				startIndex,
				PageData.PAGESIZE );
	}

	/**
	 * @param detachedCriteria
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public PageData findPageByCriteria(
			final MyDetachedCriteria detachedCriteria, final int startIndex,
			final int pageSize ) {

		Session session = getSession();
		try {
			Criteria criteria = detachedCriteria.getExecutableCriteria( session );
			criteria.setProjection( Projections.rowCount() );
			Integer result = ( Integer ) criteria.uniqueResult();
			int totalCount = result.intValue();

			detachedCriteria.fillOrdersSetting( criteria );
			detachedCriteria.fillProjectionSetting( criteria );

			criteria.setFirstResult( startIndex ).setMaxResults( pageSize );
			List items = criteria.list();

			PageData ps = new PageData( items, totalCount, pageSize, startIndex );
			return ps;
		} finally {
			closeSession( session );
		}
	}

	public List findAllByCriteria( final DetachedCriteria detachedCriteria ) {

		Session session = getSession();
		try {
			Criteria criteria = detachedCriteria.getExecutableCriteria( session );
			return criteria.list();
		} finally {
			closeSession( session );
		}
	}

	public int getCountByCriteria( final DetachedCriteria detachedCriteria ) {

		Session session = getSession();
		try {
			Criteria criteria = detachedCriteria.getExecutableCriteria( session );
			Integer count = ( Integer ) criteria.setProjection(
					Projections.rowCount() ).uniqueResult();
			return count.intValue();
		} finally {
			closeSession( session );
		}
	}
}
