import java.util.Iterator;
import java.nio.ByteBuffer;

public class CopyOnWriteByteBuffer implements CopyOnWriteBuffer {

  protected transient ByteBuffer _bb;
  protected ByteBuffer bb() { return _bb; }

  public CopyOnWriteByteBuffer(int capacity) {
    _bb = ByteBuffer.allocate(capacity);
  }
  public CopyOnWriteByteBuffer(ByteBuffer bytes) {
    _bb = bytes;
  }

  public void set(int pos, byte src) {
    if(pos < 0 || pos > getCapacity()){
      throw new IllegalArgumentException();
    }
    //if(isReferencedByOther){
    deepCopy();
    //}
    _bb.put(pos, src);
  }

  public void set(int startPos, byte[] src) {
    if(startPos < 0 || (startPos + src.length) > getCapacity()){
      throw new IllegalArgumentException();
    }
    //if(isReferencedByOther){
    deepCopy();
    //}
    _bb.position(startPos);
    _bb.put(src);
  }

  public Byte get(int index) {
    if(index < 0 || index > getCapacity()) {
      throw new IllegalArgumentException();
    }
    return _bb.get(index);
  }

  public void clear(int startPos, int length) {
    if(startPos < 0 || (startPos + length) > getCapacity()){
      throw new IllegalArgumentException();
    }
    //if(isReferencedByOther){
    deepCopy();
    //}
    _bb.position(startPos);
    for (int i=0; i<length; i++) {
      _bb.put((byte)0);
    }
  }

  public CopyOnWriteBuffer copy() {
    return new CopyOnWriteByteBuffer(bb());
  }

  public synchronized Iterator<Byte> iterator(int startPos, int length) {
    int ending = startPos + length;
    ByteBufferIterator itr;
    int originalPosition = _bb.position(), originalLimit = _bb.limit();
    if(startPos < 0 || ending > getCapacity()) {
      throw new IllegalArgumentException();
    }

    _bb.position(startPos);
    _bb.limit(length);

    itr = new ByteBufferIterator(_bb.slice());

    _bb.position(originalPosition);
    _bb.limit(originalLimit);

    return itr;
  }

  public int getCapacity() {
    return _bb.capacity();
  }

  private void deepCopy() {
    ByteBuffer clone = ByteBuffer.allocate(getCapacity());
    int position = _bb.position(), limit = _bb.limit();

    _bb.position(0).limit(_bb.capacity());

    clone.put(_bb);
    clone.order(_bb.order());
    clone.position(0);

    clone.position(position).limit(limit);

    _bb = clone;
  }

  private class ByteBufferIterator implements Iterator<Byte> {
    final ByteBuffer buff;

    public ByteBufferIterator(ByteBuffer b) {
      buff = b;
    }

    public boolean hasNext() {
      return buff.hasRemaining();
    }

    public Byte next() {
      return buff.get();
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
}

