package nbcc.repositories;

import nbcc.entities.Layout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LayoutRepository extends JpaRepository<Layout, Long> {
    List<Layout> findByArchivedFalse();

    @Query("SELECT l FROM Layout l WHERE l.archived = false OR l.id = :layoutId")
    List<Layout> findActiveAndSpecificLayout(@Param("layoutId") Long layoutId);
}
