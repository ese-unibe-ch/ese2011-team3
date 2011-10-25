package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.joda.time.DateTime;

import play.db.jpa.Model;
import utilities.DayContainer;

@Entity
public class Calendar extends Model {
	public String name;

	@ManyToOne
	public User owner;

	@OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL)
	public List<Event> events;

	public Calendar(String name, User owner) {
		this.name = name;
		this.owner = owner;
		this.events = new ArrayList<Event>();
	}

	public List<DayContainer> getMonthData(Date currentDate) {
		DateTime date = new DateTime(currentDate);

		DateTime now = new DateTime();
		DateTime firstDayOfMonth = date.withDayOfMonth(1);
		DateTime runDay = firstDayOfMonth.minusDays(firstDayOfMonth.dayOfWeek()
				.get() - 1);

		int dayOfMonth = 1;
		DayContainer[] days = new DayContainer[42];

		for (int i = 0; i < days.length; i++) {
			runDay = runDay.plusDays(1);
			days[i].number = runDay.getDayOfMonth();
			DayContainer.DayContainerType type;
			if (days[i].number == date.getDayOfMonth()) {
				type = DayContainer.DayContainerType.SELECTED;
			} else if (days[i].number == now.getDayOfMonth()) {
				type = DayContainer.DayContainerType.TODAY;
			} else if (!runDay.monthOfYear().equals(now.monthOfYear())) {
				type = DayContainer.DayContainerType.OTHERMONTH;
			}
		}

		return (List<DayContainer>) Arrays.asList(days);
	}
}
