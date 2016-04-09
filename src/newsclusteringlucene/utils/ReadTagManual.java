/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newsclusteringlucene.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author shadiq
 */
public class ReadTagManual {
  
  public static void main(String[] args) {
    
//    HashMap<Integer, Integer> conversionMap = new HashMap<Integer, Integer>();
    
//    // terorisme
//    conversionMap.put(4, 1);
//    conversionMap.put(22, 2);
//    conversionMap.put(43, 3);
//    conversionMap.put(44, 4);
//    conversionMap.put(45, 5);
//    conversionMap.put(46, 6);
//    conversionMap.put(47, 7);
//    conversionMap.put(48, 8);
//    conversionMap.put(49, 9);
//    conversionMap.put(54, 10);
//    conversionMap.put(55, 11);
//    conversionMap.put(58, 12);
//    conversionMap.put(59, 13);
//    conversionMap.put(60, 14);
//
//    // nii
//    conversionMap.put(42, 15);
//    conversionMap.put(56, 16);
//
//    // perbankan
//    conversionMap.put(14, 17);
//    conversionMap.put(15, 18);
//    conversionMap.put(16, 19);
//    conversionMap.put(17, 20);
//    conversionMap.put(30, 21);
//    conversionMap.put(40, 22);
//
//    // pertunangan ibas-aliya
//    conversionMap.put(36, 23);
//    conversionMap.put(8, 24);
//    conversionMap.put(9, 25);
//    conversionMap.put(51, 26);
//
//    // suap wafid muharram
//    conversionMap.put(32, 27);
//
//    // pembongkaran makam letkol heru
//    conversionMap.put(5, 28);
//
//    // bentrokan
//    conversionMap.put(57, 29);
//    conversionMap.put(23, 30);
//    conversionMap.put(28, 31);
//
//    // kontroversi persidangan antasari azhar
//    conversionMap.put(31, 32);
//
//    // sby
//    conversionMap.put(24, 33);
//    conversionMap.put(53, 34);
//    conversionMap.put(11, 35);
//
//    // tki
//    conversionMap.put(10, 36);
//    conversionMap.put(21, 37);
//
//    // dpr
//    conversionMap.put(20, 38);
//    conversionMap.put(33, 39);
//    conversionMap.put(34, 40);
//    conversionMap.put(35, 41);
//    conversionMap.put(61, 42);
//
//    // nasdem
//    conversionMap.put(37, 43);
//
//    // politik
//    conversionMap.put(38, 44);
//    conversionMap.put(39, 45);
//
//    // gubernur bali - moge
//    conversionMap.put(41, 46);
//
//    // gayus tambunan
//    conversionMap.put(65, 47);
//    conversionMap.put(66, 48);
//    conversionMap.put(27, 49);
//
//    // MISC
//    conversionMap.put(52, 50);
//    conversionMap.put(62, 51);
//    conversionMap.put(64, 52);
//    conversionMap.put(29, 53);
//    conversionMap.put(67, 54);
//    conversionMap.put(6, 55);
//    conversionMap.put(7, 56);
//    conversionMap.put(3, 57);
//    conversionMap.put(68, 58);
//    conversionMap.put(1, 59);
//    conversionMap.put(2, 60);
//    conversionMap.put(12, 61);
//    conversionMap.put(13, 62);
//    conversionMap.put(18, 63);
//    conversionMap.put(19, 64);
//    conversionMap.put(25, 65);
//    conversionMap.put(26, 66);
//
//    // OTHER
//    conversionMap.put(50, 67);

    
//    HashMap<Integer, topicMap> topicURLMap = getURLList2(
//            new File("resource/tag-manual-url-maintopic-mention.txt"), 
//            conversionMap);
    HashMap<Integer, topicMap> topicURLMap = getURLList(new File("resource/tag-manual-url-maintopic-mention.txt"));
    
//    Map sortedMap = SortingUtil.sortByValueLargeToSmall(topicURLMap);
//    String[] sortedValue = new String[sortedMap.size()];
    
    Iterator phrasesMapIterator = topicURLMap.keySet().iterator();
    while (phrasesMapIterator.hasNext()) {
      Integer topic = (Integer) phrasesMapIterator.next();
      int a = topicURLMap.get(topic).mainTopicURLS.size();
      int b = topicURLMap.get(topic).mentionTopicURLS.size();
      System.out.println(topic+" : ["+a+
              "] ["+b+"] ["
              +(a+b)+"]");
//      System.out.println(conversionMap.get(topic)+" : ["+a+
//              "] ["+b+"] ["
//              +(a+b)+"]");
      
    }
  }

