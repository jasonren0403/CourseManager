package com.ren.CourseManager.utils;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private Connection conn = null;

    public DBConnection() {
        // todo: Support MySQL connection.
        //�����username��password���������ݿ����ӵ�
        String username = "sa";
        String password = "123456";
        String url = "jdbc:sqlserver://127.0.0.1:1433;integratedSecurity=true;DatabaseName=CourseTable";  //if source is MSSQL...

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "�������ݿ�����ʧ�ܣ���Ҫsql server����",
                    "��ʾ", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("img/info.jpg"));
        }
        try {
            conn = DriverManager.getConnection(url, username, password);
            conn.setAutoCommit(false);
            //System.out.println("Auto commit enabled");
            //System.out.println("���ӳɹ�");
        } catch (Exception e) {
            System.out.println(e.toString());
            JOptionPane.showMessageDialog(null, "SQL����������ʧ�ܣ����Ժ�����",
                    "��ʾ", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("img/cancel.jpg"));
        }
    }

    public void CloseDBConnection(Connection conn) {
        try {
            conn.setAutoCommit(true);
            conn.close();
            //System.out.println("Connection closed.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "�ڹر�SQL����ʱ��������");
        }
    }

    public Connection getConnection() {
        try{
            conn.setAutoCommit(false);
        }catch(Exception ignored){
        }
        return conn;
    }

    public static void main(String[] args){
        DBConnection dbc=new DBConnection();
        Connection con=dbc.getConnection();
        dbc.CloseDBConnection(con);
    }
}

