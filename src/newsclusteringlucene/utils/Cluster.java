/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newsclusteringlucene.utils;

import java.util.ArrayList;
import java.util.Map;
import newsclusteringlucene.population.LuceneIndexDocument;

/**
 *
 * @author shadiq
 */
public class Cluster {
  protected String clusterLabel;
  protected String[] clusterLabels;
  protected ClusterStat clustStat;
  protected Map<String, Double> clusterLabelsScore;
  protected ArrayList<LuceneIndexDocument> clusterDocs;

  public Cluster(String label, String[] labels, ArrayList<LuceneIndexDocument> docs) {
    this.clusterLabel = label;
    this.clusterLabels = labels;
    this.clusterDocs = docs;
  }

  public Cluster(String label, String[] labels, Map<String, Double> labelsScores, ArrayList<LuceneIndexDocument> docs, ClusterStat clustStat) {
    this.clusterLabel = label;
    this.clusterLabels = labels;
    this.clusterLabelsScore = labelsScores;
    this.clusterDocs = docs;
    this.clustStat = clustStat;
  }

  public String getClusterLabel() {
    return clusterLabel;
  }

  public String[] getClusterLabels() {
    return clusterLabels;
  }

  public Map<String, Double> getClusterLabelsScore() {
    return this.clusterLabelsScore;
  }

  public ArrayList<LuceneIndexDocument> getClusterDocs() {
    return clusterDocs;
  }

  public ClusterStat getClustStat() {
    return clustStat;
  }

  @Override
  public String toString() {
    return clusterLabel;
  }
}
