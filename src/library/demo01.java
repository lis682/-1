package library;
import library.jdbc.utils.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class demo01 {
        public static void main(String[] args) {
            Connection conn=null;
            PreparedStatement pstmt=null;
            try {
                conn = JDBCUtil.getconnedction();
                String sql ="insert into user values(?,?,?,?)";
                 pstmt = conn.prepareStatement(sql);
                 pstmt.setInt(1,202012903);
                 pstmt.setString(2,"李立");
                 pstmt.setInt(3,20);
                 pstmt.setInt(4,4);
                int count = pstmt.executeUpdate(sql);
                System.out.println(count);
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                JDBCUtil.close(pstmt,conn);
            }


        }
    }


