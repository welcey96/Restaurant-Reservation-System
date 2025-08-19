package nbcc.services;

import jakarta.persistence.EntityNotFoundException;
import nbcc.entities.Menu;
import nbcc.entities.MenuItem;
import nbcc.repositories.MenuItemRepository;
import nbcc.repositories.MenuRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;
    private final MenuItemRepository menuItemRepository;

    public MenuServiceImpl(MenuRepository menuRepository, MenuItemRepository menuItemRepository) {
        this.menuRepository = menuRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public Menu create(Menu menu) {
        return menuRepository.save(menu);
    }

    @Override
    public List<Menu> get() {
        return menuRepository.findAll();
    }

    @Override
    public Optional<Menu> get(Long id) {
        return menuRepository.findById(id);
    }

    @Override
    public List<Menu> getAllNonArchived() {
        return menuRepository.getMenusByArchivedFalse();
    }

    @Override
    public Optional<Menu> getNonArchived(Long id) {
       return menuRepository.findMenuByIdAndArchivedFalse(id);
    }

    @Override
    public List<Menu> getMenuAndGetAllNonArchived(Long id) {
        return menuRepository.findMenuByIdOrArchivedFalse(id);
    }

    @Override
    public Menu update(Menu menu) {
        var db = get(menu.getId());

        if (db.isEmpty())
            throw new EntityNotFoundException("Menu not found with id " + menu.getId());

        menu.setItems(db.get().getItems());
        return menuRepository.save(menu);
    }

    @Override
    public void delete(Long id) {
        get(id).orElseThrow(() -> new EntityNotFoundException("Menu not found with id " + id));
        menuRepository.deleteById(id);
    }

    @Override
    public void archive(Long id) {
        this.get(id).orElseThrow(() -> new EntityNotFoundException("Menu not found with id " + id));

        if (menuRepository.archive(id, true) == 0)
            throw new EntityNotFoundException("Menu not archived with id " + id);
    }

    @Override
    public void addMenuItem(Menu menu, MenuItem item) {
        item.setMenu(menu);
        menuItemRepository.save(item);
    }

    @Override
    public List<MenuItem> getMenuItems(Long id) {
        return menuItemRepository.findAllByMenuId(id);
    }

    @Override
    public Optional<MenuItem> getMenuItem(Long id) {
        return menuItemRepository.getMenuItemById(id);
    }

    @Override
    public void updateMenuItem(MenuItem item, Menu menu) {
        item.setMenu(menu);
        menuItemRepository.save(item);
    }

    @Override
    public void deleteMenuItem(Long id) {
        menuItemRepository.deleteById(id);
    }
}
