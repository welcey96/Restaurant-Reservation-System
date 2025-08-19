package nbcc.repositories;

import nbcc.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    //https://www.baeldung.com/spring-data-partial-update
    @Modifying
    @Query("UPDATE Event e SET e.archived = :archived WHERE e.id = :id")
    int updateArchivedStatus(@Param("id") Long id, @Param("archived") boolean archived);

    List<Event> findEventsByArchived(boolean archived);

    // https://stackoverflow.com/questions/15359306/how-to-fetch-fetchtype-lazy-associations-with-jpa-and-hibernate-in-a-spring-cont
    @Query("SELECT e FROM Event e " +
            "LEFT JOIN FETCH e.menu " +
            "LEFT JOIN FETCH e.seatingTimes " +
            "WHERE e.id = :id AND e.archived = false")
    Optional<Event> findEventByIdAndArchivedFalse(@Param("id") Long id);

    List<Event> findEventsByArchivedFalseAndStartDateBefore(LocalDate date);

    List<Event> findEventsByArchivedFalseAndStartDateAfter(LocalDate date);

    List<Event> findEventsByArchivedFalseAndStartDateBetween(LocalDate fromDate, LocalDate toDate);

    Optional<Event> findEventById(Long id);

    List<Event> findEventsByMenuId(Long menuId);
}
