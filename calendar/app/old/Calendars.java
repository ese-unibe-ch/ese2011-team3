package old;

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

import controllers.Secure;

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

		List<Event> list = new ArrayList<Event>();

		if (user.equals(calendar.owner)) {
			for (Event ev : events) {
				if (!ev.isPublic)
					list.add(ev);
			}
		}
		renderArgs.put("events", list);

		Locale aLocale = new Locale("en", "CH");
		render(user, calendar, aDate, aLocale);
	}

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

		Locale aLocale = new Locale("en", "CH");
		render("Calendars/showCalendar.html", user, calendar, events, aDate,
				aLocale);
	}
}
