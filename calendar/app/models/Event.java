package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.joda.time.DateTime;

import play.db.jpa.Model;

@Entity
public class Event extends Model {

	public String name;
	public Date start;
	public Date end;

	public Date lowerBound;
	public Date upperBound;

	@Lob
	public String note;

	@ManyToOne
	public Calendar calendar;

	public Event(String name, Date start, Date end, Calendar calendar) {
		this.name = name;
		this.start = start;
		this.end = end;
		this.calendar = calendar;
		this.lowerBound = makeLowerBound(start);
		this.upperBound = makeUpperBound(end);
	}

	public Event(String name, Date start, Date end, Calendar calendar,
			String note) {
		this(name, start, end, calendar);
		this.note = note;
	}

	public static Date makeLowerBound(Date startDate) {
		return new DateTime(startDate).withTime(0, 0, 0, 0).toDate();
	}

	public static Date makeUpperBound(Date endDate) {
		return new DateTime(endDate).withTime(23, 59, 59, 0).toDate();
	}
}
