/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newsclusteringlucene.graph;

import newsclusteringlucene.population.LuceneIndexDocument;
import java.util.ArrayList;
import newsclusteringlucene.utils.ClusterStat;

/**
 * Wrapper for the document object. this is the cluster to wrap the containing documents
 * @author Shadev
 */
public class Vertex {

  private String title = null;
  private LuceneIndexDocument pop;
  private ArrayList<Edge> edges = new ArrayList<Edge>();
  private ArrayList<LuceneIndexDocument> clusterMembers = new ArrayList<LuceneIndexDocument>();
  private ClusterStat clusterStatistics;
  private boolean includeInClustering = true;
  private int clusterGroup = 0;

  public int getClusterGroup() {
    return clusterGroup;
  }

  public void setClusterGroup(int clusterGroup) {
    this.clusterGroup = clusterGroup;
  }

  public boolean isIncludeInClustering() {
    return includeInClustering;
  }

  public void setIncludeInClustering(boolean includeInClustering) {
    this.includeInClustering = includeInClustering;
  }

  public Vertex(String name) {
    this.title = name;
  }
  
  public Vertex(String name, boolean includeInClustering) {
    this.title = name;
    this.includeInClustering = includeInClustering;
  }

  public Vertex(LuceneIndexDocument pop) {
    this.title = pop.getTitle();
    this.pop = pop;
    clusterMembers.add(pop);
  }
  
  public Vertex(LuceneIndexDocument pop, boolean includeInClustering) {
    this.title = pop.getTitle();
    this.pop = pop;
    this.includeInClustering = includeInClustering;
    clusterMembers.add(pop);
  }
  
  
  public Vertex(LuceneIndexDocument pop, boolean includeInClustering, int clusterGroup) {
    this.title = pop.getTitle();
    this.pop = pop;
    this.includeInClustering = includeInClustering;
    this.clusterGroup = clusterGroup;
    clusterMembers.add(pop);
  }

  public Edge getEdgeToVertex(Vertex v) {
    Edge res = null;
    for (Edge e : edges) {
      if (e.getVertice()[0] == v || e.getVertice()[1] == v) {
        res = e;
        break;
      }
    }
    return res;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param title the name to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * @return the edges
   */
  public ArrayList<Edge> getEdges() {
    return edges;
  }

  /**
   * @param edges the edges to set
   */
  public void setEdges(ArrayList<Edge> edges) {
    this.edges = edges;
  }

  public LuceneIndexDocument getPop() {
    return this.pop;
  }

  public void setPop(LuceneIndexDocument pop) {
    this.pop = pop;
  }

  public ArrayList<LuceneIndexDocument> getClusterMembers() {
    return clusterMembers;
  }

  public void setClusterMembers(ArrayList<LuceneIndexDocument> clusterMembers) {
    this.clusterMembers = clusterMembers;
  }

  public ClusterStat getClusterStatistics() {
    return clusterStatistics;
  }

  public void setClusterStatistics(ClusterStat clusterStatistics) {
    this.clusterStatistics = clusterStatistics;
  }

  @Override
  public String toString() {
    return title;
  }
}
