/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package newsclusteringlucene.phrasesextractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import javax.swing.JOptionPane;

/**
 *
 * @author shadiq
 */
public class ForwardSequential implements PhrasesExtractor {
    public static final String phrasesExtractorName = "Phrases Extractor Forward Sequential";
    private ForwardSequentialConfiguration extractorConfig;
    private HashSet<String> stopWords; // stopwords would be often searched, so its hashed.
    private boolean usesNumberOfTokenFilter = false;

    public ForwardSequential(ForwardSequentialConfiguration config){
        this.extractorConfig = config;
        // <editor-fold defaultstate="collapsed" desc="NUMBER OF TOKEN CHECK">
        // Since the minimum value of MaxN always MinN, for example:
        // if MinN = 2; then MaxN value shouldnt less than 2.
        // only check for MaxN value, for determining it's uses this check or not.
        // as you should recall, that the value of those two variable could not less than 0.
        // 0 value of those two variable is the same as not using this check at all.
        if (this.extractorConfig.phraseExtractorMaxN > 0) {
            this.usesNumberOfTokenFilter = true;
        }
        // </editor-fold>
    }

    public ArrayList<Phrase> extractPhrases(String text) throws Exception {

        // TOKENIZER
        ArrayList<Token> tokens = new Tokenizer<Token>().tokenize(text);

         //if you want to see the tokenizer result
         //Phrase a = new Phrase(); for (Token token : tokens) {a.addToken(token);}System.out.println(a.toString());

        // READ PATTERN
        ArrayList<PatternForFilter> patternFilters = new ArrayList<PatternForFilter>();
        for (Pattern pattern : this.extractorConfig.patternFilters) {
            patternFilters.add(new PatternForFilter(pattern));
        }

        // <editor-fold defaultstate="collapsed" desc="USES POST TAG CHECK">
        // If uses POS Tag, then Tag every token on the document
        // if not, doth have to Tag the tokens, and remove the Pattern Configuration that uses POS Tag in its element.
        if (this.extractorConfig.usesPOSTagger) { // IF USES POS Tagger
            if (this.extractorConfig.posTagger.getClass().getName().matches("newsclusteringlucene.phrasesextractor.IndPOSTagger")) {
                tokens = this.extractorConfig.posTagger.getPOSTokens(tokens);
            }
        } else { // if not uses POS Tagger
            ArrayList<PatternForFilter> unnacceptedPattern = new ArrayList<PatternForFilter>();
            // map the unnacepted Pattern (Pattern that uses POST)
            for (int i = 0; i < patternFilters.size(); i++) {
                PatternForFilter patternFilter = patternFilters.get(i);
                Pattern pattern = (Pattern) patternFilter;
                for (PatternElement element : pattern.getElements()) {
                    if (element.getElementType() == PatternElement.ELEMENT_TYPE_POST) {
                        unnacceptedPattern.add(patternFilter);
                        break;
                    }
                }
            }
            // remove the mapped unnaceptedPattern
            for (PatternForFilter patternFilter : unnacceptedPattern) {
            //    System.out.println("removing "+pattern);
                patternFilters.remove(patternFilter);
            }
        }
        // check remaining patterns
        //for (Pattern pattern : patternFilters) {
        //    System.out.println(pattern.toString());
        //}

        if(patternFilters.isEmpty()){
            JOptionPane.showMessageDialog(null, "Every Pattern Uses POST, recheck your Pattern Configuration",
                        "Empty Pattern",JOptionPane.ERROR_MESSAGE);
            return null;
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="STOPWORDS CHECK">
        // If uses stopwords filter, in any form, then load the stopwords file.
        if (this.extractorConfig.excludePhraseContainsStopWord
                || this.extractorConfig.excludePhraseStartedWithStopWord
                || this.extractorConfig.excludePhraseEndedWithStopWord) { // IF USES STOPWORDS
            // Load Stopwords
            if (this.extractorConfig.stopWordsFile != null) {
                this.stopWords = getStopWords(this.extractorConfig.stopWordsFile);
            }
        }
        // </editor-fold>

        ArrayList<Phrase> phrases = new ArrayList<Phrase>();

        for (int i = 0; i < tokens.size(); i++) { // for each token, iterate
            Token token_1 = tokens.get(i);

            if ((token_1.getType() == Token.TOKEN_TYPE_SYMBOL) || // phrase shouldnt start with symbol (sentence terminator)
                    ((this.extractorConfig.excludePhraseContainsStopWord // phrase shoulndnt contain StopWord (if its configured so)
                        || this.extractorConfig.excludePhraseStartedWithStopWord) && // phrase shoulndnt start with StopWord (if its configured so)
                        this.isTokenStopWord(token_1))) {
                continue;
            } else {
               PhraseForFilter tempPhrase = new PhraseForFilter();
               tempPhrase.addToken(token_1);

               ArrayList<PatternForFilter> acceptedPattern = checkPhraseAgaintsPatterns(tempPhrase, patternFilters);
               
               if(!acceptedPattern.isEmpty()){ // if theres a pattern that match with this token_1
                   tempPhrase.setAcceptingPattern(acceptedPattern);
                   PhraseForFilter acceptedPhrase = null;
                   if((!this.usesNumberOfTokenFilter) || // if uses Number of Token Filter
                               (this.extractorConfig.phraseExtractorMinN <= 1)) // if the minN is less then equal 1
                   { // <editor-fold defaultstate="collapsed" desc="LOGICAL CHECK EXPLANATION">
                     // this conditional statement having two operand:
                     //     A = if uses Number of Token Filter
                     //     B = if the value of number of tokens is less than equal minN
                     //     Logical Check Statement: (!A || B) only FALSE for one condition:
                     //     A, !B
                     // </editor-fold>
                       acceptedPhrase = tempPhrase.clone();
                   }

                   for (int j = i + 1; j < tokens.size(); j++) { // token iteration Level 2
                       Token token_2 = tokens.get(j);
                       
                       if ((!((token_2.getType() == Token.TOKEN_TYPE_SYMBOL) || // phrase shouldnt start with symbol (sentence terminator)
                                (this.extractorConfig.excludePhraseContainsStopWord &&  // phrase shoulndnt contain StopWord (if its configured so)
                                        this.isTokenStopWord(token_2)))) // tambahin kalau melebihi maksimal
                           &&
                           ((!this.usesNumberOfTokenFilter) || // if uses Number of Token Filter
                               ((this.extractorConfig.phraseExtractorMaxN > 1) // if the maxN is greater than 1
                               &&
                               (tempPhrase.getTokens().size() < this.extractorConfig.phraseExtractorMaxN) // if the value of number of tokens is less than maxN
                               ))
                       ){  // <editor-fold defaultstate="collapsed" desc="LOGICAL CHECK EXPLANATION">
                           // this conditional statement having six operand that could be broken as two logical check
                           // Logical Check 1:
                           //   A = if the token_2 is  a symbol
                           //   B = if the configuration set not to contain stop words
                           //   C = if the token_2 is a stopword
                           //   Logical Check 1 statement : (A || (B && C)) only TRUE for THREE condition:
                           //   A, B, C
                           //   A, B, !C
                           //   A, !B, C
                           // Logical Check 2:
                           //   A = the value of Logical Check 1
                           //   B = if uses Number of Token Filter
                           //   C = if the maxN is greater than 1
                           //   D = if the value of number of tokens is less than maxN
                           //   Logical Check 2 Statement: (A && (!B || (C && D))) only TRUE for FIVE condition:
                           //   A, B, C, D
                           //   A, !B, C, D
                           //   A, !B, C, !D
                           //   A, !B, !C, D
                           //   A, !B, !C, !D
                           // if you want to make sure the conditional calculation, you could use "truth table".
                           // </editor-fold>

                           boolean alreadyAdded = false; // a bypass flag variable special for includeAllPossibilities true option
                           if ((acceptedPhrase != null) && isFullyMatching(acceptedPhrase)) {
                               if(this.extractorConfig.includeAllPossibilities){
                                   phrases.add((Phrase) acceptedPhrase);
                                   alreadyAdded = true;
                               }
                           }

                           // add Token_2 then pattern check it.
                           tempPhrase.addToken(token_2);
                           acceptedPattern = checkPhraseAgaintsPatterns(tempPhrase, acceptedPattern);

                           if (acceptedPattern.isEmpty() && (acceptedPhrase != null)) { // if after adding the token_2 theres not pattern that match
                               // check is it full phrase or not
                               if (isFullyMatching(acceptedPhrase)) {
                                   if (((!isPhraseAlreadyAdded(acceptedPhrase, phrases)) || // check if the phrase already added
                                           this.extractorConfig.includeAllPossibilities)) // if include all possibility (if its configured so)
                                   { // <editor-fold defaultstate="collapsed" desc="LOGICAL CHECK EXPLANATION">
                                     // this conditional statement having two operand:
                                     // A = if the phrase already added
                                     // B = if include all possibility
                                     // Logical Check Statement: (!A || B) only FALSE for ONW condition:
                                     // A, !B
                                     // </editor-fold>
                                       
                                       if(!alreadyAdded){
                                           phrases.add((Phrase) acceptedPhrase);
                                       }

                                   }
                               }
                               break;
                           } else {
                               if((!(this.extractorConfig.excludePhraseEndedWithStopWord &&  // phrase shoulndnt ended with StopWord (if its configured so)
                                        this.isTokenStopWord(token_2))) &&
                                  ((!this.usesNumberOfTokenFilter) || // if uses Number of Token Filter
                                    (this.extractorConfig.phraseExtractorMinN >= 1)) // if the maxN is greater than 1
                                    ){  // <editor-fold defaultstate="collapsed" desc="LOGICAL CHECK EXPLANATION">
                                        // this conditional statement having four operand that could be broken as two logical check
                                        // Logical Check 1:
                                        //  A = if the configuration set not to ended with stop words
                                        //  B = if the token_2 is a stopword
                                        //  Logical Check 1 statement : !(A && B) only FALSE for ONE condition:
                                        //   A, B
                                        // Logical Check 2:
                                        //  A = the value of Logical Check 1
                                        //  B = if uses Number of Token Filter
                                        //  C = if the value of number of tokens is greater than equal than minN
                                        //  Logical Check 2 statement : A && (!C || D) only TRUE for THREE condition:
                                        //  A, B, C
                                        //  A, !B, C
                                        //  A, !B, !C
                                        // </editor-fold>
                                   tempPhrase.setAcceptingPattern(acceptedPattern);
                                   acceptedPhrase = tempPhrase.clone();
                               }
                           }
                           
                       } else {
                           if (acceptedPhrase != null){
                               // cek acceptedPhrase full apa enggak
                               if (isFullyMatching(acceptedPhrase)) {
                                   if ((!isPhraseAlreadyAdded(acceptedPhrase, phrases) || // check if the phrase already added
                                           this.extractorConfig.includeAllPossibilities)) // if include all possibility (if its configured so)
                                   { // <editor-fold defaultstate="collapsed" desc="LOGICAL CHECK EXPLANATION">
                                     // this conditional statement having two operand:
                                     // A = if the phrase already added
                                     // B = if include all possibility
                                     // Logical Check Statement: (!A || B) only FALSE for ONW condition:
                                     // A, !B
                                     // </editor-fold>
                                       phrases.add((Phrase) acceptedPhrase);
                                   }
                               }
                           }
                           break;
                       }
                   }
               }
            }
        }

        if( this.extractorConfig.mergeOverlapPhrases){
            phrases = mergeOverlapPhrases(phrases, this.extractorConfig.removeOverlapPhrases);
        }

        // if you want to see the phrases extraction result
        //for (Phrase keyPhrase : phrases) {
        //    System.out.println(keyPhrase.toString());
        //}
        return phrases;
    }

    //  check if a phrase that match a pattern that match fully
    private boolean isFullyMatching(PhraseForFilter phraseFilter){
        boolean accept = false;
        for (PatternForFilter patternFilter : phraseFilter.acceptingPattern) {
            if (patternFilter.matchingStatus == PatternForFilter.PATTERN_FULLY_MATCH){
                accept = true;
                break; // just one match is enough, break if found one.
            }
        }
        return accept;
    }

    private ArrayList<PatternForFilter> checkPhraseAgaintsPatterns(PhraseForFilter checkedPhrase, ArrayList<PatternForFilter> acceptedPatternFilters) {

        ArrayList<Token> phraseTokens = checkedPhrase.getTokens();
        ArrayList<PatternForFilter> acceptedPattern = new ArrayList<PatternForFilter>();

        for (PatternForFilter patternFilter : acceptedPatternFilters) {
            PatternForFilter pattern = patternFilter;
            if (pattern.getPatternType() == Pattern.PATTERN_TYPE_EXACT) { // for the EXACT PATTERN
                if (phraseTokens.size() <= pattern.getElements().size()) { // only if the phrase having token less than the the number of elemet on Pattern
                    // untuk tiap token di phrase, cek kesamaannya dengan token pattern berdasarkan posisinya
                    boolean accept = true;
                    for (int i = 0; i < phraseTokens.size(); i++) {
                        PatternElement pe = pattern.getElements().get(i);
                        Token tok = phraseTokens.get(i);

                        if (pe.getElementType() == PatternElement.ELEMENT_TYPE_POST) { // if the pattern element type is POST
                            // if its not the same, reject it.
                            if (!tok.getProperties().get("POST").equals(pe.getElementString())) {
                                accept = false;
                                break;
                            }
                            // if its the same, but not complete, that is the chekedPhrase only contains T[P3] T[P2] from the pattern of T[P3] T[P2] T[P4] ,
                            // then its patriatlly accepted, and the break statement doesn't have to be executed.
                        } else if (pe.getElementType() == PatternElement.ELEMENT_TYPE_TEXT) { // if the pattern element type is WORD
                            // if its not the same, reject it.
                            if (!tok.getTokenString().equals(pe.getElementString())) {
                                accept = false;
                                break;
                            }
                            // if its the same, but not complete, that is the chekedPhrase only contains T[P3] T[P2] from the pattern of T[P3] T[P2] T[P4] ,
                            // then its patriatlly accepted, and the break statement doesn't have to be executed.
                        }
                    }
                    if(accept){
                        // if the breaks above doesn't reached, then the Pattern is OK
                        if (phraseTokens.size() < pattern.getElements().size()) { // if the pattern is longer than the phrase
                            patternFilter.setMatchingStatus(PatternForFilter.PATTERN_PARTIALLY_MATCH); // if the checkedPhrase  size is less than the acceptingPattern, then its Patrially accepted
                            acceptedPattern.add(patternFilter);
                        } else if (phraseTokens.size() == pattern.getElements().size()) { // if the checkedPhrase size is  the same size as the acceptingPattern
                            patternFilter.setMatchingStatus(PatternForFilter.PATTERN_FULLY_MATCH);
                            acceptedPattern.add(patternFilter);
                        } else if (phraseTokens.size() > pattern.getElements().size()) { // should't be reached
                            patternFilter.setMatchingStatus(PatternForFilter.PATTERN_NOT_MATCH);
                        }
                    }
                }
            } else if (pattern.getPatternType() == Pattern.PATTERN_TYPE_FREE) { // for the FREE PATTERN
                // for each token in Phrase, check the similarity with Pattern, doesnt have to relies on the position as Exact Pattern typr
                // if just one token in the phrase doesnt match (false), reject it.
                boolean[] match = new boolean[phraseTokens.size()];
                for (int i = 0; i < phraseTokens.size(); i++) { // for each Token in PHRASE
                    // first initialize as false
                    match[i] = false;
                    Token tok = phraseTokens.get(i);
                    for (PatternElement pe : pattern.getElements()) { // for each PatternElement in PATTERN
                        if (pe.getElementType() == PatternElement.ELEMENT_TYPE_POST) { // if the pattern element type is POST
                            // if just one of the element is equals, then break the patternElements iteration
                            if (tok.getProperties().get("POST").equals(pe.getElementString())) {
                                match[i] = true;
                                break;
                            }
                        } else if (pe.getElementType() == PatternElement.ELEMENT_TYPE_TEXT) { // if the pattern element type is WORD
                            // if just one of the element is equals, then break the patternElements iteration
                            if (tok.getTokenString().equals(pe.getElementString())) {
                                match[i] = true;
                                break;
                            }
                        }
                    }
                }
                // chek
                boolean accept = true;
                for (boolean b : match) {
                    if(!b){ // if just one token in the phrase doesnt match (false), reject it.
                        accept = false;
                        break;
                    }
                }
                if(accept){ // for the FREE PATTERN it doest have to contain any Partially accepted, since this pattern type is very loose.
                    patternFilter.setMatchingStatus(PatternForFilter.PATTERN_FULLY_MATCH);
                    acceptedPattern.add(patternFilter);
                }
            }
        }
        return acceptedPattern;
    }

    /**
        *  Returns true if the phrase is already added
        * @param checkedPhrase
        * @param savedPhrases
        * @return
        */
    private boolean isPhraseAlreadyAdded(PhraseForFilter checkedPhrase, ArrayList<Phrase> savedPhrases) {
        // the checking procedure is as follows:
        // since this extractor do its phrase extraction sequentially and forward, the possibility are:
        //  if there are a document : "FORMER/P1 IRAQ/P3 PRESIDENT/P1 SADAM/P4 HUSEIN/P4
        // and two pattern : [1] FREE /P1 /P3
        //                             [2] EXACT /P1 /P4 /P4
        // and there are already saved phrase : "FORMER/P1 IRAQ/P3 PRESIDENT/P1" from processing forward form token "FORMER"
        //      taken from the pattern of FREE /P1 /P3
        //      the next token to process after this is "IRAQ"
        //
        // processing token "IRAQ/P3"  will match pattern [1] and includes token "PRESIDENT/P1" and resulting a phrase : "IRAQ/P3 PRESIDENT/P1"
        //      taken form the pattern of [1] FREE /P1 /P3
        //      this phrase would be rejected, since both token in the phrase is already added on the previous saved phrase.
        //      the next token to process after this is "PRESIDENT/P1"
        // processing token "PRESIDENT/P1" at first will match both pattern [1,2] and will includes token "SADAM/P4" and "HUSEIN/P4", resulting a phrase: "PRESIDENT/P1 SADAM/P4 HUSEIN/P4"
        //      taken from the pattern of [2] EXACT /P1 /P4 P4
        //      this phrase would be accepted, since token "SADAM/P4" and "HUSEIN/P4" haven't been added on the previos saved phrase.
        //      and then, the saved phrase would be:
        //              [1] "FORMER/P1 IRAQ/P3 PRESIDENT/P1"
        //              [2] "PRESIDENT/P1 SADAM/P4 HUSEIN/P4"
        //      notice there are an overlap on saved phrase[2], that is the token "PRESIDENT'/P1", 
        //      but since the rest of the token, "SADAM/P4" and "HUSEIN/P4" haven't been added
        //      that phrase is added.
        //
        // the rest of the token "SADAM/P4" AND "HUSEIN/P4" doesnt match any pattern, so it's not getting any forwad processing at all.
        //
        // by the description above, we only have to check the last token on the checked phrase.
        // if the last token on checkedPhrase, doesn't match with the last token of the last saved phrase, then the checked phrase havent been added.
        //
        //
        // check it by the tokens offset
        // because i certain that the same Token would always started on the same offset
        // therefore, only check for the startOffset


        if (!savedPhrases.isEmpty()){
            Phrase lastSavedPhrase = savedPhrases.get(savedPhrases.size() - 1);
            Token lastTokenOnSavedPhrase = lastSavedPhrase.getTokens().get(lastSavedPhrase.getTokens().size() - 1);
            Token lastTokenOnCheckedPhrase = checkedPhrase.getTokens().get(checkedPhrase.getTokens().size() - 1);

            if (lastTokenOnSavedPhrase.getStartOffset() == lastTokenOnCheckedPhrase.getStartOffset()) {
                return true;
            }
        }
        return false;
    }

    /**
        * Returns ArrayList of trimmed StopWords String
        *
        * @param path the Stopwords file object.
        * @return ArrayList of trimmed StopWords String
        */
    private static HashSet<String> getStopWords(File file) throws FileNotFoundException, IOException {
        HashSet<String> sw = new HashSet<String>();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("#")) { // discarding comments
                    sw.add(line.trim());
                }
            }
            br = null;
        return sw;
    }

    private boolean isTokenStopWord(Token token){
        if (this.stopWords != null)
            if (this.stopWords.contains(token.getTokenString().trim())) return true;
        return false;
    }

    /**
        *  This method is to process phrases and merge phrases that having an overlapping token.
        *  an overlapping token in phrases is for example:
        *      [Phrase 1] "IN{2} AN{3} EVENT{4}"
        *      [Phrase 2] "FORMER{7} IRAQ{8} PRESIDENT{9}"
        *      [Phrase 3] "PRESIDENT{9} SADAM{10} HUSEIN{11}"
        *  [Phrase 2] and [Phrase 3] is overlapping on token PRESIDENT{9}, because it has the same Token Document Position.
        *  So the merged value would be : "FORMER PRESIDENT SADAN HUSEIN" [Phrase 4]
        *
        * if the attribute removeOverlapPhrases is true, then, this method only returns [phrase 1] and [Phrase 4]
        * if its false, then it returns [Phrase 1], [Phrase 2], [Phrase 4], [Phrase 3]  (specifically in this order)
        *
        * @param savedPhrases
        * @param removeOverlapPhrases
        * @return phrases that have been merged, if there is an overlap phrases in it
        */
    private ArrayList<Phrase> mergeOverlapPhrases(ArrayList<Phrase> savedPhrases, boolean removeOverlapPhrases) {
        // I make this function without carefull planning, so the algorithm are very hideous.
        // as it says, better works then perfect.
        ArrayList<Phrase> newSavedPhrases = new ArrayList<Phrase>();

        Phrase p = savedPhrases.get(0);
        boolean merged_1 = false;
        for (int i = 1; i <= savedPhrases.size(); i++) {
            Phrase sp = null;
            if(i < savedPhrases.size()) sp = savedPhrases.get(i);

            boolean merged_2 = false;
            Phrase mergedPhrase = p.clone();
            Token tok_1 = mergedPhrase.getTokens().get(mergedPhrase.getTokens().size() - 1); // get the last token of P1

            // scan the similarity location
            int dissimilarityStart = -1;
            if (sp != null) {
                for (int j = 0; j < sp.getTokens().size(); j++) {
                    Token t_2 = sp.getTokens().get(j);
                    if (tok_1.getStartOffset() == t_2.getStartOffset()) {
                        dissimilarityStart = j + 1;
                    }
                }
            }
            //if there is a similarity
            if (dissimilarityStart > -1) {
                merged_2 = true;
                for (int j = dissimilarityStart; j < sp.getTokens().size(); j++) {
                    mergedPhrase.addToken(sp.getTokens().get(j));
                }
            }

            if (merged_2) p = mergedPhrase;
            else { newSavedPhrases.add(p); p = sp; }

            // remove overlapPhrases option
            if ((merged_1 || merged_2) && !removeOverlapPhrases) {
                newSavedPhrases.add(savedPhrases.get(i - 1));
            }
            merged_1 = merged_2;
        }
        return newSavedPhrases;
    }
    
    public ForwardSequentialConfiguration getExtractorConfig() {
        return extractorConfig;
    }

    public void setExtractorConfig(ForwardSequentialConfiguration extractorConfig) {
        this.extractorConfig = extractorConfig;
    }

    public String getPhrasesExtractorDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(phrasesExtractorName).append("\n");
        sb.append("Uses Part Of Speech Tagger: ").append(extractorConfig.usesPOSTagger).append("\n");

        if (extractorConfig.usesPOSTagger){
            sb.append(extractorConfig.posTagger.getPOSTaggerDescription()).append("\n");
        }

        if(!extractorConfig.patternFilters.isEmpty()){
            sb.append(extractorConfig.patternFilters.size()).append(" Pattern Filters:").append("\n");
            for (Pattern pattern : extractorConfig.patternFilters) {
                String patternDesc = "";
                if(pattern.getPatternType() == Pattern.PATTERN_TYPE_EXACT) {
                    patternDesc += "Exact Form: ";
                } else if (pattern.getPatternType() == Pattern.PATTERN_TYPE_FREE) {
                    patternDesc += "Free Form: ";
                }
                for (PatternElement element : pattern.getElements()) {
                    patternDesc += element.getElementString() + " ";
                }
                sb.append(patternDesc).append("\n");
            }
        }
        if(extractorConfig.stopWordsFile != null){
            sb.append("Stop Words File: ").append(extractorConfig.stopWordsFile.getAbsolutePath()).append("\n");
        }

        sb.append("Exclude Phrase that Contain Stopwords: ").append(extractorConfig.excludePhraseContainsStopWord).append("\n");
        sb.append("Exclude Phrase that Started With Stopwords: ").append(extractorConfig.excludePhraseStartedWithStopWord).append("\n");
        sb.append("Exclude Phrase that Ended With Stopwords: ").append(extractorConfig.excludePhraseEndedWithStopWord).append("\n");

        sb.append("Use Number of Terms Filter: ").append(this.usesNumberOfTokenFilter).append("\n");
        if(usesNumberOfTokenFilter){
            sb.append("Min Number of Terms Filter: ").append(extractorConfig.phraseExtractorMinN).append("\n");
            sb.append("Max Number of Terms Filter: ").append(extractorConfig.phraseExtractorMaxN).append("\n");
        }

        sb.append("Merge Overlap Phrases: ").append(extractorConfig.mergeOverlapPhrases).append("\n");
        sb.append("Remove Overlapped Phrases: ").append(extractorConfig.removeOverlapPhrases).append("\n");
        return sb.toString();
    }

    // private Pattern Class with added Atributes to indicaters the pattern Matching Status
    private class PatternForFilter extends Pattern {

        public static final int PATTERN_NOT_MATCH = 0;
        public static final int PATTERN_FULLY_MATCH = 1;
        public static final int PATTERN_PARTIALLY_MATCH = 2;
        private int matchingStatus = PATTERN_NOT_MATCH;

        public PatternForFilter(Pattern pattern) {
            super(pattern.getPatternType(), pattern.getElements());
        }

        public int getMatchingStatus() {
            return matchingStatus;
        }

        public void setMatchingStatus(int acceptedStatus) {
            this.matchingStatus = acceptedStatus;
        }

        @Override
        public PatternForFilter clone(){
            PatternForFilter pf = new PatternForFilter((Pattern) this);
            pf.matchingStatus = this.matchingStatus;
            return pf;
        }
    }

    // private Phrase Class with added Atributes to indicaters the pattern that match this phrase
    private class PhraseForFilter extends Phrase {

        private ArrayList<PatternForFilter> acceptingPattern = new ArrayList<PatternForFilter>();

        public PhraseForFilter(){
            super();
        }

        public PhraseForFilter(Phrase phrase){
            super(phrase.getTokens());
        }

        public ArrayList<PatternForFilter> getAcceptingPattern() {
            return acceptingPattern;
        }

        public void setAcceptingPattern(ArrayList<PatternForFilter> acceptingPattern) {
            this.acceptingPattern = acceptingPattern;
        }

        @Override
        public PhraseForFilter clone(){
            PhraseForFilter pf = new PhraseForFilter((Phrase) this);
            pf.acceptingPattern = new ArrayList<PatternForFilter>();
            for (PatternForFilter patternFilter : this.acceptingPattern) {
                pf.acceptingPattern.add(patternFilter.clone());
            }
            return pf;
        }
    }

    @Override
    public String toString(){
        return phrasesExtractorName;
    }
}