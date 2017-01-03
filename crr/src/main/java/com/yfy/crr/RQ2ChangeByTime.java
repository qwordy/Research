package com.yfy.crr;

import org.eclipse.jgit.revwalk.RevCommit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yfy on 1/2/17.
 * month (concurrent)commit count
 */
public class RQ2ChangeByTime {
  private int count, crCount, lastYear, lastMonth;
  private String project;
  private List<Entry> list;

  public RQ2ChangeByTime(String project) {
    this.project = project;
    list = new ArrayList<>();
  }

  public void deal(RevCommit commit, Feature feature) {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    long time = commit.getCommitTime();
    String date = format.format(new Date(time * 1000));
    String[] strs = date.split("-");
    int year = Integer.parseInt(strs[0]);
    int month = Integer.parseInt(strs[1]);
    //Util.log(year + " " + month);

    if (year != lastYear || month != lastMonth) {
      if (lastYear != 0)
        list.add(new Entry(lastYear, lastMonth, count, crCount));
      lastYear = year;
      lastMonth = month;
      count = crCount = 0;
    }
    count++;
    if (feature.related()) crCount++;
  }

  public void finish() {
    // lastYear, lastMonth is the oldest
    list.add(new Entry(lastYear, lastMonth, count, crCount));
    for (Entry e : list)
      e.uniMonth = (e.year - lastYear) * 12 + e.month - lastMonth;
    list.forEach(Util::log);
  }

  private static class Entry {
    int year, month, uniMonth, count, crCount;

    public Entry(int year, int month, int count, int crCount) {
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
