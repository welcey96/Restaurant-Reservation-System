package nbcc.dtos;

import nbcc.entities.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DtoConverters {
    public static EventDto toEventDto(Event e) {
        return new EventDto(
                e.getId(),
                e.getName(),
                e.getDescription(),
                e.getStartDate(),
                e.getEndDate(),
                e.getPrice(),
                e.isArchived(),
                e.getCreatedDate(),
                toSeatingTimeDto(e.getSeatingTimes()),
                toMenuDto(e.getMenu())
        );
    }

    public static List<EventDto> toEventDto(Iterable<Event> event) {
        var dto = new ArrayList<EventDto>();
        event.forEach(e -> dto.add(toEventDto(e)));
        return dto;
    }

    public static List<SeatingTimeDto> toSeatingTimeDto(Iterable<SeatingTime> seatingTimes) {
        var dto = new ArrayList<SeatingTimeDto>();
        seatingTimes.forEach(st -> dto.add(toSeatingTimeDto(st)));
        return dto;
    }

    public static SeatingTimeDto toSeatingTimeDto(SeatingTime st) {
        return new SeatingTimeDto(
                st.getId(),
                st.getStartDateTime(),
                st.endDateTime(),
                st.getDuration(),
                st.getCreatedDate()
        );
    }

    public static MenuDto toMenuDto(Menu m) {
        return new MenuDto(
                m.getId(),
                m.getName(),
                m.getDescription(),
                m.isArchived(),
                m.getCreatedDate()
        );
    }

    public static MenuWithItemsDto toMenuWithItemsDto(Menu m) {
        return new MenuWithItemsDto(
                m.getId(),
                m.getName(),
                m.getDescription(),
                m.isArchived(),
                m.getCreatedDate(),
                toMenuItemDto(m.getItems())
        );
    }

    public static List<MenuWithItemsDto> toMenusWithItemsDto(Menu m) {
        List<MenuWithItemsDto> list = new ArrayList<>();
        list.add(toMenuWithItemsDto(m));
        return list;
    }

    public static List<MenuItemDto> toMenuItemDto(Iterable<MenuItem> items) {
        var dto = new ArrayList<MenuItemDto>();
        items.forEach(i -> dto.add(toMenuItemDto(i)));
        return dto;
    }

    public static MenuItemDto toMenuItemDto(MenuItem i) {
        return new MenuItemDto(
                i.getId(),
                i.getName(),
                i.getDescription(),
                i.getCreatedDate()
        );
    }


    public static List<ValidationErrorDTO> toValidationErrors(BindingResult bindingResult) {
        return toValidationErrors(bindingResult.getFieldErrors());
    }

    public static List<ValidationErrorDTO> toValidationErrors(Collection<FieldError> errors) {
        return errors.stream()
                .map(fieldError ->
                        new ValidationErrorDTO(
                                fieldError.getField(),
                                fieldError.getDefaultMessage())
                ).toList();
    }

    public static ReservationRequest toReservationRequest(ReservationDto dto) {
        return new ReservationRequest(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getEmail(),
                dto.getGroupSize()
        );
    }

    public static ReservationDto toReservationDto(ReservationRequest rr, Long eventId) {
        return new ReservationDto(
                rr.getId(),
                eventId,
                rr.getSeating().getId(),
                rr.getFirstName(),
                rr.getLastName(),
                rr.getEmail(),
                rr.getGroupSize()
        );
    }
}
