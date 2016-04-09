/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newsclusteringlucene;

import java.io.File;
import newsclusteringlucene.clusterlabeling.numberofwordscaling.ClusterLabelScaling;
import newsclusteringlucene.clusterlabeling.numberofwordscaling.ClusterLabelScalingPoisson;
import newsclusteringlucene.phrasesextractor.PhrasesExtractor;
import newsclusteringlucene.phrasesextractor.ForwardSequential;
import newsclusteringlucene.phrasesextractor.ForwardSequentialConfiguration;

/**
 * @author shadiq
 */
public class Configuration {

  // Clustering Method
  public final static int MERGING_SPECIFIED_TRESHOLD = 0;
  public final static int MERGING_NATURAL_TRESHOLD = 1;
  public final static int MERGING_SPECIFIED_NUMBER_OF_CLUSTER_TRESHOLD = 2;
  public final static int MERGING_MANUAL_BY_TOPIC = 3;
  // Log Type
  public final static int LOG_BASE_E = 0;
  public final static int LOG_BASE_10 = 1;
  public final static int LOG_BASE_2 = 2;
  // Phrase Extractor Method
  public final static int PHRASES_EXTRACTOR_FORWARD_SEQUENTIAL = 0;
  public final static int PHRASES_EXTRACTOR_SIMPLE = 1;
  // Labeling Methods
  public final static int LABELING_MUTUAL_INFORMATION = 0;
  public final static int LABELING_CHI_SQUARE_SELECTION = 1;
  public final static int LABELING_RANK_D_SCORE = 2;
  public final static int LABELING_MAX_TFIDF_BY_CLUSTER = 3;
  public final static int LABELING_MAX_TFIDF_BY_COLLECTION = 4;
  
  // Label Scaling Methods
  public final static int LABELING_SCALING_NONE = 0;
  public final static int LABELING_SCALING_POISSON = 1;
  
  //Lucene Index Folder
  public File luceneIndexFolder;
  public boolean useURLFilter = false;
  public File URLFilterFile;
  
  // Clustering
  public int clusterMergingMethod = MERGING_NATURAL_TRESHOLD;
  public boolean discardStopWords = true;
  public File stopWordsFile = new File("resource/stopwords/stopwords_from_crawled_news.txt");
  
  public File manualTopicFile = new File("resource/url-topic-ground-truth.txt");
  
  public double dendogramCuttingTreshold = 0.045d;
  public int specifiedNumberOfCluster = 10;
  public int clusteringLogBase = LOG_BASE_E;
  
  
  // Phrase Extractor
  public int phraseExtractorMethod = PHRASES_EXTRACTOR_FORWARD_SEQUENTIAL;
  public PhrasesExtractor phrasesExtractor = new ForwardSequential(new ForwardSequentialConfiguration());
  // Frequent Phrase Filter
  public int frequentPhraseFilterNumDocMin = 0;
  public double frequentPhraseFilterPersentDocMin = 0;
  public int frequentPhraseFilterNumDocMax = 0;
  public double frequentPhraseFilterPersentDocMax = 0;
  // Cluster Labeling
  public int clusterLabelingMethod = Configuration.LABELING_MUTUAL_INFORMATION;
  public int clusterLabelingScalingMethod = Configuration.LABELING_SCALING_POISSON;
  public int clusterLabelingLogBase = LOG_BASE_E;
  public double poissonScalingLambda = 4.0d;
  public ClusterLabelScaling clusterLabelScaling = new ClusterLabelScalingPoisson(this.poissonScalingLambda);

  @Override
  public Configuration clone() {
    Configuration c = new Configuration();
    
    c.luceneIndexFolder = this.luceneIndexFolder;
    c.useURLFilter = this.useURLFilter;
    c.URLFilterFile = this.URLFilterFile;    
    
    c.discardStopWords = this.discardStopWords;
    c.stopWordsFile = this.stopWordsFile;
    
    c.manualTopicFile = this.manualTopicFile;

    c.clusterMergingMethod = this.clusterMergingMethod;
    c.dendogramCuttingTreshold = this.dendogramCuttingTreshold;
    c.specifiedNumberOfCluster = this.specifiedNumberOfCluster;
    c.clusteringLogBase = this.clusteringLogBase;
    c.phrasesExtractor = this.phrasesExtractor;

    c.frequentPhraseFilterNumDocMin = this.frequentPhraseFilterNumDocMin;
    c.frequentPhraseFilterPersentDocMin = this.frequentPhraseFilterPersentDocMin;
    c.frequentPhraseFilterNumDocMax = this.frequentPhraseFilterNumDocMax;
    c.frequentPhraseFilterPersentDocMax = this.frequentPhraseFilterPersentDocMax;

    c.clusterLabelingMethod = this.clusterLabelingMethod;
    c.clusterLabelingScalingMethod = this.clusterLabelingScalingMethod;
    c.clusterLabelingLogBase = this.clusterLabelingLogBase;
    c.poissonScalingLambda = this.poissonScalingLambda;
    c.clusterLabelScaling = this.clusterLabelScaling;

    return c;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    if (this.luceneIndexFolder != null) {
      sb.append(this.luceneIndexFolder.getAbsolutePath()).append("\n");
    }

    sb.append("Cluster Merging Method: ").append("\n");
    if (this.clusterLabelingMethod == 0) {
      sb.append(this.clusterLabelingMethod).append(" Merging With Spesific Treshold [").append(this.dendogramCuttingTreshold).append("]\n");
    } else if (this.clusterLabelingMethod == 1) {
      sb.append(this.clusterLabelingMethod).append(" Merging Generated Treshold (Natural Treshold)").append("\n");
    } else if (this.clusterLabelingMethod == 2) {
      sb.append(this.clusterLabelingMethod).append(" Merging With Spesific Number of Cluster [").append(this.specifiedNumberOfCluster).append("]\n");
    }

    return sb.toString();
  }
}
