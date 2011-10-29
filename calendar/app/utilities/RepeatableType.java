package utilities;

import java.util.Date;

import models.Event;

public enum RepeatableType {

    NONE(1) {

	public boolean happensOnDay(Event event, Date aDay) {
	    // TODO
	    return false;
	}
    },

    DAILY(2) {

	public boolean happensOnDay(Event event, Date aDay) {
	    // TODO
	    return false;
	}
    },
    WEEKLY(3) {
	public boolean happensOnDay(Event event, Date aDay) {
	    // TODO
	    return false;
	}
    },
    MONTHLY(4) {

	public boolean happensOnDay(Event event, Date aDay) {
	    // TODO
	    return false;
	}
    },
    YEARLY(5) {

	public boolean happensOnDay(Event event, Date aDay) {
	    // TODO
	    return false;
	}
    };

    private int id;

    RepeatableType(int id) {
	this.id = id;
    }

    public int getId() {
	return id;
    }

    public static RepeatableType getType(int id) {
	switch (id) {
	case 1:
	    return RepeatableType.NONE;
	case 2:
	    return RepeatableType.DAILY;
	case 3:
	    return RepeatableType.MONTHLY;
	case 4:
	    return RepeatableType.YEARLY;
	default:
	    return RepeatableType.NONE;
	}
    }

    public abstract boolean happensOnDay(Event event, Date aDay);

}