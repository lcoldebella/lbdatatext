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
import java.util.List;

public class DocumentDefinition implements Serializable {
	private static final long serialVersionUID = -3818512967604631855L;
	private String documentDescriptor;
	private List<Key> keys;
	
	public DocumentDefinition() {
		
	}

	public DocumentDefinition(String documentDescriptor, List<Key> keys) {
		super();
		this.documentDescriptor = documentDescriptor;
		this.keys = keys;
	}

	public String getDocumentDescriptor() {
		return documentDescriptor;
	}

	public List<Key> getKeys() {
		return keys;
	}
}
