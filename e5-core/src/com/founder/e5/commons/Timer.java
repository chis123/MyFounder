package com.founder.e5.commons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * ��ʱ������¼ÿ���������Լ������е�ÿ�����裩�����ѵ�ʱ�䡣����E5ϵͳ�����ܼ�ء�<br>
 * һ��Timerֻ������¼һ��������Action����һ��������Action���ɰ���������裨Step��
 * 
 * @author li yanhui
 * @version 1.0
 * @created 10-����-2006 10:47:53
 */
public class Timer {

	/**
	 * ������
	 */
	private String actionName;

	/**
	 * ������ʼʱ��
	 */
	private long actionBeginTS;

	/**
	 * �����ܺ�ʱ
	 */
	private long actionCost;

	/**
	 * ��ǰ�����Ƿ��ѿ�ʼ
	 */
	private boolean actionBegin;

	/**
	 * ��ǰ���е��Ĳ���
	 */
	private String curStepName;

	/**
	 * ��ǰ���迪ʼʱ��
	 */
	private long stepBeginTS;

	/**
	 * ��ǰ�����Ƿ��ѿ�ʼ
	 */
	private boolean curStepBegin;

	/**
	 * ��¼���в��輰���ʱ��Ϣ
	 */
	private ArrayList steps = new ArrayList( 0 ); // stepName - stepCost(Long)

	public Timer() {
	}

	/**
	 * ��ʼ��¼һ������
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
	 * ���ô˷����൱���ȵ���begin( actionName )��Ȼ�����beginStep( stepName )
	 * 
	 * @param actionName ������
	 * @param stepName ������
	 */
	public void begin( String actionName, String stepName ) {
		begin( actionName );
		beginStep( stepName );
	}

	/**
	 * ������һ�����裨����еĻ�����Ȼ��ʼһ���²���
	 * 
	 * @param stepName
	 */
	public void beginStep( String stepName ) {
		if ( stepName == null )
			throw new NullPointerException();

		if ( !actionBegin )
			throw new IllegalStateException( "action not begin yet!" );

		// �����ǰ����һ�����迪ʼ�������֮��Ȼ���¿�һ������
		if ( curStepBegin )
			endCurStep();

		curStepName = stepName;
		stepBeginTS = System.currentTimeMillis();
		curStepBegin = true;
	}

	/**
	 * ������ǰ����
	 */
	private void endCurStep() {
		long stepCost = System.currentTimeMillis() - stepBeginTS;
		steps.add( new TimerPair( curStepName, stepCost ) );
		curStepName = null;
		curStepBegin = false;
	}

	/**
	 * ������ǰ����
	 */
	public void end() {
		if ( !actionBegin )
			throw new IllegalStateException( "action not begin yet!" );

		// ����ǰ������δ�������������ǰ����
		if ( curStepBegin )
			endCurStep();

		actionCost = System.currentTimeMillis() - actionBeginTS;
		actionBegin = false;
	}

	/**
	 * �����ض������ʱ��ķ�
	 * 
	 * @param stepName ������
	 * @return �ķѵĺ�����
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
	 * ���ص�ǰ����ʱ��ķ�
	 * 
	 * @return �ķѵĺ�����
	 */
	public long getActionCost() {
		return actionCost;
	}

	/**
	 * �������δ�������������ǰ������Ȼ������ÿ�������ʱ�仨�ѱ�����Ϣ<br>
	 * ע�⣺���ø÷���ʱ��������ǰ��¼����
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

		// ��һ��ʹ��
		timer.begin( "ǩ��", "����һ" );
		Thread.sleep( rand.nextInt( 20 ) );
		timer.beginStep( "�����" );
		Thread.sleep( rand.nextInt( 30 ) );
		timer.beginStep( "������" );
		Thread.sleep( rand.nextInt( 40 ) );
		// timer.end();
		System.out.println( timer );

		// �ڶ���ʹ��
		timer.begin( "ǩ��2", "����һ2" );
		Thread.sleep( rand.nextInt( 20 ) );
		timer.beginStep( "�����2" );
		Thread.sleep( rand.nextInt( 30 ) );
		timer.beginStep( "������2" );
		Thread.sleep( rand.nextInt( 40 ) );
		// timer.end();
		System.out.println( timer );

		Timer timer2 = new Timer();
		timer2.begin( "ǩ��" );
		Thread.sleep( rand.nextInt( 60 ) );
		System.out.println( timer2 );

		timer2.begin( "ǩ��2" );
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
