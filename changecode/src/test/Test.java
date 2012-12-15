package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Properties;


public class Test {

	public static long s = 0;

	public static long c = 0;

	public static long f = 0;
	
	public static long p = 0;

	/**
	 * @param args
	 * @throws Exception
	 */

	public void tr(File file, String outdir, String source) {
//		if (!file.isFile()) {
//			File gfile = new File(outdir
//					+ (file.getAbsolutePath().replaceAll("\\\\", "/")).replace(
//							source.replaceAll("\\\\", "/"), ""));
//			if (!gfile.exists())
//				gfile.mkdirs();
//			// System.out.println("ok");
//			return;
//		}
		
		outdir = formatString(outdir);
		source = formatString(source);
		InputStream ips = null;
		InputStreamReader ipsr = null;
		OutputStream ops = null;
		OutputStreamWriter opsr = null;
		
		File gfile = new File(outdir+"/"
				+ file.getName());
		if (gfile.exists()) {
			c++;
			return;
		}

		File filetemp = null;
		boolean isweb = false;
		boolean isNotPic = false;
		try {
			String ext = null;
			try {
				ext = file.getName().substring(
						file.getName().lastIndexOf(".") + 1,
						file.getName().length());
			} catch (Exception e) {
			}
			isweb = ext != null
					&& (ext.toLowerCase().equals("htm") || ext.toLowerCase()
							.equals("html")|| ext.toLowerCase()
							.equals("shtml"));
			isNotPic = isweb || ext.toLowerCase().equals("js")
					|| ext.toLowerCase().equals("xml")
					|| ext.toLowerCase().equals("css");
			if (isweb) {
				filetemp = new File(file.getAbsoluteFile() + ".temp");
				trMeta(file.getAbsolutePath(), filetemp);
			}

			else
				filetemp = file;

			if (isNotPic) {
				ips = new FileInputStream(filetemp);
				ipsr = new InputStreamReader(ips, "gbk");
				ops = new FileOutputStream(gfile);
				opsr = new OutputStreamWriter(ops, "UTF-8");
				int a = ipsr.read();
				while (a != -1) {
					opsr.write(a);
					a = ipsr.read();
				}
				s++;
			} else {
				ips = new FileInputStream(filetemp);
				ipsr = new InputStreamReader(ips);
				ops = new FileOutputStream(gfile);
				opsr = new OutputStreamWriter(ops);
				int a = ipsr.read();
				while (a != -1) {
					opsr.write(a);
					a = ipsr.read();
				}
				p++;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			f++;
		} finally {
			try {
				opsr.close();
			} catch (Exception e) {
			}
			try {
				ops.close();
			} catch (Exception e) {
			}
			try {
				ipsr.close();
			} catch (Exception e) {
			}
			try {
				ips.close();
			} catch (Exception e) {
			}
			try {
				if (isweb&&!filetemp.delete()) {
					filetemp.delete();
				}
			} catch (Exception e) {
			}

		}

	}

	private void trMeta(String string, File filetemp) {
		// FileInputStream fis = new FileInputStream(string);
		FileReader fr = null;
		BufferedReader br = null;
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fr = new FileReader(string);
			string = new String(string.getBytes("gbk"),"UTF-8");

			br = new BufferedReader(fr);

			fw = new FileWriter(filetemp);
			bw = new BufferedWriter(fw);
			String line = br.readLine();
			while (line != null) {
				if (line.indexOf("<meta") > -1) {
					line = line.replaceAll("[g|G][b|B]2312", "UTF-8").replaceAll("[G|g][B|b][K|k]", "UTF-8");
				}
				// System.out.println(line);
				// System.out.println("------------");
				bw.write(line + "\n");
				bw.flush();
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fr.close();
			} catch (Exception e) {
			}
			try {
				br.close();
			} catch (Exception e) {
			}
			try {
				fw.close();
			} catch (Exception e) {
			}
			try {
				fw.close();
			} catch (Exception e) {
			}
		}

	}
	
	public String formatString(String string){
		string = string.replaceAll("\\\\", "/");
		if(string.lastIndexOf("/")==(string.length()-1)){
			string = string.substring(0,string.length()-1);
		}
		return string;
	}

}
