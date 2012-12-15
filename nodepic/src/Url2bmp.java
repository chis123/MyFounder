public class Url2bmp {

    //url to convert to
    private String url = "http://www.founder.com.cn";
    //output file
    private String file = "c:/founder.png";
    //accept "png", "bmp", "jpeg" and "tiff"
    private String format = "png";
    //virtual screen width
    private int wx = 1024;
    //virtual screen height
    private int wy = 1500;
    //bitmap width
    private int bx = 1024;
    //bitmpat height
    private int by = 1500;
    //number of second to wait before bitmap generation
    private int wait = 2;
    //start in maximized mode
    private boolean maximize = false;
    //run and exit when done
    private boolean notinteractive = true;
    //remove right scroll bar
    private boolean removesb = true;
    
    public int getBx() {
        return bx;
    }
    public void setBx(int bx) {
        this.bx = bx;
    }
    public int getBy() {
        return by;
    }
    public void setBy(int by) {
        this.by = by;
    }
    public String getFile() {
        return file;
    }
    public void setFile(String file) {
        this.file = file;
    }
    public String getFormat() {
        return format;
    }
    public void setFormat(String format) {
        this.format = format;
    }
    public boolean isMaximize() {
        return maximize;
    }
    public void setMaximize(boolean maximize) {
        this.maximize = maximize;
    }
    public boolean isNotinteractive() {
        return notinteractive;
    }
    public void setNotinteractive(boolean notinteractive) {
        this.notinteractive = notinteractive;
    }
    public boolean isRemovesb() {
        return removesb;
    }
    public void setRemovesb(boolean removesb) {
        this.removesb = removesb;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public int getWait() {
        return wait;
    }
    public void setWait(int wait) {
        this.wait = wait;
    }
    public int getWx() {
        return wx;
    }
    public void setWx(int wx) {
        this.wx = wx;
    }
    public int getWy() {
        return wy;
    }
    public void setWy(int wy) {
        this.wy = wy;
    }
    
    public void run() {
        Runnable a = new Runnable(){
            public void run() {
                String cmd = getCommandeLine();
                try {
                    Runtime.getRuntime().exec(cmd);
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        Thread t = new Thread(a);
        t.start();
    }
    
    /**
     * 获取要执行的命令行参数
     * @return
     */
    private String getCommandeLine() {
        StringBuffer sb = new StringBuffer();
        sb.append("url2bmp.exe ");
        sb.append("-url ").append(url).append(" ");
        sb.append("-format ").append(format).append(" ");
        sb.append("-file \"").append(file).append("\" ");
        sb.append("-wx ").append(wx).append(" ");
        sb.append("-wy ").append(wy).append(" ");
        sb.append("-bx ").append(bx).append(" ");
        sb.append("-by ").append(by).append(" ");
        if(wait > 0)
            sb.append("-wait ").append(wait).append(" ");
        if(maximize)
            sb.append("-maximize ");
        if(notinteractive)
            sb.append("-notinteractive ");
        if(removesb)
            sb.append("-removesb ");
        return sb.toString();
    }
}
