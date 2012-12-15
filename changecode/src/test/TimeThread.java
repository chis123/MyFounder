package test;

import java.io.File;
import java.io.IOException;

public class TimeThread extends Thread {

	public String source;

	public String outdir;

	public TimeThread(String source, String outdir) {
		super();
		this.source = source;
		this.outdir = outdir;
	}

	public void run() {
		try {
			//System.out.println("开始执行 原目录：" + source + " 目标目录：" + outdir);
			// Iterator a = listFile(source).iterator();

			new File(outdir).mkdirs();
			File file = new File(source);
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile())
					new Test().tr(files[i], outdir, source);
				else if (files[i].isDirectory() && (!isSymbolicLink(file,files[i].getName()))) {
					Start.weitpool.add(new TimeThread(source + "/"
							+ files[i].getName(), outdir + "/"
							+ files[i].getName()));
				}
			}
			// while (a.hasNext()) {
			//				
			// File file = (File) a.next();
			// //System.out.println(file.getName()+" "+file.isFile());
			// if(file.isFile())
			// new Test().tr(file, outdir, source);
			// else
			// {
			// //System.out.println(source+"/"+file.getName()+"----"+outdir+"/"+file.getName());
			// Start.weitpool.add(new
			// TimeThread(source+"/"+file.getName(),outdir+"/"+file.getName()));
			// }
			// }

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("出错了:" + source + "   " + outdir);
		} finally {
			Start.pool.remove(this);
			// System.out.println();
			// System.out.println("---");
			try {
				this.interrupt();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	public boolean isSymbolicLink(File parent, String name)
    throws IOException {
    if (parent == null) {
        File f = new File(name);
        parent = f.getParentFile();
        name = f.getName();
    }
    File toTest = new File(parent.getCanonicalPath(), name);
    return !toTest.getAbsolutePath().equals(toTest.getCanonicalPath());
}

//	private List listFile(String url) {
//
//		List retu = new ArrayList();
//		File file = new File(url);
//		File[] files = file.listFiles();
//		for (int i = 0; i < files.length; i++) {
//			retu.add(files[i]);
//		}
//
//		return retu;
//	}

}
