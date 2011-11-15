package models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.joda.time.DateTime;

import play.db.jpa.Model;
import utilities.OccurrenceType;

import com.google.gson.annotations.Expose;

/**
 * Event is an entry in one or more {@link Calendar} that happens over a period
 * of time and can be repeated every day, week, month or year. It can be public
 * or not. If the <code>event</code> is not public, only the <code>owner</code>
 * ({@link User}) can see the <code>event</code>. If it's public, all users can
 * see it and can follow it, which means that they can add the
 * <code>event</code> to their calendar, but can't edit or delete it.
 * 
 * @author Alt-F4
 */
@Entity
public class Event extends Model {
	@Expose
	public String name;
	@Expose
	public Date start;
	@Expose
	public Date end;
	public boolean isPublic;
	@Enumerated(EnumType.STRING)
	public OccurrenceType occurrenceType;

	/**
	 * the <user>user</code> who created the <code>event</code>. Only he is able
	 * to edit events.
	 * 
	 * @see User
	 */
	@ManyToOne
	public User owner;

	/**
	 * a note attached to the event.
	 */
	@Lob
	public String note;

	/**
	 * the calendars this event is in.
	 * 
	 * @see Calendar
	 */
	@ManyToMany
	public List<Calendar> calendars;

	@ElementCollection
	public List<Integer> occurrenceExceptions;

	/**
	 * creates an event which is by default of type RepeatableType.NONE and is
	 * not public and therefore not followable.
	 * 
	 * @param name
	 *            the name of this event
	 * @param note
	 *            a note of this event
	 * @param start
	 *            the date the event begins.
	 * @param end
	 *            the date the event ends.
	 * @param owner
	 *            the user who creates this event
	 * @param calendar
	 *            the calendar which stores this event
	 */
	public Event(String name, String note, Date start, Date end, User owner,
			Calendar calendar) {
		this.name = name;
		this.note = note;
		this.start = start;
		this.end = end;
		this.owner = owner;
		this.occurrenceType = OccurrenceType.NONE;

		this.calendars = new ArrayList<Calendar>();
		this.occurrenceExceptions = new ArrayList<Integer>();
		this.calendars.add(calendar);
	}

	/**
	 * creates an event which is by default of type RepeatableType.NONE and is
	 * not public and therefore not followable.
	 * 
	 * @param name
	 *            the name of this event
	 * @param note
	 *            a note of this event
	 * @param start
	 *            the date the event begins.
	 * @param end
	 *            the date the event ends.
	 * @param owner
	 *            the user who creates this event
	 * @param calendar
	 *            the calendar which stores this event
	 * @param repetableType
	 *            the repeatable type of this event
	 */
	public Event(String name, String note, Date start, Date end, User owner,
			Calendar calendar, OccurrenceType repeatableType) {
		this.name = name;
		this.note = note;
		this.start = start;
		this.end = end;
		this.owner = owner;
		this.occurrenceType = repeatableType;

		this.calendars = new ArrayList<Calendar>();
		this.calendars.add(calendar);
	}

	/**
	 * creates an event.
	 * 
	 * @param name
	 *            the name of this event
	 * @param note
	 *            a note of this event
	 * @param start
	 *            the date the event begins.
	 * @param end
	 *            the date the event ends.
	 * @param owner
	 *            the user who creates this event
	 * @param calendar
	 *            the calendar which stores this event
	 * @param isPublic
	 *            the flag whether the event is public or private
	 */
	public Event(String name, String note, Date start, Date end, User owner,
			Calendar calendar, boolean isPublic) {
		this(name, note, start, end, owner, calendar);
		this.isPublic = isPublic;
	}

	/**
	 * creates an event.
	 * 
	 * @param name
	 *            the name of this event
	 * @param note
	 *            a note of this event
	 * @param start
	 *            the date the event begins.
	 * @param end
	 *            the date the event ends.
	 * @param owner
	 *            the user who creates this event
	 * @param calendar
	 *            the calendar which stores this event
	 * @param isPublic
	 *            the flag whether the event is public or private
	 * @param repeatableType
	 *            the repeatable type of this event
	 */
	public Event(String name, String note, Date start, Date end, User owner,
			Calendar calendar, boolean isPublic, OccurrenceType occurrenceType) {
		this(name, note, start, end, owner, calendar, isPublic);
		this.occurrenceType = occurrenceType;
	}

	public OccurrenceType getOccurrenceType() {
		// TODO fix this after bootstrap has been adapted!!
		return this.occurrenceType == null ? OccurrenceType.NONE
				: this.occurrenceType;
	}

	public void setStart(String start) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.start = fmt.parse(start);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void setEnd(String end) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.end = fmt.parse(end);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * makes this event public if true. if public is false, then all followers
	 * of this event are removed.
	 * 
	 * @param isPublic
	 *            if <code>true</code> make this event public and therefore
	 *            followable.
	 */
	public void setPublic(boolean isPublic) {

		/*
		 * check if state has changed from followable to not followable
		 */
		boolean switched = this.isPublic && !isPublic;

		this.isPublic = isPublic;

		/*
		 * remove this event from all calendars of followers
		 */
		if (switched == true) {
			List<Calendar> calendars = new ArrayList<Calendar>(this.calendars);
			for (Calendar calendar : calendars) {
				/*
				 * if the calendar owner is not equals to the event owner, then
				 * the calendar owner is a follower of this event.
				 */
				if (calendar.owner != this.owner) {
					this.unfollow(calendar);
					calendar.save();
				}
			}
		}
	}

	public void addOccurrenceException(Integer index) {
		this.occurrenceExceptions.add(index);
	}

	public boolean occurrenceHappens(Integer index) {
		return !this.occurrenceExceptions.contains(index);
	}

	/**
	 * returns a set of all followers of this event (including the owner )
	 * 
	 * @return a set of <code>user</code> that follows this event
	 */
	public Set<User> getFollowers() {
		Set<User> followers = new HashSet<User>();
		for (Calendar calendar : this.calendars) {
			followers.add(calendar.owner);
		}
		return followers;
	}

	/**
	 * follow this event
	 * 
	 * @param calendar
	 *            the <code>calendar</code> the event gets added (followed) to.
	 * @see Calendar
	 */
	public void follow(Calendar calendar) {
		this.calendars.add(calendar);
		calendar.events.add(this);
	}

	/**
	 * unfollow this event
	 * 
	 * @param calendar
	 *            the <code>calendar</code> the event gets removed (unfollowed)
	 *            from.
	 * @see Calendar
	 */
	public void unfollow(Calendar calendar) {
		this.calendars.remove(calendar);
		calendar.events.remove(this);
	}

	/**
	 * checks if this event is followed by an user
	 * 
	 * @param user
	 * @return boolean
	 * 
	 */
	public boolean isFollowedBy(User user, Calendar calendar) {
		return (!this.owner.equals(user) && this.calendars.contains(calendar));
	}

	public boolean isOverlapping(Date startDate, Date endDate) {
		DateTime evStart = new DateTime(this.start);
		DateTime evEnd = new DateTime(this.end);

		DateTime start = new DateTime(startDate);
		DateTime end = new DateTime(endDate);

		return ((start.isBefore(evStart) || start.equals(evStart)) && (end
				.isAfter(evEnd) || end.equals(evEnd)))
				|| ((start.isBefore(evStart) || start.equals(evStart)) && end
						.isAfter(evStart))
				|| (start.isBefore(evEnd) && (end.isAfter(evEnd) || end
						.equals(evEnd)))
				|| (evStart.isBefore(start) && evEnd.isAfter(end));
	}
}
