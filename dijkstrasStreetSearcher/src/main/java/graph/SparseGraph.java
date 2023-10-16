package hw8.graph;

import exceptions.InsertionException;
import exceptions.PositionException;
import exceptions.RemovalException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/**
 * An implementation of Graph ADT using incidence lists
 * for sparse graphs where most nodes aren't connected.
 *
 * @param <V> Vertex element type.
 * @param <E> Edge element type.
 */
public class SparseGraph<V, E> implements Graph<V, E> {

  private final HashMap<V, Vertex<V>> vertices;
  private final HashSet<Edge<E>> edges;


  /**
   * Building our SparseGraph object.
   */
  public SparseGraph() {
    vertices = new HashMap<>();
    edges = new HashSet<>();
  }

  // Converts the vertex back to a VertexNode to use internally
  private VertexNode<V> convert(Vertex<V> v) throws PositionException {
    try {
      VertexNode<V> gv = (VertexNode<V>) v;
      if (gv.owner != this) {
        throw new PositionException();
      }
      return gv;
    } catch (NullPointerException | ClassCastException ex) {
      throw new PositionException();
    }
  }

  // Converts and edge back to a EdgeNode to use internally
  private EdgeNode<E> convert(Edge<E> e) throws PositionException {
    try {
      EdgeNode<E> ge = (EdgeNode<E>) e;
      if (ge.owner != this) {
        throw new PositionException();
      }
      return ge;
    } catch (NullPointerException | ClassCastException ex) {
      throw new PositionException();
    }
  }

  @Override
  public Vertex<V> insert(V v) throws InsertionException {
    if (v == null) {
      throw new InsertionException("Cannot insert null values");
    } else if (vertices.containsKey(v)) {
      throw new InsertionException("Cannot insert duplicate values");
    }

    Vertex<V> inserted = new VertexNode<>(v, this);

    vertices.put(v, inserted);

    return inserted;
  }

  @Override
  public Edge<E> insert(Vertex<V> from, Vertex<V> to, E e)
      throws PositionException, InsertionException {
    if (from == null || to == null) {
      throw new PositionException("Cannot have null vertices around edges");
    }
    if (from.equals(to)) {
      throw new InsertionException("Cannot insert a self looping edge");
    }
    VertexNode<V> nodeFrom = convert(from);
    VertexNode<V> nodeTo = convert(to);
    if (!(vertices.containsKey(nodeFrom.data) && vertices.containsKey(nodeTo.data))) {
      throw new PositionException("One or more vertices being connected does not exist");
    }
    duplicateEdgeCheck(nodeFrom, nodeTo);
    Edge<E> inserted = new EdgeNode<>(nodeFrom, nodeTo, e, this);
    nodeFrom.outgoing.add(inserted);
    nodeTo.incoming.add(inserted);
    edges.add(inserted);
    return inserted;
  }

  private void duplicateEdgeCheck(VertexNode<V> nodeFrom, VertexNode<V> nodeTo) {
    for (Edge<E> edge : nodeFrom.outgoing) {
      if (convert(edge).to.equals(nodeTo)) {
        throw new InsertionException("Cannot insert a duplicate edge");
      }
    }
  }

  @Override
  public V remove(Vertex<V> v) throws PositionException, RemovalException {
    if (!vertices.containsValue(v)) {
      throw new PositionException("Vertex does not exist");
    }
    VertexNode<V> vertNode = convert(v);
    if (vertNode.incoming.size() != 0 || vertNode.outgoing.size() != 0) {
      throw new RemovalException("Node still has remaining edges");
    }
    V info = vertNode.data;
    vertices.remove(info);
    return info;
  }

  @Override
  public E remove(Edge<E> e) throws PositionException {
    if (!edges.contains(e)) {
      throw new PositionException("Edge does not exist");
    }
    EdgeNode<E> edgNode = convert(e);

    edgNode.from.outgoing.remove(edgNode);
    edgNode.to.incoming.remove(edgNode);
    edges.remove(e);
    return edgNode.data;
  }

  @Override
  public Iterable<Vertex<V>> vertices() {
    return Collections.unmodifiableCollection(vertices.values());
  }

  @Override
  public Iterable<Edge<E>> edges() {
    return Collections.unmodifiableCollection(edges);
  }

  @Override
  public Iterable<Edge<E>> outgoing(Vertex<V> v) throws PositionException {
    VertexNode<V> vertex = convert(v);
    return Collections.unmodifiableCollection(vertex.outgoing);
  }

  @Override
  public Iterable<Edge<E>> incoming(Vertex<V> v) throws PositionException {
    VertexNode<V> vertex = convert(v);
    return Collections.unmodifiableCollection(vertex.incoming);
  }

  @Override
  public Vertex<V> from(Edge<E> e) throws PositionException {
    return convert(e).from;
  }

  @Override
  public Vertex<V> to(Edge<E> e) throws PositionException {
    return convert(e).to;
  }

  @Override
  public void label(Vertex<V> v, Object l) throws PositionException {
    convert(v).label = l;
  }

  @Override
  public void label(Edge<E> e, Object l) throws PositionException {
    convert(e).label = l;
  }

  @Override
  public Object label(Vertex<V> v) throws PositionException {
    return convert(v).label;
  }

  @Override
  public Object label(Edge<E> e) throws PositionException {
    return convert(e).label;
  }

  @Override
  public void clearLabels() {
    for (Edge<E> edge: edges) {
      convert(edge).label = null;
    }
    for (Vertex<V> vertex: vertices.values()) {
      convert(vertex).label = null;
    }
  }

  @Override
  public String toString() {
    GraphPrinter<V, E> gp = new GraphPrinter<>(this);
    return gp.toString();
  }

  // Class for a vertex of type V
  private final class VertexNode<V> implements Vertex<V> {
    V data;
    Graph<V, E> owner;
    Object label;

    HashSet<Edge<E>> incoming = new HashSet<>();
    HashSet<Edge<E>> outgoing = new HashSet<>();

    VertexNode(V v, Graph<V,E> owner) {
      this.data = v;
      this.label = null;
      this.owner = owner;
    }

    @Override
    public V get() {
      return this.data;
    }
  }

  //Class for an edge of type E
  private final class EdgeNode<E> implements Edge<E> {
    E data;
    Graph<V, E> owner;
    VertexNode<V> from;
    VertexNode<V> to;
    Object label;
    // TODO You may need to add fields/methods here!

    // Constructor for a new edge
    EdgeNode(VertexNode<V> f, VertexNode<V> t, E e, Graph<V, E> owner) {
      this.from = f;
      this.to = t;
      this.data = e;
      this.label = null;
      this.owner = owner;
    }

    @Override
    public E get() {
      return this.data;
    }
  }
}
