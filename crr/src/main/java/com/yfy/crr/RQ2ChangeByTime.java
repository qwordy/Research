package com.yfy.crr;

import org.eclipse.jgit.revwalk.RevCommit;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yfy on 1/2/17.
 */
public class RQ2ChangeByTime {
  private int monthCommitCount, crMonthCommitCount, lastUniqueMonth;
  private String project;

  public RQ2ChangeByTime(String project) {
    this.project = project;
  }

  public void deal(RevCommit commit, Feature feature) {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    long time = commit.getCommitTime();
    String date = format.format(new Date(time * 1000));
    String[] strs = date.split("-");
    int year = Integer.parseInt(strs[0]);
    int month = Integer.parseInt(strs[1]);
    int uniqueMonth = year * 12 + month;
    //Util.log(year + " " + month);

    if (uniqueMonth != lastUniqueMonth) {
      Util.log(monthCommitCount + " " + crMonthCommitCount);
      lastUniqueMonth = uniqueMonth;
      monthCommitCount = crMonthCommitCount = 0;
    }
    monthCommitCount++;
    if (feature.related()) crMonthCommitCount++;
  }
}
