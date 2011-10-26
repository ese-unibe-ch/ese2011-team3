package controllers;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import models.Calendar;
import models.Event;
import models.User;

import org.joda.time.DateTime;

import play.db.jpa.JPA;
import play.mvc.Before;
import play.mvc.With;

@With(Secure.class)
public class Calendars extends Main {
	@Before(unless = {
		"index"
	})
	private static void putCalendarData(Long calendarId, Date currentDate) {
		Query calendarQuery = JPA.em()
				.createQuery("SELECT c FROM Calendar c WHERE c.id = :id")
				.setParameter("id", calendarId);
		Calendar calendar = (Calendar) calendarQuery.getSingleResult();
		if (currentDate == null) {
			currentDate = new Date();
		}

		renderArgs.put("calendar", calendar);
		renderArgs.put("calendarData", calendar.getCalendarData(currentDate));
		renderArgs.put("currentDate", currentDate);
	}

	public static void index() {
		Query calendarQuery = JPA.em()
				.createQuery("SELECT c FROM Calendar c WHERE c.owner.id = :id")
				.setParameter("id", getUser().id);
		List<Calendar> calendarList = calendarQuery.getResultList();
		// TODO: estimate what to do if an user don't got any calendars.
		assert calendarList.size() != 0;
		viewCalendar(calendarList.get(0).getId(), new Date());
	}

	public static void viewCalendar(Long calendarId, Date currentDate) {
		render();
	}

	public static void viewNextMonth(Long calendarId, Date currentDate) {
		viewCalendar(calendarId, new DateTime(currentDate).plusMonths(1)
				.toDate());
	}

	public static void viewPrevMonth(Long calendarId, Date currentDate) {
		viewCalendar(calendarId, new DateTime(currentDate).minusMonths(1)
				.toDate());
	}

	public static void editCalendar() {}

	public static void addEvent(Long calendarId, Date currentDate) {
		renderArgs.put("actionName", "Add Event");

		renderTemplate("Calendars/viewEvent.html");
	}

	public static void editEvent(Long calendarId, Date currentDate, Long eventId) {
		renderArgs.put("actionName", "Edit Event");

		Query eventQuery = JPA.em()
				.createQuery("SELECT e FROM Event e WHERE e.id = :id")
				.setParameter("id", eventId);
		Event event = (Event) eventQuery.getSingleResult();

		Calendars.flash.put("name", event.name);
		Calendars.flash.put("startDate", event.start);
		Calendars.flash.put("endDate", event.end);
		Calendars.flash.put("isPublic", event.isPublic);
		Calendars.flash.put("note", event.note);

		renderTemplate("Calendars/viewEvent.html");
	}

	public static void deleteEvent(Long calendarId, Date currentDate,
			Long eventId) {
		viewCalendar(calendarId, currentDate);
	}

	public static void addCalendar(String calendarName) {
		User loginUser = getUser();
		Calendar calendar = new Calendar(calendarName, loginUser).save();
		loginUser.addCalendar(calendar);

		viewCalendar(calendar.id, null);
	}
}
