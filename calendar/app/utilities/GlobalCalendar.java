package utilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import models.Calendar;
import models.Event;
import models.User;

import play.db.jpa.JPA;

public class GlobalCalendar extends Calendar {

    public GlobalCalendar(String name, User owner) {
	super(name, owner);
	this.id = 0L;
	this.owner = owner;
    }

    @Override
    // watching user must be the owner, because private events are not filtered
    public List<Event> eventsAtDay(User watchingUser, Date currentDate) {
	Query eventsQuery = JPA.em()
		.createQuery("SELECT e FROM Event e WHERE e.owner.id = :owner")
		.setParameter("owner", watchingUser.id);
	List<Event> results = eventsQuery.getResultList();
	List<Event> events = new ArrayList<Event>();
	for (Event e : results) {
	    if (e.happensOnDay(currentDate)) {
		events.add(e);
	    }
	}
	return events;
    }

    @Override
    // TODO: fix events attribute in this calendar
    public List<Event> getFollowingEvents() {
	return null;
    }

}
