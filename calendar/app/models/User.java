package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class User extends Model {

	public String nickname;
	public String fullname;
	public String password;
	public String mail;
	
	public User(String nick, String name, String pass, String mail) {
		this.nickname = nick;
		this.fullname = name;
		this.password = pass;
		this.mail = mail;
	}
	
}
