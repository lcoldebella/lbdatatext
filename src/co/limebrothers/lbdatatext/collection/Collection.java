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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import co.limebrothers.lbdatatext.document.DocumentDefinition;
import co.limebrothers.lbdatatext.document.Key;

public abstract class Collection implements Serializable, Iterable {
	private static final long serialVersionUID = -8314914024823985531L;
	private List<DocumentDefinition> documentDataDefinitions;

	public Collection() { }
	
	public Collection(List<DocumentDefinition> documentDataDefinitions) {
		super();
		this.documentDataDefinitions = documentDataDefinitions;
	}

	public static Collection load(File file) throws IOException, ClassNotFoundException {
		List<String> statements = Parser.getStatements(Files.readAllLines(Paths.get(file.getAbsolutePath(), "")));
		return CollectionFactory.get(
				CollectionTools.getDocumentStyleType(statements),
				CollectionTools.getDelimiter(statements),
				Parser.getDocumentDefinitions(statements));
	}
	
	public static Collection loadFromBinary(File file) throws IOException, ClassNotFoundException {
		FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
	    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
	    Collection collection = (Collection) objectInputStream.readObject();
	    objectInputStream.close();
	    return collection;
	}
	
	public List<DocumentDefinition> getDocumentDataDefinitions() {
		return documentDataDefinitions;
	}

	public String toString() {
		String format = "documentDataDefinitions: %s (name=%s, keysType=%s, )\n";
		StringBuilder sb = new StringBuilder();
		for (DocumentDefinition documentDataDefinition : documentDataDefinitions) {
			String desc = String.format(format, documentDataDefinition.getDocumentDescriptor());
			sb.append(desc);
		}
		return sb.toString();
	}
	
	protected DocumentDefinition getDocumentDefinitionByDescriptor(String documentDescriptor) {
		for (DocumentDefinition documentDefinition : documentDataDefinitions) {
			if (documentDefinition.getDocumentDescriptor().equalsIgnoreCase(documentDescriptor))
				return documentDefinition;
		}
		return null;
	}
	
	protected Key getKeyByName(DocumentDefinition documentDefinition, String keyName) {
		for (Key key : documentDefinition.getKeys()) {
			if (key.getName().equalsIgnoreCase(keyName))
				return key;
		}
		return null;
	}
}