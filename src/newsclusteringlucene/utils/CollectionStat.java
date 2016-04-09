/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newsclusteringlucene.utils;

import newsclusteringlucene.graph.Vertex;
import newsclusteringlucene.population.LuceneIndexDocument;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import newsclusteringlucene.Configuration;

/**
 *
 * @author shadiq
 */
public final class CollectionStat {

  ArrayList<Vertex> collection;
  private int numOfDoc; // number of documents
  private int numOfCluster;
  private ArrayList<String> collectionTerms; // list of terms
  private Map<String, Integer> collectionTermDocumentFrequency; // map of terms and number of document containing the term on cluster documents collection
  private Map<String, Integer> collectionTermClusterFrequency; // map of terms and number of document containing the term on cluster documents collection
  private Map<String, Integer> collectionTermFrequency; // map of terms and number of terms in the cluster documents collection
  private Map<String, Double> collTermTFIDF = new HashMap<String, Double>();
  private ArrayList<String> collectionPhrases; // list of phrases
  private Map<String, Integer> collectionPhrasesDocumentFrequency; // map of phrases and number of phrasesin the clusters documents collection
  private Map<String, Integer> collectionPhrasesClusterFrequency; // map of phrases and number of phrases in the clusters collection
  private Map<String, Integer> collectionPhrasesFrequency; // map of phrases and number of document containing the phrases on cluster documents collection
  private Map<String, Double> collPhrasesTFIDF = new HashMap<String, Double>();

  public CollectionStat(ArrayList<Vertex> collection, Configuration appConf) {
    this.collection = collection;
    this.numOfCluster = collection.size();
    int clusterCounter = 0;
    for (Vertex vertex1 : collection) {
      clusterCounter += vertex1.getClusterMembers().size();
    }
    this.numOfDoc = clusterCounter;
    processCollectionStatistics(collection, appConf);
  }

