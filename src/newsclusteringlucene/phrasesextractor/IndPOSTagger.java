/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newsclusteringlucene.phrasesextractor;

import NLP_ITB.POSTagger.HMM.Decoder.MainTagger;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author shadiq
 */
public class IndPOSTagger implements POSTagger {

  String taggerTitle = "iPOSTagger 1.0";
  MainTagger mt;
  ArrayList<String[]> tagListDescription;
  String[] tagList;
  IndPOSTaggerConfig indPOSTaggerConfig;

  public IndPOSTagger(IndPOSTaggerConfig indPOSTaggerConfig) {
    // IPOSTagger_v1.0 {@url http://alfan-farizki.blogspot.com/2010/04/nlp-resource-yang-tersedia-untuk-bahasa.html}
    this.indPOSTaggerConfig = indPOSTaggerConfig;
    this.mt = new MainTagger(
            indPOSTaggerConfig.getLexiconFile().getAbsolutePath(),
            indPOSTaggerConfig.getnGramFile().getAbsolutePath(),
            indPOSTaggerConfig.getnGramLanguageModel(),
            indPOSTaggerConfig.getMaxAffixTreeLength(),
            indPOSTaggerConfig.getAffixTreePruningTreshold(),
            indPOSTaggerConfig.getAffixTreeMinWord(),
            indPOSTaggerConfig.getAffixTreeType(),
            indPOSTaggerConfig.isDebug(),
            indPOSTaggerConfig.getJelinecMercerBigramSmoothing(),
            indPOSTaggerConfig.getHmmTwoPhaseType(),
            indPOSTaggerConfig.getBeamSearchDecoder(),
            indPOSTaggerConfig.getUseKBBILexicon());
    this.tagListDescription = loadTagSetListDescription(indPOSTaggerConfig.getTagSetFile());
    this.tagList = loadTagSetListDescription(tagListDescription);

//        Alternative raw constructor (example from the creator of this tagger)
//        this.mt = new MainTagger(
//                "./resource/Lexicon.trn", //filename of Lexicon file
//                "./resource/Ngram.trn", // filename of NGram file
//                0, // Chooose 2-PHASE HMM type, 0 : not used, 1 : tn-1,tn,tn+1 , 2 : tn-2,tn-1,tn,tn+1
//                3, // maximum affix length for creating affix tree
//                3, // Treshold value for prunning affix tree
//                0, // minimum word frequency in order for setting the affix tree
//                0, // 0:prefix tree, 1:suffix tree, 2:prefix-suffix tree, 3:BASELINE-NN-without tree
//                false, // mode debug
//                0.2, // for Bigram Smoothing, using JELINEC - MERCER
//                0, // Chooose 2-PHASE HMM type, 0 : not used, 1 : tn-1,tn,tn+1 , 2 : tn-2,tn-1,tn,tn+1
//                500.0, // for beam search decoder
//                1); // Using Lexicon from KBBI or not ?, 0:no 1:yes
  }

  public ArrayList<Token> getPOSTokens(ArrayList<Token> tokens) {
    // IPOSTagger_v1.0 {@url http://alfan-farizki.blogspot.com/2010/04/nlp-resource-yang-tersedia-untuk-bahasa.html}
    // requires the input string of words and symbols to be separated by space for appropriate tagging process,
    // reconstruct the document string but with each token separated by a space

    StringBuilder sb = new StringBuilder();
    for (Token token : tokens) {
      sb.append(token.getTokenString()).append(" ");
    }
    if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1); // remove last space

    ArrayList<String> ret = mt.taggingStr(sb.toString()); // tag the tokens
    if (tokens.size() == ret.size()) {
      for (int k = 0; k < ret.size(); k++) {
        String ss = ret.get(k);
        tokens.get(k).getProperties().put("POST", '/' + ss.substring(ss.lastIndexOf('/') + 1));
      }
    } else {
      System.out.println("Input String tokens is not the same size as tagged tokens and it could cause error");
//      System.exit(1);
    }

