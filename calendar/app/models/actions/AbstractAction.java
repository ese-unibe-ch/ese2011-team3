package models.actions;

public abstract class AbstractAction implements Action {

    private Long id;

    public void setId(Long id) {
	this.id = id;
    }

    public Long getId() {
	return this.id;
    }

    public String toString() {
	return this.getClass().getSimpleName();
    }

}
