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
public final class ClusterLabelingMutualInformation implements ClusterLabeling {

  private ClusterStat clustStat;
  private CollectionStat colStat;
  private Map<String, Double> clustPhrasesMI = new HashMap<String, Double>();
  private String[] sortedClusterLabels;

  /**
   * Calculate the Mutual Information of each phrase, using Laplacian Correction to prevent NaN value on Log Operation.
   * @param appConf
   * @param clustStat
   * @param colStat
   * @param scaling 
   */
  public ClusterLabelingMutualInformation(Configuration appConf, ClusterStat clustStat, CollectionStat colStat, ClusterLabelScaling scaling) {
    this.clustStat = clustStat;
    this.colStat = colStat;
    double numClustDoc = clustStat.getNumOfDoc(); // get the number of document in the cluster.
    double numColDoc = colStat.getNumOfDoc(); // get the number of document in the collection. 
    
    // for each phrase in the cluster. calculate Mutual Information Value
    //ArrayList<String> prases = clustStat.getClusterPhrases(); // non frequent phrases filtered
    ArrayList<String> prases = clustStat.getClusterFilteredPhrases();
    for (String string : prases) {

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

      double pC1T1 = dfClustPhrase / numColDoc;
      double pC1T0 = dfClustPhraseComplmt / numColDoc;
      double pC0T1 = dfColPhrase / numColDoc;
      double pC0T0 = dfColPhraseComplmnt / numColDoc;
      
      /*CALCULATE MUTUAL INFORMATION*/
      double mutualInformation;
      double a = pC1T1 / (pC1 * pT1);
      double b = pC1T0 / (pC1 * pT0);
      double c = pC0T1 / (pC0 * pT1);
      double d = pC0T0 / (pC0 * pT0);
      if (appConf.clusterLabelingLogBase == Configuration.LOG_BASE_10) {
        mutualInformation 
                = pC1T1 * Math.log10(a)
                + pC1T0 * Math.log10(b)
                + pC0T1 * Math.log10(c)
                + pC0T0 * Math.log10(d);
      } else if (appConf.clusterLabelingLogBase == Configuration.LOG_BASE_E) {
        mutualInformation 
                = pC1T1 * Math.log(a)
                + pC1T0 * Math.log(b)
                + pC0T1 * Math.log(c)
                + pC0T0 * Math.log(d);
      } else {
        mutualInformation 
                = pC1T1 * (Math.log(a) / Math.log(2d))
                + pC1T0 * (Math.log(b) / Math.log(2d))
                + pC0T1 * (Math.log(c) / Math.log(2d))
                + pC0T0 * (Math.log(d) / Math.log(2d));
      }
      
      /* SCALING */
      if (scaling != null) {
        mutualInformation *= scaling.getScale(string.split("\\s").length); //  Number of words Scaling
      }
      /* PUT ON MAP */
      this.clustPhrasesMI.put(string, mutualInformation);
    }

    // sort label by highest score
    Map sortedMap = SortingUtil.sortByValueLargeToSmall(this.clustPhrasesMI);
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
    return this.clustPhrasesMI;
  }
}
