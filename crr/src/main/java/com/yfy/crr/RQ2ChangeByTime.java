package com.yfy.crr;

import org.eclipse.jgit.revwalk.RevCommit;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by yfy on 1/2/17.
 * month (concurrent)commit count
 */
public class RQ2ChangeByTime {
  private String project;
  private Map<Integer, Entry> map;

  public RQ2ChangeByTime(String project) {
    this.project = project;
    map = new TreeMap<>();
  }

  public void deal(RevCommit commit, Feature feature) {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    long time = commit.getCommitTime();
    String date = format.format(new Date(time * 1000));
    String[] strs = date.split("-");
    int year = Integer.parseInt(strs[0]);
    int month = Integer.parseInt(strs[1]);
    //Util.log(year + " " + month);

    int key = year * 100 + month;
    if (!map.containsKey(key))
      map.put(key, new Entry(year, month));
    Entry entry = map.get(key);
    entry.count++;
    if (feature.related()) entry.crCount++;
  }

  public void finish() {
    Collection<Entry> entrys = map.values();
    Entry firstEntry = entrys.iterator().next();
    int firstYear = firstEntry.year;
    int firstMonth = firstEntry.month;
    for (Entry e : entrys)
      e.uniMonth = (e.year - firstYear) * 12 + e.month - firstMonth;
    Util.log("year month uniMonth count crCount");
    entrys.forEach(Util::log);
  }

  private static class Entry {
    int year, month, uniMonth, count, crCount;

    Entry(int year, int month) {
      this.year = year;
      this.month = month;
    }

    Entry(int year, int month, int count, int crCount) {
      this.year = year;
      this.month = month;
      this.count = count;
      this.crCount = crCount;
    }

    @Override
    public String toString() {
      return String.format("%d %d %d %d %d",
          year, month, uniMonth, count, crCount);
    }
  }
}
