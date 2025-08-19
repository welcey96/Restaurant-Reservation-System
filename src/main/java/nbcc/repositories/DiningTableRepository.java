package nbcc.repositories;

import nbcc.entities.DiningTable;
import nbcc.entities.Event;
import nbcc.entities.Layout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiningTableRepository extends JpaRepository<DiningTable, Long> {
    @Query("SELECT dt FROM DiningTable dt JOIN dt.reservationRequest rr WHERE rr.seating.event = :event")
    List<DiningTable> findTablesWithReservationsForEvent(@Param("event") Event event);

    @Query("SELECT dt FROM DiningTable dt " +
            "WHERE dt.layout IN (SELECT l FROM Layout l JOIN l.events e WHERE e = :event) " +
            "AND dt.archived = false " +
            "AND dt NOT IN (SELECT rr.diningTable FROM ReservationRequest rr WHERE rr.diningTable IS NOT NULL AND rr.status = 'APPROVED')")
    List<DiningTable> findAvailableTablesForEvent(@Param("event") Event event);

    @Query("SELECT dt FROM DiningTable dt WHERE dt.layout = :layout AND dt.archived = false AND dt NOT IN (SELECT r.diningTable FROM ReservationRequest r WHERE r.status = 'APPROVED')")
    List<DiningTable> findUnassignedTablesForLayout(@Param("layout") Layout layout);

    @Query("SELECT dt FROM DiningTable dt WHERE dt.layout = :layout AND dt.archived = false AND dt NOT IN (SELECT r.diningTable FROM ReservationRequest r WHERE r.status = 'APPROVED' AND r.seating.event = :event)")
    List<DiningTable> findAvailableTablesForEvent(@Param("layout") Layout layout, @Param("event") Event event);

    @Query("SELECT dt FROM DiningTable dt WHERE dt.archived = true")
    List<DiningTable> findAllArchivedTables();

    @Query("SELECT dt FROM DiningTable dt WHERE dt.layout = :layout AND dt.archived = true")
    List<DiningTable> findArchivedTablesByLayout(@Param("layout") Layout layout);

    @Query("SELECT dt FROM DiningTable dt WHERE dt.archived = false")
    List<DiningTable> findAllActiveTables();
}
