/**
 * SPDX-FileCopyrightText: Copyright (c) 2013 Source Auditor Inc.
 * SPDX-FileType: SOURCE
 * SPDX-License-Identifier: Apache-2.0
 */
package org.spdx.licenseTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Implements a license rule
 * @author Gary O'Neall
 *
 */
public class LicenseTemplateRule {
	
	public enum RuleType {VARIABLE, BEGIN_OPTIONAL, END_OPTIONAL}
	
	RuleType type;
	String original = null;
	String name;
	String example = null;
	String match = null;
	
	static final Pattern SPLIT_REGEX = Pattern.compile("[^\\\\];");
	private static final String EXAMPLE_KEYWORD = "example";
	private static final String NAME_KEYWORD = "name";
	private static final String ORIGINAL_KEYWORD = "original";
	private static final String MATCH_KEYWORD = "match";
	private static final String VARIABLE_RULE_TYPE_STR = "var";
	private static final String BEGIN_OPTIONAL_TYPE_STR = "beginOptional";
	private static final String END_OPTIONAL_TYPE_STR = "endOptional";
	private static final String VALUE_SEPARATOR = "=";
	
	/**
	 * Create a new LicenseTemplateRule
	 * @param name Name of the rule - must not be null
	 * @param type Type of rule
	 * @param original Original text - must not be null
	 * @param example Example text - may be null
	 * @throws LicenseTemplateRuleException if the license template could not be parsed
	 */
	public LicenseTemplateRule(String name, RuleType type, String original, String match, String example) throws LicenseTemplateRuleException {
		this.type = type;
		this.original = formatValue(original);
		this.name = name;
		this.example = formatValue(example);
		this.match = match;
		validate();
	}
	
	@Override
	public String toString() {
		if (RuleType.VARIABLE.equals(this.type)) {
			String myName = name;
			if (myName == null) {
				myName = "";
			}
			return "var: "+myName;
		} else if (RuleType.BEGIN_OPTIONAL.equals(this.type)) {
			return BEGIN_OPTIONAL_TYPE_STR;
		} else if (RuleType.END_OPTIONAL.equals(this.type)) {
			return END_OPTIONAL_TYPE_STR;
		} else {
			return "Unknown";
		}
	}
	
	/**
	 * Validates that the LicenseTemplateRule is properly initialized
	 * @throws LicenseTemplateRuleException if the license template could not be parsed
	 */
	public void validate() throws LicenseTemplateRuleException {
		if (this.type == null) {
			throw new LicenseTemplateRuleException("Rule type can not be null.");
		}
		if (this.type == RuleType.VARIABLE && this.name == null) {
			throw new LicenseTemplateRuleException("Rule name can not be null for a variable or alt rule.");
		}
		if (this.type == RuleType.VARIABLE && this.original == null) {
			throw new LicenseTemplateRuleException("Rule original text can not be null.");
		}
		if (this.type == RuleType.VARIABLE && this.match == null) {
			throw new LicenseTemplateRuleException("Rule match regular expression can not be null.");
		}
	}

	/**
	 * Create a new License Template Rule by parsing a rule string compliant with the SPDX
	 * License Template text
	 * @param parseableLicenseTemplateRule license template rule string
	 * @throws LicenseTemplateRuleException if the license template could not be parsed
	 */
	public LicenseTemplateRule(String parseableLicenseTemplateRule) throws LicenseTemplateRuleException {
		parseLicenseTemplateRule(parseableLicenseTemplateRule);
		validate();
	}

	/**
	 * @param ruleName rule name
	 * @param ruleType rule type
	 * @throws LicenseTemplateRuleException if the license template could not be parsed
	 */
	public LicenseTemplateRule(String ruleName, RuleType ruleType) throws LicenseTemplateRuleException {
		this.name = ruleName;
		this.type = ruleType;
		validate();
	}

