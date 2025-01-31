/**
 * SPDX-FileCopyrightText: Copyright (c) 2013 Source Auditor Inc.
 * SPDX-FileType: SOURCE
 * SPDX-License-Identifier: Apache-2.0
 */
package org.spdx.licenseTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Implements common conversion methods for processing SPDX license templates
 * 
 * @author Gary O'Neall
 */
public class SpdxLicenseTemplateHelper {

	static final String START_RULE = "<<";
	static final String END_RULE = ">>";
	public static final Pattern RULE_PATTERN = Pattern.compile(START_RULE + "\\s*(beginOptional|endOptional|var)");
	public static final Pattern END_RULE_PATTERN = Pattern.compile(END_RULE);
	private static final int SPACES_PER_TAB = 5;
	private static final int MAX_TABS = 4;
	private static final int[] PIXELS_PER_TAB = new int[] { 20, 40, 60, 70 };
	
	private SpdxLicenseTemplateHelper() {
		// Utility class - it should not be instantiated
	}

	/**
	 * Parses the license template calling the templateOutputHandler for any text
	 * and rules found
	 * 
	 * @param licenseTemplate       License template to be parsed
	 * @param templateOutputHandler Handles the text, optional text, and variable
	 *                              rules text found
	 * @throws LicenseTemplateRuleException if the rule can not be parsed
	 * @throws LicenseParserException if the license can not be parsed
	 */
	public static void parseTemplate(String licenseTemplate, ILicenseTemplateOutputHandler templateOutputHandler)
			throws LicenseTemplateRuleException, LicenseParserException {
		Matcher ruleMatcher = RULE_PATTERN.matcher(licenseTemplate);
		Matcher endRuleMatcher = END_RULE_PATTERN.matcher(licenseTemplate);
		int end = 0;
		int optionalNestLevel = 0;
		int start = 0;
		while (ruleMatcher.find(start)) {
			// copy everything up to the start of the find
			String upToTheFind = licenseTemplate.substring(end, ruleMatcher.start());
			if (!upToTheFind.trim().isEmpty()) {
				templateOutputHandler.text(upToTheFind);
			}
			if (!endRuleMatcher.find(ruleMatcher.end())) {
				throw (new LicenseTemplateRuleException("Missing end of rule '>>' after text '" + upToTheFind + "'"));
			}
			end = endRuleMatcher.end();
			String ruleString = licenseTemplate.substring(ruleMatcher.start() + START_RULE.length(), end - END_RULE.length());
			start = end;

			LicenseTemplateRule rule = new LicenseTemplateRule(ruleString);
			if (rule.getType() == LicenseTemplateRule.RuleType.VARIABLE) {
				templateOutputHandler.variableRule(rule);
			} else if (rule.getType() == LicenseTemplateRule.RuleType.BEGIN_OPTIONAL) {
				templateOutputHandler.beginOptional(rule);
				optionalNestLevel++;
			} else if (rule.getType() == LicenseTemplateRule.RuleType.END_OPTIONAL) {
				optionalNestLevel--;
				if (optionalNestLevel < 0) {
					throw (new LicenseTemplateRuleException(
							"End optional rule found without a matching begin optional rule after text '" + upToTheFind + "'"));
				}
				templateOutputHandler.endOptional(rule);
			} else {
				throw (new LicenseTemplateRuleException(
						"Unrecognized rule: " + rule.getType().toString() + " after text '" + upToTheFind + "'"));
			}
		}
		if (optionalNestLevel > 0) {
			throw (new LicenseTemplateRuleException("Missing EndOptional rule and end of text"));
		}
		// copy the rest of the template to the end
		String restOfTemplate = licenseTemplate.substring(end);
		if (!restOfTemplate.isEmpty()) {
			templateOutputHandler.text(restOfTemplate);
		}
		templateOutputHandler.completeParsing();
	}

	/**
	 * Converts a license template string to formatted HTML which highlights any
	 * rules or tags
	 * @param licenseTemplate standard license template
	 * @return an HTML representation of the license template
	 * @throws LicenseTemplateRuleException on a rule parsing errors
	 */
	public static String templateTextToHtml(String licenseTemplate) throws LicenseTemplateRuleException {
		HtmlTemplateOutputHandler htmlOutput = new HtmlTemplateOutputHandler();
		try {
			parseTemplate(licenseTemplate, htmlOutput);
		} catch (LicenseParserException e) {
			throw new LicenseTemplateRuleException("Parsing error parsing license template", e);
		}
		return htmlOutput.getHtml();
	}

