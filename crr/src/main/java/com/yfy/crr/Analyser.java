package com.yfy.crr;

import ch.uzh.ifi.seal.changedistiller.model.classifiers.java.JavaEntityType;
import ch.uzh.ifi.seal.changedistiller.treedifferencing.Node;
import ch.uzh.ifi.seal.changedistiller.treedifferencing.TreeDifferencer;
import ch.uzh.ifi.seal.changedistiller.treedifferencing.TreeEditOperation;

import java.sql.ResultSet;
import java.util.List;

/**
 * Created by yfy on 10/24/16.
 */
public class Analyser {

  private Db db;

  public Analyser() throws Exception {
    db = new Db();
  }

  public void compareAll() throws Exception {
    for (String project : Config.projects) {
      Util.log(project);
      ResultSet rs = db.readPair(project);
      while (rs.next()) {
        String file1 = rs.getString(1);
        String file2 = rs.getString(2);
        getEdit(file1, file2);
        Util.log(file1.length() + " " + file2.length());
      }
      rs.close();
    }
  }

  public void getEdit(String code1, String code2) {
    Node node1 = new Node(JavaEntityType.CLASS, code1);
    Node node2 = new Node(JavaEntityType.CLASS, code2);
    TreeDifferencer treeDifferencer = new TreeDifferencer();
    treeDifferencer.calculateEditScript(node1, node2);
    List<TreeEditOperation> edits = treeDifferencer.getEditScript();
    Util.log(edits.size());
  }

}
