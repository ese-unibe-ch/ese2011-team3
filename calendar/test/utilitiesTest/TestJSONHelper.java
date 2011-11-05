package utilitiesTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import models.Calendar;
import models.Event;
import models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;
import utilities.JSONHelper;

public class TestJSONHelper extends UnitTest {

    @Before
    public void setUp() throws Exception {
	Fixtures.deleteAllModels();

	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd, HH:mm");

	User testUser = new User("wuschu", "WTF", "secret", "wuschu@alt-f4.com")
		.save();
	Calendar testCalendar = new Calendar("Home", testUser).save();

	new Event("default", "small note",
		formatter.parse("2011/10/14, 09:00"),
		formatter.parse("2011/10/14, 15:00"), testUser, testCalendar,
		false).save();
    }

    @After
    public void tearDown() throws Exception {
	Fixtures.deleteAllModels();
    }

    @Test
    public void testRequest() throws ParseException {
	User user = User.find("byNickname", "wuschu").first();
	List<Event> events = Event.find("byOwner", user).fetch();
	Event event = Event.find("byName", "default").first();

	String eventAsJSON = JSONHelper.EventToJSON(event);
	String expectedJSONEvent = "{\n" + "name : 'default',\n"
		+ "start : '2011-10-14 09:00',\n" + "end : '2011-10-14 15:00'"
		+ "\n}";

	assertEquals(expectedJSONEvent, eventAsJSON);

	String eventsAsJSON = JSONHelper.eventsToJSON(events);
	String expectedJSONEvents = "[\n" + expectedJSONEvent + "\n]";

	assertEquals(expectedJSONEvents, eventsAsJSON);
    }
}
