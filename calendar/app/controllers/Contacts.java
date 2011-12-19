package controllers;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import models.User;
import play.db.jpa.JPA;

public class Contacts extends Main
{
	public static void index(String contactSearch, String userSearch)
	{
		String extContactSearch = null;
		if (contactSearch != null)
		{
			extContactSearch = "%" + contactSearch + "%";
			extContactSearch = extContactSearch.replace('*', '%');
			extContactSearch = extContactSearch.replace('?', '_');
		}

		String extUserSearch = null;
		if (userSearch != null)
		{
			extUserSearch = "%" + userSearch + "%";
			extUserSearch = extUserSearch.replace('*', '%');
			extUserSearch = extUserSearch.replace('?', '_');
		}

		Query contactsQuery = JPA
				.em()
				.createQuery(
						"SELECT l FROM User u JOIN u.contacts l WHERE u.id = :id AND l.nickname LIKE :extContactSearch");
		Query otherUsersQuery = JPA
				.em()
				.createQuery(
						"SELECT k FROM User k WHERE k NOT IN (SELECT y FROM User x JOIN x.contacts y WHERE x.id = :id) AND k.id != :id AND k.nickname LIKE :extUserSearch");

		contactsQuery.setParameter("id", getUser().id);
		contactsQuery.setParameter("extContactSearch", extContactSearch);
		otherUsersQuery.setParameter("id", getUser().id);
		otherUsersQuery.setParameter("extUserSearch", extUserSearch);

		List<User> myContacts = contactsQuery.getResultList();
		List<User> otherUsers = otherUsersQuery.getResultList();

		if (extContactSearch == null)
		{
			myContacts = getUser().contacts;
		}
		if (extUserSearch == null)
		{
			otherUsers = User.findAll();
			otherUsers.removeAll(myContacts);
			otherUsers.remove(getUser());
		}

		renderArgs.put("contacts", myContacts);
		renderArgs.put("users", otherUsers);
		renderArgs.put("currentDate", new Date());
		renderArgs.put("userSearch", userSearch);
		renderArgs.put("contactSearch", contactSearch);

		render();
	}

	public static void add(Long contactId)
	{
		User contact = (User) JPA.em()
				.createQuery("SELECT u FROM User u WHERE u.id = :id")
				.setParameter("id", contactId).getSingleResult();
		User user = getUser();

		user.contacts.add(contact);
		user.save();
		String userSearch = null;
		String contactSearch = null;
		index(contactSearch, userSearch);
	}

	public static void remove(Long contactId)
	{
		User contact = (User) JPA.em()
				.createQuery("SELECT u FROM User u WHERE u.id = :id")
				.setParameter("id", contactId).getSingleResult();
		User user = getUser();

		user.contacts.remove(contact);
		user.save();
		String userSearch = null;
		String contactSearch = null;
		index(contactSearch, userSearch);
	}

	public static void listUsers(String term)
	{

		Query query;
		if (term != null && !term.equals(""))
		{
			query = JPA
					.em()
					.createQuery(
							"SELECT u FROM User u WHERE u.nickname LIKE ?1")
					.setParameter(1, term + "%");
			query.setMaxResults(10);
		}
		else
		{
			query = JPA.em().createQuery("SELECT u FROM User u");
		}

		List<User> users = query.getResultList();

		renderArgs.put("users", users);
		renderArgs.put("term", term);
		render();
	}
}
