package models.actions;

public interface Action {

    public void execute();

    public void undo();

    public void setId(Long id);

    public Long getId();

}
