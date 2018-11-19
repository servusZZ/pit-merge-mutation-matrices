package merge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import data_import.pit.data_objects.EPitMutationStatus;
import data_import.pit.data_objects.PitMethod;
import data_import.pit.data_objects.PitMutation;

/**
 * During the process of merging methods, no linkage between methods/ mutations and test case objects 
 * are created. Instead, only a set of test case names is filled. (Due to a StackOverflowError that
 * otherwise appears during xml serialization because of the deep nesting of objects)
 * 
 */
public class PitMutationsMerger {
	private final static String METHOD_ID_SEPARATOR_CHAR = ":";
	private final static String TESTS_SEPARATOR_CHAR = "\\|";
	private final static String REGEX_TEXT_IN_BRACKETS = "\\([^(]*\\)";
	/** Mutation node. */
	private XPathExpression m_mutationNodeXPath;
	/** Class node of mutation node. */
	private XPathExpression m_mutatedClassTextXPath;
	/** Method node of mutation node. */
	private XPathExpression m_mutatedMethodTextXPath;
	/** Description node of mutation node. */
	private XPathExpression m_methodTypeSignatureTextXPath;
	/** Mutator node of mutation node. */
	private XPathExpression m_mutatorNameTextXPath;
	/** Killing test node of mutation node. */
	protected XPathExpression m_killingTestTextXPath;
	/**	Succeeding test node of mutation node. */
	protected XPathExpression m_succeedingTestTextXPath;
	/** Index node of mutation node. */
	private XPathExpression m_indexTextXPath;
	/** Line number node of mutation node. */
	private XPathExpression m_lineNumberTextXPath;
	/** Description node of mutation node. */
	private XPathExpression m_descriptionTextXPath;
	/** Source file name (file name with extension but without path) node of mutation node. */
	private XPathExpression m_sourceFileNameTextXPath;
	/** Status attribute. */
	private XPathExpression m_statusAttributeXPath;
	
	private Map<String, PitMethod> methods;
	//private Map<String, PitTestCase> tests;
	
	private List<PitMethod> methodsList;
	//private List<PitTestCase> testsList;
	
