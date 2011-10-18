package controllers;

import java.util.Date;

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

	public static void createEvent(Long calendarId, @Required String name,
			@Required Date startDate, @Required Date endDate,
			@Required String startTime, @Required String endTime,
			@Required boolean isPublic, String note) {
		System.out.println(isPublic);
		validation.required(name);
		validation.required(startDate);
		validation.required(endDate);
		validation.required(startTime);
		validation.required(endTime);
		validation.required(isPublic);

		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
			addEvent(calendarId);
		}

		Calendar calendar = Calendar.findById(calendarId);

		Event event = helperBuildEvent(new Event(name, startDate, endDate,
				calendar, isPublic, note), startTime, endTime);
		event.save();

		String nickname = calendar.owner.nickname;
		Calendars.showCalendar(nickname, calendarId);
	}

	private static Event helperBuildEvent(Event event, String startTime,
			String endTime) {

		DateTimeFormatter parser = DateTimeFormat.forPattern("HH:mm");
		int startHours = parser.parseDateTime(startTime).getHourOfDay();
		int startMinutes = parser.parseDateTime(startTime).getMinuteOfHour();

		int endHours = parser.parseDateTime(endTime).getHourOfDay();
		int endMinutes = parser.parseDateTime(endTime).getMinuteOfHour();

		Date startDate = new DateTime(event.start).withTime(startHours,
				startMinutes, 0, 0).toDate();
		Date endDate = new DateTime(event.end).withTime(endHours, endMinutes,
				0, 0).toDate();

		event.start = startDate;
		event.end = endDate;

		return event;
	}

	public static void updateEvent(Long eventId, @Required String name,
			@Required Date startDate, @Required Date endDate,
			@Required String startTime, @Required String endTime,
			@Required boolean isPublic, String note) {

		validation.required(name);
		validation.required(startDate);
		validation.required(endDate);
		validation.required(startTime);
		validation.required(endTime);
		validation.required(isPublic);

		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
			editEvent(eventId);
		}

		Event event = Event.findById(eventId);
		event.name = name;
		event.lowerBound = Event.makeLowerBound(startDate);
		event.upperBound = Event.makeUpperBound(endDate);
		event.note = note;

		helperBuildEvent(event, startTime, endTime);
		event.save();

		editEvent(eventId);
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
