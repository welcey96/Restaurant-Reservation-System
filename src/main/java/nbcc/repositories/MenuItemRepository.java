package nbcc.repositories;

import nbcc.entities.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findAllByMenuId(Long id);

    Optional<MenuItem> getMenuItemById(Long id);
}
