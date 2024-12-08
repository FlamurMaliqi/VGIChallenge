package bITe.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BookingRouteBlockId implements Serializable {

    private Long bookingId;
    private Long routeBlockId;

    public BookingRouteBlockId() {
    }

    public BookingRouteBlockId(Long bookingId, Long routeBlockId) {
        this.bookingId = bookingId;
        this.routeBlockId = routeBlockId;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getRouteBlockId() {
        return routeBlockId;
    }

    public void setRouteBlockId(Long routeBlockId) {
        this.routeBlockId = routeBlockId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingRouteBlockId that = (BookingRouteBlockId) o;
        return Objects.equals(bookingId, that.bookingId) &&
               Objects.equals(routeBlockId, that.routeBlockId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId, routeBlockId);
    }
}
