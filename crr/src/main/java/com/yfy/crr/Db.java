package com.yfy.crr;

import java.io.Reader;
import java.sql.*;

/**
 * Created by yfy on 10/22/16.
 */
public class Db {

  private Connection conn;

  private int smCount;

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
    sm.close();
    conn.commit();
  }

  public void addPairs(String table, String file1, String file2)
      throws Exception {
    String sql = "insert into " + table + " values(?, ?);";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setString(1, file1);
    ps.setString(2, file2);
    ps.execute();
    ps.close();
    smCount++;
    if (smCount > 999) {
      smCount = 0;
      conn.commit();
      Util.log(table + " commit");
    }
  }

  public void addPairs(String table, Reader reader1, Reader reader2)
      throws Exception {
    String sql = "insert into " + table + " values(?, ?);";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setClob(1, reader1);
    ps.setClob(2, reader2);
    ps.execute();
  }

  public ResultSet readPair(String table) throws Exception {
    Statement sm = conn.createStatement();
    //sm.setFetchSize(9999);
    String sql = "select * from " + table + ";";
    return sm.executeQuery(sql);
  }

  public void moveToMysql() throws Exception {
    int count = 0;
    Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost/crr?user=root");
    myConn.setAutoCommit(false);
    Statement mySm = myConn.createStatement();
    for (String project: Config.projects) {
      try {
        mySm.execute("insert into project(name) values(\"" + project + "\");");
      } catch (Exception e) {}
      ResultSet myRs = mySm.executeQuery(
          "select id from project where name=\"" + project + "\";");
      myRs.next();
      int pid = myRs.getInt("id");  // project id
      //Util.log(pid);

      Statement sm = conn.createStatement();
      String sql = "select * from " + project + ";";
      ResultSet rs = sm.executeQuery(sql);
      sql = "insert into pair(file1, file2, pid) values(?, ?, ?);";
      PreparedStatement myPs = myConn.prepareStatement(sql);
      while (rs.next()) {
        myPs.setString(1, rs.getString("file1"));
        myPs.setString(2, rs.getString("file2"));
        myPs.setInt(3, pid);
        myPs.execute();

        count++;
        Util.log(count);
        if (count % 999 == 0)
          myConn.commit();
      }
    }
    myConn.commit();
  }

  /**
   * Must call it at the end
   * @throws Exception
   */
  public void commit() throws Exception {
    conn.commit();
  }

}
