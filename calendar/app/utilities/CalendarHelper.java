package utilities;

import java.util.Date;
import java.util.List;

import models.Event;

import org.joda.time.DateTime;

/**
 * The CalendarHelper class is used to check if an event or a list of events is
 * overlapping a specific time interval which is given by a start and an end
 * date. It is located in the utilities folder of the calendar application.
 * 
 * @author Alt-F4
 */
public class CalendarHelper {

    /**
     * 
     */
    public static boolean isOverlapping(Event event, Date startDate,
	    Date endDate) {
	DateTime evStart = new DateTime(event.start);
	DateTime evEnd = new DateTime(event.end);

	DateTime start = new DateTime(startDate);
	DateTime end = new DateTime(endDate);

	return ((start.isBefore(evStart) || start.equals(evStart)) && (end
		.isAfter(evEnd) || end.equals(evEnd)))
		|| ((start.isBefore(evStart) || start.equals(evStart)) && end
			.isAfter(evStart))
		|| (start.isBefore(evEnd) && (end.isAfter(evEnd) || end
			.equals(evEnd)))
		|| (evStart.isBefore(start) && evEnd.isAfter(end));
    }

    public static boolean overlaps(List<Event> events, Date start, Date end) {
	for (Event ev : events) {
	    if (isOverlapping(ev, start, end)) {
		return true;
	    }
	}
	return false;
    }

}
