package gr.aueb.as.exporter.model;

import gr.aueb.as.exporter.enumeration.ArgumentType;

import java.io.Serializable;

public class Option implements Serializable {
    private static final long serialVersionUID = 20115222443815780L;

    private String name;
    private ArgumentType argumentType;
    private String equivalent;
    private String description;
    private boolean isLong = false;

    public Option() {
    }

    public Option(String name, ArgumentType argumentType, String equivalent, boolean aLong) {
        this.name = name;
        this.argumentType = argumentType;
        this.equivalent = equivalent;
        this.isLong = aLong;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArgumentType getArgumentType() {
        return argumentType;
    }

    public void setArgumentType(ArgumentType argumentType) {
        this.argumentType = argumentType;
    }

    public String getEquivalent() {
        return equivalent;
    }

    public void setEquivalent(String equivalent) {
        this.equivalent = equivalent;
    }

    public boolean isLong() {
        return isLong;
    }

    public void setLong(boolean aLong) {
        isLong = aLong;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Option{" +
                "name='" + name + '\'' +
                ", argumentType=" + argumentType +
                ", equivalent='" + equivalent + '\'' +
                ", description='" + description + '\'' +
                ", isLong=" + isLong +
                '}';
    }
}
