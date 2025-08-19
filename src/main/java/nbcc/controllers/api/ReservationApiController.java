package nbcc.controllers.api;

import jakarta.validation.Valid;
import javassist.NotFoundException;
import nbcc.dtos.ReservationDto;
import nbcc.services.ReservationRequestService;
import nbcc.services.SeatingTimeServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static nbcc.dtos.DtoConverters.toReservationDto;
import static nbcc.dtos.DtoConverters.toReservationRequest;

@RestController
@RequestMapping("/api")
public class ReservationApiController extends BaseController {
    private final SeatingTimeServiceImpl seatingTimeService;
    private final ReservationRequestService reservationService;

    public ReservationApiController(SeatingTimeServiceImpl seatingTimeService,
                                    ReservationRequestService reservationService) {
        this.seatingTimeService = seatingTimeService;
        this.reservationService = reservationService;
    }

    @PostMapping("/reservation/create")
    public ResponseEntity<ReservationDto> create(@RequestBody @Valid ReservationDto reservationDto) throws NotFoundException {
        var seatingTime = seatingTimeService.get(reservationDto.getSeatingTimeId());
        seatingTime.orElseThrow(() -> new NotFoundException("Seating time not found"));

        if (seatingTime.get().getEvent().getId() != reservationDto.getEventId())
            throw new NotFoundException("Seating time not found for the given event");

        var dto = toReservationDto(
                reservationService.save(toReservationRequest(reservationDto), seatingTime.get()),
                seatingTime.get().getEvent().getId()
        );

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
}
