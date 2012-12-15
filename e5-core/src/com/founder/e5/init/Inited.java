package com.founder.e5.init;

import com.founder.e5.context.E5Exception;



/**
 * @version 1.0
 * @created 08-七月-2005 14:59:54
 */
public interface Inited {

	/**
	 * 初始化方法
	 */
	public boolean init() throws E5Exception;

}