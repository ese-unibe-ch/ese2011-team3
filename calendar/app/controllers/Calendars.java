package controllers;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import models.Calendar;
import models.Event;
import models.User;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Calendars extends Controller {

	public static void showCalendars(String nickname) {
		User user = User.find("byNickname", nickname).first();
		List calendars = user.calendars;
		render(user, calendars);
	}

	public static void showCalendar(String nickname, long id) {
		User user = User.find("byNickname", nickname).first();
		Calendar calendar = Calendar.findById(id);
		List<Event> events = calendar.events;

		Date aDate = new Date();
		Locale aLocale = new Locale("en", "CH");
		render(user, calendar, events, aDate, aLocale);
	}
}
