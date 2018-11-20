package data_import.pit.data_objects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import merge.PitMutationsMerger;

public class PitMethod {
	protected String sourceFile;
	protected String className;
	protected String methodName;
	protected String methodTypeSignature;
	protected String id;
	private Set<PitTestCase> coveringTests;
	private Set<String> coveringTestsNames;
	private List<PitMutation> mutations;
	
	/** ONLY NEEDED FOR SERIALIZATION */
	public PitMethod() { }
	public PitMethod(String sourceFile, String className,
			String methodName, String methodTypeSignature) {
		this.sourceFile = sourceFile;
		this.className = className;
		this.methodName = methodName;
		this.methodTypeSignature = methodTypeSignature;
		id = PitMutationsMerger.getMethodId(className, methodName, methodTypeSignature);
		coveringTests = new HashSet<PitTestCase>();
		coveringTestsNames = new HashSet<String>();
		mutations = new ArrayList<PitMutation>();
	}
	/*public void updateCoveringTests(Set<PitTestCase> newCoveringTests) {
		if (newCoveringTests.isEmpty()) {
			return;
		}
		coveringTests.addAll(newCoveringTests);
	}	*/
	public void addCoveringTest(PitTestCase newTest) {
		coveringTests.add(newTest);
	}
	public void updateCoveringTestsNames(Set<String> newCoveringTestsNames) {
		if (newCoveringTestsNames.isEmpty()) {
			return;
		}
		coveringTestsNames.addAll(newCoveringTestsNames);
	}
	public boolean hasMutation(String mutator) {
		for (PitMutation mutation: mutations) {
			if (mutation.getMutator().equals(mutator)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Returns the PitMutation of the method with the passed mutator.
	 * Returns null, if the mutator doesn't exist yet.
	 */
	public PitMutation getMutation(String mutator) {
		for (PitMutation mutation: mutations) {
			if (mutation.getMutator().equals(mutator)) {
				return mutation;
			}
		}
		return null;
	}
	public void addMutation(PitMutation mutation) {
		mutations.add(mutation);
	}
	public String getSourceFile() {
		return sourceFile;
	}
	public String getClassName() {
		return className;
	}
	public String getMethodName() {
		return methodName;
	}
	public String getMethodTypeSignature() {
		return methodTypeSignature;
	}
	public String getId() {
		return id;
	}
	public String getXMLString() {
		return "";
	}
	public Set<PitTestCase> getCoveringTests() {
		return coveringTests;
	}
	public List<PitMutation> getMutations() {
		return mutations;
	}
	/** ONLY NEEDED FOR SERIALIZATION */
	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public void setMethodTypeSignature(String methodTypeSignature) {
		this.methodTypeSignature = methodTypeSignature;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setCoveringTests(Set<PitTestCase> coveringTests) {
		this.coveringTests = coveringTests;
	}
	public void setMutations(List<PitMutation> mutations) {
		this.mutations = mutations;
	}
	
	public Set<String> getCoveringTestsNames() {
		return coveringTestsNames;
	}
	public void setCoveringTestsNames(Set<String> coveringTestsNames) {
		this.coveringTestsNames = coveringTestsNames;
	}
	@Override
	public String toString() {
		return getId();
	}
}
