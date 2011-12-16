package models.actions;

import java.io.Serializable;

import models.Calendar;
import models.Event;

public class SaveEvent extends AbstractAction implements Serializable {

    private Long calendarId;
    private Long eventId;
    private transient Calendar calendar;
    private transient Event event;
    private String eventName;
    private String calendarName;

    public SaveEvent(Event event, Calendar calendar) {
	this.calendarId = calendar.id;
	this.eventId = event.id;
	this.calendar = calendar;
	this.event = event;
	this.eventName = this.event.name;
	this.calendarName = this.calendar.name;
    }

    @Override
    public void execute() {
	this.event.save();
	this.calendar.save();
	this.eventId = event.id;
    }

    @Override
    public void undo() {
	event = Event.findById(eventId);
	calendar = Calendar.findById(calendarId);

	calendar.events.remove(event);
	calendar.save();
	event.calendars.remove(calendar);
	event.delete();
    }

    public String toString() {
	return "Save and add event '" + this.eventName + "' to calendar "
		+ this.calendarName;
    }
}
