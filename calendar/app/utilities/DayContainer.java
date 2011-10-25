package utilities;

import java.util.Date;

import org.joda.time.DateTime;

public class DayContainer {
	public enum DayContainerType {
		THISMONTH(""),
		OTHERMONTH("day_other"),
		TODAY("day_today"),
		SELECTED("day_selected");

		private String id;

		private DayContainerType(String id) {
			this.id = id;
		}

		public String getId() {
			return this.id;
		}
	}

	public Date date;
	public DayContainerType type;
	public boolean containsEvents;

	public DayContainer() {
		this.date = new Date();
		this.type = DayContainerType.THISMONTH;
		this.containsEvents = false;
	}

	public int getNumber() {
		DateTime d = new DateTime(this.date);
		return d.getDayOfMonth();
	}
}
