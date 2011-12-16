package models.actions;

import models.Calendar;
import models.Event;

public class UnfollowEvent extends FollowEvent {

    public UnfollowEvent(Event event, Calendar calendar) {
	super(event, calendar);
    }

    @Override
    public void execute() {
	super.undo();
    }

    @Override
    public void undo() {
	super.execute();
    }

    public String toString() {
	return "Unfollow event '" + this.eventName + "' in Calendar '"
		+ this.calendarName + "'";

    }

}
