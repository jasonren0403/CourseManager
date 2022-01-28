import java.sql.*;
import java.util.Scanner;
import org.junit.Test;

public class dbdatatest {
    /*测试数据*/
    public static void main(String[] args) {
        String connectDB = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=stu";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");//加载数据库引擎，返回给定字符串名的类
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("加载数据库引擎失败");
            System.exit(0);
        }
        System.out.println("数据库驱动成功");

        try {
            String user = "sa";
            String password = "123456";
            Connection con = DriverManager.getConnection(connectDB, user, password);//连接数据库对象
            System.out.println("连接数据库成功");
            Statement stmt = con.createStatement();//创建SQL命令对象
            System.out.println("***********菜单选择******************");
            System.out.println("1:按身高升序排序输出");
            System.out.println("2:实现姓名模糊查询并输出");
            System.out.println("3:将张三的身高更改为2.0m,并输出");
            System.out.println("4:退出");
            System.out.println("************************************");
            while (true) {
                System.out.println("请输入（1-4）：");
                Scanner reader = new Scanner(System.in);
                int x = reader.nextInt();
                if (x == 4) {
                    System.out.println("退出~");
                    break;
                } else if (x == 1) {
                    //读取数据
                    ResultSet rs = stmt.executeQuery("SELECT * FROM stu order by sheight");//返回SQL语句查询结果集(集合)
                    System.out.println("开始读取数据");
                    //循环输出每一条记录
                    while (rs.next()) {
                        System.out.println(rs.getString("sno") + "\t" + rs.getString("sname") + "\t" + rs.getString("ssex") + "\t" + rs.getString("sheight"));
                    }
                    System.out.println("读取完毕");
                } else if (x == 2) {
                    System.out.println("请输入模糊名（例：李）:");
                    reader = new Scanner(System.in);
                    String str = reader.next();
                    //读取数据
                    ResultSet rs = stmt.executeQuery("SELECT * FROM stu where sname like '%" + str + "%'");//返回SQL语句查询结果集
                    System.out.println("开始读取数据");
                    //循环输出每一条记录
                    while (rs.next()) {
                        System.out.println(rs.getString("sno") + "\t" + rs.getString("sname") + "\t" + rs.getString("ssex") + "\t" + rs.getString("sheight"));
                    }
                    System.out.println("读取完毕");
                } else if (x == 3) {
                    stmt.executeUpdate("update stu set sheight=200.00 where sname='张三' ");
                    ResultSet rs = stmt.executeQuery("select * from stu where sname='张三'");
                    System.out.println("开始读取数据");
                    while (rs.next()) {
                        System.out.println(rs.getString("sno") + "\t" + rs.getString("sname") + "\t" + rs.getString("ssex") + "\t" + rs.getString("sheight"));
                    }
                    System.out.println("读取完毕");
                } else
                    System.out.println("输入错误！重新输入");
            }

            //关闭连接
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            //System.out.println("数据库连接错误");
            System.exit(0);
        }
    }
}
