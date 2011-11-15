package utilities;

public enum OccurrenceType {
	NONE(1, "no repetition"),
	WEEKLY(2, "repeats weekly"),
	MONTHLY(3, "repeats monthly"),
	YEARLY(4, "repeats yearly");

	private int id;
	private String description;

	OccurrenceType(int id, String desc) {
		this.id = id;
		this.description = desc;
	}

	public int getId() {
		return id;
	}

	public String getDescription() {
		return this.description;
	}

	public static OccurrenceType getType(int id) {
		switch (id) {
		case 1:
			return OccurrenceType.NONE;
		case 2:
			return OccurrenceType.WEEKLY;
		case 3:
			return OccurrenceType.MONTHLY;
		case 4:
			return OccurrenceType.YEARLY;
		default:
			return OccurrenceType.NONE;
		}
	}

	public String toString() {
		return this.name().substring(0, 1)
				+ this.name().substring(1).toLowerCase();
	}
}
