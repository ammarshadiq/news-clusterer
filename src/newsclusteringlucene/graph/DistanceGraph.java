/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newsclusteringlucene.graph;

import java.util.ArrayList;
import newsclusteringlucene.population.LuceneIndexDocument;

/**
 *
 * @author Shadev
 */
public class DistanceGraph {

  private ArrayList<Edge> edges = new ArrayList<Edge>();
  private ArrayList<Vertex> vertice = new ArrayList<Vertex>();

  public DistanceGraph() {
  }

  ; // singleton
    public DistanceGraph(DistanceGraph dg) {
    for (Edge edge : dg.getEdges()) {
      Edge e = new Edge();
      e.setVertice(edge.getVertice());
      this.edges.add(e);
    }
    for (Vertex ver : dg.getVertice()) {
      Vertex v = new Vertex(ver.getTitle(), ver.isIncludeInClustering());
      for (LuceneIndexDocument pop : ver.getClusterMembers()) {
        v.getClusterMembers().add(pop);
      }
      this.vertice.add(v);
    }
  }

    /**
     * GET the edge that most similar between two verticles
     * @return edge index value
     */
  public int getMaxEdgeIndex() {
    int res = 0;
    double max = 0;
    for (int i = 0; i < this.getEdges().size(); i++) { //iterate through all edges
      double val = this.getEdges().get(i).getSimilarityValue();
      if (val > max) { //replace if val bigger than current bigest.
        max = val;
        res = i;
      }
    }
    return res;
  }
  
  public int getCurrentMaxEdgeIndex() {
    int res = -1;
    double max = 0;
    for (int i = 0; i < this.getEdges().size(); i++) { //iterate through all edges
      double val = this.getEdges().get(i).getSimilarityValue();
      
      boolean a = this.getEdges().get(i).getVertice()[0].isIncludeInClustering();
      boolean b = this.getEdges().get(i).getVertice()[1].isIncludeInClustering();
      
      if (a && b && val > max) { //replace if val bigger than current bigest.
        max = val;
        res = i;
      }
    }
    return res;
  }

// <editor-fold defaultstate="collapsed" desc="comment">
// created by Ammar Shadiq
//    public int getMinEdgeIndex() {
//        int res = 0;
//
//        double max = 0;
//        for (int i = 0; i < this.getEdges().size(); i++) { //iterate through all edges
//            double val = this.getEdges().get(i).getValue();
//            if (val < max) { //replace if val bigger than current bigest.
//                max = val;
//                res = i;
//            }
//        }
//        return res;
//    }// </editor-fold>
  public void printGraph() {
    for (Vertex v : this.getVertice()) {
      for (Edge e : v.getEdges()) {
        String title0 = e.getVertice()[0].getTitle().replaceAll(" ", "_");
        title0 = title0.replaceAll(";", "_");
        title0 = title0.replaceAll(",", "_");
        String title1 = e.getVertice()[1].getTitle().replaceAll(" ", "_");
        title1 = title1.replaceAll(";", "_");
        title1 = title1.replaceAll(",", "_");
        System.out.println(title0 + " " + title1 + " " + e.getSimilarityValue());
      }
    }
  }

  /** 
   * update the graph by merging 2 vertex, 
   * @param toStay
   * @param toRemove 
   */
  public void updateEdges(Vertex toStay, Vertex toRemove) {
    Edge tempEdge = null;
    Vertex tempVertex = null;
    // update the edges
    for (Edge e : toStay.getEdges()) {      // for each edges
      if (e.getVertice()[0] != toStay) {  // if removed
        tempVertex = e.getVertice()[0];
      } else {                            // if stay
        tempVertex = e.getVertice()[1];
      }
      tempEdge = tempVertex.getEdgeToVertex(toRemove);
      e.setSimilarityValue((e.getSimilarityValue() + tempEdge.getSimilarityValue()) / 2);
    }

    // update cluster group members
    ArrayList<LuceneIndexDocument> als = toStay.getClusterMembers();
    ArrayList<LuceneIndexDocument> alr = toRemove.getClusterMembers();
    for (LuceneIndexDocument doc : alr) {
      als.add(doc);
    }
    alr.clear();
    toStay.setClusterMembers(als);

  }

  public void removeVertex(Vertex v) {
    for (Edge e : v.getEdges()) {
      this.getEdges().remove(e);
      if (e.getVertice()[0] != v) {
        e.getVertice()[0].getEdges().remove(e);
      } else {
        e.getVertice()[1].getEdges().remove(e);
      }
    }
    this.getVertice().remove(v);
  }

  /**
   * @return the edges
   */
  public ArrayList<Edge> getEdges() {
    return edges;
  }

  /**
   * @return the vertice
   */
  public ArrayList<Vertex> getVertice() {
    return vertice;
  }
}
