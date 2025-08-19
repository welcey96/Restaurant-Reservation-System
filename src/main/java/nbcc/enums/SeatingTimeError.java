package nbcc.enums;

public enum SeatingTimeError {
    SEATING_TIME_OUT_OF_RANGE("Seating Date and Time must be within the event's date range"),
    SEATING_TIME_CONFLICT("Seating Date and Time conflicts with an existing seating time");

    private final String message;

    SeatingTimeError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
