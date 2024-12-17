/**
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) 2024 Source Auditor Inc.
 */
package org.spdx.licenseTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Static helper class for comparing license text
 * 
 * @author Gary O'Neall
 *
 */
public class LicenseTextHelper {
	
	protected static final String TOKEN_SPLIT_REGEX = "(^|[^\\s.,?'();:\"/\\[\\]]{1,100})((\\s|\\.|,|\\?|'|\"|\\(|\\)|;|:|/|\\[|]|$){1,100})";
	public static final Pattern TOKEN_SPLIT_PATTERN = Pattern.compile(TOKEN_SPLIT_REGEX);
	protected static final Set<String> PUNCTUATION = Collections.unmodifiableSet(new HashSet<>(
			Arrays.asList(".",",","?","\"","'","(",")",";",":","/","[", "]")));
	// most of these are comments for common programming languages (C style, Java, Ruby, Python)
	protected static final Set<String> SKIPPABLE_TOKENS = Collections.unmodifiableSet(new HashSet<>(
		Arrays.asList("//","/*","*/","/**","#","##","*","**","\"\"\"","/","=begin","=end")));
	static final String DASHES_REGEX = "[\\u2012\\u2013\\u2014\\u2015]";
	static final Pattern SPACE_PATTERN = Pattern.compile("[\\u202F\\u2007\\u2060\\u2009]");
	static final Pattern COMMA_PATTERN = Pattern.compile("[\\uFF0C\\uFE10\\uFE50]");
	static final Pattern PER_CENT_PATTERN = Pattern.compile("per cent", Pattern.CASE_INSENSITIVE);
	static final Pattern COPYRIGHT_HOLDER_PATTERN = Pattern.compile("copyright holder", Pattern.CASE_INSENSITIVE);
	static final Pattern COPYRIGHT_HOLDERS_PATTERN = Pattern.compile("copyright holders", Pattern.CASE_INSENSITIVE);
	static final Pattern COPYRIGHT_OWNERS_PATTERN = Pattern.compile("copyright owners", Pattern.CASE_INSENSITIVE);
	static final Pattern COPYRIGHT_OWNER_PATTERN = Pattern.compile("copyright owner", Pattern.CASE_INSENSITIVE);
	@SuppressWarnings("unused")
    static final Pattern PER_CENT_PATTERN_LF = Pattern.compile("per\\s{0,100}\\n{1,10}\\s{0,100}cent", Pattern.CASE_INSENSITIVE);
	static final Pattern COPYRIGHT_HOLDERS_PATTERN_LF = Pattern.compile("copyright\\s{0,100}\\n{1,10}\\s{0,100}holders", Pattern.CASE_INSENSITIVE);
	static final Pattern COPYRIGHT_HOLDER_PATTERN_LF = Pattern.compile("copyright\\s{0,100}\\n{1,10}\\s{0,100}holder", Pattern.CASE_INSENSITIVE);
	static final Pattern COPYRIGHT_OWNERS_PATTERN_LF = Pattern.compile("copyright\\s{0,100}\\n{1,10}\\s{0,100}owners", Pattern.CASE_INSENSITIVE);
	static final Pattern COPYRIGHT_OWNER_PATTERN_LF = Pattern.compile("copyright\\s{0,100}\\n{1,10}\\s{0,100}owner", Pattern.CASE_INSENSITIVE);
	static final Pattern COPYRIGHT_SYMBOL_PATTERN = Pattern.compile("\\(c\\)", Pattern.CASE_INSENSITIVE);
	public static final Map<String, String> NORMALIZE_TOKENS = new HashMap<>();
	static {
		//TODO: These should be moved to a property file
		NORMALIZE_TOKENS.put("&","and");
		NORMALIZE_TOKENS.put("acknowledgment","acknowledgement");   
		NORMALIZE_TOKENS.put("analogue","analog");   
		NORMALIZE_TOKENS.put("analyse","analyze");   
		NORMALIZE_TOKENS.put("artefact","artifact");   
		NORMALIZE_TOKENS.put("authorisation","authorization");   
		NORMALIZE_TOKENS.put("authorised","authorized");   
		NORMALIZE_TOKENS.put("calibre","caliber");   
		NORMALIZE_TOKENS.put("cancelled","canceled");   
		NORMALIZE_TOKENS.put("apitalisations","apitalizations");   
		NORMALIZE_TOKENS.put("catalogue","catalog");   
		NORMALIZE_TOKENS.put("categorise","categorize");   
		NORMALIZE_TOKENS.put("centre","center");   
		NORMALIZE_TOKENS.put("emphasised","emphasized");   
		NORMALIZE_TOKENS.put("favour","favor");   
		NORMALIZE_TOKENS.put("favourite","favorite");   
		NORMALIZE_TOKENS.put("fulfil","fulfill");   
		NORMALIZE_TOKENS.put("fulfilment","fulfillment");   
		NORMALIZE_TOKENS.put("initialise","initialize");   
		NORMALIZE_TOKENS.put("judgment","judgement");   
		NORMALIZE_TOKENS.put("labelling","labeling");   
		NORMALIZE_TOKENS.put("labour","labor");   
		NORMALIZE_TOKENS.put("licence","license");   
		NORMALIZE_TOKENS.put("maximise","maximize");   
		NORMALIZE_TOKENS.put("modelled","modeled");   
		NORMALIZE_TOKENS.put("modelling","modeling");   
		NORMALIZE_TOKENS.put("offence","offense");   
		NORMALIZE_TOKENS.put("optimise","optimize");   
		NORMALIZE_TOKENS.put("organisation","organization");   
		NORMALIZE_TOKENS.put("organise","organize");   
		NORMALIZE_TOKENS.put("practise","practice");   
		NORMALIZE_TOKENS.put("programme","program");   
		NORMALIZE_TOKENS.put("realise","realize");   
		NORMALIZE_TOKENS.put("recognise","recognize");   
		NORMALIZE_TOKENS.put("signalling","signaling");   
		NORMALIZE_TOKENS.put("utilisation","utilization");   
		NORMALIZE_TOKENS.put("whilst","while");   
		NORMALIZE_TOKENS.put("wilful","wilfull");   
		NORMALIZE_TOKENS.put("non-commercial","noncommercial");    
		NORMALIZE_TOKENS.put("copyright-owner", "copyright-holder");
		NORMALIZE_TOKENS.put("sublicense", "sub-license");
		NORMALIZE_TOKENS.put("non-infringement", "noninfringement");
		NORMALIZE_TOKENS.put("(c)", "-c-");
		NORMALIZE_TOKENS.put("©", "-c-");
		NORMALIZE_TOKENS.put("copyright", "-c-");
		NORMALIZE_TOKENS.put("\"", "'");
		NORMALIZE_TOKENS.put("merchantability", "merchantability");
	}
		
