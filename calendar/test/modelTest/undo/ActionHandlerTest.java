package modelTest.undo;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import models.undo.ActionHandler;
import models.undo.EditEvent;
import models.undo.SaveEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class ActionHandlerTest extends UnitTest {
    @Before
    public void setUp() throws Exception {
	Fixtures.deleteAllModels();

    }

    @After
    public void tearDown() throws Exception {
	Fixtures.deleteAllModels();
    }

    @Test
    public void testActionHandler() {
	SaveEvent saveEvent = mock(SaveEvent.class);
	EditEvent editEvent = mock(EditEvent.class);

	ActionHandler actionHandler = new ActionHandler();

	actionHandler.invoke(saveEvent);
	actionHandler.invoke(editEvent);

	// undo last action
	actionHandler.undoLast();

	// verify
	verify(saveEvent).execute();
	verify(editEvent).execute();
	verify(editEvent).undo();

	// undo the last undo action
	actionHandler.redoLast();
	verify(editEvent, times(2)).execute();
    }
}
