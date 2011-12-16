package models.occurrences;

import models.Event;

public class YearlyOccurrence extends Occurrence {

	public YearlyOccurrence(Event event) {
		super(event);
	}

	public YearlyOccurrence(Event event, int iteration) {
		super(event, iteration);
	}

	@Override
	public Occurrence getNext() {
		YearlyOccurrence next = new YearlyOccurrence(this.event, iteration + 1);

		next.start = this.start.plusYears(1);
		next.end = this.end.plusYears(1);

		return next;
	}

}
