/**
* LBDataText is an open source library for structured data text manipulation.
* Copyright LimeBrothers CO
* 
* @author  Leonardo Coldebella
* @version 1.0
* @since   2020-02-29
*  
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      https://www.apache.org/licenses/LICENSE-2.0
*      
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
* 
*/
package co.limebrothers.lbdatatext.document;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.limebrothers.lbdatatext.collection.KeyTools;
import co.limebrothers.lbdatatext.enums.KeyType;
import co.limebrothers.lbdatatext.enums.PaddingType;

public class Key implements Serializable {
	private static final long serialVersionUID = -3753732055179346896L;
	private static Pattern pDecimal = Pattern.compile("DECIMAL\\s*\\(\\d*\\,\\d*\\)");
	private static Pattern pLength = Pattern.compile("LENGTH\\s*\\([0-9]*\\)");
	private static Pattern pPadLeft = Pattern.compile("PADLEFT\\s*\\([\\w\" \"]*\\)");
	private static Pattern pPadRight = Pattern.compile("PADRIGHT\\s*\\([\\w\" \"]*\\)");
	private static Pattern pFormat = Pattern.compile("FORMAT\\s*\\([a-zA-Z:-]*\\)");
	private static Pattern pDecimalSeparator = Pattern.compile("DEC_SEPARATOR\\s*\\(\"[,. ]\"\\)");
	private String name;
	private KeyType keyType;
	private int length;
	private int integerPartSize;
	private int decimalPartSize;
	private String format;
	private String defaultValue;
	private PaddingType paddingType;
	private char paddingChar;
	private char decimalSeparator;
	
	public Key() { }

	public Key(String name, KeyType keyType, int length, int integerPartSize, int decimalPartSize, String format,
			String defaultValue, PaddingType paddingType, char paddingChar, char decimalSeparator) {
		super();
		this.name = name;
		this.keyType = keyType;
		this.length = length;
		this.integerPartSize = integerPartSize;
		this.decimalPartSize = decimalPartSize;
		this.format = format;
		this.defaultValue = defaultValue;
		this.paddingType = paddingType;
		this.paddingChar = paddingChar;
		this.decimalSeparator = decimalSeparator;
	}
	
	public Key(String name, KeyType keyType, int length, int integerPartSize, int decimalPartSize,
			String defaultValue) {
		super();
		this.name = name;
		this.keyType = keyType;
		this.length = length;
		this.integerPartSize = integerPartSize;
		this.decimalPartSize = decimalPartSize;
		this.format = "";
		this.defaultValue = defaultValue;
	}
	
	public Key(String name, KeyType keyType, int length, int integerPartSize, int decimalPartSize) {
		super();
		this.name = name;
		this.keyType = keyType;
		this.length = length;
		this.integerPartSize = integerPartSize;
		this.decimalPartSize = decimalPartSize;
		this.format = "";

		switch (keyType) {
		case STRING:
			this.defaultValue = "";	
			break;

		default:
			this.defaultValue = "0";
			break;
		}
	}

	public Key(String statement) {
		this.name = getKeyName(statement);
		this.keyType = getKeyTypeByDesc(statement);
		
		if (this.keyType == KeyType.DECIMAL) {
			String decimalDesc = getByPattern(statement, pDecimal);
			if (decimalDesc != null) {
				String[] v = KeyTools.getKeyValues(decimalDesc);
				this.integerPartSize = Integer.parseInt(v[0]);
				this.decimalPartSize = Integer.parseInt(v[1]);
				
				String decimalSeparatorDesc = getByPattern(statement, pDecimalSeparator);
				if (decimalSeparatorDesc != null) {
					String decimalSeparatorChar = KeyTools.getKeyValues(decimalSeparatorDesc)[0];
					this.decimalSeparator = decimalSeparatorChar.charAt(0);
				} else
					this.decimalSeparator = ' ';
			}			
		}
		
		String lengthDesc = getByPattern(statement, pLength);
		if (lengthDesc != null)
			this.length = Integer.parseInt(KeyTools.getKeyValues(lengthDesc)[0]);
		
		String PadDesc = getByPattern(statement, pPadLeft);
		if (PadDesc != null) {
			String paddingChar = KeyTools.getKeyValues(PadDesc)[0];
			this.paddingType = PaddingType.PAD_LEFT;
			this.paddingChar = paddingChar.charAt(0);
		} else {
			PadDesc = getByPattern(statement, pPadRight);
			if (PadDesc != null) {
				String paddingChar = KeyTools.getKeyValues(PadDesc)[0];
				this.paddingType = PaddingType.PAD_RIGHT;
				this.paddingChar = paddingChar.charAt(0);
			}
		}
						
		String formatDesc = getByPattern(statement, pFormat);
		if (formatDesc != null)
			this.format = KeyTools.getKeyValues(PadDesc)[0];
	}

	public String getName() {
		return name;
	}

	public KeyType getKeyType() {
		return keyType;
	}

	public int getLength() {
		return length;
	}

	public int getIntegerPartSize() {
		return integerPartSize;
	}

	public int getDecimalPartSize() {
		return decimalPartSize;
	}

	public String getFormat() {
		return format;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public PaddingType getPaddingType() {
		return paddingType;
	}

	public char getPaddingChar() {
		return paddingChar;
	}

	public char getDecimalSeparator() {
		return decimalSeparator;
	}

	@Override
	public String toString() {
		return String.format("name=%s, keyType=%s, length=%s, integerPartSize=%s, decimalPartSize=%s, format=%s, defaultValue=%s",
				this.name, this.keyType.toString(), this.length, this.integerPartSize, this.decimalPartSize,
				this.format, this.defaultValue);
	}
	
	private String getKeyName(String key) {
		return key.trim().split(" ")[0];
	}
	
	private static KeyType getKeyTypeByDesc(String key) {
		if (key.trim().toUpperCase().split(" ")[1].startsWith("STRING"))
			return KeyType.STRING;
		else if (key.trim().toUpperCase().split(" ")[1].startsWith("INTEGER"))
			return KeyType.INTEGER;
		else if (key.trim().toUpperCase().split(" ")[1].startsWith("DECIMAL"))
			return KeyType.DECIMAL;
		else if (key.trim().toUpperCase().split(" ")[1].startsWith("DATETIME"))
			return KeyType.DATETIME;
		else
			return null;
	}
	
	private String getByPattern(String statement, Pattern pattern) {
		Matcher m = pattern.matcher(statement);
		
		if (m.find())
			return m.group();
		else
			return null;
	}
}
