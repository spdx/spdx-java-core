/**
 * Copyright (c) 2013 Source Auditor Inc.
 * Copyright (c) 2013 Black Duck Software Inc.
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
*/
package org.spdx.licenseTemplate;


import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * @author Gary O'Neall
 *
 */
public class LicenseTextHelperTest extends TestCase {
	

    

	/**
	 * @throws java.lang.Exception
	 */
	@Override
	public void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link org.spdx.licenseTemplate.LicenseTextHelper#isLicenseTextEquivalent(java.lang.String, java.lang.String)}.
	 */
	public void testLicensesMatch() {
		// equal strings
		String testA = "Now is the time  for all . good. men/to \\come to the aid of their country.";
		boolean result = LicenseTextHelper.isLicenseTextEquivalent(testA, testA);
		assertTrue(result);
		// b is longer
		String testB = testA + " A bit longer";
		result = LicenseTextHelper.isLicenseTextEquivalent(testA, testB);
		assertFalse(result);
		// first parameter is longer
		result = LicenseTextHelper.isLicenseTextEquivalent(testB, testA);
		assertFalse(result);
		// white space doesn't matter
		String testPart1 = "Now is the time ";
		String testPart2 = " for all good men";
		String whiteSpace = " \t\n\r";
		testA = testPart1 + testPart2;
		testB = testPart1 + whiteSpace + testPart2;
		result = LicenseTextHelper.isLicenseTextEquivalent(testA, testB);
		assertTrue(result);
		// trailing white space
		testB = testA + whiteSpace;
		result = LicenseTextHelper.isLicenseTextEquivalent(testA, testB);
		assertTrue(result);
		result = LicenseTextHelper.isLicenseTextEquivalent(testB, testA);
		assertTrue(result);
		// preceeding white space
		testB = whiteSpace + testA;
		result = LicenseTextHelper.isLicenseTextEquivalent(testA, testB);
		assertTrue(result);
		result = LicenseTextHelper.isLicenseTextEquivalent(testB, testA);
		assertTrue(result);
		// case shouldnt matter
		result = LicenseTextHelper.isLicenseTextEquivalent(testA, testA.toUpperCase());
		assertTrue(result);
		// punctuation should matter
		testA = testPart1 + testPart2;
		String punctuation = ",";
		testB = testPart1 + punctuation + testPart2;
		result = LicenseTextHelper.isLicenseTextEquivalent(testA, testB);
		assertFalse(result);
		// dash variations
		testA = testPart1 + "-" + testPart2;
		testB = testPart1 + "\u2013" + testPart2;
		result = LicenseTextHelper.isLicenseTextEquivalent(testA, testB);
		assertTrue(result);
		testB = testPart1 + "\u2014" + testPart2;
		result = LicenseTextHelper.isLicenseTextEquivalent(testA, testB);
		assertTrue(result);
		testB = testPart1 + "\u2015" + testPart2;
		result = LicenseTextHelper.isLicenseTextEquivalent(testA, testB);
		assertTrue(result);
	}
	
	public void testLicenseMatchCodeComments() {
		String part1 = " now is the time for all good men\n";
		String part2 = "\tto come to the aid ";
		// c style line comment
		String cCommentLine = "//";
		String testA = part1 + part2;
		String testB = cCommentLine + part1 + cCommentLine + part2 + "\n"+ cCommentLine;
		boolean result = LicenseTextHelper.isLicenseTextEquivalent(testA, testB);
		assertTrue(result);
		result = LicenseTextHelper.isLicenseTextEquivalent(testB, testA);
		assertTrue(result);
		// c style multi line
		String startCMulti = "/*";
		String endCMulti = "*/";
		testB = startCMulti + part1 + part2 + endCMulti;
		result = LicenseTextHelper.isLicenseTextEquivalent(testA, testB);
		assertTrue(result);
		result = LicenseTextHelper.isLicenseTextEquivalent(testB, testA);
		assertTrue(result);
		// javaDocs comments
		String startJavaDocs = "/**";
		testB = startJavaDocs + part1 + part2 + endCMulti;
		result = LicenseTextHelper.isLicenseTextEquivalent(testA, testB);
		assertTrue(result);
		result = LicenseTextHelper.isLicenseTextEquivalent(testB, testA);
		assertTrue(result);
		// script line comment
		String scriptLineComment = "#";
		testB = scriptLineComment + part1 + scriptLineComment + part2 + "\n"+ scriptLineComment;
		result = LicenseTextHelper.isLicenseTextEquivalent(testA, testB);
		assertTrue(result);
		result = LicenseTextHelper.isLicenseTextEquivalent(testB, testA);
		assertTrue(result);
	}
	
	public void testLicenseMatchEquivWords() {
		// per cent -> percent
		String part1 = "now is the time for ";
		String testA = part1 + "per cent";
		String testB = part1 + "percent";
		boolean result = LicenseTextHelper.isLicenseTextEquivalent(testA, testB);
		assertTrue(result);
		// copyright holder-> copyright owner
		testA = "Copyright holder "+part1;
		testB = "copyright Owner "+ part1;
		result = LicenseTextHelper.isLicenseTextEquivalent(testA, testB);
		assertTrue(result);
		// "license","licence"
		testA = part1 + " license " + part1;
		testB = part1 + " licence " + part1;
		result = LicenseTextHelper.isLicenseTextEquivalent(testA, testB);
		assertTrue(result);
	}	
	
