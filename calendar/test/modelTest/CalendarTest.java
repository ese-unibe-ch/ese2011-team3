package modelTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public void testGetAllEventsOnADay() throws ParseException {
	User wuschu = new User("wuschu", "WTF", "secret", "wuschu@alt-f4.com")
		.save();
	Calendar wuschusCalendar = new Calendar("Home", wuschu).save();

	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd, HH:mm");

	new Event("ESE sucks", "small note",
		formatter.parse("2011/10/14, 09:00"),
		formatter.parse("2011/10/14, 15:00"), wuschu, wuschusCalendar,
		false, true).save();

	new Event("ESE sucks again!", "small note",
		formatter.parse("2011/10/15, 09:00"),
		formatter.parse("2011/10/15, 15:00"), wuschu, wuschusCalendar,
		false, true).save();

	List<Event> events = Calendar.getAllEventsOnDay(wuschusCalendar,
		formatter.parse("2011/10/14, 09:00"));
	assertEquals(1, events.size());

    }

}
