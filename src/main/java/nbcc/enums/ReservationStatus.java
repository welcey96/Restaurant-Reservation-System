package nbcc.enums;

public enum ReservationStatus {
    ALL("All"),
    PENDING("Pending"),
    APPROVED("Approved"),
    DENIED("Denied");

    private final String name;

    ReservationStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name.toLowerCase();
    }
}
