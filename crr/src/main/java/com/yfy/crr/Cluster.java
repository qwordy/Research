package com.yfy.crr;

import weka.clusterers.SimpleKMeans;
import weka.core.DistanceFunction;
import weka.core.ManhattanDistance;

/**
 * Created by yfy on 11/5/16.
 */
public class Cluster {
  public void run() throws Exception {
    SimpleKMeans km = new SimpleKMeans();
    km.setDistanceFunction(new ManhattanDistance()

  }
}
