package com.ren.CourseManager.entity;


import static com.ren.CourseManager.entity.constant.CLASSES_PER_DAY;
import static com.ren.CourseManager.entity.constant.TOTAL_WEEK;

public class Course {
    /**
     * classNumber �γ̱��(String)
     */
    private String cno;
    /**
     * className �γ�����(String)
     */
    private String cname;
    /**
     * classRoom �γ����ڽ���(String)
     */
    private String croom;
    /**
     * startDate �γ̿�ʼʱ�䣨���ڣ�
     */
    private String startdate;
    /**
     * finishDate �γ̽���ʱ�䣨���ڣ�
     */
    private String findate;
    /**
     * classWeeks �γ��������� ע�ⲻʹ��cweek[0] cweek[i]=1��ʾ�ÿγ��ڵ�i���п�
     */
    private int[] cweek;
    /**
     * classTimes �γ��ڵڼ��� ctime[i][j]=1��ʾ�ÿγ�������i����j���пΣ���ʹ��0�±�Ԫ��
     */
    private int[][] ctime;

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
                if (getCtime(_ctime, i, j) == 1)
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
        for (int i = 0; i < _cweek.length; i++) {
            setCweek(cweek, _cweek[i], 1);
        }
    }

    public void setCweek(int[] _cweek, int loc, int setValue) {
        _cweek[loc] = setValue;
    }

    public String getCweekStringDescription(int[] _cweek) {
        StringBuilder cweektime = new StringBuilder();
        cweektime.append("��");
        for (int i = 0; i < _cweek.length; i++) {
            if (_cweek[i] == 1) {
                if (isLastOne(_cweek, i))
                    cweektime.append(i);
                else cweektime.append(i).append(",");
            }
        }
        cweektime.append("��\n");
        return cweektime.toString();
    }

    public int[][] getCtime() {
        return ctime;
    }

    public int getCtime(int[][] _ctime, int loci, int locj) {
        return _ctime[loci][locj];
    }

    public String getCtimeStringDescription(int[][] _ctime) {
        final String[] weekdesc_en = {"", "Mon", "Tues", "Wed", "Thur", "Fri", "Sat", "Sun"};
        final String[] weekdesc_ch = {"", "��һ", "�ܶ�", "����", "����", "����", "����", "����"};
        StringBuilder fullString = new StringBuilder();
        int[] findcol = {0, 0, 0, 0, 0, 0, 0, 0};
        int[][] ct = _ctime;
        for (int i = 0; i < ct.length; i++) {
            for (int j = 0; j < ct[i].length; j++) {
                if (getCtime(ct, i, j) == 1)
                    findcol[j] = 1;
            }
        }
        for (int i : findcol) {
            if (findcol[i] == 1) {
                fullString.append(weekdesc_en[i]);
                fullString.append(",");
                for (int j = 0; j < ct.length; j++) {
                    if (getCtime(ct, i, j) == 1) {
                        fullString.append(j);
                        if (!isLastOne(ct, i, j)) {
                            fullString.append("/"); //isn't the last"1"��
                        }
                    }
                }
                if (!isLastOne(findcol, i)) {
                    fullString.append("\n");    //isn't the last"1"��
                }
            }
        }
        //todo:����3/4/5/6��������ʽ����Ϊ3-6��������ʽ
        return fullString.toString();
    }

    public void setCtime(int[][] _ctime, int loci, int locj, int setValue) {  //�����趨ĳ�ڿε�״̬
        _ctime[loci][locj] = setValue;
    }

    /**
     * �жϵ�ǰλ���Ƿ�Ϊ��������һ����1��
     *
     * @param arr
     * @param from
     * @return
     */
    private boolean isLastOne(int[] arr, int from) {
        if (from > arr.length) throw new IllegalArgumentException("Error array length");
        for (int i = from + 1; i < arr.length; i++) {
            if (arr[i] == 1)
                return false;
        }
        return true;
    }

    /**
     * �жϵ�ǰλ���Ƿ�Ϊ��������һ����1��
     *
     * @param arr
     * @param col
     * @param colfrom
     * @return
     */
    private boolean isLastOne(int[][] arr, int col, int colfrom) {
        if (colfrom > CLASSES_PER_DAY) throw new IllegalArgumentException("Error array length");
        for (int i = colfrom + 1; i < CLASSES_PER_DAY + 1; i++) {
            if (arr[i][col] == 1)
                return false;
        }
        return true;
    }

    /**
     * ��ӡ�γ̺ţ��γ������γ��ڵڼ�����Ϣ������Ϣ����ʾ��EditTable�ؼ���
     *
     * @return �γ���Ϣ���ַ�����ʾ
     */
    public String toString() {
        String coursenum = "�α�ţ�" + getCno() + "\n";
        System.out.print(coursenum);
        String coursename = "�γ�����" + getCname() + "\n";
        System.out.print(coursename);
        String cweektime = getCweekStringDescription(cweek);
        System.out.print(cweektime);
        String ct = getCtimeStringDescription(ctime);
        System.out.print(ct);
        return coursenum + coursename + cweektime + ct;
    }
}
