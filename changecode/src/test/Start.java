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
			System.err.println("读取配置文件出错:" + e.getMessage());
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
		System.out.println("程序开始执行：\n开始执行时间:" + begintime + "\n"
				+ (a / (1000 * 60 * 60)) + "小时" + (a / (1000 * 60)) % 60
				+ "分钟后开始执行" + "最大线程数：" + threadcount);
		// 写文件的主线程tiemr
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
			System.out.println("开始执行 原目录：" + source + "\n目标目录：" + outdir);

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
					System.out.println("成功：" + Test.s + "  失败：" + Test.f
							+ "  存在:" + Test.c + " pic:" + Test.p);
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
			System.out.println("成功啦~~~~~~~~~~~~~~~~~~~~~~");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("出错了");
		}
		time = System.currentTimeMillis() - time;
		StringBuffer out = new StringBuffer("共耗时");
		if (time / (1000 * 60 * 60) != 0) {
			out.append(time / (1000 * 60 * 60)).append("小时");
		}
		if (time / (1000 * 60) != 0) {
			out.append((time / (1000 * 60)) % 60).append("分");
		}
		if (time / (1000) != 0) {
			out.append((time / (1000)) % 60).append("秒");
		}
		System.out.println(out);
		System.out.println("成功：" + Test.s + "  失败：" + Test.f + "  存在:" + Test.c
				+ " pic:" + Test.p);

	}

}
