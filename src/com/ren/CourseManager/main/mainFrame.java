package com.ren.CourseManager.main;

import com.ren.CourseManager.register;
import com.ren.CourseManager.utils.DBConnection;
import com.ren.CourseManager.utils.FrameManager;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class mainFrame extends JFrame {
    //登录的用户名和密码
    private String userName;
    private String password;

    private DBConnection loginusr = null;
    private Connection conn = null;

    //构造函数
    public mainFrame() {
        super("课程查看与管理-系统登录"); //设置标题
        FrameManager.FrameSetup(this, 320, 200, null, true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //设置能关闭窗口
        init();   //执行初始化函数（将用户名密码等组件加入到面板中）
        this.setVisible(true);
    }

    public void init() {
        FrameManager.setWindowLoc(this, FrameManager.FRAME_MANAGER_LOGIN_WINDOW);

        // 声明姓名，密码的标签
        JLabel username_label = new JLabel("学号/教职工号");
        JLabel password_label = new JLabel("密码");
        // 声明姓名输入框和密码输入框
        final JTextField user_field = new JTextField();
        final JPasswordField password_field = new JPasswordField();
        //声明登录按钮
        JButton login_btn = new JButton("登录");
        //声明注册按钮
        JButton register_btn = new JButton("注册");
        //声明关于信息
        JLabel info = new JLabel("关于");

        //设置各个标签和输入框的大小和位置
        username_label.setBounds(10, 20, 150, 30);
        password_label.setBounds(10, 70, 150, 30);
        user_field.setBounds(100, 20, 200, 30);
        password_field.setBounds(100, 70, 200, 30);
        login_btn.setBounds(40, 120, 80, 30);
        register_btn.setBounds(160, 120, 80, 30);
        info.setBounds(275, 140, 40, 20);

        FrameManager.addComponents(this, username_label, password_label, user_field, password_field, login_btn, register_btn, info);

        //登录按钮的监听器
        ActionListener l = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //如果数据库中存在此用户名和密码，登陆成功，并且开启另一个主框架（功能页）
                //连接用户库
                userName = user_field.getText();
                password = new String(password_field.getPassword());
                if (user_is_valid(userName, password)) {
                    DBConnection nameRequest = new DBConnection();
                    conn = nameRequest.getConnection();
                    String nameQuery = "SELECT * FROM users WHERE no='" + userName + "'";
                    try {
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(nameQuery);
                        if (rs.next()) {
                            new mainfunction(userName, rs.getString("name").trim(), rs.getString("type").trim());
                            rs.close();
                        }
                        stmt.close();
                    } catch (SQLException e1) {
                        System.out.println("SELECT * FROM users WHERE no='" + userName + "'");
                        e1.printStackTrace();
                    } finally {
                        nameRequest.CloseDBConnection(conn);
                        setVisible(false);
                    }
                } else//反之，登录不成功，重新登录
                {
                    //设置输入框的内容为空，让用户重新输入
                    FrameManager.clearField(user_field, password_field);
                }
            }
        };
        login_btn.addActionListener(l);
        login_btn.registerKeyboardAction(l, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        //注册按钮的监听器
        register_btn.addActionListener(event -> {
            new register();
        });
        //关于按钮的监听器
        info.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String version = "Version 2018.12\n" + "***，***，***小组制作\n" + "****** **********班\n";
                JOptionPane.showMessageDialog(null, version, "关于",
                        JOptionPane.PLAIN_MESSAGE, new ImageIcon("img/info.jpg"));
            }
        });
        //密码框的回车登录监视器
        password_field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    //如果数据库中存在此用户名和密码，登陆成功，并且开启另一个主框架（功能页）
                    userName = user_field.getText();
                    password = new String(password_field.getPassword());
                    if (user_is_valid(userName, password)) {
                        DBConnection nameRequest = new DBConnection();
                        conn = nameRequest.getConnection();
                        String nameQuery = "SELECT * FROM users WHERE no='" + userName + "'";
                        try {
                            Statement stmt = conn.createStatement();
                            ResultSet rs = stmt.executeQuery(nameQuery);
                            if (rs.next()) {
                                new mainfunction(userName, rs.getString("name").trim(), rs.getString("type").trim());
                                rs.close();
                            }
                            stmt.close();
                        } catch (SQLException e1) {
                            System.out.println("SELECT * FROM users WHERE no='" + userName + "'");
                            e1.printStackTrace();
                        } finally {
                            nameRequest.CloseDBConnection(conn);
                            setVisible(false);
                        }
                    } else//反之，登录不成功，重新登录
                    {
                        //设置输入框的内容为空，让用户重新输入
                        FrameManager.clearField(user_field, password_field);
                    }
                }
            }
        });
    }

    private boolean user_is_valid(String usrName, String passWord) {  //检测登录过程合法性
        boolean flag = true; // 默认合法
        if (usrName.isEmpty() || passWord.isEmpty()) {
            JOptionPane.showMessageDialog(null, "请输入用户名和密码！",
                    "提示", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("img/info.jpg"));
            return false;
        }
        if (judge(usrName) || judge(passWord)) {   //若包含非法字符，直接退出
            JOptionPane.showMessageDialog(null, "请勿输入非法字符！",
                    "提示", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("img/info.jpg"));
            return false;
        } else {
            loginusr = new DBConnection();
            conn = loginusr.getConnection();
            try {
                String loginQuery = "SELECT * FROM users WHERE no='" + trim(usrName) + "' AND password='" + trim(passWord) + "'";
                //SELECT * FROM users WHERE no='登录名' AND password='登录密码'
                //登录名和密码做过处理，防止注入
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(loginQuery);
                if (!rs.next()) flag = false;  //没有找到，就说明用户名或密码至少有一个输错
                else rs.close();
                stmt.close();
            } catch (SQLException sqlex) {
                JOptionPane.showMessageDialog(null, "SQL 运行时出了一些小状况，请稍候再试。",
                        "提示", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("img/info.jpg"));
            } finally {
                if (!flag) {
                    JOptionPane.showMessageDialog(null, "您的账号或密码错误，请重新输入", "验证错误",
                            JOptionPane.ERROR_MESSAGE, new ImageIcon("img/cancel.jpg"));
                }
                password = "";        //提高安全性，密码在检查完毕后置空
                loginusr.CloseDBConnection(conn);
            }
        }
        return flag;
    }

    private boolean judge(String source) {
        if (source == null) return false;
        String regEx = "^[!$^&*+=|{}';'\\\",<>/?~！#￥%……&*——|{}【】‘；：”“'。，、？]|\\n|\\r|\\t+$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(source);
        return m.find();   //判断给定字符串中是否有特殊字符，有则返回true
    }

    private String trim(String source) {
        return source.replaceAll("^0-9A-Za-z", ""); //所有非数字和字母字符均会被替换为空字符
    }

    private String hash(String ori_pass) {
        //todo: return hashed password
        return null;
    }
}

