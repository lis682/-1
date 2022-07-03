package library.systemutil;

import library.jdbc.utils.JDBCUtil;

import java.sql.*;
import java.util.List;
import java.util.Scanner;


public class UserService {//完成

    public static boolean isExist_user(String username, int id) {
        try {

            String sql = "select * from user where id = ? and userName =?";    //要执行的SQL
            List<library.user> result =  JDBCUtil.selectList(sql,new Object[]{id,username}, library.user.class);
            if(result==null || result.size()==0){
                return  false ;
            }
        } catch (Exception e) {
            e.printStackTrace();
           throw new RuntimeException("查询出错了！");
        }
        return true;

    }//完成

    public static boolean userlogin(String username, int password) {
        try {
            Connection conn = JDBCUtil.getconnedction();
            String sql = "select * from user";    //要执行的SQL
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery(sql);//创建数据对象
            while (rs.next()) {
                if (rs.getString(2).equals(username) && rs.getInt(3) == password) {
                    rs.close();
                    stmt.close();
                    conn.close();
                    return true;
                }
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }//完成

    public static void adduser(int id, String username, int userpassword) throws SQLException {//完成
        boolean boo = isExist_user(username, id);
        if (boo == true) {
            System.out.println("用户名已存在请更换用户名之后再进行添加");
        } else {
            String sql = "insert into user values(?,?,?)";
            int result = JDBCUtil.excuteSaveorUpdateOrDelete(sql, new Object[]{id, username, userpassword});


        }
    }//完成

    public static void updateuserpassword(int id, String username, int userpassword, int newpassword) {//完成
        boolean boo = isExist_user(username, id);
        if (boo == false) {
            System.out.println("您所输入的用户信息不存在，请重新输入");
        } else {
            Connection conn = null;
            PreparedStatement pstmt = null;
            try {
                conn = JDBCUtil.getconnedction();
                String sql = "update user set userpassword = '" + newpassword + "' where username = '" + username + "';";
                pstmt = conn.prepareStatement(sql);
                int count = pstmt.executeUpdate();
                if (count == 2) {
                    System.out.println("修改成功");
                } else {
                    System.out.println("修改失败");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                JDBCUtil.close(pstmt, conn);
            }
        }
    }//完成

    public static void backbook(int id, String name, String bookname, int backnum) {//完成
        int over1 = backbooknum(bookname, backnum);
        backbookwriteindatabase(over1, bookname);
        backbookwriteinrecord(id, name, bookname, backnum);
    }//完成

    public static int backbooknum(String bookname, int backnum) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int overi = 0;
        try {
            conn = JDBCUtil.getconnedction();
            String sql = "select *from bookmassage";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                if (rs.getString(2).equals(bookname)) {
                    int i = rs.getInt(6);
                    overi = i - backnum;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(rs, pstmt, conn);

        }
        return overi;
    }//完成

    public static void backbookwriteindatabase(int overi, String bookname) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = JDBCUtil.getconnedction();
            String sql = "update bookmassage set jiechunum = '" + overi + "' where bookname = '" + bookname + "';";
            pstmt = conn.prepareStatement(sql);
            int count = pstmt.executeUpdate();
            if (count == 1) {
                System.out.println("还书成功");
            } else {
                System.out.println("还书失败");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(pstmt, conn);
        }
    }//完成

    public static void backbookwriteinrecord(int id, String username, String bookname, int backnum) {
        boolean boo = isExist_user(username, id);
        String str = "";
        str = "学号为" + id + "姓名" + username + "归还的书名" + bookname + "归还的数量" + backnum;
        if (boo == false) {
            System.out.println("用户名不存在请更换用户名之后再进行还书操作");
        } else {

            PreparedStatement pstmt = null;
            Connection conn = null;
            try {
                conn = JDBCUtil.getconnedction();
                String sql = "INSERT INTO record (id,NAME,record)VALUES(?,?,?);";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                pstmt.setString(2, username);
                pstmt.setString(3, str);
                int count = pstmt.executeUpdate();
                if (count == 1) {
                    System.out.println("添加记录成功");
                } else {
                    System.out.println("添加记录失败");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            JDBCUtil.close(pstmt, conn);
        }
    }

    public static void brrowbook(int id, String name, String bookname, int brrownum) {//完成
        int over1 = brrowbooknum(bookname, brrownum);
        brrowbookwriteindatabase(over1, bookname);
        brrowbookwriteinrecord(id, name, bookname, brrownum);
    }//完成

    public static int brrowbooknum(String bookname, int brrownum) {
        Connection conn = null;
        Connection conn1 = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        ResultSet rs = null;
        int overi = 0;
        try {
            conn = JDBCUtil.getconnedction();
            String sql = "select *from bookmassage";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                if (rs.getString(2).equals(bookname)) {
                    int i = rs.getInt(6);
                    overi = i + brrownum;

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(rs, pstmt, conn);

        }
        return overi;
    }//完成

    public static void brrowbookwriteindatabase(int overi, String bookname) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = JDBCUtil.getconnedction();
            String sql = "update bookmassage set jiechunum = '" + overi + "' where bookname = '" + bookname + "';";
            pstmt = conn.prepareStatement(sql);
            int count = pstmt.executeUpdate();
            if (count == 1) {
                System.out.println("借出成功");
            } else {
                System.out.println("借出失败");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(pstmt, conn);
        }
    }//完成

    public static void brrowbookwriteinrecord(int id, String username, String bookname, int brrownum) {
        boolean boo = isExist_user(username, id);
        String str = "";
        str = "学号为" + id + "姓名" + username + "借出的书名" + bookname + "借出的数量" + brrownum;
        if (boo == false) {
            System.out.println("用户名不存在请更换用户名之后再进行借书操作");
        } else {

            PreparedStatement pstmt = null;
            Connection conn = null;
            try {
                conn = JDBCUtil.getconnedction();
                String sql = "INSERT INTO record (id,NAME,record)VALUES(?,?,?);";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                pstmt.setString(2, username);
                pstmt.setString(3, str);
                int count = pstmt.executeUpdate();
                if (count == 1) {
                    System.out.println("添加记录成功");
                } else {
                    System.out.println("添加记录失败");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            JDBCUtil.close(pstmt, conn);
        }
    }//完成

    public static void serarchuserrecord(int id, String name) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getconnedction();
            String sql = "select * from record where name = ? and  id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setInt(2, id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString(1) + "\t");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(rs, pstmt, conn);
        }
    }//完成

    public static void userloginmenu() {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入您的用户名");
        String name = sc.next();
        System.out.println("请输入您的密码");
        int password = sc.nextInt();
        boolean boo = userlogin(name, password);
        if (boo == true) {
            System.out.println("登陆成功");
        } else {
            System.out.println("登录失败，请重试");
        }

    }//完成

    public static void userloginmenu1(String name, int password) throws SQLException {
        Scanner sc = new Scanner(System.in);
        int id = UserService.getid(name, password);
        System.out.println("请输入您的选择：");
        System.out.println("1==添加用户2==更改密码3==借书4==还书5==查看自身记录");
        int choise = sc.nextInt();
        switch (choise) {
            case 1:
                System.out.println("请输入新的id");
                int newid = sc.nextInt();
                System.out.println("请输入新的用户名");
                String str = sc.next();
                System.out.println("请输入您的密码");
                int newpassword = sc.nextInt();
                adduser(newid, str, newpassword);
                break;
            case 2:
                System.out.println("请输入新的密码");
                int password2 = sc.nextInt();
                updateuserpassword(id, name, password, password2);
                break;
            case 3:


        }


    }//完成

    public static void showbookmassage() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getconnedction();
            String sql = "select * from bookmassage";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery(sql);
            while (rs.next()) {
                System.out.print("isbn: " + rs.getInt(1) + "\t");
                System.out.print("书名: " + rs.getString(2) + "\t");
                System.out.print("作者: " + rs.getString(3) + "\t");
                System.out.print("出版社: " + rs.getString(4) + "\t");
                System.out.print("总数" + rs.getInt(5) + "\t");
                System.out.print("已借出" + rs.getInt(6) + "\t");


                System.out.println();

            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }//完成

    public static int getid(String username, int password) {
        Connection conn = null;
        Connection conn1 = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        ResultSet rs = null;
        int i = 0;
        try {
            conn = JDBCUtil.getconnedction();
            String sql = "select *from user";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                if (rs.getString(2).equals(username) && rs.getInt(3) == password) {
                    i = rs.getInt(1);

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(rs, pstmt, conn);

        }
        return i;
    }
}
