package com.founder.e5.commons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * 计时器，记录每个动作（以及动作中的每个步骤）所花费的时间。用于E5系统的性能监控。<br>
 * 一个Timer只用来记录一个动作（Action），一个动作（Action）可包含多个步骤（Step）
 * 
 * @author li yanhui
 * @version 1.0
 * @created 10-二月-2006 10:47:53
 */
public class Timer {

	/**
	 * 动作名
	 */
	private String actionName;

	/**
	 * 动作开始时间
	 */
	private long actionBeginTS;

	/**
	 * 动作总耗时
	 */
	private long actionCost;

	/**
	 * 当前动作是否已开始
	 */
	private boolean actionBegin;

	/**
	 * 当前进行到的步骤
	 */
	private String curStepName;

	/**
	 * 当前步骤开始时间
	 */
	private long stepBeginTS;

	/**
	 * 当前步骤是否已开始
	 */
	private boolean curStepBegin;

	/**
	 * 记录所有步骤及其耗时信息
	 */
	private ArrayList steps = new ArrayList( 0 ); // stepName - stepCost(Long)

	public Timer() {
	}

	/**
	 * 开始记录一个动作
	 * 
	 * @param actionName
	 */
	public void begin( String actionName ) {
		if ( actionName == null )
			throw new NullPointerException();

		if ( actionBegin )
			throw new IllegalStateException( "action already begin!" );

		this.actionName = actionName;
		actionBeginTS = System.currentTimeMillis();
		actionBegin = true;
		steps = new ArrayList();
	}

	/**
	 * 调用此方法相当于先调用begin( actionName )，然后调用beginStep( stepName )
	 * 
	 * @param actionName 动作名
	 * @param stepName 步骤名
	 */
	public void begin( String actionName, String stepName ) {
		begin( actionName );
		beginStep( stepName );
	}

	/**
	 * 结束上一个步骤（如果有的话），然后开始一个新步骤
	 * 
	 * @param stepName
	 */
	public void beginStep( String stepName ) {
		if ( stepName == null )
			throw new NullPointerException();

		if ( !actionBegin )
			throw new IllegalStateException( "action not begin yet!" );

		// 如果当前已有一个步骤开始，则结束之；然后新开一个步骤
		if ( curStepBegin )
			endCurStep();

		curStepName = stepName;
		stepBeginTS = System.currentTimeMillis();
		curStepBegin = true;
	}

	/**
	 * 结束当前步骤
	 */
	private void endCurStep() {
		long stepCost = System.currentTimeMillis() - stepBeginTS;
		steps.add( new TimerPair( curStepName, stepCost ) );
		curStepName = null;
		curStepBegin = false;
	}

	/**
	 * 结束当前动作
	 */
	public void end() {
		if ( !actionBegin )
			throw new IllegalStateException( "action not begin yet!" );

		// 若当前步骤尚未结束，则结束当前步骤
		if ( curStepBegin )
			endCurStep();

		actionCost = System.currentTimeMillis() - actionBeginTS;
		actionBegin = false;
	}

	/**
	 * 返回特定步骤的时间耗费
	 * 
	 * @param stepName 步骤名
	 * @return 耗费的毫秒数
	 */
	public long getStepCost( String stepName ) {
		for ( Iterator i = steps.iterator(); i.hasNext(); ) {
			TimerPair pair = ( TimerPair ) i.next();
			if ( stepName.equals( pair.name ) )
				return pair.cost;
		}
		return 0;
	}

	/**
	 * 返回当前动作时间耗费
	 * 
	 * @return 耗费的毫秒数
	 */
	public long getActionCost() {
		return actionCost;
	}

	/**
	 * 如果动作未结束，则结束当前动作；然后生成每个步骤的时间花费报告信息<br>
	 * 注意：调用该方法时即结束当前记录动作
	 */
	public String toString() {
		if ( actionBegin )
			end();

		StringBuffer sb = new StringBuffer();
		sb.append( actionName ).append( ":" ).append(
				TimeUtils.toManFriendTime( actionCost ) );

		if ( steps.size() > 0 ) {
			sb.append( "{" );
			for ( Iterator i = steps.iterator(); i.hasNext(); ) {
				TimerPair pair = ( TimerPair ) i.next();
				sb.append( pair.name ).append( ":" ).append(
						TimeUtils.toManFriendTime( pair.cost ) );
				if ( i.hasNext() )
					sb.append( ", " );
			}
			sb.append( "}" );
		}

		return sb.toString();
	}

	public static void main( String[] args ) throws InterruptedException {
		Random rand = new Random();
		Timer timer = new Timer();

		// 第一次使用
		timer.begin( "签发", "步骤一" );
		Thread.sleep( rand.nextInt( 20 ) );
		timer.beginStep( "步骤二" );
		Thread.sleep( rand.nextInt( 30 ) );
		timer.beginStep( "步骤三" );
		Thread.sleep( rand.nextInt( 40 ) );
		// timer.end();
		System.out.println( timer );

		// 第二次使用
		timer.begin( "签发2", "步骤一2" );
		Thread.sleep( rand.nextInt( 20 ) );
		timer.beginStep( "步骤二2" );
		Thread.sleep( rand.nextInt( 30 ) );
		timer.beginStep( "步骤三2" );
		Thread.sleep( rand.nextInt( 40 ) );
		// timer.end();
		System.out.println( timer );

		Timer timer2 = new Timer();
		timer2.begin( "签发" );
		Thread.sleep( rand.nextInt( 60 ) );
		System.out.println( timer2 );

		timer2.begin( "签发2" );
		Thread.sleep( rand.nextInt( 20 ) );
		System.out.println( timer2 );
	}

	// ----------------------------------------------------------------------

	private static class TimerPair {
		public String name;

		public long cost;

		/**
		 * @param name
		 * @param cost
		 */
		public TimerPair( String name, long cost ) {
			this.name = name;
			this.cost = cost;
		}
	}

}
