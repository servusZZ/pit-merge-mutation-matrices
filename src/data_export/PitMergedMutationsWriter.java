package data_export;

import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import data_import.pit.data_objects.PitMethod;
import data_import.pit.data_objects.PitTestCase;

public class PitMergedMutationsWriter {
	public static final String MERGED_METHODS_FILE_NAME = "methods-and-mutations-merged.xml";
	public static final String MERGED_TESTS_FILE_NAME = "tests-merged.xml";
	
	public static void writeMergedMethodsAndMutationsFile(String outputDir, List<PitMethod> methods) throws IOException {
		String outputFile = outputDir + MERGED_METHODS_FILE_NAME;
		Path outputFilePath = Paths.get(outputFile);
		Files.deleteIfExists(outputFilePath);
		
		System.out.println("INFO: Writing all methods to file " + outputDir + MERGED_METHODS_FILE_NAME);
		FileOutputStream fos = new FileOutputStream(new File(outputDir + MERGED_METHODS_FILE_NAME));
		XMLEncoder encoder = new XMLEncoder(fos);
		encoder.writeObject(methods);
		encoder.close();
		fos.close();
	}
	public static void writeAllTestsFile(String outputDir, List<PitTestCase> tests) throws IOException {
		String outputFile = outputDir + MERGED_TESTS_FILE_NAME;
		Path outputFilePath = Paths.get(outputFile);
		Files.deleteIfExists(outputFilePath);
		
		System.out.println("INFO: Writing all tests to file " + outputDir + MERGED_TESTS_FILE_NAME);
		FileOutputStream fos = new FileOutputStream(new File(outputDir + MERGED_TESTS_FILE_NAME));
		XMLEncoder encoder = new XMLEncoder(fos);
		encoder.writeObject(tests);
		encoder.close();
		fos.close();
	}
}
