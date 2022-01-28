package com.ren.CourseManager;

import com.ren.CourseManager.utils.DBConnection;
import com.ren.CourseManager.utils.FrameManager;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

//给出username和password
public class register extends JFrame {

    //用户和密码（用户输入框）
    private String username;
    private String password;
    private String repassword;
    private String name;
    private String dept;

    private boolean logged_in = false;
    private DBConnection newusr;
    private Connection conn;

    //构造函数
    public register() {
        super("课程查看与管理-用户注册"); //设置标题
        FrameManager.FrameSetup(this, 350, 350, null, false);
        init();   //执行初始化函数（将用户名密码等组件加入到面板中）
        setVisible(true);
    }

    public void init() {
        FrameManager.setWindowLoc(this, FrameManager.FRAME_MANAGER_REGISTER_WINDOW);

        // 声明姓名，密码的标签
        JLabel username_label = new JLabel("学号/教职工号");
        JLabel name_label = new JLabel("姓名");
        JLabel password_label = new JLabel("密码");
        JLabel repassword_label = new JLabel("重复密码");
        JLabel choose_occupation = new JLabel("您是：");
        //声明教师或学生的单选框
        JRadioButton teacher = new JRadioButton("教师");
        JRadioButton student = new JRadioButton("学生");
        ButtonGroup bg = new ButtonGroup();
        bg.add(teacher);
        bg.add(student);
        // 声明姓名输入框和密码输入框
        JLabel dept_label = new JLabel("专业（学生）/部门（教师）");
        final JTextField user_field = new JTextField();
        final JTextField name_field = new JTextField();
        final JPasswordField password_field = new JPasswordField();
        final JPasswordField repassword_field = new JPasswordField();
        final JTextField dept_field = new JTextField();
        //声明登录按钮
        JButton confirm_btn = new JButton("确认", new ImageIcon("img/reg_ok.jpg"));
        //声明注册按钮
        JButton cancel_btn = new JButton("取消", new ImageIcon("img/reg_cancel.jpg"));

        //设置各个标签和输入框的大小和位置
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

        //确认按钮的监听器
        ActionListener l = e -> {
            //如果密码相同，那么弹出对话框显示注册成功
            password = new String(password_field.getPassword());
            repassword = new String(repassword_field.getPassword());
            username = user_field.getText();
            name = name_field.getText();
            dept = dept_field.getText();
            if (FrameManager.check_field_is_filled(password_field, repassword_field, user_field, name_field, dept_field)
                    && password.equals(repassword) && (teacher.isSelected() || student.isSelected())) {
                if (user_already_exists(username)) {
                    JOptionPane.showMessageDialog(null, "该账号已被注册！", "验证错误",
                            JOptionPane.ERROR_MESSAGE, new ImageIcon("img/info.jpg"));
                } else if (username.contains(" ") || name.contains(" ")) {
                    JOptionPane.showMessageDialog(null, "请不要在用户名和学号中输入空格！",
                            "验证错误", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("img/info.jpg"));
                } else {
                    if (student.isSelected()) DealWithStudent();
                    else DealWithTeacher();
                    if (logged_in) {
                        JOptionPane.showMessageDialog(null, "注册成功", "验证成功",
                                JOptionPane.INFORMATION_MESSAGE, new ImageIcon("img/ok.jpg"));
                        setVisible(false);
                    }
                }
            } else {                                                   //反之，注册不成功，重新注册
                JOptionPane.showMessageDialog(null, "请确认所有内容均已填写，且重复密码输入正确",
                        "验证错误",
                        JOptionPane.ERROR_MESSAGE, new ImageIcon("img/cancel.jpg"));
            }
            //设置输入框的内容为空，让用户重新输入
            FrameManager.clearField(user_field, name_field, password_field, repassword_field, dept_field);
            bg.clearSelection();
        };
        confirm_btn.addActionListener(l);
        confirm_btn.registerKeyboardAction(l, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        //取消按钮的监听器
        cancel_btn.addActionListener(event -> {
            FrameManager.clearField(user_field, password_field, password_field, repassword_field, dept_field);
            bg.clearSelection();
            setVisible(false);
        });
        //重复密码输入框的回车监听器
        repassword_field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    //如果密码相同，那么弹出对话框显示注册成功
                    password = new String(password_field.getPassword());
                    repassword = new String(repassword_field.getPassword());
                    username = user_field.getText();
                    name = name_field.getText();
                    dept = dept_field.getText();
                    if (FrameManager.check_field_is_filled(password_field,repassword_field,user_field,name_field,dept_field)
                            &&password.equals(repassword)&& (teacher.isSelected() || student.isSelected())) {
                        if (user_already_exists(username)) {
                            JOptionPane.showMessageDialog(null, "该账号已被注册！", "验证错误",
                                    JOptionPane.ERROR_MESSAGE, new ImageIcon("img/info.jpg"));
                        } else if (username.contains(" ") || name.contains(" ")) {
                            JOptionPane.showMessageDialog(null, "请不要在用户名和学号中输入空格！",
                                    "验证错误", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("img/info.jpg"));
                        } else {
                            if (student.isSelected()) DealWithStudent();
                            else DealWithTeacher();
                            if (logged_in) {
                                JOptionPane.showMessageDialog(null, "注册成功", "验证成功",
                                        JOptionPane.INFORMATION_MESSAGE, new ImageIcon("img/ok.jpg"));
                                setVisible(false);
                            }
                        }
                    } else {                                                   //反之，注册不成功，重新注册
                        JOptionPane.showMessageDialog(null, "请确认所有内容均已填写，且重复密码输入正确",
                                "验证错误",
                                JOptionPane.ERROR_MESSAGE, new ImageIcon("img/cancel.jpg"));
                    }
                    //设置输入框的内容为空，让用户重新输入
                    FrameManager.clearField(user_field, name_field, password_field, repassword_field, dept_field);
                    bg.clearSelection();
                }
            }
        });
    }

    private boolean user_already_exists(String usrname) {  //检测已有用户名
        boolean flag = false;  //默认没找到
        DBConnection checkusr = new DBConnection();
        Connection conn1 = checkusr.getConnection();
        try {
            Statement stmt = conn1.createStatement();
            String query = "SELECT * FROM users WHERE no='" + usrname + "'"; //SELECT * FROM users WHERE no='要找的用户名'
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {//找到了记录，就说明数据库中已存在此用户
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
            newusr = new DBConnection();//连接用户库，添加新用户
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
            JOptionPane.showMessageDialog(null, "Cannot add current user to database,try again", "请重试",
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
            newusr = new DBConnection();//连接用户库，添加新用户
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
            JOptionPane.showMessageDialog(null, "Cannot add current user to database,try again", "请重试",
                    JOptionPane.INFORMATION_MESSAGE, new ImageIcon("img/info.jpg"));
            System.out.println(sqlex.toString());
            System.out.println("INSERT INTO users VALUES ('" + username + "','" + name + "','" + password + "','" + new Timestamp((new java.util.Date()).getTime()) + "','" + type + "')");
            System.out.println("INSERT INTO teacher VALUES('" + username + "','" + name + "','" + dept + "')");
        } finally {
            newusr.CloseDBConnection(conn);
        }
    }

}
