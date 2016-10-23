package com.yfy.crr;

import java.sql.*;

/**
 * Created by yfy on 10/22/16.
 */
public class Db {

  private Connection conn;

  //private Statement stmt;

  public Db() throws Exception {
    String dbFile = Config.projectsDir + '/' + "pairs.db";
    conn = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
    conn.setAutoCommit(false);
    //stmt = conn.createStatement();
    //stmt.setFetchSize(200);
  }

  public void createTable(String name) throws Exception {
    String sql = "create table " + name + " (file1 text, file2 text);";
    Statement sm = conn.createStatement();
    sm.execute(sql);
    conn.commit();
  }

  public void addPairs(String table, String file1, String file2)
      throws Exception {
    String sql = "insert into " + table + " values(?, ?);";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setString(1, file1);
    ps.setString(2, file2);
  }

}
