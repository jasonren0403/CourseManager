package com.ren.CourseManager;

import com.ren.CourseManager.utils.DBConnection;
import com.ren.CourseManager.utils.FrameManager;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

//����username��password
public class register extends JFrame {

    //�û������루�û������
    private String username;
    private String password;
    private String repassword;
    private String name;
    private String dept;

    private boolean logged_in = false;
    private DBConnection newusr;
    private Connection conn;

    //���캯��
    public register() {
        super("�γ̲鿴�����-�û�ע��"); //���ñ���
        FrameManager.FrameSetup(this, 350, 350, null, false);
        init();   //ִ�г�ʼ�����������û��������������뵽����У�
        setVisible(true);
    }

    public void init() {
        FrameManager.setWindowLoc(this, FrameManager.FRAME_MANAGER_REGISTER_WINDOW);

        // ��������������ı�ǩ
        JLabel username_label = new JLabel("ѧ��/��ְ����");
        JLabel name_label = new JLabel("����");
        JLabel password_label = new JLabel("����");
        JLabel repassword_label = new JLabel("�ظ�����");
        JLabel choose_occupation = new JLabel("���ǣ�");
        //������ʦ��ѧ���ĵ�ѡ��
        JRadioButton teacher = new JRadioButton("��ʦ");
        JRadioButton student = new JRadioButton("ѧ��");
        ButtonGroup bg = new ButtonGroup();
        bg.add(teacher);
        bg.add(student);
        // �����������������������
        JLabel dept_label = new JLabel("רҵ��ѧ����/���ţ���ʦ��");
        final JTextField user_field = new JTextField();
        final JTextField name_field = new JTextField();
        final JPasswordField password_field = new JPasswordField();
        final JPasswordField repassword_field = new JPasswordField();
        final JTextField dept_field = new JTextField();
        //������¼��ť
        JButton confirm_btn = new JButton("ȷ��", new ImageIcon("img/reg_ok.jpg"));
        //����ע�ᰴť
        JButton cancel_btn = new JButton("ȡ��", new ImageIcon("img/reg_cancel.jpg"));

        //���ø�����ǩ�������Ĵ�С��λ��
        username_label.setBounds(10, 10, 150, 30);
        name_label.setBounds(10, 60, 140, 30);
        password_label.setBounds(10, 110, 150, 30);
        repassword_label.setBounds(10, 160, 150, 30);
        choose_occupation.setBounds(10, 200, 150, 30);
        dept_label.setBounds(10, 230, 180, 30);

        user_field.setBounds(100, 10, 200, 30);
        name_field.setBounds(100, 60, 200, 30);
        password_field.setBounds(100, 110, 200, 30);
        repassword_field.setBounds(100, 160, 200, 30);
        teacher.setBounds(100, 200, 80, 30);
        student.setBounds(180, 200, 80, 30);
        dept_field.setBounds(200, 230, 100, 30);

        confirm_btn.setBounds(40, 270, 80, 30);
        cancel_btn.setBounds(160, 270, 80, 30);

        FrameManager.addComponents(this, username_label, name_label, password_label, repassword_label, dept_label,
                user_field, name_field, password_field, repassword_field, dept_field, confirm_btn, cancel_btn, choose_occupation,
                teacher, student);

        //ȷ�ϰ�ť�ļ�����
        ActionListener l = e -> {
            //���������ͬ����ô�����Ի�����ʾע��ɹ�
            password = new String(password_field.getPassword());
            repassword = new String(repassword_field.getPassword());
            username = user_field.getText();
            name = name_field.getText();
            dept = dept_field.getText();
            if (FrameManager.check_field_is_filled(password_field, repassword_field, user_field, name_field, dept_field)
                    && password.equals(repassword) && (teacher.isSelected() || student.isSelected())) {
                if (user_already_exists(username)) {
                    JOptionPane.showMessageDialog(null, "���˺��ѱ�ע�ᣡ", "��֤����",
                            JOptionPane.ERROR_MESSAGE, new ImageIcon("img/info.jpg"));
                } else if (username.contains(" ") || name.contains(" ")) {
                    JOptionPane.showMessageDialog(null, "�벻Ҫ���û�����ѧ��������ո�",
                            "��֤����", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("img/info.jpg"));
                } else {
                    if (student.isSelected()) DealWithStudent();
                    else DealWithTeacher();
                    if (logged_in) {
                        JOptionPane.showMessageDialog(null, "ע��ɹ�", "��֤�ɹ�",
                                JOptionPane.INFORMATION_MESSAGE, new ImageIcon("img/ok.jpg"));
                        setVisible(false);
                    }
                }
            } else {                                                   //��֮��ע�᲻�ɹ�������ע��
                JOptionPane.showMessageDialog(null, "��ȷ���������ݾ�����д�����ظ�����������ȷ",
                        "��֤����",
                        JOptionPane.ERROR_MESSAGE, new ImageIcon("img/cancel.jpg"));
            }
            //��������������Ϊ�գ����û���������
            FrameManager.clearField(user_field, name_field, password_field, repassword_field, dept_field);
            bg.clearSelection();
        };
        confirm_btn.addActionListener(l);
        confirm_btn.registerKeyboardAction(l, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        //ȡ����ť�ļ�����
        cancel_btn.addActionListener(event -> {
            FrameManager.clearField(user_field, password_field, password_field, repassword_field, dept_field);
            bg.clearSelection();
            setVisible(false);
        });
        //�ظ����������Ļس�������
        repassword_field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    //���������ͬ����ô�����Ի�����ʾע��ɹ�
                    password = new String(password_field.getPassword());
                    repassword = new String(repassword_field.getPassword());
                    username = user_field.getText();
                    name = name_field.getText();
                    dept = dept_field.getText();
                    if (FrameManager.check_field_is_filled(password_field,repassword_field,user_field,name_field,dept_field)
                            &&password.equals(repassword)&& (teacher.isSelected() || student.isSelected())) {
                        if (user_already_exists(username)) {
                            JOptionPane.showMessageDialog(null, "���˺��ѱ�ע�ᣡ", "��֤����",
                                    JOptionPane.ERROR_MESSAGE, new ImageIcon("img/info.jpg"));
                        } else if (username.contains(" ") || name.contains(" ")) {
                            JOptionPane.showMessageDialog(null, "�벻Ҫ���û�����ѧ��������ո�",
                                    "��֤����", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("img/info.jpg"));
                        } else {
                            if (student.isSelected()) DealWithStudent();
                            else DealWithTeacher();
                            if (logged_in) {
                                JOptionPane.showMessageDialog(null, "ע��ɹ�", "��֤�ɹ�",
                                        JOptionPane.INFORMATION_MESSAGE, new ImageIcon("img/ok.jpg"));
                                setVisible(false);
                            }
                        }
                    } else {                                                   //��֮��ע�᲻�ɹ�������ע��
                        JOptionPane.showMessageDialog(null, "��ȷ���������ݾ�����д�����ظ�����������ȷ",
                                "��֤����",
                                JOptionPane.ERROR_MESSAGE, new ImageIcon("img/cancel.jpg"));
                    }
                    //��������������Ϊ�գ����û���������
                    FrameManager.clearField(user_field, name_field, password_field, repassword_field, dept_field);
                    bg.clearSelection();
                }
            }
        });
    }

    private boolean user_already_exists(String usrname) {  //��������û���
        boolean flag = false;  //Ĭ��û�ҵ�
        DBConnection checkusr = new DBConnection();
        Connection conn1 = checkusr.getConnection();
        try {
            Statement stmt = conn1.createStatement();
            String query = "SELECT * FROM users WHERE no='" + usrname + "'"; //SELECT * FROM users WHERE no='Ҫ�ҵ��û���'
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {//�ҵ��˼�¼����˵�����ݿ����Ѵ��ڴ��û�
                rs.close();
                flag = true;
            }
            stmt.close();
        } catch (SQLException sqlex) {

        } finally {
            checkusr.CloseDBConnection(conn1);
        }
        return flag;
    }

    private void DealWithStudent() {
        String updateusr = "INSERT INTO users VALUES (?,?,?,?,?)";
        //INSERT INTO users VALUES('no','name','password',register_date,'type')
        String updatestu = "INSERT INTO student VALUES (?,?,?)";
        //INSERT INTO student VALUES('sno','sname','sdept')
        String type = "S";
        try {
            newusr = new DBConnection();//�����û��⣬������û�
            conn = newusr.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement usr = conn.prepareStatement(updateusr);
            usr.setString(1, username.trim());
            usr.setString(2, name.trim());
            usr.setString(3, password);
            usr.setTimestamp(4, new Timestamp((new java.util.Date()).getTime()));
            usr.setString(5, type);
            usr.addBatch();

            PreparedStatement stu = conn.prepareStatement(updatestu);
            stu.setString(1, username.trim());
            stu.setString(2, name.trim());
            stu.setString(3, dept.trim());
            stu.addBatch();

            usr.executeBatch();
            stu.executeBatch();

            conn.commit();

            logged_in = true;
            stu.close();
            usr.close();
        } catch (SQLException sqlex) {
            logged_in = false;
            JOptionPane.showMessageDialog(null, "Cannot add current user to database,try again", "������",
                    JOptionPane.INFORMATION_MESSAGE, new ImageIcon("img/info.jpg"));
            System.out.println(sqlex.toString());
            System.out.println("INSERT INTO users VALUES('" + username + "','" + name + "','" + password + "','" + new Timestamp((new java.util.Date()).getTime()) + "','" + type + "')");
            System.out.println("INSERT INTO student VALUES('" + username + "','" + name + "','" + dept + "')");
        } finally {
            newusr.CloseDBConnection(conn);
        }
    }

    private void DealWithTeacher() {
        String updateusr = "INSERT INTO users VALUES (?,?,?,?,?)";
        //INSERT INTO users VALUES('no','name','password',register_date,'type')
        String updatetea = "INSERT INTO teacher VALUES (?,?,?)";
        //INSERT INTO teacher VALUES('tno','tname','tdept')
        String type = "T";
        try {
            newusr = new DBConnection();//�����û��⣬������û�
            conn = newusr.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement usr = conn.prepareStatement(updateusr);
            usr.setString(1, username.trim());
            usr.setString(2, name.trim());
            usr.setString(3, password);
            usr.setTimestamp(4, new Timestamp((new java.util.Date()).getTime()));
            usr.setString(5, type);
            usr.addBatch();

            PreparedStatement tea = conn.prepareStatement(updatetea);
            tea.setString(1, username.trim());
            tea.setString(2, name.trim());
            tea.setString(3, dept.trim());
            tea.addBatch();


            usr.executeBatch();
            tea.executeBatch();
            conn.commit();

            logged_in = true;
            tea.close();
            usr.close();
        } catch (SQLException sqlex) {
            logged_in = false;
            JOptionPane.showMessageDialog(null, "Cannot add current user to database,try again", "������",
                    JOptionPane.INFORMATION_MESSAGE, new ImageIcon("img/info.jpg"));
            System.out.println(sqlex.toString());
            System.out.println("INSERT INTO users VALUES ('" + username + "','" + name + "','" + password + "','" + new Timestamp((new java.util.Date()).getTime()) + "','" + type + "')");
            System.out.println("INSERT INTO teacher VALUES('" + username + "','" + name + "','" + dept + "')");
        } finally {
            newusr.CloseDBConnection(conn);
        }
    }

}
