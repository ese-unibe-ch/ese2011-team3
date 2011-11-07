package utilities;

import java.util.List;

import models.Event;

import com.google.gson.annotations.Expose;

public class OverlappingObject {
    @Expose
    private boolean isOverlapping;
    @Expose
    private List<Event> events;

    public OverlappingObject(List<Event> events) {
	this.events = events;
	this.isOverlapping = events.size() != 0;
    }
}
