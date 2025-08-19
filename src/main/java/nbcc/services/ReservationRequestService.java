package nbcc.services;

import nbcc.config.EmailSenderConfig;
import nbcc.entities.ReservationRequest;
import nbcc.entities.SeatingTime;
import nbcc.enums.ReservationStatus;
import nbcc.repositories.ReservationRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationRequestService {
    private final ReservationRequestRepository reservationRequestRepository;
    private final EmailSenderService emailSenderService;
    private final EmailSenderConfig emailSenderConfig;
    private final DiningTableService diningTableService;

    public ReservationRequestService(ReservationRequestRepository reservationRequestRepository,
                                     EmailSenderService emailSenderService,
                                     EmailSenderConfig emailSenderConfig,
                                     DiningTableService diningTableService) {
        this.reservationRequestRepository = reservationRequestRepository;
        this.emailSenderService = emailSenderService;
        this.emailSenderConfig = emailSenderConfig;
        this.diningTableService = diningTableService;
    }

    public List<ReservationRequest> findAll() {
        return reservationRequestRepository.findAll();
    }

    public Optional<ReservationRequest> findById(Long id) {
        return reservationRequestRepository.findById(id);
    }

    public ReservationRequest save(ReservationRequest reservationRequest) {
        // reservationRequestRepository.save(reservationRequest);
        ReservationRequest savedRequest = reservationRequestRepository.save(reservationRequest);
        sendReservationConfirmation(savedRequest);
        return savedRequest;
    }

    public ReservationRequest save(ReservationRequest reservationRequest, SeatingTime seatingTime) {
        if (seatingTime != null)
            reservationRequest.setSeating(seatingTime);

        return reservationRequestRepository.save(reservationRequest);
    }

    public void delete(Long id) {
        reservationRequestRepository.deleteById(id);
    }


    private void sendReservationConfirmation(ReservationRequest reservation) {
        try {
            String subject;
            switch (reservation.getStatus().toString()) {
                case "APPROVED":
                    subject = "Reservation Approved";
                    break;
                case "DENIED":
                    subject = "Reservation Denied";
                    break;
                default:
                    subject = "Reservation Confirmation";
                    break;
            }

            String text = String.format(
                    "Dear %s %s,\n\n" +
                            "Your reservation has been %s!\n\n" +
                            "Reservation Details:\n" +
                            "Name: %s %s\n" +
                            "Event: %s\n" +
                            "Date: %s\n" +
                            "Time: %s\n" +
                            "Group Size: %d\n" +
                            "Status: %s\n\n" +
                            "Thank you for choosing our service!\n" +
                            "We will contact you if there are any changes to your reservation.",
                    reservation.getFirstName(),
                    reservation.getLastName(),
                    reservation.getStatus().toString().toLowerCase(), // "approved" or "denied"
                    reservation.getFirstName(),
                    reservation.getLastName(),
                    reservation.getSeating().getEvent().getName(),
                    reservation.getSeating().getStartDateTime().format(java.time.format.DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")),
                    reservation.getSeating().getStartDateTime().format(java.time.format.DateTimeFormatter.ofPattern("h:mm a")),
                    reservation.getGroupSize(),
                    reservation.getStatus().toString()
            );

            emailSenderService.sendEmail(
                    subject,
                    text,
                    emailSenderConfig.getDefaultFrom(),
                    reservation.getEmail()
            );
        } catch (Exception e) {
            System.err.println("Failed to send reservation confirmation email: " + e.getMessage());
            e.printStackTrace();
        }
    }


    //filter condition

    public List<ReservationRequest> filterReservations(String selectedFilter, Long eventId) {
        // If no filter is selected or filter is ALL, return all reservation
        if (selectedFilter == null || selectedFilter.equalsIgnoreCase("ALL")) {
            if (eventId != null) {
                return reservationRequestRepository.findByEventIdOrderByStartDateTime(eventId);
            } else {
                return reservationRequestRepository.findAllOrderByStartDateTime();
            }
        }

        // For specific status filters, convert to ReservationStatus
        try {
            ReservationStatus status = ReservationStatus.valueOf(selectedFilter.toUpperCase());
            if (eventId != null) {
                return reservationRequestRepository.findByEventIdAndStatusOrderByStartDateTime(eventId, status);
            } else {
                return reservationRequestRepository.findByStatusOrderByStartDateTime(status);
            }
        } catch (IllegalArgumentException e) {
            // If invalid status, return records
            return findAll();
        }
    }

    //added part
    @Transactional
    public boolean denyReservation(Long reservationId) {
        var reservationOpt = findById(reservationId);

        if (reservationOpt.isEmpty()) {
            return false;
        }

        var reservation = reservationOpt.get();

        // Prevent status change if already approved
        if (reservation.getStatus() == ReservationStatus.APPROVED) {
            return false;
        }

        // If there's an assigned table, unlink it
        if (reservation.getDiningTable() != null) {
            var table = reservation.getDiningTable();
            table.setReservationRequest(null);
            reservation.setDiningTable(null);
            diningTableService.save(table);
        }

        reservation.setStatus(ReservationStatus.DENIED);
        save(reservation);
        return true;
    }

    @Transactional
    public boolean approveReservation(Long reservationId, Long tableId) {
        var reservationOpt = findById(reservationId);
        var tableOpt = diningTableService.findById(tableId);

        if (reservationOpt.isEmpty() || tableOpt.isEmpty()) {
            return false;
        }

        var reservation = reservationOpt.get();
        var table = tableOpt.get();

        // Prevent status change if already approved
        if (reservation.getStatus() == ReservationStatus.APPROVED) {
            return false;
        }

        // Check if table is archived
        if (table.isArchived()) {
            return false;
        }

        // Check if table is already assigned to another reservation for the same event
        if (reservationRequestRepository.existsByDiningTableAndSeating_Event(table, reservation.getSeating().getEvent())) {
            return false;
        }

        // Set up the bidirectional relationship
        reservation.setDiningTable(table);
        table.setReservationRequest(reservation);

        // Set the status to approved
        reservation.setStatus(ReservationStatus.APPROVED);

        // Save both entities to maintain the relationship
        save(reservation);
        diningTableService.save(table);

        return true;
    }
}
