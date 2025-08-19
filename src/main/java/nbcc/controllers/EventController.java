package nbcc.controllers;

import jakarta.validation.Valid;
import nbcc.entities.*;
import nbcc.enums.*;
import nbcc.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import static nbcc.utils.Utilities.setFieldError;

@Controller
public class EventController {

    private final LayoutService layoutService;
    private final EventService eventService;
    private final SeatingTimeServiceImpl seatingTimeService;
    private final MenuService menuService;
    private final ReservationRequestService reservationRequestService;

    public EventController(EventService eventService, SeatingTimeServiceImpl seatingTimeService,
                           LayoutService layoutService, MenuService menuService,
                           ReservationRequestService reservationRequestService) {

        this.eventService = eventService;
        this.seatingTimeService = seatingTimeService;
        this.layoutService = layoutService;
        this.menuService = menuService;
        this.reservationRequestService = reservationRequestService;

    }

    @GetMapping({"/", "/events"})
    public String getAll(Model model) {

        model.addAttribute("startDate", null);
        model.addAttribute("endDate", null);
        model.addAttribute("events", eventService.get());
        model.addAttribute("selectedFilter", EventDatesFilter.ALL.getName());
        model.addAttribute("eventFilters", EventDatesFilter.values());

        return "events/index";
    }


    @GetMapping({"/events/filter"})
    public String getAll(@RequestParam(required = false) String selectedFilter,
                         @ModelAttribute @RequestParam(required = false) LocalDate startDate,
                         @ModelAttribute @RequestParam(required = false) LocalDate endDate,
                         Model model) {

        if (selectedFilter.equals(EventDatesFilter.ALL.getName()))
            return "redirect:/events";

        startDate = startDate == null ? LocalDate.now() : startDate;
        endDate = endDate == null ? LocalDate.now().plusDays(1) : endDate;

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("events", eventService.filterEvents(selectedFilter, startDate, endDate));
        model.addAttribute("selectedFilter", selectedFilter);
        model.addAttribute("eventFilters", EventDatesFilter.values());

        return "events/index";
    }

    @GetMapping({"/event/create"})
    public String create(Model model) {
        model.addAttribute("event", new Event());
        model.addAttribute("seatingTime", new SeatingTime());
        model.addAttribute("layouts", layoutService.findAll());
        model.addAttribute("menus", menuService.getAllNonArchived());

        return "events/create";
    }

    @GetMapping({"/event/details/{id}"})
    public String get(@PathVariable long id, Model model) {
        var entity = eventService.get(id);

        if (entity.isPresent()) {
            model.addAttribute("event", entity.get());
            return "events/details";
        }

        return "redirect:/events";
    }

    @GetMapping({"/event/edit/{id}"})
    public String edit(@PathVariable long id, Model model) {
        var entity = eventService.get(id);

        if (entity.isPresent()) {
            var event = entity.get();

            // Get all non-archived layouts plus the current layout (even if it's archived)
            if (event.getLayout() != null) {
                model.addAttribute("layouts", layoutService.findAllActiveAndSpecific(event.getLayout().getId()));
            } else {
                model.addAttribute("layouts", layoutService.findAll());
            }

            model.addAttribute("event", event);
            model.addAttribute("menus", menuService.getMenuAndGetAllNonArchived(event.getMenu().getId()));

            return "events/edit";
        }

        return "redirect:/events";
    }

    @GetMapping({"/event/delete/{id}"})
    public String delete(@PathVariable long id, Model model) {
        var entity = eventService.get(id);

        if (entity.isPresent()) {
            model.addAttribute("event", entity.get());
            return "events/delete";
        }

        return "redirect:/events";
    }

    @Transactional
    @PostMapping({"/event/create"})
    public String create(@ModelAttribute @Valid Event event, BindingResult eventBindingResult,
                         @ModelAttribute @Valid SeatingTime seatingTime, BindingResult seatBindingResult, Model model) {
        try {
            if (eventService.isDatesFieldValid(eventBindingResult)) {
                eventService.ensureValidDateRange(event, eventBindingResult);

                if (!seatBindingResult.hasFieldErrors("startDateTime"))
                    seatingTimeService.ensureValidSeatingTime(seatingTime, event, seatBindingResult);
            }

            if (!eventBindingResult.hasErrors() && !seatBindingResult.hasErrors()) {
                event.addSeatingTime(seatingTime);
                eventService.create(event);
                return "redirect:/events";
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        model.addAttribute("layouts", layoutService.findAll());
        model.addAttribute("menus", menuService.getAllNonArchived());

        return "events/create";
    }

    @Transactional
    @PostMapping({"/event/edit/{id}"})
    public String edit(@PathVariable long id, @ModelAttribute @Valid Event event,
                       BindingResult bindingResult, Model model) {

        var entity = eventService.get(id);

        try {
            if (entity.isPresent() && id == event.getId()) {
                if (eventService.isDatesFieldValid(bindingResult)) {
                    eventService.ensureValidDateRange(event, bindingResult);

                    if (seatingTimeService.seatingTimeOutOfRange(event))
                        setFieldError("startDate", EventError.SEATING_TIME_CONFLICT.getMessage(), bindingResult);
                }

                if (!bindingResult.hasErrors()) {
                    eventService.update(event);
                    return "redirect:/events";
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        model.addAttribute("layouts", layoutService.findAll());
        model.addAttribute("menus", menuService.getMenuAndGetAllNonArchived(entity.get().getMenu().getId()));

        return "events/edit";
    }

    @Transactional
    @PostMapping({"/event/delete/{id}"})
    public String delete(@PathVariable long id) {
        try {
            var entity = eventService.get(id);

            if (entity.isPresent()) {
                var e = entity.get();

                boolean hasReservation = reservationRequestService.findAll().stream()
                        .anyMatch(req -> req.getSeating().getEvent().getId() == e.getId());

                if (hasReservation || e.getStartDate().isBefore(LocalDate.now()))
                    eventService.archive(id);
                else
                    eventService.delete(id);

                return "redirect:/events";
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return "events/delete/";
    }
}

