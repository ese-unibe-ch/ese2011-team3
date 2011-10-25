package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import models.Calendar;
import models.Event;
import models.User;
import play.mvc.With;

@With(Secure.class)
public class Calendars extends Application {

    /**
     * shows a list of calendars
     * 
     * @param nickname
     */
    public static void showCalendars(String nickname) {
	User user = User.find("byNickname", nickname).first();

	List<Calendar> calendars = Calendar.find("byOwner", user).fetch();
	render(user, calendars);
    }

    /**
     * shows a calendar at the current date
     * 
     * @param nickname
     * @param id the calendar id
     */
    public static void showCalendar(String nickname, Long calendarId) {
	Date aDate = new Date();
	showDate(nickname, calendarId, aDate);
    }

    /**
     * shows a specific date in the calendar
     * 
     * @param nickname
     * @param id the calendar id
     * @param aDate a specific date
     */
    public static void showDate(String nickname, Long calendarId, Date aDate) {
	User user = User.find("byNickname", Security.connected()).first();
	Calendar calendar = Calendar.findById(calendarId);

	// get all events on this day aDate
	List<Event> events = calendar.getAllEventsOnDay(user, aDate);

	List<Event> list = new ArrayList();

	// check if logged in user is not equals to calendar owner
	if (!user.equals(calendar.owner)) {
	    // if so, then copy all public events in a list
	    for (Event ev : events) {
		if (ev.isPublic) {
		    list.add(ev);
		}
	    }
	} else { // otherwise logged in user is equals to calendar owner
	    list = events;
	}

	renderArgs.put("events", list);

	Locale aLocale = new Locale("en", "CH");
	render("Calendars/showCalendar.html", user, calendar, aDate, aLocale);
    }
    
    public static void newCalendar(){
    	render();
    }
    public static void addCalendar(String calendarName){
    	User user = User.find("byNickname",Security.connected()).first();
    	Calendar calendar = new Calendar(calendarName, user).save();
    	user.addCalendar(calendar);
    	
    	showCalendar(user.nickname, calendar.id);
    }

    
}
