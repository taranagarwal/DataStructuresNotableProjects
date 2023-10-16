package hw8;

import hw8.graph.Graph;
import hw8.graph.SparseGraph;
import hw8.spp.DijkstraStreetSearcher;
import hw8.spp.StreetSearcher;
import java.io.File;

public final class Config {
  public String from;
  public String to;
  public File data;

  private Config(String data, String from, String to) {
    this.from = from;
    this.to = to;
    this.data = new File(Config.class.getResource("/" + data).getFile());
  }

  public static Config getConfig() {
    /* Sample valid endpoints */
    return new Config("baltimore.streets.txt", "-76.6214,39.3212", "-76.6383,39.3206");
  }

  /**
   * Change this to experiment with different implementations of Graph ADT.
   *
   * @param <V> Vertex element type.
   * @param <E> Edge element type.
   * @return an implementation of the Graph ADT.
   */
  public static <V, E> Graph<V, E> getGraph() {
    return new SparseGraph<>();
  }

  /**
   * Change this to experiment with different implementations of StreetSearcher.
   *
   * @param graph an implementation of the Graph ADT.
   * @return an implementation of StreetSearcher.
   */
  public static StreetSearcher getStreetSearcher(Graph<String, String> graph) {
    return new DijkstraStreetSearcher(graph);
  }

  @Override
  public String toString() {
    return String.format("Config: %s from %s to %s", data.getName(), from, to);
  }
}
