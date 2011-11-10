package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;

import play.data.validation.Required;
import models.User;

public class Profile extends Main {
	public static void index() {
		viewProfile(getUser().id);
	}

	public static void viewProfile(Long userId) {
		renderArgs.put("user", User.findById(userId));

		render();
	}
	
	public static void editProfile(Long userId) {
		User user = User.findById(userId);
		renderArgs.put("user", user);
		
		if(user.birthday==null){
			flash.put("birthday", "yyyy-MM-dd");
		}
		else{
			flash.put("birthday", new DateTime(user.birthday).toString("yyyy-MM-dd"));
		}
		flash.put("id", user.id);
		flash.put("name", user.fullname);
		flash.put("email", user.mail);
		flash.put("telNumber", user.telNumber);
		flash.put("officeNumber", user.officeNumber);
		flash.put("description", user.profile);
		flash.put("officeTimes", user.officeTimes);
		flash.put("monday", user.getOfficeTime(0));
		flash.put("tuesday", user.getOfficeTime(1));
		flash.put("wednesday", user.getOfficeTime(2));
		flash.put("thursday", user.getOfficeTime(3));
		flash.put("friday", user.getOfficeTime(4));
		flash.put("saturday", user.getOfficeTime(5));
		flash.put("sunday", user.getOfficeTime(6));

		renderTemplate("Profile/editProfile.html");
	}	
	
	public static void saveProfile(Long userId, @Required String name, 
			@Required String email, String birthday, String telNumber,
			String description, String officeNumber, String monday, String tuesday,
			String wednesday, String thursday, String friday, String saturday, String sunday){
		
	    validation.email(email).message("Invalid e-Mail address");
		
		if (validation.hasErrors()) {
		    flash.keep();
		    User user = User.findById(userId);
			renderArgs.put("user", user);
		    renderTemplate("Profile/editProfile.html");
		}
		
		User user = User.findById(userId);
		user.fullname = name;
		user.mail = email;
		
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			user.birthday = fmt.parse(birthday);
			} catch (ParseException e) {
			user.birthday = null;
			}
		user.telNumber = telNumber;
		user.profile = description;
		user.officeNumber = officeNumber;
		user.setOfficeTime(0,monday);
		user.setOfficeTime(1,tuesday);
		user.setOfficeTime(2,wednesday);
		user.setOfficeTime(3,thursday);
		user.setOfficeTime(4,friday);
		user.setOfficeTime(5,saturday);
		user.setOfficeTime(6,sunday);
		user.save();
		
		viewProfile(userId);
	}
}
