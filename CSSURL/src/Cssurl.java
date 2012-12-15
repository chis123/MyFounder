import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class Cssurl {

    private static Log log = LogFactory.getLog(Cssurl.class);
    /**
     * @param args
     */
    public static void main(String[] args) {
        log.info("开始");
        if(args.length < 1){
            System.out.println("参数不对");
            System.exit(1);
        }
        Cssurl cu = new Cssurl();
        try {
            cu.run(args[0]);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private void run(String file) throws Exception {
        log.info(file);
        File f = new File(file);
        if(!f.isFile())
            return;
        String path = f.getParentFile().getAbsolutePath();
        //log.info(path);
        //String filename = f.getName();
        //log.info(filename);
        
        List lines = FileUtils.readLines(f);
        StringBuffer sb = new StringBuffer();
        for (Iterator iter = lines.iterator(); iter.hasNext();) {
            String line = (String) iter.next();
            //.info(line);
            sb.append(parse(line, path)).append("\r\n");
        }
        FileUtils.writeStringToFile(f, sb.toString(), "UTF-8");
    }
    
    private String parse(String line, String path) {
        String ret = line;
        String url = getUrl(line);
        if(url != null) {
            String filename = getName(url);
            String filepath = path + "\\" + filename;
            //log.info(">>>>"+filepath);
            saveFileByHttp(url, filepath);
            ret = StringUtils.replace(line, url, filename);
        }
        return ret;
    }
    
    private String getUrl(String line) {
        String ret = null;
        int p1=0,p2=0;
        p1 = line.indexOf("url");
        if(p1 < 0)
            return ret;
        else {
            p1 = line.indexOf("(", p1+3);
            p2 = line.indexOf(")", p1);
            ret = line.substring(p1+1,p2);
            //log.info("*********"+ret);
            return ret;
        }
    }
    
    private String getName(String url) {
        int p = url.lastIndexOf("/");
        return url.substring(p+1);
    }
    
    private void saveFileByHttp(String url, String filepath) {
        InputStream is = null;
        FileOutputStream fos = null;
        GetMethod method = null;
        HttpClient httpclient;
        try {
            httpclient = new HttpClient();
            method = new GetMethod(url);
            httpclient.executeMethod(method);
            is = method.getResponseBodyAsStream();
            fos = new FileOutputStream(filepath);
            IOUtils.copy(is, fos);
            fos.flush();
            method.releaseConnection();
        }
        catch(Exception ex) {
            log.error("", ex);
        }
        finally {
            IOUtils.closeQuietly(fos);
            IOUtils.closeQuietly(is);
        }
    }
}
