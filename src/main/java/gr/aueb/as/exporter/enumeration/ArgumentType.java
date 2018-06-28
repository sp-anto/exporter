package gr.aueb.as.exporter.enumeration;

public enum ArgumentType {
    NO_ARGUMENT(0),
    ARGUMENT_REQUIRED(1),
    OPTIONAL_ARGUMENT(2);

    private int colons;

    ArgumentType(int colons) {
        this.colons = colons;
    }

    public int getColons() {
        return colons;
    }

    public static ArgumentType fromColonCount(int colons) {
        for (ArgumentType each : ArgumentType.values()) {
            if (each.getColons() == colons) {
                return each;
            }
        }
        return null;
    }
}
