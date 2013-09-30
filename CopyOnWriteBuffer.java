//package com.centzy.codetest;

import java.util.Iterator;

/**
 * A simple byte buffer that uses copy-on-write.
 * http://en.wikipedia.org/wiki/Copy-on-write
 *
 * @author peter@centzy.com (Peter Edge)
 */
public interface CopyOnWriteBuffer {

  /**
   * Set the value of the buffer to the specified {@code byte} at the specified position.
   *
   * If there is more than one {@code CopyOnWriteBuffer} using this buffer, the buffer
   * must be copied so that the other {@code CopyOnWriteBuffer}s are not affected.
   *
   * @param pos the position to set
   * @param src the {@code byte} to set
   * @throws IllegalArgumentException if {@code pos} is less than 0, or
   *    {@code pos} is greater than or equal to {@link #getCapacity()}
   */
   void set(int pos, byte src) throws IllegalArgumentException;

  /**
   * Set the value of the buffer to the specified {@code byte}s, starting at the
   * specified position within the buffer.
   *
   * If there is more than one {@code CopyOnWriteBuffer} using this buffer, the buffer
   * must be copied so that the other {@code CopyOnWriteBuffer}s are not affected.
   *
   * @param startPos the starting position
   * @param src the {@code byte}s to set
   * @throws IllegalArgumentException if {@code startPos} is less than 0, or
   *    {@code startPos + bytes.length} is greater than {@link #getCapacity()}
   */
   void set(int startPos, byte[] src) throws IllegalArgumentException;

  /**
   * Get the value of the buffer at a specific index
   *
   * This should not cause the {@code CopyOnWriteBuffer} to be copied.
   *
   * @param index the index at which to grab the byte
   * @throws IllegalArguementException if {@code index} is less than 0 or 
   *    {@code index} is greater than the {@link #getCapacity()}
   */
   Byte get(int index) throws IllegalArgumentException;

  /**
   * Zero out the buffer, starting at the specified position and continuing
   * for the specified length.
   *
   * If there is more than one {@code CopyOnWriteBuffer} using this buffer, the buffer
   * must be copied so that the other {@code CopyOnWriteBuffer}s are not affected.
   *
   * @param startPos the starting position
   * @param length the length
   * @throws IllegalArgumentException if {@code startPos} is less than 0, or
   *    {@code startPos + bytes.length} is greater than {@link #getCapacity()}
   */
   void clear(int startPos, int length) throws IllegalArgumentException;

  /**
   * Provides an {@link Iterator} over the buffer.
   *
   * A call to this must not result in the buffer being copied. The returned {@code Iterator}
   * may throw an {@link UnsupportedOperationException} for {@link java.util.Iterator#remove()}.
   *
   * @param startPos the starting position within the buffer
   * @param length the length to iterate over
   * @return a new {@Iterator} over the buffer
   * @throws IllegalArgumentException if {@code startPos} is less than 0, or
   *    {@code startPos + length} is greater than {@link #getCapacity()}
   */
  Iterator<Byte> iterator(int startPos, int length) throws IllegalArgumentException;

  /**
   * Return a copy of {@code this}.
   *
   * Any calls to {@link #set(int, byte)} or {@link #set(int, byte[])} on the original
   * {@code CopyOnWriteBuffer} must not affect this copy, and vice versa.
   *
   * @return a copy of {@code this}
   */
  CopyOnWriteBuffer copy();

  /**
   * @return the capacity
   */
  int getCapacity();
}
