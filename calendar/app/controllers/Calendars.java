package controllers;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import models.Calendar;
import models.User;

import org.joda.time.DateTime;

import play.db.jpa.JPA;
import play.mvc.With;

@With(Secure.class)
public class Calendars extends Main {
	public static void index() {
		Query calendarQuery = JPA.em()
				.createQuery("SELECT c FROM Calendar c WHERE c.owner.id = :id")
				.setParameter("id", getUser().id);
		List<Calendar> calendarList = calendarQuery.getResultList();
		// TODO: estimate what to do if an user don't got any calendars.
		// @poljpocket
		assert calendarList.size() != 0;
		viewCalendar(calendarList.get(0).getId(), null);
	}

	public static void viewCalendar(Long calendarId, Date currentDate) {
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

	public static void editCalendar() {

	}

	public static void addEvent(Long calendarId, Date currentDate) {

	}

	public static void editEvent(Long eventId) {
		render();
	}

	public static void addCalendar(String calendarName) {
		User user = User.find("byNickname", Security.connected()).first();
		Calendar calendar = new Calendar(calendarName, user).save();
		user.addCalendar(calendar);

		viewCalendar(calendar.id, null);
	}

	public static void saveEvent() {

	}
}
