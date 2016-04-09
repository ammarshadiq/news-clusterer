/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package newsclusteringlucene.utils;

import java.util.Iterator;
import newsclusteringlucene.population.LuceneIndexDocument;

/**
 *
 * @author shadiq
 */
public class CosineSimilarityCalc implements DistanceCalculator<LuceneIndexDocument>{

    public double getDistance(LuceneIndexDocument doc1, LuceneIndexDocument doc2) {
        double similiarity = 0d;
        Iterator termMapIterator1 = doc1.getTfidfMap().keySet().iterator(); // for each term
        while (termMapIterator1.hasNext()) { // iterate through all terms in doc B
            String doc1term = (String) termMapIterator1.next();
            double doc1TfIdfWeight = ((Double) doc1.getTfidfMap().get(doc1term)).doubleValue();

            // get the term weight value in doc B
            if(doc2.getTfidfMap().get(doc1term) != null){ // ignore if doc B dont have the same term
                double doc2TfIdfWeight = ((Double) doc2.getTfidfMap().get(doc1term)).doubleValue();
                similiarity += ((doc1TfIdfWeight/doc1.getDocNormalizer())*
                        (doc2TfIdfWeight/doc2.getDocNormalizer()));
            }
        }
        return (similiarity);
    }
}
