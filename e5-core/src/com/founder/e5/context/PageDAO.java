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
 * �ṩ��ҳ��ʾ֧�ֵ�DAO<br>
 * <br>
 * ע��ԭ��������javaeye��robbin�Ĵ��룬������findPageByCriteria�������֧�� <br>
 * <br>
 * ע�⣺�����ѷ�ӳ���������DetachedCriteria���ܶ�η���ʹ�ã��������⣻��һ��DetachedCriteria
 * ���󷴸�����ҳ��ѯ����һ�β�ѯʱ���õ�setMaxResults������setFirstResult���������
 * ״̬������DetachedCriteria���ˣ���Ӱ����һ��count���������ÿ�β�ѯ����newһ��DetachedCriteria����
 * ���⣬�������������������createAlias�����ص�items�ͻ������⡱
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