	public void testTokenizeLicenseText() {
		String test = "Now is the.time,for? \"all\" good men.";
		Map<Integer, LineColumn> tokenToLocation = new HashMap<Integer, LineColumn>();
		String[] result = LicenseTextHelper.tokenizeLicenseText(test, tokenToLocation);
		assertEquals(14,result.length);
		assertEquals("now",result[0]);
		assertEquals("is",result[1]);
		assertEquals("the",result[2]);
		assertEquals(".",result[3]);
		assertEquals("time",result[4]);
		assertEquals(",",result[5]);
		assertEquals("for",result[6]);
		assertEquals("?",result[7]);
		assertEquals("\"",result[8]);
		assertEquals("all",result[9]);
		assertEquals("\"",result[10]);
		assertEquals("good",result[11]);
		assertEquals("men",result[12]);
		assertEquals(".",result[13]);
		assertEquals(0,tokenToLocation.get(0).getColumn());
		assertEquals(4,tokenToLocation.get(1).getColumn());
		assertEquals(7,tokenToLocation.get(2).getColumn());
		assertEquals(10,tokenToLocation.get(3).getColumn());
		assertEquals(11,tokenToLocation.get(4).getColumn());
		assertEquals(15,tokenToLocation.get(5).getColumn());
		assertEquals(16,tokenToLocation.get(6).getColumn());
		assertEquals(19,tokenToLocation.get(7).getColumn());
		assertEquals(21,tokenToLocation.get(8).getColumn());
		assertEquals(22,tokenToLocation.get(9).getColumn());
		assertEquals(25,tokenToLocation.get(10).getColumn());
		assertEquals(27,tokenToLocation.get(11).getColumn());
		assertEquals(32,tokenToLocation.get(12).getColumn());
		assertEquals(35,tokenToLocation.get(13).getColumn());
	}
	
	public void regressionTokenString() {
		String test = "THIS SOFTWARE IS PROVIDED BY COPYRIGHT HOLDER \"AS IS\" AND";
		Map<Integer, LineColumn> tokenToLocation = new HashMap<Integer, LineColumn>();
		String[] result = LicenseTextHelper.tokenizeLicenseText(test, tokenToLocation);
		assertEquals(11, result.length);
		assertEquals("this",result[0]);
		assertEquals("software",result[1]);
		assertEquals("is",result[2]);
		assertEquals("provided",result[3]);
		assertEquals("by",result[4]);
		assertEquals("copyright-holder",result[5]);
		assertEquals("\"",result[6]);
		assertEquals("as",result[7]);
		assertEquals("is",result[8]);
		assertEquals("\"",result[9]);
		assertEquals("and",result[10]);
		assertEquals(0,tokenToLocation.get(0).getColumn());
		assertEquals(5,tokenToLocation.get(1).getColumn());
		assertEquals(14,tokenToLocation.get(2).getColumn());
		assertEquals(17,tokenToLocation.get(3).getColumn());
		assertEquals(26,tokenToLocation.get(4).getColumn());
		assertEquals(29,tokenToLocation.get(5).getColumn());
		assertEquals(46,tokenToLocation.get(6).getColumn());
		assertEquals(47,tokenToLocation.get(7).getColumn());
		assertEquals(50,tokenToLocation.get(8).getColumn());
		assertEquals(52,tokenToLocation.get(9).getColumn());
		assertEquals(54,tokenToLocation.get(10).getColumn());
	}
	
	public void testOddChars() {
		String test = "COPYRIGHT   I B M   CORPORATION 2002";
		Map<Integer, LineColumn> tokenToLocation = new HashMap<Integer, LineColumn>();
		String[] result = LicenseTextHelper.tokenizeLicenseText(test, tokenToLocation);
		assertEquals(6,result.length);
		assertEquals("copyright", result[0]);
		assertEquals("i", result[1]);
		assertEquals("b", result[2]);
		assertEquals("m", result[3]);
		assertEquals("corporation", result[4]);
		assertEquals("2002", result[5]);
		test = "Claims      If";
		result = LicenseTextHelper.tokenizeLicenseText(test, tokenToLocation);
		assertEquals(2, result.length);
		assertEquals("claims",result[0]);
		assertEquals("if", result[1]);	
	}
	
	public void testSpaceNormalization() {
		String t1 = "This is a test of space extra";
		String t2 = "This is\u2060a\u2007test\u202Fof space\u2009extra";
		assertTrue(LicenseTextHelper.isLicenseTextEquivalent(t1, t2));
	}
	
	   public void testCommaNormalization() {
	        String t1 = "This, is, a,test , of commas";
	        String t2 = "This\uFE10 is\uFF0C a\uFE50test , of commas";
	        assertTrue(LicenseTextHelper.isLicenseTextEquivalent(t1, t2));
	    }
}
