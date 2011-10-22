package modelTest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import models.Calendar;
import models.Event;
import models.User;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class EventTest extends UnitTest {

    @Before
    public void setUp() throws Exception {
	Fixtures.deleteAllModels();

	User testUser = new User("wuschu", "WTF", "secret", "wuschu@alt-f4.com")
		.save();

	User stranger = new User("joe", "WTF", "secret", "joe@alt-f4.com")
		.save();

	Calendar testCalendar = new Calendar("Home", testUser).save();
	Calendar strangerCalendar = new Calendar("Home", stranger).save();

	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd, HH:mm");
	Date start = formatter.parse("2011/10/14, 09:00");
	Date end = formatter.parse("2011/10/14, 15:00");

	new Event("ESE sucks", "small note", start, end, testUser,
		testCalendar, false, false).save();

	new Event("public event", "small note",
		formatter.parse("2011/10/14, 15:00"),
		formatter.parse("2011/10/14, 15:00"), testUser, testCalendar,
		true, true).save();

	new Event("strangers event", "small note",
		formatter.parse("2011/10/14, 15:00"),
		formatter.parse("2011/10/14, 15:00"), stranger,
		strangerCalendar, true, true).save();
    }

    @After
    public void tearDown() throws Exception {
	Fixtures.deleteAllModels();
    }

    @Test
    public void testCountEntities() {
	assertEquals(3, Event.count());
	assertEquals(2, Calendar.count());
	assertEquals(2, User.count());
    }

    @Test
    public void testEvent() {
	User user = User.find("byNickname", "wuschu").first();

	List<Event> testEvents = Event.find("byOwner", user).fetch();
	Assert.assertEquals(testEvents.size(), 2);
    }

    @Test
    public void testGetEventsFromAUser() {
	User user = User.find("byNickname", "joe").first();
	List<Event> events = Event.find("byOwner", user).fetch();

	Event ev = events.get(0);
	assertEquals(1, events.size());
	assertEquals("small note", ev.note);
    }

    @Test
    public void testGetFollowableEvents() {
	List<Event> followableEvents = Event.find("byIsFollowable", true)
		.fetch();
	assertEquals(followableEvents.size(), 2);
    }

    @Test
    public void testFollowEvent() {
	// get a stranger
	User stranger = User.find("byNickname", "joe").first();

	// get a calendar from the stranger
	Calendar strangersCalendar = Calendar.find("byOwnerAndName", stranger,
		"Home").first();

	// get an event from another user
	User user = User.find("byNickname", "wuschu").first();

	Event followEvent = Event.find("byNameAndOwner", "public event", user)
		.first();

	// add this event from another user to the calendar of the stranger
	followEvent.calendars.add(strangersCalendar);
	followEvent.save();
	/*
	 * strangersCalendar.events.add(followEvent); strangersCalendar.save();
	 */

	assertEquals(2, strangersCalendar.events.size());
    }

    @Test
    public void testGetFollowedEvents() {
	// get a stranger
	User stranger = User.find("byNickname", "joe").first();

	// get a calendar from the stranger
	Calendar strangersCalendar = Calendar.find("byOwnerAndName", stranger,
		"Home").first();

	// get an event from another user
	User user = User.find("byNickname", "wuschu").first();

	Event followEvent = Event.find("byOwnerAndName", user, "public event")
		.first();

	// add this event from another user to the calendar of the stranger
	assertNotNull(followEvent);
	assertNotNull(followEvent.calendars);
	followEvent.calendars.add(strangersCalendar);
	followEvent.save();

	assertEquals(strangersCalendar.events.size(), 2);

    }
}
