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
	public boolean isFollowable;

	@ManyToOne
	public User owner;

	@Lob
	public String note;

	@ManyToMany
	public List<Calendar> calendars;

	/**
	 * creates an event which is by default not followable and not public
	 * 
	 * @param name name of this event
	 * @param note note of this event
	 * @param start starting date
	 * @param end ending date
	 * @param owner a user who owns this event
	 * @param calendar a calendar which stores this event
	 */
	public Event(String name, String note, Date start, Date end, User owner,
			Calendar calendar) {
		this.name = name;
		this.note = note;
		this.start = start;
		this.end = end;
		this.owner = owner;

		this.calendars = new ArrayList<Calendar>();
		this.calendars.add(calendar);
	}

	/**
	 * creates an event
	 * 
	 * @param name name of this event
	 * @param note note of this event
	 * @param start starting date
	 * @param end ending date
	 * @param owner a user who owns this event
	 * @param calendar a calendar which stores this event
	 * @param isPublic
	 * @param isFollowable
	 */
	public Event(String name, String note, Date start, Date end, User owner,
			Calendar calendar, boolean isPublic, boolean isFollowable) {
		this(name, note, end, end, owner, calendar);
		this.isPublic = isPublic;
		this.isFollowable = isFollowable;
	}

	/**
	 * @return a lower bound of this event
	 */
	public DateTime getLowerBound() {
		return makeLowerBound(this.start);
	}

	/**
	 * @return a upper bound of this event
	 */
	public DateTime getUpperBound() {
		return makeUpperBound(this.end);
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	/*
	 * helper
	 */
	private static DateTime makeLowerBound(Date startDate) {
		return new DateTime(startDate).withTime(0, 0, 0, 0);
	}

	/*
	 * helper
	 */
	private static DateTime makeUpperBound(Date endDate) {
		return new DateTime(endDate).withTime(23, 59, 59, 0);
	}

	/**
	 * checks if an event happens on a specific day
	 * 
	 * @param aDay a day
	 * @return true if this event happens on this day
	 */
	public boolean happensOnDay(Date aDay) {
		DateTime dayLowerBound = makeLowerBound(aDay);
		DateTime dayUpperBound = makeUpperBound(aDay);
		DateTime eventStart = this.getLowerBound();
		DateTime eventEnd = this.getUpperBound();
		return (dayLowerBound.isAfter(eventStart) || dayLowerBound
				.equals(eventStart))
				&& (dayUpperBound.isBefore(eventEnd) || dayUpperBound
						.equals(eventEnd));
	}

	/**
	 * returns a list of all followers of this event
	 * 
	 * @return a list of followers
	 */
	public List<User> getFollowers() {
		List<User> followers = new ArrayList<User>();
		for (Calendar calendar : this.calendars) {
			if (!calendar.owner.equals(this.owner))
				followers.add(calendar.owner);
		}
		return followers;
	}

	/**
	 * follow this event
	 * 
	 * @param calendar
	 */
	public void follow(Calendar calendar) {
		this.calendars.add(calendar);
		calendar.events.add(this);
	}

	/**
	 * unfollow this event
	 * 
	 * @param calendar
	 */
	public void unfollow(Calendar calendar) {
		this.calendars.remove(calendar);
		calendar.events.remove(this);
	}

	/**
	 * check if this event is followed an user
	 * 
	 * @param user
	 * @return boolean
	 * 
	 */
	public boolean isFollowedBy(User user) {
		for (Calendar calendar : this.calendars) {
			if (calendar.owner.equals(user) && !this.owner.equals(user)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * check if this event is followed by another user except the owner
	 * 
	 * @return boolean
	 */
	public boolean isFollowed() {
		for (Calendar calendar : this.calendars) {
			if (!calendar.owner.equals(this.owner)) {
				return true;
			}
		}
		return false;
	}
}
