package bITe.entity;

import bITe.common.Connection;
import bITe.common.ConnectionSegment;
import bITe.common.ConnectionStop;
import bITe.common.Coordinates;
import jakarta.persistence.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long bookingId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "contact_id", referencedColumnName = "contact_id")
    private Contact contact;

    @Column(name = "pax", nullable = false)
    private int pax;

    @Column(name = "booking_hash", nullable = false, unique = true)
    private String bookingHash;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(
        name = "booking_route_block",
        joinColumns = @JoinColumn(name = "booking_id"),
        inverseJoinColumns = @JoinColumn(name = "route_block_id")
    )
    private List<RouteBlock> routeBlocks;

    public Booking() {}

    public Booking(Contact contact, int pax, String bookingHash, List<RouteBlock> routeBlocks) {
        this.contact = contact;
        this.pax = pax;
        this.bookingHash = bookingHash;
        this.routeBlocks = routeBlocks;
    }

    public Connection mapToConnection() {
        Connection.Builder connectionBuilder = new Connection.Builder();
        ConnectionStop.Builder connectionStopBuilder = new ConnectionStop.Builder();
        ConnectionSegment.Builder connectionSegmentBuilder = new ConnectionSegment.Builder();

        ConnectionStop fromStop = null;
        ConnectionStop toStop = null;
        List<ConnectionSegment> connectionSegments = new ArrayList<>();

        List<RouteBlock> routeBlocks = getRouteBlocks();

        for (RouteBlock routeBlock : routeBlocks) {
            ConnectionStop connectionSegmentFromStop = connectionStopBuilder
                    .setStopName(routeBlock.getDepartureStopName())
                    .setDeparture(routeBlock.getDepartureTime())
                    .setCoordinates(new Coordinates(
                            routeBlock.getDepartureStopLatitude(),
                            routeBlock.getDepartureStopLongitude()))
                    .build();

            if (fromStop == null || (fromStop.departure() != null && fromStop.departure().isAfter(connectionSegmentFromStop.departure()))) {
                fromStop = connectionSegmentFromStop;
            }

            ConnectionStop connectionSegmentToStop = connectionStopBuilder
                    .setStopName(routeBlock.getArrivalStopName())
                    .setArrival(routeBlock.getArrivalTime())
                    .setCoordinates(new Coordinates(
                            routeBlock.getArrivalStopLatitude(),
                            routeBlock.getArrivalStopLongitude()))
                    .build();

            if (toStop == null || (toStop.arrival() != null && toStop.arrival().isBefore(connectionSegmentToStop.arrival()))) {
                toStop = connectionSegmentToStop;
            }

            ConnectionSegment connectionSegment = connectionSegmentBuilder
                    .setRouteShortName(routeBlock.getShortName())
                    .setTripHeadsign(routeBlock.getHeadSign())
                    .setExpectedOccupation(-1)
                    .setIsBlocked(true)
                    .setFromStop(connectionSegmentFromStop)
                    .setToStop(connectionSegmentToStop)
                    .build();

            connectionSegments.add(connectionSegment);
        }

        return connectionBuilder
                .setFrom(fromStop)
                .setTo(toStop)
                .setConnectionSegments(connectionSegments)
                .setDurationInMinutes(Duration.between(fromStop.departure(), toStop.arrival()).toMinutes())
                .setIsBlocked(true)
                .build();
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public List<RouteBlock> getRouteBlocks() {
        // Sort the list by departure time in ascending order
        routeBlocks.sort(Comparator.comparing(RouteBlock::getDepartureTime));
        return routeBlocks;
    }

    public void setRouteBlocks(List<RouteBlock> routeBlocks) {
        this.routeBlocks = routeBlocks;
    }

    public String getBookingHash() {
        return bookingHash;
    }

    public void setBookingHash(String bookingHash) {
        this.bookingHash = bookingHash;
    }

    public int getPax() {
        return pax;
    }

    public void setPax(int pax) {
        this.pax = pax;
    }
}
