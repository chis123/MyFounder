package test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class Start {

	public static int threadcount = 500;

	public static ThreadPool pool = new ThreadPool();

	public static ThreadWeitPool weitpool = new ThreadWeitPool();

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Timer timer = new Timer();

		java.util.Properties p = new Properties();
		try {
			p.load(new FileInputStream(new File("config")));
		} catch (Exception e) {
			System.err.println("��ȡ�����ļ�����:" + e.getMessage());
		}
		String source = p.getProperty("sourcedir");
		String outdir = p.getProperty("outdir");
		String begintime = p.getProperty("begintime");
		try {
			threadcount = Integer.valueOf(p.getProperty("count")).intValue();
		} catch (Exception e) {
		}
		Date now = Calendar.getInstance().getTime();
		Date now1 = Calendar.getInstance().getTime();
		now1.setHours(new Integer(begintime.substring(0, 2)).intValue());
		now1.setMinutes(new Integer(begintime.substring(3, 5)).intValue());

		long temp = now1.getTime() - now.getTime();
		long a = temp >= 0 ? temp : (1000 * 60 * 60 * 24 + temp);

		a = 1000;
		System.out.println("����ʼִ�У�\n��ʼִ��ʱ��:" + begintime + "\n"
				+ (a / (1000 * 60 * 60)) + "Сʱ" + (a / (1000 * 60)) % 60
				+ "���Ӻ�ʼִ��" + "����߳�����" + threadcount);
		// д�ļ������߳�tiemr
		TimeTask cwt = new TimeTask(source, outdir);

		timer.schedule(cwt, a, 1000 * 60 * 60 * 24);

	}
    

}

class TimeTask extends TimerTask {

	private String source;

	private String outdir;

	public TimeTask() {
		super();
	}

	public TimeTask(String source, String outdir) {
		super();
		this.source = formatString(source);
		this.outdir = formatString(outdir);
	}
	public String formatString(String string){
		string = string.replaceAll("\\\\", "/");
		if(string.lastIndexOf("/")==(string.length()-1)){
			string = string.substring(0,string.length()-1);
		}
		return string;
	}
	public void run() {
		long time = System.currentTimeMillis();
		try {
			System.out.println("��ʼִ�� ԭĿ¼��" + source + "\nĿ��Ŀ¼��" + outdir);

			// Iterator a = FileUtil.listFile(source).iterator();

			TimeThread timethread = new TimeThread(source, outdir);
			Start.pool.put(timethread);
			timethread.start();
			int i = 0;
			while (true) {
				i++;
				if (i % 1000 == 0) {
					System.out.println("p:" + Start.pool.getSize() + "  w:"
							+ Start.weitpool.getSize());
				}
				if (i % 10000 == 0) {
					System.out.println("�ɹ���" + Test.s + "  ʧ�ܣ�" + Test.f
							+ "  ����:" + Test.c + " pic:" + Test.p);
				}
				if (Start.pool.getSize() > Start.threadcount) {
					Thread.sleep(200);
				} else if (Start.pool.getSize() == 0
						&& (!Start.weitpool.getThread())) {
					break;
				} else {
					TimeThread tt = Start.weitpool.get();
					if (tt != null) {
						Start.pool.put(tt);
						tt.start();
					} else {
						Thread.sleep(200);
					}
				}
			}
			System.out.println("�ɹ���~~~~~~~~~~~~~~~~~~~~~~");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("������");
		}
		time = System.currentTimeMillis() - time;
		StringBuffer out = new StringBuffer("����ʱ");
		if (time / (1000 * 60 * 60) != 0) {
			out.append(time / (1000 * 60 * 60)).append("Сʱ");
		}
		if (time / (1000 * 60) != 0) {
			out.append((time / (1000 * 60)) % 60).append("��");
		}
		if (time / (1000) != 0) {
			out.append((time / (1000)) % 60).append("��");
		}
		System.out.println(out);
		System.out.println("�ɹ���" + Test.s + "  ʧ�ܣ�" + Test.f + "  ����:" + Test.c
				+ " pic:" + Test.p);

	}

}
