package utilities;

import java.util.List;

import models.Event;

import com.google.gson.annotations.Expose;

public class OverlappingData {
    @Expose
    private boolean isOverlapping;
    @Expose
    private List<Event> events;

    public OverlappingData(List<Event> events) {
	this.events = events;
	this.isOverlapping = events.size() != 0;
    }

    public boolean isOverlapping() {
	return isOverlapping;
    }

    public List<Event> getEvents() {
	return this.events;
    }
}