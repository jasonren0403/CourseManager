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
    //��¼���û���������
    private String userName;
    private String password;

    private DBConnection loginusr = null;
    private Connection conn = null;

    //���캯��
    public mainFrame() {
        super("�γ̲鿴�����-ϵͳ��¼"); //���ñ���
        FrameManager.FrameSetup(this, 320, 200, null, true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //�����ܹرմ���
        init();   //ִ�г�ʼ�����������û��������������뵽����У�
        this.setVisible(true);
    }

    public void init() {
        FrameManager.setWindowLoc(this, FrameManager.FRAME_MANAGER_LOGIN_WINDOW);

        // ��������������ı�ǩ
        JLabel username_label = new JLabel("ѧ��/��ְ����");
        JLabel password_label = new JLabel("����");
        // �����������������������
        final JTextField user_field = new JTextField();
        final JPasswordField password_field = new JPasswordField();
        //������¼��ť
        JButton login_btn = new JButton("��¼");
        //����ע�ᰴť
        JButton register_btn = new JButton("ע��");
        //����������Ϣ
        JLabel info = new JLabel("����");

        //���ø�����ǩ�������Ĵ�С��λ��
        username_label.setBounds(10, 20, 150, 30);
        password_label.setBounds(10, 70, 150, 30);
        user_field.setBounds(100, 20, 200, 30);
        password_field.setBounds(100, 70, 200, 30);
        login_btn.setBounds(40, 120, 80, 30);
        register_btn.setBounds(160, 120, 80, 30);
        info.setBounds(275, 140, 40, 20);

        FrameManager.addComponents(this, username_label, password_label, user_field, password_field, login_btn, register_btn, info);

        //��¼��ť�ļ�����
        ActionListener l = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //������ݿ��д��ڴ��û��������룬��½�ɹ������ҿ�����һ������ܣ�����ҳ��
                //�����û���
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
                } else//��֮����¼���ɹ������µ�¼
                {
                    //��������������Ϊ�գ����û���������
                    FrameManager.clearField(user_field, password_field);
                }
            }
        };
        login_btn.addActionListener(l);
        login_btn.registerKeyboardAction(l, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        //ע�ᰴť�ļ�����
        register_btn.addActionListener(event -> {
            new register();
        });
        //���ڰ�ť�ļ�����
        info.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String version = "Version 2018.12\n" + "***��***��***С������\n" + "****** **********��\n";
                JOptionPane.showMessageDialog(null, version, "����",
                        JOptionPane.PLAIN_MESSAGE, new ImageIcon("img/info.jpg"));
            }
        });
        //�����Ļس���¼������
        password_field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    //������ݿ��д��ڴ��û��������룬��½�ɹ������ҿ�����һ������ܣ�����ҳ��
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
                    } else//��֮����¼���ɹ������µ�¼
                    {
                        //��������������Ϊ�գ����û���������
                        FrameManager.clearField(user_field, password_field);
                    }
                }
            }
        });
    }

    private boolean user_is_valid(String usrName, String passWord) {  //����¼���̺Ϸ���
        boolean flag = true; // Ĭ�ϺϷ�
        if (usrName.isEmpty() || passWord.isEmpty()) {
            JOptionPane.showMessageDialog(null, "�������û��������룡",
                    "��ʾ", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("img/info.jpg"));
            return false;
        }
        if (judge(usrName) || judge(passWord)) {   //�������Ƿ��ַ���ֱ���˳�
            JOptionPane.showMessageDialog(null, "��������Ƿ��ַ���",
                    "��ʾ", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("img/info.jpg"));
            return false;
        } else {
            loginusr = new DBConnection();
            conn = loginusr.getConnection();
            try {
                String loginQuery = "SELECT * FROM users WHERE no='" + trim(usrName) + "' AND password='" + trim(passWord) + "'";
                //SELECT * FROM users WHERE no='��¼��' AND password='��¼����'
                //��¼������������������ֹע��
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(loginQuery);
                if (!rs.next()) flag = false;  //û���ҵ�����˵���û���������������һ�����
                else rs.close();
                stmt.close();
            } catch (SQLException sqlex) {
                JOptionPane.showMessageDialog(null, "SQL ����ʱ����һЩС״�������Ժ����ԡ�",
                        "��ʾ", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("img/info.jpg"));
            } finally {
                if (!flag) {
                    JOptionPane.showMessageDialog(null, "�����˺Ż������������������", "��֤����",
                            JOptionPane.ERROR_MESSAGE, new ImageIcon("img/cancel.jpg"));
                }
                password = "";        //��߰�ȫ�ԣ������ڼ����Ϻ��ÿ�
                loginusr.CloseDBConnection(conn);
            }
        }
        return flag;
    }

    private boolean judge(String source) {
        if (source == null) return false;
        String regEx = "^[!$^&*+=|{}';'\\\",<>/?~��#��%����&*����|{}��������������'��������]|\\n|\\r|\\t+$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(source);
        return m.find();   //�жϸ����ַ������Ƿ��������ַ������򷵻�true
    }

    private String trim(String source) {
        return source.replaceAll("^0-9A-Za-z", ""); //���з����ֺ���ĸ�ַ����ᱻ�滻Ϊ���ַ�
    }

    private String hash(String ori_pass) {
        //todo: return hashed password
        return null;
    }
}

