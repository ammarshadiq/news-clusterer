/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newsclusteringlucene.utils;

import newsclusteringlucene.population.LuceneIndexDocument;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import newsclusteringlucene.Configuration;

/**
 * Generate Cluster statistics including terms and phrases. also filters frequent phrases.
 * Only  count the statistics <b> within</b> the cluster.
 * @author shadiq
 */
public class ClusterStat {

  private ArrayList<LuceneIndexDocument> cluster;
  private int numOfDoc; // number of documents
  private ArrayList<String> clusterTerms; // list of terms
  private Map<String, Integer> clusterTermDocumentFrequency; // map of terms and number of document containing the term on cluster documents collection
  private Map<String, Integer> clusterTermFrequency; // map of terms and number of terms in the cluster documents collection
  private ArrayList<String> clusterPhrase; // list of phrases
  private ArrayList<String> clusterFilteredPhrase; // list of filtered phrases (from frequent phrases filter result)
  private Map<String, Integer> clusterPhraseDocumentFrequency; // map of phrases and number of phrases in the cluster documents collection
  private Map<String, Integer> clusterPhraseFrequency; // map of phrases and number of document containing the phrases on cluster documents collection

  public ClusterStat(ArrayList<LuceneIndexDocument> cluster, Configuration appConf) { // documents collection in the cluster
    this.cluster = cluster;
    this.numOfDoc = cluster.size();
    ProcessClusterStatistics(cluster, appConf);
  }

  private void ProcessClusterStatistics(ArrayList<LuceneIndexDocument> cluster, Configuration appConf) { // documents collection in the cluster
    Map<String, Integer> phrasesDocumentFrequency = new HashMap<String, Integer>();
    Map<String, Integer> phraseFrequency = new HashMap<String, Integer>();
    ArrayList<String> phrases = new ArrayList<String>();
    ArrayList<String> filteredPhrases = new ArrayList<String>();

    Map<String, Integer> termDocumentFrequency = new HashMap<String, Integer>();
    Map<String, Integer> termFrequency = new HashMap<String, Integer>();
    ArrayList<String> terms = new ArrayList<String>();

    for (LuceneIndexDocument document : cluster) { // for each document in cluster
      Map<String, Integer> v1Map = document.getPhrasesMap();

      Iterator phraseMapIterator = v1Map.keySet().iterator();
      while (phraseMapIterator.hasNext()) {
        String phrase = (String) phraseMapIterator.next();

        if (phrasesDocumentFrequency.containsKey(phrase)) {
          phrasesDocumentFrequency.put(phrase, phrasesDocumentFrequency.get(phrase) + 1);
        } else {
          phrasesDocumentFrequency.put(phrase, 1);
          phrases.add(phrase);
          filteredPhrases.add(phrase);
        }

        if (phraseFrequency.containsKey(phrase)) {
          phraseFrequency.put(phrase, phraseFrequency.get(phrase) + v1Map.get(phrase));
        } else {
          phraseFrequency.put(phrase, v1Map.get(phrase));
        }
      }

      Map<String, Integer> v2Map = document.getTermFrequencyMap();
      Iterator termMapIterator = v2Map.keySet().iterator();
      while (termMapIterator.hasNext()) {
        String term = (String) termMapIterator.next();

        if (termDocumentFrequency.containsKey(term)) {
          termDocumentFrequency.put(term, termDocumentFrequency.get(term) + 1);
        } else {
          termDocumentFrequency.put(term, 1);
          terms.add(term);
        }

        if (termFrequency.containsKey(term)) {
          termFrequency.put(term, termFrequency.get(term) + v2Map.get(term));
        } else {
          termFrequency.put(term, v2Map.get(term));
        }
      }
    }

    // FREQUENT PHRASES FILTER
    if (appConf.frequentPhraseFilterNumDocMin != 0
            || appConf.frequentPhraseFilterPersentDocMin != 0
            || appConf.frequentPhraseFilterNumDocMax != 0
            || appConf.frequentPhraseFilterPersentDocMax != 0) {
      ArrayList<String> removePhrases = new ArrayList<String>();
      for (String phrase : phrases) { // for each phrase in cluster
        double dfValue = phrasesDocumentFrequency.get(phrase);
        if (!isFrequentPhrase(appConf, dfValue, this.numOfDoc)) {
          removePhrases.add(phrase);
        }
      }
      for (String phrase : removePhrases) { // for each remove phrase
        filteredPhrases.remove(phrase);
      }
    }


    this.clusterTermDocumentFrequency = termDocumentFrequency;
    this.clusterTermFrequency = termFrequency;
    this.clusterTerms = terms;
    this.clusterPhraseDocumentFrequency = phrasesDocumentFrequency;
    this.clusterPhraseFrequency = phraseFrequency;
    this.clusterPhrase = phrases;
    this.clusterFilteredPhrase = filteredPhrases;
  }

