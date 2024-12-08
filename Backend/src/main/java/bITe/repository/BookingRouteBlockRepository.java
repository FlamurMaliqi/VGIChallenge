package bITe.repository;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

import bITe.entity.Booking;
import bITe.entity.BookingRouteBlock;
import bITe.entity.RouteBlock;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class BookingRouteBlockRepository implements PanacheRepository<BookingRouteBlock> {

    public Optional<Booking> findByRouteId(Long routeId) {
        Optional<BookingRouteBlock> routeBlock = find("routeBlock.routeBlockId", routeId).firstResultOptional();
        return routeBlock.map(BookingRouteBlock::getBooking);
    }

    public List<RouteBlock> findByBookingId(Long bookingId) {
        return find("booking.bookingId", bookingId).list().stream()
                                            .map(BookingRouteBlock::getRouteBlock)
                                            .toList();
    } 
}
