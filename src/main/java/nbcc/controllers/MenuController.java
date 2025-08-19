package nbcc.controllers;

import jakarta.validation.Valid;
import nbcc.entities.Menu;
import nbcc.entities.MenuItem;
import nbcc.services.EventService;
import nbcc.services.MenuService;
import nbcc.services.RouteService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MenuController {
    private final MenuService menuService;
    private final RouteService routeService;
    private final EventService eventService;

    public MenuController(MenuService menuService, RouteService routeService, EventService eventService) {
        this.menuService = menuService;
        this.routeService = routeService;
        this.eventService = eventService;
    }

    @GetMapping({"/menus"})
    public String getAll(Model model) {
        model.addAttribute("menus", menuService.getAllNonArchived());
        model.addAttribute("item", new MenuItem());
        model.addAttribute("menu", new Menu());
        return "menus/index";
    }

    @GetMapping({"/menu/details/{id}"})
    public String get(@PathVariable long id, Model model) {
        var entity = menuService.getNonArchived(id);

        if (entity.isPresent()) {
            var menu = entity.get();
            model.addAttribute("items", menuService.getMenuItems(menu.getId()));
            model.addAttribute("menu", menu);
            model.addAttribute("item", new MenuItem(menu));
            return "menus/details";
        }

        return "redirect:/menus";
    }

    @GetMapping({"/menu/delete/{id}"})
    public String delete(@PathVariable long id, Model model) {
        var entity = menuService.getNonArchived(id);

        if (entity.isPresent()) {
            model.addAttribute("menu", entity.get());
            return "menus/delete";
        }

        return "redirect:/menus";
    }

    @PostMapping({"/menu/create"})
    public String create(@ModelAttribute @Valid Menu menu, BindingResult bindingResult) {
        if (!bindingResult.hasErrors())
            menuService.create(menu);

        if (routeService.getCurrentRoute().contains("event/create"))
            return "redirect:/event/create";

        return "redirect:/menus";
    }

    @PostMapping({"/menu/edit/{id}"})
    public String edit(@ModelAttribute @Valid Menu menu,
                       BindingResult bindingResult) {

        if (!bindingResult.hasErrors())
            menuService.update(menu);

        if (routeService.getCurrentRoute().contains("menu/details"))
            return "redirect:/menu/details/" + menu.getId();

        return "redirect:/menus";
    }

    @Transactional
    @PostMapping({"/menu/delete/{id}"})
    public String delete(@PathVariable long id) {
        try {
            var entity = menuService.getNonArchived(id);

            if (entity.isPresent()) {
                var events = eventService.findEventsWithMenu(id);
                if (events.isEmpty())
                    menuService.delete(id);
                else
                    menuService.archive(id);

                return "redirect:/menus";
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return "menus/delete";
    }

    @PostMapping({"/menu/{menuId}/item/add"})
    public String add(@PathVariable long menuId, @ModelAttribute @Valid MenuItem item) {
        var entity = menuService.getNonArchived(menuId);
        entity.ifPresent(menu -> menuService.addMenuItem(menu, item));

        if (routeService.getCurrentRoute().contains("/menu/details/"))
            return "redirect:/menu/details/" + menuId;

        return "redirect:/menus";
    }

    @PostMapping({"/menu/{menuId}/item/edit/{id}"})
    public String edit(@PathVariable long menuId, @PathVariable Long id, @ModelAttribute("item") @Valid MenuItem item,
                       BindingResult bindingResult) {

        if (!id.equals(item.getId()))
            return "redirect:/menu/details/" + menuId;

        var entity = menuService.getMenuItem(id);
        if (entity.isEmpty())
            return "redirect:/menus";

        var menuItemMenu = entity.get().getMenu();
        if (menuItemMenu.getId() != menuId)
            return "redirect:/menus";

        if (!bindingResult.hasErrors())
            menuService.updateMenuItem(item, menuItemMenu);

        return "redirect:/menu/details/" + menuId;
    }

    @PostMapping({"/menu/{menuId}/item/delete/{id}"})
    public String delete(@PathVariable long menuId, @PathVariable long id) {

        if (menuService.getMenuItem(id).isPresent())
            menuService.deleteMenuItem(id);

        return "redirect:/menu/details/" + menuId;
    }
}
