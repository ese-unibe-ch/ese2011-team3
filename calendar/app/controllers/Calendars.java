package controllers;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import models.Calendar;
import models.GlobalCalendar;
import models.User;
import models.actions.Action;
import models.actions.CalendarEdit;
import models.actions.CalendarRemove;

import org.joda.time.DateTime;

import play.db.jpa.JPA;
import play.mvc.With;

@With(Secure.class)
public class Calendars extends Main {

    static void putCalendarData(Long calendarId, Date currentDate) {
	Query calendarQuery = JPA.em()
		.createQuery("SELECT c FROM Calendar c WHERE c.id = :id")
		.setParameter("id", calendarId);
	Calendar calendar = (Calendar) calendarQuery.getSingleResult();
	if (currentDate == null) {
	    currentDate = new Date();
	}

	renderArgs.put("calendar", calendar);
	renderArgs.put("calendarData",
		calendar.getCalendarData(getUser(), currentDate));
	renderArgs.put("currentDate", currentDate);
    }

    private static void putGlobalCalendarData(Calendar calendar,
	    Date currentDate) {
	if (currentDate == null) {
	    currentDate = new Date();
	}
	renderArgs.put("calendar", calendar);
	renderArgs.put("calendarData",
		calendar.getCalendarData(getUser(), currentDate));
	renderArgs.put("currentDate", currentDate);
    }

    public static void index() {
	Query calendarQuery = JPA.em()
		.createQuery("SELECT c FROM Calendar c WHERE c.owner.id = :id")
		.setParameter("id", getUser().id);
	List<Calendar> calendarList = calendarQuery.getResultList();
	// TODO: estimate what to do if an user don't got any calendars.
	assert calendarList.size() != 0;
	viewCalendar(calendarList.get(0).getId(), new Date());
    }

    public static void viewGlobalCalendar(Date currentDate) {
	Calendar calendar = new GlobalCalendar("global", getUser());
	putGlobalCalendarData(calendar, currentDate);
	renderTemplate("Calendars/viewStrangerCalendar.html");
    }

    public static void viewCalendar(Long calendarId, Date currentDate) {
	if (calendarId == 0) {
	    viewGlobalCalendar(currentDate);
	}

	if (currentDate == null) {
	    currentDate = new Date();
	}

	putCalendarData(calendarId, currentDate);
	Query calendarQuery = JPA.em()
		.createQuery("SELECT c FROM Calendar c WHERE c.id = :id")
		.setParameter("id", calendarId);
	Calendar calendar = (Calendar) calendarQuery.getSingleResult();
	if (getUser().equals(calendar.owner)) {
	    renderTemplate("Calendars/viewCalendar.html");
	} else {
	    renderTemplate("Calendars/viewStrangerCalendar.html");
	}
    }

    public static void viewNextMonth(Long calendarId, Date currentDate) {
	viewCalendar(calendarId, new DateTime(currentDate).plusMonths(1)
		.toDate());
    }

    public static void viewPrevMonth(Long calendarId, Date currentDate) {
	viewCalendar(calendarId, new DateTime(currentDate).minusMonths(1)
		.toDate());
    }

    public static void createNewCalendar() {
	renderTemplate("Calendars/newCalendar.html");
    }

    public static void addCalendar(String calendarName) {
	User loginUser = getUser();
	Calendar calendar = new Calendar(calendarName, loginUser).save();
	loginUser.addCalendar(calendar);

	viewCalendar(calendar.id, null);
    }

    public static void editCalendar(Long calendarId) {
	Calendar calendar = (Calendar) JPA
		.em()
		.createQuery(
			"SELECT c FROM Calendar c WHERE c.id = :calendarId")
		.setParameter("calendarId", calendarId).getSingleResult();

	renderArgs.put("calendar", calendar);
	renderTemplate("Calendars/editCalendar.html");
    }

    public static void updateCalendar(Long calendarId, String calendarName) {
	Calendar calendar = (Calendar) JPA
		.em()
		.createQuery(
			"SELECT c FROM Calendar c WHERE c.id = :calendarId")
		.setParameter("calendarId", calendarId).getSingleResult();

	CalendarEdit edit = new CalendarEdit(calendar);
	edit.setName(calendarName);

	getActionHandler().invoke(edit);

	Calendars.index();
    }

    public static void deleteCalendar(Long calendarId) {
	Calendar calendar = (Calendar) JPA
		.em()
		.createQuery(
			"SELECT c FROM Calendar c WHERE c.id = :calendarId")
		.setParameter("calendarId", calendarId).getSingleResult();

	if (getUser().calendars.size() == 1) {
	    User loginUser = getUser();
	    Calendar newCalendar = new Calendar("default", loginUser).save();
	    loginUser.addCalendar(newCalendar);
	}

	Action remove = new CalendarRemove(calendar, getUser());

	getActionHandler().invoke(remove);

	/*
	 * for (Event event : calendar.events) { event.delete(); }
	 * calendar.save(); calendar.delete();
	 */
	Calendars.index();
    }
}
