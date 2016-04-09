/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package newsclusteringlucene.utils;

import newsclusteringlucene.graph.*;
import java.util.ArrayList;
import newsclusteringlucene.population.LuceneIndexDocument;

/**
 *
 * @author Shadev
 */
public class LuceneDistanceGraphGenerator {
    DistanceCalculator luceneDistanceCalculator = null;

    public LuceneDistanceGraphGenerator(DistanceCalculator distanceCalc){
        this.luceneDistanceCalculator = distanceCalc;
    }

    public DistanceCalculator getLuceneDistanceCalculator() {
        return luceneDistanceCalculator;
    }

    public void setLuceneDistanceCalculator(DistanceCalculator luceneDistanceCalculator) {
        this.luceneDistanceCalculator = luceneDistanceCalculator;
    }

    public DistanceGraph generateGraph(ArrayList<LuceneIndexDocument> docsAL){
        DistanceGraph graph = new DistanceGraph();
        this.addVertice(docsAL, graph);
        int i = 0;
        double distance = 0;

        LuceneIndexDocument currentPop = null;
        while (i < docsAL.size()) { //for each population
            currentPop = docsAL.get(i);
            for (int j = i + 1; j < docsAL.size(); j++) {
                distance = this.luceneDistanceCalculator.getDistance(currentPop, docsAL.get(j));
                this.addEdge(graph.getVertice().get(i),
                        graph.getVertice().get(j),
                        distance,
                        graph);
            }
            i++;
        }
        return graph;
    }

    private void addVertice(ArrayList<LuceneIndexDocument> pop, DistanceGraph g){
        for(int i=0; i < pop.size(); i++){
            g.getVertice().add(new Vertex(pop.get(i), pop.get(i).isIncludeInClustering(), pop.get(i).getClusterGroup()));
        }
    }

    private void addEdge(Vertex a, Vertex b, double distance, DistanceGraph g){
        Edge e = new Edge();
        e.setSimilarityValue(distance);
        e.getVertice()[0]=a;
        e.getVertice()[1]=b;
        a.getEdges().add(e);
        b.getEdges().add(e);
        g.getEdges().add(e);
    }
}