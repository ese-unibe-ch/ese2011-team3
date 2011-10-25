package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Query;

import org.joda.time.DateTime;

import play.db.jpa.JPA;
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

	public List<DayContainer> getCalendarData(Date currentDate) {
		DateTime date = new DateTime(currentDate);

		DateTime now = new DateTime();
		DateTime firstDayOfMonth = date.withDayOfMonth(1);
		DateTime runDay = firstDayOfMonth.minusDays(firstDayOfMonth.dayOfWeek()
				.get());

		DayContainer[] days = new DayContainer[42];

		for (int i = 0; i < days.length; i++) {
			days[i] = new DayContainer();
			runDay = runDay.plusDays(1);
			days[i].date = runDay.toDate();
			DayContainer.DayContainerType type = DayContainer.DayContainerType.THISMONTH;
			if (runDay.getDayOfMonth() == date.getDayOfMonth()
					&& runDay.getMonthOfYear() == date.getMonthOfYear()
					&& runDay.getYear() == date.getYear()) {
				type = DayContainer.DayContainerType.SELECTED;
			} else if (runDay.getDayOfMonth() == now.getDayOfMonth()
					&& runDay.getMonthOfYear() == now.getMonthOfYear()
					&& runDay.getYear() == now.getYear()) {
				type = DayContainer.DayContainerType.TODAY;
			} else if (!runDay.monthOfYear().equals(date.monthOfYear())) {
				type = DayContainer.DayContainerType.OTHERMONTH;
			}
			days[i].type = type;
			days[i].containsEvents = (this.eventsAtDay(runDay.toDate()).size() > 0);
		}
		return (List<DayContainer>) Arrays.asList(days);
	}

	public List<Event> eventsAtDay(Date currentDate) {
		Query eventsQuery = JPA.em()
				.createQuery("SELECT e FROM Event e WHERE e.calendar.id = :id")
				.setParameter("id", this.id);
		List<Event> results = eventsQuery.getResultList();
		List<Event> events = new ArrayList<Event>();
		for (Event e : results) {
			if (e.happensOnDay(currentDate)) {
				events.add(e);
			}
		}
		return events;
	}
}
