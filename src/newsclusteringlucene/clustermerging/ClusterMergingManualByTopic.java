/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newsclusteringlucene.clustermerging;

import java.util.ArrayList;
import newsclusteringlucene.graph.DistanceGraph;
import newsclusteringlucene.graph.Edge;
import newsclusteringlucene.graph.Vertex;
import newsclusteringlucene.population.LuceneIndexDocument;
import newsclusteringlucene.utils.LuceneDistanceGraphGenerator;

/**
 *
 * @author shadiq
 */
public class ClusterMergingManualByTopic implements ClusterMerging {

  DistanceGraph mergedDistanceGraph;
  LuceneDistanceGraphGenerator generator;

  public ClusterMergingManualByTopic(ArrayList<LuceneIndexDocument> pop,
          LuceneDistanceGraphGenerator generator, DistanceGraph distanceGraph) {
    this.mergedDistanceGraph = distanceGraph;
    this.generator = generator;
  }

  public DistanceGraph getMergedGraph() {
    Vertex v1 = null;
    Vertex v2 = null;
    Edge e1 = null;
    boolean stilOK = true;
    
    while (stilOK){
      boolean found = false;
      for (int i = 0; i < mergedDistanceGraph.getEdges().size(); i++) { //iterate through all edges
        // dont care about distance, as long as it's the same topic, then its merged.
        boolean a = mergedDistanceGraph.getEdges().get(i).getVertice()[0].isIncludeInClustering();
        boolean b = mergedDistanceGraph.getEdges().get(i).getVertice()[1].isIncludeInClustering();
        int ia = mergedDistanceGraph.getEdges().get(i).getVertice()[0].getClusterGroup();
        int ib = mergedDistanceGraph.getEdges().get(i).getVertice()[1].getClusterGroup();

//        if (a && b && ia == ib && ia != 0) {
        if (a && b && ia == ib) {
          e1 = mergedDistanceGraph.getEdges().get(i);
          v1 = e1.getVertice()[0];
          v2 = e1.getVertice()[1];
          mergedDistanceGraph.getEdges().remove(e1);
          mergedDistanceGraph.updateEdges(v1, v2); // merging v1 and v2.
          mergedDistanceGraph.removeVertex(v2);
          found = true;
          break;
        }
      }
      if (!found) stilOK = false;
    }
    return this.mergedDistanceGraph;
  }  
}