    return tokens;
  }

  public ArrayList<String[]> getTagSetListDescription() {
    if (tagListDescription != null) {
      return tagListDescription;
    } else {
      this.tagListDescription = loadTagSetListDescription(this.indPOSTaggerConfig.getTagSetFile());
      return tagListDescription;
    }
  }

  public String[] getTagSetList() {
    if (tagList != null) {
      return tagList;
    } else {
      this.tagList = loadTagSetListDescription(tagListDescription);
      return tagList;
    }
  }

  public static ArrayList<String[]> loadTagSetListDescription(File file) {
    ArrayList<String[]> sw = new ArrayList<String[]>();
    try {
      BufferedReader br = new BufferedReader(new FileReader(file));
      String line = null;
      while ((line = br.readLine()) != null) {
        if (!line.isEmpty() && !line.startsWith("#") && !line.startsWith("\\s")) { // discarding comments or blank
          String[] splittedLine = line.split("\t", 3);
          sw.add(splittedLine);
        }
      }
    } catch (IOException e) {
      System.out.println("Tag Set File not found!");
      e.printStackTrace();
//      System.exit(1);
    }
    return sw;
  }

  public static String[] loadTagSetListDescription(ArrayList<String[]> tagListDescription) {
    ArrayList<String> tagList;
    if (tagListDescription != null) {
      tagList = new ArrayList<String>();
      for (String[] strings : tagListDescription) {
        tagList.add(strings[0]);
      }
      return tagList.toArray(new String[tagList.size()]);
    }
    return null;
  }

  public String getTaggerTitle() {
    return this.taggerTitle;
  }

  public String getPOSTaggerDescription() {
//        ArrayList<String> description = new ArrayList<String>();
    StringBuilder sb = new StringBuilder();
    sb.append("Part-Of-Speech-Tagger Name: ").append(taggerTitle).append("\n");
    sb.append("Lexicon File: ").append(indPOSTaggerConfig.getLexiconFile().getAbsolutePath()).append("\n");
    sb.append("N-Gram File: ").append(indPOSTaggerConfig.getnGramFile().getAbsolutePath()).append("\n");
    if (indPOSTaggerConfig.getnGramLanguageModel()
            == IndPOSTaggerConfig.NGRAM_LANGUAGE_MODEL_BIGRAM) {
      sb.append("N-Gram Langue Model: Bigram");
    } else if (indPOSTaggerConfig.getnGramLanguageModel()
            == IndPOSTaggerConfig.NGRAM_LANGUAGE_MODEL_TRIGRAM) {
      sb.append("N-Gram Langue Model: Trigram");
    }
    sb.append("\n");

    sb.append("Max Affix Length: ").append(indPOSTaggerConfig.getMaxAffixTreeLength()).append("\n");
    sb.append("Affix Tree Pruning Treshold: ").append(indPOSTaggerConfig.getAffixTreePruningTreshold()).append("\n");
    sb.append("Affix Tree Min Word: ").append(indPOSTaggerConfig.getAffixTreeMinWord()).append("\n");

    if (indPOSTaggerConfig.getAffixTreeType() == IndPOSTaggerConfig.AFFIX_TREE_TYPE_PREFIX) {
      sb.append("Affix Tree Type: Prefix");
    } else if (indPOSTaggerConfig.getAffixTreeType() == IndPOSTaggerConfig.AFFIX_TREE_TYPE_SUFIX) {
      sb.append("Affix Tree Type: Sufix");
    } else if (indPOSTaggerConfig.getAffixTreeType() == IndPOSTaggerConfig.AFFIX_TREE_TYPE_PREFIX_SUFIX) {
      sb.append("Affix Tree Type: Prefix-Sufix");
    } else if (indPOSTaggerConfig.getAffixTreeType() == IndPOSTaggerConfig.AFFIX_TREE_TYPE_BASELINE_NN_NO_TREE) {
      sb.append("Affix Tree Type: BASELINE-NN-without tree");
    }
    sb.append("\n");

    sb.append("JELINEC-MERCER Bigram Smoothing: ").append(indPOSTaggerConfig.getJelinecMercerBigramSmoothing()).append("\n");
    if (indPOSTaggerConfig.getHmmTwoPhaseType() == IndPOSTaggerConfig.HMM_TWO_PHRASE_TYPE_NOT_USED) {
      sb.append("HMM Two Phase Type: Not Used");
    } else if (indPOSTaggerConfig.getHmmTwoPhaseType() == IndPOSTaggerConfig.HMM_TWO_PHRASE_TYPE_1) {
      sb.append("HMM Two Phase Type: tn-1, tn, tn+1");
    } else if (indPOSTaggerConfig.getHmmTwoPhaseType() == IndPOSTaggerConfig.HMM_TWO_PHRASE_TYPE_2) {
      sb.append("HMM Two Phase Type: tn-2, tn-1, tn, tn+1");
    }
    sb.append("\n");

    sb.append("Beam Search Decoder: ").append(indPOSTaggerConfig.getBeamSearchDecoder()).append("\n");
    if (indPOSTaggerConfig.getUseKBBILexicon() == IndPOSTaggerConfig.USE_KBBI_LEXICON_NO) {
      sb.append("Use KBBI Lexicon: FALSE");
    } else if (indPOSTaggerConfig.getUseKBBILexicon() == IndPOSTaggerConfig.USE_KBBI_LEXICON_YES) {
      sb.append("Use KBBI Lexicon: TRUE");
    }
    sb.append("\n");

    sb.append("Tag Description File: ").append(indPOSTaggerConfig.getTagSetFile().getAbsolutePath());

    return sb.toString();
  }

  public IndPOSTaggerConfig getIndPOSTaggerConfig() {
    return indPOSTaggerConfig;
  }
}
