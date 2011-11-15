package models.occurrences;

import models.Event;

public class WeeklyOccurrence extends Occurrence {

	public WeeklyOccurrence(Event event) {
		this(event, 0);
	}

	public WeeklyOccurrence(Event event, int iteration) {
		super(event, iteration);
	}

	@Override
	public Occurrence getNext() {
		WeeklyOccurrence next = new WeeklyOccurrence(this.event, iteration + 1);

		next.start = this.start.plusWeeks(1);
		next.end = this.end.plusWeeks(1);

		return next;
	}

}
