package com.yfy.crr;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * Created by yfy on 10/20/16.
 */
public class Execute {
  /**
   * Execute cmd
   * @param cmd command
   * @param dir directory to execute in
   * @param task deal with output
   * @return 0 on success, -1 otherwise
   */
  public static int exec(String cmd, String dir, ITaskAfterRun task) {
    try {
      Process process;
      if (dir == null)
        process = Runtime.getRuntime().exec(cmd);
      else
        process = Runtime.getRuntime().exec(cmd, null, new File(dir));
      BufferedReader br =
          new BufferedReader(new InputStreamReader(process.getInputStream()));
      if (task == null)
        while (br.readLine() != null);
      else
        task.run(br);
    } catch (Exception e) {
      e.printStackTrace();
      return -1;
    }
    return 0;
  }
}
