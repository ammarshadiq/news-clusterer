/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package newsclusteringlucene.clustermerging;

import newsclusteringlucene.graph.DistanceGraph;
import newsclusteringlucene.graph.Edge;
import newsclusteringlucene.graph.Vertex;

/**
 *
 * @author shadiq
 */
public class ClusterMergingUPGMASpecifiedTreshold implements ClusterMerging {

    DistanceGraph mergedDistanceGraph;
    double treshold;

    public ClusterMergingUPGMASpecifiedTreshold(DistanceGraph distanceGraph, double treshold) {
        this.mergedDistanceGraph = distanceGraph;
        this.treshold = treshold;
    }

    public DistanceGraph getMergedGraph() {
        int tempMaxIndex = 1;
        Vertex v1 = null;
        Vertex v2 = null;
        Edge e1 = null;

        while (mergedDistanceGraph.getVertice().size() > 1) { //merging operation
            tempMaxIndex = mergedDistanceGraph.getCurrentMaxEdgeIndex();
            e1 = mergedDistanceGraph.getEdges().get(tempMaxIndex);
            v1 = e1.getVertice()[0];
            v2 = e1.getVertice()[1];
            if (e1.getSimilarityValue() < this.treshold) { // treshold <-- CUT OPERATION
                System.out.println("treshold - cut on: "+e1.getSimilarityValue());
                break;
            }
            mergedDistanceGraph.getEdges().remove(e1);
            mergedDistanceGraph.updateEdges(v1, v2); // merging v1 and v2.
            mergedDistanceGraph.removeVertex(v2);
        }
        return this.mergedDistanceGraph;
    }
}
