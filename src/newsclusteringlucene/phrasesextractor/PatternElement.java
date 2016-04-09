/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package newsclusteringlucene.phrasesextractor;

/**
 *
 * @author shadiq
 */
public class PatternElement {
    public static final int ELEMENT_TYPE_TEXT = 0;
    public static final int ELEMENT_TYPE_POST = 1;

    private int elementType;
    private String elementString;

    public PatternElement() {};

    public PatternElement(int elementType, String elementString){
        this.elementType = elementType;
        this.elementString = elementString;
    }

    public String getElementString() {
        return elementString;
    }

    public void setElementString(String elementString) {
        this.elementString = elementString;
    }

    public int getElementType() {
        return elementType;
    }

    public void setElementType(int elementType) {
        this.elementType = elementType;
    }

    @Override
    public PatternElement clone(){
        PatternElement pe = new PatternElement();
        pe.elementType = (new Integer(this.elementType)).intValue();
        pe.elementString = this.elementString;
        return pe;
    }

    @Override
    /**
     *  Returns the string of this patternElement, in form of html:
     * [elementType]: elementString
     */
    public String toString(){
        String patternString = "<html>";
        if (elementType == ELEMENT_TYPE_TEXT) {
            patternString += "<b><font color=red>Word: </font></b>";
        } else if (elementType == ELEMENT_TYPE_POST) {
            patternString += "<b><font color=green>POST: </font></b>";
        }
        patternString += elementString;
        patternString += "</html>";
        return patternString;
//        return elementString;
    }
}
