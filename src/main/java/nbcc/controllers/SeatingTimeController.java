package nbcc.controllers;

import jakarta.validation.Valid;
import nbcc.entities.*;
import nbcc.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class SeatingTimeController {
    private final SeatingTimeServiceImpl seatingTimeService;
    private final EventService eventService;

    public SeatingTimeController(SeatingTimeServiceImpl seatingTimeService, EventService eventService) {
        this.seatingTimeService = seatingTimeService;
        this.eventService = eventService;
    }

    @GetMapping({"/seatingtime/event/{eventId}/create"})
    public String create(@PathVariable Long eventId, Model model) {
        var entity = eventService.get(eventId);

        if (entity.isPresent()) {
            model.addAttribute("event", entity.get());
            model.addAttribute("seatingTime", new SeatingTime());
            return "seatingTimes/create";
        }

        return "redirect:/events";
    }

    @GetMapping({"/seatingtime/event/{eventId}/edit/{id}"})
    public String edit(@PathVariable long id, @PathVariable long eventId, Model model) {
        var entity = seatingTimeService.get(id);

        if (eventService.get(eventId).isPresent() && entity.isPresent()) {
            var st = entity.get();
            if (st.getEvent().getId() == eventId) {
                model.addAttribute("seatingTime", st);
                return "seatingTimes/edit";
            }
        }

        return "redirect:/events";
    }

    @Transactional
    @PostMapping({"/seatingtime/event/{eventId}/create"})
    public String create(@PathVariable long eventId, @ModelAttribute @Valid SeatingTime seatingTime,
                         BindingResult bindingResult, Model model) {
        var entity = eventService.get(eventId);

        if (entity.isEmpty())
            return "redirect:/events";

        try {
            var event = entity.get();
            if (!bindingResult.hasErrors() &&
                    seatingTimeService.ensureValidSeatingTime(seatingTime, event, bindingResult)) {

                seatingTimeService.create(seatingTime, event);
                return "redirect:/event/details/{eventId}";
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        model.addAttribute("event", entity.get());
        return "seatingTimes/create";
    }

    @PostMapping({"/seatingtime/event/{eventId}/edit/{id}"})
    public String update(@PathVariable long id, @PathVariable long eventId,
                         @ModelAttribute @Valid SeatingTime seatingTime,
                         BindingResult bindingResult) {

        var stEntity = seatingTimeService.get(id);
        var eEntity = eventService.get(eventId);

        if (eEntity.isPresent() && stEntity.isPresent()) {
            var st = stEntity.get();

            if (st.getId() != seatingTime.getId())
                return "redirect:/event/details/{eventId}";

            if (st.getEvent().getId() != eventId)
                return "redirect:/events";

            if (!bindingResult.hasErrors() &&
                    seatingTimeService.ensureValidSeatingTime(seatingTime, st.getEvent(), bindingResult)) {

                seatingTimeService.update(seatingTime);
                return "redirect:/event/details/{eventId}";
            }

            return "seatingTimes/edit";
        }

        if (eEntity.isPresent())
            return "redirect:/event/details/{eventId}";

        return "redirect:/events";
    }

    @Transactional
    @PostMapping({"/seatingtime/event/{id}/delete/{seatingTimeId}"})
    public String delete(@PathVariable long id, @PathVariable long seatingTimeId) {
        try {
            var entity = eventService.get(id);

            if (entity.isPresent()) {
                var st = seatingTimeService.get(seatingTimeId);
                if (st.isPresent() && st.get().getEvent().getId() == id)
                    seatingTimeService.delete(entity.get(), seatingTimeId);

                return "redirect:/event/details/{id}";
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return "redirect:/events";
    }
}
