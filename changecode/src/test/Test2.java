package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Test2 {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
//		HashMap a = new HashMap();
//		a.put("1","1");
//		a.put("2","12");
//		System.out.println(a.get(0));
//		System.out.println(a.remove("1"));
//		System.out.println(a.size());
//		File[] files = new File("E:/19").listFiles();
//		for(File f:files){
//			System.out.println(f.getName());
//		}
		
//		java.util.Calendar c = java.util.Calendar.getInstance();
//		String year = Integer.toString(1900+c.getTime().getYear());
//		String month =  Integer.toString(c.getTime().getMonth()+1);
//		String date = Integer.toString(c.getTime().getDate());
//		String hour = Integer.toString(c.getTime().getHours());
//		String minute =  Integer.toString(c.getTime().getMinutes());
//		String second = Integer.toString(c.getTime().getSeconds());
//		File file = new File("c://dbimport.sql");
//		if(!file.exists())
//			file.createNewFile();
//		FileWriter flie = new FileWriter(file);
//		BufferedWriter bw = new BufferedWriter(flie);
//		
//		for(int i=1;i<=3000001;i=i+1000){
//			bw.write("insert into releaselib2 select * from releaselib where articleid between "+i+" and "+(i+999)+";\ncommit;\n") ;
//			//System.out.println("insert into releaselib2 select * from releaselib where articleid between "+i+" and "+(i+1000));
//		}
//		bw.close();
//		flie.close();
		String aa= "E:\\for zhong wenfeng\\gzmp\\gb\\node\\2003-12\\";
		aa = aa.replaceAll("\\\\", "/");
		System.out.println(aa);
		System.out.println(aa.lastIndexOf("/"));
		System.out.println(aa.length());
		if(aa.lastIndexOf("/")==(aa.length()-1)){
			aa = aa.substring(0,aa.length()-1);
		}
		System.out.println(aa);System.out.println(aa);
	}

}
