package utilities;

import java.util.Date;

import models.Event;

import org.joda.time.DateTime;

public enum RepeatableType {
	NONE(1, "no repetition") {
		public boolean happensOnDay(Event event, Date aDay) {
			DateTime dayLowerBound = Event.makeLowerBound(aDay);
			DateTime dayUpperBound = Event.makeUpperBound(aDay);
			DateTime eventStart = event.getLowerBound();
			DateTime eventEnd = event.getUpperBound();
			return (dayLowerBound.isAfter(eventStart) || dayLowerBound
					.equals(eventStart))
					&& (dayUpperBound.isBefore(eventEnd) || dayUpperBound
							.equals(eventEnd));
		}
	},

	DAILY(2, "repeats daily") {
		public boolean happensOnDay(Event event, Date aDay) {
			DateTime dayUpperBound = Event.makeUpperBound(aDay);
			DateTime eventStart = event.getLowerBound();
			return eventStart.isBefore(dayUpperBound);
		}
	},

	WEEKLY(3, "repeats weekly") {
		public boolean happensOnDay(Event event, Date aDay) {
			DateTime dayLowerBound = Event.makeLowerBound(aDay);
			DateTime dayUpperBound = Event.makeUpperBound(aDay);
			DateTime eventStart = event.getLowerBound();
			DateTime eventEnd = event.getUpperBound();
			return eventStart.isBefore(dayUpperBound)
					&& eventStart.getDayOfWeek() <= dayLowerBound
							.getDayOfWeek()
					&& eventEnd.getDayOfWeek() >= dayUpperBound.getDayOfWeek();
		}
	},

	MONTHLY(4, "repeats monthly") {

		public boolean happensOnDay(Event event, Date aDay) {
			DateTime dayLowerBound = Event.makeLowerBound(aDay);
			DateTime dayUpperBound = Event.makeUpperBound(aDay);
			DateTime eventStart = event.getLowerBound();
			DateTime eventEnd = event.getUpperBound();
			return eventStart.isBefore(dayUpperBound)
					&& eventStart.getDayOfMonth() <= dayLowerBound
							.getDayOfMonth()
					&& eventEnd.getDayOfMonth() >= dayUpperBound
							.getDayOfMonth();
		}
	},

	YEARLY(5, "repeats yearly") {
		public boolean happensOnDay(Event event, Date aDay) {
			DateTime dayLowerBound = Event.makeLowerBound(aDay);
			DateTime dayUpperBound = Event.makeUpperBound(aDay);
			DateTime eventStart = event.getLowerBound();
			DateTime eventEnd = event.getUpperBound();
			return eventStart.isBefore(dayUpperBound)
					&& eventStart.getDayOfMonth() <= dayLowerBound
							.getDayOfMonth()
					&& eventEnd.getDayOfMonth() >= dayUpperBound
							.getDayOfMonth()
					&& eventStart.getMonthOfYear() <= dayLowerBound
							.getMonthOfYear()
					&& eventEnd.getMonthOfYear() >= dayUpperBound
							.getMonthOfYear();
		}
	};

	private int id;
	private String description;

	RepeatableType(int id, String desc) {
		this.id = id;
		this.description = desc;
	}

	public int getId() {
		return id;
	}

	public String getDescription() {
		return this.description;
	}

	public static RepeatableType getType(int id) {
		switch (id) {
		case 1:
			return RepeatableType.NONE;
		case 2:
			return RepeatableType.DAILY;
		case 3:
			return RepeatableType.WEEKLY;
		case 4:
			return RepeatableType.MONTHLY;
		case 5:
			return RepeatableType.YEARLY;
		default:
			return RepeatableType.NONE;
		}
	}

	public abstract boolean happensOnDay(Event event, Date aDay);

}