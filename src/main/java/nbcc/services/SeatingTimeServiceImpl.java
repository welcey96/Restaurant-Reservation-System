package nbcc.services;

import jakarta.persistence.EntityNotFoundException;
import nbcc.entities.*;
import nbcc.enums.*;
import nbcc.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

import static nbcc.utils.Utilities.setFieldError;

@Service
public class SeatingTimeServiceImpl {

    private final SeatingTimeRepository seatingTimeRepository;
    private final EventRepository eventRepository;

    public SeatingTimeServiceImpl(SeatingTimeRepository seatingTimeRepository, EventRepository eventRepository) {
        this.seatingTimeRepository = seatingTimeRepository;
        this.eventRepository = eventRepository;
    }

    public void create(SeatingTime seatingTime, Event event) {
        seatingTime.setEvent(event);
        seatingTimeRepository.save(seatingTime);
    }

    public SeatingTime update(SeatingTime seatingTime) {
        var dbSt = get(seatingTime.getId());
        if (dbSt.isEmpty())
            throw new EntityNotFoundException("Seating time not found with id " + seatingTime.getId());

        var stToUpdate = dbSt.get();
        stToUpdate.setDuration(seatingTime.getDuration());
        stToUpdate.setStartDateTime(seatingTime.getStartDateTime());

        return seatingTimeRepository.save(stToUpdate);
    }

    public Optional<SeatingTime> get(Long id) {
        return seatingTimeRepository.findById(id);
    }

    public List<SeatingTime> getByEventId(Long eventId) {
        return seatingTimeRepository.findSeatingTimesByEvent_Id(eventId);
    }

    //https://stackoverflow.com/questions/22688402/delete-not-working-with-jparepository
    public void delete(Event event, Long seatingTimeId) {
        event.getSeatingTimes().removeIf(st -> st.getId() == seatingTimeId);
        eventRepository.save(event);
    }

    public boolean ensureValidSeatingTime(SeatingTime seatingTime, Event event, BindingResult bindingResult) {
        if (seatingTimeOutOfRange(seatingTime, event)) {
            setFieldError("startDateTime", SeatingTimeError.SEATING_TIME_OUT_OF_RANGE.getMessage(), bindingResult);
            return false;
        } else if (hasSeatingTimeConflict(seatingTime, event.getId())) {
            setFieldError("startDateTime", SeatingTimeError.SEATING_TIME_CONFLICT.getMessage(), bindingResult);
            return false;
        }

        return true;
    }

    public boolean seatingTimeOutOfRange(Event event) {
        var sts = getByEventId(event.getId());
        if (!sts.isEmpty()) {
            for (var st : sts) {
                if (seatingTimeOutOfRange(st, event))
                    return true;
            }
        }

        return false;
    }

    private boolean hasSeatingTimeConflict(SeatingTime seatingTime, Long eventId) {
        var sts = getByEventId(eventId);

        if (!sts.isEmpty()) {
            var start = seatingTime.getStartDateTime();
            var end = start.plusMinutes(seatingTime.getDuration());

            for (var st : sts) {
                var existingST = st.getStartDateTime();

                if (!start.isAfter(existingST.plusMinutes(st.getDuration())) &&
                        !end.isBefore(existingST) && st.getId() != seatingTime.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean seatingTimeOutOfRange(SeatingTime st, Event event) {
        var stDateTime = st.getStartDateTime();
        var stDateTimeDuration = stDateTime.plusMinutes(st.getDuration());
        return stDateTime.toLocalDate().isBefore(event.getStartDate()) ||
                stDateTime.toLocalDate().isAfter(event.getEndDate()) ||
                stDateTimeDuration.toLocalDate().isAfter(event.getEndDate());
    }

    //added
    public List<SeatingTime> findAll() {
        return seatingTimeRepository.findAll();
    }
}
