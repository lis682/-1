package library.jdbc.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import library.demo01;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JDBCUtil {

    private static DataSource ds;

    static {
        try {
            //加载配置文件
            Properties pro = new Properties();
            InputStream is = demo01.class.getClassLoader().getResourceAsStream("druid-config.properties");
            pro.load(is);
            //获取DataSource
            ds = DruidDataSourceFactory.createDataSource(pro);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 保存至数据库
     *
     * @param sql      sql语句
     * @param saveList 要保存的元素
     */
    public static int excuteSaveorUpdateOrDelete(String sql, Object[] saveList) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = JDBCUtil.getconnedction();
            pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < saveList.length; i++) {
                pstmt.setObject(i + 1, saveList[i]);
            }
            int count = pstmt.executeUpdate();
            System.out.println(count);
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(pstmt, conn);
        }
        return -1;
    }


    public static <T> List<T> selectList(String sql, Object[] saveList, Class<T> clazz) {
        List<T> list = null;
        try {
            Connection conn = JDBCUtil.getconnedction();
            PreparedStatement stmt = conn.prepareStatement(sql);
            for (int i = 0; i < saveList.length; i++) {
                stmt.setObject(i + 1, saveList[i]);
            }
            ResultSet rs = stmt.executeQuery();//创建数据对象

            list = resultSetToList(rs, clazz);
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //定义静态方法，并根据泛型和反射，实现转换
    //注意：要求数据库的列名必须和JAVA实体类的属性名、类型完全一致
    public static <T> List<T> resultSetToList(ResultSet rs, Class<T> cls) {
        //定义接收的集合
        List<T> list = new ArrayList<T>();
        //创建一个对象，方便后续反射赋值
        Object obj = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            while (rs.next()) {
                //利用反射获取，执行类的实例化对象
                obj = cls.newInstance();

                //利用反射，获取对象类信息中的所有属性
                Field[] fields = cls.getDeclaredFields();
                boolean flag = false;
                for (Field fd : fields) {
                    //屏蔽权限
                    fd.setAccessible(true);
                    Object fdobj = rs.getObject(fd.getName());
                    //为空不处理
                    if (fdobj != null) {
                        flag= true;
                        //时间类型转字符串
                        if (fdobj instanceof Date && fd.getType().equals(String.class)) {
                            String s = format.format(fdobj);
                            fd.set(obj, s);
                        }
                        //非字符串类型转字符串
                        else if (!(fdobj instanceof Date) && fd.getType().equals(String.class)) {
                            //为对象属性赋值
                            fd.set(obj, String.valueOf(fdobj));
                        } else {
                            //为对象属性赋值
                            fd.set(obj, fdobj);
                        }
                    }
                }
              if(!flag){
                  break;
              }
                //返回转换后的集合
                list.add((T) obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //返回集合
        return list;
    }

    public static Connection getconnedction() throws SQLException {
        return ds.getConnection();
    }

    public static void close(Statement stmt, Connection conn) {
        close(null, stmt, conn);

    }

    public static void close(ResultSet rs, Statement stmt, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static DataSource getDataSource() {
        return ds;
    }

}

