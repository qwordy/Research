package com.yfy.crr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by yfy on 10/22/16.
 */
public class Db {

  private Connection conn;

  private Statement stmt;

  public Db() throws Exception {
    String dbFile = Config.projectsDir + '/' + "pairs.db";
    conn = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
    conn.setAutoCommit(false);
    stmt = conn.createStatement();
    stmt.setFetchSize(200);
  }

  public void initTable(String name) throws Exception {
    String sql = "create table " + name + " (file1 text, file2 text);";
    stmt.execute(sql);
  }
}
