/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package newsclusteringlucene.phrasesextractor;

import java.util.ArrayList;
import newsclusteringlucene.phrasesextractor.Token;

/**
 *
 * @author shadiq
 */
public interface POSTagger {
    String getTaggerTitle();
    String getPOSTaggerDescription();
    ArrayList<Token> getPOSTokens (ArrayList<Token> tokens);
    ArrayList<String[]> getTagSetListDescription ();
    String[] getTagSetList ();
}
