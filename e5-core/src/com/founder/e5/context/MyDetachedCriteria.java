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
 * 单独的值对象，用于暂存与Criteria相关的查询条件，并可与Session结合生成一个可执行的Criteria<br>
 * <br>
 * 该类继承自DetachedCriteria只是为了保持接口兼容性，其实与DetachedCriteria没关系 <br>
 * （之所以叫“Detached”，是指与Session脱离）
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
	 * 根据Session实例创建一个Criteria实例，并填充入此对象自身保存的条件后返回<br>
	 * 注意：返回的Criteria实例不含排序和投影信息，用户需要自己调用fillOrdersSetting、
	 * fillProjectionSetting填充入排序和投影信息
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
	 * 填充入排序信息设置
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
	 * 填充入投影信息设置
	 * 
	 * @param criteria
	 * @return
	 */
	public Criteria fillProjectionSetting( Criteria criteria ) {
		criteria.setProjection( projection );
		return criteria;
	}

}
