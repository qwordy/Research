package com.yfy.crr;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
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
  public static int execTask(String cmd, String dir, ITaskAfterRun task) {
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

  public static BufferedReader execWithOutput(String cmd, String dir) {
    try {
      Process process;
      if (dir == null)
        process = Runtime.getRuntime().exec(cmd);
      else
        process = Runtime.getRuntime().exec(cmd, null, new File(dir));
      return new BufferedReader(
          new InputStreamReader(process.getInputStream()));
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private static byte[] buf = new byte[4096];

  public static void execIgnoreOutput(String cmd, String dir) {
    try {
      InputStream is;
      if (dir == null)
        is = Runtime.getRuntime().exec(cmd).getInputStream();
      else
        is = Runtime.getRuntime().exec(cmd, null, new File(dir))
            .getInputStream();
      while (is.read(buf) != -1);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}