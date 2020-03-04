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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class Builder {
	
	public Builder() { }

	public static void build(File file, List<String> lines) throws IOException {
		List<String> statements = Parser.getStatements(lines);
		Collection collection = CollectionFactory.get(
				CollectionTools.getDocumentStyleType(lines),
				CollectionTools.getDelimiter(lines),
				Parser.getDocumentDefinitions(statements));
		FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsolutePath());
	    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
	    objectOutputStream.writeObject(collection);
	    objectOutputStream.flush();
	    objectOutputStream.close();
	}
}
