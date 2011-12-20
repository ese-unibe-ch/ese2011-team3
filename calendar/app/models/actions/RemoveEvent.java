package models.actions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Calendar;
import models.Event;
import models.User;
import play.db.jpa.JPA;
import utilities.OccurrenceType;

public class RemoveEvent extends AbstractAction {

    private Event event;
    private String name;
    private String note;
    private Date start;
    private Date end;
    private User owner;
    private OccurrenceType occurrenceType;
    private boolean isPublic;
    private Long id;
    private ArrayList<Long> calendarIds = new ArrayList<Long>();

    public RemoveEvent(Event event) {
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

	for (Calendar calendar : event.calendars) {
	    this.calendarIds.add(calendar.id);
	}
    }

    @Override
    public void execute() {
	List<Calendar> calendars = new ArrayList<Calendar>(this.event.calendars);
	for (Calendar calendar : calendars) {
	    this.event.calendars.remove(calendar);
	    calendar.events.remove(this.event);
	    calendar.save();
	}

	this.event.delete();
    }

    @Override
    public void undo() {
	List<Calendar> calendars = JPA.em()
		.createQuery("SELECT c FROM Calendar c where c.id in :list")
		.setParameter("list", this.calendarIds).getResultList();

	Calendar aCalendar = calendars.get(0);
	calendars.remove(aCalendar);

	Event event = new Event(this.name, this.note, this.start, this.end,
		this.owner, aCalendar, this.occurrenceType);

	for (Calendar calendar : calendars) {
	    event.calendars.add(calendar);
	    calendar.save();

	}
	event.save();
	aCalendar.save();
    }

    public void addCalendar(Calendar calendar) {
	this.calendarIds.add(calendar.id);
    }

}
