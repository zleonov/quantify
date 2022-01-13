quantify
========

quantify is a *poor* attempt to deal with the [surprisingly difficult](https://en.wikipedia.org/wiki/Wikipedia:Manual_of_Style/Dates_and_numbers#Quantities_of_bytes_and_bits) issue of representing the size of digital quantities. 

Overview
========

It is notoriously hard to deal with the digital units of bytes and bits when representing and converting sizes of the file system and memory.
Do you use [SI](https://en.wikipedia.org/wiki/International_System_of_Units) Units? Do you use the [JEDEC](https://en.wikipedia.org/wiki/JEDEC_memory_standards#JEDEC_Standard_100B.01) Standard? How do you print a human readable string to ``System.out``? It is so hard that [The most copied StackOverflow snippet of all time is flawed](https://programming.guide/worlds-most-copied-so-snippet.html).

With quantify do yourself a favor and write code like this:

```java
final Path file = Paths.get(...);
final long bytes = Files.size(file);
...
final double megabytes = BinaryByteUnit.BYTES.toMegabytes(bytes);
...
System.out.println(BinaryByteUnit.format(megabytes, BinaryByteUnit.MEGABYTES));
```

Goals
=====
- Easy to use for common cases
- A simple, concise, and user-friendly API which caters to 90 percent of application needs
- Java 8 or higher
- **No dependencies** (other than the JDK)
- And more...

What's next?
============
- Publish the project on Maven Central
- Should this project implement the [JSR 385](https://jcp.org/en/jsr/detail?id=385) specification (Units of Measurement API)? 

Can I help?
===========
Yes! Send me your patches and contributions. I will come up with a Contributor License Agreement. 