package models.undo;

public interface Action {

    public void execute();

    public void undo();

}
