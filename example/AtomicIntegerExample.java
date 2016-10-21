private AtomicInteger ai;

private void inc1() {
  //...
  ai.getAndIncrement();
  //...
}

/**
 * Atomically increments by one the current value.
 *
 * @return the previous value
 */
public final int getAndIncrement() {
  return unsafe.getAndAddInt(this, valueOffset, 1);
}

public final int getAndAddInt(Object var1, long var2, int var4) {
  int var5;
  do {
    var5 = this.getIntVolatile(var1, var2);
  } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));

  return var5;
}

public final native boolean compareAndSwapInt(Object var1, long var2, int var4, int var5);

private final byte[] lock = new byte[0];

private int i;

private void inc2() {
  //...
  synchronized (lock) {
    i++;
  }
  //...
}