	/**
	 * Converts template text to standard default text using any default parameters
	 * in the rules
	 * 
	 * @param template standard license template
	 * @return text representation of the license
	 * @throws LicenseTemplateRuleException on any rule parsing errors
	 */
	public static String templateToText(String template) throws LicenseTemplateRuleException {
		TextTemplateOutputHandler textOutput = new TextTemplateOutputHandler();
		try {
			parseTemplate(template, textOutput);
		} catch (LicenseParserException e) {
			throw new LicenseTemplateRuleException("Parsing error parsing license template", e);
		}
		return textOutput.getText();
	}

	/**
	 * Escapes and formats text
	 * 
	 * @param text unformatted text
	 * @return text suitable for inclusion in HTML
	 */
	public static String formatEscapeHTML(String text) {
		return formatEscapeHTML(text, false);
	}

	/**
	 * Escapes and formats text
	 * 
	 * @param text unformatted text
	 * @param inParagraph true if inside a paragraph tag
	 * @return text escaped and formatted for inclusion in HTML
	 */
	public static String formatEscapeHTML(String text, boolean inParagraph) {
		String retval = StringEscapeUtils.escapeXml11(text);
		return addHtmlFormatting(retval, inParagraph);
	}

	/**
	 * Adds HTML formatting {@code <br>
	 * } and {@code 
	 * <p>
	 * }
	 * 
	 * @param text unformatted text
	 * @return HTML formatted text
	 */
	public static String addHtmlFormatting(String text) {
		return addHtmlFormatting(text, false);
	}

	/**
	 * Adds HTML formatting {@code <br>
	 * } and {@code 
	 * <p>
	 * }
	 * 
	 * @param text        unformatted text
	 * @param inParagraph true if inside a paragraph tag
	 * @return HTML formatted text
	 */
	public static String addHtmlFormatting(String text, boolean inParagraph) {
		String[] lines = text.split("\n");
		StringBuilder result = new StringBuilder();
		result.append(lines[0]);
		int i = 1;
		while (i < lines.length) {
			if (lines[i].trim().isEmpty()) {
				// paragraph boundary
				if (inParagraph) {
					result.append("</p>");
				}
				result.append("\n");
				i++;
				if (i < lines.length) {
					String paragraphTag = getParagraphTagConsideringTags(lines[i]);
					result.append(paragraphTag);
					result.append(lines[i++]);
				} else {
					result.append("<p>");
				}
				inParagraph = true;
			} else {
				// just a line break
				result.append("<br/>");
				result.append("\n");
				result.append(lines[i++]);
			}
		}
		if (inParagraph) {
			result.append("</p>");
		} else if (text.endsWith("\n")) {
			result.append("<br/>\n");
		}
		return result.toString();
	}

	/**
	 * Creating a paragraph tag and add the correct margin considering the number of
	 * spaces or tabs
	 * 
	 * @param line line of license text
	 * @return text including any paragraph tags
	 */
	private static String getParagraphTagConsideringTags(String line) {
		int numSpaces = countLeadingSpaces(line);
		StringBuilder result = new StringBuilder();
		if (numSpaces >= SPACES_PER_TAB) {
			int numTabs = numSpaces / SPACES_PER_TAB;
			if (numTabs > MAX_TABS) {
				numTabs = MAX_TABS;
			}

			int pixels = PIXELS_PER_TAB[numTabs - 1];
			result.append("<p style=\"margin-left: ");
			result.append(pixels);
			result.append("px;\">");
		} else {
			result.append("<p>");
		}
		return result.toString();
	}

	/**
	 * Counts the number of leading spaces in a given line
	 * 
	 * @param string input string
	 * @return number of leading spaces
	 */
	private static int countLeadingSpaces(String string) {
		char[] charArray = string.toCharArray();
		int retval = 0;
		while (retval < charArray.length && charArray[retval] == ' ') {
			retval++;
		}
		return retval;
	}

	/**
	 * Converts an HTML string to text preserving line breaks for {@code <br/>
	 * } tags
	 * 
	 * @param html string of HTML formatted text
	 * @return text representation of the HTML
	 */
	public static String htmlToText(String html) {
		String newlineString = "NewLineGoesHere";
		String replaceBrs = html.replaceAll("(?i)<br[^>]*>", newlineString);
		String replaceBrsAndPs = replaceBrs.replaceAll("(?i)<p[^>]*>", newlineString);
		Document doc = Jsoup.parse(replaceBrsAndPs);
		String retval = doc.text();
		retval = retval.replace(newlineString, "\n");
		return retval;
	}

}
