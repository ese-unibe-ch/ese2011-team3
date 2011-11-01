package modelTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import models.Calendar;
import models.Event;
import models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;
import utilities.RepeatableType;

public class EventTest extends UnitTest {

    @Before
    public void setUp() throws Exception {
	Fixtures.deleteAllModels();
    }

    @After
    public void tearDown() throws Exception {
	Fixtures.deleteAllModels();
    }

    @Test
    public void testRepeatableTypes() throws ParseException {
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd, HH:mm");

	User testUser = new User("wuschu", "WTF", "secret", "wuschu@alt-f4.com")
		.save();
	Calendar testCalendar = new Calendar("Home", testUser).save();

	new Event("default", "small note",
		formatter.parse("2011/10/14, 09:00"),
		formatter.parse("2011/10/14, 15:00"), testUser, testCalendar,
		false).save();

	new Event("daily", "small note", formatter.parse("2011/10/14, 09:00"),
		formatter.parse("2011/10/14, 15:00"), testUser, testCalendar,
		false, RepeatableType.DAILY).save();

	new Event("monthly", "small note",
		formatter.parse("2011/10/14, 09:00"),
		formatter.parse("2011/10/14, 15:00"), testUser, testCalendar,
		false, RepeatableType.MONTHLY).save();

	new Event("weekly", "small note", formatter.parse("2011/10/14, 09:00"),
		formatter.parse("2011/10/14, 15:00"), testUser, testCalendar,
		false, RepeatableType.WEEKLY).save();

	new Event("yearly", "small note", formatter.parse("2011/10/14, 09:00"),
		formatter.parse("2011/10/14, 15:00"), testUser, testCalendar,
		false, RepeatableType.YEARLY).save();

	new Event("none", "small note", formatter.parse("2011/10/14, 09:00"),
		formatter.parse("2011/10/14, 15:00"), testUser, testCalendar,
		false, RepeatableType.NONE).save();

	Event defaultEvent = Event.find("byName", "default").first();
	Event daily = Event.find("byName", "daily").first();
	Event monthly = Event.find("byName", "monthly").first();
	Event weekly = Event.find("byName", "weekly").first();
	Event yearly = Event.find("byName", "yearly").first();
	Event none = Event.find("byName", "none").first();

	assertEquals(RepeatableType.NONE, defaultEvent.getRepeatableType());
	assertEquals(RepeatableType.DAILY, daily.getRepeatableType());
	assertEquals(RepeatableType.MONTHLY, monthly.getRepeatableType());
	assertEquals(RepeatableType.WEEKLY, weekly.getRepeatableType());
	assertEquals(RepeatableType.YEARLY, yearly.getRepeatableType());
	assertEquals(RepeatableType.NONE, none.getRepeatableType());
    }

    @Test
    public void testRepeatableEventDaily() throws ParseException {
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd, HH:mm");

	User testUser = new User("wuschu", "WTF", "secret", "wuschu@alt-f4.com")
		.save();
	Calendar testCalendar = new Calendar("Home", testUser).save();

	new Event("daily", "small note", formatter.parse("2011/10/14, 09:00"),
		formatter.parse("2011/10/14, 15:00"), testUser, testCalendar,
		false, RepeatableType.DAILY).save();

	Event event = Event.find("byName", "daily").first();

	assertTrue(event.happensOnDay(formatter.parse("2011/10/14, 09:00")));
	assertTrue(event.happensOnDay(formatter.parse("2011/10/15, 10:00")));
	assertTrue(event.happensOnDay(formatter.parse("2011/10/16, 11:00")));
	assertTrue(event.happensOnDay(formatter.parse("2011/10/17, 12:00")));
    }

    @Test
    public void testRepeatableEventWeekly() throws ParseException {
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd, HH:mm");

	User testUser = new User("wuschu", "WTF", "secret", "wuschu@alt-f4.com")
		.save();
	Calendar testCalendar = new Calendar("Home", testUser).save();

	new Event("weekly", "small note", formatter.parse("2011/10/14, 09:00"),
		formatter.parse("2011/10/14, 15:00"), testUser, testCalendar,
		false, RepeatableType.WEEKLY).save();

	Event event = Event.find("byName", "weekly").first();

	assertTrue(event.happensOnDay(formatter.parse("2011/10/14, 09:00")));
	assertTrue(event.happensOnDay(formatter.parse("2011/10/21, 10:00")));
    }

    @Test
    public void testRepeatableEventMonthly() throws ParseException {
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd, HH:mm");

	User testUser = new User("wuschu", "WTF", "secret", "wuschu@alt-f4.com")
		.save();
	Calendar testCalendar = new Calendar("Home", testUser).save();

	new Event("monthly", "small note",
		formatter.parse("2011/10/14, 09:00"),
		formatter.parse("2011/10/14, 15:00"), testUser, testCalendar,
		false, RepeatableType.MONTHLY).save();

	Event event = Event.find("byName", "monthly").first();

	assertTrue(event.happensOnDay(formatter.parse("2011/10/14, 09:00")));
	assertTrue(event.happensOnDay(formatter.parse("2012/11/14, 10:00")));
    }

