package models.actions;

import java.io.Serializable;

import models.Calendar;
import models.Event;

public class FollowEvent extends AbstractAction implements Serializable {

    private transient Event event;
    private transient Calendar calendar;
    private Long calendarId;
    private Long eventId;
    protected String calendarName;
    protected String eventName;

    public FollowEvent(Event event, Calendar calendar) {
	this.event = event;
	this.eventName = event.name;
	this.calendarName = calendar.name;
	this.calendar = calendar;
	this.eventId = event.id;
	this.calendarId = calendar.id;
    }

    @Override
    public void execute() {

	event = Event.findById(eventId);
	calendar = Calendar.findById(calendarId);

	this.event.calendars.add(calendar);
	calendar.events.add(this.event);

	this.event.save();
	this.calendar.save();
    }

    @Override
    public void undo() {
	event = Event.findById(eventId);
	calendar = Calendar.findById(calendarId);

	this.event.calendars.remove(calendar);
	calendar.events.remove(event);
	this.event.save();
	this.calendar.save();
    }

    public String toString() {
	return "Follow event '" + this.eventName + "' in Calendar '"
		+ this.calendarName + "'";

    }

}
