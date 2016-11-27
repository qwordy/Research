package com.yfy.crr;

/**
 * Created by yfy on 16-11-27.
 * Feature of commit
 */
public class Feature {
  public int msgKey, file, hunk, lineAdd, lineRemove, lineSub, lineSum,
      keyAdd, keyRemove, keySub, keySum, contextKey;

  public boolean related() {
    return msgKey > 0 || keyAdd > 0 || keyRemove > 0;// || contextKey > 0;
  }
}
