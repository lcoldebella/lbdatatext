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

public class Parser {
	private static final String CREATE_DOCUMENT_DESCRIPTOR = "CREATE DOCUMENT_DESCRIPTOR";
	
	public static List<String> getStatements(List<String> lines) {
		List<String> statements = new ArrayList<String>();
		
		String statement = "";
		for (String line : lines) {
			for (int i = 0; i < line.length(); i++) {
				if ((line.charAt(i) == ';') && (line.charAt(i - 1) != '"')) {
					statements.add(statement.replaceAll("\\t", " ").replace("\\s{2,}", " "));
					statement = "";						
				}						
				else
					statement+=line.charAt(i);
			}
		}
		
		return statements;
	}
	
	public static List<DocumentDefinition> getDocumentDefinitions(List<String> statements) {
		List<DocumentDefinition> documentsDefinition = new ArrayList<DocumentDefinition>();
		
		for (String statement : statements) {
			if (isCreateDocumentDescriptorStatement(statement))
				documentsDefinition.add(buildDocumentDefinition(statement.toUpperCase()));
		}
		
		return documentsDefinition;
	}
	
	private static boolean isCreateDocumentDescriptorStatement(String statement) {
		return statement.toUpperCase().startsWith(CREATE_DOCUMENT_DESCRIPTOR);
	}
	
	private static DocumentDefinition buildDocumentDefinition(String statement) {
		List<Key> keys =  new ArrayList<Key>();
		int posDocDescriptorValue = statement.indexOf(CREATE_DOCUMENT_DESCRIPTOR) + CREATE_DOCUMENT_DESCRIPTOR.length() + 1;
		int leftParentheses = statement.indexOf("(");
		String documentDescriptor = statement.substring(posDocDescriptorValue, leftParentheses).trim();
		String keysDesc = statement.substring(leftParentheses + 1, statement.length() - 1);
		String keyDesc = "";
		boolean betweenParentheses = false;
		for (int i = 0; i < keysDesc.length(); i++) {
			if (keysDesc.charAt(i) == '(')
				betweenParentheses = true;
			else if (keysDesc.charAt(i) == ')')
				betweenParentheses = false;
			
			if ((keysDesc.charAt(i) == ',') && (keysDesc.charAt(i) - 1 != '"') && !betweenParentheses) {
				keys.add(new Key(keyDesc));
				keyDesc = "";
			} else
				keyDesc += keysDesc.charAt(i);
		}
		
		if (keyDesc.length() > 0)
			keys.add(new Key(keyDesc));
		
		return new DocumentDefinition(documentDescriptor, keys);
	}
}
