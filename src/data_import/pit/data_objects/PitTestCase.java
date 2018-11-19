package data_import.pit.data_objects;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is not used during the merging. Only after the import of the merged methods xml file
 * the test case objects are created.
 *
 */
public class PitTestCase {
	private String name;
	private Set<PitMethod> coveredMethods;
	private Set<PitMutation> possibleFaults;
	
	/** ONLY NEEDED FOR SERIALIZATION */
	public PitTestCase() { }
	public PitTestCase(String name) {
		this.name = name;
		coveredMethods = new HashSet<PitMethod>();
		possibleFaults = new HashSet<PitMutation>();
	}
	public void updateCoveredMethods(Set<PitMethod> newCoveredMethods) {
		if (newCoveredMethods.isEmpty()) {
			return;
		}
		coveredMethods.addAll(newCoveredMethods);
	}
	public void updateCoveredMethods(PitMethod method) {
		coveredMethods.add(method);
	}
	public void addPossibleFault(PitMutation fault) {
		possibleFaults.add(fault);
	}
	public String getName() {
		return name;
	}
	public Set<PitMethod> getCoveredMethods(){
		return coveredMethods;
	}
	@Override
	public String toString() {
		return name;
	}
	/** ONLY NEEDED FOR SERIALIZATION */
	public void setName(String name) {
		this.name = name;
	}
	public void setCoveredMethods(Set<PitMethod> coveredMethods) {
		this.coveredMethods = coveredMethods;
	}
	public Set<PitMutation> getPossibleFaults() {
		return possibleFaults;
	}
	public void setPossibleFaults(Set<PitMutation> possibleFaults) {
		this.possibleFaults = possibleFaults;
	}
}
