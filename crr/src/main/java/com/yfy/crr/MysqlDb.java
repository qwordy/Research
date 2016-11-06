package com.yfy.crr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by yfy on 11/6/16.
 */
public class MysqlDb {

  private Connection conn;

  public MysqlDb() throws Exception {
    conn = DriverManager.getConnection("jdbc:mysql://localhost/crr?user=root");
  }

  public ResultSet readFile1() throws Exception {
    Statement sm = conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
        java.sql.ResultSet.CONCUR_READ_ONLY);
    sm.setFetchSize(Integer.MIN_VALUE);
    return sm.executeQuery("select file1 from pair");
  }

  public void read() throws Exception {
    Statement sm = conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
        java.sql.ResultSet.CONCUR_READ_ONLY);
    sm.setFetchSize(Integer.MIN_VALUE);
    ResultSet rs = sm.executeQuery("select file1, file2 from pair;");
    int count = 0;
    while (rs.next()) {
      Util.log(++count);
    }
  }

}
