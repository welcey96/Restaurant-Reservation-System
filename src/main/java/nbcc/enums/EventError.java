package nbcc.enums;

public enum EventError {
    START_DATE_BEFORE_END("Start Date cannot be before the end date"),
    END_DATE_BEFORE_START("End Date cannot be before the start date"),
    SEATING_TIME_CONFLICT("Date conflicts with an existing seating time for this event");

    private final String message;

    EventError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
