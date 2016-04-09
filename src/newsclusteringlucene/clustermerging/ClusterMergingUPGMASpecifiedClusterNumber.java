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
public class ClusterMergingUPGMASpecifiedClusterNumber implements ClusterMerging {

    private DistanceGraph mergedDistanceGraph;
    private int treshold;

    public ClusterMergingUPGMASpecifiedClusterNumber(DistanceGraph distanceGraph, int treshold){
        this.mergedDistanceGraph = distanceGraph;
        this.treshold = treshold;
    }

    // In specified number of cluster, we use number of document (initial cluster) minus treshold.
    // binary merging always do n-1 merge operation, where n is the number of document.
    // for example : we have 125 documents. and to create a complete tree (with one cluster, the root)
    // it has to do 124 times of merging operation (125 - 1 = 124)
    // so if we want to get only 10 cluster out of it, then, 125-10 = 115
    public DistanceGraph getMergedGraph() {
        int timesOfMerging = mergedDistanceGraph.getVertice().size() - treshold;
        int tempMaxIndex = 1;
        Vertex v1 = null;
        Vertex v2 = null;
        Edge e1 = null;

        for (int i = 1; i <= timesOfMerging; i++) {
            tempMaxIndex = mergedDistanceGraph.getMaxEdgeIndex();
            e1 = mergedDistanceGraph.getEdges().get(tempMaxIndex);
            v1 = e1.getVertice()[0];
            v2 = e1.getVertice()[1];
            
            mergedDistanceGraph.getEdges().remove(e1);
            mergedDistanceGraph.updateEdges(v1, v2); // merging v1 and v2.
            mergedDistanceGraph.removeVertex(v2);
            System.out.println("I: "+ i);
        }
        System.out.println("cluster # - cut on: " + e1.getSimilarityValue());

        return this.mergedDistanceGraph;
    }

}
