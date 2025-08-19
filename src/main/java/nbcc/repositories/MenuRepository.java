package nbcc.repositories;

import nbcc.entities.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    //https://www.baeldung.com/spring-data-partial-update
    @Modifying
    @Query("UPDATE Menu m SET m.archived = :archived WHERE m.id = :id")
    int archive(@Param("id") Long id, @Param("archived") boolean archived);

    List<Menu> getMenusByArchivedFalse();

    Optional<Menu> findMenuByIdAndArchivedFalse(Long id);

    List<Menu> findMenuByIdOrArchivedFalse(Long id);
}