	private LicenseTextHelper() {
		// static class
	}
	
	/**
     * Returns true if two sets of license text is considered a match per
     * the SPDX License matching guidelines documented at spdx.org (currently <a href="https://spdx.github.io/spdx-spec/v2.3/license-matching-guidelines-and-templates/">license matching guidelines</a>)
     * There are 2 unimplemented features - bullets/numbering is not considered and comments with no whitespace between text is not skipped
     * @param licenseTextA text to compare
     * @param licenseTextB text to compare
     * @return true if the license text is equivalent
     */
	public static boolean isLicenseTextEquivalent(String licenseTextA, String licenseTextB) {
		//TODO: Handle comment characters without white space before text
		//TODO: Handle bullets and numbering
		// Need to take care of multi-word equivalent words - convert to single words with hyphens
		
		// tokenize each of the strings
		if (licenseTextA == null) {
			return (licenseTextB == null || licenseTextB.isEmpty());
		}
		if (licenseTextB == null) {
			return licenseTextA.isEmpty();
		}
		if (licenseTextA.equals(licenseTextB)) {
			return true;
		}
		Map<Integer, LineColumn> tokenToLocationA = new HashMap<>();
		Map<Integer, LineColumn> tokenToLocationB = new HashMap<>();
		String[] licenseATokens = tokenizeLicenseText(licenseTextA,tokenToLocationA);
		String[] licenseBTokens = tokenizeLicenseText(licenseTextB,tokenToLocationB);
		int bTokenCounter = 0;
		int aTokenCounter = 0;
		String nextAToken = getTokenAt(licenseATokens, aTokenCounter++);
		String nextBToken = getTokenAt(licenseBTokens, bTokenCounter++);
		while (nextAToken != null) {
			if (nextBToken == null) {
				// end of b stream
				while (canSkip(nextAToken)) {
					nextAToken = getTokenAt(licenseATokens, aTokenCounter++);
				}
				if (nextAToken != null) {
					return false;	// there is more stuff in the license text B, so not equal
				}
			} else if (tokensEquivalent(nextAToken, nextBToken)) { 
				// just move onto the next set of tokens
				nextAToken = getTokenAt(licenseATokens, aTokenCounter++);
				nextBToken = getTokenAt(licenseBTokens, bTokenCounter++);
			} else {
				// see if we can skip through some B tokens to find a match
				while (canSkip(nextBToken)) {
					nextBToken = getTokenAt(licenseBTokens, bTokenCounter++);
				}
				// just to be sure, skip forward on the A license
				while (canSkip(nextAToken)) {
					nextAToken = getTokenAt(licenseATokens, aTokenCounter++);
				}
				if (!tokensEquivalent(nextAToken, nextBToken)) {
					return false;
				} else {
					nextAToken = getTokenAt(licenseATokens, aTokenCounter++);
					nextBToken = getTokenAt(licenseBTokens, bTokenCounter++);
				}
			}
		}
		// need to make sure B is at the end
        while (canSkip(nextBToken)) {
			nextBToken = getTokenAt(licenseBTokens, bTokenCounter++);
		}
		return (nextBToken == null);
	}
	
