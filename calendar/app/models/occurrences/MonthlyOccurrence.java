package models.occurrences;

import models.Event;

public class MonthlyOccurrence extends Occurrence {

	public MonthlyOccurrence(Event event) {
		super(event);
	}

	public MonthlyOccurrence(Event event, int iteration) {
		super(event, iteration);
	}

	@Override
	public Occurrence getNext() {
		MonthlyOccurrence next = new MonthlyOccurrence(this.event,
				iteration + 1);

		next.start = this.start.plusMonths(1);
		next.end = this.end.plusMonths(1);

		return next;
	}

}
