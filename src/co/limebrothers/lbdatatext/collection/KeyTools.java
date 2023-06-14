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
package co.limebrothers.lbdatatext.collection;

import java.util.Arrays;

import co.limebrothers.lbdatatext.enums.ComparisonOperatorType;
import co.limebrothers.lbdatatext.enums.KeyType;
import co.limebrothers.lbdatatext.exceptions.InvalidOperatorException;

public class KeyTools {
	
	public static String[] getKeyValues(String value) {
		return getValues(getValueRaw(value));
	}
	
	private static String[] getValues(String valueRaw) {
		if (valueRaw.matches("\\d+\\,\\d+"))
			return valueRaw.split(",");
		else
			return new String[] { valueRaw };
	}
	
	private static String getValueRaw(String cmd) {
		String core = "";
		
		if (isParentheseValue(cmd))
			core = getCoreParenthese(cmd);
		
		if (isQuotValue(cmd))
			core = getCoreQuot(cmd);
		
		if ((core.length() ==1) && ((int)core.charAt(0) == 32)) {
			return core;
		}
		
		boolean openedQuot = false;
		String value = "";
		for (int i = 0; i < core.length(); i++) {
			if (core.charAt(i) == '"') {
				openedQuot = !openedQuot;
				continue;
			}
				
			if ((core.charAt(i) == ' ') && (!openedQuot))
				continue;
				
			if (((int)core.charAt(i) < 32) || ((int)core.charAt(i) > 126)) 
				continue;
			
			value += core.charAt(i);  				
		}
		
		return value;
	}
	
	private static boolean isParentheseValue(String cmd) {
		return cmd.indexOf('(') > -1;
	}
	
	private static boolean isQuotValue(String cmd) {
		return cmd.indexOf('"') > -1;
	}
	
	private static String getCoreParenthese(String cmd) {
		int firstLeftParentheseIndex = cmd.indexOf('(');
		int lastLeftParentheseIndex = -1;
		int lastLeftParentheseIndexTmp = -1;
		
		do {
			lastLeftParentheseIndex = lastLeftParentheseIndexTmp;
			lastLeftParentheseIndexTmp = cmd.indexOf(')', lastLeftParentheseIndex + 1);
		} while (lastLeftParentheseIndexTmp != -1);
		
		return cmd.substring(firstLeftParentheseIndex + 1, lastLeftParentheseIndex);
	}
	
	private static String getCoreQuot(String cmd) {
		int firstLeftQuotIndex = cmd.indexOf('"');
		int lastLeftQuotIndex = -1;
		int lastLeftQuotIndexTmp = -1;
		
		do {
			lastLeftQuotIndex = lastLeftQuotIndexTmp;
			lastLeftQuotIndexTmp = cmd.indexOf('"', lastLeftQuotIndex + 1);
		} while (lastLeftQuotIndexTmp != -1);
		
		return cmd.substring(firstLeftQuotIndex + 1, lastLeftQuotIndex);
	}
	
	public static void validateComparisonOperator(KeyType keyType, ComparisonOperatorType comparisonOperator) {
		ComparisonOperatorType[] comparisonOperatorForNumeric = new ComparisonOperatorType[] { ComparisonOperatorType.EQ, ComparisonOperatorType.GT, ComparisonOperatorType.GTE, ComparisonOperatorType.LT, ComparisonOperatorType.LTE };
		ComparisonOperatorType[] comparisonOperatorForString = new ComparisonOperatorType[] { ComparisonOperatorType.EQ, ComparisonOperatorType.REGEX };
		switch (keyType) {
		case INTEGER:
		case DATETIME:
		case DECIMAL:
			if (!Arrays.stream(comparisonOperatorForNumeric).anyMatch(t -> t == comparisonOperator))
				throw new InvalidOperatorException();
			
		case STRING:
			if (!Arrays.stream(comparisonOperatorForString).anyMatch(t -> t == comparisonOperator))
				throw new InvalidOperatorException();			
			break;

		default:
			break;
		}
	}
}
