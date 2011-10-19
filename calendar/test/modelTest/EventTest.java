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
		Calendar testCalendar = new Calendar("Home", testUser).save();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd, HH:mm");
		Date start = formatter.parse("2011/10/14, 09:00");
		Date end = formatter.parse("2011/10/14, 15:00");
		new Event("ESE sucks", start, end, testCalendar, false).save();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEvent() {
		Calendar testCalendar = Calendar.find("byName", "Home").first();
		List<Event> testEvents = Event.find("byCalendar", testCalendar).fetch();
		Assert.assertEquals(testEvents.size(), 1);
	}

}