  public void processCollectionStatistics(ArrayList<Vertex> collection, Configuration appConf) {
    Map<String, Integer> phrasesDocumentFrequency = new HashMap<String, Integer>();
    Map<String, Integer> phrasesClusterFrequency = new HashMap<String, Integer>();
    Map<String, Integer> phrasesFrequency = new HashMap<String, Integer>();
    Map<String, Double> phrasesTFIDF = new HashMap<String, Double>();
    ArrayList<String> phrases = new ArrayList<String>();

    Map<String, Integer> termDocumentFrequency = new HashMap<String, Integer>();
    Map<String, Integer> termClusterFrequency = new HashMap<String, Integer>();
    Map<String, Integer> termFrequency = new HashMap<String, Integer>();
    Map<String, Double> termTFIDF = new HashMap<String, Double>();
    ArrayList<String> terms = new ArrayList<String>();

    for (Vertex cluster : collection) { // root vertex (cluster vertex)
      ArrayList<LuceneIndexDocument> documents = cluster.getClusterMembers();

      // Generate cluster Statisctics if null
      if (cluster.getClusterStatistics() == null) {
        cluster.setClusterStatistics(new ClusterStat(documents, appConf));
      }

      // generate terms cluster frequency
      ArrayList<String> clusterTerms = cluster.getClusterStatistics().getClusterTerms();
      for (String term : clusterTerms) {
        if (termClusterFrequency.containsKey(term)) {
          termClusterFrequency.put(term, termClusterFrequency.get(term) + 1);
        } else {
          termClusterFrequency.put(term, 1);
          terms.add(term);
        }
      }

      // generate phrases cluster frequency
      ArrayList<String> clusterPhrases = cluster.getClusterStatistics().getClusterPhrases();
      for (String phrase : clusterPhrases) {
        if (phrasesClusterFrequency.containsKey(phrase)) {
          phrasesClusterFrequency.put(phrase, phrasesClusterFrequency.get(phrase) + 1);
        } else {
          phrasesClusterFrequency.put(phrase, 1);
          phrases.add(phrase);
        }
      }


      for (LuceneIndexDocument document : documents) { // for each document in the cluster

        // phrases iterator
        Map<String, Integer> v1Map = document.getPhrasesMap();
        Iterator phraseMapIterator = v1Map.keySet().iterator();
        while (phraseMapIterator.hasNext()) { // for each phrase
          String phrs = (String) phraseMapIterator.next();

          if (phrasesDocumentFrequency.containsKey(phrs)) {
            phrasesDocumentFrequency.put(phrs, phrasesDocumentFrequency.get(phrs) + 1);
          } else {
            phrasesDocumentFrequency.put(phrs, 1);
            phrases.add(phrs);
          }

          if (phrasesFrequency.containsKey(phrs)) {
            phrasesFrequency.put(phrs, phrasesFrequency.get(phrs) + v1Map.get(phrs));
          } else {
            phrasesFrequency.put(phrs, v1Map.get(phrs));
          }
        }

        //terms iterator
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
    }

    this.collectionTermDocumentFrequency = termDocumentFrequency;
    this.collectionTermClusterFrequency = termClusterFrequency;
    this.collectionTermFrequency = termFrequency;
    this.collectionTerms = terms;
    this.collectionPhrasesDocumentFrequency = phrasesDocumentFrequency;
    this.collectionPhrasesClusterFrequency = phrasesClusterFrequency;
    this.collectionPhrasesFrequency = phrasesFrequency;
    this.collectionPhrases = phrases;

    // calculate tf-idf score for terms
    Double normalizer = 0d;
    for (String string : terms) {
      double tfValue = collectionTermFrequency.get(string);
      double dfValue = collectionTermDocumentFrequency.get(string);
      double tfidfValue;
      if (appConf.clusteringLogBase == Configuration.LOG_BASE_10) {
        tfidfValue = tfValue * (1d + (Math.log10(numOfDoc / (dfValue + 1d))));
      } else if (appConf.clusteringLogBase == Configuration.LOG_BASE_2) {
        tfidfValue = tfValue * (1d + (Math.log(numOfDoc / (dfValue + 1d)) / Math.log(2d)));
      } else {
        tfidfValue = tfValue * (1d + (Math.log(numOfDoc / (dfValue + 1d))));
      }
      normalizer += Math.pow(tfidfValue, 2d);
      termTFIDF.put(string, tfidfValue);
    }

    normalizer = Math.sqrt(normalizer);
    for (String string : terms) {
      Double tfidfValue = termTFIDF.get(string);
      tfidfValue *= (1d / normalizer); // normalize
      termTFIDF.put(string, tfidfValue);
    }
    this.collTermTFIDF = termTFIDF;

    // calculate tf-idf score for phrases
    normalizer = 0d; // reset normalizer
    for (String string : phrases) {
      double tfValue = collectionPhrasesFrequency.get(string);
      double dfValue = collectionPhrasesDocumentFrequency.get(string);
      double tfidfValue;
      if (appConf.clusteringLogBase == Configuration.LOG_BASE_10) {
        tfidfValue = tfValue * (1d + (Math.log10(numOfDoc / (dfValue + 1d))));
      } else if (appConf.clusteringLogBase == Configuration.LOG_BASE_2) {
        tfidfValue = tfValue * (1d + (Math.log(numOfDoc / (dfValue + 1d)) / Math.log(2d)));
      } else {
        tfidfValue = tfValue * (1d + (Math.log(numOfDoc / (dfValue + 1d))));
      }
      normalizer += Math.pow(tfidfValue, 2d);
      phrasesTFIDF.put(string, tfidfValue);
    }

    normalizer = Math.sqrt(normalizer);
    for (String string : phrases) {
      Double tfidfValue = phrasesTFIDF.get(string);
      tfidfValue *= (1d / normalizer); // normalize
      phrasesTFIDF.put(string, tfidfValue);
    }
    this.collPhrasesTFIDF = phrasesTFIDF;
  }

  public ArrayList<Vertex> getCollection() {
    return collection;
  }

  public Map<String, Integer> getCollectionTermDocumentFrequency() {
    return collectionTermDocumentFrequency;
  }

  public Map<String, Integer> getCollectionTermFrequency() {
    return collectionTermFrequency;
  }

  public ArrayList<String> getCollectionTerms() {
    return collectionTerms;
  }

  public Map<String, Double> getCollPhrasesTFIDF() {
    return collPhrasesTFIDF;
  }

  public Map<String, Double> getCollTermTFIDF() {
    return collTermTFIDF;
  }

  public ArrayList<String> getCollectionPhrases() {
    return collectionPhrases;
  }

  public Map<String, Integer> getCollectionPhrasesDocumentFrequency() {
    return collectionPhrasesDocumentFrequency;
  }

  public Map<String, Integer> getCollectionPhrasesFrequency() {
    return collectionPhrasesFrequency;
  }

  public Map<String, Integer> getCollectionPhrasesClusterFrequency() {
    return collectionPhrasesClusterFrequency;
  }

  public Map<String, Integer> getCollectionTermClusterFrequency() {
    return collectionTermClusterFrequency;
  }

  public int getNumOfCluster() {
    return numOfCluster;
  }

  public int getNumOfDoc() {
    return numOfDoc;
  }

  public int getNumOfTerm() {
    return collectionTerms.size();
  }

  public int getNumOfPhrases() {
    return collectionPhrases.size();
  }
}
