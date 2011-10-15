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

	@ManyToMany
	public List<User> following;

	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
	public List<Calendar> calendars;

	public User(String nick, String name, String pass, String mail) {
		this.nickname = nick;
		this.fullname = name;
		this.password = pass;
		this.mail = mail;
		this.following = new ArrayList<User>();
	}

	public static User connect(String nickname, String password) {
		return User.find("byNicknameAndPassword", nickname, password).first();
	}

	public int compareTo(User u) {
		int compare = nickname.compareTo(u.nickname);
		return compare;
	}
}