  /**
   *  Returns true if the frequent phrases configuration criteria are met, return false if it doesn't.
   * @param appConf
   * @param dfValue
   * @param numOfDoc
   * @return 
   */
  private static boolean isFrequentPhrase(Configuration appConf,
          double dfValue, int numOfDoc) {
    if (appConf.frequentPhraseFilterNumDocMin != 0
            && dfValue < appConf.frequentPhraseFilterNumDocMin) {
      return false;
    }
    if (appConf.frequentPhraseFilterNumDocMax != 0
            && dfValue > appConf.frequentPhraseFilterNumDocMax) {
      return false;
    }

    double persentDoc = (dfValue / numOfDoc) * 100;
    if (appConf.frequentPhraseFilterPersentDocMin != 0
            && persentDoc < appConf.frequentPhraseFilterPersentDocMin) {
      return false;
    }
    if (appConf.frequentPhraseFilterPersentDocMax != 0
            && persentDoc > appConf.frequentPhraseFilterPersentDocMax) {
      return false;
    }
    return true;
  }

  public ArrayList<String> getClusterTerms() {
    return clusterTerms;
  }

  public ArrayList<LuceneIndexDocument> getCluster() {
    return cluster;
  }

  /**
   * returns map of terms and the document frequency of the term within cluster.
   * @return 
   */
  public Map<String, Integer> getClusterTermDocumentFrequency() {
    return clusterTermDocumentFrequency;
  }

  /**
   * returns map of term and the frequency of the term within cluster..
   * @return 
   */
  public Map<String, Integer> getClusterTermFrequency() {
    return clusterTermFrequency;
  }

  /**
   * Get All Extracted Phrases on each document in this cluster
   * @return ArrayList<String>
   */
  public ArrayList<String> getClusterPhrases() {
    return clusterPhrase;
  }
  
  /**
   * Get All Extracted Phrases on each document in this cluster with frequent phrases filter (from frequent phrases filter result)
   * @return ArrayList<String>
   */
  public ArrayList<String> getClusterFilteredPhrases() {
    return clusterFilteredPhrase;
  }

  /**
   *  Get The number of document of each extracted phrases in this cluster
   * @return Map<String, Integer>
   */
  public Map<String, Integer> getClusterPhraseDocumentFrequency() {
    return clusterPhraseDocumentFrequency;
  }

  /**
   * Get the number of appearance  of  each extracted phrases in this cluster
   * @return 
   */
  public Map<String, Integer> getClusterPhraseFrequency() {
    return clusterPhraseFrequency;
  }

  /**
   *  Get the number of document in this cluster
   * @return  int
   */
  public int getNumOfDoc() {
    return numOfDoc;
  }

  /**
   * Get the number of term in this cluster
   * @return int
   */
  public int getNumOfTerm() {
    return clusterTerms.size();
  }

  /**
   * Get the number of phrases in this cluster
   * @return 
   */
  public int getNumOfPhrases() {
    return clusterPhrase.size();
  }
}
