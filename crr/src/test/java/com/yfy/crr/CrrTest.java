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
  public void changeByTime() throws Exception {
    new GitLogParser().setTaskType(TaskType.ChangeByTime).parseAll();
  }

  @Test
  public void rq3() throws Exception {
    new GitLogParser().parseAll();
  }

  @Test
  public void countCrrKeywords() throws Exception {
    new CountCrrKey().count();
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

  @Test
  public void moveToMysql() throws Exception {
    new Db().moveToMysql();
  }

  @Test
  public void mysql() throws Exception {
    new MysqlDb().read();
  }

  @Test
  public void cluster() throws Exception {
    new Cluster().run();
  }

  @Test
  public void train() throws Exception {
    new Train();
  }



}
