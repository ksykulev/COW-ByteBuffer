Copy-On-Write ByteBuffer
========================

A code challenge.

"A simple interface for a copy-on-write byte buffer is given in Java. Send us an implemenation! There are some simplications - the API is very small, and buffers cannot be resized, but feel free to edit the API as you see fit."


Running
-------
```
$ make

$ java CopyOnWriteRunner #without assertions

$ java -ea CopyOnWriteRunner #with assertions

$ make clean #clean up class files

```

My my has java changed a lot since version 4.. It was like riding a bike that turned into a motorcycle. 


Todos:
- More efficient copies
- Thread safety
