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

	User follower = new User("joe", "WTF", "secret", "joe@alt-f4.com")
		.save();

	Calendar testCalendar = new Calendar("Home", testUser).save();
	Calendar followerCalendar = new Calendar("Home", follower).save();

	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd, HH:mm");
	Date start = formatter.parse("2011/10/14, 09:00");
	Date end = formatter.parse("2011/10/14, 15:00");

	new Event("ESE sucks", start, end, testUser, testCalendar, false)
		.save();
	new Event("public event", formatter.parse("2011/10/14, 15:00"),
		formatter.parse("2011/10/14, 15:00"), testUser, testCalendar,
		true).save();

    }

    @After
    public void tearDown() throws Exception {
	Fixtures.deleteAllModels();
    }

    @Test
    public void testEvent() {
	User user = User.find("byNickname", "wuschu").first();

	List<Event> testEvents = Event.find("byOwner", user).fetch();
	Assert.assertEquals(testEvents.size(), 2);
    }

    @Test
    public void testFollowEvent() {
	// get a stranger
	User stranger = User.find("byNickname", "joe").first();

	// get a calendar from the stranger
	Calendar followerCalendar = Calendar.find("byOwnerAndName", stranger,
		"Home").first();

	// get an event from another user
	User user = User.find("byNickname", "joe").first();

	Event followEvent = Event.find("byNameAndOwner", "public event", user)
		.first();

	// add this event from another user to the calendar of the stranger
	followerCalendar.followEvent(followEvent);

	assertEquals(followerCalendar.events.size(), 1);

	assertEquals(Event.count(), 2);
    }

    @Test
    public void testGetFollowedEvents() {
	// get a stranger
	User stranger = User.find("byNickname", "joe").first();

	// get a calendar from the stranger
	Calendar followerCalendar = Calendar.find("byOwnerAndName", stranger,
		"Home").first();

	// get an event from another user
	User user = User.find("byNickname", "joe").first();
	Event followEvent = Event.find("byNameAndOwner", "public event", user)
		.first();

	// add this event from another user to the calendar of the stranger
	followerCalendar.followEvent(followEvent);

	List<Event> followedEvents = followerCalendar.getFollowedEvents();
	assertEquals(followedEvents.size(), 1);
    }
}
