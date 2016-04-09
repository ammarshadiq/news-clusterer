/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newsclusteringlucene.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import newsclusteringlucene.phrasesextractor.PhrasesExtractor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import newsclusteringlucene.Configuration;
import newsclusteringlucene.phrasesextractor.Phrase;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.store.FSDirectory;
import newsclusteringlucene.population.LuceneIndexDocument;
import org.apache.lucene.document.Field;

/**
 *
 * @author Shadev
 */
public class LuceneIndexParser {

  IndexReader reader;
  Map docMap = new HashMap();
  Configuration appConfig;
  private HashSet<String> stopWords; // stopwords would be often searched, so its hashed.
  private HashSet<String> includedURLList; // would be often searched, so its hashed.
  private HashMap<String, Integer> URLTopicClusterList; // would be often searched, so its hashed.

  public LuceneIndexParser(String location, Configuration appConfig) throws FileNotFoundException, IOException {
    FSDirectory index = FSDirectory.open(new File(location));
    reader = IndexReader.open(index);
    if (appConfig.useURLFilter) {
      includedURLList = getURLList(appConfig.URLFilterFile);
    }
    this.appConfig = appConfig;
  }

  public void closeLuceneIndex() {
    try {
      reader.close();
    } catch (IOException ex) {
      Logger.getLogger(LuceneIndexParser.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public ArrayList<LuceneIndexDocument> getDocuments() throws FileNotFoundException, IOException {

    int docCount = reader.maxDoc();
    ArrayList<LuceneIndexDocument> pop = new ArrayList<LuceneIndexDocument>();
    System.out.println("num of LuceneDocs: " + docCount);
    // Creating a temporary configuration file before compressing it
    File configFile = new File("index.csv");
    BufferedWriter bw;
    try {
      bw = new BufferedWriter(new FileWriter(configFile));



      // for all of the documents
      int counter = 0;
      for (int i = 0; i < docCount; i++) { // iterate through all docs
        if (!reader.isDeleted(i) && reader.getTermFreqVector(i, "content") != null) { // if document is not deleted
          Document doc = reader.document(i);
          String title = doc.get("title");
          LuceneIndexDocument lucenePop = new LuceneIndexDocument(title);
          lucenePop.setUrl(doc.get("url"));
          lucenePop.setDateTime(doc.get("datetime"));
          lucenePop.setTimestamp(doc.get("tstamp"));
          lucenePop.setReporter(doc.get("reporter"));
          lucenePop.setLocation(doc.get("location"));
          lucenePop.setShortDescription(doc.get("description"));
          lucenePop.setKeyWords(doc.get("keywords"));
          lucenePop.setPictureURL(doc.get("picture"));
          lucenePop.setContent(doc.get("content"));


          if (appConfig.useURLFilter) {
            if (!checkUrllist(lucenePop.getUrl())) {
              lucenePop.setIncludeInClustering(false);
            }
          }
          if (appConfig.clusterMergingMethod == Configuration.MERGING_MANUAL_BY_TOPIC) {
            // get topic number
            lucenePop.setClusterGroup(getManualCluster(lucenePop.getUrl().trim()));
          }

          String A = lucenePop.getTitle();
          String B = lucenePop.getUrl();
          String C = lucenePop.getDateTime();
          String D = lucenePop.getTimestamp();
          String E = lucenePop.getReporter();
          String F = lucenePop.getLocation();
          String G = lucenePop.getShortDescription();
          String H = lucenePop.getKeyWords();
          String I = lucenePop.getPictureURL();
          String J = lucenePop.getContent();
          int K = lucenePop.getClusterGroup();

          String cccc =
                  i + " | "
                  + A + " | "
                  + B + " | "
                  + C + " | "
                  + D + " | "
                  + E + " | "
                  + F + " | "
                  + G + " | "
                  + H + " | "
                  + I + " | "
                  + J + " | "
                  + K;

          bw.write(cccc + "\n");


          // SELECT ICON FOR NODE
          Pattern patternAntara = Pattern.compile("http://www.antaranews.com/berita/\\d+/.*");
          Pattern patternKompas1 = Pattern.compile("http://nasional.kompas.com/read/\\d+/\\d+/\\d+/\\d+/.*");
          Pattern patternRepublika = Pattern.compile("http://www.republika.co.id/berita/nasional/.*/\\d+/\\d+/\\d+/.*");
          Pattern patternVivanews = Pattern.compile("http://nasional.vivanews.com/news/read/.*");


          Matcher matcher = patternVivanews.matcher(lucenePop.getUrl().toString());
          if (matcher.matches()) {
            lucenePop.setIcon(org.jdesktop.application.Application.getInstance(newsclusteringlucene.NewsClusteringLuceneApp.class).
                    getContext().
                    getResourceMap(newsclusteringlucene.NewsClusteringLuceneView.class).
                    getIcon("NewsFavIcon.vivanews"));
          }
          matcher = patternKompas1.matcher(lucenePop.getUrl().toString());
          if (matcher.matches()) {
            lucenePop.setIcon(org.jdesktop.application.Application.getInstance(newsclusteringlucene.NewsClusteringLuceneApp.class).
                    getContext().
                    getResourceMap(newsclusteringlucene.NewsClusteringLuceneView.class).
                    getIcon("NewsFavIcon.kompas"));
          }
          matcher = patternRepublika.matcher(lucenePop.getUrl().toString());
          if (matcher.matches()) {
            lucenePop.setIcon(org.jdesktop.application.Application.getInstance(newsclusteringlucene.NewsClusteringLuceneApp.class).
                    getContext().
                    getResourceMap(newsclusteringlucene.NewsClusteringLuceneView.class).
                    getIcon("NewsFavIcon.republika"));
          }
          matcher = patternAntara.matcher(lucenePop.getUrl().toString());
          if (matcher.matches()) {
            lucenePop.setIcon(org.jdesktop.application.Application.getInstance(newsclusteringlucene.NewsClusteringLuceneApp.class).
                    getContext().
                    getResourceMap(newsclusteringlucene.NewsClusteringLuceneView.class).
                    getIcon("NewsFavIcon.antara"));
          }

          // CALCULATE TERM FREQUENCY (TF)
          Map<String, Integer> tfMap = new HashMap<String, Integer>();
          TermFreqVector termFreqVector = reader.getTermFreqVector(i, "content");

          counter += 1;
          addTermFreqToMap(tfMap, termFreqVector);

          lucenePop.setTermFrequencyMap(tfMap);

          // CALCULATE TERM FREQUENCY - INVERSE DOCUMENT FREQUENCY (TF-IDF)
          Map<String, Double> tfidfMap = new HashMap<String, Double>();
          double docNorm = 0;

          Iterator termMapIterator = tfMap.keySet().iterator();
          while (termMapIterator.hasNext()) { // iterate through all term in doc/tfMap
            String term = (String) termMapIterator.next(); // sorted alphabeticaly                    
            int tf = ((Integer) tfMap.get(term)).intValue();
            double tfidf;
            if (this.appConfig.clusteringLogBase == Configuration.LOG_BASE_10) {
              tfidf = (tf * (Math.log10(docCount / ((reader.docFreq(new Term("content", term))))))); // f(d,t)*(Nd/df(t))
            } else if (this.appConfig.clusteringLogBase == Configuration.LOG_BASE_2) {
              //double tfidf = (tf * (Math.log(docCount / ((reader.docFreq(new Term("title", term))))))); // f(d,t)*1+log(Nd/df(t)+1)
              tfidf = (tf * (Math.log(docCount / ((reader.docFreq(new Term("content", term))))) / Math.log(2d))); // f(d,t)*(Nd/df(t))
            } else {
              //double tfidf = (tf * (Math.log(docCount / ((reader.docFreq(new Term("title", term))))))); // f(d,t)*1+log(Nd/df(t)+1)
              tfidf = (tf * (Math.log(docCount / ((reader.docFreq(new Term("content", term))))))); // f(d,t)*(Nd/df(t))
            }
            tfidfMap.put(term, tfidf);
            docNorm += Math.pow(tfidf, 2d);
          }
          lucenePop.setTfidfMap(tfidfMap);

          // document norm  (vector length or L2 norm) || d ||
          lucenePop.setDocNormalizer(Math.sqrt(docNorm)); // == akar (sigma(tfidf^2))

          // Extract the raw text
//        Field f = reader.document(i).getField("description");
          Field f = reader.document(i).getField("content");
          String text = "";
//        if (f != null) text = f.stringValue().substring(0, 150).toLowerCase(Locale.US);
          if (f != null) {
            text = f.stringValue().toLowerCase(Locale.US);
          }

          // EXTRACT PHRASES FROM RAW TEXT
          PhrasesExtractor pe = appConfig.phrasesExtractor;
          ArrayList<Phrase> phrases = null;
          try {
            phrases = pe.extractPhrases(text);
          } catch (Exception ex) {
            Logger.getLogger(LuceneIndexParser.class.getName()).log(Level.SEVERE, null, ex);
          }

          // MAP PHRASES
          Map<String, Integer> phrasesMap = new HashMap<String, Integer>();
          if (phrases != null) {
            for (Phrase p : phrases) {
              String string = p.toString();
              if (phrasesMap.containsKey(string)) {
                phrasesMap.put(string, phrasesMap.get(string) + 1);
              } else {
                phrasesMap.put(string, 1);
              }
            }
          }

          lucenePop.setPhrasesMap(phrasesMap);

          pop.add(lucenePop); // add doc to population
        }
      }



      System.out.println("Processed lucene Doc: " + counter);
      if (counter < docCount) {
        System.out.println("there are " + (docCount - counter)
                + " document not processed, probably because it has been deleted.");
      }

      bw.close();
    } catch (IOException ex) {
    }

    return pop;
  }

  /**
   *  Mapping the term, including stopwords removal (if discard stopwords option on config are set to true).
   * @param vectorMap
   * @param termFreqVector 
   */
  private void addTermFreqToMap(Map vectorMap, TermFreqVector termFreqVector) {
    String[] terms = termFreqVector.getTerms();
    int[] freqs = termFreqVector.getTermFrequencies();
    // Load Stopwords
    if (this.stopWords == null
            && (appConfig.discardStopWords && appConfig.stopWordsFile != null)) {
      this.stopWords = getStopWords(appConfig.stopWordsFile);
    }
    for (int i = 0; i < terms.length; i++) {
      String term = terms[i].toLowerCase(); // convert all term to lower case
      // CHECK FOR STOP WORDS IF DISCARDED OPTION ARE TRUE
      if (appConfig.discardStopWords && isTermStopWord(term)) {
        continue; // skip if the term are stop word
      }
      if (vectorMap.containsKey(term)) {
        Integer value = (Integer) vectorMap.get(term);
        vectorMap.put(term, new Integer(value.intValue() + freqs[i]));
      } else {
        vectorMap.put(term, new Integer(freqs[i]));
      }
    }
  }

  //<editor-fold defaultstate="collapsed" desc="STOP WORD METHODS">
  /**
   * Returns ArrayList of trimmed StopWords String
   *
   * @param path the Stopwords file object.
   * @return ArrayList of trimmed StopWords String
   */
  private static HashSet<String> getStopWords(File file) {
    try {
      HashSet<String> sw = new HashSet<String>();
      BufferedReader br = new BufferedReader(new FileReader(file));
      String line = null;
      while ((line = br.readLine()) != null) {
        // discarding comments, empty line and consist only space characters
        if (!line.startsWith("#") && !line.isEmpty() && !line.matches("\\s+")) {
          sw.add(line.trim());
        }
      }
      br = null;
      return sw;
    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return null;
  }
  
  private boolean isTermStopWord(String term) {
    if (this.stopWords != null) {
      if (this.stopWords.contains(term.trim())) {
        return true;
      }
    }
    return false;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="URL FILTER LIST METHOD">
  /**
   * check if the input URL are included as new document
   * @param url
   * @return 
   */
  private boolean checkUrllist(String url) {
    if (this.includedURLList == null) {
      this.includedURLList = getURLList(appConfig.URLFilterFile);
    }
    if (this.includedURLList.contains(url.trim())) {
      return true;
    }
    return false;
  }
  
  /**
   * Returns ArrayList of trimmed urlList String
   *
   * @param path the URLList file object.
   * @return HashSer of trimmed URL String
   */
  private static HashSet<String> getURLList(File file) {
    try {
      HashSet<String> sw = new HashSet<String>();
      BufferedReader br = new BufferedReader(new FileReader(file));
      String line = null;
      while ((line = br.readLine()) != null) {
        // discarding comments, empty line and consist only space characters
        if (!line.startsWith("#") && !line.isEmpty() && !line.matches("\\s+")) {
          sw.add(line.trim());
        }
      }
      br = null;
      return sw;
    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return null;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="MANUAL TOPIC CLUSTER METHODS">
  private int getManualCluster(String url) {
    if (this.URLTopicClusterList == null) {
      this.URLTopicClusterList = getManualClusterList(this.appConfig.manualTopicFile);
    }
    if (this.URLTopicClusterList.containsKey(url)) {
      return this.URLTopicClusterList.get(url.trim()).intValue();
    } else {
      System.out.println("No Topic found for: " + url);
      return 0;
    }
  }
  
  private static HashMap<String, Integer> getManualClusterList(File file) {
    try {
      HashMap<String, Integer> topicURLMap = new HashMap<String, Integer>();
      BufferedReader br = new BufferedReader(new FileReader(file));
      String line = null;
      while ((line = br.readLine()) != null) {
        // discarding comments, empty line and consist only space characters
        if (!line.startsWith("#") && !line.isEmpty() && !line.matches("\\s+")) {
          
          String[] part = line.split("\t", 3);
          String url = part[0].trim();
          int mainTopic = Integer.valueOf(part[1].trim()).intValue();
          topicURLMap.put(url, mainTopic);
        }
      }
      br = null;
      return topicURLMap;
    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return null;
  }
  //</editor-fold>
}
