package modelTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import models.Event;

import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class BootstrapTest extends UnitTest {

    @Test
    public void testDates() throws ParseException {
	Fixtures.deleteAllModels();
	Fixtures.loadModels("bootstrap-data.yml");

	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/*
	 * start: 2011-10-12 10:00:00.00 end: 2011-11-12 12:00:00.00
	 */
	Date start = formatter.parse("2011-10-12 10:00:00");
	Date end = formatter.parse("2011-11-12 12:00:00");
	Event longEvent = Event.find("byName", "long event").first();

	assertEquals(start, longEvent.start);
	assertEquals(end, longEvent.end);
    }

    @Test
    public void testMalformedDates() throws ParseException {

	new Event("test", null, null, null, null, null).save();
	Event test = Event.find("byName", "test").first();

	test.setStart("ashdfhasf");
	test.setEnd("ashdfhasf");
	assertNull(test.start);
	assertNull(test.end);
    }
}
