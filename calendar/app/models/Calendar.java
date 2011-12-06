package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Query;

import org.joda.time.DateTime;

import play.db.jpa.JPA;
import play.db.jpa.Model;
import utilities.DayContainer;
/**
 * Calendar provides a model for a calendar or agenda. A calendar contains one or 
 * more {@link Event Events}, or is empty. The <code>owner</code> is a unique {@link User} who 
 * should be the only User who can manipulate this calendar. To distinguish the calendars, 
 * a <code>name</code> is assigned to a calendar, though the <code>name</code> does not have to be unique. 
 * Calendar provides methods to retrieve a calendar's <code>events</code> by several criteria. 
 * Example: one day's <code>events</code>, visible to any <code>User</code>.
 * <p>
 * All fields are <code>public</code> to provide access to the JPA.
 * 
 * @author alt+F4
 */
@Entity
public class Calendar extends Model {
	/**
	 * the calendar's <code>name</code> as non-unique identifier of this calendar.
	 */
	public String name;
	/**
	 * the unique <code>User</code> owning the calendar.
	 *  <br/>
	 *  This field is the inverse of {@link User#calendars} in the JPA relation,
	 *   as one <code>User</code> can own many calendars.
	 *   @see User
	 */
	@ManyToOne
	public User owner;
	/**
	 * a <code>List</code> of this calendar's <code>events</code>.
	 *  It is empty if there are no <code>events</code> but never <code>null</code>. 
	 *  The list is not sorted by default.
	 *  <br/>
	 *  This field is mapped by {@link Event#calendars} in a JPA <code>many-to-many</code> relation, 
	 *  since one calendar contains many <code>events</code> and an <code>event</code> can be in 
	 *  multiple calendars.
	 *  @see User
	 *  @see Event
	 */
	@ManyToMany(mappedBy = "calendars")
	public List<Event> events;
	
	/**
	 * Creates a new calendar by retrieving and setting it's <code>name</code> and <code>user</code>. 
	 * @param name		calendar's <code>name</code>
	 * @param owner		the <code>owner</code> of this calendar
	 */
	public Calendar(String name, User owner) {
		this.name = name;
		this.owner = owner;
		this.events = new ArrayList<Event>();
	}
	
