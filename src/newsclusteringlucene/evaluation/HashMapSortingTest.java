/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newsclusteringlucene.evaluation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 *
 * @author shadiq
 */
public class HashMapSortingTest {
  public static void main(String[] args) {
    HashMap<String, Double> unSorted1 = new HashMap<String, Double>();
    unSorted1.put("Bristol", 23.45);
    unSorted1.put("London", 345.122);
    unSorted1.put("Manchester", 12.3);
    unSorted1.put("Edinburgh", 11.4);
    
    HashMap<String, Double> sorted1 = new HashMap<String, Double>();
    sorted1 = sortHashMap1(unSorted1);
    for (String cityName : sorted1.keySet()) {
      System.out.println(cityName + " " + sorted1.get(cityName));
      
    }
    
    HashMap<Integer, Integer> unSorted2 = new HashMap<Integer, Integer>();
    unSorted2.put(3 , 4);
    unSorted2.put(4, 9);
    unSorted2.put(5, 2);
    unSorted2.put(6, 12);
    unSorted2.put(7,12);
    
//    HashMap<Integer, Integer> sorted2 = new HashMap<Integer, Integer>();
//    sorted2 = sortHashMap2(unSorted2);
//    for (Integer cityName : sorted2.keySet()) {
//      System.out.println(cityName + " " + sorted2.get(cityName));
//      
//    }
    
    LinkedHashMap<Integer, Integer> sortedHashMap = sortHashMapByValueDIntInt(unSorted2);
//    HashMap<Integer, Integer> sorted2 = new HashMap<Integer, Integer>();
//    sorted2 = sortHashMap2(unSorted2);
    for (Integer cityName : sortedHashMap.keySet()) {
      System.out.println(cityName + " " + sortedHashMap.get(cityName));
      
    }
  }
  
  private static HashMap<String, Double> sortHashMap1(HashMap<String, Double> input) {
    Map<String, Double> tempMap = new HashMap<String, Double>();
    for (String wsState : input.keySet()) {
      tempMap.put(wsState, input.get(wsState));
    }

    List<String> mapKeys = new ArrayList<String>(tempMap.keySet());
    List<Double> mapValues = new ArrayList<Double>(tempMap.values());
    HashMap<String, Double> sortedMap = new LinkedHashMap<String, Double>();
    TreeSet<Double> sortedSet = new TreeSet<Double>(mapValues);
    Object[] sortedArray = sortedSet.toArray();
    int size = sortedArray.length;
    for (int i = 0; i < size; i++) {
      sortedMap.put(mapKeys.get(mapValues.indexOf(sortedArray[i])), (Double) sortedArray[i]);
    }
    return sortedMap;
  }
  
  private static HashMap<Integer, Integer> sortHashMap2(HashMap<Integer, Integer> input) {
    Map<Integer, Integer> tempMap = new HashMap<Integer, Integer>();
    for (Integer wsState : input.keySet()) {
      tempMap.put(wsState, input.get(wsState));
    }

    List<Integer> mapKeys = new ArrayList<Integer>(tempMap.keySet());
    List<Integer> mapValues = new ArrayList<Integer>(tempMap.values());
    HashMap<Integer, Integer> sortedMap = new LinkedHashMap<Integer, Integer>();
    TreeSet<Integer> sortedSet = new TreeSet<Integer>(mapValues);
    Object[] sortedArray = sortedSet.toArray();
    
    int size = sortedArray.length;
    for (int i = size-1; i >= 0; i--) {
      sortedMap.put(mapKeys.get(mapValues.indexOf(sortedArray[i])), (Integer) sortedArray[i]);
    }
    return sortedMap;
  }
  
  public LinkedHashMap sortHashMapByValueD(HashMap passedMap){
    List mapKeys = new ArrayList(passedMap.keySet());
    List mapValues = new ArrayList(passedMap.values());
    Collections.sort(mapValues);
    Collections.sort(mapKeys);
    
    LinkedHashMap sortedMap = new LinkedHashMap();
    
    Iterator valueIt = mapValues.iterator();
    while (valueIt.hasNext()){
      Object val = valueIt.next();
      Iterator keyIt = mapKeys.iterator();
      
      while(keyIt.hasNext()){
        Object key = keyIt.next();
        String comp1 = passedMap.get(key).toString();
        String comp2 = val.toString();
        
        if(comp1.equals(comp2)){
          passedMap.remove(key);
          mapKeys.remove(key);
          sortedMap.put((String)key, (Double)val);
          break;
        }
      }
    }
    return sortedMap;
  }
  
  private static LinkedHashMap<Integer, Integer> sortHashMapByValueDIntInt(HashMap<Integer, Integer> passedMap){
    List mapKeys = new ArrayList(passedMap.keySet());
    List mapValues = new ArrayList(passedMap.values());
    Collections.sort(mapValues, new byMostArticles());
    Collections.sort(mapKeys, new byMostArticles());
    
    LinkedHashMap<Integer, Integer> sortedMap = new LinkedHashMap<Integer, Integer>();
    
    Iterator valueIt = mapValues.iterator();
    while (valueIt.hasNext()){
      Object val = valueIt.next();
      Iterator keyIt = mapKeys.iterator();
      
      while(keyIt.hasNext()){
        Object key = keyIt.next();
        Integer comp1 = (Integer) passedMap.get(key);
        Integer comp2 = (Integer) val;
        
        if(comp1.equals(comp2)){
          passedMap.remove(key);
          mapKeys.remove(key);
          sortedMap.put((Integer)key, (Integer)val);
          break;
        }
      }
    }
    return sortedMap;
  }
  
  public static class byMostArticles implements java.util.Comparator {

    public int compare(Object t, Object t1) {
      int max = (Integer) t1 - (Integer) t;
      return max;
    }
  }
}
