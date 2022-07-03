package library.systemutil;

import library.jdbc.utils.JDBCUtil;
import library.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class system {
    public static void main(String[] args) {
        adduser();
    }
    public static void adduser(){
      user user1=new user ();
      /* * Scanner sc= new Scanner(System.in);
        System.out.println("您正在注册用户");
        System.out.println("请输入您的学号");
         user1.setId(sc.nextInt());
        System.out.println("请输入您的姓名");
        user1.setName(sc.next());
        System.out.println("请输入您的年龄");
        user1.setAge(sc.nextInt());
        System.out.println("请输入您要注册的账号类型");
        System.out.println("0==管理员,1=普通用户");
        user1.setUsertype(sc.nextInt());
        System.out.println("请输入您要注册的账号密码");
        user1.setPassword(sc.nextInt());**/
       System.out.println();
        Connection conn=null;
        PreparedStatement pstmt=null;
        try {
            conn = JDBCUtil.getconnedction();
            String sql ="insert into user values (?,?,?);";
              pstmt = conn.prepareStatement(sql);
                 pstmt.setInt(1,122);
                 pstmt.setString(2,"xxxx");
                 pstmt.setInt(3,222);
            int count = pstmt.executeUpdate();
            System.out.println(count);

            /*pstmt.executeQuery();
            // 得到操作数据库sql语句的预编译对象PraparementStatement
            PreparedStatement ps = conn.prepareStatement("insert into user(id,userName,userPassword) values(?,?,?)");
            ps.setString(1, "5");
            ps.setString(2, "xiaoming");
            ps.setInt(3, 1234);

            // 执行
            int rs = ps.executeUpdate();
            System.out.println(rs);
            //int count = pstmt.executeUpdate();
           // System.out.println(count);*/
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtil.close(pstmt,conn);
        }
    }


        public static void userloin(){
            Map<String,String> userLoginInfo = initUI();
            boolean loginSuccess = login(userLoginInfo);
            System.out.println(loginSuccess ? "登录成功" : "登录失败");
        }


        private static boolean login(Map<String, String> userLoginInfo) {
            boolean loginSuccess = false;
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                // 1、注册驱动
               conn = JDBCUtil.getconnedction();
                // 3、获取预编译的数据库操作对象
                // sql语句的框架中，一个?，表示一个占位符，一个?将来接收一个值。注意：?不要用单引号括起来
                String sql = "select * from user where userName = ? and userPassword = ?";
                // 程序执行到此处，会发送sql语句框架给DBMS，DBMS对sql语句框架进行预编译。
                ps = conn.prepareStatement(sql);
                // 给占位符?传值，第一个?的下标是1，第二个?的下标是2（JDBC中下标都从1开始）
                ps.setString(1,userLoginInfo.get("userName"));
                ps.setString(2,userLoginInfo.get("userPassword"));
                // 4、执行sql语句
                rs = ps.executeQuery();
                // 5、处理结果集
                if(rs.next()) {
                    loginSuccess = true;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                // 6、释放资源
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
            return loginSuccess;
        }



        private static Map<String, String> initUI() {
            Scanner s = new Scanner(System.in);

            System.out.print("请输入用户:");
            String userName = s.nextLine();
            System.out.print("请输入密码:");
            String userPassword = s.nextLine();

            Map<String,String> userLoginInfo = new HashMap<>();
            userLoginInfo.put("userName",userName);
            userLoginInfo.put("userPassword",userPassword);

            return userLoginInfo;
        }
    }



