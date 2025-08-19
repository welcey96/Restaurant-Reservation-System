package nbcc.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Layout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Date is required")
    private LocalDate createdDate = LocalDate.now();

    @UpdateTimestamp
    private LocalDateTime lastUpdated = LocalDateTime.now();

    @OneToMany(mappedBy = "layout", fetch = FetchType.LAZY,  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiningTable> diningTables;

    private boolean archived = false;

    @OneToMany(mappedBy = "layout", fetch = FetchType.LAZY)
    private List<Event> events = new ArrayList<>();

    public Layout() {
    }

    public Layout(String name, String description, LocalDateTime createdDate) {
        this.name = name;
        this.description = description;
        this.createdDate = LocalDate.now();
        this.lastUpdated = LocalDateTime.now();
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public List<DiningTable> getDiningTables() {
        return diningTables;
    }

    public void setDiningTables(List<DiningTable> diningTables) {
        this.diningTables = diningTables;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
    //Helper method to add
    public void addDiningTable(DiningTable diningTable) {
        diningTables.add(diningTable);
        diningTable.setLayout(this);
    }

    // Helper method to remove a dining table from this layout
    public void removeDiningTable(DiningTable diningTable) {
        diningTables.remove(diningTable);
        diningTable.setLayout(null);
    }

    //helper method to add the event and remove
    public void addEvent(Event event) {
        events.add(event);
        event.setLayout(this);
    }

    public void removeEvent(Event event) {
        events.remove(event);
        event.setLayout(null);
    }




}
