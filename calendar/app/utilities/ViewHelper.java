package utilities;

import java.util.Date;

import models.Event;

import org.joda.time.DateTime;

import play.templates.JavaExtensions;

public class ViewHelper extends JavaExtensions {

    public static String adaptDate(Event event, Date aDate) {
	/*
	 * this method replaces: ${event.start.format('HH:mm')} -
	 * ${event.end.format('HH:mm')}
	 */
	DateTime startDateTime = new DateTime(event.start);
	DateTime endDateTime = new DateTime(event.end);

	String format = "yyyy-MM-dd, HH:mm";

	if (event.repeatableType != RepeatableType.NONE) {
	    DateTime start = new DateTime(aDate).withTime(
		    startDateTime.getHourOfDay(),
		    startDateTime.getMinuteOfHour(), 0, 0);

	    DateTime end = new DateTime(aDate).withTime(
		    endDateTime.getHourOfDay(), endDateTime.getMinuteOfHour(),
		    0, 0);

	    return start.toString(format) + " - " + end.toString(format);
	}
	return new DateTime(event.start).toString(format) + " - "
		+ new DateTime(event.end).toString(format);
    }
}