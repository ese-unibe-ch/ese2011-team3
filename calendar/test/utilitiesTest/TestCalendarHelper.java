package utilitiesTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import models.Calendar;
import models.Event;
import models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;
import utilities.CalendarHelper;

public class TestCalendarHelper extends UnitTest {
    private SimpleDateFormat formatter = new SimpleDateFormat(
	    "yyyy/MM/dd, HH:mm");

    @Before
    public void setUp() throws ParseException {
	Fixtures.deleteAllModels();

	User testUser = new User("wuschu", "WTF", "secret", "wuschu@alt-f4.com")
		.save();
	Calendar testCalendar = new Calendar("Home", testUser).save();

	new Event("default", "small note",
		formatter.parse("2011/10/14, 09:00"),
		formatter.parse("2011/10/14, 15:00"), testUser, testCalendar,
		false).save();

	new Event("ese", "small note", formatter.parse("2011/10/14, 08:00"),
		formatter.parse("2011/10/14, 10:00"), testUser, testCalendar,
		false).save();
    }

    @After
    public void tearDown() {
	Fixtures.deleteAllModels();
    }

    @Test
    public void testOverlapping() throws ParseException {
	List<Event> events = Event.findAll();
	Date start = formatter.parse("2011/10/14, 08:00");
	Date end = formatter.parse("2011/10/14, 13:00");

	assertTrue(CalendarHelper.overlaps(events, start, end).size() > 0);
    }

    /*
     * test all overlapping cases!
     */

    /*
     * case: start < event.start and end < event.end
     */
    @Test
    public void testOverlappingCase1() throws ParseException {
	Date start = formatter.parse("2011/10/14, 08:00");
	Date end = formatter.parse("2011/10/14, 13:00");
	Event event = Event.find("byName", "default").first();

	boolean overlapping = CalendarHelper.isOverlapping(event, start, end);
	assertTrue(overlapping);
    }

    /*
     * case: start > event.start and end > event.end
     */
    @Test
    public void testOverlappingCase2() throws ParseException {
	Date start = formatter.parse("2011/10/14, 10:00");
	Date end = formatter.parse("2011/10/14, 16:00");
	Event event = Event.find("byName", "default").first();

	boolean overlapping = CalendarHelper.isOverlapping(event, start, end);
	assertTrue(overlapping);
    }

    /*
     * case: start < event.start and end > event.end
     */
    @Test
    public void testOverlappingCase3() throws ParseException {
	Date start = formatter.parse("2011/10/14, 08:00");
	Date end = formatter.parse("2011/10/14, 16:00");
	Event event = Event.find("byName", "default").first();

	boolean overlapping = CalendarHelper.isOverlapping(event, start, end);
	assertTrue(overlapping);
    }

    /*
     * case: start = event.start and end = event.end
     */
    @Test
    public void testOverlappingCase4() throws ParseException {
	Date start = formatter.parse("2011/10/14, 09:00");
	Date end = formatter.parse("2011/10/14, 15:00");
	Event event = Event.find("byName", "default").first();

	boolean overlapping = CalendarHelper.isOverlapping(event, start, end);
	assertTrue(overlapping);
    }

    /*
     * case: start = event.start and end > event.end
     */
    @Test
    public void testOverlappingCase5() throws ParseException {
	Date start = formatter.parse("2011/10/14, 09:00");
	Date end = formatter.parse("2011/10/14, 15:30");
	Event event = Event.find("byName", "default").first();

	boolean overlapping = CalendarHelper.isOverlapping(event, start, end);
	assertTrue(overlapping);
    }

    /*
     * case: start > event.start and end = event.end
     */
    @Test
    public void testOverlappingCase6() throws ParseException {
	Date start = formatter.parse("2011/10/14, 09:30");
	Date end = formatter.parse("2011/10/14, 15:00");
	Event event = Event.find("byName", "default").first();

	boolean overlapping = CalendarHelper.isOverlapping(event, start, end);
	assertTrue(overlapping);
    }

    /*
     * case: start > event.start and end < event.end
     */
    @Test
    public void testOverlappingCase7() throws ParseException {
	Date start = formatter.parse("2011/10/14, 10:00");
	Date end = formatter.parse("2011/10/14, 13:00");
	Event event = Event.find("byName", "default").first();

	boolean overlapping = CalendarHelper.isOverlapping(event, start, end);
	assertTrue(overlapping);
    }

    /*
     * test three non-overlapping cases!
     */
    @Test
    public void testNonOverlappingCase1() throws ParseException {
	Date start = formatter.parse("2011/10/14, 08:00");
	Date end = formatter.parse("2011/10/14, 08:30");
	Event event = Event.find("byName", "default").first();

	boolean overlapping = CalendarHelper.isOverlapping(event, start, end);
	assertFalse(overlapping);
    }

    @Test
    public void testNonOverlappingCase2() throws ParseException {
	Date start = formatter.parse("2011/10/14, 15:30");
	Date end = formatter.parse("2011/10/14, 16:00");
	Event event = Event.find("byName", "default").first();

	boolean overlapping = CalendarHelper.isOverlapping(event, start, end);
	assertFalse(overlapping);
    }

    @Test
    public void testNonOverlappingCase3() throws ParseException {
	Date start = formatter.parse("2011/10/15, 09:00");
	Date end = formatter.parse("2011/10/15, 15:00");
	Event event = Event.find("byName", "default").first();

	boolean overlapping = CalendarHelper.isOverlapping(event, start, end);
	assertFalse(overlapping);
    }

}
