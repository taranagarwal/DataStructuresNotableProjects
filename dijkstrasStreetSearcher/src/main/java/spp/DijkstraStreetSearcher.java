package hw8.spp;

import hw8.graph.Edge;
import hw8.graph.Graph;
import hw8.graph.Vertex;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;


public class DijkstraStreetSearcher extends StreetSearcher {

  /**
   * Creates a StreetSearcher object.
   *
   * @param graph an implementation of Graph ADT.
   */
  public DijkstraStreetSearcher(Graph<String, String> graph) {
    super(graph);
  }

  @Override
  public void findShortestPath(String startName, String endName) {
    Vertex<String> start = vertices.get(startName);
    Vertex<String> end = vertices.get(endName);

    if (start == null) {
      System.out.println("Endpoint: " + startName + " is invalid");
    } else if (end == null) {
      System.out.println("Endpoint: " + endName + " is invalid");
    }
    double totalDist;
    totalDist = findShortestPath(start, end);
    if (totalDist == -1) {
      System.out.println("No path found");
    } else {
      // These method calls will create and print the path for you
      List<Edge<String>> path = getPath(end, start);
      if (VERBOSE) {
        printPath(path, totalDist);
      }
    }
  }

  /**
   * private overloaded method to avoid for checkstyle length.
   * @param start node starting at
   * @param end node ending at
   * @return shortest distance between the 2
   */
  private double findShortestPath(Vertex<String> start, Vertex<String> end) {
    PriorityQueue<VertNode> priorityQueue = new PriorityQueue<>(new VertComparator());
    HashSet<Vertex<String>> checked = new HashSet<>();
    HashMap<Vertex<String>, Double> nodeDistance = new HashMap<>();
    fillMap(nodeDistance, priorityQueue, start);
    while (priorityQueue.size() > 0) {
      VertNode closest = priorityQueue.poll();
      Vertex<String> curNode = closest.vertex();
      Double distToCurNode = closest.nodeDistance();
      if (!(checked.contains(curNode))) {
        checked.add(curNode);

        if (curNode.equals(end)) {
          return distToCurNode;
        }

        checkAdjacent(curNode, distToCurNode, nodeDistance,priorityQueue, checked);
      }
    }

    return -1;
  }

  /**
   * computes distance with adjacent nodes to minimize path length.
   */
  private void checkAdjacent(Vertex<String> curNode, double distToCurNode,
                             HashMap<Vertex<String>, Double> nodeDistance,
                             PriorityQueue<VertNode> priorityQueue, HashSet<Vertex<String>> checked) {
    for (Edge<String> outgoingEdge : graph.outgoing(curNode)) {
      Vertex<String> adj = graph.to(outgoingEdge);
      double distToAdj = (double) graph.label(outgoingEdge);
      double newDist = distToCurNode + distToAdj;

      if (!nodeDistance.containsKey(adj) || newDist < nodeDistance.get(adj)) {
        nodeDistance.put(adj, newDist);
        graph.label(adj, outgoingEdge);
        priorityQueue.offer(new VertNode(newDist, adj));
      }
    }
    checked.add(curNode);
  }

  /**
   * Fills the map and start is made the first in priority queue.
   */
  private void fillMap(HashMap<Vertex<String>, Double> nodeDistance, PriorityQueue<VertNode> priorityQueue,
                       Vertex<String> start) {
    for (Vertex<String> vertex : graph.vertices()) {
      if (vertex.equals(start)) {
        nodeDistance.put(vertex, 0.0);
        priorityQueue.offer(new VertNode(0, vertex));
      } else {
        nodeDistance.put(vertex, MAX_DISTANCE);
      }
    }
  }

  /**
   * Class to compare verticies.
   */
  private static class VertComparator implements Comparator<VertNode> {
    @Override
    public int compare(VertNode a, VertNode b) {
      return a.nodeDistance.compareTo(b.nodeDistance);
    }
  }

  /**
   * Class is used to store distance values in vertexes.
   */
  private static class VertNode {
    private final Vertex<String> vertex;
    private final Double nodeDistance;

    VertNode(double nodeDistance, Vertex<String> vertex) {
      this.vertex = vertex;
      this.nodeDistance = nodeDistance;
    }

    Vertex<String> vertex() {
      return vertex;
    }

    Double nodeDistance() {
      return nodeDistance;
    }
  }
}
