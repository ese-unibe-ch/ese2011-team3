package controllers;

import models.User;
import old.Security;
import play.mvc.Before;
import play.mvc.Controller;

public class Main extends Controller {
	@Before
	protected static void setConnectedUser() {
		User login = User.find("byNickname", Security.connected()).first();
		renderArgs.put("loginUser", login);
	}
}
