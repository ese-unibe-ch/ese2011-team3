package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class Calendar extends Model {

	public String name;

	@ManyToOne
	public User owner;

	public Calendar(String name, User owner) {
		this.name = name;
		this.owner = owner;
	}

}
