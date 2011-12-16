package models.occurrences;

import models.Event;

public class WorkDayOccurrence extends Occurrence {

	public WorkDayOccurrence(Event event) {
		super(event);

		int dayOfWeek = this.start.getDayOfWeek();
		if (dayOfWeek > 5) {
			this.start = this.start.plusDays(8 - dayOfWeek);
			this.end = this.end.plusDays(8 - dayOfWeek);
		}
	}

	public WorkDayOccurrence(Event event, int iteration) {
		super(event, iteration);
	}

	@Override
	public Occurrence getNext() {
		WorkDayOccurrence next = new WorkDayOccurrence(this.event,
				iteration + 1);

		next.start = this.start.plusDays(1);
		next.end = this.end.plusDays(1);

		int dayOfWeek = next.start.getDayOfWeek();
		if (dayOfWeek > 5) {
			next.start = next.start.plusDays(8 - dayOfWeek);
			next.end = next.end.plusDays(8 - dayOfWeek);
		}

		return next;
	}

}
