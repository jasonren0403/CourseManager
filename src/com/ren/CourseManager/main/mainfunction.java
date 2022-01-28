package com.ren.CourseManager.main;

import com.ren.CourseManager.entity.Course;
import com.ren.CourseManager.entity.constant;
import com.ren.CourseManager.utils.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;


public class mainfunction extends JFrame {

    private DBConnection getteacherinfo, getclassroominfo, editcon;
    private Connection conn, conn2, conn3;
    private EditTable teacher_timetable, room_timetable, date_timetable;
    private boolean auth;
    private EditTable.MyTableModel model;
    private String UID, type, sno, tno; //��ǰ�û�ID�����ͣ�ѧ���ţ���ʦ��
    private static int id = 0;

    //��ʦ�嵥
    List<String> Teacher = new ArrayList<String>();
    //�����嵥
    List<String> Room = new ArrayList<String>();

    //���캯��
    public mainfunction(String username, String name, String _type) {
        super("�γ̲鿴�����--" + _type + "-" + username + "-" + name); //���ñ���
        UID = username;
        type = _type;
        FrameManager.FrameSetup(this,1000,500,null,true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        init();
        this.setVisible(true);

        //��Ҫ���ݿ��ν�
        Room.add("220");
        Teacher.add("�ϵ»���У");
    }

    public void InitTeacherList() {
        DBConnection getFullTeacherInfo = new DBConnection();
        Connection con = getFullTeacherInfo.getConnection();
        try {
            String query = "SELECT * FROM teacher";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs == null) {
                JOptionPane.showMessageDialog(null, "û�н�ʦ����");
            } else {
                while (rs.next()) {
                    String name = rs.getString("tname").trim();
                    Teacher.add(name);
                }
                rs.close();
            }
            stmt.close();
        } catch (SQLException sqlex) {
            System.out.println(sqlex.toString());
        } finally {
            getFullTeacherInfo.CloseDBConnection(con);
        }
    }

