package controllers;

import javax.persistence.Query;

import org.hibernate.exception.ConstraintViolationException;

import models.Calendar;
import models.User;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.db.jpa.JPA;

public class Security extends Secure.Security {
    public static boolean authenticate(String nickname, String password) {
    	return (User.connect(nickname, password) != null);
    }

    public static void registerUser(@Required String fullname,
	    @Required String nickname, @Required String mail,
	    @Required String password, @Required String confirmPassword) {
    	
	    validation.email(mail);
	    validation.equals(password, confirmPassword);
	    
	    if(!isUniqueNickname(nickname)){
	       	validation.addError("nickname",nickname+" is already taken");
	    }
	    
	    if (validation.hasErrors()){
			params.flash(); // !
			flash.keep();
			renderTemplate("Secure/registration.html");
		}
	    User user = new User(nickname, fullname, password, mail).save();
		Calendar calendar = new Calendar("defaultCalendar", user).save();
		user.calendars.add(calendar);
		user.save();
		Calendars.index();
	}

    public static void registration() {
    	renderTemplate("Secure/registration.html");
    }
    
    private static boolean isUniqueNickname(String nickname){
    	Query nicknameQuery = JPA.em()
    			.createQuery("SELECT u FROM User u WHERE u.nickname = :nickname")
    			.setParameter("nickname", nickname);
    	return (nicknameQuery.getResultList().size() == 0);
    }
}