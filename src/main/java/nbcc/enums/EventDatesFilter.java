package nbcc.enums;

public enum EventDatesFilter {
    ALL("All"),
    BEFORE("Before"),
    AFTER("After"),
    BETWEEN("Between");

    private final String name;

    EventDatesFilter(String name) {
        this.name = name;
    }

    public String getName() {
        return name.toLowerCase();
    }
}

