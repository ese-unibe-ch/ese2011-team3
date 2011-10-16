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
			@Required Date startDate, @Required Date endDate,
			@Required String startTime, @Required String endTime, String note) {

		validation.required(name);
		validation.required(startDate);
		validation.required(endDate);
		validation.required(startTime);
		validation.equals(endTime);
		validation.match("d{2}:d{2}", startTime);

		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
			Date aDate = startDate;
			addEvent(calendarId);
		}

		helperMakeDates(startDate, endDate, startTime, endTime);

		Calendar calendar = Calendar.findById(calendarId);
		new Event(name, startDate, endDate, calendar, note).save();

		String nickname = calendar.owner.nickname;
		Calendars.showCalendar(nickname, calendarId);
	}

	public static void updateEvent(Long eventId, @Required String name,
			@Required Date startDate, @Required Date endDate,
			@Required String startTime, @Required String endTime, String note) {

		validation.required(name);
		validation.required(startDate);
		validation.required(endDate);
		validation.required(startTime);
		validation.equals(endTime);

		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
			editEvent(eventId);
		}

		helperMakeDates(startDate, endDate, startTime, endTime);

		Event event = Event.findById(eventId);
		event.name = name;
		event.start = startDate;
		event.lowerBound = Event.makeLowerBound(startDate);
		event.end = endDate;
		event.upperBound = Event.makeUpperBound(endDate);
		event.note = note;
		event.save();

		editEvent(eventId);
		String nickname = event.calendar.owner.nickname;
		Long calendarId = event.calendar.id;
		Calendars.showCalendar(nickname, calendarId);
	}

	private static void helperMakeDates(Date startDate, Date endDate,
			String startTime, String endTime) {
		String[] startTimeA = startTime.split(":");
		String[] endTimeA = endTime.split(":");

		int hoursS = Integer.parseInt(startTimeA[0]);
		int minutesS = Integer.parseInt(startTimeA[1]);
		startDate.setHours(hoursS);
		startDate.setMinutes(minutesS);

		int hoursE = Integer.parseInt(endTimeA[0]);
		int minutesE = Integer.parseInt(endTimeA[1]);
		endDate.setHours(hoursE);
		endDate.setMinutes(minutesE);
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
