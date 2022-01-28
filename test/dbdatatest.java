import java.sql.*;
import java.util.Scanner;
import org.junit.Test;

public class dbdatatest {
    /*��������*/
    public static void main(String[] args) {
        String connectDB = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=stu";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");//�������ݿ����棬���ظ����ַ���������
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("�������ݿ�����ʧ��");
            System.exit(0);
        }
        System.out.println("���ݿ������ɹ�");

        try {
            String user = "sa";
            String password = "123456";
            Connection con = DriverManager.getConnection(connectDB, user, password);//�������ݿ����
            System.out.println("�������ݿ�ɹ�");
            Statement stmt = con.createStatement();//����SQL�������
            System.out.println("***********�˵�ѡ��******************");
            System.out.println("1:����������������");
            System.out.println("2:ʵ������ģ����ѯ�����");
            System.out.println("3:����������߸���Ϊ2.0m,�����");
            System.out.println("4:�˳�");
            System.out.println("************************************");
            while (true) {
                System.out.println("�����루1-4����");
                Scanner reader = new Scanner(System.in);
                int x = reader.nextInt();
                if (x == 4) {
                    System.out.println("�˳�~");
                    break;
                } else if (x == 1) {
                    //��ȡ����
                    ResultSet rs = stmt.executeQuery("SELECT * FROM stu order by sheight");//����SQL����ѯ�����(����)
                    System.out.println("��ʼ��ȡ����");
                    //ѭ�����ÿһ����¼
                    while (rs.next()) {
                        System.out.println(rs.getString("sno") + "\t" + rs.getString("sname") + "\t" + rs.getString("ssex") + "\t" + rs.getString("sheight"));
                    }
                    System.out.println("��ȡ���");
                } else if (x == 2) {
                    System.out.println("������ģ�����������:");
                    reader = new Scanner(System.in);
                    String str = reader.next();
                    //��ȡ����
                    ResultSet rs = stmt.executeQuery("SELECT * FROM stu where sname like '%" + str + "%'");//����SQL����ѯ�����
                    System.out.println("��ʼ��ȡ����");
                    //ѭ�����ÿһ����¼
                    while (rs.next()) {
                        System.out.println(rs.getString("sno") + "\t" + rs.getString("sname") + "\t" + rs.getString("ssex") + "\t" + rs.getString("sheight"));
                    }
                    System.out.println("��ȡ���");
                } else if (x == 3) {
                    stmt.executeUpdate("update stu set sheight=200.00 where sname='����' ");
                    ResultSet rs = stmt.executeQuery("select * from stu where sname='����'");
                    System.out.println("��ʼ��ȡ����");
                    while (rs.next()) {
                        System.out.println(rs.getString("sno") + "\t" + rs.getString("sname") + "\t" + rs.getString("ssex") + "\t" + rs.getString("sheight"));
                    }
                    System.out.println("��ȡ���");
                } else
                    System.out.println("���������������");
            }

            //�ر�����
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            //System.out.println("���ݿ����Ӵ���");
            System.exit(0);
        }
    }
}
