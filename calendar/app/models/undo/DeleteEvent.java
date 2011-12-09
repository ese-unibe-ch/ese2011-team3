package models.undo;

import models.Calendar;
import models.Event;

public class DeleteEvent extends AbstractEventAction {

    public DeleteEvent(Event event) {
	super(event);
    }

    @Override
    public void execute() {
	for (Calendar calendar : this.calendars) {
	    event.calendars.remove(calendar);
	    calendar.events.remove(event);
	    calendar.save();
	}

	this.event.delete();
    }

    @Override
    public void undo() {
	Calendar aCalendar = this.calendars.get(0);
	this.calendars.remove(aCalendar);

	Event event = new Event(this.name, this.note, this.start, this.end,
		this.owner, aCalendar, this.occurrenceType);

	for (Calendar calendar : this.calendars) {
	    event.calendars.add(calendar);
	    calendar.save();

	}
	event.save();
	aCalendar.save();
    }

}
