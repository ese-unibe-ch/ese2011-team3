package models.undo;

import java.io.Serializable;
import java.util.Stack;

public class ActionHandler implements Serializable {

    private Stack<Action> undos;
    private Stack<Action> redos;

    public ActionHandler() {
	this.undos = new Stack<Action>();
	this.redos = new Stack<Action>();
    }

    public void invoke(Action action) {
	this.undos.push(action);
	action.execute();
	this.redos.clear();
    }

    public void remove(Action a) {
	this.undos.remove(a);
    }

    public void undoAll() {
	while (!this.undos.isEmpty()) {
	    this.undoLast();
	}
    }

    public void undoLast() {
	Action action = this.undos.pop();
	action.undo();

	this.redos.push(action);
    }

    public void redoLast() {
	Action action = this.redos.pop();
	this.undos.push(action);
	action.execute();
    }

    public String toString() {
	StringBuilder sb = new StringBuilder();

	sb.append("undos: \n");

	for (Action undo : this.undos) {
	    sb.append(undo.toString() + "\n");
	}

	sb.append("redos: \n");
	for (Action redo : this.redos) {
	    sb.append(redo.toString() + "\n");
	}
	return sb.toString();
    }
}
