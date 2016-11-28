package com.yfy.crr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yfy on 16-11-28.
 */
public class Train {

  public void train() throws Exception {
    feature(new File("../svm/positive"), 1);
    feature(new File("../svm/negative"), 0);
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
      Util.log(f.toStr());
    }
  }
}
