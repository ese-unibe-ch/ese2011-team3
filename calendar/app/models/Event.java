package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

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
		this.lowerBound = (Date) start.clone();
		// TODO: refactor this!
		this.lowerBound.setHours(0);
		this.lowerBound.setMinutes(0);
		this.lowerBound.setSeconds(0);
		// TODO: refactor this!
		this.upperBound = (Date) end.clone();
		this.upperBound.setHours(23);
		this.upperBound.setMinutes(59);
		this.upperBound.setSeconds(59);
	}

	public Event(String name, Date start, Date end, Calendar calendar,
			String note) {
		this(name, start, end, calendar);
		this.note = note;
	}

	public static Date makeLowerBound(Date startDate) {
		Date copy = (Date) startDate.clone();
		copy.setHours(0);
		copy.setMinutes(0);
		copy.setSeconds(0);
		return copy;
	}

	public static Date makeUpperBound(Date endDate) {
		Date copy = (Date) endDate.clone();
		copy.setHours(23);
		copy.setMinutes(59);
		copy.setSeconds(59);
		return copy;
	}
}
