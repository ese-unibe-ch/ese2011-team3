package utilities;

import java.util.List;

import models.Event;

import org.joda.time.DateTime;

/**
 * The JSONHelper class is used to create a JSON represntation of an event or a
 * list of events. It is located in the utilities folder of the calendar
 * application.
 * 
 * @author Alt-F4
 */
public abstract class JSONHelper {
    /*
     * This stuff should be done with gson (in play integrated) or flexJSON.
     */

    /**
     * Creates array with events in json notation.
     * 
     * @param events a list of events
     * @return string a json representation of a list of events
     */
    public static String eventsToJSON(List<Event> events) {
	StringBuilder sb = new StringBuilder();
	sb.append("[\n");
	int c = 1;
	for (Event ev : events) {
	    sb.append(EventToJSON(ev));
	    if (c != events.size()) {
		sb.append(",\n");
	    }
	    c++;
	}
	sb.append("\n]");
	return sb.toString();
    }

    /**
     * Creates an event in json notation.
     * 
     * @param event an event
     * @return string a json representation of this event
     */
    public static String EventToJSON(Event event) {
	assert event != null;
	StringBuilder sb = new StringBuilder();
	sb.append("{\n");
	sb.append("name : '" + event.name + "',\n");
	sb.append("start : '"
		+ new DateTime(event.start).toString("yyyy-MM-dd HH:mm")
		+ "',\n");
	sb.append("end : '"
		+ new DateTime(event.end).toString("yyyy-MM-dd HH:mm") + "'");
	sb.append("\n}");
	return sb.toString();
    }
}
