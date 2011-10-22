package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.joda.time.DateTime;

import play.db.jpa.JPA;
import play.db.jpa.Model;

@Entity
public class Calendar extends Model {

    public String name;

    @ManyToOne
    public User owner;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL)
    public List<Event> events;

    public Calendar(String name, User owner) {
	this.name = name;
	this.owner = owner;
	this.events = new ArrayList<Event>();
    }

    public static List<Event> getAllEventsOnDay(Calendar calendar, Date aDate) {
	Date start = new DateTime(aDate).withTime(0, 0, 0, 0).toDate();
	Date end = new DateTime(aDate).withTime(23, 59, 59, 999).toDate();
	Query query = JPA
		.em()
		.createQuery(
			"from Event where lowerBound <= :start and upperBound >= :end and calendar_id=:cid order by start")
		.setParameter("start", start, TemporalType.DATE)
		.setParameter("end", end, TemporalType.DATE)
		.setParameter("cid", calendar.id);

	List<Event> events = query.getResultList();
	return events;
    }

}
