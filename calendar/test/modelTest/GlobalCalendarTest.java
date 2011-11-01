package modelTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import models.Calendar;
import models.Event;
import models.GlobalCalendar;
import models.User;

import org.junit.Test;

import play.test.UnitTest;
import utilities.RepeatableType;

public class GlobalCalendarTest extends UnitTest {

    @Test
    public void testGlobalCalendar() throws ParseException {
	User user = new User("wuschu", "WTF", "secret", "wuschu@alt-f4.com")
		.save();
	Calendar calendar1 = new Calendar("Home", user).save();
	Calendar calendar2 = new Calendar("Work", user).save();

	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd, HH:mm");

	new Event("event #1", "small note",
		formatter.parse("2011/10/14, 09:00"),
		formatter.parse("2011/10/14, 15:00"), user, calendar1, false,
		RepeatableType.DAILY).save();

	new Event("event #2", "small note",
		formatter.parse("2011/10/14, 09:00"),
		formatter.parse("2011/10/14, 15:00"), user, calendar2, false,
		RepeatableType.DAILY).save();

	Calendar calendar = new GlobalCalendar("global", user);

	assertEquals(
		2,
		calendar.eventsAtDay(user, formatter.parse("2011/10/14, 09:00"))
			.size());

    }

}
