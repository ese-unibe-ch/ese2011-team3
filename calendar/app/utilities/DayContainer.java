package utilities;

public class DayContainer {
	public enum DayContainerType {
		THISMONTH("normal"),
		OTHERMONTH("other"),
		TODAY("today"),
		SELECTED("selected");

		private String id;

		private DayContainerType(String id) {
			this.id = id;
		}

		public String getId() {
			return this.id;
		}
	}

	public int number;
	public DayContainerType type;
	public boolean containsEvents;

	public DayContainer() {
		this.number = 0;
		this.type = DayContainerType.THISMONTH;
		this.containsEvents = false;
	}
}
