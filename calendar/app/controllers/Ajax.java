package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Calendar;
import models.Event;
import models.User;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import play.data.validation.Required;
import utilities.OverlappingData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Ajax extends Main {

    private static final GsonBuilder gsonBuilder = new GsonBuilder();

    public static void isOverlappingEvent(@Required String startDate,
	    @Required String endDate) {
	String format = "yyyy-MM-dd-HH:mm";
	DateTimeFormatter formatter = DateTimeFormat.forPattern(format);

	// parse dates
	Date start = formatter.parseDateTime(startDate).toDate();
	Date end = formatter.parseDateTime(endDate).toDate();

	// check for overlapping events for this user
	OverlappingData overlapping = Calendar.getOverlappingData(getUser(),
		start, end);

	Gson gson = gsonBuilder.setPrettyPrinting()
		.excludeFieldsWithoutExposeAnnotation().setDateFormat(format)
		.create();

	renderJSON(gson.toJson(overlapping));
    }

    public static void searchUser(String term) {
	List<User> users = User.searchUserByNickname(term, getUser());
	ArrayList<String> suggestions = new ArrayList<String>();
	for (User user : users) {
	    suggestions.add(user.nickname);
	}
	renderJSON(suggestions);
    }

    public static void searchEvent(Long id, String term) {

	List<Event> events = Events.searchEventByName(term, id, getUser());

	ArrayList<String> suggestions = new ArrayList<String>();
	for (Event event : events) {
	    suggestions.add(event.name);
	}

	renderJSON(suggestions);
    }
}
