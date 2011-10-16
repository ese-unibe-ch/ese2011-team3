package controllers;

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

	public static void showCalendars(String nickname) {
		User user = User.find("byNickname", nickname).first();
		List<Calendar> calendars = user.calendars;
		render(user, calendars);
	}

	public static void showCalendar(String nickname, Long id) {
		User user = User.find("byNickname", nickname).first();
		Calendar calendar = Calendar.findById(id);

		Date aDate = new Date();
		Date start = new DateTime().withHourOfDay(0).withMinuteOfHour(0)
				.withSecondOfMinute(0).withMillisOfSecond(0).toDate();
		Date end = new DateTime().withHourOfDay(23).withMinuteOfHour(59)
				.withSecondOfMinute(59).toDate();

		Query query = JPA
				.em()
				.createQuery(
						"from Event where lowerBound <= :start and upperBound >= :end and calendar_id=:cid order by start")
				.setParameter("start", start, TemporalType.DATE)
				.setParameter("end", end, TemporalType.DATE)
				.setParameter("cid", calendar.id);

		List<Event> events = query.getResultList();

		Locale aLocale = new Locale("en", "CH");
		render(user, calendar, events, aDate, aLocale);
	}

	public static void showDate(String nickname, Long id, Date aDate) {
		User user = User.find("byNickname", nickname).first();
		Calendar calendar = Calendar.findById(id);

		Date start = new DateTime(aDate).withHourOfDay(0).withMinuteOfHour(0)
				.withSecondOfMinute(0).withMillisOfSecond(0).toDate();
		Date end = new DateTime(aDate).withHourOfDay(23).withMinuteOfHour(59)
				.withSecondOfMinute(59).toDate();

		Query query = JPA
				.em()
				.createQuery(
						"from Event where lowerBound <= :start and upperBound >= :end and calendar_id=:cid order by start")
				.setParameter("start", start, TemporalType.DATE)
				.setParameter("end", end, TemporalType.DATE)
				.setParameter("cid", calendar.id);

		List<Event> events = query.getResultList();

		Locale aLocale = new Locale("en", "CH");
		render("Calendars/showCalendar.html", user, calendar, events, aDate,
				aLocale);
	}
}
