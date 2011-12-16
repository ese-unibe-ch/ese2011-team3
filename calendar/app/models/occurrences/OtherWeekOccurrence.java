package models.occurrences;

import models.Event;

public class OtherWeekOccurrence extends Occurrence {

	public OtherWeekOccurrence(Event event) {
		super(event);
		// TODO Auto-generated constructor stub
	}

	public OtherWeekOccurrence(Event event, int iteration) {
		super(event, iteration);
	}

	@Override
	public Occurrence getNext() {
		OtherWeekOccurrence next = new OtherWeekOccurrence(this.event,
				iteration + 1);

		next.start = this.start.plusWeeks(2);
		next.end = this.end.plusWeeks(2);

		return next;
	}

}
