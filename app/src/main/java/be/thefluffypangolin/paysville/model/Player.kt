package be.thefluffypangolin.paysville.model;

import org.jetbrains.annotations.NotNull;

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

    @NotNull
    @Override
    public String toString() {
        return name;
    }
}
