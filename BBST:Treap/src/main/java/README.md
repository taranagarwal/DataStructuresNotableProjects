# Discussion

## Unit testing TreapMap

Testing the TreapMap was the most difficult part of this assignment. Even with creating
the seed values, the order of the priorities of every value, along with the interaction 
of the seed values and the key values to make the tree rotate the way I want it to and 
test for specific cases.

My ASCII tree diagrams are written in the corresponding test's body in the TreapMapTestFile.

The first test that was difficult was the insert singleRightRotationInsert(). This, while
being one of the more basic ones was especially difficult due to the way that the random numbers
lined up based on my seed value of 13. To get around this, I had to add, remove, then re-add my
second element so that the third element had the second-smallest priority (first being the second
element and smallest being the root), so that it would rotate right and move the second added element
to the leaf.

The next test was the basic downward rotation of a node to be removed, in this case 2, in the unit test
sinkWithOneChildLeftRotationRemove(). In this, I had to build a tree, and understand the formation of the
tree so that I could have a node 1 level higher than one leaf sink down to the leaf's level by using
the rotation that I was checking for, in this case, the left rotation. Then I had to make sure that the 
leaf was removed properly and not mess with the rest of the tree's structure.

Finally, the data worked pretty cleanly for the double rotations, although I spent a lot of time thinking
about how they would work because they were the first tests that I implemented, more specifically,
the doubleLeftRotationAfterInsertion() test. With this, since my seed value of 13 when it found 4 elements
rerturned the smallest priority value first, the 3rd smallest second, the biggest third, and the second smallest
last, a perfect opportunity for the leaf to rotate up twice, in this case, both times left.

## Benchmarking
Data set: hotel_california.txt
Benchmark                  Mode  Cnt  Score   Error  Units
JmhRuntimeTest.arrayMap    avgt    2  0.769          ms/op
JmhRuntimeTest.avlTreeMap  avgt    2  0.561          ms/op
JmhRuntimeTest.bstMap      avgt    2  0.495          ms/op
JmhRuntimeTest.treapMap    avgt    2  0.588          ms/op

Data set: federalist01.txt
Benchmark                  Mode  Cnt  Score   Error  Units
JmhRuntimeTest.arrayMap    avgt    2  6.659          ms/op
JmhRuntimeTest.avlTreeMap  avgt    2  2.629          ms/op
JmhRuntimeTest.bstMap      avgt    2  2.601          ms/op
JmhRuntimeTest.treapMap    avgt    2  2.960          ms/op

Data set: pride_and_prejudice.txt
Benchmark                  Mode  Cnt     Score   Error  Units
JmhRuntimeTest.arrayMap    avgt    2  1542.569          ms/op
JmhRuntimeTest.avlTreeMap  avgt    2   175.416          ms/op
JmhRuntimeTest.bstMap      avgt    2   181.757          ms/op
JmhRuntimeTest.treapMap    avgt    2   209.097          ms/op

Data set: moby_dick.txt
Benchmark                  Mode  Cnt     Score   Error  Units
JmhRuntimeTest.arrayMap    avgt    2  7184.941          ms/op
JmhRuntimeTest.avlTreeMap  avgt    2   324.591          ms/op
JmhRuntimeTest.bstMap      avgt    2   332.194          ms/op
JmhRuntimeTest.treapMap    avgt    2   468.714          ms/op

My thoughts/justification:

The biggest standout in this data is that the arrayMap was the slowest data
structure out of them all simply due to the fact that it has the highest (slowest) score
through all four experiments, and it was by a significant margin the larger the data set is.
This is not surprising at all, given that the worst case time complexity of an ArrayMap is O(n),
or linear. The most interesting thing that I picked up was the relationship between the avlTreeMap
and the bstMap. In the smaller two data sets, the bstMap outperformed the avlTree map, but it got 
closer together the larger the data set got. In the bigger two, the avlTree map outperformed the
bstTree. My hypothesis is that the balancing in the avlTree is expensive to the point that it does 
not outweigh the benefits of a longer search in the unbalanced tree, whereas when the search gets 
longer in the bigger data sets, the time to balance it is not as significant in the grand scheme of 
things, but makes the searches easier. There is still not a huge difference with the bstMap and 
the avlMap in the larger data sets, but I think that the difference will grow as data sets increase
in size. Finally, the treap was the second-slowest data structure through all four tests, but not
a very large margin between the binary search trees. I think this is because it has a similar 
rotation/balancing process to the avl, but is a little larger due to the randomness. I personally
believe that the avlTree is the most consistently improving and scalable data structure, because it
is still decently fast in the smaller data structures, but becomes the fastest in the larger sets.