  /**
   * Returns ArrayList of trimmed urlList String
   *
   * @param path the Stopwords file object.
   * @return ArrayList of trimmed StopWords String
   */
  private static HashMap<Integer, topicMap> getURLList(File file) {
    try {
      HashMap<Integer, topicMap> topicURLMap = new HashMap<Integer, topicMap>();
      BufferedReader br = new BufferedReader(new FileReader(file));
      String line = null;
      while ((line = br.readLine()) != null) {
        if (!line.startsWith("#")) { // discarding comments
          
          String[] part = line.split("\t", 3);
          String url = part[0];
//          System.out.println(url);
          
          
          int mainTopic = Integer.valueOf(part[1].trim()).intValue();
          if(topicURLMap.containsKey(mainTopic)){
            topicURLMap.get(mainTopic).mainTopicURLS.add(url);
          } else {
            topicMap newTopicMap = new topicMap();
            newTopicMap.mainTopicURLS.add(url);
            topicURLMap.put(mainTopic, newTopicMap);
          }
          
          if(!part[2].isEmpty()){
            String mentions = part[2];
            String[] mention = mentions.split(",");
            for (int i = 0; i < mention.length; i++) {
              int mentionTopic = Integer.valueOf(mention[i].trim()).intValue();
              if (topicURLMap.containsKey(mentionTopic)) {
                topicURLMap.get(mentionTopic).mentionTopicURLS.add(url);
              } else {
                topicMap newTopicMap = new topicMap();
                newTopicMap.mentionTopicURLS.add(url);
                topicURLMap.put(mentionTopic, newTopicMap);
              }
            }
          }
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
  
  /**
   * Returns ArrayList of trimmed urlList String
   *
   * @param path the Stopwords file object.
   * @return ArrayList of trimmed StopWords String
   */
  private static HashMap<Integer, topicMap> getURLList2(File file, HashMap<Integer, Integer> conversionMap) {
    try {
      HashMap<Integer, topicMap> topicURLMap = new HashMap<Integer, topicMap>();
      BufferedReader br = new BufferedReader(new FileReader(file));
      String line = null;
      while ((line = br.readLine()) != null) {
        if (!line.startsWith("#")) { // discarding comments
          
          String[] part = line.split("\t", 3);
          String url = part[0];
//          System.out.println(url);
          
          
          if(!part[1].isEmpty()){
            int mainTopic = Integer.valueOf(part[1].trim()).intValue();
            mainTopic = conversionMap.get(mainTopic);
            
            System.out.print(url+ "\t" + mainTopic + "\t");
            
            
            if (topicURLMap.containsKey(mainTopic)) {
              topicURLMap.get(mainTopic).mainTopicURLS.add(url);
            } else {
              topicMap newTopicMap = new topicMap();
              newTopicMap.mainTopicURLS.add(url);
              topicURLMap.put(mainTopic, newTopicMap);
            }
          }
          
          if(!part[2].isEmpty()){
            String mentions = part[2];
            String[] mention = mentions.split(",");
            
            for (int i = 0; i < mention.length; i++) {
              int mentionTopic = Integer.valueOf(mention[i].trim()).intValue();
              mentionTopic = conversionMap.get(mentionTopic);
              
              if (topicURLMap.containsKey(mentionTopic)) {
                topicURLMap.get(mentionTopic).mentionTopicURLS.add(url);
              } else {
                topicMap newTopicMap = new topicMap();
                newTopicMap.mentionTopicURLS.add(url);
                topicURLMap.put(mentionTopic, newTopicMap);
              }
              
              if(i != (mention.length-1)){
                System.out.print(mentionTopic+", ");
              } else {
                System.out.print(mentionTopic);
              }
            }
            
//            for (String string : mention) {
//              int mentionTopic = Integer.valueOf(string.trim()).intValue();
//              mentionTopic = conversionMap.get(mentionTopic);
//              
//              if (topicURLMap.containsKey(mentionTopic)) {
//                topicURLMap.get(mentionTopic).mentionTopicURLS.add(url);
//              } else {
//                topicMap newTopicMap = new topicMap();
//                newTopicMap.mentionTopicURLS.add(url);
//                topicURLMap.put(mentionTopic, newTopicMap);
//              }
//            }
          }
        }
        System.out.println("");
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
  
  private static class topicMap {
    public ArrayList<String> mainTopicURLS = new ArrayList<String>();
    public ArrayList<String> mentionTopicURLS = new ArrayList<String>();
  }
}
