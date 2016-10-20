package com.yfy.crr;

import java.io.BufferedReader;

/**
 * Created by yfy on 10/20/16.
 */
public class EchoTask implements ITaskAfterRun {
  @Override
  public void run(BufferedReader br) throws Exception {
    String line;
    while ((line = br.readLine()) != null)
      System.out.println(line);
  }
}
