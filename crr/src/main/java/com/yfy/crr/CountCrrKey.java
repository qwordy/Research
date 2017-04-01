package com.yfy.crr;

import java.io.File;

/**
 * Created by yfy on 2017/4/1.
 */
public class CountCrrKey {
  public CountCrrKey() {}

  public void count() {
    File root = new File(Config.projectsDir);
    dfs(root);
  }

  private void dfs(File root) {
    File[] files = root.listFiles();
    for (File file : files) {
      if (file.isDirectory())
        dfs(file);
      else if (file.getName().endsWith(".java"))
        analyze(file);
    }
  }

  private void analyze(File file) {
    Util.log(file.getAbsolutePath());
    
  }
}
