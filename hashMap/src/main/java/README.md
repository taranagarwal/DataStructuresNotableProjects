# Discussion
Development: 
I developed both my open addressing and chaining maps using Entry<K, V> objects to store the 
information for each entry into the map. Both of them stored a K key variable and a V value variable,
but open addressing's entry also stored a tombstone boolean to check if the node had been removed and
chaining's stored a next and previous pointer to aid with collision handling. My open addressing
utilized linear probing to handle collisions. My chaining utilized a next and previous pointer
in my Entry<K, V> nodes to create a doubly linked list to facilitate chaining and removing values. 
In both implementations, I initially used a load value of 0.75 to determine rehashing.

Evaluation:
Ran the open addressing RuntimeTest after initial implementation with load factor of 0.75. The data for space and runtime is:
Benchmark                                                                 (fileName)  Mode  Cnt           Score   Error   Units
JmhRuntimeTest.buildSearchEngine                                          apache.txt  avgt    2         391.307           ms/op
JmhRuntimeTest.buildSearchEngine                                             jhu.txt  avgt    2           0.493           ms/op
JmhRuntimeTest.buildSearchEngine                                          joanne.txt  avgt    2           0.206           ms/op
JmhRuntimeTest.buildSearchEngine                                          newegg.txt  avgt    2         206.650           ms/op
JmhRuntimeTest.buildSearchEngine                                       random164.txt  avgt    2        1622.464           ms/op
JmhRuntimeTest.buildSearchEngine                                            urls.txt  avgt    2           0.052           ms/op
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc               apache.txt  avgt    2   101797176.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                  jhu.txt  avgt    2    16056720.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc               joanne.txt  avgt    2    16215344.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc               newegg.txt  avgt    2    77860176.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc            random164.txt  avgt    2   975585544.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                 urls.txt  avgt    2    15052292.000           bytes
Ran the chaining RuntimeTest after initial implementation with load factor of 0.75. The data for space and runtime is:
Benchmark                                                                 (fileName)  Mode  Cnt           Score   Error   Units
JmhRuntimeTest.buildSearchEngine                                          apache.txt  avgt    2         403.416           ms/op
JmhRuntimeTest.buildSearchEngine                                             jhu.txt  avgt    2           0.427           ms/op
JmhRuntimeTest.buildSearchEngine                                          joanne.txt  avgt    2           0.203           ms/op
JmhRuntimeTest.buildSearchEngine                                          newegg.txt  avgt    2         221.362           ms/op
JmhRuntimeTest.buildSearchEngine                                       random164.txt  avgt    2        2036.107           ms/op
JmhRuntimeTest.buildSearchEngine                                            urls.txt  avgt    2           0.069           ms/op
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc               apache.txt  avgt    2   165094544.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                  jhu.txt  avgt    2    16080528.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc               joanne.txt  avgt    2    16120824.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc               newegg.txt  avgt    2   115751284.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc            random164.txt  avgt    2   990908524.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                 urls.txt  avgt    2    14817140.000           bytes

From these tests, it seems that the data is strikingly similar, but a few things stand out to me. 
One is that the chaining method is slightly slower in larger tests, but the largest difference comes 
into the newegg and apache space difference, because in chaining, the apache.txt and newegg.txt took 
~1.5 times as much space as open addressing. As of now, I would have open addressing as the better method.

Improvements:
In my attempt to improve the code, I am going to reduce the load factor to .5 see if I can pick up any changes.

Open Addressing:
Benchmark                                                                 (fileName)  Mode  Cnt           Score   Error   Units
JmhRuntimeTest.buildSearchEngine                                          apache.txt  avgt    2         448.023           ms/op
JmhRuntimeTest.buildSearchEngine                                             jhu.txt  avgt    2           0.614           ms/op
JmhRuntimeTest.buildSearchEngine                                          joanne.txt  avgt    2           0.192           ms/op
JmhRuntimeTest.buildSearchEngine                                          newegg.txt  avgt    2         206.487           ms/op
JmhRuntimeTest.buildSearchEngine                                       random164.txt  avgt    2        1547.880           ms/op
JmhRuntimeTest.buildSearchEngine                                            urls.txt  avgt    2           0.048           ms/op
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc               apache.txt  avgt    2   141807016.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                  jhu.txt  avgt    2    15550776.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc               joanne.txt  avgt    2    16086456.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc               newegg.txt  avgt    2    98899368.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                 urls.txt  avgt    2    15043584.000           bytes
A load factor of 0.5 seemed to speed up joanne.txt and the longest timed test, random164.txt, 
by a small margin, but had a significant increase in the space taken to build certain search engines, 
so I would say that .75 is better here.

