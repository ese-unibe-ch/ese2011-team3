package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import models.occurrences.Occurrence;

import org.joda.time.DateTime;

import play.db.jpa.JPA;

public class GlobalCalendar extends Calendar {

	public GlobalCalendar(String name, User owner) {
		super(name, owner);
		this.id = 0L;
		this.owner = owner;
	}

	@Override
	// watching user must be the owner, because private events are not filtered
	public List<Occurrence> eventsAtDay(User watchingUser, DateTime currentDate) {
		Query eventsQuery = JPA.em()
				.createQuery("SELECT e FROM Event e WHERE e.owner.id = :owner")
				.setParameter("owner", watchingUser.id);
		List<Event> results = eventsQuery.getResultList();
		List<Occurrence> events = new ArrayList<Occurrence>();
		for (Event e : results) {
			Occurrence o;
			if ((o = Occurrence.getOccurrence(e).occurrenceForDay(currentDate)) != null) {
				events.add(o);
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
