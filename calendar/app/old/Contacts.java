package old;

import java.util.Collections;
import java.util.List;

import controllers.Secure;

import models.User;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Contacts extends Controller {

	public static void index() {
		User user = User.find("byNickname", Security.connected()).first();
		List<User> contacts = user.following;
		render(contacts);
	}

	public static void addContact(Long contactId) {
		User user = User.find("byNickname", Security.connected()).first();
		User contact = User.findById(contactId);
		user.following.add(contact);
		user.save();
		index();
	}

	public static void addContacts() {
		User user = User.find("byNickname", Security.connected()).first();
		List<User> contacts = User.findAll();
		contacts.removeAll(user.following);
		contacts.remove(user);
		Collections.sort(contacts);
		render(contacts);
	}

	public static void removeContact(Long contactId) {
		User user = User.find("byNickname", Security.connected()).first();
		User contact = User.findById(contactId);
		user.following.remove(contact);
		user.save();
		index();
	}
}
