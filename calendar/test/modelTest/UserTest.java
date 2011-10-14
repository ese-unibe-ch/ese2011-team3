package modelTest;

import static org.junit.Assert.*;

import models.User;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;

public class UserTest extends UnitTest {

	@Before
	public void setUp() throws Exception {
		new User("wuschu", "WTF", "secret", "wuschu@alt-f4.com").save();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testUser() {
		User u = User.find("byFullname", "WTF").first();
		User v = User.find("byMail", "wuschu@alt-f4.com").first();
		Assert.assertEquals(u, v);
	}

}
