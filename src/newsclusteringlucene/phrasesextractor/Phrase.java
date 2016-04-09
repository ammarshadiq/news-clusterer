/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package newsclusteringlucene.phrasesextractor;

import java.util.ArrayList;

/**
 *
 * @author shadiq
 */
public class Phrase {
    private String phraseString = "";
    private ArrayList<Token> tokens = new ArrayList<Token>();

    public Phrase(){}

    public Phrase(ArrayList<Token> tokens){
        this.tokens.addAll(tokens);
    }

    public void addToken(Token token){
        this.tokens.add(token);
    }

    public ArrayList<Token> getTokens(){
        return tokens;
    }

    @Override
    public Phrase clone(){
        Phrase p = new Phrase();
        p.phraseString = this.toString();
        for (Token token : this.tokens) {
            p.tokens.add(token.clone());
        }
        return p;
    }

    @Override
    public String toString(){
        this.phraseString = "";
        for (Token token : tokens) {
            this.phraseString = this.phraseString + token.getTokenString() + " ";
        }
        this.phraseString.trim();
        return phraseString;
    }
}
