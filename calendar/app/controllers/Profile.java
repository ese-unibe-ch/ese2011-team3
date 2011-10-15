package controllers;

import models.User;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Profile extends Controller {

	public static void index() {
		User user = User.find("byNickname", Security.connected()).first();
		renderArgs.put("name", user.nickname);
		renderArgs.put("description", user.profile);
		render();
	}

	public static void edit() {
		User user = User.find("byNickname", Security.connected()).first();
		renderArgs.put("description", user.profile);
		render();
	}

	public static void save(String description) {
		User user = User.find("byNickname", Security.connected()).first();
		user.profile = description;
		user.save();
		index();
	}

}
