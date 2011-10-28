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
 * @author Alt-F4
 * 
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
	 * @param nickname
	 * @param password
	 * @return the user which is logged in
	 */
	public static User connect(String nickname, String password) {
		return User.find("byNicknameAndPassword", nickname, password).first();
	}

	/**
	 * @return the calendar that should be displayed by default after loggin in
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
	 * @param calendar
	 *            - set it to default if it is the very first calendar.
	 */
	public void addCalendar(Calendar calendar) {
		if (this.defaultCalendar == null) {
			this.defaultCalendar = calendar;
		}
		this.calendars.add(calendar);
	}

	// TODO: set defaultCalendar correctly. why is the list calendars empty,
	// that the JPA hands over?
	public void setCalendars(List<Calendar> calendars) {
		this.calendars = calendars;
		if (this.defaultCalendar == null && !calendars.isEmpty()) {
			this.defaultCalendar = calendars.get(0);
		}
	}

	/**
	 * @return a list of all the calendars of a user.
	 */
	public List<Calendar> getCalendars() {
		return this.calendars;
	}
}
