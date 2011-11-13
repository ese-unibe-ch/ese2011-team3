package controllers;

import java.util.Date;
import java.util.List;

import models.Event;
import models.User;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import play.data.validation.Required;
import play.db.jpa.JPA;
import utilities.CalendarHelper;
import utilities.CalendarHelper.OverlappingObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Ajax extends Main {

    public static void isOverlappingEvent(@Required String startDate,
	    @Required String endDate) {
	String format = "yyyy-MM-dd-HH:mm";
	DateTimeFormatter formatter = DateTimeFormat.forPattern(format);
	// parse dates
	Date start = formatter.parseDateTime(startDate).toDate();

	Date end = formatter.parseDateTime(endDate).toDate();

	// get all events of this user
	User user = getUser();
	List<Event> events = JPA
		.em()
		.createQuery(
			"SELECT e FROM Event e JOIN e.calendars c WHERE c.owner.id = :uid")
		.setParameter("uid", user.id).getResultList();

	// check overlapping events
	OverlappingObject overlapping = CalendarHelper.overlaps(events, start,
		end);

	Gson gson = new GsonBuilder().setPrettyPrinting()
		.excludeFieldsWithoutExposeAnnotation()
		.setDateFormat("yyyy-MM-dd HH:mm").create();

	renderJSON(gson.toJson(overlapping));
    }

}
