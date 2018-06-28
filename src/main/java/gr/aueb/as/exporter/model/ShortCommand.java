package gr.aueb.as.exporter.model;

import java.io.Serializable;

public class ShortCommand implements Serializable {
    private static final long serialVersionUID = 732249222227240233L;

    private String name;
    private String shortDescription;

    public ShortCommand(Command command) {
        this.name = command.getName();
        this.shortDescription = command.getShortDescription();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    @Override
    public String toString() {
        return "ShortCommand{" +
                "name='" + name + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                '}';
    }
}
