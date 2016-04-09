/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package newsclusteringlucene.phrasesextractor;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


/**
 * Describing Token object, consist of:
 * <ul>
 *  <li>{@code string} the token String</li>
 *  <li>{@code startOffset} the token starting position on the document (the first position character)</li>
 *  <li>{@code endOffset} the token last position on the document(the last position character)</li>
 *  <li>{@code type} the token type, there are two type: TERM and SYMBOL</li>
 * </ul>
 *
 * @author shadiq
 */
public class Token {
    public static final int TOKEN_TYPE_TERM = 0;
    public static final int TOKEN_TYPE_SYMBOL = 1;
    private String tokenString;
    private int startOffset;
    private int endOffset;
    private int type;
    
    private Map<String, String> Properties = new TreeMap<String, String>();

    public Token() {
    }

    public Token(String s) {
        tokenString = s;
    }

    /**
     * @return the token String
     */
    public String getTokenString() {
        return tokenString;
    }

    /**
     * @param string the token String
     */
    public void setTokenString(String s) {
        this.tokenString = s;
    }

    /**
     * @return the
     */
    public int getEndOffset() {
        return endOffset;
    }

    public void setEndOffset(int endOffset) {
        this.endOffset = endOffset;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
    }

    public int getType(){
        return this.type;
    }

    public void setType(int type){
        this.type = type;
    }

    public Map<String, String> getProperties() {
        return Properties;
    }

    public void setProperties(Map<String, String> Properties) {
        this.Properties = Properties;
    }

    @Override
    public Token clone(){
        Token t = new Token();
        t.tokenString = this.tokenString;
        t.startOffset = this.startOffset;
        t.endOffset = this.endOffset;
        t.type = this.type;

        Iterator propertiesIterator = this.Properties.keySet().iterator();
        while (propertiesIterator.hasNext()) { // iterate through all term in doc/tfMap
            String key = (String) propertiesIterator.next();
            String value = (String) Properties.get(key);
            t.Properties.put(key, value);
        }
        return t;
    }
}
