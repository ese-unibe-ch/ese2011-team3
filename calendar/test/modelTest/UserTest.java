package modelTest;

import models.User;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class UserTest extends UnitTest {

	@Before
	public void setUp() throws Exception {
		new User("wuschu", "WTF", "secret", "wuschu@alt-f4.com").save();
	}

	@After
	public void tearDown() throws Exception {
		Fixtures.deleteAllModels();
	}

	@Test
	public void testUser() {
		User userOne = User.find("byFullname", "WTF").first();
		User userTwo = User.find("byMail", "wuschu@alt-f4.com").first();
		Assert.assertEquals(userOne, userTwo);
	}

}
