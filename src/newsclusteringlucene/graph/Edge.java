/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package newsclusteringlucene.graph;

/**
 *
 * @author Shadev
 */
public class Edge {
    private Vertex[] vertice = new Vertex[2];
    private double similarityValue = 0; // the similarity of two edge


    /**
     * @return the vertice
     */
    public Vertex[] getVertice() {
        return vertice;
    }

    /**
     * @param vertice the vertice to set
     */
    public void setVertice(Vertex[] vertice) {
        this.vertice = vertice;
    }

    /**
     * @return the value
     */
    public double getSimilarityValue() {
        return similarityValue;
    }

    /**
     * @param value the value to set
     */
    public void setSimilarityValue(double similarityValue) {
        this.similarityValue = similarityValue;
    }

}
