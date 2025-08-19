package nbcc.services;

import jakarta.persistence.EntityNotFoundException;
import nbcc.entities.Event;
import nbcc.enums.*;
import nbcc.repositories.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static nbcc.utils.Utilities.setFieldError;

@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> get() {
        return eventRepository.findEventsByArchived(false);
    }

    public Optional<Event> get(Long id) {
        return eventRepository.findEventByIdAndArchivedFalse(id);
    }

    public Optional<Event> getById(Long id){
        //include archived
        return eventRepository.findEventById(id);
    }

    public Event create(Event event) {
        return eventRepository.save(event);
    }

    public Event update(Event event) {
        var dbEvent = get(event.getId());

        if (dbEvent.isEmpty())
            throw new EntityNotFoundException("Event not found with id " + event.getId());

        var eventToUpdate = dbEvent.get();

        eventToUpdate.setName(event.getName());
        eventToUpdate.setDescription(event.getDescription());
        eventToUpdate.setStartDate(event.getStartDate());
        eventToUpdate.setEndDate(event.getEndDate());
        eventToUpdate.setPrice(event.getPrice());
        eventToUpdate.setMenu(event.getMenu());

        //capturing the changes that was made on layout
        eventToUpdate.setLayout(event.getLayout());
        return eventRepository.save(eventToUpdate);
    }

    public void delete(Long id) {
        get(id).orElseThrow(() -> new EntityNotFoundException("Event not found with id " + id));
        eventRepository.deleteById(id);
    }

    public List<Event> findEventsWithMenu(Long menuId) {
        return eventRepository.findEventsByMenuId(menuId);
    }

    public void archive(Long id) {
        get(id).orElseThrow(() -> new EntityNotFoundException("Event not found with id " + id));

        if (eventRepository.updateArchivedStatus(id, true) == 0)
            throw new EntityNotFoundException("Event not found with id " + id);
    }

    public void ensureValidDateRange(Event event, BindingResult bindingResult) {
        var start = event.getStartDate();
        var end = event.getEndDate();

        if (end.isBefore(start))
            setFieldError("endDate", EventError.END_DATE_BEFORE_START.getMessage(),
                    bindingResult);

        if (start.isAfter(end))
            setFieldError("startDate", EventError.START_DATE_BEFORE_END.getMessage(),
                    bindingResult);
    }

    public boolean isDatesFieldValid(BindingResult bindingResult) {
        return !bindingResult.hasFieldErrors("startDate") &&
                !bindingResult.hasFieldErrors("endDate");
    }

    public List<Event> filterEvents(String selectedFilter, LocalDate date1, LocalDate toDate) {
        var sf = selectedFilter.toLowerCase();
        if (sf.equals(EventDatesFilter.BEFORE.getName()))
            return eventRepository.findEventsByArchivedFalseAndStartDateBefore(date1);
        else if (sf.equals(EventDatesFilter.AFTER.getName()))
            return eventRepository.findEventsByArchivedFalseAndStartDateAfter(date1);
        else if (sf.equals(EventDatesFilter.BETWEEN.getName()))
            return eventRepository.findEventsByArchivedFalseAndStartDateBetween(date1, toDate);

        return eventRepository.findEventsByArchived(false);
    }
}
