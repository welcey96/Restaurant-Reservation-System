package nbcc.repositories;

import nbcc.entities.DiningTable;
import nbcc.entities.Event;
import nbcc.entities.ReservationRequest;
import nbcc.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRequestRepository extends JpaRepository<ReservationRequest, Long> {

    @Query("SELECT r FROM ReservationRequest r ORDER BY r.seating.startDateTime ASC")
    List<ReservationRequest> findAllOrderByStartDateTime();

    @Query("SELECT r FROM ReservationRequest r WHERE r.status = :status ORDER BY r.seating.startDateTime ASC")
    List<ReservationRequest> findByStatusOrderByStartDateTime(@Param("status") ReservationStatus status);

    @Query("SELECT r FROM ReservationRequest r WHERE r.seating.event.id = :eventId ORDER BY r.seating.startDateTime ASC")
    List<ReservationRequest> findByEventIdOrderByStartDateTime(@Param("eventId") Long eventId);

    @Query("SELECT r FROM ReservationRequest r WHERE r.seating.event.id = :eventId AND r.status = :status ORDER BY r.seating.startDateTime ASC")
    List<ReservationRequest> findByEventIdAndStatusOrderByStartDateTime(@Param("eventId") Long eventId, @Param("status") ReservationStatus status);

    boolean existsByDiningTableAndSeating_Event(DiningTable diningTable, Event event);
}
