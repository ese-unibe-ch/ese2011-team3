package controllers;

import models.User;

public class Profile extends Main {
	public static void index() {
		viewProfile(getUser().id);
	}

	public static void viewProfile(Long userId) {
		renderArgs.put("user", User.findById(userId));

		render();
	}
}
