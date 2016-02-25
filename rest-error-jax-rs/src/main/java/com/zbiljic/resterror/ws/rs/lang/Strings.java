package com.zbiljic.resterror.ws.rs.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Strings {

  /**
   * Constant representing the empty string, equal to &quot;&quot;
   */
  public static final String EMPTY_STRING = "";

  //---------------------------------------------------------------------
  // General convenience methods for working with Strings
  //---------------------------------------------------------------------

  /**
   * Check that the given CharSequence is neither {@code null} nor of length 0. Note: Will return
   * {@code true} for a CharSequence that purely consists of whitespace.
   *
   * <pre>
   * Strings.hasLength(null) = false
   * Strings.hasLength("") = false
   * Strings.hasLength(" ") = true
   * Strings.hasLength("Hello") = true
   * </pre>
   *
   * @param str the CharSequence to check (may be {@code null})
   * @return {@code true} if the CharSequence is not null and has length
   * @see #hasText(String)
   */
  public static boolean hasLength(CharSequence str) {
    return (str != null && str.length() > 0);
  }

  /**
   * Check that the given String is neither {@code null} nor of length 0. Note: Will return {@code
   * true} for a String that purely consists of whitespace.
   *
   * @param str the String to check (may be {@code null})
   * @return {@code true} if the String is not null and has length
   * @see #hasLength(CharSequence)
   */
  public static boolean hasLength(String str) {
    return hasLength((CharSequence) str);
  }

  /**
   * Check whether the given CharSequence has actual text. More specifically, returns {@code true}
   * if the string not {@code null}, its length is greater than 0, and it contains at least one
   * non-whitespace character.
   *
   * <pre>
   * Strings.hasText(null) = false
   * Strings.hasText("") = false
   * Strings.hasText(" ") = false
   * Strings.hasText("12345") = true
   * Strings.hasText(" 12345 ") = true
   * </pre>
   *
   * @param str the CharSequence to check (may be {@code null})
   * @return {@code true} if the CharSequence is not {@code null}, its length is greater than 0, and
   * it does not contain whitespace only
   * @see Character#isWhitespace
   */
  public static boolean hasText(CharSequence str) {
    if (!hasLength(str)) {
      return false;
    }
    int strLen = str.length();
    for (int i = 0; i < strLen; i++) {
      if (!Character.isWhitespace(str.charAt(i))) {
        return true;
      }
    }
    return false;
  }

  /**
   * Check whether the given String has actual text. More specifically, returns {@code true} if the
   * string not {@code null}, its length is greater than 0, and it contains at least one
   * non-whitespace character.
   *
   * @param str the String to check (may be {@code null})
   * @return {@code true} if the String is not {@code null}, its length is greater than 0, and it
   * does not contain whitespace only
   * @see #hasText(CharSequence)
   */
  public static boolean hasText(String str) {
    return hasText((CharSequence) str);
  }

  /**
   * Returns a 'cleaned' representation of the specified argument.  'Cleaned' is defined as the
   * following:
   *
   * <pre>
   *   <ol>
   *     li>If the specified {@code String} is {@code null}, return {@code null}</li>
   *     <li>If not {@code null}, {@link String#trim() trim()} it.</li>
   *     <li>If the trimmed string is equal to the empty String (i.e. &quot;&quot;), return {@code
   * null}</li>
   *     <li>If the trimmed string is not the empty string, return the trimmed version</li>
   *   </ol>
   * </pre>
   *
   * Therefore this method always ensures that any given string has trimmed text, and if it doesn't,
   * {@code null} is returned.
   *
   * @param str the input String to clean.
   * @return a populated-but-trimmed String or {@code null} otherwise
   */
  public static String clean(String str) {
    if (str == null) {
      return null;
    }
    String value = trimWhitespace(str);
    if (value == null || EMPTY_STRING.equals(value)) {
      return null;
    }
    return value;
  }

  /**
   * Trim leading and trailing whitespace from the given String.
   *
   * @param str the String to check
   * @return the trimmed String
   * @see Character#isWhitespace
   */
  public static String trimWhitespace(String str) {
    if (!hasLength(str)) {
      return str;
    }
    StringBuilder sb = new StringBuilder(str);
    while (sb.length() > 0 && Character.isWhitespace(sb.charAt(0))) {
      sb.deleteCharAt(0);
    }
    while (sb.length() > 0 && Character.isWhitespace(sb.charAt(sb.length() - 1))) {
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }

  /**
   * Delete any character in a given String.
   *
   * @param inString      the original String
   * @param charsToDelete a set of characters to delete. E.g. "az\n" will delete 'a's, 'z's and new
   *                      lines.
   * @return the resulting String
   */
  public static String deleteAny(String inString, String charsToDelete) {
    if (!hasLength(inString) || !hasLength(charsToDelete)) {
      return inString;
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < inString.length(); i++) {
      char c = inString.charAt(i);
      if (charsToDelete.indexOf(c) == -1) {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  //---------------------------------------------------------------------
  // Convenience methods for working with String arrays
  //---------------------------------------------------------------------

  /**
   * Copy the given Collection into a String array. The Collection must contain String elements
   * only.
   *
   * @param collection the Collection to copy
   * @return the String array ({@code null} if the passed-in Collection was {@code null})
   */
  public static String[] toStringArray(Collection<String> collection) {
    if (collection == null) {
      return null;
    }
    return collection.toArray(new String[collection.size()]);
  }

  /**
   * Split a String at the first occurrence of the delimiter. Does not include the delimiter in the
   * result.
   *
   * @param toSplit   the string to split
   * @param delimiter to split the string up with
   * @return a two element array with index 0 being before the delimiter, and index 1 being after
   * the delimiter (neither element includes the delimiter); or {@code null} if the delimiter wasn't
   * found in the given input String
   */
  public static String[] split(String toSplit, String delimiter) {
    if (!hasLength(toSplit) || !hasLength(delimiter)) {
      return null;
    }
    int offset = toSplit.indexOf(delimiter);
    if (offset < 0) {
      return null;
    }
    String beforeDelimiter = toSplit.substring(0, offset);
    String afterDelimiter = toSplit.substring(offset + delimiter.length());
    return new String[]{beforeDelimiter, afterDelimiter};
  }

  /**
   * Take a String which is a delimited list and convert it to a String array.
   *
   * A single delimiter can consists of more than one character: It will still be considered as
   * single delimiter string, rather than as bunch of potential delimiter characters.
   *
   * @param str       the input String
   * @param delimiter the delimiter between elements (this is a single delimiter, rather than a
   *                  bunch individual delimiter characters)
   * @return an array of the tokens in the list
   */
  public static String[] delimitedListToStringArray(String str, String delimiter) {
    return delimitedListToStringArray(str, delimiter, null);
  }

  /**
   * Take a String which is a delimited list and convert it to a String array.
   *
   * A single delimiter can consists of more than one character: It will still be considered as
   * single delimiter string, rather than as bunch of potential delimiter characters.
   *
   * @param str           the input String
   * @param delimiter     the delimiter between elements (this is a single delimiter, rather than a
   *                      bunch individual delimiter characters)
   * @param charsToDelete a set of characters to delete. Useful for deleting unwanted line breaks:
   *                      e.g. "\r\n\f" will delete all new lines and line feeds in a String.
   * @return an array of the tokens in the list
   */
  public static String[] delimitedListToStringArray(String str, String delimiter, String charsToDelete) {
    if (str == null) {
      return new String[0];
    }
    if (delimiter == null) {
      return new String[]{str};
    }
    List<String> result = new ArrayList<>();
    if ("".equals(delimiter)) {
      for (int i = 0; i < str.length(); i++) {
        result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
      }
    } else {
      int pos = 0;
      int delPos;
      while ((delPos = str.indexOf(delimiter, pos)) != -1) {
        result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
        pos = delPos + delimiter.length();
      }
      if (str.length() > 0 && pos <= str.length()) {
        // Add rest of String, but not in case of empty input.
        result.add(deleteAny(str.substring(pos), charsToDelete));
      }
    }
    return toStringArray(result);
  }

}
