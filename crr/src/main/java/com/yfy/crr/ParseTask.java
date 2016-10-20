package com.yfy.crr;

import java.io.BufferedReader;

/**
 * Created by yfy on 10/20/16.
 */
public class ParseTask implements ITaskAfterRun {
  @Override
  public void run(BufferedReader br) throws Exception {
    String line;
    while ((line = br.readLine()) != null) {
      if (line.length() > 5 && line.charAt(0) == 'M' &&
          line.substring(line.length() - 5).equals(".java")) {
        String filename = line.substring(2);
        Util.log(filename);
      }
    }
  }
}
