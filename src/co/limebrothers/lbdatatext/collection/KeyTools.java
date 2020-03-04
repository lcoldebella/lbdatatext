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
		String core = getCore(cmd);
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
	
	private static String getCore(String cmd) {
		int firstLeftParentheseIndex = cmd.indexOf('(');
		int lastLeftParentheseIndex = -1;
		int lastLeftParentheseIndexTmp = -1;
		
		do {
			lastLeftParentheseIndex = lastLeftParentheseIndexTmp;
			lastLeftParentheseIndexTmp = cmd.indexOf(')', lastLeftParentheseIndex + 1);
		} while (lastLeftParentheseIndexTmp != -1);
		
		return cmd.substring(firstLeftParentheseIndex + 1, lastLeftParentheseIndex);
	}
}
