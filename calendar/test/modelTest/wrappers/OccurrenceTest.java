package modelTest.wrappers;

import java.util.Date;

import junit.framework.Assert;
import models.Event;
import models.occurrences.Occurrence;
import models.occurrences.OneTimeOccurrence;
import models.occurrences.WeeklyOccurrence;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;
import utilities.OccurrenceType;

public class OccurrenceTest extends UnitTest {
	@Before
	public void setUp() throws Exception {
		Fixtures.deleteAllModels();
	}

	@After
	public void tearDown() throws Exception {
		Fixtures.deleteAllModels();
	}

	@Test
	public void testOneTimeRecurringEvent() {
		Event event = new Event("Test Event", "", new Date(), new DateTime()
				.plusDays(1).toDate(), null, null, true, OccurrenceType.NONE)
				.save();

		Occurrence wrapper = Occurrence.getOccurrence(event);

		Assert.assertEquals(wrapper.getClass(), OneTimeOccurrence.class);

		Assert.assertEquals(wrapper.getStart().toDate(), event.start);
	}

	@Test
	public void testWeeklyRecurringEvent() {
		Event event = new Event("Test Event", "", new Date(), new DateTime()
				.plusDays(1).toDate(), null, null, true, OccurrenceType.WEEKLY)
				.save();

		Occurrence wrapper = Occurrence.getOccurrence(event);

		Assert.assertEquals(wrapper.getClass(), WeeklyOccurrence.class);

		Assert.assertEquals(wrapper.getStart().toDate(), event.start);

		Occurrence nextWrapper = wrapper.getNext();

		Assert.assertEquals(nextWrapper.getClass(), WeeklyOccurrence.class);

		Assert.assertEquals(wrapper.getStart().plusWeeks(1),
				nextWrapper.getStart());

		Occurrence fifthNextWrapper = wrapper.getNext().getNext().getNext()
				.getNext().getNext();

		Assert.assertEquals(wrapper.getStart().plusDays(35),
				fifthNextWrapper.getStart());
	}

	@Test
	public void testWeeklyRecurringOccurrenceForDay() {
		Event event = new Event("Test Event", "", new Date(), new DateTime()
				.plusDays(2).toDate(), null, null, true, OccurrenceType.WEEKLY)
				.save();

		Occurrence wrapper = Occurrence.getOccurrence(event);

		Assert.assertNull(wrapper.occurrenceForDay(new DateTime().plusDays(6)));
		Assert.assertEquals(
				wrapper.occurrenceForDay(new DateTime().plusDays(7)),
				wrapper.getNext());
		Assert.assertEquals(
				wrapper.occurrenceForDay(new DateTime().plusDays(8)),
				wrapper.getNext());
		Assert.assertEquals(
				wrapper.occurrenceForDay(new DateTime().plusDays(9)),
				wrapper.getNext());
		Assert.assertNull(wrapper.occurrenceForDay(new DateTime().plusDays(10)));

		Assert.assertEquals(
				wrapper.occurrenceForDay(new DateTime().plusWeeks(3)), wrapper
						.getNext().getNext().getNext());
	}
}