	public PitMutationsMerger() throws XPathExpressionException {
		m_mutationNodeXPath = Main.compileXPath("mutations/mutation");
		m_mutatedClassTextXPath = Main.compileXPath("./mutatedClass/text()");
		m_mutatedMethodTextXPath = Main.compileXPath("./mutatedMethod/text()");
		m_methodTypeSignatureTextXPath = Main.compileXPath("./methodDescription/text()");
		m_mutatorNameTextXPath = Main.compileXPath("./mutator/text()");
		m_killingTestTextXPath = Main.compileXPath("./killingTest/text()");
		m_succeedingTestTextXPath = Main.compileXPath("./succeedingTest/text()");
		m_indexTextXPath = Main.compileXPath("./index/text()");
		m_lineNumberTextXPath = Main.compileXPath("./lineNumber/text()");
		m_descriptionTextXPath = Main.compileXPath("./description/text()");
		m_sourceFileNameTextXPath = Main.compileXPath("./sourceFile/text()");
		m_statusAttributeXPath = Main.compileXPath("./@status");;
		methods = new HashMap<String, PitMethod>();
		//tests = new HashMap<String, PitTestCase>();
		methodsList	= new ArrayList<PitMethod>();
		//testsList = new ArrayList<PitTestCase>();
	}
	/**
	 * Calls mergeMethodsPerDocument for each document.
	 * Merges Methods and Mutations of all documents, creates all tests
	 * with method coverage.
	 */
	public void mergeMutationNodes(List<Document> mutationDocuments) throws Exception {
		for (Document doc: mutationDocuments) {
			mergeMethodsPerDocument((NodeList)m_mutationNodeXPath.evaluate(doc, XPathConstants.NODESET));
		}
	}
	private void mergeMethodsPerDocument(NodeList mutationNodes) throws Exception {
		for (int i = 0; i < mutationNodes.getLength(); i++) {
			PitMethod method = methods.get(getMethodId(mutationNodes.item(i)));
			if (method == null) {
				// create new PitMethod
				method = new PitMethod(getSourceFileName(mutationNodes.item(i)),
						getClassName(mutationNodes.item(i)),
						getMethodName(mutationNodes.item(i)),
						getMethodTypeSignature(mutationNodes.item(i)));
				methods.put(method.getId(), method);
			}
			// update PitMethod and allTests map
			//Set<PitTestCase> newCoveringTests = updateTests(mutationNodes.item(i), method);
			method.updateCoveringTestsNames(getCoveringTestNames(mutationNodes.item(i)));
			PitMutation mutation = method.getMutation(getMutatorName(mutationNodes.item(i)));
			if (mutation == null) {
				// create new PitMutation
				mutation = new PitMutation(getMutatorName(mutationNodes.item(i)),
						getLineNumber(mutationNodes.item(i)),
						getIndex(mutationNodes.item(i)),
						getDescription(mutationNodes.item(i)),
						getMutationStatus(mutationNodes.item(i)));
				method.addMutation(mutation);
			} else { // update Status
				mutation.setStatus(EPitMutationStatus.mergeStatus(mutation.getStatus(), getMutationStatus(mutationNodes.item(i))));
			}
			//Set<PitTestCase> killingTests = getTestCasesFromNames(getKillingTestNames(mutationNodes.item(i)));
			//updatePossibleFaultsOfTests(killingTests, mutation);
			mutation.updateKillingTestsNames(new HashSet<String>(Arrays.asList(getKillingTestNames(mutationNodes.item(i)))));
		}
	}
	public void reset() {
		methods = new HashMap<String, PitMethod>();
		methodsList	= new ArrayList<PitMethod>();
	}
	/*private void updatePossibleFaultsOfTests(Set<PitTestCase> killingTests, PitMutation mutation) {
		if (!EPitMutationStatus.isFault(mutation)) {
			return;
		}
		for (PitTestCase killingTest: killingTests) {
			killingTest.addPossibleFault(mutation);
		}
	}	*/
	private String getMethodId(Node mutationNode) throws XPathExpressionException {
		return m_mutatedClassTextXPath.evaluate(mutationNode, XPathConstants.STRING) + METHOD_ID_SEPARATOR_CHAR
		+ m_mutatedMethodTextXPath.evaluate(mutationNode, XPathConstants.STRING) + METHOD_ID_SEPARATOR_CHAR
		+ m_methodTypeSignatureTextXPath.evaluate(mutationNode, XPathConstants.STRING);
	}
	public static String getMethodId(String className, String methodName, String methodTypeSignature) {
		return className + METHOD_ID_SEPARATOR_CHAR + methodName + METHOD_ID_SEPARATOR_CHAR + methodTypeSignature;
	}
	private String getClassName(Node mutationNode) throws XPathExpressionException {
		return (String)m_mutatedClassTextXPath.evaluate(mutationNode, XPathConstants.STRING);
	}
	private String getMethodName(Node mutationNode) throws XPathExpressionException {
		return (String)m_mutatedMethodTextXPath.evaluate(mutationNode, XPathConstants.STRING);
	}
	private String getMethodTypeSignature(Node mutationNode) throws XPathExpressionException {
		return (String)m_methodTypeSignatureTextXPath.evaluate(mutationNode, XPathConstants.STRING);
	}
	private String getSourceFileName(Node mutationNode) throws XPathExpressionException {
		return (String)m_sourceFileNameTextXPath.evaluate(mutationNode, XPathConstants.STRING);
	}
	private String getMutatorName(Node mutationNode) throws XPathExpressionException {
		return (String)m_mutatorNameTextXPath.evaluate(mutationNode, XPathConstants.STRING);
	}
	private String getIndex(Node mutationNode) throws XPathExpressionException {
		return (String)m_indexTextXPath.evaluate(mutationNode, XPathConstants.STRING);
	}
	private String getLineNumber(Node mutationNode) throws XPathExpressionException {
		return (String)m_lineNumberTextXPath.evaluate(mutationNode, XPathConstants.STRING);
	}
	private String getDescription(Node mutationNode) throws XPathExpressionException {
		return (String)m_descriptionTextXPath.evaluate(mutationNode, XPathConstants.STRING);
	}
	private EPitMutationStatus getMutationStatus(Node mutationNode) throws XPathExpressionException {
		return EPitMutationStatus.valueOf((String)m_statusAttributeXPath.evaluate(mutationNode, XPathConstants.STRING));
	}
	/**
	 * Updates the tests Map by all new tests in killingTest and succeedingTest
	 * And returns all covering Tests for this Method.
	 * @return covering tests (union of killing and succeeding)
	 */
	/*private Set<PitTestCase> updateTests(Node mutationNode, PitMethod method) throws XPathExpressionException{
		Set<PitTestCase> coveringTests = createOrUpdateTests(getKillingTestNames(mutationNode), method);
		coveringTests.addAll(createOrUpdateTests(getSucceedingTestNames(mutationNode), method));
		return coveringTests;
	}	*/
	/**
	 * updates the tests Map for the passed tests and returns the respective 
	 * PitTestCase objects for all passed tests as strings.
	 */
	/*private Set<PitTestCase> createOrUpdateTests(String[] newTests, PitMethod method) {
		Set<PitTestCase> passedTests = new HashSet<PitTestCase>();
		if (newTests == null || newTests.length == 0) {
			return passedTests;
		}
		for (String newTestName:newTests) {
			PitTestCase test = tests.get(newTestName);
			if (test == null) {
				test = new PitTestCase(newTestName);
				tests.put(newTestName, test);
			}
			test.updateCoveredMethods(method);
			passedTests.add(test);
		}
		return passedTests;
	}	*/
	private Set<String> getCoveringTestNames(Node mutationNode) throws XPathExpressionException {
		Set<String> coveringTests = new HashSet<String>(Arrays.asList(getKillingTestNames(mutationNode)));
		coveringTests.addAll(Arrays.asList(getSucceedingTestNames(mutationNode)));
		return coveringTests;
	}
	private String[] getKillingTestNames(Node mutationNode) throws XPathExpressionException {
		String killingTestsString = (String)m_killingTestTextXPath.evaluate(mutationNode, XPathConstants.STRING);
		if (killingTestsString.isEmpty()) {
			return new String[] {};
		}
		killingTestsString = killingTestsString.replaceAll(REGEX_TEXT_IN_BRACKETS, "");
		return killingTestsString.split(TESTS_SEPARATOR_CHAR);
	}
	
