package models.occurrences;

import models.Event;

public class OneTimeOccurrence extends Occurrence {

	public OneTimeOccurrence(Event event) {
		super(event);
	}

	@Override
	public Occurrence getNext() {
		return null;
	}

}
