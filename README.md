# LBDataText Project
LBDataText is an (in very initial stage) open source library for structured data text manipulation.

To use LBDataText it is necessary two files:
1. Structure data file descriptor: Contains the description of data file (document, keys, etc...)
2. Data file: Data! :)

**1.Structure Data File Descriptor**

This file describes the data structure of file, as below:

**Document Structure Types**
Define how the keys (aka fields/columns) are separate. These can be:
1. COLUMN_STYLE=FIXED_WIDTH: The keys have a start index and a fixed length.
2. COLUMN_STYLE=CSV: An ordinary CSV file. The separator character is defined by DELIMITER=String.

**Document Descriptor Syntax**
```
CREATE DOCUMENT_DESCRIPTOR {DESC}
(
    [Keys]
);
```

**Keys**

A key defines the attributes for each value in the data file.

**Key Syntax**
```
{Key_Name} {Data_Type} Length(int) [Format] [PadLeft/Padright] [Dec_Separator]
```

**Data Types**
1. Integer
2. Decimal(x,y)
3. String
4. Datetime

***Attributes***
1. Length(int)
2. PadLeft(String)
3. PadRight(String)
4. Format(String)
5. Dec_Separator(String)

## Examples
**Document Descriptor**

Below we have an example of a document (fixed width based) descriptor where two documents structures are described. The first document is identified by a ‘0’ character in first position and has four keys (id, name, birthdate and score). The second document is identified by a ‘1’ character in first position and has three keys (quarter1_value, quarter2_value and quarter3_value).

**File: fixed_width_example.desc**
```
COLUMN_STYLE=FIXED_WIDTH;
CREATE DOCUMENT_DESCRIPTOR 0
(
  id INTEGER LENGTH(1),
  name STRING LENGTH(40) PADRIGHT(" "),
  birthdate DATETIME LENGTH(8) FORMAT("yyyyMMdd"),
  score DECIMAL(3,2) LENGTH(5) PADLEFT("0")
);
CREATE DOCUMENT_DESCRIPTOR 1 (
  quarter1_value DECIMAL(3,2) LENGTH(5) PADLEFT("0"),
  quarter2_value DECIMAL(3,2) LENGTH(5) PADLEFT("0"),
  quarter3_value DECIMAL(3,2) LENGTH(5) PADLEFT("0")
);
```
**File: fixed_width_data_example.txt**
```
0SMITCH, CLAUS                           1981011503251
1125320567804563
0abcdefghijklmnopqrstuvxyzabcdefghijklmno0123456701234
```
**Getting Values**
```
public static void main(String[] args) {
  try {
    //Load data structure descriptor file
    File file = new File("examples/fixed_width_example.desc");
    Collection collection = Collection.load(file);
        
    //Load data file
    List<String> data = Files.readAllLines(
                          Paths.get("examples/fixed_width_data_example.txt", ""));
            
    //Get values from 'score' key in documents with descriptor '0'
    List<String> values = collection.getValue(data, "0", "score");
    for (String value : values) {
      System.out.println("Result: " + value + " (length: " + value.length() + ")");
    }
  } catch (Exception e) {
    e.printStackTrace();
  }    
}
```