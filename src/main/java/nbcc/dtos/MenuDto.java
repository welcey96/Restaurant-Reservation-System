package nbcc.dtos;

import java.time.LocalDateTime;

public class MenuDto {
    private long id;
    private String name;
    private String description;
    private boolean archived;
    private LocalDateTime createdAt;

    public MenuDto(long id, String name, String description, boolean archived, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.archived = archived;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
