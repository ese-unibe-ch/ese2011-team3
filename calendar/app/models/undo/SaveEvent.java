package models.undo;

import models.Calendar;
import models.Event;

public class SaveEvent extends AbstractEventAction {

    private Calendar calendar;

    public SaveEvent(Event event, Calendar calendar) {
	super(event);
	this.calendar = calendar;
    }

    @Override
    public void execute() {
	this.event.save();
	this.calendar.save();
    }

    @Override
    public void undo() {
	this.event.delete();

    }
}
