package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class User extends Model {

	public String nickname;
	public String fullname;
	public String password;
	public String mail;

	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
	public List<Calendar> calendars;

	public User(String nick, String name, String pass, String mail) {
		this.nickname = nick;
		this.fullname = name;
		this.password = pass;
		this.mail = mail;
	}

}