	/**
	 * Parse a license template rule string compliant with the SPDX license template text and
	 * replace all properties with the parsed values
	 * @param parseableLicenseTemplateRule String representation of a license template rule
	 * @throws LicenseTemplateRuleException if the license template could not be parsed
	 */
	public void parseLicenseTemplateRule(String parseableLicenseTemplateRule) throws LicenseTemplateRuleException {
		//TODO: Check for repeated keywords
		this.example = null;
		this.name = null;
		this.original = null;
		this.type = null;
		this.match = null;
		Matcher rulePartMatcher = SPLIT_REGEX.matcher(parseableLicenseTemplateRule);
		int start = 0;
		// parse out the first field - should be the rule type
		String typeStr;
		if (rulePartMatcher.find()) {
			typeStr = parseableLicenseTemplateRule.substring(start, rulePartMatcher.start()+1).trim();
			start = rulePartMatcher.end();
		} else {
			typeStr = parseableLicenseTemplateRule.trim();
			start = parseableLicenseTemplateRule.length();
		}
		this.type = typeStringToType(typeStr);
		
		// parse out remaining fields
		while (rulePartMatcher.find()) {
			String rulePart = parseableLicenseTemplateRule.substring(start, rulePartMatcher.start()+1);
			parseRulePart(rulePart.trim());
			start = rulePartMatcher.end();
		}
		String remainingRuleString = parseableLicenseTemplateRule.substring(start).trim();
		if (!remainingRuleString.isEmpty()) {
			parseRulePart(remainingRuleString);
		}
		validate();
	}

	/**
	 * @param typeStr string representing the type of rule
	 * @return rule type
	 * @throws LicenseTemplateRuleException if the license template could not be parsed
	 */
	private RuleType typeStringToType(String typeStr) throws LicenseTemplateRuleException {
        switch (typeStr) {
            case VARIABLE_RULE_TYPE_STR:
                return RuleType.VARIABLE;
            case BEGIN_OPTIONAL_TYPE_STR:
                return RuleType.BEGIN_OPTIONAL;
            case END_OPTIONAL_TYPE_STR:
                return RuleType.END_OPTIONAL;
            default:
                throw new LicenseTemplateRuleException("Unknown rule type: " + typeStr);
        }
	}
	
	/**
	 * @return rule type
	 */
	public RuleType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(RuleType type) {
		this.type = type;
	}

	/**
	 * @return the original
	 */
	public String getOriginal() {
		return original;
	}

	/**
	 * @param original the original to set
	 */
	public void setOriginal(String original) {
		this.original = original;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the example
	 */
	public String getExample() {
		return example;
	}

	/**
	 * @param example the example to set
	 */
	public void setExample(String example) {
		this.example = example;
	}

	/**
	 * @return the match
	 */
	public String getMatch() {
		return match;
	}

	/**
	 * @param match the match to set
	 */
	public void setMatch(String match) {
		this.match = match;
	}

	/**
	 * Parse the part of a rule and stores the result as a property
	 * @param rulePart string representation of the license rule
	 * @throws LicenseTemplateRuleException if the license template could not be parsed
	 */
	private void parseRulePart(String rulePart) throws LicenseTemplateRuleException {
		if (rulePart.startsWith(EXAMPLE_KEYWORD)) {
			String value = getValue(rulePart, EXAMPLE_KEYWORD);
			this.example = formatValue(value);
		} else if (rulePart.startsWith(NAME_KEYWORD)) {
			this.name = getValue(rulePart, NAME_KEYWORD);
		} else if (rulePart.startsWith(ORIGINAL_KEYWORD)) {
			String value = getValue(rulePart, ORIGINAL_KEYWORD);
			this.original = formatValue(value);
		} else if (rulePart.startsWith(MATCH_KEYWORD)) {
			this.match = getValue(rulePart, MATCH_KEYWORD);
		} else {
			throw new LicenseTemplateRuleException("Unknown rule keyword: "+rulePart);
		}
	}

	/**
	 * Formats the string interpreting escape characters
	 * @param value string to format
	 * @return formatted string
	 */
	private String formatValue(String value) {
		String retval = value.replace("\\n", "\n");
		retval = retval.replace("\\t", "\t");
		return retval;
	}

	/**
	 * Retrieve the value portion of a rule part
	 * @param rulePart the rule part in string format
	 * @param keyword keyword
	 * @return the value portion of a rule part
	 * @throws LicenseTemplateRuleException if the license template could not be parsed
	 */
	private String getValue(String rulePart, String keyword) throws LicenseTemplateRuleException {
		String retval = rulePart.substring(keyword.length());
		retval = retval.trim();
		if (!retval.startsWith(VALUE_SEPARATOR)) {
			throw new LicenseTemplateRuleException("Missing "+VALUE_SEPARATOR+" for "+keyword);
		}
		retval = retval.substring(1).trim();
		if (retval.startsWith("\"")) {
			retval = retval.substring(1);
		}
		if (retval.endsWith("\"")) {
			retval = retval.substring(0, retval.length()-1);
		}
		return retval;
	}

}
