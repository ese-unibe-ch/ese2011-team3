package modelTest.undo;

import java.util.Date;

import models.Calendar;
import models.Event;
import models.actions.Action;
import models.actions.RemoveEvent;
import models.actions.EditEvent;
import models.actions.SaveEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class ActionTest extends UnitTest {

    @Before
    public void setUp() throws Exception {
	Fixtures.deleteAllModels();
    }

    @After
    public void tearDown() throws Exception {
	Fixtures.deleteAllModels();
    }

    @Test
    public void testSaveAction() {
	Event e = new Event("name", "note", new Date(), new Date(), null, null,
		false, null).save();

	Calendar calendar = new Calendar("Calendar", null).save();

	Action save = new SaveEvent(e, calendar);

	Event e1 = Event.find("byName", "name").first();
	assertNotNull(e1);
	save.undo();
	Event e2 = Event.find("byName", "name").first();
	assertNull(e2);

    }

    @Test
    public void testEditAction() {
	Event e = new Event("name", "note", new Date(), new Date(), null, null,
		false, null);
	Calendar calendar = new Calendar("Calendar", null).save();

	Action save = new SaveEvent(e, calendar);
	save.execute();

	Event event = Event.find("byName", "name").first();
	assertNotNull(event);

	EditEvent edit = new EditEvent(e);
	edit.setName("okay");
	edit.execute();

	Event e1 = Event.find("byName", "name").first();
	assertNull(e1);

	Event e2 = Event.find("byName", "okay").first();
	assertNotNull(e2);

	edit.undo();

	Event e3 = Event.find("byName", "okay").first();
	assertNull(e3);

	Event e4 = Event.find("byName", "name").first();
	assertNotNull(e4);
    }

    @Test
    public void testDeleteAction() {
	Calendar calendar = new Calendar("Calendar", null).save();

	new Event("name", "note", new Date(), new Date(), null, calendar,
		false, null).save();

	Event event = Event.find("byName", "name").first();
	assertNotNull(event);

	Action delete = new RemoveEvent(event);
	delete.execute();

	Event e1 = Event.find("byName", "name").first();
	assertNull(e1);
    }
}
