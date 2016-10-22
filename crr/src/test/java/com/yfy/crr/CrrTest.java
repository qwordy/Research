package com.yfy.crr;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by yfy on 10/22/16.
 */
public class CrrTest {
  @Test
  public void gitLogParser() throws Exception {
    GitLogParser parser = new GitLogParser();
    parser.parseAll();
  }
}
