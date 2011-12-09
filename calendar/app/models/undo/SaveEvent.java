package models.undo;

import models.Event;

public class SaveEvent implements Action {

    private Event event;

    public SaveEvent(Event event) {
	this.event = event;
    }

    @Override
    public void execute() {
	// TODO Auto-generated method stub

    }

    @Override
    public void undo() {
	this.event.delete();

    }
}
