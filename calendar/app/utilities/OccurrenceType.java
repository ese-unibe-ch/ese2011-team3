package utilities;

public enum OccurrenceType {
	NONE(1, "None", "no repetition"),
	WEEKLY(2, "Every week", "repeats weekly"),
	MONTHLY(3, "Every month", "repeats monthly"),
	YEARLY(4, "Every year", "repeats yearly"),
	OTHERDAY(5, "Every other day", "repeats every other day"),
	OTHERWEEK(6, "Every other week", "repeats every other week");

	private int id;
	private String title;
	private String description;

	OccurrenceType(int id, String title, String desc) {
		this.id = id;
		this.title = title;
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
		case 5:
			return OccurrenceType.OTHERDAY;
		case 6:
			return OccurrenceType.OTHERWEEK;
		default:
			return OccurrenceType.NONE;
		}
	}

	@Override
	public String toString() {
		return this.title;
	}
}
