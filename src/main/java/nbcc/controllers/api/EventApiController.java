package nbcc.controllers.api;

import javassist.NotFoundException;
import nbcc.dtos.EventDto;
import nbcc.dtos.MenuWithItemsDto;
import nbcc.services.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static nbcc.dtos.DtoConverters.toEventDto;
import static nbcc.dtos.DtoConverters.toMenusWithItemsDto;

@RestController
@RequestMapping("/api")
public class EventApiController extends BaseController {
    private final EventService eventService;

    public EventApiController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventDto>> getAll() {
        return ResponseEntity.ok(toEventDto(eventService.get()));
    }

    @GetMapping("/event/{id}")
    public ResponseEntity<EventDto> get(@PathVariable long id) throws NotFoundException {
        var event = eventService.getById(id);
        return event.map(e -> ResponseEntity.ok(toEventDto(e)))
                .orElseThrow(() -> new NotFoundException("Event not found"));
    }

    @GetMapping("/event/{id}/menus")
    public ResponseEntity<List<MenuWithItemsDto>> getAll(@PathVariable long id) throws NotFoundException {
        var event = eventService.getById(id);
        return event.map(e -> ResponseEntity.ok(toMenusWithItemsDto(e.getMenu())))
                .orElseThrow(() -> new NotFoundException("Event not found"));
    }
}
