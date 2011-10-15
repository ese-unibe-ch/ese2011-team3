package controllers;

import java.util.Date;

import models.Calendar;
import models.Event;
import models.User;
import play.data.validation.Required;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Events extends Controller {

	public static void createEvent(Long calendarId, @Required String name,
			@Required Date start, @Required Date end, String note) {
		validation.required(name);
		validation.required(start);
		validation.required(end);

		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
			addEvent(calendarId);
		}

		Calendar calendar = Calendar.findById(calendarId);
		new Event(name, start, end, calendar, note).save();

		String nickname = calendar.owner.nickname;
		Calendars.showCalendar(nickname, calendarId);
	}

	public static void updateEvent(Long eventId, @Required String name,
			@Required Date start, @Required Date end, String note) {
		validation.required(name);
		validation.required(start);
		validation.required(end);

		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
			editEvent(eventId);
		}

		Event event = Event.findById(eventId);
		event.name = name;
		event.start = start;
		event.end = end;
		event.note = note;
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
