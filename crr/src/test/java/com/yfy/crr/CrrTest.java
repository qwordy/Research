package com.yfy.crr;

import org.junit.Test;

/**
 * Created by yfy on 10/22/16.
 */
public class CrrTest {
  @Test
  public void gitLogParser() throws Exception {
    GitLogParser parser = new GitLogParser();
    parser.parseAll();
  }

  @Test
  public void initTable() throws Exception {
    Db db = new Db();
    db.createTable("guava");
    db.commit();
  }

  @Test
  public void readDb() throws Exception {
    Analyser analyser = new Analyser();
    analyser.compareAll();
  }
}
