package old;

import java.util.Collections;
import java.util.List;

import controllers.Secure;

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
		User login = User.find("byNickname", Security.connected()).first();
		Application.renderArgs.put("loginUser", login);
	}

	public static void index() {
		User login = User.find("byNickname", Security.connected()).first();
		Calendars.showCalendar(Security.connected(), login.calendars.get(0)
				.getId());
	}

	public static void showOtherUsers(String userNickname) {
		User user = User.find("byNickname", userNickname).first();
		List users = User.findAll();
		users.remove(user);
		Collections.sort(users);
		render(users);
	}

	public static void showContacts(String userNickname) {
		User user = User.find("byNickname", userNickname).first();
		List users = user.following;
		users.remove(user);
		Collections.sort(users);
		render(users);
	}

}