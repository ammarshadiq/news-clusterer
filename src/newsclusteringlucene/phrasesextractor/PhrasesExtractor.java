/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package newsclusteringlucene.phrasesextractor;

//import newsclusteringlucene.phrasesextractor.postagger.POSToken;
import java.util.ArrayList;

/**
 *
 * @author shadiq
 */
public interface PhrasesExtractor {
    public ArrayList<Phrase> extractPhrases(String text) throws Exception;

    public String getPhrasesExtractorDescription();
}
