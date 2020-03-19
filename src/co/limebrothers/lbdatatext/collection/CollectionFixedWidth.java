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
import java.util.stream.Collectors;

import co.limebrothers.lbdatatext.document.DocumentDefinition;
import co.limebrothers.lbdatatext.document.Key;
import co.limebrothers.lbdatatext.enums.ComparisonOperatorType;
import co.limebrothers.lbdatatext.exceptions.KeyNotFoundException;

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
		Key key = getKeyByName(documentDefinition, keyName);
		if (key == null) return result;
		int lengthToKey = lengthUpToKey(documentDefinition, key);				
		for (String line : data) {
			if (line.toUpperCase().startsWith(documentDescriptor.toUpperCase()))
				result.add(line.substring(lengthToKey, lengthToKey + key.getLength()));
		}
		return result;
	}
	
	@Override
	public <T> List<String> find(List<String> data, String documentDescriptor, String keyName,
			ComparisonOperatorType comparison, T value) {
		return find(data, documentDescriptor, keyName, comparison, value, null);
	}

	@Override
	public <T> List<String> find(List<String> data, String documentDescriptor, String keyName,
			ComparisonOperatorType comparison, T value, String[] returnKeys) {
		DocumentDefinition doc = getDocumentDefinitionByDescriptor(documentDescriptor);
		Key key = doc.getKeys().stream().filter(k -> k.getName().equalsIgnoreCase(keyName)).findAny().orElseThrow(KeyNotFoundException::new);
		KeyTools.validateComparisonOperator(key.getKeyType(), comparison);
		int keyStartIndex = lengthUpToKey(doc, key);
		int len = key.getLength();
		
		switch (key.getKeyType()) {
		case STRING:
			return findString(data, documentDescriptor, keyStartIndex, len, (String)value, comparison, returnKeys);
			
		case INTEGER:
			return findInteger(data, documentDescriptor, keyStartIndex, len, (int)value, comparison);

		default:
			return null;
		}
	}
	
	private List<String> findString(List<String> data, String documentDescriptor, int keyStartIndex, int keyLength, String value, ComparisonOperatorType comparison, String[] returnKeys) {
		List<String> r0 = findString(data, documentDescriptor, keyStartIndex, keyLength, (String)value, comparison);
		
		if (returnKeys == null)
			return r0;
		else {
			List<String> r1 = new ArrayList<String>();
			for (String document : r0) {
				StringBuilder sb = new StringBuilder();
				for (String keyName : returnKeys) {
					sb.append(getValue(document, documentDescriptor, keyName));
				}
				r1.add(sb.toString());
			}
			return r1;
		}
	}
	
	private List<String> findString(List<String> data, String documentDescriptor, int keyStartIndex, int keyLength, String value, ComparisonOperatorType comparison) {
		switch (comparison) {
		case EQ:
			return findStringEqual(data, documentDescriptor, keyStartIndex, keyLength, value);
			
		case REGEX:
			return findStringRegex(data, documentDescriptor, keyStartIndex, keyLength, value);
			
		default:
			return null;
		}
	}
	
	private List<String> findStringEqual(List<String> data, String documentDescriptor, int keyStartIndex, int keyLength, String value) {
		return data.stream().filter(s -> s.startsWith(documentDescriptor)).filter(s -> s.substring(keyStartIndex, keyStartIndex + keyLength).trim().equalsIgnoreCase(value)).collect(Collectors.toList());
	}
	
	private List<String> findStringRegex(List<String> data, String documentDescriptor, int keyStartIndex, int keyLength, String value) {
		return data.stream().filter(s -> s.startsWith(documentDescriptor)).filter(s -> s.substring(keyStartIndex, keyStartIndex + keyLength).trim().matches((String)value)).collect(Collectors.toList());
	}
	
	public String getValue(String data, String documentDescriptor, String keyName) {
		DocumentDefinition documentDefinition = getDocumentDefinitionByDescriptor(documentDescriptor);
		if (documentDefinition == null) return "";
		Key key = getKeyByName(documentDefinition, keyName);
		if (key == null) return "";
		int lengthToKey = lengthUpToKey(documentDefinition, key);
		if (!data.toUpperCase().startsWith(documentDescriptor.toUpperCase()))
			return "";
		
		return data.substring(lengthToKey, lengthToKey + key.getLength());
	}
	
	private int lengthUpToKey(DocumentDefinition documentDefinition, Key key) {
		int length = 0;
		for (Key k : documentDefinition.getKeys()) {
			if (k.getName().equalsIgnoreCase(key.getName()))
				return length;
			else
				length+=k.getLength();
		}
		return length;
	}
	
	private List<String> findInteger(List<String> data, String documentDescriptor, int keyStartIndex, int keyLength, int value, ComparisonOperatorType comparison) {
		return null;
	}
}
