package models.undo;

import models.Event;

public class EditEvent extends AbstractEventAction {

    public EditEvent(Event event) {
	super(event);
    }

    @Override
    public void execute() {
	event.save();
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
