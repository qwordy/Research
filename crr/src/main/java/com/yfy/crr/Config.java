package com.yfy.crr;

import java.util.Arrays;
import java.util.List;

/**
 * Created by yfy on 10/22/16.
 */
public class Config {

  public static String projectsDir = "/home/yfy/stap/projects";

  public static String tmp1 = projectsDir + "/tmp1";

  public static String tmp2 = projectsDir + "/tmp2";

  public static List<String> projects = Arrays.asList("hadoop", "flink",
      "tomcat", "mahout", "cassandra", "luceneSolr", "netty", "guava");

}
