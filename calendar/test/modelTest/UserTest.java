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
		Fixtures.deleteAllModels();
		new User("wuschu", "WTF", "secret", "wuschu@alt-f4.com").save();
		new User("polj", "WTH", "hidden", "polj@alt-f4.com").save();
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

	@Test
	public void testFollowSystem() {
		User userOne = User.find("byNickname", "wuschu").first();
		User userTwo = User.find("byNickname", "polj").first();

		userOne.contacts.add(userTwo);

		Assert.assertTrue(userOne.contacts.contains(userTwo));
	}

	@Test
	public void testUserAuthenticate() {
		String testNickname = "wuschu";
		String testUserPassword = "secret";
		User testUser = User.connect(testNickname, testUserPassword);
		assertNotNull(testUser);
		assertEquals(testUser.nickname, testNickname);

	}

}
