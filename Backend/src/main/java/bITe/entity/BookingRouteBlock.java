package bITe.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "booking_route_block")
public class BookingRouteBlock implements Serializable {

    @EmbeddedId
    private BookingRouteBlockId id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @MapsId("bookingId")
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @MapsId("routeBlockId")
    @JoinColumn(name = "route_block_id")
    private RouteBlock routeBlock;

    public BookingRouteBlock() {
    }

    public BookingRouteBlock(Booking booking, RouteBlock routeBlock) {
        this.booking = booking;
        this.routeBlock = routeBlock;
        this.id = new BookingRouteBlockId(booking.getBookingId(), routeBlock.getRouteBlockId());
    }

    public BookingRouteBlockId getId() {
        return id;
    }

    public void setId(BookingRouteBlockId id) {
        this.id = id;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public RouteBlock getRouteBlock() {
        return routeBlock;
    }

    public void setRouteBlock(RouteBlock routeBlock) {
        this.routeBlock = routeBlock;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