	/**
	 * Tokenizes the license text, normalizes quotes, lowercases and converts
	 * multi-words for better equiv. comparisons
	 * 
	 * @param tokenToLocation location for all of the tokens
	 * @param licenseText text to tokenize
	 * @return tokens array of tokens from the licenseText
	 */
	public static String[] tokenizeLicenseText(String licenseText, Map<Integer, LineColumn> tokenToLocation) {
		String textToTokenize = normalizeText(replaceMultWord(replaceSpaceComma(licenseText))).toLowerCase();
		List<String> tokens = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new StringReader(textToTokenize))) {
            int currentLine = 1;
            int currentToken = 0;
            String line = reader.readLine();
            while (line != null) {
                line = removeLineSeparators(line);
                Matcher lineMatcher = TOKEN_SPLIT_PATTERN.matcher(line);
                while (lineMatcher.find()) {
                    String token = lineMatcher.group(1).trim();
                    if (!token.isEmpty()) {
                        tokens.add(token);
                        tokenToLocation.put(currentToken, new LineColumn(currentLine, lineMatcher.start(), token.length()));
                        currentToken++;
                    }
                    String fullMatch = lineMatcher.group(0);
                    for (int i = lineMatcher.group(1).length(); i < fullMatch.length(); i++) {
                        String possiblePunctuation = fullMatch.substring(i, i + 1);
                        if (PUNCTUATION.contains(possiblePunctuation)) {
                            tokens.add(possiblePunctuation);
                            tokenToLocation.put(currentToken, new LineColumn(currentLine, lineMatcher.start() + i, 1));
                            currentToken++;
                        }
                    }
                }
                currentLine++;
                line = reader.readLine();
            }
        } catch (IOException e) {
            // Don't fill in the lines, take a simpler approach
            Matcher m = TOKEN_SPLIT_PATTERN.matcher(textToTokenize);
            while (m.find()) {
                String word = m.group(1).trim();
                String seperator = m.group(2).trim();
                tokens.add(word);
                if (PUNCTUATION.contains(seperator)) {
                    tokens.add(seperator);
                }
            }
        }
        // ignore
        return tokens.toArray(new String[0]);
	}

	/**
	 * Just fetches the string at the index checking for range.  Returns null if index is out of range.
	 * @param tokens array of tokens
	 * @param tokenIndex index of token to retrieve
	 * @return the token at the index or null if the token does not exist
	 */
	public static String getTokenAt(String[] tokens, int tokenIndex) {
		if (tokenIndex >= tokens.length) {
			return null;
		} else {
			return tokens[tokenIndex];
		}
	}
	
	/**
	 * Returns true if the token can be ignored per the rules
	 * @param token token to check
	 * @return true if the token can be ignored per the rules
	 */
	public static boolean canSkip(String token) {
		if (token == null) {
			return false;
		}
		if (token.trim().isEmpty()) {
			return true;
		}
		return SKIPPABLE_TOKENS.contains(token.trim().toLowerCase());
	}
	
	/**
	 * Returns true if the two tokens can be considered equivalent per the SPDX license matching rules
	 * @param tokenA token to compare
	 * @param tokenB token to compare
	 * @return true if tokenA is equivalent to tokenB
	 */
	public static boolean tokensEquivalent(String tokenA, String tokenB) {
		if (tokenA == null) {
			return tokenB == null;
		} else if (tokenB == null) {
			return false;
		} else {
			String s1 = tokenA.trim().toLowerCase().replaceAll(DASHES_REGEX, "-");
			String s2 = tokenB.trim().toLowerCase().replaceAll(DASHES_REGEX, "-");
			if (s1.equals(s2)) {
				return true;
			} else {
				// check for equivalent tokens by normalizing the tokens
				String ns1 = NORMALIZE_TOKENS.get(s1);
				if (ns1 == null) {
					ns1 = s1;
				}
				String ns2 = NORMALIZE_TOKENS.get(s2);
				if (ns2 == null) {
					ns2 = s2;
				}
				return ns1.equals(ns2);
			}
		}
	}
	
	/**
	 * Replace different forms of space with a normalized space and different forms of commas with a normalized comma
	 * @param s input string
	 * @return input string replacing all UTF-8 spaces with " " and all UTF-8 commas with ","
	 */
	public static String replaceSpaceComma(String s) {
		Matcher spaceMatcher = SPACE_PATTERN.matcher(s);
		Matcher commaMatcher = COMMA_PATTERN.matcher(spaceMatcher.replaceAll(" "));
		return commaMatcher.replaceAll(",");
	}
	
	/**
	 * replaces all multi-words with a single token using a dash to separate
	 * @param s input string
	 * @return input string with all multi-words with a single token using a dash to separate
	 */
	public static String replaceMultWord(String s) {
		//TODO: There is certainly some room for optimization - perhaps a single regex in a find loop
		Matcher m = COPYRIGHT_HOLDERS_PATTERN.matcher(s);
		String retval = m.replaceAll("copyright-holders");
		m = COPYRIGHT_HOLDERS_PATTERN_LF.matcher(retval);
		retval = m.replaceAll("copyright-holders\n");
		m = COPYRIGHT_OWNERS_PATTERN.matcher(retval);
		retval = m.replaceAll("copyright-owners");
		m = COPYRIGHT_OWNERS_PATTERN_LF.matcher(retval);
		retval = m.replaceAll("copyright-owners\n");
		m = COPYRIGHT_HOLDER_PATTERN.matcher(retval);
		retval = m.replaceAll("copyright-holder");
		m = COPYRIGHT_HOLDER_PATTERN_LF.matcher(retval);
		retval = m.replaceAll("copyright-holder\n");
		m = COPYRIGHT_OWNER_PATTERN.matcher(retval);
		retval = m.replaceAll("copyright-owner");
		m = COPYRIGHT_OWNER_PATTERN_LF.matcher(retval);
		retval = m.replaceAll("copyright-owner\n");
		m = PER_CENT_PATTERN.matcher(retval);
		retval = m.replaceAll("percent");
		m = PER_CENT_PATTERN.matcher(retval);
		retval = m.replaceAll("percent\n");
		m = COPYRIGHT_SYMBOL_PATTERN.matcher(retval);
		retval = m.replaceAll("-c-");	// replace the parenthesis with a dash so that it results in a single token rather than 3
		return retval;
	}
	
	/**
	 * Normalize quotes and no-break spaces
	 * @param s String to normalize
	 * @return String normalized for comparison
	 */
	public static String normalizeText(String s) {
		// First normalize single quotes, then normalize two single quotes to a double quote, normalize double quotes 
		// then normalize non-breaking spaces to spaces
		return s.replaceAll("[‘’‛‚`]", "'")	// Take care of single quotes first
				.replace("http://", "https://") // Normalize the http protocol scheme
 				.replace("''","\"")			// This way, we can change doulbe single quotes to a single double cquote
				.replaceAll("[“”‟„]", "\"")	// Now we can normalize the double quotes
				.replaceAll("\\u00A0", " ")		// replace non-breaking spaces with spaces since Java does not handle the former well
				.replaceAll("[—–]","-")			// replace em dash, en dash with simple dash
				.replaceAll("\\u2028", "\n");	// replace line separator with newline since Java does not handle the former well
	}
	
	/**
	 * @param s Input string
	 * @return s without any line separators (---, ***, ===)
	 */
	public static String removeLineSeparators(String s) {
		return s.replaceAll("[-=*]{3,}\\s*$", "");  // Remove ----, ***,  and ====
	}
}