    @Test
    public void testRepeatableEventYearly() throws ParseException {
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd, HH:mm");

	User testUser = new User("wuschu", "WTF", "secret", "wuschu@alt-f4.com")
		.save();
	Calendar testCalendar = new Calendar("Home", testUser).save();

	new Event("yearly", "small note", formatter.parse("2011/10/14, 09:00"),
		formatter.parse("2011/10/14, 15:00"), testUser, testCalendar,
		false, RepeatableType.YEARLY).save();

	Event event = Event.find("byName", "yearly").first();

	assertTrue(event.happensOnDay(formatter.parse("2011/10/14, 09:00")));
	assertTrue(event.happensOnDay(formatter.parse("2012/10/14, 10:00")));
    }

    @Test
    public void testCreateEvent() throws ParseException {
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd, HH:mm");

	User testUser = new User("wuschu", "WTF", "secret", "wuschu@alt-f4.com")
		.save();
	Calendar testCalendar = new Calendar("Home", testUser).save();

	new Event("ESE sucks", "small note",
		formatter.parse("2011/10/14, 09:00"),
		formatter.parse("2011/10/14, 15:00"), testUser, testCalendar,
		false).save();

	User user = User.find("byNickname", "wuschu").first();
	Event testEvent = Event.find("byOwner", user).first();
	assertNotNull(testEvent);
    }

    @Test
    public void testIfEventIsFollowedBy() throws ParseException {
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd, HH:mm");
	User wuschu = new User("wuschu", "WTF", "secret", "wuschu@alt-f4.com")
		.save();

	User joe = new User("wuschu", "WTF", "secret", "wuschu@alt-f4.com")
		.save();
	Calendar wuschusCalendar = new Calendar("Home", wuschu).save();
	Calendar joesCalendar = new Calendar("Home", joe).save();

	new Event("ESE sucks", "small note",
		formatter.parse("2011/10/14, 09:00"),
		formatter.parse("2011/10/14, 15:00"), wuschu, wuschusCalendar,
		false).save();

	User user = User.find("byNickname", "wuschu").first();
	Event testEvent = Event.find("byOwner", user).first();

	testEvent.follow(joesCalendar);
	testEvent.save();

	assertTrue(testEvent.getFollowers().contains(joe));
    }

    @Test
    public void testIfEventIsFollowed() throws ParseException {
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd, HH:mm");
	User wuschu = new User("wuschu", "WTF", "secret", "wuschu@alt-f4.com")
		.save();

	User joe = new User("wuschu", "WTF", "secret", "wuschu@alt-f4.com")
		.save();
	Calendar wuschusCalendar = new Calendar("Home", wuschu).save();
	Calendar joesCalendar = new Calendar("Home", joe).save();

	new Event("ESE sucks", "small note",
		formatter.parse("2011/10/14, 09:00"),
		formatter.parse("2011/10/14, 15:00"), wuschu, wuschusCalendar,
		false).save();

	User user = User.find("byNickname", "wuschu").first();
	Event testEvent = Event.find("byOwner", user).first();

	testEvent.follow(joesCalendar);
	testEvent.save();

	assertTrue(testEvent.getFollowers().size() > 0);
    }

    @Test
    public void testGetFollowableEvents() throws ParseException {
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd, HH:mm");

	User testUser = new User("wuschu", "WTF", "secret", "wuschu@alt-f4.com")
		.save();
	Calendar testCalendar = new Calendar("Home", testUser).save();

	new Event("ESE sucks", "small note",
		formatter.parse("2011/10/14, 09:00"),
		formatter.parse("2011/10/14, 15:00"), testUser, testCalendar,
		true).save();

	Event followableEvent = Event
		.find("byIsPublicAndOwner", true, testUser).first();
	assertNotNull(followableEvent);
    }

    @Test
    public void testFollowEvent() throws ParseException {
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd, HH:mm");

	User wuschu = new User("wuschu", "WTF", "secret", "wuschu@alt-f4.com")
		.save();

	User joe = new User("joe", "WTF", "secret", "joe@alt-f4.com").save();

	Calendar joesCalendar = new Calendar("Home", joe).save();
	Calendar wuschusCalendar = new Calendar("Home", wuschu).save();

	new Event("ESE sucks", "small note",
		formatter.parse("2011/10/14, 09:00"),
		formatter.parse("2011/10/14, 15:00"), wuschu, wuschusCalendar,
		true).save();

	Event followableEvent = Event.find("byIsPublic", true).first();

	assertNotNull(followableEvent);

	followableEvent.follow(joesCalendar); // ugly!
	followableEvent.save();

	Calendar testCalendar = Calendar.find("byOwner", joe).first();
	assertEquals(1, testCalendar.events.size());
    }

}
