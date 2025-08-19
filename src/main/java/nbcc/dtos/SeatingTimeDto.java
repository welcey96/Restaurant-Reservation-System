package nbcc.dtos;

import java.time.LocalDateTime;

public class SeatingTimeDto {
    private long id;
    private LocalDateTime startDateTime;
    private int duration;

    private LocalDateTime endDateTime;
    private LocalDateTime createdAt;

    public SeatingTimeDto(long id, LocalDateTime startDateTime, LocalDateTime endDateTime,
                          int duration, LocalDateTime createdAt) {
        this.id = id;
        this.startDateTime = startDateTime;
        this.duration = duration;
        this.endDateTime = endDateTime;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