    public void InitClassroomList() {
        DBConnection getFullClassroomInfo = new DBConnection();
        Connection con = getFullClassroomInfo.getConnection();
        try {
            String query = "SELECT DISTINCT croom FROM course";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs == null) {
                JOptionPane.showMessageDialog(null, "û�н�������");
            } else {
                while (rs.next()) {
                    String room = rs.getString("croom").trim();
                    Room.add(room);
                }
                rs.close();
            }
            stmt.close();
        } catch (SQLException sqlex) {
            System.out.println(sqlex.toString());
        } finally {
            getFullClassroomInfo.CloseDBConnection(con);
        }
    }

    public void init() {
        FrameManager.setWindowLoc(this,FrameManager.FRAME_MANAGER_MAINFUNC_WINDOW);

        JTabbedPane tp = new JTabbedPane();
        this.setContentPane(tp);
        //����Ĭ�ϵ�����panel ��ǰ�γ̱�͹���

        //��ȡ��ǰ����
        Calendar cal = Calendar.getInstance();
        int current_year = cal.get(Calendar.YEAR);
        int current_month = cal.get(Calendar.MONTH) + 1;
        int current_day = cal.get(Calendar.DATE);

        EditTable current_timetable = new EditTable(true);//��Ҫ���ݿ��ν�
        String datestr = String.valueOf(10000 * current_year + 100 * current_month + current_day);
        current_timetable.InitEditTableByDate(datestr, "full");

        JPanel manager = new JPanel();
        manager.setBounds(50, 50, 500, 500);
        tp.add(current_timetable);
        tp.setTitleAt(0, "��ѧ�ڿγ̱�");
        tp.add(manager);
        tp.setTitleAt(1, "����");

        // �����γ̱��γ̹����鿴�ı�ǩ
        JLabel timetable_label = new JLabel("�γ̱�");
        JLabel m_label = new JLabel("�γ̹���");
        JLabel delete_label = new JLabel("ɾ���γ̱�");

        JLabel check_label = new JLabel("�鿴");
        JLabel check_by_date_label = new JLabel("�����ڲ鿴��");
        JLabel check_by_room_label = new JLabel("�����Ҳ鿴��");
        JLabel check_by_teacher_label = new JLabel("����ʦ�鿴��");
        JLabel year_label = new JLabel("��");
        JLabel month_label = new JLabel("��");
        JLabel day_label = new JLabel("��");

        JLabel delete_day_label = new JLabel("����");
        JLabel delete_month_label = new JLabel("��");
        JLabel delete_year_label = new JLabel("��");
        JLabel delete_to_day_label = new JLabel("��");
        JLabel delete_to_month_label = new JLabel("��");
        JLabel delete_to_year_label = new JLabel("��");

        //������ť
        JButton add_timetable_btn = new JButton("��ӿγ̱�");
        JButton delete_timetable_btn = new JButton("ȷ��ɾ���γ̱�");
        JButton confirm_date_btn = new JButton("ȷ��");
        JButton confirm_room_btn = new JButton("ȷ��");
        JButton confirm_teacher_btn = new JButton("ȷ��");
        JButton close = new JButton("�ر�", new ImageIcon("img/return.jpg"));
        //��Ӱ�ť����
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final int t = tp.getSelectedIndex();
                tp.remove(t);
            }
        });
        current_timetable.add(close, BorderLayout.SOUTH);

        //���������
        JTextField check_by_room_field = new JTextField();
        JTextField check_by_teacher_field = new JTextField();
        JTextField year_field = new JTextField(current_year + "");
        JTextField month_field = new JTextField(current_month + "");
        JTextField day_field = new JTextField(current_day + "");
        JTextField delete_year_field = new JTextField();
        JTextField delete_month_field = new JTextField();
        JTextField delete_day_field = new JTextField();
        JTextField delete_to_year_field = new JTextField();
        JTextField delete_to_month_field = new JTextField();
        JTextField delete_to_day_field = new JTextField();
        year_field.setMaximumSize(new Dimension(40, 30));
        month_field.setMaximumSize(new Dimension(30, 30));
        day_field.setMaximumSize(new Dimension(30, 30));
        check_by_room_field.setMaximumSize(new Dimension(100, 30));
        check_by_teacher_field.setMaximumSize(new Dimension(100, 30));
        delete_year_field.setMaximumSize(new Dimension(40, 30));
        delete_month_field.setMaximumSize(new Dimension(30, 30));
        delete_day_field.setMaximumSize(new Dimension(30, 30));
        delete_to_year_field.setMaximumSize(new Dimension(40, 30));
        delete_to_month_field.setMaximumSize(new Dimension(30, 30));
        delete_to_day_field.setMaximumSize(new Dimension(30, 30));

        //������������еı�ǩ��������С��λ��
        manager.setLayout(new BoxLayout(manager, BoxLayout.Y_AXIS));
        Box timetable_box = Box.createVerticalBox();
        Box add_timetable_box = Box.createHorizontalBox();
        Box delete_timetable_box = Box.createHorizontalBox();
        Box check_box = Box.createHorizontalBox();
        Box check_date_box = Box.createHorizontalBox();
        Box check_teacher_box = Box.createHorizontalBox();
        Box check_room_box = Box.createHorizontalBox();
        Box right_check_box = Box.createVerticalBox();

        FrameManager.addComponents(add_timetable_box,add_timetable_btn);

        FrameManager.addComponents(delete_timetable_box,delete_label,delete_year_field,delete_year_label,
                delete_month_field,delete_month_label,delete_day_field,delete_day_label,delete_to_day_field,delete_to_day_label,
                delete_to_month_field,delete_to_month_label,delete_to_year_field,delete_to_year_label,delete_timetable_btn);

        FrameManager.addComponents(timetable_box,add_timetable_box,delete_timetable_box);

        FrameManager.addComponents(check_date_box,check_by_date_label,year_field,year_label,month_field,month_label,day_field,day_label,confirm_date_btn);

        FrameManager.addComponents(check_teacher_box,check_by_teacher_field,check_by_teacher_label,confirm_teacher_btn);

        FrameManager.addComponents(check_room_box,check_by_room_field,check_by_room_label,confirm_room_btn);

        FrameManager.addComponents(right_check_box,check_date_box,check_teacher_box,check_room_box);

        FrameManager.addComponents(check_box,check_label,right_check_box);

        FrameManager.addComponents(manager,timetable_box,check_box);

        FrameManager.addComponents(manager,timetable_box,check_box);

        //��������ȷ�ϰ�ť����
        confirm_date_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!year_field.getText().isEmpty() && !month_field.getText().isEmpty() && !day_field.getText().isEmpty()
                        && Fdate.validate(year_field.getText() + "-" + month_field.getText() + "-" + day_field.getText())) {
                    String inputdatestr = String.valueOf(Fdate.convertToNum
                            (year_field.getText() + "-" + month_field.getText() + "-" + day_field.getText()));
                    date_timetable = new EditTable(true);//��Ҫ���ݿ��νӡ�
                    date_timetable.InitEditTableByDate(inputdatestr, "single_day");
                    JButton close = new JButton("�ر�", new ImageIcon("img/return.jpg"));
                    //��Ӱ�ť����
                    close.addActionListener(e12 -> {
                        final int t = tp.getSelectedIndex();
                        tp.remove(t);
                    });
                    date_timetable.add(close, BorderLayout.SOUTH);
                    final int temp = tp.getTabCount();
                    String date = year_field.getText() + "��" + month_field.getText() + "��" + day_field.getText() + "�տγ̱�";
                    tp.add(date_timetable);
                    tp.setTitleAt(temp, date);

                } else if (!Fdate.validate(year_field.getText() + "-" + month_field.getText() + "-" + day_field.getText())) {
                    JOptionPane.showMessageDialog(null, "�����������������ȷ��", "����",
                            JOptionPane.ERROR_MESSAGE, new ImageIcon("img/cancel.jpg"));
                } else if (year_field.getText().isEmpty() || month_field.getText().isEmpty() || day_field.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "������Ϊ�գ�����������", "����",
                            JOptionPane.ERROR_MESSAGE, new ImageIcon("img/cancel.jpg"));
                }
            }
        });

        //������ʦ��ѯȷ�ϰ�ť����
        confirm_teacher_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String search_teacher_id = "teacher";//��Ҫ���ݿ��ν�
                getteacherinfo = new DBConnection();
                conn = getteacherinfo.getConnection();
                String queryteacherid = "SELECT * FROM teacher WHERE tname='" + check_by_teacher_field.getText() + "'";
                //SELECT * FROM teacher WHERE tname='Ҫ���ҵ���ʦ������'
                try {      //������ʦid
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(queryteacherid);
                    while (rs.next()) {
                        search_teacher_id = rs.getString("tno").trim();
                    }
                } catch (SQLException sqlex) {
                    System.out.println(sqlex.toString());
                    System.out.println(queryteacherid);
                } finally {
                    getteacherinfo.CloseDBConnection(conn);
                }

                if (!check_by_teacher_field.getText().isEmpty() && Teacher.contains(check_by_teacher_field.getText())) {
                    auth = false;
                    if (UID.equals(search_teacher_id)) {//��ǰsearch_teacher_id��teacher
                        auth = true;
                    }
                    teacher_timetable = new EditTable(auth);//��Ҫ���ݿ��ν�
                    teacher_timetable.InitEditTableByTeacher(search_teacher_id);
                    JButton close = new JButton("�ر�", new ImageIcon("img/return.jpg"));
                    //��Ӱ�ť����
                    close.addActionListener(e13 -> {
                        final int t = tp.getSelectedIndex();
                        tp.remove(t);
                    });
                    teacher_timetable.add(close, BorderLayout.SOUTH);
                    final int temp = tp.getTabCount();
                    String teacher = check_by_teacher_field.getText() + "��ʦ�Ŀγ̱�";
                    tp.add(teacher_timetable);
                    tp.setTitleAt(temp, teacher);
                } else if (check_by_teacher_field.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "��������Ϊ�գ���ȷ��", "����",
                            JOptionPane.ERROR_MESSAGE, new ImageIcon("img/cancel.jpg"));
                } else if (!Teacher.contains(check_by_teacher_field.getText())) {
                    JOptionPane.showMessageDialog(null, "δ�ҵ��ý�ʦ����ȷ��", "����",
                            JOptionPane.INFORMATION_MESSAGE, new ImageIcon("img/info.jpg"));
                }
            }
        });

        //�������Ҳ�ѯȷ�ϰ�ť����
        confirm_room_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String search_room_id = "room";//��Ҫ��ѯ���ҵ�id
                // ��Ҫ���ݿ��νӡ�
                getclassroominfo = new DBConnection();
                conn = getclassroominfo.getConnection();
                try {      //���ҽ�������
                    Statement stmt = conn.createStatement();
                    String getclassroominfo = "SELECT * FROM course WHERE croom='" + check_by_room_field.getText() + "'";
                    //SELECT * FROM course WHERE croom='Ҫ���ҵĽ��ҵ�����'
                    ResultSet rs = stmt.executeQuery(getclassroominfo);
                    while (rs.next()) {
                        search_room_id = rs.getString("croom");
                    }
                } catch (SQLException sqlex) {
                    System.out.println(sqlex.toString());
                } finally {
                    getclassroominfo.CloseDBConnection(conn);
                }
                if (!check_by_room_field.getText().isEmpty() && Room.contains(check_by_room_field.getText())) {
                    auth = false;
                    //��Ҫ����ҵ�id ��ǰsearch_room_id��room //���ҿα�ԭ���ϲ��ܸ�
                    /*if (username.equals(search_room_id)) {
                        auth = true;
                    }*/
                    room_timetable = new EditTable(auth);//��Ҫ���ݿ��νӡ�
                    room_timetable.InitEditTableByRoom(search_room_id);
                    JButton close = new JButton("�ر�", new ImageIcon("img/return.jpg"));
                    //��Ӱ�ť����
                    close.addActionListener(e14 -> {
                        final int t = tp.getSelectedIndex();
                        tp.remove(t);
                    });
                    room_timetable.add(close, BorderLayout.SOUTH);
                    final int temp = tp.getTabCount();
                    String room = check_by_room_field.getText() + "���ҵĿγ̱�";
                    tp.add(room_timetable);
                    tp.setTitleAt(temp, room);
                } else if (check_by_room_field.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "��������Ϊ�գ���ȷ��", "����",
                            JOptionPane.ERROR_MESSAGE, new ImageIcon("img/cancel.jpg"));
                } else if (!Room.contains(check_by_room_field.getText())) {
                    JOptionPane.showMessageDialog(null, "δ�ҵ��ý��ң���ȷ��", "����",
                            JOptionPane.INFORMATION_MESSAGE, new ImageIcon("img/info.jpg"));
                }
            }
        });

        //todo:�½��γ̱�ť�����߼�����
        add_timetable_btn.addActionListener(e -> {
            final int temp = tp.getTabCount();
            EditTable one = new EditTable(true);
            tp.add(one);
            tp.setTitleAt(temp, "�µĿγ̱�");
            JButton close1 = new JButton("�ر�", new ImageIcon("img/return.jpg"));
            //��Ӱ�ť����
            close1.addActionListener(e15 -> {
                final int t = tp.getSelectedIndex();
                tp.remove(t);
            });
            one.add(close1, BorderLayout.SOUTH);
            //�����γ̱�Ŀ�ʼʱ�����ֹʱ���textfield��label
            //��Ҫ���ݿ��ν�
            JTextField from_year = new JTextField();
            JTextField from_month = new JTextField();
            JTextField from_day = new JTextField();
            JTextField to_year = new JTextField();
            JTextField to_month = new JTextField();
            JTextField to_day = new JTextField();
            JLabel year_label1 = new JLabel("��");
            JLabel month_label1 = new JLabel("��");
            JLabel day_label1 = new JLabel("��");
            JLabel to_year_label = new JLabel("��");
            JLabel to_month_label = new JLabel("��");
            JLabel to_day_label = new JLabel("��");
            JLabel to_label = new JLabel("��");
            //����ȷ�ϵİ�ť
            JButton confirm_date_button = new JButton("ȷ��");
            confirm_date_button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int get_from_year = Integer.valueOf(from_year.getText());
                    int get_from_month = Integer.valueOf(from_month.getText());
                    int get_from_day = Integer.valueOf(from_day.getText());
                    int get_to_year = Integer.valueOf(to_year.getText());
                    int get_to_month = Integer.valueOf(to_month.getText());
                    int get_to_day = Integer.valueOf(to_day.getText());
                    if (from_day.getText().isEmpty() || from_month.getText().isEmpty() || from_year.getText().isEmpty() || to_year.getText().isEmpty() || to_month.getText().isEmpty() || to_day.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "������������пհף���ȷ��", "����",
                                JOptionPane.ERROR_MESSAGE);
                    } else if (Fdate.validate(from_year.getText() + "-" + from_month.getText() + "-" + from_day.getText()) && Fdate.validate(to_year.getText() + "-" + to_month.getText() + "-" + to_day.getText())) {
                        if ((get_from_year * 10000 + get_from_month * 100 + get_from_day) <= (get_to_year * 10000 + get_to_month * 100 + get_to_day)) {
                            DBConnection query1 = new DBConnection();
                            conn = query1.getConnection();
                            String dateQuery = "select * from course where ? between startdate and findate";
                            boolean exist_flag = true;
                            try {
                                Statement stmt = conn.createStatement();
                                ResultSet rs = stmt.executeQuery(dateQuery);
                                while (rs.next()) {
                                    int start = Integer.parseInt(rs.getString("startdate").trim());
                                    int end = Integer.parseInt(rs.getString("findate").trim());
                                    if ((get_from_year * 10000 + get_from_month * 100 + get_from_day) >= start || (get_to_year * 10000 + get_to_month * 100 + get_to_day) <= end) {
                                        JOptionPane.showMessageDialog(null, "��������������Ѵ��ڿγ̱���ȷ��", "����",
                                                JOptionPane.ERROR_MESSAGE);
                                        exist_flag = false;
                                    }
                                }
                                rs.close();
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                                System.out.println(dateQuery);
                            } finally {
                                query1.CloseDBConnection(conn);
                            }
                            if (exist_flag) {
                                //todo:���⣬����
                                JOptionPane.showMessageDialog(null, "��ӳɹ���", "��ʾ",//��Ҫ���ݿ��ν�
                                        JOptionPane.INFORMATION_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "�����������������ȷ��", "����",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (!Fdate.validate(from_year.getText() + "-" + from_month.getText() + "-" + from_day.getText()) || !Fdate.validate(to_year.getText() + "-" + to_month.getText() + "-" + to_day.getText())) {
                        JOptionPane.showMessageDialog(null, "�����������������ȷ��", "����",
                                JOptionPane.ERROR_MESSAGE);
                    }

                }
            });
            Box a = Box.createHorizontalBox();
            FrameManager.addComponents(a, from_year, year_label1, from_month, month_label1, from_day, day_label1,
                    to_label, to_year, to_year_label, to_month, to_month_label, to_day, to_day_label, confirm_date_button);
            one.add(a, BorderLayout.NORTH);
        });

        //todo:����ɾ���γ̱�ť�Ĺ���
        delete_timetable_btn.addActionListener(e -> {
            int get_from_year = Integer.parseInt(delete_year_field.getText());
            int get_from_month = Integer.parseInt(delete_month_field.getText());
            int get_from_day = Integer.parseInt(delete_day_field.getText());
            int get_to_year = Integer.parseInt(delete_to_year_field.getText());
            int get_to_month = Integer.parseInt(delete_to_month_field.getText());
            int get_to_day = Integer.parseInt(delete_to_day_field.getText());
            if (delete_day_field.getText().isEmpty() || delete_month_field.getText().isEmpty() || delete_year_field.getText().isEmpty() || delete_to_year_field.getText().isEmpty() || delete_to_month_field.getText().isEmpty() || delete_to_day_field.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "������������пհף�����������", "����",
                        JOptionPane.ERROR_MESSAGE);
            } else if (Fdate.validate(delete_year_field.getText() + "-" + delete_month_field.getText() + "-" + delete_day_field.getText()) && Fdate.validate(delete_to_year_field.getText() + "-" + delete_to_month_field.getText() + "-" + delete_to_day_field.getText())) {
                if ((get_from_year * 10000 + get_from_month * 100 + get_from_day) <= (get_to_year * 10000 + get_to_month * 100 + get_to_day)) {
                    JOptionPane.showMessageDialog(null, "ɾ���ɹ���", "��ʾ",//��Ҫ���ݿ��ν�
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "�����������������ȷ��", "����",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else if (!Fdate.validate(delete_year_field.getText() + "-" + delete_month_field.getText() + "-" + delete_day_field.getText()) || !Fdate.validate(delete_to_year_field.getText() + "-" + delete_to_month_field.getText() + "-" + delete_to_day_field.getText())) {
                JOptionPane.showMessageDialog(null, "�����������������ȷ��", "����",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        //����������ݿ�
        //�����ʦ���ݿ�
        InitClassroomList();
        InitTeacherList();
    }

    private int[] parseCweek(String value) {  //ע��value��ʽ
        String val = "";
        if (value.contains(" ")) {
            val = value.replace(" ", ",");
        } else if (value.contains("��")) {
            val = value.replace("��", ",");
        } else if (value.contains("/")) {
            val = value.replace("/", ",");
        } else
            val = value;
        int[] intArr = new int[constant.TOTAL_WEEK + 1];
        for(int a:intArr)
            intArr[a]=0;
        if (val.matches("/\\d{1,2}-\\d{1,2}/")) {  //��x-y��ʽ����������7-12
            int pos = val.indexOf("-");
            int start = Integer.parseInt(val.substring(0, pos - 1));
            int end = Integer.parseInt(val.substring(pos + 1, val.length()));
            intArr = new int[end - start + 1];
            for (int i = start; i <= end; i++) {
                intArr[i] = 1;
            }
        } else {                                          //�Զ��ŷָ���ʽ����������7,9,10
            String[] valueArr = val.split(",");
            for (String i : valueArr) {
                intArr[Integer.parseInt(i)] = 1;
            }
        }
        return intArr;
    }

    private int[][] parseCtime(String value) {
        //ǰ��������ڼ�д�������ǿγ�ʱ���x-y(x<y)���Զ��ŷָ�
        int week = 0;
        int class_start = 0;
        int class_end = 0;
        int[][] CtimeArr = new int[15][8];
        if (value.matches("[a-zA-Z]{1,4},\\w+")) {
            String args[] = value.split(",��");
            switch (args[0]) {
                case "Mon":
                case "��һ":
                    week = 1;
                    break;
                case "Tues":
                case "�ܶ�":
                    week = 2;
                    break;
                case "Wed":
                case "����":
                    week = 3;
                    break;
                case "Thur":
                case "����":
                    week = 4;
                    break;
                case "Fri":
                case "����":
                    week = 5;
                    break;
                case "Sat":
                case "����":
                    week = 6;
                    break;
                case "Sun":
                case "����":
                    week = 7;
                    break;
                default:
                    return new int[0][0];
            }
            if (args[1].matches("/\\d{1,2}-\\d{1,2}/")) {
                int pos = args[1].indexOf("-");
                class_start = Integer.parseInt(args[1].substring(0, pos - 1));
                class_end = Integer.parseInt(args[1].substring(pos + 1));
                if (class_start > class_end) return new int[0][0];
                for (int i = class_start; i <= class_end; i++) {
                    CtimeArr[week][i] = 1;
                }
                return CtimeArr;
            }
        }
        return new int[0][0];
    }

    /**
     * TableDemo is just like SimpleTableDemo, except that it
     * uses a custom TableModel.
     */
    public class EditTable extends JPanel {
        private boolean AUTHORIZATION;
        private String start;
        private String end;
        private int startweek;
        private int endweek;

        public EditTable(boolean auth) {
            super(new BorderLayout());
            AUTHORIZATION = auth;
            model = new MyTableModel();
            JTable table = new JTable(model);
            table.setPreferredScrollableViewportSize(new Dimension(500, 500));
            table.setFillsViewportHeight(true);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            //Create the scroll pane and add the table to it.
            JScrollPane scrollPane = new JScrollPane(table);

            //Add the scroll pane to this panel.
            add(scrollPane);
            //Add tip If auth==false
            if (!auth) {
                add(new JLabel("��δ����޸ĸÿγ̱��Ȩ��"), BorderLayout.NORTH);
            } else {
                Box manageone = Box.createHorizontalBox();
                Box managetwo = Box.createHorizontalBox();
                Box manage = Box.createVerticalBox();
                JLabel tip = new JLabel("��Ҫ��ʾ�������ԡ��γ�������ʦ�����Һš���˳���޸Ŀγ̱�");
                tip.setFont(new Font("����", Font.BOLD, 18));
                JLabel m_label = new JLabel("�γ̹���");
                JLabel delete_class_label = new JLabel("ɾ���γ�:");
                JButton delete_confirm = new JButton("ɾ��ȷ��");
                JLabel update_class_label = new JLabel("�޸Ŀγ�:");
                JButton update_confirm = new JButton("�޸�ȷ��");
                JLabel to = new JLabel("Ϊ");
                JTextField old_class = new JTextField();
                JTextField update_class = new JTextField();
                JTextField delete_class_text = new JTextField();
                delete_confirm.addActionListener(e -> {
                    if (model.deleteLesson(delete_class_text.getText())) {
                        JOptionPane.showMessageDialog(null, "��ɾ����", "��ʾ",
                                JOptionPane.INFORMATION_MESSAGE, new ImageIcon("img/info.jpg"));
                    } else {
                        JOptionPane.showMessageDialog(null, "δ�ҵ���Ҫɾ���Ŀγ̡�", "����",
                                JOptionPane.ERROR_MESSAGE, new ImageIcon("img/cancel.jpg"));
                    }
                });
                update_confirm.addActionListener(e -> {
                    if (model.updateLesson(old_class.getText(), update_class.getText())) {
                        JOptionPane.showMessageDialog(null, "�޸ĳɹ���", "��ʾ",
                                JOptionPane.INFORMATION_MESSAGE, new ImageIcon("img/info.jpg"));
                    } else {
                        JOptionPane.showMessageDialog(null, "δ�ҵ���Ҫ�޸ĵĿγ̡�", "����",
                                JOptionPane.ERROR_MESSAGE, new ImageIcon("img/cancel.jpg"));
                    }
                });

                FrameManager.addComponents(manage, m_label);
                FrameManager.addComponents(manageone, delete_class_label, delete_class_text, delete_confirm);
                FrameManager.addComponents(managetwo, update_class_label, old_class, to, update_class, update_confirm);
                FrameManager.addComponents(manage, manageone, managetwo, tip);

                add(manage, BorderLayout.NORTH);
            }
        }

        public void InitEditTableByRoom(String roomid) {
            editcon = new DBConnection();
            conn3 = editcon.getConnection();
            try {   //connect to database and fetch all course data
                Statement stmt = conn3.createStatement();
                String query = "SELECT * FROM course WHERE croom='" + roomid + "'";
                //SELECT * FROM course WHERE croom='Ҫ���ҵĽ��ұ��'
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    String cno = rs.getString("cno");
                    String cname = rs.getString("cname");
                    String croom = rs.getString("croom");
                    String c_week = rs.getString("cweek");
                    String c_time = rs.getString("ctime");
                    String startdate = rs.getString("startdate");
                    String findate = rs.getString("findate");
                    int[][] ctime = parseCtime(c_time.trim());
                    int[] cweek = parseCweek(c_week.trim());
                    Course c = new Course(cno.trim(), cname.trim(), croom.trim(), startdate.trim(), findate.trim(), cweek, ctime);
                    for (int row = 1; row < ctime.length; row++)
                        for (int col = 1; col < ctime[row].length; col++)
                            model.setValueAt(c.toString(), row - 1, col);//���γ̼���EditTable��
                }
                rs.close();
                stmt.close();
            } catch (SQLException sqlex) {
                JOptionPane.showMessageDialog(null, "�ڳ�ʼ����������ʱ������һЩ����");
            } finally {
                editcon.CloseDBConnection(conn3);
            }
        }

        public void InitEditTableByTeacher(String teacherid) {
            editcon = new DBConnection();
            conn3 = editcon.getConnection();
            try {   //connect to database and fetch all course data
                Statement stmt = conn3.createStatement();
                String query = "SELECT * FROM teacher,course,SC WHERE course.cno=SC.cno AND " +
                        "teacher.tno=SC.tno AND teacher.tno='" + teacherid + "'";
                //SELECT * FROM teacher,course,SC WHERE course.cno=SC.cno AND teacher.tno=SC.tno AND teacher.tno='Ҫ���ҵĽ�ʦ���'
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    String cno = rs.getString("course.cno");
                    String cname = rs.getString("course.cname");
                    String croom = rs.getString("course.croom");
                    String c_week = rs.getString("course.cweek");
                    String c_time = rs.getString("course.ctime");
                    String tname = rs.getString("teacher.tname");
                    String startdate = rs.getString("startdate");
                    String findate = rs.getString("findate");
                    int[][] ctime = parseCtime(c_time.trim());
                    int[] cweek = parseCweek(c_week.trim());
                    Course c = new Course(cno.trim(), cname.trim(), croom.trim(), startdate.trim(), findate.trim(), cweek, ctime);
                    for (int row = 1; row < ctime.length; row++)
                        for (int col = 1; col < ctime[row].length; col++)
                            model.setValueAt(c.toString() + tname, row - 1, col);//���γ̼���EditTable��
                }
                rs.close();
                stmt.close();
            } catch (SQLException sqlex) {
                JOptionPane.showMessageDialog(null, "�ڳ�ʼ����ʦ����ʱ������һЩ����");
            } finally {
                editcon.CloseDBConnection(conn3);
            }
        }

        public void InitEditTableByDate(String date, String mode) {
            editcon = new DBConnection();
            conn3 = editcon.getConnection();
            try {
                String query = "SELECT * FROM course WHERE ? BETWEEN startdate AND findate";
                PreparedStatement pstmt = conn3.prepareStatement(query);
                pstmt.setString(1, date);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    String cno = rs.getString("cno");
                    String cname = rs.getString("cname");
                    String croom = rs.getString("croom");
                    String c_week = rs.getString("cweek");
                    String c_time = rs.getString("ctime");
                    String startdate = rs.getString("startdate");
                    String findate = rs.getString("findate");
                    int[][] ctime = parseCtime(c_time.trim());
                    int[] cweek = parseCweek(c_week.trim());
                    Course c = new Course(cno.trim(), cname.trim(), croom.trim(), startdate.trim(), findate.trim(), cweek, ctime);
                    if (mode.equals("single_day")) {
                        int col = Fdate.convertToWeek(date); //��ȡ��ǰ����Ϊ������
                        for (int row : ctime[col])
                            model.setValueAt(c.toString(), row - 1, col);
                    } else {
                        for (int row = 1; row < ctime.length; row++)
                            for (int col = 1; col < ctime[row].length; col++)
                                model.setValueAt(c.toString(), row - 1, col);//���γ̼���EditTable��
                    }
                }
                rs.close();
                pstmt.close();
            } catch (SQLException sqlex) {
                JOptionPane.showMessageDialog(null, "�ڳ�ʼ�������������ʱ������һЩ����");
                System.out.println(sqlex.toString());
                System.out.println("SELECT * FROM course WHERE '" + date + "' BETWEEN startdate AND findate");
            } finally {
                editcon.CloseDBConnection(conn3);
            }
        }

        private void request_Start_Date_And_End_Date() {
            do {
                start = JOptionPane.showInputDialog(null, "������ÿγ̿�ʼ���ڣ�8λ���֣���20081209��",
                        "��Ϣ", JOptionPane.INFORMATION_MESSAGE);
                if (!start.matches("^\\\\d{8}$")) {
                    JOptionPane.showMessageDialog(null, "������8λ���֣�");
                    break;
                }
                end = JOptionPane.showInputDialog(null, "������ÿγ̽������ڣ�8λ���֣���20190104��",
                        "��Ϣ", JOptionPane.INFORMATION_MESSAGE);
                if (!end.matches("^\\\\d{8}$")) {
                    JOptionPane.showMessageDialog(null, "������8λ���֣�");
                    break;
                }
                if (Integer.parseInt(start) >= Integer.parseInt(end)) {
                    JOptionPane.showMessageDialog(null, "��ʼ���ڴ�����ֹ���ڣ�����������");
                }
            } while (!(Integer.parseInt(start) < Integer.parseInt(end)));
        }

        private void request_Start_Week_And_End_Week() {
            do {
                String input = JOptionPane.showInputDialog(null, "������γ���ʼ��������������Զ��ŷָ�",
                        "��Ϣ", JOptionPane.INFORMATION_MESSAGE);
                if (!input.matches("\\\\d{1,2}[,��]\\\\d{1,2}")) {
                    JOptionPane.showMessageDialog(null, "��ʽ��������������");
                    break;
                }
                String[] inputs = input.split("[,��]");
                startweek = Integer.parseInt(inputs[0]);
                endweek = Integer.parseInt(inputs[1]);
            } while (!(startweek < endweek));  //׼��cweek����
        }

        class MyTableModel extends AbstractTableModel {
            private String[] columnNames = {"����", "��һ", "�ܶ�", "����", "����", "����", "����", "����"};
            //��Ҫ���ݿ��ν������Բ�ѯdata���޸�data
            private Object[][] data = {
                    {"1", "", "", "", "", "", "", ""},
                    {"2", "", "", "", "", "", "", ""},
                    {"3", "", "", "", "", "", "", ""},
                    {"4", "", "", "", "", "", "", ""},
                    {"5", "", "", "", "", "", "", ""},
                    {"6", "", "", "", "", "", "", ""},
                    {"7", "", "", "", "", "", "", ""},
                    {"8", "", "", "", "", "", "", ""},
                    {"9", "", "", "", "", "", "", ""},
                    {"10", "", "", "", "", "", "", ""},
                    {"11", "", "", "", "", "", "", ""},
                    {"12", "", "", "", "", "", "", ""},
                    {"13", "", "", "", "", "", "", ""},
                    {"14", "", "", "", "", "", "", ""},
            };


            public int getColumnCount() {
                return columnNames.length;
            }

            public int getRowCount() {
                return data.length;
            }

            public String getColumnName(int col) {
                return columnNames[col];
            }

            public Object getValueAt(int row, int col) {
                return data[row][col];
            }

            /*
             * JTable uses this method to determine the default renderer/
             * editor for each cell.  If we didn't implement this method,
             * then the last column would contain text ("true"/"false"),
             * rather than a check box.
             */
            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }

            /*
             * Don't need to implement this method unless your table's
             * editable.
             */
            public boolean isCellEditable(int row, int col) {
                //Note that the data/cell address is constant,
                //no matter where the cell appears onscreen.
                if (col < 1 || !AUTHORIZATION) {
                    return false;
                } else {
                    return true;
                }
            }

            /*
             * Don't need to implement this method unless your table's
             * data can change.
             */
            //��Ҫ���ݿ��ν� ��ǰΪprint��������壻
            public void setValueAt(Object value, int row, int col) {
                boolean jumpout_flag = false;
                if (AUTHORIZATION) {
                    System.out.println("Setting value at " + row + "," + col
                            + " to " + value
                            + " (an instance of "
                            + value.getClass() + ")");
                }
                Object o = model.getValueAt(row, col);
                String[] origin_value;
                if (o != "") {
                    origin_value = o.toString().split("[,��]");

                    for (int i = 0; i < origin_value.length; i++) {
                        origin_value[i] = origin_value[i].trim();
                    }
                } else origin_value = new String[0];


                data[row][col] = value;
                String[] new_value = value.toString().split("[,��]"); //��Ҫ�޸ĵĲ���
                for (int i = 0; i < new_value.length && !jumpout_flag; i++) {
                    System.out.println(new_value[i]);
                    //���i=0,update���ݿ��еĿγ�����
                    //i=1,update���ݿ��еĽ�ʦ��i=2,update���ݿ��еĶ�Ӧ�γ̵Ľ��Һš�
                    if (new_value.length > 3) {
                        JOptionPane.showMessageDialog(null, "�ṩ�����������࣬�밴���Ϸ���ʾ�޸Ŀγ̣�", "����",
                                JOptionPane.ERROR_MESSAGE, new ImageIcon("img/info.jpg"));
                        jumpout_flag = true;
                        break;
                    }
                    switch (i) {     //��ԭ��Ԫ��Ϊ��ʱ��ֻ����������������������ӿγ���Ϣ
                        case 0:
                            if (o == "") {
                                System.out.println("Update course name at " + row + "," + col + " from " + "" + " to " + new_value[0].trim());
                                if (!AUTHORIZATION) JOptionPane.showMessageDialog(null, "����Ȩɾ���γ̣�����ϵϵͳ����Ա");
                                int choose = JOptionPane.showConfirmDialog(mainfunction.this, "ȷ��ɾ���γ̣�", "Confirm",
                                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                                if (choose == JOptionPane.OK_OPTION) updateDatabase(origin_value[0], "", "delete");
                            } else
                                System.out.println("Update course name at " + row + "," + col + " from " + origin_value[0] + " to " + new_value[0].trim());
                            if (new_value[0].trim().equals("") && !origin_value[0].equals("")) {
                                JOptionPane.showMessageDialog(null, "ʲô��û�䰡��");
                                jumpout_flag = true;
                                break;
                            } else if (o == "" || origin_value[0].equals(new_value[0].trim())) {
                                break;
                            } else
                                updateDatabase(origin_value[0].trim(), new_value[0].trim(), "course_name");
                            break;
                        case 1:
                            if (o == "")
                                System.out.println("Update teacher name at " + row + "," + col + " from " + "" + " to " + new_value[1].trim());
                            else
                                System.out.println("Update teacher name at" + row + ", " + col + " from " + origin_value[1] + " to " + new_value[1].trim());
                            if (new_value[1].trim().equals("")) {
                                JOptionPane.showMessageDialog(null, "ȱ�ٽ�ʦ���ͽ��Һš�");
                                jumpout_flag = true;
                                break;
                            } else if (o == "" || origin_value[1].equals(new_value[1].trim())) {
                                break;
                            } else
                                updateDatabase(origin_value[1].trim(), new_value[1].trim(), "teacher");
                            break;
                        case 2:
                            if (o == "")
                                System.out.println("Update room name at " + row + "," + col + " from " + "" + " to " + new_value[2].trim());
                            else
                                System.out.println("Update room name at" + row + ", " + col + " from " + origin_value[2] + " to " + new_value[2].trim());
                            if (new_value[2].trim().equals("")) {
                                JOptionPane.showMessageDialog(null, "ȱ�ٽ��Һš�");
                                jumpout_flag = true;
                                break;
                            } else if (o == "") {
                                insertDatabase(new_value, row, col);
                                break;
                            } else if (origin_value[2].equals(new_value[2].trim())) break;
                            else updateDatabase(origin_value[2].trim(), new_value[2].trim(), "room_num");
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "����ϵ���߽������", "����",
                                    JOptionPane.ERROR_MESSAGE, new ImageIcon("img/cancel.jpg"));
                            break;
                    }
                }

                fireTableCellUpdated(row, col);

                if (AUTHORIZATION) {
                    System.out.println("New value of data:");
                    printDebugData();
                }
            }

            private void printDebugData() {
                int numRows = getRowCount();
                int numCols = getColumnCount();

                for (int i = 0; i < numRows; i++) {
                    System.out.print("    row " + i + ":");
                    for (int j = 0; j < numCols; j++) {
                        System.out.print("  " + data[i][j]);
                    }
                    System.out.println();
                }
                System.out.println("--------------------------");
            }

            /*
             *Don't need to implement this method unless your table's
             * data can change.
             */
            private boolean deleteLesson(String lesson) {
                int numRows = getRowCount();
                int numCols = getColumnCount();
                boolean find = false;

                for (int i = 0; i < numRows; i++) {
                    for (int j = 0; j < numCols; j++) {
                        if (data[i][j].equals(lesson)) {
                            find = true;
                            setValueAt("", i, j);
                            fireTableCellUpdated(i, j);
                        }
                    }
                }

                return find;
            }

            private boolean updateLesson(String oldLesson, String updateLesson) {
                int numRows = getRowCount();
                int numCols = getColumnCount();
                boolean find = false;

                for (int i = 0; i < numRows; i++) {
                    for (int j = 0; j < numCols; j++) {
                        if (data[i][j].equals(oldLesson)) {
                            find = true;
                            setValueAt(updateLesson, i, j);
                            fireTableCellUpdated(i, j);
                        }
                    }
                }

                return find;
            }

            private void updateDatabase(String oldvalue, String newvalue, String choose) {
                DBConnection updatedb = new DBConnection();
                conn2 = updatedb.getConnection();
                if (choose.equals("course_name")) {
                    try {
                        conn2.setAutoCommit(false);
                        String updatecn = "UPDATE course SET cname= ? WHERE cname= ?";
                        PreparedStatement pstmt = conn2.prepareStatement(updatecn);
                        pstmt.setString(1, newvalue);
                        pstmt.setString(2, oldvalue);
                        pstmt.executeUpdate();
                        conn2.commit();
                        pstmt.close();
                    } catch (SQLException sqlex) {
                        System.out.println(sqlex.toString());
                        System.out.println("UPDATE course SET cname= '" + newvalue + "' WHERE cname= '" + oldvalue + "'");
                    } finally {
                        updatedb.CloseDBConnection(conn2);
                    }
                } else if (choose.equals("teacher")) {
                    try {
                        conn2.setAutoCommit(false);
                        String updatetea = "UPDATE SC SET tno=(SELECT tno FROM teacher WHERE tname= ?) WHERE tno =( " +
                                "SELECT tno FROM teacher WHERE tname= ?)";
                        PreparedStatement pstmt = conn2.prepareStatement(updatetea);
                        pstmt.setString(1, newvalue);
                        pstmt.setString(2, oldvalue);
                        int c = pstmt.executeUpdate();
                        if (c != 0)
                            conn2.commit();
                        pstmt.close();
                    } catch (SQLException sqlex) {
                        System.out.println(sqlex.toString());
                        System.out.println("update SC set tno=(select tno from teacher where tname='" + newvalue + "') where tno =" +
                                "( select tno from teacher where tname='" + oldvalue + "')");
                    } finally {
                        updatedb.CloseDBConnection(conn2);
                    }
                } else if (choose.equals("room_num")) {
                    try {
                        conn2.setAutoCommit(false);
                        String updaternum = "UPDATE course SET croom= ? WHERE croom= ?";
                        PreparedStatement pstmt = conn2.prepareStatement(updaternum);
                        pstmt.setString(1, newvalue);
                        pstmt.setString(2, oldvalue);
                        int c = pstmt.executeUpdate();
                        if (c != 0) conn2.commit();
                        pstmt.close();
                    } catch (SQLException sqlex) {
                        System.out.println(sqlex.toString());
                        System.out.println("UPDATE course SET croom= '" + newvalue + "' WHERE croom= '" + oldvalue + "'");
                    } finally {
                        updatedb.CloseDBConnection(conn2);
                    }
                } else {
                    System.out.println("You are not supposed to see this.");
                }
            }

            private void insertDatabase(String[] newvalue, int row, int col) {  //insert course
                start = ""; //��ʼ���ڣ��û�׼������
                end = ""; //��ֹ���ڣ��û�׼������
                startweek = 0;
                endweek = 0;

                if (type.equals("S")) {    //��ѧ����ݣ���������һ�����ݿⲻ���ڵĽ�ʦ���֣������ʧ��
                    if (searchTno(newvalue[1])) {
                        sno = UID;
                        request_Start_Date_And_End_Date();
                        request_Start_Week_And_End_Week();

                        int[] cweek = generateCweekArrayByChoice(startweek, endweek);

                        String cweekString = generateCweekString(cweek);

                        String cno = String.valueOf(new Random().nextInt(1000000000));  //todo:�ֶ���ӿγ̺ţ�����Ա��

                        Course c = new Course(cno, newvalue[0].trim(), newvalue[2].trim(), start, end);

                        DBConnection insertdb=new DBConnection();
                        conn3=insertdb.getConnection();
                        try {
                            conn3.setAutoCommit(false);
                            String insertcn = "INSERT INTO course (inner_cno,cno,cname,croom,cweek,ctime,startdate,findate)VALUES(?,?,?,?,?,?,?,?)";
                            //INSERT INTO course VALUES(inner_cno,cno,cname,croom,cweek,ctime,startdate,findate)
                            String insertSC = "INSERT INTO SC VALUES(?,?,?)";
                            //INSERT INTO SC VALUES(sno,cno,tno)
                            PreparedStatement pstmt = conn3.prepareStatement(insertcn);
                            pstmt.setString(1, c.getCno() + "_" + id);
                            id++;
                            pstmt.setString(2, c.getCno());
                            pstmt.setString(3, c.getCname());
                            pstmt.setString(4, c.getCroom());
                            pstmt.setString(5, cweekString);
                            pstmt.setString(6, getColumnName(col) + "," + row);
                            pstmt.setString(7, c.getStartdate());
                            pstmt.setString(8, c.getFindate());

                            pstmt.addBatch();

                            PreparedStatement pstmt2 = conn3.prepareStatement(insertSC);
                            pstmt2.setString(1, sno);
                            pstmt2.setString(2, c.getCno());
                            pstmt2.setString(3, tno);
                            pstmt2.addBatch();

                            pstmt.executeBatch();
                            pstmt2.executeBatch();

                            conn3.commit();
                            pstmt.close();
                            pstmt2.close();
                        } catch (SQLException sqlex) {
                            id--;
                            System.out.println(sqlex.toString());
                            System.out.println("INSERT INTO course VALUES(" + c.getCno() + "," + c.getCname() + "," + c.getCroom() + "," +
                                    cweekString + "," + getColumnName(col) + ',' + row + "," + c.getStartdate() + "," + c.getFindate() + ")");
                        } finally {
                            insertdb.CloseDBConnection(conn3);
                        }
                    } else
                        JOptionPane.showMessageDialog(null, "��Ӧ��ʦ����ʦ�Ų����ڣ�����������");
                } else {  //�ǽ�ʦ��ݣ�ֱ�Ӹ���course��SC�����µ�ԭ���ǻ�û��ѡ��
                    tno = UID;
                    String cno = String.valueOf(new Random().nextInt(1000000000));
                    request_Start_Date_And_End_Date();
                    request_Start_Week_And_End_Week();

                    int[] cweek = generateCweekArrayByChoice(startweek, endweek);

                    String cweekString = generateCweekString(cweek);

                    Course c = new Course(cno, newvalue[0].trim(), newvalue[2].trim(), start, end);

                    DBConnection insertdb=new DBConnection();
                    conn3=insertdb.getConnection();
                    try {
                        conn3.setAutoCommit(false);
                        String insertcn = "INSERT INTO course (inner_cno,cno,cname,croom,cweek,ctime,startdate,findate) VALUES(?,?,?,?,?,?,?,?)";
                        //INSERT INTO course VALUES(inner_cno,cno,cname,croom,cweek,ctime,startdate,findate)

                        PreparedStatement pstmt = conn3.prepareStatement(insertcn);
                        pstmt.setString(1, c.getCno() + "_" + id);
                        id++;
                        pstmt.setString(2, c.getCno());
                        pstmt.setString(3, c.getCname());
                        pstmt.setString(4, c.getCroom());
                        pstmt.setString(5, cweekString);
                        pstmt.setString(6, getColumnName(col) + ',' + row);
                        pstmt.setString(7, c.getStartdate());
                        pstmt.setString(8, c.getFindate());
                        pstmt.executeUpdate();

                        conn3.commit();
                        pstmt.close();
                    } catch (SQLException sqlex) {
                        id--;
                        System.out.println(sqlex.toString());
                        System.out.println("INSERT INTO course VALUES(" + c.getCno() + "," + c.getCname() + "," + c.getCroom() + "," +
                                cweekString + "," + getColumnName(col) + ',' + row + "," + c.getStartdate() + "," + c.getFindate() + ")");
                    } finally {
                        insertdb.CloseDBConnection(conn3);
                    }
                }
            }


            private int[] generateCweekArrayByChoice(int start, int end) {
                boolean isFullweek = true;
                boolean isOddweek = false;
                boolean isEvenweek = false;
                int[] generatedCweek = new int[26];
                int chooseParseWeekOption = JOptionPane.showInternalOptionDialog(null, "����ѡ����ѡ��" +
                                "�γ̽��е�ģʽ��ȫ��/����/˫�ܣ�", "ģʽѡ��", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, new ImageIcon("img/info.jpg"),
                        new String[]{"ȫ��", "����", "˫��"}, "ȫ��"); //todo:����ѡ����ģʽ
                if (chooseParseWeekOption != JOptionPane.CLOSED_OPTION) {
                    switch (chooseParseWeekOption) {
                        case 0:
                            isFullweek = true;
                            break;
                        case 1:
                            isOddweek = true;
                            break;
                        case 2:
                            isEvenweek = true;
                            break;
                        default:
                            break;
                    }
                }
                for (int i = start; i <= end; i++) {
                    if (isOddweek) {
                        if (i % 2 != 0)
                            generatedCweek[i] = 1;
                        else continue;
                    }
                    if (isEvenweek) {
                        if (i % 2 == 0)
                            generatedCweek[i] = 1;
                        else continue;
                    }
                    if (isFullweek)
                        generatedCweek[i] = 1;
                }
                return generatedCweek;
            }

            private String generateCweekString(int[] CweekArray) {
                String goal = "";
                for (int iter = 1; iter < CweekArray.length; iter++) {
                    if (CweekArray[iter] == 1) {
                        if (iter == CweekArray.length - 1)
                            goal += iter;
                        else goal += iter + ",";
                    } else continue;
                }
                return goal;
                //todo:��CweekStringΪstart,start+1,...,end-1,endʱ�����Ϊ"start-end"
            }

            private boolean searchTno(String _tno) {
                DBConnection st = new DBConnection();
                conn2 = st.getConnection();
                boolean flag = false;
                try {
                    String query = "SELECT * FROM teacher where tno= '" + _tno + "'";
                    Statement stmt = conn2.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        flag = true;
                        tno = rs.getString("tno").trim();
                        rs.close();
                    }
                    stmt.close();
                } catch (SQLException sqlex) {
                    System.out.println(sqlex.toString());
                    System.out.println("SELECT * FROM teacher where tno= '" + _tno + "'");
                } finally {
                    st.CloseDBConnection(conn2);
                }
                return flag;
            }

        }

    }

}



