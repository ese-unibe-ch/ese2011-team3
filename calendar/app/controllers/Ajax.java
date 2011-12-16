package controllers;

import java.util.Date;

import models.Calendar;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import play.data.validation.Required;
import utilities.OverlappingData;

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

	// check for overlapping events for this user
	OverlappingData overlapping = Calendar.getOverlappingData(getUser(),
		start, end);

	Gson gson = new GsonBuilder().setPrettyPrinting()
		.excludeFieldsWithoutExposeAnnotation().setDateFormat(format)
		.create();

	renderJSON(gson.toJson(overlapping));
    }
}
