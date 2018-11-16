package data_import.pit.data_objects;

public enum EPitMutationStatus {
	// entries are ordered
	KILLED, TIMED_OUT, MEMORY_ERROR, SURVIVED, RUN_ERROR, NON_VIABLE, NO_COVERAGE;
	
	public static EPitMutationStatus mergeStatus(EPitMutationStatus status1, EPitMutationStatus status2) {
		if (status1.ordinal() <= status2.ordinal()) {
			return status1;
		}
		return status2;
	}
	public static boolean isFault(PitMutation mutation) {
		return mutation.getStatus().equals(KILLED);
	}
}
