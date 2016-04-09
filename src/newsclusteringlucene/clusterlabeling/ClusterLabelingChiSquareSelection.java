/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newsclusteringlucene.clusterlabeling;

import newsclusteringlucene.utils.ClusterStat;
import newsclusteringlucene.utils.CollectionStat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import newsclusteringlucene.Configuration;
import newsclusteringlucene.clusterlabeling.numberofwordscaling.ClusterLabelScaling;
import newsclusteringlucene.utils.SortingUtil;

/**
 *
 * @author shadiq
 */
public class ClusterLabelingChiSquareSelection implements ClusterLabeling {

  private ClusterStat clustStat;
  private CollectionStat colStat;
  private Map<String, Double> clustPhrasesChiSquare = new HashMap<String, Double>();
  private String[] sortedClusterLabels;
  
  /**
   * Calculate the Mutual Information of each phrase, using Laplacian Correction to prevent NaN value on Log Operation.
   * @param appConf
   * @param clustStat
   * @param colStat
   * @param scaling 
   */
  public ClusterLabelingChiSquareSelection(Configuration appConf, ClusterStat clustStat, CollectionStat colStat, ClusterLabelScaling scaling) {
    this.clustStat = clustStat;
    this.colStat = colStat;
    double numClustDoc = clustStat.getNumOfDoc(); // get the number of document in the cluster.
    double numColDoc = colStat.getNumOfDoc(); // get the number of document in the collection. 

    // for each phrase in the cluster. calculate Mutual Information Value
    //ArrayList<String> phrases = clustStat.getClusterPhrases(); // non frequent phrases filtered
    ArrayList<String> phrases = clustStat.getClusterFilteredPhrases();
    for (String string : phrases) {

      /* COLLECT THE STAT */

      // get the number of document in the cluster containing the phrase (clustDFphrase).
      double dfClustPhrase = clustStat.getClusterPhraseDocumentFrequency().get(string); // 60

      // get the number of document in the cluster NOT containing the phrase (clustNumOfDocs - clustDFphrase). 
      double dfClustPhraseComplmt = numClustDoc - dfClustPhrase; //200

      // get the number of document in the collection containing the phrase, minus the document in the cluster that contains it.
      double dfColPhrase = colStat.getCollectionPhrasesDocumentFrequency().get(string) - dfClustPhrase; // 10,000

      // get the number of document in the collection NOT containing the phrase, minus the document in the cluster that NOT contains it (colNumOfDocs - colDFphrase).
      double dfColPhraseComplmnt = (numColDoc - numClustDoc) - dfColPhrase; // 500,000

      /* LAPLACIAN CORRECTION */
      numColDoc += 4; // +4 for Laplacian Correction
      dfClustPhrase += 1; // +1 for Laplacian Correction
      dfClustPhraseComplmt += 1; // +1 for Laplacian Correction
      dfColPhrase += 1; // +1 for Laplacian Correction
      dfColPhraseComplmnt += 1; // +1 for Laplacian Correction
      
      double pC1 = (dfClustPhrase + dfClustPhraseComplmt) / numColDoc;
      double pC0 = (dfColPhrase + dfColPhraseComplmnt) / numColDoc;
      double pT1 = (dfClustPhrase + dfColPhrase) / numColDoc;
      double pT0 = (dfClustPhraseComplmt + dfColPhraseComplmnt) / numColDoc;
      
      double E00 = numColDoc * pC0 * pT0;
      double E01 = numColDoc * pC0 * pT1;
      double E10 = numColDoc * pC1 * pT0;
      double E11 = numColDoc * pC1 * pT1;
      
      /*CALCULATE CHI SQUARE VALUE*/
      double chiSquareValue 
              = Math.pow((dfColPhraseComplmnt - E00), 2d) / E00
              + Math.pow((dfColPhrase - E01), 2d) / E01
              + Math.pow((dfClustPhraseComplmt - E10), 2d) / E10
              + Math.pow((dfClustPhrase - E11), 2d) / E11;
      
      /* SCALING */
      if (scaling != null) {
        chiSquareValue *= scaling.getScale(string.split("\\s").length); //  Number of words Scaling
      }
      /* PUT ON MAP */
      this.clustPhrasesChiSquare.put(string, chiSquareValue);
    }
    // sort label by highest score
    Map sortedMap = SortingUtil.sortByValueLargeToSmall(this.clustPhrasesChiSquare);
    String[] sortedValue = new String[sortedMap.size()];
    Iterator phraseMapIterator = sortedMap.keySet().iterator();
    int counter = 0;
    while (phraseMapIterator.hasNext()) {
      String phrase = (String) phraseMapIterator.next();
      sortedValue[counter] = phrase;
      counter += 1;
    }
    this.sortedClusterLabels = sortedValue;
  }
  
  public String getClusterLabel() {
    if (sortedClusterLabels.length > 0) {
      return this.sortedClusterLabels[0]
              + " (" + this.clustStat.getNumOfDoc() + ")";
    } else {
      return "EMPTY_LABEL"
              + " (" + this.clustStat.getNumOfDoc() + ")";
    }
  }

  public String[] getClusterLabels() {
    return this.sortedClusterLabels;
  }

  public int getNumDoc() {
    return clustStat.getNumOfDoc();
  }

  public CollectionStat getCollectionStatistics() {
    return this.colStat;
  }

  public ClusterStat getClusterStatistics() {
    return this.clustStat;
  }

  public Map<String, Double> getClusterLabelsScore() {
    return this.clustPhrasesChiSquare;
  }
}
