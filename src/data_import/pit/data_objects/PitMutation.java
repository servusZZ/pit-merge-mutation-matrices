package data_import.pit.data_objects;

import java.util.HashSet;
import java.util.Set;

public class PitMutation {
	/**	unique for each method */
	private String mutator;
	private String lineNumber;
	private String index;
	private String description;
	private EPitMutationStatus status;
	private Set<PitTestCase> killingTests;
	private Set<String> killingTestsNames;
	/** ONLY NEEDED FOR SERIALIZATION */
	public PitMutation() {
		
	}
	public PitMutation(String mutator, String lineNumber,
			String index, String description, EPitMutationStatus status) {
		this.mutator = mutator;
		this.lineNumber = lineNumber;
		this.index = index;
		this.description = description;
		this.setStatus(status);
		killingTests = new HashSet<PitTestCase>();
		killingTestsNames = new HashSet<String>();
	}
	/**	adds the passed killing tests to the currently killing tests */
	/*public void updateKillingTests(Set<PitTestCase> killingTests) {
		if (killingTests.isEmpty()) {
			return;
		}
		this.killingTests.addAll(killingTests);
	}	*/
	/**	adds the passed killing tests names to the currently killing tests names */
	public void updateKillingTestsNames(Set<String> killingTestsNames) {
		if (killingTestsNames.isEmpty()) {
			return;
		}
		this.killingTestsNames.addAll(killingTestsNames);
	}
	public String getMutator() {
		return mutator;
	}
	public String getLineNumber() {
		return lineNumber;
	}
	public String getIndex() {
		return index;
	}
	public String getDescription() {
		return description;
	}
	public Set<PitTestCase> getKillingTests() {
		return killingTests;
	}
	public EPitMutationStatus getStatus() {
		return status;
	}
	public void setStatus(EPitMutationStatus status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return mutator + "  " + status;
	}
	/** ONLY NEEDED FOR SERIALIZATION */
	public void setMutator(String mutator) {
		this.mutator = mutator;
	}
	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setKillingTests(Set<PitTestCase> killingTests) {
		this.killingTests = killingTests;
	}
	public Set<String> getKillingTestsNames() {
		return killingTestsNames;
	}
	public void setKillingTestsNames(Set<String> killingTestsNames) {
		this.killingTestsNames = killingTestsNames;
	}
	
}
