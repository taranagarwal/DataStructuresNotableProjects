package hw8;

import exceptions.InsertionException;
import exceptions.PositionException;
import exceptions.RemovalException;
import hw8.graph.Edge;
import hw8.graph.Graph;
import hw8.graph.SparseGraph;
import hw8.graph.Vertex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public abstract class GraphTest {

  protected Graph<String, String> graph;

  @BeforeEach
  public void setupGraph() {
    this.graph = createGraph();
  }

  protected abstract Graph<String, String> createGraph();

  @Test
  @DisplayName("insert(v) returns a vertex with given data")
  public void canGetVertexAfterInsert() {
    Vertex<String> v1 = graph.insert("v1");
    assertEquals(v1.get(), "v1");
  }

  @Test
  @DisplayName("insert(U, V, e) returns an edge with given data")
  public void canGetEdgeAfterInsert() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    assertEquals(e1.get(), "v1-v2");
  }

  @Test
  @DisplayName("insert(null, V, e) threw exception")
  public void insertEdgeThrowsPositionExceptionWhenfirstVertexIsNull() {
    try {
      Vertex<String> v = graph.insert("v");
      graph.insert(null, v, "e");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("insert threw exception when inserting duplicate vertex")
  public void insertDuplicateVertex() {
    graph.insert("v");
    try {
      graph.insert("v");
      fail();
    } catch (InsertionException err) {
      System.out.println("Successfully threw exception");
    }
  }

  @Test
  @DisplayName("insert throws an exception when adding a null value")
  public void insertNullVertex() {
    try {
      graph.insert(null);
      fail();
    } catch (InsertionException err) {
      System.out.println("Successfully threw exception");
    }
  }

  @Test
  @DisplayName("Insert self looping edge throws exception")
  public void insertSelfLoopingEdge() {
    Vertex<String> v = graph.insert("v");
    try {
      graph.insert(v, v, "v");
      fail();
    } catch (InsertionException err) {
      System.out.println("Successfully threw exception");
    }
  }

  @Test
  @DisplayName("Inserting duplicate edge throws exception")
  public void insertingDuplicateEdge() {
    Vertex<String> from = graph.insert("v");
    Vertex<String> to = graph.insert("s");
    graph.insert(from, to, "t");
    try {
      graph.insert(from, to, "t");
      fail();
    } catch (InsertionException err) {
      System.out.println("Successfully threw exception");
    }
  }

  @Test
  @DisplayName("Get endpoint from of an edge returns the from Vertex")
  public void getFrom() {
    Vertex<String> from = graph.insert("v");
    Vertex<String> to = graph.insert("s");
    Edge<String> test = graph.insert(from, to, "t");
    assertEquals(from, graph.from(test));
  }

  @Test
  @DisplayName("Get endpoint to of an edge returns the to Vertex")
  public void getTo() {
    Vertex<String> from = graph.insert("v");
    Vertex<String> to = graph.insert("s");
    Edge<String> test = graph.insert(from, to, "t");
    assertEquals(to, graph.to(test));
  }

  @Test
  @DisplayName("Removing a vertex with no edges returns its value and successfully removes")
  public void basicRemoval() {
    Vertex<String> t = graph.insert("t");
    assertEquals("t", graph.remove(t));
    try {
      graph.insert("t");
    } catch (InsertionException ex) {
      fail("InsertionException was thrown");
    }
  }

  @Test
  @DisplayName("Removing non-existent vertex throws exception")
  public void removingNonExistentVertex() {
    Vertex<String> t = graph.insert("t");
    graph.remove(t);
    try {
      graph.remove(t);
      fail();
    } catch (PositionException err) {
      System.out.println("Successfully threw exception");
    }
  }

  @Test
  @DisplayName("Removing vertex with edges throws exception")
  public void removeWithEdges() {
    Vertex<String> t = graph.insert("t");
    Vertex<String> p = graph.insert("p");
    Vertex<String> s = graph.insert("s");
    Edge<String> e1 = graph.insert(t, p, "e1");
    Edge<String> e2 = graph.insert(p, s, "e2");
    try {
      graph.remove(p);
      fail();
    } catch (RemovalException err) {
      System.out.println("Successfully threw exception");
    }
  }

  @Test
  @DisplayName("Removing a null Vertex throws exception")
  public void removeNullVertex() {
    try {
      graph.remove((Vertex<String>) null);
      fail("Expected exception was not thrown");
    } catch (PositionException err) {
      System.out.println("Successfully threw exception");
    }
  }

  @Test
  @DisplayName("Removing an edge returns its value and successfully removes the edge")
  public void basicEdgeRemoval() {
    Vertex<String> t = graph.insert("t");
    Vertex<String> s = graph.insert("s");
    Edge<String> e = graph.insert(t, s, "e");
    assertEquals("e", graph.remove(e));
    for (Edge<String> edg: graph.edges()) {
      if (edg.equals(e)) {
        fail();
      }
    }
    for (Edge<String> edg: graph.outgoing(t)) {
      if (edg.equals(e)) {
        fail();
      }
    }
    for (Edge<String> edg: graph.incoming(s)) {
      if (edg.equals(e)) {
        fail();
      }
    }
  }

  @Test
  @DisplayName("Removing non-existent edge throws exception")
  public void removingNonExistentEdge() {
    Vertex<String> t = graph.insert("t");
    Vertex<String> s = graph.insert("s");
    Edge<String> e = graph.insert(t, s, "e");
    graph.remove(e);
    try {
      graph.remove(e);
      fail();
    } catch (PositionException err) {
      System.out.println("Successfully threw exception");
    }
  }

  @Test
  @DisplayName("Vertex() iterable has all inserted vertices")
  public void vertexIterableHasAllInsertedVertices() {
    Vertex<String> t = graph.insert("t");
    Vertex<String> s = graph.insert("s");
    Vertex<String> p = graph.insert("p");
    Vertex<String> q = graph.insert("q");

    HashSet<Vertex<String>> vertices = new HashSet<>(Arrays.asList(t,s,p,q));

    for (Vertex<String> vertex : graph.vertices()) {
      if (!vertices.contains(vertex)) {
        fail();
      }
      vertices.remove(vertex);
    }

    assertEquals(0, vertices.size());
  }

  @Test
  @DisplayName("Edge() iterable has all inserted edges")
  public void edgeIterableHasAllInsertedEdges() {
    Vertex<String> t = graph.insert("t");
    Vertex<String> s = graph.insert("s");
    Vertex<String> p = graph.insert("p");
    Vertex<String> q = graph.insert("q");

    Edge<String> e1 = graph.insert(t, s, "e1");
    Edge<String> e2 = graph.insert(s, p, "e2");
    Edge<String> e3 = graph.insert(p, q, "e3");

    HashSet<Edge<String>> edges = new HashSet<>(Arrays.asList(e1,e2,e3));

    for (Edge<String> edge : graph.edges()) {
      if (!edges.contains(edge)) {
        fail();
      }
      edges.remove(edge);
    }

    assertEquals(0, edges.size());
  }

  @Test
  @DisplayName("Outgoing() iterable has all outgoing edges")
  public void outgoingIterableHasAllOutgoingEdges() {
    Vertex<String> t = graph.insert("t");
    Vertex<String> s = graph.insert("s");
    Vertex<String> p = graph.insert("p");
    Vertex<String> q = graph.insert("q");

    Edge<String> e1 = graph.insert(t, s, "e1");
    Edge<String> e2 = graph.insert(t, p, "e2");
    Edge<String> e3 = graph.insert(t, q, "e3");

    HashSet<Edge<String>> edges = new HashSet<>(Arrays.asList(e1,e2,e3));

    for (Edge<String> edge : graph.outgoing(t)) {
      if (!edges.contains(edge)) {
        fail();
      }
      edges.remove(edge);
    }
    assertEquals(0, edges.size());
  }

  @Test
  @DisplayName("Incoming() iterable has all outgoing edges")
  public void incomingIterableHasAllIncomingEdges() {
    Vertex<String> t = graph.insert("t");
    Vertex<String> s = graph.insert("s");
    Vertex<String> p = graph.insert("p");
    Vertex<String> q = graph.insert("q");

    Edge<String> e1 = graph.insert(s, t, "e1");
    Edge<String> e2 = graph.insert(p, t, "e2");
    Edge<String> e3 = graph.insert(q, t, "e3");

    HashSet<Edge<String>> edges = new HashSet<>(Arrays.asList(e1,e2,e3));

    for (Edge<String> edge : graph.incoming(t)) {
      if (!edges.contains(edge)) {
        fail();
      }
      edges.remove(edge);
    }
    assertEquals(0, edges.size());
  }

  @Test
  @DisplayName("Label(vertex) returns null if no label exists")
  public void labelVertexReturnsNull() {
    Vertex<String> t = graph.insert("t");
    assertNull(graph.label(t));
  }

  @Test
  @DisplayName("Label(edge) returns null if no label exists")
  public void labelEdgeReturnsNull() {
    Vertex<String> t = graph.insert("t");
    Vertex<String> s = graph.insert("s");
    Edge<String> e1 = graph.insert(t, s, "e1");
    assertNull(graph.label(e1));
  }

  @Test
  @DisplayName("Label(vertex, obj) correctly labels and label(vertex) returns correct label")
  public void vertexAddingAndReturningLabel() {
    Vertex<String> t = graph.insert("t");
    graph.label(t, "Hello");
    assertEquals("Hello", graph.label(t));
    graph.label(t, 18);
    assertEquals(18, graph.label(t));
  }

  @Test
  @DisplayName("Label(edge, obj) correctly labels and label(edge) returns correct label")
  public void edgeAddingAndReturningLabel() {
    Vertex<String> t = graph.insert("t");
    Vertex<String> s = graph.insert("s");
    Edge<String> e1 = graph.insert(t, s, "e1");
    graph.label(e1, "Hello");
    assertEquals("Hello", graph.label(e1));
    graph.label(e1, 18);
    assertEquals(18, graph.label(e1));
  }

  @Test
  @DisplayName("clearLabels() sets all vertex and edge labels to null")
  public void clearLabelsResetsNull() {
    Vertex<String> t = graph.insert("t");
    Vertex<String> s = graph.insert("s");
    Edge<String> e1 = graph.insert(t, s, "e1");

    graph.label(t, "I");
    graph.label(s, "Like");
    graph.label(s, "CS");

    graph.clearLabels();

    assertNull(graph.label(t));
    assertNull(graph.label(s));
    assertNull(graph.label(e1));
  }

  @Test
  @DisplayName("Test that from() gives correct vertex")
  public void basicFromTest() {
    Vertex<String> t = graph.insert("t");
    Vertex<String> s = graph.insert("s");
    Edge<String> e1 = graph.insert(t, s, "e1");

    assertEquals(t, graph.from(e1));
  }

  @Test
  @DisplayName("Test that to() gives correct vertex")
  public void basicToTest() {
    Vertex<String> t = graph.insert("t");
    Vertex<String> s = graph.insert("s");
    Edge<String> e1 = graph.insert(t, s, "e1");

    assertEquals(s, graph.to(e1));
  }
}
