package modelTest.undo;

import java.util.Date;

import models.Event;
import models.undo.Action;
import models.undo.EditEvent;
import models.undo.SaveEvent;

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

	Action s = new SaveEvent(e);

	Event e1 = Event.find("byName", "name").first();
	assertNotNull(e1);
	s.undo();
	Event e2 = Event.find("byName", "name").first();
	assertNull(e2);

    }

    @Test
    public void testEditAction() {
	Event e = new Event("name", "note", new Date(), new Date(), null, null,
		false, null).save();

	Action save = new SaveEvent(e);
	Action edit = new EditEvent(e);

	Event event = Event.find("byName", "name").first();

	event.name = "okay";
	event.save();

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
}
