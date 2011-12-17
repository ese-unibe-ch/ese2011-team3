package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import models.Calendar;
import models.Event;
import models.User;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import play.data.validation.Required;
import play.db.jpa.JPA;
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
	Query q = JPA
		.em()
		.createQuery(
			"SELECT u FROM User u WHERE u.nickname LIKE ?1 ORDER BY u.nickname ASC")
		.setParameter(1, term + "%");
	List<User> users = q.getResultList();
	users.remove(getUser());

	ArrayList<String> suggestions = new ArrayList<String>();
	for (User user : users) {
	    suggestions.add(user.nickname);
	}

	Gson gson = gsonBuilder.setPrettyPrinting().create();

	renderJSON(gson.toJson(suggestions));

    }

    public static void searchEvent(Long id, String term) {

	Query eventsQuery = JPA
		.em()
		.createQuery(
			"SELECT e FROM Event e JOIN e.calendars c WHERE c.id = ?1 AND (e.isPublic = true OR c.owner.id = ?2 ) AND upper(e.name) LIKE ?3")
		.setParameter(1, id).setParameter(2, getUser().id)
		.setParameter(3, term.toUpperCase() + "%");
	List<Event> events = eventsQuery.getResultList();

	ArrayList<String> suggestions = new ArrayList<String>();
	for (Event event : events) {
	    suggestions.add(event.name);
	}

	Gson gson = gsonBuilder.setPrettyPrinting().create();

	renderJSON(gson.toJson(suggestions));
    }
}
