package models.actions;

import java.util.ArrayList;

import models.Calendar;
import models.Event;
import models.User;

public class CalendarRemove extends AbstractAction {

    private ArrayList<Action> actions;
    private String calendarName;
    private Long userId;
    private transient Calendar calendar;

    public CalendarRemove(Calendar calendar, User user) {
	this.userId = user.id;
	this.calendarName = calendar.name;
	this.actions = new ArrayList<Action>();
	this.calendar = calendar;

	for (Event event : calendar.events) {
	    RemoveEvent a = new RemoveEvent(event);

	    this.actions.add(a);
	}

    }

    @Override
    public void execute() {
	for (Action a : this.actions) {
	    a.execute();
	}

	this.calendar.delete();
    }

    @Override
    public void undo() {
	User user = User.findById(this.userId);
	Calendar calendar = new Calendar(this.calendarName, user).save();

	for (Action action : this.actions) {

	    RemoveEvent a = (RemoveEvent) action;
	    a.addCalendar(calendar);
	    a.undo();
	}

    }
}
