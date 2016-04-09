/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newsclusteringlucene.population;

import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;

/**
 *
 * @author Shadev
 */
public class LuceneIndexDocument {

  private String title = null;
  private String url = null;
  private String dateTime = null;
  private String timestamp = null;
  private String reporter = null;
  private String location = null;
  private String shortDescription = null;
  private String keyWords = null;
  private String pictureURL = null;
  private String content = null;
  private Icon icon = null;
  private Map<String, Integer> termFrequencyMap = new HashMap<String, Integer>();
  private Map<String, Double> tfidfMap = new HashMap<String, Double>();
  private boolean includeInClustering = true;
  private int clusterGroup = 0;

  public int getClusterGroup() {
    return clusterGroup;
  }

  public void setClusterGroup(int clusterGroup) {
    this.clusterGroup = clusterGroup;
  }
  // the number of phrases
  // String is the phrase, Integer is the number of phrase in the document
  private Map<String, Integer> phrasesMap = new HashMap<String, Integer>();
  private double docNormalizer = 0; // L2 norm || d ||

  public LuceneIndexDocument(String docTitle) {
    this.title = docTitle;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param name the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getDateTime() {
    return dateTime;
  }

  public void setDateTime(String dateTime) {
    this.dateTime = dateTime;
  }

  public String getKeyWords() {
    return keyWords;
  }

  public void setKeyWords(String keyWords) {
    this.keyWords = keyWords;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getPictureURL() {
    return pictureURL;
  }

  public void setPictureURL(String pictureURL) {
    this.pictureURL = pictureURL;
  }

  public String getReporter() {
    return reporter;
  }

  public void setReporter(String reporter) {
    this.reporter = reporter;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public void setShortDescription(String shordDescription) {
    this.shortDescription = shordDescription;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * @return the termFrequencyMap
   */
  public Map<String, Integer> getTermFrequencyMap() {
    return termFrequencyMap;
  }

  /**
   * @param termFrequencyMap the termFrequencyMap to set
   */
  public void setTermFrequencyMap(Map<String, Integer> termFrequencyMap) {
    this.termFrequencyMap = termFrequencyMap;
  }

  public Map<String, Double> getTfidfMap() {
    return tfidfMap;
  }

  public void setTfidfMap(Map<String, Double> tfidfMap) {
    this.tfidfMap = tfidfMap;
  }

  public double getDocNormalizer() {
    return docNormalizer;
  }

  public void setDocNormalizer(double docNormalizer) {
    this.docNormalizer = docNormalizer;
  }

  public Map<String, Integer> getPhrasesMap() {
    return phrasesMap;
  }

  public void setPhrasesMap(Map<String, Integer> phrasesMap) {
    this.phrasesMap = phrasesMap;
  }

  public Icon getIcon() {
    return icon;
  }

  public void setIcon(Icon icon) {
    this.icon = icon;
  }

  public boolean isIncludeInClustering() {
    return includeInClustering;
  }

  public void setIncludeInClustering(boolean includeInClustering) {
    this.includeInClustering = includeInClustering;
  }

  @Override
  public String toString() {
    return title;
  }
}