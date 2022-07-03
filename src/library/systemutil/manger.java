package library.systemutil;

import library.jdbc.utils.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class manger {
    public static boolean isExist_user(String username, int id) {
        try {
            Connection conn = JDBCUtil.getconnedction();
            String sql = "select * from user where userName = ? ";    //要执行的SQL
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setObject(1,username);
            ResultSet rs = stmt.executeQuery(sql);//创建数据对象
            while (rs.next()) {
                if (rs.getString(2).equals(username)&&rs.getInt(1)==id) {
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
    public static void adduser(int id,String username,int userpassword) throws SQLException {//完成
        boolean boo=isExist_user(username,id);
        if(boo==true){
            System.out.println("用户名已存在请更换用户名之后再进行添加");
        }else {

            PreparedStatement pstmt = null;
            Connection conn = null;
            conn = JDBCUtil.getconnedction();
            String sql = "insert into user values(?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setString(2, username);
            pstmt.setInt(3, userpassword);
            int count = pstmt.executeUpdate();
            if (count == 1) {
                System.out.println("添加成功");
            } else {
                System.out.println("添加失败");
            }
            JDBCUtil.close(pstmt, conn);
        }
    }//完成
    public static void showusermassage() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getconnedction();
            String sql = "select * from user";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery(sql);
            while (rs.next()) {
                System.out.print(rs.getInt(1) + "\t");
                System.out.print(rs.getString(2) + "\t");
                System.out.print(rs.getString(3) + "\t");

                System.out.println();

            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }//完成
    public static void  serarchuserrecord(int id,String name){
        Connection conn=null;
        PreparedStatement pstmt=null;
        ResultSet rs=null;
        try {
            conn=JDBCUtil.getconnedction();
            String sql = "select * from record where name = ? and  id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,name);
            pstmt.setInt(2,id);
            rs=pstmt.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString(1) + "\t");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtil.close(rs,pstmt,conn);
        }
    }//完成

}
