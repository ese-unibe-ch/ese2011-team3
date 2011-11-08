package controllers;

import models.Calendar;
import models.User;
import play.data.validation.Required;

public class Security extends Secure.Security {
	public static boolean authenticate(String nickname, String password) {
		return (User.connect(nickname, password) != null);
	}

	public static void registerUser(@Required String fullname,
			@Required String nickname, @Required String mail,
			@Required String password, @Required String confirmPassword) {
		User user = new User(nickname, fullname, password, mail).save();
		Calendar calendar = new Calendar("defaultCalendar", user).save();
		user.calendars.add(calendar);
		user.save();
		Calendars.index();

	}

	public static void registration() {
		renderTemplate("Secure/registration.html");

	}
}