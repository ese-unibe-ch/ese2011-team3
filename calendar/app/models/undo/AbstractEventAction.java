package models.undo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Calendar;
import models.Event;
import models.User;
import utilities.OccurrenceType;

abstract class AbstractEventAction implements Action {
    protected String name;
    protected String note;
    protected Date start;
    protected Date end;
    protected List<Calendar> calendars;
    protected User owner;
    protected boolean isPublic;
    protected OccurrenceType occurrenceType;
    protected Event event;

    public AbstractEventAction(Event event) {
	// copy all values
	this.name = event.name;
	this.note = event.note;
	this.start = event.start;
	this.end = event.end;
	this.owner = event.owner;
	this.calendars = event.calendars;
	this.occurrenceType = event.occurrenceType;
	this.isPublic = event.isPublic;
	this.event = event;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.event.name = name;
    }

    public String getNote() {
	return note;
    }

    public void setNote(String note) {
	this.event.note = note;
    }

    public Date getStart() {
	return start;
    }

    public void setStart(Date start) {
	this.event.start = start;
    }

    public Date getEnd() {
	return end;
    }

    public void setEnd(Date end) {
	this.event.end = end;
    }

    public List<Calendar> getCalendars() {
	return calendars;
    }

    public void setCalendars(List<Calendar> calendars) {
	// copy list, otherwise concurrent issues could occur.
	this.event.calendars = new ArrayList<Calendar>(calendars);
    }

    public User getOwner() {
	return owner;
    }

    public void setOwner(User owner) {
	this.event.owner = owner;
    }

    public boolean isPublic() {
	return isPublic;
    }

    public void setPublic(boolean isPublic) {
	this.event.setPublic(isPublic);
	// this.isPublic = isPublic;
    }

    public OccurrenceType getOccurrenceType() {
	return occurrenceType;
    }

    public void setOccurrenceType(OccurrenceType occurrenceType) {
	this.event.occurrenceType = occurrenceType;
    }

    public String toString() {
	return this.getClass().getSimpleName() + ", " + this.event.name
		+ " to: " + this.name;
    }

}
