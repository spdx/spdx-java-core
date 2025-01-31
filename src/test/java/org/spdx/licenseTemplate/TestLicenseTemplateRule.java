/**
 * SPDX-FileCopyrightText: Copyright (c) 2013 Source Auditor Inc.
 * SPDX-FileType: SOURCE
 * SPDX-License-Identifier: Apache-2.0
 */
package org.spdx.licenseTemplate;

import static org.junit.Assert.*;

import org.junit.Test;
import org.spdx.licenseTemplate.LicenseTemplateRule.RuleType;

/**
 * Test license template rule
 *
 * @author Source Auditor
 */
public class TestLicenseTemplateRule {

	static final String PARSEABLE_RULE = "var;original=Copyright (c) <year> <owner>\\nAll rights reserved.;match=Copyright \\(c\\) .+All rights reserved.;name=copyright;example=Copyright (C) 2013 John Doe\\nAll rights reserved.\n";
	static final String RULE_NAME = "copyright";
	static final RuleType RULE_TYPE = RuleType.VARIABLE;
	static final String RULE_ORIGINAL = "Copyright (c) <year> <owner>\nAll rights reserved.";
	static final String RULE_MATCH = "Copyright \\(c\\) .+All rights reserved.";
	static final String RULE_EXAMPLE = "Copyright (C) 2013 John Doe\nAll rights reserved.";

	@Test
	public void testParseLicenseTemplateRule() throws LicenseTemplateRuleException {
		LicenseTemplateRule rule = new LicenseTemplateRule("Name", RuleType.BEGIN_OPTIONAL, "original", "match", "example");
		rule.parseLicenseTemplateRule(PARSEABLE_RULE);
		assertEquals(RULE_EXAMPLE, rule.getExample());
		assertEquals(RULE_NAME, rule.getName());
		assertEquals(RULE_ORIGINAL, rule.getOriginal());
		assertEquals(RULE_TYPE, rule.getType());
		assertEquals(RULE_MATCH, rule.getMatch());
	}

	@Test
	public void testBeginOptionalRule() throws LicenseTemplateRuleException {
		String ruleName = "optionalName";
		String ruleStr = "beginOptional;name="+ruleName;
		LicenseTemplateRule rule = new LicenseTemplateRule(ruleStr);
		assertEquals(RuleType.BEGIN_OPTIONAL, rule.getType());
		assertEquals(ruleName, rule.getName());
	}

	@Test
	public void testEndOptionalRule() throws LicenseTemplateRuleException {
		String ruleStr = "endOptional";
		LicenseTemplateRule rule = new LicenseTemplateRule(ruleStr);
		assertEquals(RuleType.END_OPTIONAL, rule.getType());
	}
}
