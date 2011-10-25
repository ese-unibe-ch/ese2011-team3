package controllers;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.Query;

import models.Calendar;
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
		renderArgs.put("currentDate", currentDate);
		renderArgs.put("currentLocale", new Locale("en", "CH"));

		render();
	}

	public static void editCalendar() {

	}

	public static void addEvent(Long calendarId, Date currentDate) {

	}

	public static void editEvent() {

	}

	public static void saveEvent() {

	}
}
