package co.limebrothers.lbdatatext.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import co.limebrothers.lbdatatext.collection.Builder;
import co.limebrothers.lbdatatext.collection.Collection;

public class Main {
	private static final String sampleDestPathMap ="fixed_width_sample.map";
	private static final String sampleMapDescPath ="samples/fixed_width_sample.desc";
	private static final String sampleDataPath ="samples/fixed_width_sample_data.txt";
	
	private static final String sicrediDestPathMap ="sicredi_CNAB_400.map";
	private static final String sicrediMapDescPath ="samples/Sicredi_CNAB_400.desc";
	private static final String sicrediDataPath ="samples/cnab_sicredi_400.CRM";
	
	public static void main(String[] args) {
		try {
			//buildMap(sampleMapDescPath, sampleDestPathMap);
			//testMap(sampleDestPathMap, sampleDataPath, "0", "score");			
			//testMapToString(sampleDestPathMap);
			testMapFromDesc(sampleMapDescPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static void buildMap(String mapDescPath, String destPathMap) throws IOException {
		File file = new File(destPathMap);		
		List<String> lines = Files.readAllLines(Paths.get(mapDescPath, ""));
		Builder.build(file, lines);
	}
	
	private static void testMap(String mapFilePath, String dataFilePath, String rowDescriptor, String fieldName) throws IOException, ClassNotFoundException {
		File file = new File(mapFilePath);
		Collection collection = Collection.loadFromBinary(file);	
		List<String> data = Files.readAllLines(Paths.get(dataFilePath, ""));
		List<String> values = collection.getValue(data, rowDescriptor, fieldName);
		for (String value : values) {
			System.out.println("Result: " + value + " (length: " + value.length() + ")");
		}
	}
	
	private static void testMapToString(String mapFilePath) throws IOException, ClassNotFoundException {
		File file = new File(mapFilePath);
		Collection collection = Collection.load(file);
		System.out.println(collection.toString());
	}
	
	private static void testMapFromDesc(String mapFilePath) throws IOException, ClassNotFoundException {
		File file = new File("samples/fixed_width_sample.desc");
		Collection collection = Collection.load(file);
		List<String> data = Files.readAllLines(Paths.get("samples/fixed_width_sample_data.txt", ""));
		List<String> values = collection.getValue(data, "0", "score");
		for (String value : values) {
			System.out.println("Result: " + value + " (length: " + value.length() + ")");
		}
	}
	
}
