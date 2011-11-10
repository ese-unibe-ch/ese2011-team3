package controllers;

import javax.persistence.Query;

import models.User;
import play.cache.Cache;
import play.db.jpa.JPA;
import play.libs.Images;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Main extends Controller {
	@Before
	protected static void setConnectedUser() {
		renderArgs.put("loginUser", getUser());
	}

	protected static User getUser() {
		Query userQuery = JPA.em()
				.createQuery("SELECT u FROM User u WHERE u.nickname = :name")
				.setParameter("name", Security.connected());
		User user = (User) userQuery.getSingleResult();
		return user;
	}

	public static void captcha(String id) {
		Images.Captcha captcha = Images.captcha();
		String code = captcha.getText("#FF0080");
		Cache.set(id, code, "10mn");
		renderBinary(captcha);
	}
}
