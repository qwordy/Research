package com.yfy.crr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yfy on 16-11-28.
 */
public class Train {

  private PrintWriter pw;

  public Train() throws Exception {
    pw = new PrintWriter("../svm2/train");
    feature(new File("../svm2/p"), 1);
    feature(new File("../svm2/n"), 0);
    pw.close();
  }

  private void feature(File dir, int label) throws Exception {
    Util.log("label: " + label);
    GitLogParser parser = new GitLogParser();
    for (File file : dir.listFiles()) {
      BufferedReader br = new BufferedReader(new FileReader(file));
      List<String> lines = br.lines().collect(Collectors.toList());
      Feature f = new Feature();
      parser.textFeature(lines, f);
      parser.codeFeature(lines, f);
      pw.println(label + " " + f.toStr());
    }
  }
}