	/**
	 * Lists {@link DayContainer DayContainers} of the <code>events</code> of a selected month. 
	 * A day has exactly one <code>DayContainer</code>. It's {@link DayContainer#type type} 
	 * is set with respect to the <code>events</code> taking place that day.  
	 * Only <code>events</code> visible to the requesting user <code>watchingUser</code> are 
	 * considered in the evaluation of the dayContainers <code>type</code>.
	 * The month is specified by a <code>Date</code> containing any day of this month. 
	 * In respect of the table, representing a month in the view, having 6 weeks, the list 
	 * contains 42 elements.
	 * 
	 * @param watchingUser		the requesting <code>user</code>. 
	 * The filtering of the lists elements is done in respect of this <code>user</code>.
	 * @param currentDate		<code>Date</code> containing a day of the month to be selected.
	 * @return					<code>List</code> containing one <code>DayContainer</code> for 
	 * each day. In respect of the table, representing a month in the view, having 6 weeks, the list 
	 * contains 42 elements.
	 * @see DayContainer
	 */
	public List<DayContainer> getCalendarData(User watchingUser, Date currentDate) {
		DateTime date = new DateTime(currentDate);

		DateTime now = new DateTime();
		DateTime firstDayOfMonth = date.withDayOfMonth(1);
		DateTime runDay = firstDayOfMonth.minusDays(firstDayOfMonth.dayOfWeek()
				.get());

		DayContainer[] days = new DayContainer[42];

		for (int i = 0; i < days.length; i++) {
			days[i] = new DayContainer();
			runDay = runDay.plusDays(1);
			days[i].date = runDay.toDate();
			DayContainer.DayContainerType type = DayContainer.DayContainerType.THISMONTH;
			if (runDay.getDayOfMonth() == date.getDayOfMonth()
					&& runDay.getMonthOfYear() == date.getMonthOfYear()
					&& runDay.getYear() == date.getYear()) {
				type = DayContainer.DayContainerType.SELECTED;
			} else if (runDay.getDayOfMonth() == now.getDayOfMonth()
					&& runDay.getMonthOfYear() == now.getMonthOfYear()
					&& runDay.getYear() == now.getYear()) {
				type = DayContainer.DayContainerType.TODAY;
			} else if (!runDay.monthOfYear().equals(date.monthOfYear())) {
				type = DayContainer.DayContainerType.OTHERMONTH;
			}
			days[i].type = type;
			days[i].containsEvents = (this.eventsAtDay(watchingUser, runDay.toDate()).size() > 0);
		}
		return (List<DayContainer>) Arrays.asList(days);
	}
	/**
	 * Lists all <code>events</code> taking place on a selected day (<code>currentDate</code>). 
	 * Only those <code>events</code> are returned, that are actually visible to the requesting user (<code>watchingUser</code>)
	 * @param watchingUser		the <code>user</code> requesting the list of <code>events</code>
	 * @param currentDate		a <code>Date</code> specifying the day of which the <code>events</code> should be listed.
	 * @return					a <code>List</code> of <code>events</code> taking place on the specified day.
	 * The events hidden from the requesting user are filtered. If there are no <code>events</code> visible on that day for 
	 * the specific user, the list is empty. 
	 */
	public List<Event> eventsAtDay(User watchingUser, Date currentDate) {
		Query eventsQuery = JPA
				.em()
				.createQuery(
						"SELECT e FROM Event e JOIN e.calendars c WHERE c.id = :id AND (e.isPublic = true OR c.owner.id = :watchingUserId)")
				.setParameter("id", this.id)
				.setParameter("watchingUserId", watchingUser.id);
		List<Event> results = eventsQuery.getResultList();
		List<Event> events = new ArrayList<Event>();
		for (Event e : results) {
			if (e.happensOnDay(currentDate)) {
				events.add(e);
			}
		}
		return events;
	}
	
	/**
	 * Gets all matching events.
	 * @author Team 1
	 */
	public List<Event> matchingEvents(User watchingUser, Date currentDate, String query) {
		if (query == null) {
			//System.out.println("Got null argument...");
			return null; 
		}
		if (query.equals("")) {
			//System.out.println("Got empty argument.");
			return null; 
		}
		
		Query eventsQuery = JPA
				.em()
				.createQuery(
						"SELECT e FROM Event e JOIN e.calendars c WHERE c.id = :id AND (e.isPublic = true OR c.owner.id = :watchingUserId)")
				.setParameter("id", this.id)
				.setParameter("watchingUserId", watchingUser.id);
		List<Event> results = eventsQuery.getResultList();
		List<Event> events = new ArrayList<Event>();
		for (Event e : results) {
			//System.out.println("Name: "+e.name+", query="+query);
			if (e.name.contains(query)) {
				events.add(e);
			}
		}
		return events;
	}
	
	
	/**
	 * Lists this calendars <code>events</code> that are not owned by this calendar's <code>owner</code>. 
	 * That is to say all the events the <code>owner</code> is following.
	 * @return 		<code>List</code> all this calendar's events the <code>owner</code> is following. 
	 * 	If there are none, the list is empty.
	 * @see User
	 */
	public List<Event> getFollowingEvents() {
		List<Event> followingEvents = new ArrayList<Event>();
		for (Event ev : this.events) {
			/*
			 * check if the owner of the calendar is equals to the event owner,
			 * if not, then this event is followed by the calendar owner.
			 */
			if (!ev.owner.equals(this.owner)) {
				followingEvents.add(ev);
			}
		}
		return followingEvents;
	}
}
