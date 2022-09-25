package edu.jsu.mcis.cs310;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
    
    /*
        
        Consider the following CSV data:
        
        "ID","Total","Assignment 1","Assignment 2","Exam 1"
        "111278","611","146","128","337"
        "111352","867","227","228","412"
        "111373","461","96","90","275"
        "111305","835","220","217","398"
        "111399","898","226","229","443"
        "111160","454","77","125","252"
        "111276","579","130","111","338"
        "111241","973","236","237","500"
        
        The corresponding JSON data would be similar to the following (tabs and
        other whitespace have been added for clarity).  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings and which values should be encoded as integers, as
        well as the overall structure of the data!
        
        {
            "colHeaders":["ID","Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160",
            "111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }
        
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
        
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and JSON.simple.  See the "Data
        Exchange" lecture notes for more details, including examples.
        
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String results = "";
        
        try {
            
            // Initialize CSV Reader and Iterator
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();
            
            /* Conversion #1 */
            JSONObject jsonObject = new JSONObject();
            JSONArray column = new JSONArray();
            JSONArray row = new JSONArray();
            JSONArray data = new JSONArray();
            JSONArray tempHold;
            String[] storage = iterator.next();
            
            for(int i = 0; i < storage.length; i++){
                column.add(storage[i]);
            }
            
            while(iterator.hasNext()){
                tempHold = new JSONArray();
                storage = iterator.next();
                row.add(storage[0]);
                for(int i = 1; i < storage.length; i++){
                    int stringHolder = Integer.parseInt(storage[i]);
                    tempHold.add(stringHolder);
                }
                data.add(tempHold);
            }
            jsonObject.put("rowHeaders", row);
            jsonObject.put("colHeaders", column);
            jsonObject.put("data", data);
            results = JSONValue.toJSONString(jsonObject);
        }
        catch(Exception e) { e.printStackTrace(); }
        
        // Return JSON String
        
        return results.trim();
        
    }
    
    public static String jsonToCsv(String jsonString) {
        
        String results = "";
        
        try {
            
            // Initialize JSON Parser and CSV Writer
            
            JSONParser parser = new JSONParser();
            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\\', "\n");
            
            /* Conversion #2 */
            JSONObject Object = (JSONObject)parser.parse(jsonString);
            JSONArray column = (JSONArray)Object.get("colHeaders");
            JSONArray row = (JSONArray)Object.get("rowHeaders");
            JSONArray data = (JSONArray)Object.get("data");
            JSONArray tempHold;
            String[] storage = new String[column.size()];
            
            for(int i = 0; i < column.size(); i++){
                storage[i] = (String) column.get(i);
            }
            csvWriter.writeNext(storage);
            for(int i = 0; i < data.size(); i++){
                tempHold = (JSONArray)data.get(i);
                storage = new String[tempHold.size() + 1];
                storage[0] = (String)row.get(i);
                for(int j = 0; j < tempHold.size(); j++){
                storage[j + 1] = Long.toString((long)tempHold.get(j));
                }
                csvWriter.writeNext(storage);
            }
            results = writer.toString();
            
        }
        catch(Exception e) { e.printStackTrace(); }
        
        // Return CSV String
        
        return results.trim();
        
    }
	
}