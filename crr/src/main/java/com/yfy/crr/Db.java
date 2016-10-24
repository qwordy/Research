package com.yfy.crr;

import java.io.Reader;
import java.sql.*;

/**
 * Created by yfy on 10/22/16.
 */
public class Db {

  private Connection conn;

  public Db() throws Exception {
    String dbFile = Config.projectsDir + '/' + "pairs.db";
    conn = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
    conn.setAutoCommit(false);
    //stmt = conn.createStatement();
    //stmt.setFetchSize(200);
  }

  public void createTable(String name) throws Exception {
    Statement sm = conn.createStatement();
    String sql = "drop table " + name;
    try {
      sm.execute(sql);
    } catch (Exception e) {}
    sql = "create table " + name + " (file1 text, file2 text);";
    sm.execute(sql);
  }

  public void addPairs(String table, String file1, String file2)
      throws Exception {
    String sql = "insert into " + table + " values(?, ?);";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setString(1, file1);
    ps.setString(2, file2);
    ps.execute();
  }

  public void addPairs(String table, Reader reader1, Reader reader2)
      throws Exception {
    String sql = "insert into " + table + " values(?, ?);";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setClob(1, reader1);
    ps.setClob(2, reader2);
    ps.execute();
  }

  public void 

  /**
   * Must call it
   * @throws Exception
   */
  public void commit() throws Exception {
    conn.commit();
  }

}
