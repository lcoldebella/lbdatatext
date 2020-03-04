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

import java.util.ArrayList;
import java.util.List;

import co.limebrothers.lbdatatext.document.DocumentDefinition;
import co.limebrothers.lbdatatext.document.Key;

public class CollectionFixedWidth extends Collection {
	private static final long serialVersionUID = 541191535271545757L;

	public CollectionFixedWidth() {
		super();
	}
	
	public CollectionFixedWidth(List<DocumentDefinition> documentDefinitions) {
		super(documentDefinitions);
	}
	
	@Override
	public List<String> getValue(List<String> data, String documentDescriptor, String keyName) {
		List<String> result = new ArrayList<String>();
		DocumentDefinition documentDefinition = getDocumentDefinitionByDescriptor(documentDescriptor);
		if (documentDefinition == null) return result;
		int lengthToKey = lengthToKey(documentDescriptor, keyName);
		Key key = getKeyByName(documentDefinition, keyName);
		if (key == null) return result;
		for (String line : data) {
			if (line.toUpperCase().startsWith(documentDescriptor.toUpperCase()))
				result.add(line.substring(lengthToKey, lengthToKey + key.getLength()));
		}
		return result;
	}
	
	private int lengthToKey(String documentDescriptor, String keyName) {
		DocumentDefinition documentDefinition = getDocumentDefinitionByDescriptor(documentDescriptor);
		if (documentDefinition == null) return 0;
		int length = 0;
		for (Key key : documentDefinition.getKeys()) {
			if (key.getName().equalsIgnoreCase(keyName))
				return length;
			else
				length+=key.getLength();
		}
		return 0;
	}
}
