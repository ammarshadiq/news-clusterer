/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package newsclusteringlucene.phrasesextractor;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author shadiq
 */
public class Pattern {
    public static int PATTERN_TYPE_EXACT = 0;
    public static int PATTERN_TYPE_FREE = 1;

    private int patternType;
    private ArrayList<PatternElement> elements = new ArrayList<PatternElement>();

    public Pattern(){}

    public Pattern(int patternType, ArrayList<PatternElement> elements){
        this.patternType = patternType;
        this.elements = elements;
    }

    public Pattern(int patternType, PatternElement[] elements){
        this.patternType = patternType;
        this.elements.addAll(Arrays.asList(elements));
    }

    public ArrayList<PatternElement> getElements() {
        return elements;
    }

    public void setElements(ArrayList<PatternElement> elements) {
        this.elements = elements;
    }

    public void addElement(PatternElement element){
        this.elements.add(element);
    }

    public int getPatternType() {
        return patternType;
    }

    public void setPatternType(int patternType) {
        this.patternType = patternType;
    }

    @Override
    public Pattern clone(){
        Pattern p = new Pattern();
        p.patternType = this.patternType;
        p.elements = new ArrayList<PatternElement>();
        for (PatternElement patternElement : this.elements) {
            p.elements.add(patternElement.clone());
        }
        return p;
    }

    @Override
    /**
        *  Returns the string of this patternElement, in form of html:
        * [patternType]: patternElements
        */
    public String toString(){
        String patternString = "<html>";
        if (patternType == PATTERN_TYPE_EXACT){
            patternString += "<b><font color=red>Exact Form: </font></b>";
        } else if (patternType == PATTERN_TYPE_FREE){
            patternString += "<b><font color=green>Free Form: </font></b>";
        }
        for (PatternElement patternElement : elements) {
            patternString += patternElement.getElementString() + " ";
        }
        patternString += "</html>";
        return patternString;
    }
}
