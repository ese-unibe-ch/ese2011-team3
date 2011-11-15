package models.occurrences;

import models.Event;

import org.joda.time.DateTime;

public abstract class Occurrence {
	protected Event event;
	protected int iteration;
	protected DateTime start;
	protected DateTime end;

	public Occurrence() {
		this(null);
	}

	public Occurrence(Event event) {
		this(event, 0);
	}

	public Occurrence(Event event, int iteration) {
		this.event = event;
		this.iteration = iteration;
		this.start = new DateTime(event.start);
		this.end = new DateTime(event.end);
	}

	public Event getEvent() {
		return event;
	}

	public final int getIteration() {
		return this.iteration;
	}

	public DateTime getStart() {
		return this.start;
	}

	public DateTime getEnd() {
		return this.end;
	}

	public static Occurrence getOccurrence(Event event) {
		switch (event.occurrenceType) {
		case NONE:
			return new OneTimeOccurrence(event);
		case WEEKLY:
			return new WeeklyOccurrence(event);
		case MONTHLY:
			return new MonthlyOccurrence(event);
		case YEARLY:
			return new YearlyOccurrence(event);
		case OTHERDAY:
			return new OtherDayOccurrence(event);
		case OTHERWEEK:
			return new OtherWeekOccurrence(event);
		case ENDMONTH:
			return new EndMonthOccurrence(event);
		case WORKDAY:
			return new WorkDayOccurrence(event);
		}
		return null;
	}

	public Occurrence occurrenceForDay(DateTime day) {
		Occurrence currentWrapper = this;
		while (currentWrapper != null
				&& (day.plusDays(1).equals(currentWrapper.getStart()) || day
						.plusDays(1).isAfter(currentWrapper.getStart()))) {
			if (currentWrapper.happensOnDay(day)) {
				return currentWrapper;
			}
			currentWrapper = currentWrapper.getNext();
		}
		return null;
	}

	protected boolean happensOnDay(DateTime day) {
		if (!this.event.occurrenceHappens(this.iteration)) {
			return false;
		}
		DateTime normalizedStart = new DateTime(this.getStart())
				.withTimeAtStartOfDay();
		DateTime normalizedEnd = new DateTime(this.getEnd())
				.withTimeAtStartOfDay().plusDays(1);
		return (normalizedStart.isBefore(day) || normalizedStart.equals(day))
				&& normalizedEnd.isAfter(day);
	}

	@Override
	public boolean equals(Object object) {
		if (!object.getClass().equals(this.getClass())) {
			return false;
		}
		Occurrence obj = (Occurrence) object;
		return (obj.event.equals(this.event) && obj.iteration == this.iteration);
	}

	public abstract Occurrence getNext();
}
