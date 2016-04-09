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
public class Tokenizer<U extends Token> {

  /**
   * Divide the String into words and symbols, for example:<br>
   * <i>{@code “We have repeatedly contended — and still believe, as do many.}</i><br>
   * would be divided as :<br>
   * <i>{@code [“] [We] [have] [repeatedly] [contended] [—] [and] [still] [believe] [,] [as] [do] [many] [.]}</i><br>
   * <br>
   * This method is also filters Symbols character with constraint of (UTF-8): <b> &lt; 0x20</b> or <b> &gt; 0x7e</b><br>
   * With exception for [ . ], [ ' ], [ - ], [ @ ] and [ # ] characters <br><br>
   * for [ . ] and [ ' ] that's not trailed and or (exclusive or) followed by whitespace or another symbol is considered part of the token. For example:<br>
   * <ul>
   *  <li>{@code www.detik.com} considered as a whole token [www.detik.com] </li>
   *  <li>{@code still belive. As for} would be divide as [still] [belive] [.] [as] [for]</li>
   *  <li>{@code still belive .As for} would be divide as [still] [belive] [.] [as] [for]</li>
   *  <li>{@code ba'asyir} considered as a whole token [ba'asyir]</li>
   *  <li>{@code says 'susno} would be divide as [says] ['] [susno]</li>
   *  <li>{@code embarrassing'. he say} would be divide as [embarrassing] ['] [.] [he] [say]</li>
   * </ul>
   * <br><br>
   * Bahasa Indonesia have a different way to write plural, instead of adding "s" or "es", it uses dash [ - ]. For example:<br>
   * <ul>
   *  <li>"House" = "Rumah" </li>
   *  <li>"Houses" = "Rumah-rumah"</li>
   * </ul>
   * therefore, for [ - ] character that's not trailed and or (exclusive or) followed by whitespace or another symbol is considered part of the token.<br><br>
   *
   * For [ @ ] character, since I live in the age of twitter and email, that uses [ @ ] to mention someone and for email address.<br>
   * This [ @ ] character is treated different, its only considered as part of a token, ONLY if followed by a nonsymbol character. For example:<br>
   * <ul>
   *  <li>{@code kodok@sungai} considered as a whole token [kodok@sungai] </li>
   *  <li>{@code kodok@sungai.com} considered as a whole token [kodok@sungai.com] </li>
   *  <li>{@code in his twitter @kodok} would be divide as [in] [his] [twitter] [@kodok]</li>
   *  <li>{@code this@ that} would be divide as [this] [@] [that]</li>
   * </ul>
   * <br><br>
   *
   * For [ # ] character to describe hastag on twitter, its only considered as part of the token, ONLY if followed by a nonsymbol character AND trailed by a whitespace character.
   * <ul>
   *  <li>{@code hastag #prayforjapan} would be divide as [hastag] [#prayforjapan]</li>
   *  <li>{@code hastag# prayforjapan} would be divide as [hastag] [#] [prayforjapan] </li>
   *  <li>{@code hastag#prayforjapan} would be divide as [hastag] [#] [prayforjapan] </li>
   * </ul>
   * <br><br>
   *
   * @param s the String that would be tokenize
   * @return ArrayList of {@see POSToken} containing the divided String and Symbols
   */
  public ArrayList<U> tokenize(String s) {
    ArrayList<U> listOfTokens = new ArrayList<U>();
    char[] chars = s.toCharArray();
    StringBuilder tempTokenContainer = new StringBuilder();
    for (int i = 0; i < chars.length; i++) {
      switch (chars[i]) {
        case 0: // null character
          continue;
        case '\'': // 0x27 singel quot for example Ba'asyir --> check the previous char too
          if ((i + 1 < chars.length && i != 0) && (((String.valueOf(chars[i + 1])).matches("\\s") || isSymbol(chars[i + 1]))
                  ^ (String.valueOf(chars[i - 1])).matches("\\s"))) { // if the next or previous char is whitespace
            addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
            tempTokenContainer = new StringBuilder();
            addToken(i, "'", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
            continue;
          } else if (i == 0) {
            addToken(i, "-", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
            tempTokenContainer = new StringBuilder();
            continue;
          } else {
            tempTokenContainer.append(chars[i]);
            continue;
          }
        case '-': // 0x2d dash --> check the previous char too
          if ((i + 1 < chars.length && i != 0) // not the first or last char
                  && (((String.valueOf(chars[i + 1])).matches("\\s") || isSymbol(chars[i + 1]))
                  ^ ((String.valueOf(chars[i - 1])).matches("\\s") || isSymbol(chars[i - 1]))   )) { // if the next or previous char is whitespace
            addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
            tempTokenContainer = new StringBuilder();
            addToken(i, "-", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
            continue;
          } else if (i == 0) {
            addToken(i, "-", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
            tempTokenContainer = new StringBuilder();
            continue;
          } else {
            tempTokenContainer.append(chars[i]);
            continue;
          }
        case '—': // hyphen
          //if ((i + 1 < chars.length && i != 0) && (((String.valueOf(chars[i + 1])).matches("\\s") || isSymbol(chars[i + 1]))
          //        ^ (String.valueOf(chars[i - 1])).matches("\\s"))) { // if the next or previous char is whitespace
          //  addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
          //  tempTokenContainer = new StringBuilder();
          //  addToken(i, "-", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
          //  continue;
          //} else if (i == 0) {
          //  addToken(i, "-", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
          //  tempTokenContainer = new StringBuilder();
          //  continue;
          //} else {
          //  tempTokenContainer.append(chars[i]);
          //  continue;
          //}
          // USING PLAIN TOKENIZER
          addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
          tempTokenContainer = new StringBuilder();
          addToken(i, "—", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
          continue;
        case '.': // 0x2e
          if ((i + 1 < chars.length && i != 0) && (((String.valueOf(chars[i + 1])).matches("\\s") || isSymbol(chars[i + 1]))
                  ^ (String.valueOf(chars[i - 1])).matches("\\s"))) { // if the next or previous char is whitespace
            addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
            tempTokenContainer = new StringBuilder();
            addToken(i, ".", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
            continue;
          } else if (i == 0) {
            addToken(i, "-", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
            tempTokenContainer = new StringBuilder();
            continue;
          } else {
            tempTokenContainer.append(chars[i]);
            continue;
          }
        case '@': // 0x40 --> only check next char
          if (i + 1 < chars.length && ((String.valueOf(chars[i + 1])).matches("\\s") || isSymbol(chars[i + 1]))) { // if the next char is whitespace
            addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
            tempTokenContainer = new StringBuilder();
            addToken(i, "@", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
            continue;
          } else {
            tempTokenContainer.append(chars[i]);
            continue;
          }
        case '#': // 0x23
          if ((i + 1 < chars.length && i != 0) && (((String.valueOf(chars[i + 1])).matches("\\s") || isSymbol(chars[i + 1]))
                  && !(String.valueOf(chars[i - 1])).matches("\\s"))) { // if the next or previous char is whitespace
            addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
            tempTokenContainer = new StringBuilder();
            addToken(i, "#", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
            continue;
          } else {
            tempTokenContainer.append(chars[i]);
            continue;
          }
        case ' ': // 0x20 --> Space character
          addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
          tempTokenContainer = new StringBuilder();
          continue;
        case '\n': // --> newline character
          addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
          tempTokenContainer = new StringBuilder();
          continue;
        case '\t': // --> tab character
          addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
          tempTokenContainer = new StringBuilder();
          continue;
        case '!': // 0x21
          addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
          tempTokenContainer = new StringBuilder();
          addToken(i, "!", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
          continue;
        case '"': // 0x22
          addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
          tempTokenContainer = new StringBuilder();
          addToken(i, "\"", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
          continue;
        case '$': // 0x24
          addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
          tempTokenContainer = new StringBuilder();
          addToken(i, "$", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
          continue;
        case '%': // 0x25
          addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
          tempTokenContainer = new StringBuilder();
          addToken(i, "%", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
          continue;
        case '&': // 0x26
          addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
          tempTokenContainer = new StringBuilder();
          addToken(i, "&", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
          continue;
        case '(': // 0x28
          addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
          tempTokenContainer = new StringBuilder();
          addToken(i, "(", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
          continue;
        case ')': // 0x29
          addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
          tempTokenContainer = new StringBuilder();
          addToken(i, ")", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
          continue;
        case '*': // 0x2a
          addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
          tempTokenContainer = new StringBuilder();
          addToken(i, "*", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
          continue;
        case '+': // 0x2b
          addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
          tempTokenContainer = new StringBuilder();
          addToken(i, "+", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
          continue;
        case ',': // 0x2c
          addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
          tempTokenContainer = new StringBuilder();
          addToken(i, ",", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
          continue;
        case '/': // 0x2f
          addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
          tempTokenContainer = new StringBuilder();
          addToken(i, "/", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
          continue;
        case ':': // 0x3a
          addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
          tempTokenContainer = new StringBuilder();
          addToken(i, ":", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
          continue;
        case ';': // 0x3b
          addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
          tempTokenContainer = new StringBuilder();
          addToken(i, ";", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
          continue;
        case '<': // 0x3c
          addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
          tempTokenContainer = new StringBuilder();
          addToken(i, "<", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
          continue;
        case '=': // 0x3d
          addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
          tempTokenContainer = new StringBuilder();
          addToken(i, "=", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
          continue;
        case '>': // 0x3e
          addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
          tempTokenContainer = new StringBuilder();
          addToken(i, ">", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
          continue;
        case '?': // 0x3f
          addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
          tempTokenContainer = new StringBuilder();
          addToken(i, "?", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
          continue;
        case '[': // 0x5b
          addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
          tempTokenContainer = new StringBuilder();
          addToken(i, "[", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
          continue;
        case '\\': // 0x5c
          addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
          tempTokenContainer = new StringBuilder();
          addToken(i, "\\", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
          continue;
        case ']': // 0x5d
          addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
          tempTokenContainer = new StringBuilder();
          addToken(i, "]", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
          continue;
        case '^': // 0x5e
          addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
          tempTokenContainer = new StringBuilder();
          addToken(i, "^", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
          continue;
        case '_': // 0x5f
          addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
          tempTokenContainer = new StringBuilder();
          addToken(i, "_", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
          continue;
        case '`': // 0x60
          addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
          tempTokenContainer = new StringBuilder();
          addToken(i, "`", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
          continue;
        case '{': // 0x7b
          addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
          tempTokenContainer = new StringBuilder();
          addToken(i, "{", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
          continue;
        case '|': // 0x7c
          addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
          tempTokenContainer = new StringBuilder();
          addToken(i, "|", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
          continue;
        case '}': // 0x7d
          addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
          tempTokenContainer = new StringBuilder();
          addToken(i, "}", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
          continue;
        case '~': // 0x7e
          addToken(i - 1, tempTokenContainer.toString(), listOfTokens, Token.TOKEN_TYPE_TERM);
          tempTokenContainer = new StringBuilder();
          addToken(i, "~", listOfTokens, Token.TOKEN_TYPE_SYMBOL);
          continue;
        default:
          if (chars[i] < 0x20 || chars[i] > 0x7e) { // symbols IGNORE IT
          } else {
            tempTokenContainer.append(chars[i]);
          }
          continue;
      }
    }
    return listOfTokens;
  }

  /**
   * Adding Token to ArrayList of Token Object by filtering the inputString for characters with 0 length and whitespace character
   * @param lastCharPossition
   * @param inputString
   * @param listOfTokens
   */
  private void addToken(int lastCharPossition, String inputString, ArrayList<U> listOfTokens, int wordType) {
    if (inputString.length() > 0 && !inputString.equalsIgnoreCase("\\s")) {
      U tok = (U) new Token(inputString);
      tok.setStartOffset(lastCharPossition - (inputString.length() - 1));
      tok.setEndOffset(lastCharPossition + 1);
      tok.setType(wordType);
      listOfTokens.add(tok);
    }
  }

  public boolean isSymbol(char c) {
    for (char d : symbols) {
      if (c == d) {
        return true;
      }
    }
    return false;
  }
  public final static char[] symbols = {
    '\'', '-', '.', '@', '#',
    '!', '"', '$', '%', '&',
    '(', ')', '*', '+', ',',
    '/', ':', ';', '<', '=',
    '>', '?', '[', '\\', ']',
    '^', '_', '`', '{', '|',
    '}', '~', '—'
  };
}
