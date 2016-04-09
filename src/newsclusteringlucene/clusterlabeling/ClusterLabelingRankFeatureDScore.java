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
import java.util.TreeMap;
import newsclusteringlucene.Configuration;
import newsclusteringlucene.clusterlabeling.numberofwordscaling.ClusterLabelScaling;
import newsclusteringlucene.utils.SortingUtil;

/**
 *
 * @author shadiq
 */
public class ClusterLabelingRankFeatureDScore implements ClusterLabeling {

  private ClusterStat clustStat;
  private CollectionStat colStat;
  private Map<String, Double> clusterPhrasesDscore;
  private String[] sortedClusterLabels;

  public ClusterLabelingRankFeatureDScore(Configuration appConf, ClusterStat clustStat, CollectionStat colStat, ClusterLabelScaling scaling) {
    this.clustStat = clustStat;
    this.colStat = colStat;
    int numClustDoc = clustStat.getNumOfDoc(); // get the number of document in the cluster.
    Double normalizer = 0d;
    Map<String, Double> phrasesTFIDF = new HashMap<String, Double>();

    // for each terminology in the cluster. calculate tfidf
    //ArrayList<String> phrases = clustStat.getClusterPhrases(); // non frequent phrases filtered
    ArrayList<String> phrases = clustStat.getClusterFilteredPhrases();
    for (String string : phrases) {
      double tfValue = clustStat.getClusterPhraseFrequency().get(string);
      double dfValue = clustStat.getClusterPhraseDocumentFrequency().get(string);

      double tfidfValue;
      if (appConf.clusterLabelingLogBase == Configuration.LOG_BASE_10) {
        tfidfValue = tfValue * (Math.log10(numClustDoc / (dfValue + 1d)));
      } else if (appConf.clusterLabelingLogBase == Configuration.LOG_BASE_2) {
        tfidfValue = tfValue * (Math.log(numClustDoc / (dfValue + 1d)) / Math.log(2d));
      } else {
        tfidfValue = tfValue * (Math.log(numClustDoc / (dfValue + 1d)));
      }
      normalizer += Math.pow(tfidfValue, 2d);
      phrasesTFIDF.put(string, tfidfValue);
    }

    // normalize
    normalizer = Math.sqrt(normalizer);
    for (String string : phrases) {
      Double tfidfValue = phrasesTFIDF.get(string);
      tfidfValue *= (1d / normalizer); // normalize
      phrasesTFIDF.put(string, tfidfValue);
    }

    // generate RankMap
    Map<String, Integer> dfClusterPhrasesRank = getValueMapRank(clustStat.getClusterPhraseDocumentFrequency());
    Map<String, Integer> dfCollectionPhrasesRank = getValueMapRank(colStat.getCollectionPhrasesDocumentFrequency());
    Map<String, Integer> tfidfClusterPhrasesRank = getValueMapRank(phrasesTFIDF);
    Map<String, Integer> tfidfCollectionPhrasesRank = getValueMapRank(colStat.getCollPhrasesTFIDF());

    // USING ONLY RANK FEATURE HYPHOTHESIS
    // ref : Automatically Labeling Hierarchical Clusters; Treeratpituk, Pucla; Callan, Jamie
    Map<String, Double> clustPhrasesDscore = new TreeMap<String, Double>();
    for (String string : phrases) {
      int dfClustRank = dfClusterPhrasesRank.get(string);
      int dfCollRank = dfCollectionPhrasesRank.get(string);
      int tfidfClustRank = tfidfClusterPhrasesRank.get(string);
      int tfidfCollRank = tfidfCollectionPhrasesRank.get(string);
      int numOfClusterDocs = clustStat.getNumOfDoc();
      int numOfCollDocs = colStat.getNumOfDoc();

      double dScore;
      if (appConf.clusterLabelingLogBase == Configuration.LOG_BASE_10) {
        dScore = 0.122d
                + (0.0000d * (dfClustRank / numOfClusterDocs))
                - (0.0001d * (dfCollRank / numOfCollDocs))
                + (0.0000d * tfidfClustRank)
                - (0.0001d * tfidfCollRank)
                + (0.0509d * (Math.log10(dfCollRank / numOfCollDocs) - Math.log10(dfClustRank / numOfClusterDocs)))
                + (0.1875d * (Math.log10(tfidfCollRank) - Math.log10(tfidfClustRank)));
      } else if (appConf.clusterLabelingLogBase == Configuration.LOG_BASE_2) {
        dScore = 0.122d
                + (0.0000d * (dfClustRank / numOfClusterDocs))
                - (0.0001d * (dfCollRank / numOfCollDocs))
                + (0.0000d * tfidfClustRank)
                - (0.0001d * tfidfCollRank)
                + (0.0509d * ((Math.log(dfCollRank / numOfCollDocs) / Math.log(2d)) - (Math.log(dfClustRank / numOfClusterDocs) / Math.log(2d))))
                + (0.1875d * ((Math.log(tfidfCollRank) / Math.log(2d)) - (Math.log(tfidfClustRank) / Math.log(2d))));
      } else {
        dScore = 0.122d
                + (0.0000d * (dfClustRank / numOfClusterDocs))
                - (0.0001d * (dfCollRank / numOfCollDocs))
                + (0.0000d * tfidfClustRank)
                - (0.0001d * tfidfCollRank)
                + (0.0509d * (Math.log(dfCollRank / numOfCollDocs) - Math.log(dfClustRank / numOfClusterDocs)))
                + (0.1875d * (Math.log(tfidfCollRank) - Math.log(tfidfClustRank)));
      }
      // SCALING
      if (scaling != null) {
        dScore *= scaling.getScale(string.split("\\s").length); //  Number of words Scaling
      }
      clustPhrasesDscore.put(string, dScore);
    }
    this.clusterPhrasesDscore = phrasesTFIDF; // normalized value

    // sort label by highest score
    Map sortedMap = SortingUtil.sortByValueLargeToSmall(this.clusterPhrasesDscore);
    String[] sortedValue = new String[sortedMap.size()];
    Iterator termMapIterator = sortedMap.keySet().iterator();
    int counter = 0;
    while (termMapIterator.hasNext()) {
      String term = (String) termMapIterator.next();
      Object value = sortedMap.get(term);
      sortedValue[counter] = term;
      counter += 1;
    }
    this.sortedClusterLabels = sortedValue;
  }

  private Map<String, Integer> getValueMapRank(Map valueMap) {
    Map<String, Integer> valueMapRank = new TreeMap<String, Integer>();
    Map sortedMap = SortingUtil.sortByValueLargeToSmall(valueMap);
    Iterator termMapIterator = sortedMap.keySet().iterator();
    int counter = 1;
    while (termMapIterator.hasNext()) {
      String term = (String) termMapIterator.next();
      valueMapRank.put(term, counter);
      counter += 1;
    }
    return valueMapRank;
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
    return this.clusterPhrasesDscore;
  }
}
