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

import java.util.HashMap;
import java.util.List;

import co.limebrothers.lbdatatext.document.DocumentDefinition;
import co.limebrothers.lbdatatext.enums.DocumentStyleType;

public class CollectionFactory {
	public static Collection get(DocumentStyleType value, String delimiter, List<DocumentDefinition> documentDefinitions) {
		HashMap<DocumentStyleType, Collection> map = new HashMap<>();
		map.put(DocumentStyleType.FIXED_WIDTH, new CollectionFixedWidth(documentDefinitions));
		map.put(DocumentStyleType.CSV, new CollectionCSV(delimiter, documentDefinitions));	
		return map.get(value);
	}
}
