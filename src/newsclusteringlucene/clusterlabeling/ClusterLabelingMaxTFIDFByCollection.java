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
public class ClusterLabelingMaxTFIDFByCollection implements ClusterLabeling {

  private ClusterStat clustStat;
  private CollectionStat colStat;
  private Map<String, Double> clustPhrasesTFIDF;
  private String[] sortedClusterLabels;

  public ClusterLabelingMaxTFIDFByCollection(Configuration appConf, ClusterStat clustStat,
          CollectionStat colStat, ClusterLabelScaling scaling) {

    this.clustStat = clustStat;
    this.colStat = colStat;
    int numColClust = colStat.getNumOfCluster();

    Double normalizer = 0d;
    Map<String, Double> phrasesTFIDF = new HashMap<String, Double>();

    // for each selected phrases in the cluster.
    //ArrayList<String> phrases = clustStat.getClusterPhrases(); // non frequent phrases filtered
    ArrayList<String> phrases = clustStat.getClusterFilteredPhrases();
    for (String phrase : phrases) {
      double tfValue = colStat.getCollectionPhrasesFrequency().get(phrase);
      double dfValue = colStat.getCollectionPhrasesDocumentFrequency().get(phrase);

      double tfidfValue;
      if (appConf.clusterLabelingLogBase == Configuration.LOG_BASE_10) {
        tfidfValue = tfValue * (Math.log10(numColClust / (dfValue)));
      } else if (appConf.clusterLabelingLogBase == Configuration.LOG_BASE_2) {
        tfidfValue = tfValue * (Math.log(numColClust / (dfValue)) / Math.log(2d));
      } else {
        tfidfValue = tfValue * (Math.log(numColClust / (dfValue)));
      }
      if (scaling != null) {
        tfidfValue *= scaling.getScale(phrase.split("\\s").length); //  Number of words Scaling
      }
      normalizer += Math.pow(tfidfValue, 2d);
      phrasesTFIDF.put(phrase, tfidfValue);
    }

    // normalize
    normalizer = Math.sqrt(normalizer);
    for (String phrase : phrases) {
      Double tfidfValue = phrasesTFIDF.get(phrase);
      tfidfValue *= (1d / normalizer); // normalize
      if (scaling != null) {
        tfidfValue *= scaling.getScale(phrase.split("\\s").length); //  Number of words Scaling
      }
      phrasesTFIDF.put(phrase, tfidfValue);
    }
    this.clustPhrasesTFIDF = phrasesTFIDF; // normalized value

    // sort label by highest score
    Map sortedMap = SortingUtil.sortByValueLargeToSmall(this.clustPhrasesTFIDF);
    String[] sortedValue = new String[sortedMap.size()];
    Iterator phrasesMapIterator = sortedMap.keySet().iterator();
    int counter = 0;
    while (phrasesMapIterator.hasNext()) {
      String phrase = (String) phrasesMapIterator.next();
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
    return this.colStat.getNumOfDoc();
  }

  public CollectionStat getCollectionStatistics() {
    return this.colStat;
  }

  public ClusterStat getClusterStatistics() {
    return this.clustStat;
  }

  public Map<String, Double> getClusterLabelsScore() {
    return this.clustPhrasesTFIDF;
  }
}
