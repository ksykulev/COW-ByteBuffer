import java.util.Arrays;
import java.nio.ByteBuffer;

public class CopyOnWriteByteBufferProvider implements CopyOnWriteBufferProvider {

  /**
   * Create a new zeroed-out {@link CopyOnWriteBuffer} with the specified capacity.
   * Any writes to this {@code CopyOnWriteBuffer} outside the range
   * of the initial capacity will result in exceptions.
   *
   * @param capacity the capacity
   * @return a new {@code CopyOnWriteBuffer}
   */
  public CopyOnWriteBuffer create(int capacity){
    byte[] zeros = new byte[capacity];
    Arrays.fill(zeros, (byte)0);

    ByteBuffer bb = ByteBuffer.allocate(zeros.length);
    bb.put(zeros);
    bb.position(0);

    return new CopyOnWriteByteBuffer(bb);
    //Documentation doesn't specifically say that bytebuffers are allocated with 0s
    //return new CopyOnWriteByteBuffer(capacity);
  }

}
