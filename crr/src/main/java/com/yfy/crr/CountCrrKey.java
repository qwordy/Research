package com.yfy.crr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yfy on 2017/4/1.
 */
public class CountCrrKey {
  private Map<String, Integer> map;
  private int sum;

  public CountCrrKey() {}

  public void count() throws Exception {
    map = new HashMap<>();
    File root = new File(Config.projectsDir);
    dfs(root);
    for (String key : map.keySet())
      Util.log(key + " " + map.get(key));
    Util.log(sum);
  }

  private void dfs(File root) throws Exception {
    File[] files = root.listFiles();
    for (File file : files) {
      if (file.isDirectory())
        dfs(file);
      else if (file.getName().endsWith(".java"))
        analyze(file);
    }
  }

  private void analyze(File file) throws Exception {
    //Util.log(file.getAbsolutePath());
    BufferedReader br = new BufferedReader(new FileReader(file));
    String line;
    while ((line = br.readLine()) != null) {
      for (String key : ConcurrentKeywords.classList) {
        if (line.contains(key)) {
          map.put(key, map.getOrDefault(key, 0) + 1);
          sum++;
        }
      }
    }
  }
}
