package nbcc.controllers.api;

import javassist.NotFoundException;
import nbcc.dtos.MenuWithItemsDto;
import nbcc.services.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static nbcc.dtos.DtoConverters.toMenuWithItemsDto;

@RestController
@RequestMapping("/api")
public class MenuApiController extends BaseController {

    private final MenuService menuService;

    public MenuApiController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/menu/{id}")
    public ResponseEntity<MenuWithItemsDto> get(@PathVariable long id) throws NotFoundException {
        var menu = menuService.get(id);
        return menu.map(m -> ResponseEntity.ok(toMenuWithItemsDto(m)))
                .orElseThrow(() -> new NotFoundException("Menu not found"));
    }
}
