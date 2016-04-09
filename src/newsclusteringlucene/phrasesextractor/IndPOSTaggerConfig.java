/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package newsclusteringlucene.phrasesextractor;

import java.io.File;

/**
 *
 * @author shadiq
 */
public class IndPOSTaggerConfig {
    
    public static final int NGRAM_LANGUAGE_MODEL_BIGRAM = 0;
    public static final int NGRAM_LANGUAGE_MODEL_TRIGRAM = 1;
    public static final int AFFIX_TREE_TYPE_PREFIX = 0;
    public static final int AFFIX_TREE_TYPE_SUFIX = 1;
    public static final int AFFIX_TREE_TYPE_PREFIX_SUFIX = 2;
    public static final int AFFIX_TREE_TYPE_BASELINE_NN_NO_TREE = 3;
    public static final int HMM_TWO_PHRASE_TYPE_NOT_USED = 0;
    public static final int HMM_TWO_PHRASE_TYPE_1 = 1; // tn-1,tn,tn+1
    public static final int HMM_TWO_PHRASE_TYPE_2 = 2; //tn-2,tn-1,tn,tn+1
    public static final int USE_KBBI_LEXICON_NO = 0;
    public static final int USE_KBBI_LEXICON_YES = 1;

    private File lexiconFile = new File("resource/iPOSTagger/original-Lexicon.trn");
    private File nGramFile = new File("resource/iPOSTagger/original-Ngram.trn");
    private int nGramLanguageModel = NGRAM_LANGUAGE_MODEL_BIGRAM;
    private int maxAffixTreeLength = 3;
    private int affixTreePruningTreshold = 3;
    private int affixTreeMinWord = 0;
    private int affixTreeType = AFFIX_TREE_TYPE_PREFIX;
    private boolean debug = false;
    private double jelinecMercerBigramSmoothing = 0.2d;
    private int hmmTwoPhaseType = HMM_TWO_PHRASE_TYPE_NOT_USED;
    private double beamSearchDecoder = 500.0d;
    private int useKBBILexicon =  USE_KBBI_LEXICON_YES;
    private File tagSetFile = new File("resource/iPOSTagger/BhsIndPOSTagSet.txt");

    public int getAffixTreeMinWord() {
        return affixTreeMinWord;
    }

    public void setAffixTreeMinWord(int affixTreeMinWord) {
        this.affixTreeMinWord = affixTreeMinWord;
    }

    public int getAffixTreePruningTreshold() {
        return affixTreePruningTreshold;
    }

    public void setAffixTreePruningTreshold(int affixTreePruningTreshold) {
        this.affixTreePruningTreshold = affixTreePruningTreshold;
    }

    public int getAffixTreeType() {
        return affixTreeType;
    }

    public void setAffixTreeType(int affixTreeType) {
        this.affixTreeType = affixTreeType;
    }

    public double getBeamSearchDecoder() {
        return beamSearchDecoder;
    }

    public void setBeamSearchDecoder(double beamSearchDecoder) {
        this.beamSearchDecoder = beamSearchDecoder;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public int getHmmTwoPhaseType() {
        return hmmTwoPhaseType;
    }

    public void setHmmTwoPhaseType(int hmmTwoPhaseType) {
        this.hmmTwoPhaseType = hmmTwoPhaseType;
    }

    public double getJelinecMercerBigramSmoothing() {
        return jelinecMercerBigramSmoothing;
    }

    public void setJelinecMercerBigramSmoothing(double jelinecMercerBigramSmoothing) {
        this.jelinecMercerBigramSmoothing = jelinecMercerBigramSmoothing;
    }

    public File getLexiconFile() {
        return lexiconFile;
    }

    public void setLexiconFile(File lexiconFile) {
        this.lexiconFile = lexiconFile;
    }

    public int getMaxAffixTreeLength() {
        return maxAffixTreeLength;
    }

    public void setMaxAffixTreeLength(int maxAffixTreeLength) {
        this.maxAffixTreeLength = maxAffixTreeLength;
    }

    public File getnGramFile() {
        return nGramFile;
    }

    public void setnGramFile(File nGramFile) {
        this.nGramFile = nGramFile;
    }

    public int getnGramLanguageModel() {
        return nGramLanguageModel;
    }

    public void setnGramLanguageModel(int nGramLanguageModel) {
        this.nGramLanguageModel = nGramLanguageModel;
    }

    public File getTagSetFile() {
        return tagSetFile;
    }

    public void setTagSetFile(File tagSetFile) {
        this.tagSetFile = tagSetFile;
    }

    public int getUseKBBILexicon() {
        return useKBBILexicon;
    }

    public void setUseKBBILexicon(int useKBBILexicon) {
        this.useKBBILexicon = useKBBILexicon;
    }

    @Override
    public IndPOSTaggerConfig clone(){
        IndPOSTaggerConfig iptc = new IndPOSTaggerConfig();
        iptc.lexiconFile = this.lexiconFile;
        iptc.nGramFile = this.nGramFile;
        iptc.nGramLanguageModel = this.nGramLanguageModel;
        iptc.maxAffixTreeLength = this.maxAffixTreeLength;
        iptc.affixTreePruningTreshold = this.affixTreePruningTreshold;
        iptc.affixTreeMinWord = this.affixTreeMinWord;
        iptc.affixTreeType = this.affixTreeType;
        iptc.debug = this.debug;
        iptc.jelinecMercerBigramSmoothing = this.jelinecMercerBigramSmoothing;
        iptc.hmmTwoPhaseType = this.hmmTwoPhaseType;
        iptc.beamSearchDecoder = this.beamSearchDecoder;
        iptc.useKBBILexicon =  this.useKBBILexicon;
        iptc.tagSetFile = this.tagSetFile;
        return iptc;
    }


}
