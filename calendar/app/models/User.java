package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

/**
 * The User class is used to create
 * {@link #User(String, String, String, String)} and handle users. It is located
 * in the models folder of the calendar application. A user knows about his
 * calendars {@link Calendar} and events {@link Event}. Every user has a
 * specific nickname and a password to login to the calendar application.
 * 
 * @author Alt-F4
 */
@Entity
public class User extends Model implements Comparable<User> {
	public String nickname;
	public String fullname;
	public String password;
	public String mail;

	@Lob
	public String profile;

	@OneToMany(mappedBy = "owner")
	public List<Event> events;

	@ManyToMany
	public List<User> contacts;

	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
	public List<Calendar> calendars;

	/**
	 * Before no new calendar is created there is no default calendar.
	 */
	public Calendar defaultCalendar = null;

	/**
	 * @param nick
	 *            - give the nickname of the new user
	 * @param name
	 *            - give the full name of the new user
	 * @param pass
	 *            - the password of the new user
	 * @param mail
	 *            - the email-address of the new user
	 */
	public User(String nick, String name, String pass, String mail) {
		this.nickname = nick;
		this.fullname = name;
		this.password = pass;
		this.mail = mail;
		this.contacts = new ArrayList<User>();
		this.events = new ArrayList<Event>();
		this.calendars = new ArrayList<Calendar>();

	}

	/**
	 * this method is used to authenticate a user who tries to log in on the
	 * application. The user is granted access to the application if the
	 * name/pwd combination is right and existing. otherwise the user is
	 * rejected and logging in fails.
	 * 
	 * @param nickname
	 *            the nickname of the given name/pwd combination
	 * @param password
	 *            the password of the given name/pwd combination
	 * @return 'null' if no match is found, otherwise the User object with the
	 *         matching name/pwd combination
	 */
	public static User connect(String nickname, String password) {
		return User.find("byNicknameAndPassword", nickname, password).first();
	}

	/**
	 * returns the default calendar of a specific user. If
	 * {@link User.defaultCalendar} is null and the user has no calendars yet,
	 * then a new calendar with the fullname of the user is created and set as
	 * default calendar. if no default calendar is set but the user has
	 * calendars, the first calendar in the user's calendar list is chosen to be
	 * the default calendar.
	 * 
	 * @return the calendar that should be displayed by default after logging in
	 *         on the page.
	 */
	public Calendar getDefaultCalendar() {
		if (this.defaultCalendar != null) {
			if (this.calendars.isEmpty()) {
				this.defaultCalendar = new Calendar(
						this.fullname + " Calendar", this);
			} else {
				this.defaultCalendar = this.calendars.get(0);
			}
		}
		return this.defaultCalendar;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(User u) {
		int compare = nickname.compareTo(u.nickname);
		return compare;
	}

	// should be used to add calendars
	/**
	 * Add a calendar to the user's list of calendars.
	 * 
	 * @param calendar
	 *            - set it to default if it is the very first calendar.
	 */
	public void addCalendar(Calendar calendar) {
		if (this.defaultCalendar == null) {
			this.defaultCalendar = calendar;
		}
		this.calendars.add(calendar);
	}

	/**
	 * set the default calendar if there already exist calendars but no default
	 * calendar is specified yet.
	 * 
	 * @param calendars
	 */

	// TODO: set defaultCalendar correctly. why is the list calendars empty,
	// that the JPA hands over?
	public void setCalendars(List<Calendar> calendars) {
		this.calendars = calendars;
		if (this.defaultCalendar == null && !calendars.isEmpty()) {
			this.defaultCalendar = calendars.get(0);
		}
	}

	/**
	 * returns a list of all the calendars of a user.
	 * 
	 * @return a list of all the calendars of a user.
	 */
	public List<Calendar> getCalendars() {
		return this.calendars;
	}
}
