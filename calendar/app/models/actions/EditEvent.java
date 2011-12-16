package models.actions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Calendar;
import models.Event;
import models.User;
import utilities.OccurrenceType;

public class EditEvent extends AbstractAction {
    private transient Event event;
    private String name;
    private String note;
    private Date start;
    private Date end;
    private User owner;
    private OccurrenceType occurrenceType;
    private boolean isPublic;
    private Long id;

    public EditEvent(Event event) {
	this.event = event;
	// copy all values
	this.name = event.name;
	this.note = event.note;
	this.start = event.start;
	this.end = event.end;
	this.owner = event.owner;

	this.occurrenceType = event.occurrenceType;
	this.isPublic = event.isPublic;
	this.event = event;
	this.id = event.id;
    }

    @Override
    public void execute() {
	this.event.save();
    }

    @Override
    public void undo() {
	Event event = Event.findById(this.id);
	event.name = name;
	event.note = note;
	event.start = this.start;
	event.end = this.end;
	event.occurrenceType = this.occurrenceType;
	event.save();
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
	Event event = Event.findById(this.id);
	String out = "Edit event '" + this.name + "'";
	if (!event.name.equals(this.name)) {
	    out += " and change name to '" + event.name + "'";

	}
	return out;
    }
}
