import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class NodePic {
    
    //数据库连接的信息
    private static final String URL = "jdbc:oracle:thin:@128.0.168.15:1521:cms40";
    private static final String USER = "cmsuser";
    private static final String PWD = "xyz";
    private File picdir;
    private int nodeid = 2;
    
    public int getNodeid() {
        return nodeid;
    }

    public void setNodeid(int nodeid) {
        this.nodeid = nodeid;
    }

    private Connection getConn() {
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(URL, USER, PWD);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        return conn;
    }
    
    private void listNode(Connection conn) {
        picdir = new File(System.getProperty("user.dir") + File.separator + "pic");
        if(!picdir.exists())
            picdir.mkdirs();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "select nodeid,nodename,f_nodepath_name(nodeid),f_webrule_node(nodeid) " +
                "from typestruct where siteid=1 " +
                "and parentid=" + nodeid + " " +
                "order by nodeid";
        String nodeid,nodename,nodepath,nodeurl;
        String file;
        
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()) {
                nodeid = rs.getString(1);
                nodename = rs.getString(2);
                nodepath = rs.getString(3);
                nodeurl = rs.getString(4);
                file = picdir.getAbsolutePath() + File.separator + nodeid + "_" + nodepath + ".png";
                
                if(nodeurl == null)
                    System.out.println("[" + nodeid + "]没有设置发布规则");
                else {
                    savePic(nodeurl, file);
                    try {
                        Thread.sleep(5*1000);
                    }
                    catch(Exception ex) {
                        //ignore
                    }
                }
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        finally {
            try{rs.close();}catch(Exception ex){}
            try{pst.close();}catch(Exception ex){}
        }
    }
    
    private void savePic(String url, String file) {
        System.out.println("开始保存图片{"+url+"}...");
        Url2bmp u2b = new Url2bmp();
        u2b.setUrl(url);
        u2b.setFile(file);
        u2b.setWy(2000);
        u2b.run();
        System.out.println("保存图片完毕");
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("程序开始运行...");
        NodePic np = new NodePic();
        if(args.length > 0)
            np.setNodeid(Integer.parseInt(args[0]));
        
        Connection conn = null;
        try {
            conn = np.getConn();
            np.listNode(conn);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        finally {
            try{conn.close();}catch(Exception ex){}
        }
        System.out.println("程序运行结束");
    }

}
