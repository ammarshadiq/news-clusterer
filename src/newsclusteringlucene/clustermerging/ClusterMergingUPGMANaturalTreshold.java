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
import newsclusteringlucene.utils.DistanceCalculator;
import newsclusteringlucene.utils.LuceneDistanceGraphGenerator;

/**
 *
 * @author shadiq
 */
public class ClusterMergingUPGMANaturalTreshold implements ClusterMerging {
    
    DistanceGraph mergedDistanceGraph;
    LuceneDistanceGraphGenerator generator;

    public ClusterMergingUPGMANaturalTreshold(ArrayList<LuceneIndexDocument> pop,
            LuceneDistanceGraphGenerator generator, DistanceGraph distanceGraph){
        this.mergedDistanceGraph = distanceGraph;
        this.generator = generator;
    }


    /**
     * On natural clustering each cluster / vertex / node merging is calculated.
     * for getting the value of FDI.
     * since the value of FDI is abstract, we need to do the merging until the end,
     * that is until all of the document cluster are merged. and then calculate the value of FDI of each merger operation.
     * the smaller the value of FDI are preferred.
     * since there are a probability that there the same small value of FDI, the later was taken, since here used <= operator
     * @return
     */
    public DistanceGraph getMergedGraph(){
        int tempMaxIndex = 1;
        Vertex v1 = null;
        Vertex v2 = null;
        Edge e1 = null;

        double selectedFdi = 0d;
        double selectedFdiSimVal = 0d;
        DistanceGraph selectedMergedDistanceGraph = null;

        while (mergedDistanceGraph.getCurrentMaxEdgeIndex() != -1 
                && mergedDistanceGraph.getVertice().size() > 1) { //merging operation
          
            tempMaxIndex = mergedDistanceGraph.getCurrentMaxEdgeIndex();
            e1 = mergedDistanceGraph.getEdges().get(tempMaxIndex);
            v1 = e1.getVertice()[0];
            v2 = e1.getVertice()[1];
            //System.out.println(e1.getValue());
            if (e1.getSimilarityValue() <= 0) { // treshold
                break;
            }
            
            double fdi = getFdi(this.generator.getLuceneDistanceCalculator(), v1, v2);
            //if (fdi <= selectedFdi) { // min (prefer the last MIN)
            if (fdi >= selectedFdi) { // max (prefer the last MAX)
                selectedFdi = fdi;
                selectedFdiSimVal = e1.getSimilarityValue();
                selectedMergedDistanceGraph = new DistanceGraph(mergedDistanceGraph);
            }

            mergedDistanceGraph.getEdges().remove(e1);
            mergedDistanceGraph.updateEdges(v1, v2); // merging v1 and v2.
            mergedDistanceGraph.removeVertex(v2);
        }
        System.out.println("natural - cut on: "+selectedFdiSimVal);
        return selectedMergedDistanceGraph;
    }

    private static double getFdi(DistanceCalculator dcalc, Vertex v1, Vertex v2){
        double fdi = (naturalClusteringCalculation(dcalc, v1, v2)+
                naturalClusteringCalculation(dcalc, v2, v1))/2d;
        return fdi;
    }

    private static double naturalClusteringCalculation(DistanceCalculator dcalc, Vertex v1, Vertex v2) {
        if (v1.getClusterMembers().size() == 1) {
            return 0d;
        }
        double avgDocFitness = 0;
        for (LuceneIndexDocument vd11 : v1.getClusterMembers()) {
            double docSelfClustSim = 0;
            // self cluster -> a(di)
            for (LuceneIndexDocument vd12 : v1.getClusterMembers()) {
                if (!vd11.equals(vd12)) {
                    docSelfClustSim += (1d - dcalc.getDistance(vd11, vd12));
                }
            }
            docSelfClustSim = docSelfClustSim / (v1.getClusterMembers().size() - 1);
            // other cluster b(di)
            double docOtherClustSim = 0;
            for (LuceneIndexDocument vd2 : v2.getClusterMembers()) {
                docOtherClustSim += (1d - dcalc.getDistance(vd11, vd2));
            }
            docOtherClustSim = docOtherClustSim / v1.getClusterMembers().size();
            double compare = Math.max(docSelfClustSim, docOtherClustSim);
            double docFitness = docOtherClustSim - docSelfClustSim / compare;
            avgDocFitness += docFitness;
        }
        return avgDocFitness / (v1.getClusterMembers().size() + v2.getClusterMembers().size());
    }
}
