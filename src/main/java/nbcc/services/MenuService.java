package nbcc.services;

import nbcc.entities.Menu;
import nbcc.entities.MenuItem;

import java.util.List;
import java.util.Optional;

public interface MenuService {
    Menu create(Menu menu);

    List<Menu> get();

    Optional<Menu> get(Long id);

    List<Menu> getAllNonArchived();

    Optional<Menu> getNonArchived(Long id);

    Menu update(Menu menu);

    void delete(Long id);

    void archive(Long id);

    List<MenuItem> getMenuItems(Long id);

    Optional<MenuItem> getMenuItem(Long id);

    List<Menu> getMenuAndGetAllNonArchived(Long id);

    void addMenuItem(Menu menu, MenuItem item);

    void updateMenuItem(MenuItem item, Menu menu);

    void deleteMenuItem(Long id);

}
