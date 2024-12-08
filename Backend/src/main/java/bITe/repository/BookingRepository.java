package bITe.repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import bITe.entity.Booking;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BookingRepository implements PanacheRepository<Booking> {

    public List<Booking> findBookingsByContactId(Long contactId) {
        return list("contact.contactId", contactId);
    }

    public Optional<Booking> findBookingById(Long bookingId) {
        return find("bookingId", bookingId).firstResultOptional();
    }

    public Optional<Booking> findBookingByHash(String bookingHash) {
        return find("bookingHash", bookingHash).firstResultOptional();
    }

    public boolean existsByBookingHash(String bookingHash) {
        long count = count("bookingHash", bookingHash);
        return count > 0;
    }

    public List<Booking> getBookingsAfter(ZonedDateTime timestamp) {
        return streamAll()
            .filter(booking -> booking.getRouteBlocks().stream()
                .anyMatch(routeBlock -> routeBlock.getDepartureTime().isAfter(timestamp) || routeBlock.getDepartureTime().isEqual(timestamp))
            )
            .toList();
    }
     
}
