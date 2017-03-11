package com.yfy.crr;

import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by yfy on 2017/3/10.
 */
public class RQ3 {
  // key, time, count
  private Map<String, Map<Integer, Integer>> addMap, minusMap;

  public RQ3() {
    addMap = new HashMap<>();
    minusMap = new HashMap<>();
  }

  public void add(String word, boolean add, RevCommit commit) {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    long time = commit.getCommitTime();
    String date = format.format(new Date(time * 1000));
    String[] strs = date.split("-");
    int year = Integer.parseInt(strs[0]);
    int month = Integer.parseInt(strs[1]);
    int monthId = year * 12 + month;

    if (add) {
      Map<Integer, Integer> map = addMap.get(word);
      if (map == null) {
        map = new TreeMap<>();
        addMap.put(word, map);
      }
      map.put(monthId, map.getOrDefault(monthId, 0) + 1);
    } else {
      Map<Integer, Integer> map = minusMap.get(word);
      if (map == null) {
        map = new TreeMap<>();
        minusMap.put(word, map);
      }
      map.put(monthId, map.getOrDefault(monthId, 0) + 1);
    }
  }

  public void print() throws Exception {
    for (String key : addMap.keySet()) {
      String filename  = "rq3" + File.separator + key + " add";
      PrintWriter pw = new PrintWriter(filename);
      Map<Integer, Integer> map = addMap.get(key);
      for (int time : map.keySet())
        pw.println(time + " " + map.get(time));
      pw.close();
    }
    for (String key : minusMap.keySet()) {
      String filename  = "rq3" + File.separator + key + " minus";
      PrintWriter pw = new PrintWriter(filename);
      Map<Integer, Integer> map = minusMap.get(key);
      for (int time : map.keySet())
        pw.println(time + " " + map.get(time));
      pw.close();
    }
  }
}
