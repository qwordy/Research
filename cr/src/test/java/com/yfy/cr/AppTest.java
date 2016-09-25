package com.yfy.cr;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AppTest {

  @Test
  public void parse() throws Exception {
    GitLogParser parser = new GitLogParser();
    parser.parseAll();
  }
}
