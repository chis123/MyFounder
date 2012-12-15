package com.founder.e5.commons;

/**
 * 该类对象通过指定当前线程名和当前代码位置定义了运行时定位信息（RTLocation），唯一标识
 * 了程序运行时的一个执行点；这个运行时执行点在动态跟踪中被称为"探测点"（probe），用户通
 * 过在运行时控制探测点的打开和关闭，来控制程序中预置信息的输出，即所谓的动态跟踪。
 * 
 * @author li yanhui
 * @version 1.0
 * @created 10-二月-2006 14:36:43
 */
public class RTLocation {

	/**
	 * 当前线程名
	 */
	private final String threadName;

	/**
	 * 代码位置信息，如"com.founder.e5.dom.DocLibManagerImpl.create"
	 */
	private final String codeLocation;

	private final String id;

	/**
	 * 创建一个"探测点"对象，其中信息表明了探测点的运行时位置信息
	 * 
	 * @param threadName 线程名
	 * @param codeLocation 代码位置
	 */
	public RTLocation( String threadName, String codeLocation ) {
		if ( threadName == null || codeLocation == null )
			throw new NullPointerException();

		this.threadName = threadName;
		this.codeLocation = codeLocation;
		this.id = "[" + this.threadName + "]" + this.codeLocation;
	}

	public String getThreadName() {
		return threadName;
	}

	public String getCodeLocation() {
		return codeLocation;
	}

	/**
	 * 检查自身所代表的模式是否包含给定的具体RTLocation<br>
	 * 注意：调用该方法意味着把自身的线程名、代码位置值看做正则表达式，代表了一类探测点
	 * 的集合；而传入的RTLocation对象则看做单个探测点
	 * 
	 * @param rtl 描述了一个具体位置
	 * @return
	 */
	public boolean contain( RTLocation rtl ) {
		if ( equals( rtl ) )
			return true;

		return ( rtl.threadName.matches( threadName ) && rtl.codeLocation.matches( codeLocation ) );
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals( Object obj ) {
		if ( obj instanceof RTLocation ) {
			RTLocation rtl = ( RTLocation ) obj;
			return id.equals( rtl.id );
		}
		return false;
	}

	public int hashCode() {
		return id.hashCode();
	}

	public String toString() {
		return id;
	}

}
