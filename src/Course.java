public class Course {
    private String cno;  //课编号
    private String cname;//课程名称
    private String croom;//课程所在教室
    private String startdate; //课程开始时间（日期）
    private String findate;  //课程结束时间（日期）
    private int[] cweek;//课程所在周数 注意不使用cweek[0] cweek[i]=1表示该课程在第i周有课
    private int[][] ctime;//课程在第几节 ctime[i][j]=1表示该课程在星期i，第j节有课，不使用0下标元素

    public static final int CLASSES_PER_DAY = 14;
    public static final int TOTAL_WEEK = 25;

    public Course(String _cno, String _cname, String _croom, String _startdate, String _findate, int[] _cweek, int[][] _ctime) {
        cno = _cno;
        cname = _cname;
        croom = _croom;
        startdate = _startdate;
        findate = _findate;
        cweek = new int[TOTAL_WEEK + 1];
        ctime = new int[CLASSES_PER_DAY + 1][8];
        for (int i = 0; i < ctime.length; i++) {
            for (int j = 0; j < ctime[i].length; j++)
                setCtime(ctime, i, j, 0);
        }
        for (int i = 0; i < cweek.length; i++) {
            setCweek(cweek, i, 0);
        }
        for (int j = 0; j < _cweek.length; j++) {
            setCweek(cweek, _cweek[j], 1);
        }
        for (int i = 0; i < _ctime.length; i++) {
            for (int j = 0; j < ctime[i].length; j++)
                if (getCtime(_ctime,i, j) == 1)
                    setCtime(ctime, i, j, 1);
        }
    }

    public Course(String _cno, String _cname, String _croom, String _startdate, String _findate) {
        cno = _cno;
        cname = _cname;
        croom = _croom;
        startdate = _startdate;
        findate = _findate;
        cweek = new int[TOTAL_WEEK + 1];
        ctime = new int[CLASSES_PER_DAY + 1][8];
        for (int i = 0; i < ctime.length; i++) {
            for (int j = 0; j < ctime[i].length; j++)
                setCtime(ctime, i, j, 0);
        }
        for (int i = 0; i < cweek.length; i++) {
            setCweek(cweek, i, 0);
        }
    }

    public String getCno() {
        return cno;
    }

    public void setCno(String cno) {
        this.cno = cno;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCroom() {
        return croom;
    }

    public void setCroom(String croom) {
        this.croom = croom;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public void setFindate(String findate) {
        this.findate = findate;
    }

    public String getStartdate() {
        return startdate;
    }

    public String getFindate() {
        return findate;
    }

    public int[] getCweek() {
        return cweek;
    }

    public void setCweek(int[] _cweek) {
        cweek = new int[TOTAL_WEEK + 1];
        for(int i=0;i<_cweek.length;i++){
            setCweek(cweek, _cweek[i], 1);
        }
    }

    public void setCweek(int[] _cweek, int loc, int setValue) {
        _cweek[loc] = setValue;
    }

    public String getCweekStringDescription(int[] _cweek) {
        StringBuilder cweektime = new StringBuilder();
        cweektime.append("第");
        int[] cw = _cweek;
        for (int i = 0; i < cw.length; i++) {
            if (cw[i] == 1) {
                if (isLastOne(cw,i))
                    cweektime.append(i);
                else cweektime.append(i + ",");
            }
        }
        cweektime.append("周\n");
        return cweektime.toString();
    }

    public int[][] getCtime() {
        return ctime;
    }

    public int getCtime(int [][]_ctime,int loci, int locj) {
        return _ctime[loci][locj];
    }

    public String getCtimeStringDescription(int[][] _ctime) {
        final String[] weekdesc_en={"","Mon","Tues","Wed","Thur","Fri","Sat","Sun"};
        final String[] weekdesc_ch={"","周一","周二","周三","周四","周五","周六","周日"};
        StringBuilder fullString=new StringBuilder();
        int []findcol={0,0,0,0,0,0,0,0};
        int[][] ct = _ctime;
        for (int i = 0; i < ct.length; i++) {
            for (int j = 0; j < ct[i].length; j++) {
                if(getCtime(ct,i,j)==1)
                    findcol[j]=1;
            }
        }
        for(int i:findcol){
            if(findcol[i]==1){
                fullString.append(weekdesc_en[i]);
                fullString.append(",");
                for(int j=0;j<ct.length;j++){
                    if(getCtime(ct,i,j)==1){
                        fullString.append(j);
                        if(!isLastOne(ct,i,j)){
                            fullString.append("/"); //isn't the last"1"↓
                        }
                    }
                }
                if(!isLastOne(findcol,i)){
                    fullString.append("\n");    //isn't the last"1"→
                }
            }
        }
        //todo:对于3/4/5/6的描述形式，改为3-6的描述形式
        return fullString.toString();
    }

    public void setCtime(int[][] _ctime, int loci, int locj, int setValue) {  //用于设定某节课的状态
        _ctime[loci][locj] = setValue;
    }

    /**
     * 判断当前位置是否为横向的最后一个“1”
     * @param arr
     * @param from
     * @return
     */
    private boolean isLastOne(int []arr,int from){
        if(from>arr.length) throw new IllegalArgumentException("Error array length");
        for(int i=from+1;i<arr.length;i++){
            if(arr[i]==1)
                return false;
        }
        return true;
    }

    /**
     * 判断当前位置是否为纵向的最后一个“1”
     * @param arr
     * @param col
     * @param colfrom
     * @return
     */
    private boolean isLastOne(int [][]arr,int col,int colfrom){
        if(colfrom>CLASSES_PER_DAY) throw new IllegalArgumentException("Error array length");
        for(int i=colfrom+1;i<CLASSES_PER_DAY+1;i++){
            if(arr[i][col]==1)
                return false;
        }
        return true;
    }


    public String toString() {     //打印课程号，课程名，课程在第几节信息，该信息将显示在EditTable控件中
        String coursenum = "课编号：" + getCno() + "\n";
        System.out.print(coursenum);
        String coursename = "课程名：" + getCname() + "\n";
        System.out.print(coursename);
        String cweektime = getCweekStringDescription(cweek);
        System.out.print(cweektime);
        String ct = getCtimeStringDescription(ctime);
        System.out.print(ct);
        return coursenum + coursename + cweektime + ct;
    }
}
