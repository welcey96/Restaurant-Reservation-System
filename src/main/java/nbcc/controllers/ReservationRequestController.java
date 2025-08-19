package nbcc.controllers;

import jakarta.validation.Valid;
import nbcc.entities.ReservationRequest;
import nbcc.enums.ReservationStatus;
import nbcc.services.DiningTableService;
import nbcc.services.EventService;
import nbcc.services.ReservationRequestService;

import nbcc.services.SeatingTimeServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReservationRequestController {
    private final ReservationRequestService reservationRequestService;
    private final SeatingTimeServiceImpl seatingTimeService;
    private final EventService eventService;
    private final DiningTableService diningTableService;


    public ReservationRequestController(ReservationRequestService reservationRequestService,
                                        SeatingTimeServiceImpl seatingTimeService,
                                        EventService eventService,
                                        DiningTableService diningTableService) {
        this.reservationRequestService = reservationRequestService;
        this.seatingTimeService = seatingTimeService;
        this.eventService = eventService;
        this.diningTableService = diningTableService;
    }

    @GetMapping("/reservation/create")
    public String create(Model model) {
        model.addAttribute("reservationRequest", new ReservationRequest());
        model.addAttribute("seatingTimes", seatingTimeService.findAll());
        return "reservationRequest/create";
    }

    @PostMapping("/reservation/create")
    public String create(@Valid @ModelAttribute ReservationRequest reservationRequest,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("seatingTimes", seatingTimeService.findAll());
            return "reservationRequest/create";
        }
//        reservationRequestService.save(reservationRequest);
//        return "redirect:/reservation/list";
        try {
            //redirect guest to a success page
            ReservationRequest savedReservation = reservationRequestService.save(reservationRequest);
            return "redirect:/reservation/confirmation/" + savedReservation.getId();
        } catch (Exception e) {
            model.addAttribute("error", "Failed to create reservation. Please try again.");
            model.addAttribute("seatingTimes", seatingTimeService.findAll());
            return "reservationRequest/create";
        }
    }
    @GetMapping("/reservation/confirmation/{id}")
    public String showConfirmation(@PathVariable Long id, Model model) {
        var reservationOpt = reservationRequestService.findById(id);
        if (reservationOpt.isPresent()) {
            model.addAttribute("reservation", reservationOpt.get());
            model.addAttribute("message", "Your reservation has been confirmed successfully!");
            return "reservationRequest/confirmation";
        }
        return "redirect:/reservation/list";
    }

    @GetMapping("/reservation/list")
    public String list(@RequestParam(required = false) String selectedFilter,
                       @RequestParam(required = false) Long eventId,
                       Model model) {
        if (selectedFilter == null) {
            selectedFilter = ReservationStatus.ALL.getName();
        }

        model.addAttribute("reservations", reservationRequestService.filterReservations(selectedFilter, eventId));
        model.addAttribute("selectedFilter", selectedFilter);
        model.addAttribute("selectedEventId", eventId);
        model.addAttribute("reservationFilters", ReservationStatus.values());
        model.addAttribute("events", eventService.get());
        return "reservationRequest/list";
    }

    @GetMapping("/reservation/details/{id}")
    public String details(@PathVariable Long id, Model model) {
        var reservationOpt = reservationRequestService.findById(id);
        if (reservationOpt.isPresent()) {
            var reservation = reservationOpt.get();
            model.addAttribute("reservation", reservation);
            // Add available tables for the event
            if (reservation.getSeating() != null && reservation.getSeating().getEvent() != null) {
                var event = reservation.getSeating().getEvent();
                // Get tables that are not assigned to any reservation
                model.addAttribute("reservedTables",
                        diningTableService.findAvailableTablesForEvent(event));
            }
            return "reservationRequest/detail";
        }
        return "redirect:/reservation/list";
    }
    @GetMapping("/reservation/event/{eventId}")
    public String viewEventReservations(@PathVariable Long eventId, Model model) {
        var eventOpt = eventService.get(eventId);
        if (eventOpt.isEmpty()) {
            return "redirect:/events";
        }

        var event = eventOpt.get();
        var reservations = reservationRequestService.filterReservations(ReservationStatus.APPROVED.name(), eventId);

        model.addAttribute("event", event);
        model.addAttribute("reservations", reservations);

        return "reservationRequest/eventReservations";
    }

    @PostMapping("/reservation/approve/{id}")
    public String approve(@PathVariable Long id, @RequestParam(required = false) Long tableId, Model model) {
        var reservationOpt = reservationRequestService.findById(id);
        if (reservationOpt.isEmpty()) {
            return "redirect:/reservation/list";
        }

        var reservation = reservationOpt.get();

        if (tableId == null) {
            model.addAttribute("error", "Please select a table to approve the reservation");
            return details(id, model);
        }

        if (reservation.getStatus() == ReservationStatus.APPROVED) {
            model.addAttribute("error", "Cannot change status of an approved reservation");
            return details(id, model);
        }

        if (reservationRequestService.approveReservation(id, tableId)) {
            return "redirect:/reservation/details/" + id;
        }

        // Get the table to check if it's archived
        var tableOpt = diningTableService.findById(tableId);
        if (tableOpt.isPresent() && tableOpt.get().isArchived()) {
            model.addAttribute("error", "Cannot assign an archived table to a reservation");
        } else {
            model.addAttribute("error", "Failed to approve reservation. The table might be already assigned to another reservation.");
        }
        return details(id, model);
    }

    @PostMapping("/reservation/deny/{id}")
    public String deny(@PathVariable Long id, Model model) {
        var reservationOpt = reservationRequestService.findById(id);
        if (reservationOpt.isEmpty()) {
            return "redirect:/reservation/list";
        }

        var reservation = reservationOpt.get();

        if (reservation.getStatus() == ReservationStatus.APPROVED) {
            model.addAttribute("error", "Cannot change status of an approved reservation");
            return details(id, model);
        }

        if (reservationRequestService.denyReservation(id)) {
            return "redirect:/reservation/details/" + id;
        }
        return "redirect:/reservation/list";
    }

}
