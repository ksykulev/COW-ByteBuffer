//package com.centzy.codetest;

/**
 * Provides {@link CopyOnWriteBuffer}s.
 *
 * @author peter@centzy.com (Peter Edge)
 */
public interface CopyOnWriteBufferProvider {

  /**
   * Create a new zeroed-out {@link CopyOnWriteBuffer} with the specified capacity.
   * Any writes to this {@code CopyOnWriteBuffer} outside the range
   * of the initial capacity will result in exceptions.
   *
   * @param capacity the capacity
   * @return a new {@code CopyOnWriteBuffer}
   */
  CopyOnWriteBuffer create(int capacity);
}
