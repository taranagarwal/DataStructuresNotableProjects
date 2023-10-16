# Homework 8

## Discussion 

JHU to Druid Lake:
Driver:
Network Loaded: 27598 roads, 9024 endpoints
Total Distance: 8818.5187
MemoryMonitor:
Used memory: 4998.61 KB (Δ = 0.000)
Instantiating empty Graph data structure
Instantiating empty StreetSearcher object
Used memory: 5064.34 KB (Δ = 65.727)
Loading the network
Used memory: 15823.22 KB (Δ = 10758.883)
Finding the shortest path
Used memory: 15926.28 KB (Δ = 103.063)
Setting objects to null (so GC does its thing!)
Used memory: 6222.70 KB (Δ = -9703.586)
SystemRuntime:
Loading network took 442 milliseconds.
Finding shortest path took 37 milliseconds.

7-11 to Druid Lake
Driver:
Used memory: 4935.34 KB (Δ = 0.000)
Instantiating empty Graph data structure
Instantiating empty StreetSearcher object
Used memory: 4999.44 KB (Δ = 64.102)
Loading the network
Used memory: 15793.54 KB (Δ = 10794.102)
Finding the shortest path
Used memory: 15924.85 KB (Δ = 131.313)
Setting objects to null (so GC does its thing!)
Used memory: 6218.02 KB (Δ = -9706.836)
SystemRuntime:
Loading network took 495 milliseconds.
Finding shortest path took 33 milliseconds.

Inner Harbor to JHU
Driver:
Network Loaded: 27598 roads, 9024 endpoints
Total Distance: 16570.4909
MemoryMonitor:
Used memory: 5000.29 KB (Δ = 0.000)
Instantiating empty Graph data structure
Instantiating empty StreetSearcher object
Used memory: 5069.78 KB (Δ = 69.492)
Loading the network
Used memory: 15819.63 KB (Δ = 10749.852)
Finding the shortest path
Used memory: 15945.43 KB (Δ = 125.797)
Setting objects to null (so GC does its thing!)
Used memory: 6233.47 KB (Δ = -9711.961)
SystenRuntime:
Loading network took 474 milliseconds.
Finding shortest path took 75 milliseconds.

I definitely see some inefficiency within my code. The loading of the network took significantly
longer than finding the path in all cases. I believe this inefficiency comes from my internal 
HashSets that are in my VertexNode class, where I created the incoming and outgoing HashSets for
each vertex whether it had incoming/outgoing edges or not. This feels like a big waste of space
and unnecessary time spent on initializing maps even if they are not used. I believe that this could
be fixed by adding an external HashMap for outgoing and one for incoming, with the key being a 
vertex and the value being a HashSet of edges, which would remove the need for unnecessary maps
being created.
An observation I made is that all of the tests took about the same time to run, which makes
sense to me because most of the time goes into initializing and creating the graph to map every
street in Baltimore. This is also the reason I believe that the space complexity is similar
because all tests have to map out Baltimore street, so it is not surprising at all that these match.
Overall, I believe that my graph ADT has inefficiencies as I described above, but my Dijkstra was
pretty efficient as seen from the relatively faster search times.