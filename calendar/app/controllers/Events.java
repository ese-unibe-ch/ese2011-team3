package controllers;

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
	    @Required boolean isPublic, String note) {

	validation.required(name);
	validation.required(startDate);
	validation.required(endDate);
	validation.required(isPublic);
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
	Event event = new Event(name, note, startDate, endDate, owner,
		calendar, isPublic, false);

	event.save();

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

    public static void updateEvent(Long eventId, @Required String name,
	    @Required Date startDate, @Required Date endDate,
	    @Required String startTime, @Required String endTime,
	    @Required boolean isPublic, String note) {

	validation.required(name);
	validation.required(startDate);
	validation.required(endDate);
	validation.match(startTime, regexTime).message("Invalid!");
	validation.match(endTime, regexTime).message("Invalid!");
	validation.required(isPublic);

	if (validation.hasErrors()) {
	    params.flash();
	    validation.keep();
	    editEvent(eventId);
	}

	Event event = Event.findById(eventId);
	event.name = name;
	event.start = helperCreateDate(startDate, startTime, "HH:mm");
	event.end = helperCreateDate(endDate, endTime, "HH:mm");
	event.lowerBound = Event.makeLowerBound(startDate);
	event.upperBound = Event.makeUpperBound(endDate);
	event.note = note;
	event.isPublic = isPublic;

	event.save();

	String nickname = event.owner.nickname;
	// TODO:
	Long calendarId = event.calendars.get(0).id;
	Calendars.showCalendar(nickname, calendarId);
    }

    public static void editEvent(Long eventId) {
	Event event = Event.findById(eventId);
	// TODO:
	Calendar calendar = event.calendars.get(0);
	User user = event.owner;
	render(calendar, event, user);
    }

    public static void addEvent(Long calendarId) {
	Calendar calendar = Calendar.findById(calendarId);
	User user = calendar.owner;
	render(calendar, user);
    }

    public static void removeEvent(Long eventId) {
	Event event = Event.findById(eventId);
	event.delete();
	String nickname = event.owner.nickname;
	Long calendarId = event.calendars.get(0).id;
	Calendars.showCalendar(nickname, calendarId);
    }

    // TODO: implement this controller
    public static void selectCalendarToFollowEvent(Long eventId, Long originalCalendarId) {
    	User connectedUser = User.find("byNickname", Security.connected()).first();
    	List<Calendar> calendars =connectedUser.calendars;
    	Calendar originalCalendar = Calendar.find("byId", originalCalendarId).first();
    	Event event = Event.find("byID", eventId).first();
    	render(calendars, originalCalendar, event);
    }
    public static void followEvent(Long ownCalendarId, Long originalCalendarId, Long eventId){
    	String nickname = Security.connected();
    	Event event = Event.find("byId", eventId).first();
    	Calendar calendar = Calendar.find("byId", ownCalendarId).first();
    	event.follow(calendar);
    	event.save();
    	Calendars.showCalendar(nickname, originalCalendarId);
    }
}
