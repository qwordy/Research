package com.yfy.crr;

import org.apache.commons.lang3.StringUtils;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yfy on 11/5/16.
 */
public class Cluster {

  public void run() throws Exception {
    tfidf();
    kmeans();
  }

  private void tfidf() throws Exception {
    // how many files each key occurs
    int[] keyFileNums = new int[ConcurrentKeywords.list.size()];

    // tf
    int fileCount = 0;
    List<String> keyList = ConcurrentKeywords.list;
    List<double[]> tfs = new ArrayList<>();
    ResultSet rs = new MysqlDb().readFile1();
    while (rs.next()) {  // for each file
      fileCount++;
      String file = rs.getString(1);
      int wordCount = file.split("\\s").length;
      //Util.log(wordCount);
      double[] tf = new double[keyList.size()];
      for (int i = 0; i < keyList.size(); i++) {
        int keyCount = StringUtils.countMatches(file, keyList.get(i));
        tf[i] = keyCount / (double)wordCount;
        if (keyCount > 0)
          keyFileNums[i]++;
      }
      tfs.add(tf);
      //Util.log(Arrays.toString(tf));
    }

    // idf
    for (double[] tf : tfs) {
      for (int i = 0; i < tf.length; i++) {
        tf[i] *= Math.log(fileCount / (double)(keyFileNums[i] + 1));
      }
      Util.log(Arrays.toString(tf));
    }
  }

  private void kmeans() {

  }

}
