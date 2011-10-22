package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.joda.time.DateTime;

import play.db.jpa.Model;

@Entity
public class Event extends Model {

	public String name;
	public Date start;
	public Date end;
	public boolean isPublic;
	public Date lowerBound;
	public Date upperBound;
	public boolean followable;
	
	@Lob
	public String note;

	@ManyToOne
	public User owner;
	
	@ManyToMany
	public List<User> follower;
	
	@ManyToMany
	public List<Calendar> calendars;
	
	public Event(String name, Date start, Date end, Calendar calendar,
			boolean isPublic, boolean followable) {
		this.name = name;
		this.start = start;
		this.end = end;
		this.calendars = new ArrayList<Calendar>();
		calendars.add(calendar);
		this.isPublic = isPublic;
		this.lowerBound = makeLowerBound(start);
		this.upperBound = makeUpperBound(end);
		this.followable = followable;
		this.owner = calendar.owner;
		this.follower = new ArrayList<User>();
	}

	public Event(String name, Date start, Date end, Calendar calendar,
			boolean isPublic, boolean followable, String note) {
		this(name, start, end, calendar, isPublic, followable);
		this.note = note;
	}

	public static Date makeLowerBound(Date startDate) {
		return new DateTime(startDate).withTime(0, 0, 0, 0).toDate();
	}

	public static Date makeUpperBound(Date endDate) {
		return new DateTime(endDate).withTime(23, 59, 59, 0).toDate();
	}

	public boolean happensOnDay(Date aDay) {
		DateTime dayLowerBound = new DateTime(makeLowerBound(aDay));
		DateTime dayUpperBound = new DateTime(makeUpperBound(aDay));
		DateTime eventStart = new DateTime(this.lowerBound);
		DateTime eventEnd = new DateTime(this.upperBound);
		return (dayLowerBound.isAfter(eventStart) || dayLowerBound
				.equals(eventStart))
				&& (dayUpperBound.isBefore(eventEnd) || dayUpperBound
						.equals(eventEnd));
	}
}
