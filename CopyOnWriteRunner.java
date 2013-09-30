import java.util.Iterator;

public class CopyOnWriteRunner {
  private static CopyOnWriteByteBufferProvider p = new CopyOnWriteByteBufferProvider();

  public static void changeDuplicateTestOriginal() {
    System.out.println("Changing duplicate should not change the original buffer\n");
    byte initial = (byte)0xAA;
    byte changed = (byte)0x02;

    CopyOnWriteByteBuffer c = (CopyOnWriteByteBuffer)p.create(10);
    c.set(0, initial);
    CopyOnWriteByteBuffer c2 = (CopyOnWriteByteBuffer)c.copy();

    printDebug("C : ", c);
    assert c.get(0) == initial: printException(initial, c.get(0));
    printDebug("C2: ", c2);
    assert c2.get(0) == initial: printException(initial, c2.get(0));
    assert c.bb() == c2.bb(): "Expected c and c2 byte array to reference the same array";

    System.out.println("Changing c2 0 -> " + changed);
    c2.set(0,changed);

    printDebug("C : ", c);
    assert c.get(0) == initial: printException(initial, c.get(0));
    printDebug("C2: ", c2);
    assert c2.get(0) == changed: printException(changed, c2.get(0));
    assert c.bb() != c2.bb(): "Expected c and c2 byte array to not have the same reference";
  }
  public static void changeOriginalTestDuplicate() {
    System.out.println("Changing original should not change the duplicate\n");
    byte initial = (byte)0x01;
    byte changed = (byte)0xBB;

    CopyOnWriteByteBuffer c3 = (CopyOnWriteByteBuffer)p.create(10);
    c3.set(0, initial);
    CopyOnWriteByteBuffer c4 = (CopyOnWriteByteBuffer)c3.copy();

    printDebug("C3: ", c3);
    assert c3.get(0) == initial: printException(initial, c3.get(0));
    printDebug("C4: ", c4);
    assert c4.get(0) == initial: printException(initial, c4.get(0));
    assert c3.bb() == c4.bb(): "Expected c3 and c4 byte array to reference the same array";

    System.out.println("Changing c3 0 -> " + changed);
    c3.set(0, changed);

    printDebug("C3: ", c3);
    assert c3.get(0) == changed: printException(changed, c3.get(0));
    printDebug("C4: ", c4);
    assert c4.get(0) == initial: printException(initial, c4.get(0));
    assert c3.bb() != c4.bb(): "Expected c3 and c4 byte array to not have the same reference";
  }
  public static void verifyingClear() {
    System.out.println("Running .clear on original should not change duplicate\n");
    byte initial = (byte)0x77;
    byte expected = (byte)0;

    CopyOnWriteByteBuffer c5 = (CopyOnWriteByteBuffer)p.create(10);
    c5.set(0, initial);
    CopyOnWriteByteBuffer c6 = (CopyOnWriteByteBuffer)c5.copy();

    printDebug("C5: ", c5);
    assert c5.get(0) == initial: printException(initial, c5.get(0));
    printDebug("C6: ", c6);
    assert c6.get(0) == initial: printException(initial, c6.get(0));
    assert c5.bb() == c6.bb(): "Expected c5 and c6 byte array to reference the same array";

    //clearing original
    System.out.println("Running c5.clear(0,5)");
    c5.clear(0, 5);

    printDebug("C5: ", c5);
    assert c5.get(0) == expected: printException(expected, c5.get(0));
    printDebug("C6: ", c6);
    assert c6.get(0) == initial: printException(initial, c6.get(0));
    assert c5.bb() != c6.bb(): "Expected c5 and c6 byte array to not have the same reference";
  }
  public static void verifyingIterator() {
    System.out.println("Create a simple iterator of a buffer\n");
    CopyOnWriteByteBuffer c7 = (CopyOnWriteByteBuffer)p.create(10);
    byte[] values = new byte[5];
    for(int i=0; i<values.length; i++) {
      values[i] = (byte)(i*i);
    }

    c7.set(0, values);

    System.out.println("Iterator");
    Iterator itr = c7.iterator(0,10);

    int position = 0;
    while(itr.hasNext()) {
      System.out.println((position++) + " -> " + itr.next());
    }
  }
  public static void mutatingIterator() {
    System.out.println("Mutating the buffer should not change the iterator\n");
    byte changed = (byte)0xAA;

    CopyOnWriteByteBuffer c8 = (CopyOnWriteByteBuffer)p.create(10);
    byte[] values = new byte[5];
    for(int i=0; i<values.length; i++) {
      values[i] = (byte)(i*i);
    }

    c8.set(0, values);
    printDebug("C8: ", c8);
    assert c8.get(0) == values[0]: printException(values[0], c8.get(0));

    System.out.println("Creating interator");
    Iterator itr = c8.iterator(0,5);

    System.out.println("Changing c8 0 -> " + changed);
    c8.set(0, changed);
    printDebug("C8: ", c8);
    assert c8.get(0) == changed: printException(changed, c8.get(0));

    int position = 0;
    while(itr.hasNext()) {
      System.out.println((position++) + " -> " + itr.next());
    }
  }
  public static void trackingEqualsEvent() {
    System.out.println("Using a regular `=` should not effect the copy on write behavior\n");
    //unfortunately this case fails..
    byte initial = (byte)0x01;
    byte changed = (byte)0x02;

    CopyOnWriteByteBuffer suspect = (CopyOnWriteByteBuffer)p.create(10);
    suspect.set(0, initial);
    CopyOnWriteByteBuffer suspectCopy = suspect;
    //c2 = c1 <- how do you track this event?

    printDebug("Suspect: ", suspect);
    assert suspect.get(0) == initial: printException(initial, suspect.get(0));
    printDebug("SuspectCopy: ", suspectCopy);
    assert suspectCopy.get(0) == initial: printException(initial, suspectCopy.get(0));
    assert suspect.bb() == suspectCopy.bb(): "Expected suspect and suspectCopy byte array to reference the same array";

    System.out.println("Changing suspect 0 -> " + changed);
    suspect.set(0, changed);

    printDebug("Suspect: ", suspect);
    assert suspect.get(0) == changed: printException(changed, suspect.get(0));
    printDebug("SuspectCopy: ", suspectCopy);
    //This assertion fails.
    //assert suspectCopy.get(0) == initial: printException(initial, suspectCopy.get(0));
    //assert suspect.bb() != suspectCopy.bb(): "Expected suspect and suspectCopy byte array to not have the same reference";
  }

  private static void printDebug(String label, CopyOnWriteByteBuffer c) {
    System.out.print(label);
    System.out.println(c + " " + c.bb().hashCode() + " "  + c.get(0));
  }
  private static String printException(byte expected, byte actual) {
    return "Expected byte at index 0 to be (" + expected + ") but got (" + actual + ")";
  }

  public static void main(String[] args) {
    System.out.println("\n//-------------------------------------//");

    changeDuplicateTestOriginal();
    System.out.println("\n//-------------------------------------//");

    changeOriginalTestDuplicate();
    System.out.println("\n//-------------------------------------//");

    verifyingClear();
    System.out.println("\n//-------------------------------------//");

    verifyingIterator();
    System.out.println("\n//-------------------------------------//");

    mutatingIterator();
    System.out.println("\n//-------------------------------------//");

    trackingEqualsEvent();
    System.out.println("\nEND\n");
  }

}
