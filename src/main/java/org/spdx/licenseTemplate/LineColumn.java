/**
 * SPDX-FileCopyrightText: Copyright (c) 2019 Source Auditor Inc.
 * SPDX-FileType: SOURCE
 * SPDX-License-Identifier: Apache-2.0
 */
package org.spdx.licenseTemplate;

/**
 * Holds information on lines and columns
 * @author Gary O'Neall
 *
 */
public class LineColumn {
	private int line;
	private int column;
	private int len;
	
	public LineColumn(int line, int column,int len) {
		this.line = line;
		this.column = column;
		this.len = len;
	}

	/**
	 * @return line number
	 */
	public int getLine() {
		return line;
	}

	/**
	 * @param line line number
	 */
	public void setLine(int line) {
		this.line = line;
	}

	/**
	 * @return column
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * @param column column
	 */
	@SuppressWarnings("unused")
    public void setColumn(int column) {
		this.column = column;
	}

	/**
	 * @return length of the line
	 */
	public int getLen() {
		return len;
	}

	@SuppressWarnings("unused")
    public void setLen(int len) {
		this.len = len;
	}		
}
