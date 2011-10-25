package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Calendar;
import models.Event;
import models.User;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import play.data.validation.Required;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Events extends Controller {
    // HH:mm
    private static final String regexTime = "^([0-1][0-9]|[2][0-3]):([0-5][0-9])$";

    public static void createEvent(Long calendarId, @Required String name,
	    @Required Date startDate, @Required Date endDate,
	    @Required String startTime, @Required String endTime,
	    @Required boolean isPublic, @Required boolean isFollowable,
	    String note) {

	validation.required(name);
	validation.required(startDate);
	validation.required(endDate);
	validation.required(isPublic);
	validation.required(isFollowable);
	validation.match(startTime, regexTime).message("Invalid!");
	validation.match(endTime, regexTime).message("Invalid!");

	if (validation.hasErrors()) {
	    params.flash();
	    validation.keep();
	    addEvent(calendarId);
	}

	Calendar calendar = Calendar.findById(calendarId);

	startDate = helperCreateDate(startDate, startTime, "HH:mm");
	endDate = helperCreateDate(endDate, endTime, "HH:mm");

	User owner = calendar.owner;
	new Event(name, note, startDate, endDate, owner, calendar, isPublic,
		isFollowable).save();

	calendar.save();

	String nickname = calendar.owner.nickname;
	Calendars.showCalendar(nickname, calendarId);
    }

    private static Date helperCreateDate(Date date, String timeString,
	    String pattern) {
	DateTimeFormatter parser = DateTimeFormat.forPattern("HH:mm");
	DateTime time = parser.parseDateTime(timeString);
	DateTime aDate = new DateTime(date);
	aDate = aDate.withTime(time.getHourOfDay(), time.getMinuteOfHour(),
		time.getSecondOfMinute(), time.getMillisOfSecond());
	return aDate.toDate();
    }

    public static void updateEvent(Long eventId, Long calendarId,
	    @Required String name, @Required Date startDate,
	    @Required Date endDate, @Required String startTime,
	    @Required String endTime, @Required boolean isPublic,
	    @Required boolean isFollowable, String note) {

	validation.required(name);
	validation.required(startDate);
	validation.required(endDate);
	validation.match(startTime, regexTime).message("Invalid!");
	validation.match(endTime, regexTime).message("Invalid!");
	validation.required(isPublic);

	if (validation.hasErrors()) {
	    params.flash();
	    validation.keep();
	    editEvent(eventId, calendarId);
	}

	Event event = Event.findById(eventId);
	event.name = name;
	event.start = helperCreateDate(startDate, startTime, "HH:mm");
	event.end = helperCreateDate(endDate, endTime, "HH:mm");
	event.lowerBound = Event.makeLowerBound(startDate);
	event.upperBound = Event.makeUpperBound(endDate);
	event.note = note;
	event.isPublic = isPublic;
	event.isFollowable = isFollowable;

	/*
	 * if an event is set to private or not followable, then all user which
	 * follow this event are set to non followers.
	 */
	if (isFollowable == false || isPublic == false) {
	    List<Calendar> calendars = new ArrayList<Calendar>(event.calendars);
	    for (Calendar calendar : calendars) {
		/*
		 * if the calendar owner is not equals to the event owner, then
		 * the calendar owner is a follower of this event.
		 */
		if (calendar.owner != event.owner) {
		    event.unfollow(calendar);
		    event.save();
		    calendar.save();
		}
	    }
	}
	event.save();
	String nickname = event.owner.nickname;
	// TODO:
	Date aDate = event.start;
	Calendars.showDate(nickname, calendarId, aDate);
    }

    public static void editEvent(Long eventId, Long calendarId) {
	Event event = Event.findById(eventId);
	// TODO:
	Calendar calendar = Calendar.find("byId", calendarId).first();
	User user = event.owner;
	render(calendar, event, user);
    }

    public static void addEvent(Long calendarId) {
	Calendar calendar = Calendar.findById(calendarId);
	User user = calendar.owner;
	render(calendar, user);
    }

    public static void removeEvent(Long eventId, Long calendarId) {
	Calendar calendar = Calendar.find("byId", calendarId).first();
	Event event = Event.findById(eventId);
	calendar.events.remove(event);
	event.calendars.remove(calendar);

	event.save();
	if (event.calendars.size() == 0) {
	    event.delete();
	}
	calendar.save();

	String nickname = event.owner.nickname;
	Calendars.showCalendar(nickname, calendarId);
    }

    public static void showFollowingEvents() {
	User user = User.find("byNickname", Security.connected()).first();

	List<Calendar> calendars = user.calendars;
	List<Event> followEvents = new ArrayList<Event>();

	for (Calendar calendar : calendars) {
	    followEvents.addAll(calendar.getFollowingEvents());
	}

	render(followEvents);
    }

    public static void showFollowedEvents() {
	User user = User.find("byNickname", Security.connected()).first();

	List<Calendar> calendars = user.calendars;
	List<Event> events = new ArrayList<Event>();

	for (Calendar calendar : calendars) {
	    events.addAll(calendar.events);
	}
	ArrayList<Event> followEvents = new ArrayList<Event>();
	for (Event ev : events) {
	    if (ev.isFollowed()) {
		followEvents.add(ev);
	    }
	}
	render(followEvents);
    }

    public static void selectCalendarToFollowEvent(Long eventId,
	    Long originalCalendarId) {
	User connectedUser = User.find("byNickname", Security.connected())
		.first();
	List<Calendar> calendars = connectedUser.calendars;
	Calendar originalCalendar = Calendar.find("byId", originalCalendarId)
		.first();
	Event event = Event.find("byId", eventId).first();
	render(calendars, originalCalendar, event);
    }

    public static void followEvent(Long ownCalendarId, Long originalCalendarId,
	    Long eventId) {

	Event event = Event.find("byId", eventId).first();
	Calendar calendar = Calendar.find("byId", ownCalendarId).first();

	event.follow(calendar);
	event.save();
	calendar.save();

	String nickname = Security.connected();
	Date aDate = event.start;

	Calendars.showDate(nickname, originalCalendarId, aDate);
    }

    public static void unfollowEvent(Long calendarId, Long eventId) {
	Calendar calendar = Calendar.find("byId", calendarId).first();
	Event event = Event.find("byId", eventId).first();

	event.unfollow(calendar);
	event.save();
	calendar.save();

	Date aDate = event.start;
	String nickname = Security.connected();
	Calendars.showDate(nickname, calendarId, aDate);
    }
}