	private String[] getSucceedingTestNames(Node mutationNode) throws XPathExpressionException{
		String succeedingTestsString = (String)m_succeedingTestTextXPath.evaluate(mutationNode, XPathConstants.STRING);
		if (succeedingTestsString.isEmpty()) {
			return new String[] {};
		}
		succeedingTestsString = succeedingTestsString.replaceAll(REGEX_TEXT_IN_BRACKETS, "");
		return succeedingTestsString.split(TESTS_SEPARATOR_CHAR);
	}
	/**
	 * Returns the PitTestCase objects contained in the tests map of all
	 * passed test names.
	 */
	/*private Set<PitTestCase> getTestCasesFromNames(String[] testNames) throws Exception {
		Set<PitTestCase> testCases = new HashSet<PitTestCase>();
		if (testNames == null || testNames.length == 0) {
			return testCases;
		}
		for (String testName: testNames) {
			if (tests.containsKey(testName)) {
				testCases.add(tests.get(testName));
			} else {
				throw new Exception("Killing Test of Mutation was not contained in the allTests Map!");
			}
		}
		return testCases;
	}	*/
	public List<PitMethod> getMergedMethods(){
		if (methodsList.isEmpty()) {
			for (PitMethod method:methods.values()) {
				methodsList.add(method);
			}
		}
		return methodsList;
	}
	/*public List<PitTestCase> getAllTests(){
		if (testsList.isEmpty()) {
			for (PitTestCase test:tests.values()) {
				testsList.add(test);
			}
		}
		return testsList;
	}	*/
}
