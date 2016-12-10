package com.yfy.crr;

/**
 * Created by yfy on 16-11-27.
 * Feature of commit
 */
public class Feature {
  public int msgKey, file, hunk, lineAdd, lineRemove, lineSub, lineSum,
      keyAdd, keyRemove, keySub, keySum, contextKey;

  public boolean related() {
    return msgKey > 0;// || keyAdd > 0 || keyRemove > 0;// || contextKey > 0;
  }

  public String toStr() {
    return String.format("1:%d 2:%d 3:%d 4:%d 5:%d" +
        " 6:%d 7:%d 8:%d 9:%d 10:%d 11:%d 12:%d",
        msgKey, file, hunk, lineAdd, lineRemove, lineSub, lineSum,
        keyAdd, keyRemove, keySub, keySum, contextKey);
  }

}
