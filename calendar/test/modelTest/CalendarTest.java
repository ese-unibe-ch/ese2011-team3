package modelTest;

import java.util.List;

import models.Calendar;
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
		User testUser = new User("wuschu", "WTF", "secret", "wuschu@alt-f4.com")
				.save();
		new Calendar("Home", testUser).save();
	}

	@After
	public void tearDown() throws Exception {
		Fixtures.deleteAllModels();
	}

	@Test
	public void testUsersCalendarList() {
		User testUser = User.find("byNickname", "wuschu").first();
		Assert.assertNotNull(testUser);
		List<Calendar> testList = Calendar.find("byOwner", testUser).fetch();
		Calendar testCalendar = Calendar.find("byName", "Home").first();
		Assert.assertEquals(testList.get(0), testCalendar);
	}

}
