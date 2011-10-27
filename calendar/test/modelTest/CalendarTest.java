package modelTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

import models.Calendar;
import models.Event;
import models.User;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class CalendarTest extends UnitTest {

	@Before
	public void setUp() throws Exception {
		Fixtures.deleteAllModels();
	}

	@After
	public void tearDown() throws Exception {
		Fixtures.deleteAllModels();
	}

	@Test
	public void testUsersCalendarList() {
		User testUser = new User("wuschu", "WTF", "secret", "wuschu@alt-f4.com")
				.save();
		new Calendar("Home", testUser).save();

		User wuschu = User.find("byNickname", "wuschu").first();
		Assert.assertNotNull(wuschu);
		List<Calendar> testList = Calendar.find("byOwner", wuschu).fetch();
		Calendar testCalendar = Calendar.find("byName", "Home").first();
		Assert.assertEquals(testList.get(0), testCalendar);
	}

	@Test
	public void testGetAllFollowersOfEvent() throws ParseException {
		User wuschu = new User("wuschu", "WTF", "secret", "wuschu@alt-f4.com")
				.save();

		User joe = new User("joe", "WTF", "secret", "joe@alt-f4.com").save();

		User nic = new User("nic", "WTF", "secret", "nic@alt-f4.com").save();

		Calendar wuschusCalendar = new Calendar("Home", wuschu).save();
		Calendar joesCalendar = new Calendar("Home", joe).save();
		Calendar nicsCalendar = new Calendar("Home", nic).save();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd, HH:mm");

		new Event("ESE sucks", "small note",
				formatter.parse("2011/10/14, 09:00"),
				formatter.parse("2011/10/14, 15:00"), wuschu, wuschusCalendar,
				true).save();

		Event followEvent = Event.find("byName", "ESE sucks").first();

		followEvent.follow(joesCalendar);
		followEvent.follow(nicsCalendar);
		followEvent.save();

		Event event = Event.find("byName", "ESE sucks").first();

		Set<User> followers = event.getFollowers();

		assertEquals(2, followers.size());
		assert (joesCalendar.events.contains(event));
		assert (nicsCalendar.events.contains(event));
	}

	@Test
	public void testGetAllFollowedEvents() throws ParseException {
		User wuschu = new User("wuschu", "WTF", "secret", "wuschu@alt-f4.com")
				.save();

		User joe = new User("joe", "WTF", "secret", "joe@alt-f4.com").save();

		User nic = new User("nic", "WTF", "secret", "nic@alt-f4.com").save();

		Calendar wuschusCalendar = new Calendar("Home", wuschu).save();
		Calendar joesCalendar = new Calendar("Home", joe).save();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd, HH:mm");

		new Event("ESE sucks", "small note",
				formatter.parse("2011/10/14, 09:00"),
				formatter.parse("2011/10/14, 15:00"), wuschu, wuschusCalendar,
				true).save();

		new Event("ESE sucks again", "small note",
				formatter.parse("2011/10/14, 09:00"),
				formatter.parse("2011/10/14, 15:00"), wuschu, wuschusCalendar,
				true).save();

		new Event("ESE", "small note", formatter.parse("2011/10/14, 09:00"),
				formatter.parse("2011/10/14, 15:00"), joe, joesCalendar, true)
				.save();

		Event event1 = Event.find("byName", "ESE sucks").first();
		Event event2 = Event.find("byName", "ESE sucks").first();

		event1.follow(joesCalendar);
		event1.save();

		event2.follow(joesCalendar);
		event2.save();

		Calendar calendar = Calendar.find("byNameAndOwner", "Home", joe)
				.first();

		assertEquals(2, calendar.getFollowingEvents().size());
	}

	@Test
	public void testGetAllEventsOnADay() throws ParseException {
		User wuschu = new User("wuschu", "WTF", "secret", "wuschu@alt-f4.com")
				.save();
		User tom = new User("tom", "WTF", "secret", "tom@alt-f4.com").save();

		Calendar wuschusCalendar = new Calendar("Home", wuschu).save();
		Calendar tomsCalendar = new Calendar("Home", tom).save();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd, HH:mm");

		new Event("ESE sucks", "small note",
				formatter.parse("2011/10/14, 09:00"),
				formatter.parse("2011/10/14, 15:00"), wuschu, wuschusCalendar,
				true).save();

		new Event("ESE sucks again!", "small note",
				formatter.parse("2011/10/15, 09:00"),
				formatter.parse("2011/10/15, 15:00"), wuschu, wuschusCalendar,
				true).save();

		new Event("ESE sucks again!", "small note",
				formatter.parse("2011/10/15, 09:00"),
				formatter.parse("2011/10/15, 15:00"), tom, tomsCalendar, true)
				.save();

		List<Event> events = wuschusCalendar.eventsAtDay(wuschu,
				formatter.parse("2011/10/14, 10:00"));
		assertEquals(1, events.size());

	}

	@Test
	public void testAddNewCalendar() {
		User tom = new User("tom", "WTF", "secret", "tom@alt-f4.com").save();
		Calendar tomsCalendar = new Calendar("Home", tom).save();
		tom.addCalendar(tomsCalendar);
		assertEquals(tom.calendars.size(), 1);
		assertEquals(tomsCalendar, tom.calendars.get(0));
		assertEquals(tom.defaultCalendar, tomsCalendar);
	}

	@Test
	public void testDefaultCalendar() {
		User tom = new User("tom", "WTF", "secret", "tom@alt-f4.com").save();
		Calendar tomsCalendar = new Calendar("Home", tom).save();
		tomsCalendar.save();
		tom.refresh();
		List<Calendar> calendars = Calendar.find("byOwner", tom).fetch();
		assertEquals(calendars.size(), 1);
		assertEquals(tom.calendars.size(), 1);
		assertEquals(Calendar.find("byName", "Home").first(), tomsCalendar);
		assertEquals(tom.defaultCalendar, tomsCalendar);
	}

}
