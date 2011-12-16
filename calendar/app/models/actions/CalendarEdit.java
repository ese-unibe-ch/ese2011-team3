package models.actions;

import models.Calendar;

public class CalendarEdit extends AbstractAction {

    private String name;
    private Long calendarId;
    private transient Calendar calendar;

    public CalendarEdit(Calendar calendar) {
	this.calendarId = calendar.id;
	this.name = calendar.name;
	this.calendar = calendar;
    }

    public void setName(String name) {
	this.calendar.name = name;
    }

    @Override
    public void execute() {
	this.calendar.save();
    }

    @Override
    public void undo() {
	this.calendar = Calendar.findById(this.calendarId);
	this.calendar.name = this.name;
	this.calendar.save();
    }

    public String toString() {
	Calendar calendar = Calendar.findById(this.calendarId);
	String out = "Edit calendar '" + this.name + "'";
	if (!calendar.name.equals(this.name)) {
	    out += " and change name to '" + calendar.name + "'";

	}
	return out;
    }
}
