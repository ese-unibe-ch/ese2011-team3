package controllers;

import java.util.Date;

import models.Calendar;
import models.Event;
import models.User;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import play.data.validation.Required;
import play.mvc.With;

@With(Secure.class)
public class Events extends Application {
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

		Event event = new Event(name, startDate, endDate, calendar, isPublic,
				note);

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

		String nickname = event.calendar.owner.nickname;
		Long calendarId = event.calendar.id;
		Calendars.showCalendar(nickname, calendarId);
	}

	public static void editEvent(Long eventId) {
		Event event = Event.findById(eventId);
		Calendar calendar = event.calendar;
		User user = calendar.owner;
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
		String nickname = event.calendar.owner.nickname;
		Long calendarId = event.calendar.id;
		Calendars.showCalendar(nickname, calendarId);
	}
}
