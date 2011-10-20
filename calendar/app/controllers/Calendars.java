package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.Query;
import javax.persistence.TemporalType;

import models.Calendar;
import models.Event;
import models.User;

import org.joda.time.DateTime;

import play.db.jpa.JPA;
import play.mvc.With;

@With(Secure.class)
public class Calendars extends Application {

	/**
	 * shows a list of calendars
	 * 
	 * @param nickname
	 */
	public static void showCalendars(String nickname) {
		User user = User.find("byNickname", nickname).first();
		List<Calendar> calendars = user.calendars;
		render(user, calendars);
	}

	/**
	 * shows a calendar at the current date
	 * 
	 * @param nickname
	 * @param id the calendar id
	 */
	public static void showCalendar(String nickname, Long id) {
		User user = User.find("byNickname", Security.connected()).first();
		Calendar calendar = Calendar.findById(id);

		Date aDate = new Date();
		Date start = new DateTime().withTime(0, 0, 0, 0).toDate();
		Date end = new DateTime().withTime(23, 59, 59, 999).toDate();

		Query query = JPA
				.em()
				.createQuery(
						"from Event where lowerBound <= :start and upperBound >= :end and calendar_id=:cid order by start")
				.setParameter("start", start, TemporalType.DATE)
				.setParameter("end", end, TemporalType.DATE)
				.setParameter("cid", calendar.id);

		List<Event> events = query.getResultList();
		ArrayList<Event> list = new ArrayList(events);

		if (!user.equals(calendar.owner)) {
			for (Event ev : list) {
				if (!ev.isPublic) {
					events.remove(ev);
				}
			}
		}

		renderArgs.put("events", events);

		Locale aLocale = new Locale("en", "CH");
		render(user, calendar, aDate, aLocale);
	}

	/**
	 * shows a specific date in the calendar
	 * 
	 * @param nickname
	 * @param id the calendar id
	 * @param aDate a specific date
	 */
	public static void showDate(String nickname, Long id, Date aDate) {
		User user = User.find("byNickname", Security.connected()).first();
		Calendar calendar = Calendar.findById(id);

		Date start = new DateTime(aDate).withTime(0, 0, 0, 0).toDate();
		Date end = new DateTime(aDate).withTime(23, 59, 59, 999).toDate();

		Query query = JPA
				.em()
				.createQuery(
						"from Event where lowerBound <= :start and upperBound >= :end and calendar_id=:cid order by start")
				.setParameter("start", start, TemporalType.DATE)
				.setParameter("end", end, TemporalType.DATE)
				.setParameter("cid", calendar.id);

		List<Event> events = query.getResultList();
		ArrayList<Event> list = new ArrayList(events);

		if (!user.equals(calendar.owner)) {
			for (Event ev : list) {
				if (!ev.isPublic) {
					events.remove(ev);
				}
			}
		}

		renderArgs.put("events", events);

		Locale aLocale = new Locale("en", "CH");
		render("Calendars/showCalendar.html", user, calendar, aDate, aLocale);
	}
}
