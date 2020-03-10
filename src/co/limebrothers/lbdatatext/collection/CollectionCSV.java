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
import co.limebrothers.lbdatatext.enums.ComparisonOperatorType;

public class CollectionCSV extends Collection  {
	private static final long serialVersionUID = 5322356515050547346L;
	private String delimiter;
	
	public CollectionCSV() {
		super();
	}
	
	public CollectionCSV(String delimiter, List<DocumentDefinition> documentDefinitions) {
		super(documentDefinitions);
		this.delimiter = delimiter;
	}
	
	@Override
	public List<String> getValue(List<String> data, String documentDescriptor, String keyName) {
		List<String> result = new ArrayList<String>();
		int index = indexOf(documentDescriptor, keyName);
		if (index == -1) return result;
		for (String line : data) {
			if (line.toUpperCase().startsWith(documentDescriptor.toUpperCase()))
				result.add(line.split(delimiter)[index]);
		}
		return result;
	}

	@Override
	public <T> List<String> find(List<String> data, String documentDescriptor, String keyName,
			ComparisonOperatorType comparison, T value) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private int indexOf(String documentDescriptor, String keyName) {
		DocumentDefinition documentDataDefinition = getDocumentDefinitionByDescriptor(documentDescriptor);
		if (documentDataDefinition == null) return -1;
		return indexOf(documentDataDefinition, keyName);
	}
	
	private int indexOf(DocumentDefinition documentDefinition, String keyName) {
		int count = 0;
		for (Key key : documentDefinition.getKeys()) {
			if (key.getName().equalsIgnoreCase(keyName))
				return count + 1;
			else
				count++;
		}
		return -1;
	}
}
