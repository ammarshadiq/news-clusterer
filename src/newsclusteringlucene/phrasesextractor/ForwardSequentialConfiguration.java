/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package newsclusteringlucene.phrasesextractor;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author shadiq
 */
public class ForwardSequentialConfiguration {

    // Phrase Pattern Filters
    protected boolean usesPOSTagger = true;
    protected POSTagger posTagger = new IndPOSTagger(new IndPOSTaggerConfig());

    protected ArrayList<Pattern> patternFilters =
            new ArrayList<Pattern>(Arrays.asList(
                new Pattern[] {
                    new Pattern(Pattern.PATTERN_TYPE_FREE,
                        new PatternElement[] {
                            new PatternElement(PatternElement.ELEMENT_TYPE_POST, "/NN")
                            ,
                            new PatternElement(PatternElement.ELEMENT_TYPE_POST, "/NNP")
                    })
                }
            ));
    protected boolean includeAllPossibilities = false;

    // Stop Words Filter
    protected File stopWordsFile = new File("resource/stopwords/stopwords_from_crawled_news.txt");
    protected boolean excludePhraseContainsStopWord = false;
    protected boolean excludePhraseStartedWithStopWord = true;
    protected boolean excludePhraseEndedWithStopWord = true;

    // Num Of Words Filter
    // the value of phraseExtractorMaxN could not less than phraseExtractorMinN
    // and the value of phraseExtractorMinN could not greater than phraseExtractorMaxN
    protected int phraseExtractorMaxN = 0;
    protected int phraseExtractorMinN = 0;

    // Merge Overlap Phrases
    // the rule is as follows :
    //  if merge OverlapPhrases is True, then removeOverlapPhrases could be changed to True
    //  if mergeOverlapPhrases is Flase, then removeOverlapPhrases always False
    protected boolean mergeOverlapPhrases = false;
    protected boolean removeOverlapPhrases = false;

    @Override
    public ForwardSequentialConfiguration clone(){
        ForwardSequentialConfiguration c = new ForwardSequentialConfiguration();
        c.usesPOSTagger = this.usesPOSTagger;
        c.posTagger = this.posTagger;
        c.patternFilters = new ArrayList<Pattern>();
        for (Pattern pattern : this.patternFilters) {
            c.patternFilters.add(pattern.clone());
        }
        c.stopWordsFile = new File(this.stopWordsFile.getAbsolutePath());
        c.excludePhraseContainsStopWord = this.excludePhraseContainsStopWord;
        c.excludePhraseStartedWithStopWord = this.excludePhraseStartedWithStopWord;
        c.excludePhraseEndedWithStopWord = this.excludePhraseEndedWithStopWord;
        c.phraseExtractorMaxN = this.phraseExtractorMaxN;
        c.phraseExtractorMinN = this.phraseExtractorMinN;
        c.includeAllPossibilities = this.includeAllPossibilities;
        c.mergeOverlapPhrases = this.mergeOverlapPhrases;
        c.removeOverlapPhrases = this.removeOverlapPhrases;

        return c;
    }
}


