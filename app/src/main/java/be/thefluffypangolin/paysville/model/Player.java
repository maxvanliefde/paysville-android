package be.thefluffypangolin.paysville.model;

/**
 * Cette classe représente un jour, désigné par son nom.
 */
public class Player {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player(String name) {
        this.name = name;
    }
}
