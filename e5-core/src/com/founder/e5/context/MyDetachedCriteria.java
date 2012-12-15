package com.founder.e5.context;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;

/**
 * ������ֵ���������ݴ���Criteria��صĲ�ѯ������������Session�������һ����ִ�е�Criteria<br>
 * <br>
 * ����̳���DetachedCriteriaֻ��Ϊ�˱��ֽӿڼ����ԣ���ʵ��DetachedCriteriaû��ϵ <br>
 * ��֮���ԽС�Detached������ָ��Session���룩
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-4-10 15:10:36
 */
public class MyDetachedCriteria extends DetachedCriteria {

	private static final long serialVersionUID = -7396962070775543643L;

	private Class entityClass;

	private ArrayList criterions = new ArrayList();

	private ArrayList orders = new ArrayList();

	private Projection projection;

	/**
	 * @param entityClass
	 */
	public MyDetachedCriteria( Class entityClass ) {
		super( entityClass.getName() );
		this.entityClass = entityClass;
	}

	/**
	 * @see org.hibernate.criterion.DetachedCriteria#add(org.hibernate.criterion.Criterion)
	 */
	public DetachedCriteria add( Criterion criterion ) {
		criterions.add( criterion );
		return this;
	}

	/**
	 * @see org.hibernate.criterion.DetachedCriteria#addOrder(org.hibernate.criterion.Order)
	 */
	public DetachedCriteria addOrder( Order order ) {
		orders.add( order );
		return this;
	}

	/**
	 * @see org.hibernate.criterion.DetachedCriteria#setProjection(org.hibernate.criterion.Projection)
	 */
	public DetachedCriteria setProjection( Projection projection ) {
		this.projection = projection;
		return this;
	}

	// ---------------------------------------------------------------------------

	/**
	 * ����Sessionʵ������һ��Criteriaʵ�����������˶���������������󷵻�<br>
	 * ע�⣺���ص�Criteriaʵ�����������ͶӰ��Ϣ���û���Ҫ�Լ�����fillOrdersSetting��
	 * fillProjectionSetting����������ͶӰ��Ϣ
	 * 
	 * @see org.hibernate.criterion.DetachedCriteria#getExecutableCriteria(org.hibernate.Session)
	 */
	public Criteria getExecutableCriteria( Session session ) {
		Criteria result = session.createCriteria( entityClass );

		for ( Iterator i = criterions.iterator(); i.hasNext(); ) {
			Criterion criterion = ( Criterion ) i.next();
			result.add( criterion );
		}

		return result;
	}

	/**
	 * �����������Ϣ����
	 * 
	 * @param criteria
	 * @return
	 */
	public Criteria fillOrdersSetting( Criteria criteria ) {
		for ( Iterator i = orders.iterator(); i.hasNext(); ) {
			Order order = ( Order ) i.next();
			criteria.addOrder( order );
		}

		return criteria;
	}

	/**
	 * �����ͶӰ��Ϣ����
	 * 
	 * @param criteria
	 * @return
	 */
	public Criteria fillProjectionSetting( Criteria criteria ) {
		criteria.setProjection( projection );
		return criteria;
	}

}
