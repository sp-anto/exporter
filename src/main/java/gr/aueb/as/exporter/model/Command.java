package gr.aueb.as.exporter.model;

import java.io.Serializable;
import java.util.List;

public class Command implements Serializable {
    private static final long serialVersionUID = 2479263167987271294L;

    private String name;
    private String shortDescription;
    private String longDescription;
    private List<String> syntaxes;
    private List<Option> options;

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

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public List<String> getSyntaxes() {
        return syntaxes;
    }

    public void setSyntaxes(List<String> syntaxes) {
        this.syntaxes = syntaxes;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "Command{" +
                "name='" + name + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", longDescription='" + longDescription + '\'' +
                ", syntaxes=" + syntaxes +
                ", options=" + options +
                '}';
    }
}
