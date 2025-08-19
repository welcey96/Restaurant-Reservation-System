package nbcc.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ReservationDto {
    private Long id = 0L;

    @NotNull(message = "Event is required")
    private Long eventId;

    @NotNull(message = "Seating Time is required")
    private Long seatingTimeId;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Group size is required")
    @Positive(message = "Group size must be greater than 0")
    private Integer groupSize;

    public ReservationDto() {
    }

    public ReservationDto(Long eventId, Long seatingTimeId,
                          String firstName, String lastName,
                          String email, Integer groupSize) {

        this.eventId = eventId;
        this.seatingTimeId = seatingTimeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.groupSize = groupSize;
    }

    public ReservationDto(Long id, Long eventId, Long seatingTimeId, String firstName,
                          String lastName, String email, Integer groupSize) {

        this(eventId, seatingTimeId, firstName, lastName, email, groupSize);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getSeatingTimeId() {
        return seatingTimeId;
    }

    public void setSeatingTimeId(Long seatingTimeId) {
        this.seatingTimeId = seatingTimeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(Integer groupSize) {
        this.groupSize = groupSize;
    }
}