Chaining:
Benchmark                                                                 (fileName)  Mode  Cnt           Score   Error   Units
JmhRuntimeTest.buildSearchEngine                                          apache.txt  avgt    2         424.299           ms/op
JmhRuntimeTest.buildSearchEngine                                             jhu.txt  avgt    2           0.520           ms/op
JmhRuntimeTest.buildSearchEngine                                          joanne.txt  avgt    2           0.195           ms/op
JmhRuntimeTest.buildSearchEngine                                          newegg.txt  avgt    2         242.176           ms/op
JmhRuntimeTest.buildSearchEngine                                       random164.txt  avgt    2        2135.361           ms/op
JmhRuntimeTest.buildSearchEngine                                            urls.txt  avgt    2           0.056           ms/op
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc               apache.txt  avgt    2   145977676.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                  jhu.txt  avgt    2    15813044.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc               joanne.txt  avgt    2    15901296.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc               newegg.txt  avgt    2   116800216.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc            random164.txt  avgt    2  1010247928.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                 urls.txt  avgt    2    15030336.000           bytes
With 0.5 load factor, the runtime statistics are almost the same as the .75 load factor, 
and space complexity also is. However, Time complexity is very slightly better overall with 
.75 but I would go with 0.75. 

Now I am going to increase it to 0.9 to see if increasing the load factor has any effects on 
the speed and space of the code.

Open Addressing:
JmhRuntimeTest.buildSearchEngine                                          apache.txt  avgt    2         581.274           ms/op
JmhRuntimeTest.buildSearchEngine                                             jhu.txt  avgt    2           0.539           ms/op
JmhRuntimeTest.buildSearchEngine                                          joanne.txt  avgt    2           0.205           ms/op
JmhRuntimeTest.buildSearchEngine                                          newegg.txt  avgt    2         230.132           ms/op
JmhRuntimeTest.buildSearchEngine                                       random164.txt  avgt    2        2101.483           ms/op
JmhRuntimeTest.buildSearchEngine                                            urls.txt  avgt    2           0.054           ms/op
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc               apache.txt  avgt    2   109857832.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                  jhu.txt  avgt    2    15985520.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc               joanne.txt  avgt    2    15997540.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc               newegg.txt  avgt    2    79906064.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc            random164.txt  avgt    2  1009176936.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                 urls.txt  avgt    2    14945624.000           bytes

This just seemed to slow down the longer data sets and take up similar space, so it is just a worse implementation.

Chaining:
Benchmark                                                                 (fileName)  Mode  Cnt           Score   Error   Units
JmhRuntimeTest.buildSearchEngine                                          apache.txt  avgt    2         477.653           ms/op
JmhRuntimeTest.buildSearchEngine                                             jhu.txt  avgt    2           0.441           ms/op
JmhRuntimeTest.buildSearchEngine                                          joanne.txt  avgt    2           0.175           ms/op
JmhRuntimeTest.buildSearchEngine                                          newegg.txt  avgt    2         186.369           ms/op
JmhRuntimeTest.buildSearchEngine                                       random164.txt  avgt    2        1871.910           ms/op
JmhRuntimeTest.buildSearchEngine                                            urls.txt  avgt    2           0.059           ms/op
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc               apache.txt  avgt    2   140840592.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                  jhu.txt  avgt    2    16200452.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc               joanne.txt  avgt    2    15833624.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc               newegg.txt  avgt    2   106650848.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc            random164.txt  avgt    2  1032805328.000           bytes
JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                 urls.txt  avgt    2    14847288.000           bytes
This was slightly faster for the larger data sets so I would change this implementation (chaining) to .9 for load factor.

Overall I would stick with 0.75 for open addressing but .9 with chaining due to the mix of speed efficiency and space complexity.

Overall, I would go with open addressing, for the reason that it is marginaly faster for most 
cases than the chaining and in the larger time complexity file random164.txt, open addressing 
performed significantly better (1622.464 ms/op vs 2036.107 ms/op), so overall, I'd call open 
addressing the better collision handler. And the only change I made was increasing load factor 
to .9 for chaining due to faster speeds on the largest data set time wise.