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

	@Lob
	public String note;

	@ManyToOne
	public Calendar calendar;

	public Event(String name, Date start, Date end, Calendar calendar) {
		this.name = name;
		this.start = start;
		this.end = end;
		this.calendar = calendar;
	}

	public Event(String name, Date start, Date end, Calendar calendar,
			String note) {
		this(name, start, end, calendar);
		this.note = note;
	}
}
