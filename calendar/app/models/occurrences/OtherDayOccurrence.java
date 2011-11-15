package models.occurrences;

import models.Event;

public class OtherDayOccurrence extends Occurrence {

	public OtherDayOccurrence(Event event) {
		super(event);
		// TODO Auto-generated constructor stub
	}

	public OtherDayOccurrence(Event event, int iteration) {
		super(event, iteration);
	}

	@Override
	public Occurrence getNext() {
		OtherDayOccurrence next = new OtherDayOccurrence(this.event,
				iteration + 1);

		next.start = this.start.plusDays(2);
		next.end = this.end.plusDays(2);

		return next;
	}

}
