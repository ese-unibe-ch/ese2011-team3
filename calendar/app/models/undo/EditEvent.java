package models.undo;

import java.util.Date;

import models.Event;
import utilities.OccurrenceType;

public class EditEvent implements Action {

    private Event event;
    private String name;
    private String note;
    private Date end;
    private Date start;
    private OccurrenceType occurrenceType;

    public EditEvent(Event event) {
	this.event = event;
	this.name = event.name;
	this.note = event.name;
	this.end = event.end;
	this.start = event.start;
	this.occurrenceType = event.occurrenceType;
    }

    @Override
    public void execute() {
	// TODO Auto-generated method stub

    }

    @Override
    public void undo() {
	this.event.name = name;
	this.event.note = note;
	this.event.start = this.start;
	this.event.end = this.end;
	this.event.occurrenceType = this.occurrenceType;
	this.event.save();
    }

}
