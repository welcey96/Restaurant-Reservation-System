package nbcc.repositories;

import nbcc.entities.SeatingTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatingTimeRepository extends JpaRepository<SeatingTime, Long> {
    List<SeatingTime> findSeatingTimesByEvent_Id(long eventId);
}
