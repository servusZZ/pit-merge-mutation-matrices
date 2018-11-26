package merge;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

import data_export.PitMergedMutationsWriter;
import data_import.pit.data_objects.PitMethod;

public class Main {
	public static final String PIT_MUTATIONS_BASE_DIR = "C:\\study\\SWDiag\\sharedFolder_UbuntuVM\\MA\\pit_data_tests\\";
	public static final String PIT_DATA_FOLDER_NAME = "pit-data\\";
	private static XPath xPath;
	
	public static XPathExpression compileXPath(String expression) throws XPathExpressionException {
		return xPath.compile(expression);
	}
	
	public static void main(String[] args) throws Exception {	
		XPathFactory xPathFactory = XPathFactory.newInstance();
		xPath = xPathFactory.newXPath();
		
		File[] directories = new File(PIT_MUTATIONS_BASE_DIR).listFiles(File::isDirectory);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder loader = factory.newDocumentBuilder();
	    
	    PitMutationsMerger merger = new PitMutationsMerger();

	    for (File projectDirectory: directories) {
	    	System.out.println("INFO: Collecting mutations.xml files of project " + projectDirectory.getPath());
	    	File[] mutationMatrixes = new File(projectDirectory.getPath() + "\\" + PIT_DATA_FOLDER_NAME).listFiles(File::isFile);
	    	List<Document> mutationDocuments = new ArrayList<Document>();
	    	for (File mutationFile: mutationMatrixes) {
	    		System.out.println("DEBUG: Collecting next document " + mutationFile.getPath());
	    		Document doc = loader.parse(mutationFile.getPath());
	    		mutationDocuments.add(doc);
	    	}
	    	System.out.println("INFO: Merging files...");
	    	merger.mergeMutationNodes(mutationDocuments);
	    	List<PitMethod> methods = merger.getMergedMethods();
	    	merger.reset();
	    	//List<PitTestCase> allTests = merger.getAllTests();
	    	//StatisticsPrinter statistics = new StatisticsPrinter(methods, allTests);
	    	//statistics.printTestStatistics();
	    	String outputDir = projectDirectory.getPath() + "\\" + PIT_DATA_FOLDER_NAME;
	    	PitMergedMutationsWriter.writeMergedMethodsAndMutationsFile(outputDir, methods);
	    	//PitMergedMutationsWriter.writeAllTestsFile(outputDir, allTests);
	    }
	    System.out.println("INFO: Merging of documents finished!");
	}
}
