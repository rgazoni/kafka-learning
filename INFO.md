# Kafka protocol guide

> Docs: https://kafka.apache.org/protocol.html

## Preliminaries

### Network

Kafka uses a binary protocol over TCP. The client initiates a socket connection and 
then writes a sequence of request messages and reads back the corresponding response 
message. TCP is happier if you maintain persistent connections used for many requests 
to amortize the cost of the TCP handshake, but beyond this penalty connecting is pretty 
cheap.

TCP guarantees that requests will be processed in the order they are sent and responses 
will return in that order as well. requests will be processed in the order they are sent 
and responses will return in that order as well.

Note that clients can (and ideally should) use non-blocking IO to implement request 
pipelining and achieve higher throughput. i.e., clients can send requests even while 
awaiting responses for preceding requests since the outstanding requests will be 
buffered in the underlying OS socket buffer. All requests are initiated by the client, 
and result in a corresponding response message from the server except where noted.

### Partitioning and bootstrapping

Kafka is a partitioned system so not all servers have the complete data set. Instead 
recall that topics are split into a pre-defined number of partitions, P, and each 
partition is replicated with some replication factor, N. Topic partitions themselves 
are just ordered "commit logs" numbered 0, 1, ..., P-1.

To publish messages the client directly addresses messages to a particular 
partition, and when fetching messages, fetches from a particular partition. If two 
clients want to use the same partitioning scheme they must use the same method to 
compute the mapping of key to partition. These requests to publish or fetch data must 
be sent to the broker that is currently acting as the leader for a given partition.
This condition is enforced by the broker, so a request for a particular partition to 
the wrong broker will result in an the NotLeaderForPartition error code.

How can the client...


# Bytes handling - Big Endian
> Docs: https://developer.mozilla.org/en-US/docs/Glossary/Endianness

## Java promoting to int with sign-extension

### Java bytes are signed

- A byte in Java holds a value in the range –128 to +127, using two’s-complement encoding.
- So when you write byte b = (byte)0xAB;, you’re not storing “171” in b—you’re storing the 
two’s-complement interpretation of the bit pattern 10101011, which is –85.

### Widening primitive conversions preserve the numeric value

- The Java spec says that when you convert (widen) a signed byte to an int, it must keep the same 
numeric value (–85), not just copy the raw bits.
- To do that, it “sign-extends” the byte’s top bit into all the new higher bits.

```java
byte b = (byte)0xAB;    // b = 0xAB two’s-complement → –85
int i = b;              // sign‐extend 0xAB to 0xFFFFFFAB → –85
```

- If it instead zero-extended (filled high bits with 0), you’d get 0x000000AB (171), which would change the value from –85 to +171—that breaks the rule that widening preserves the numeric value.

### If you want the unsigned interpretation (0–255), you must mask

- Masking with 0xFF or using Byte.toUnsignedInt(b) tells Java “I want you to treat this byte as if it were unsigned,” zero-extending its bits rather than sign-extending:

````java
byte b = (byte)0xAB;         // bit-pattern 10101011 two’s-complement = –85
int signed  = b;             // sign-extends → 0xFFFFFFAB (–85)
int unsigned = b & 0xFF;     // zero-extends → 0x000000AB (171)
System.out.println(signed);   // prints –85
System.out.println(unsigned); // prints 171
````


