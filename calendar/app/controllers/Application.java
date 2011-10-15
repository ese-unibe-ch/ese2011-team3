package controllers;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import models.Calendar;
import models.Event;
import models.User;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Application extends Controller {

	/**
	 * this method creates a user object of the currently logged in user and
	 * makes it accessible for all views and tag views.
	 */
	@Before
	static void setConnectedUser() {
		if (Security.isConnected()) {
			User account = User.find("byNickname", Security.connected())
					.first();
			renderArgs.put("account", account);
		}
	}

	public static void index() {
		Calendars.showCalendars(Security.connected());
	}

	public static void showOtherUsers(String userNickname) {
		User user = User.find("byNickname", userNickname).first();
		List users = User.findAll();
		for (int i = 0; i < users.size(); i++)
			if (user.equals(users.get(i)))
				users.remove(i);
		Collections.sort(users);
		render(users);
	}
}