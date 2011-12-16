package models.occurrences;

import models.Event;

public class EndMonthOccurrence extends Occurrence {

	public EndMonthOccurrence(Event event) {
		this(event, 0);

		int offset = this.start.dayOfMonth().withMaximumValue().getDayOfMonth()
				- this.start.getDayOfMonth();
		this.start = this.start.plusDays(offset);
		this.end = this.end.plusDays(offset);
	}

	public EndMonthOccurrence(Event event, int iteration) {
		super(event, iteration);
	}

	@Override
	public Occurrence getNext() {
		EndMonthOccurrence next = new EndMonthOccurrence(this.event,
				iteration + 1);

		next.start = this.start.plusMonths(1);
		next.end = this.end.plusMonths(1);

		int offset = next.start.dayOfMonth().withMaximumValue().getDayOfMonth()
				- next.start.getDayOfMonth();
		next.start = next.start.plusDays(offset);
		next.end = next.end.plusDays(offset);

		return next;
	}

}
