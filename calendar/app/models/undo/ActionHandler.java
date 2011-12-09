package models.undo;

import java.util.ArrayList;


public class ActionHandler {

    private ArrayList<Action> actions;

    public ActionHandler() {
	this.actions = new ArrayList<Action>();
    }

    public void add(Action a) {
	this.actions.add(a);
    }

    public void remove(Action a) {
	this.actions.remove(a);
    }

    public void undo() {
	for (Action action : this.actions) {
	    action.undo();
	}
    }
}
