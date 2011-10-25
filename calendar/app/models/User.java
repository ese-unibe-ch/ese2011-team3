package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

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
	public List<User> following;

	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
	public List<Calendar> calendars;

	public Calendar defaultCalendar = null;

	public User(String nick, String name, String pass, String mail) {
		this.nickname = nick;
		this.fullname = name;
		this.password = pass;
		this.mail = mail;
		this.following = new ArrayList<User>();
		this.events = new ArrayList<Event>();
		this.calendars = new ArrayList<Calendar>();

	}

	public static User connect(String nickname, String password) {
		return User.find("byNicknameAndPassword", nickname, password).first();
	}

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

	public int compareTo(User u) {
		int compare = nickname.compareTo(u.nickname);
		return compare;
	}

	// should be used to add calendars
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

	public List<Calendar> getCalendars() {
		return this.calendars;
	}
}
