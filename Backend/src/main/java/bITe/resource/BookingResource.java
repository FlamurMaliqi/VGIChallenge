package bITe.resource;

import bITe.common.CreateBookingRequest;
import bITe.common.DeleteBookingRequest;
import bITe.entity.Booking;
import bITe.service.BookingService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Path("/v1/bookings")
public class BookingResource {

    @Inject
    BookingService bookingService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBooking(@RequestBody CreateBookingRequest createBookingRequest) {
        Optional<Booking> booking = bookingService.createBooking(createBookingRequest);
        if (booking.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error creating booking")
                    .build();
        } else {
            return Response.ok(booking).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookingsAfter(@QueryParam("timestamp") String timestamp) {
        try {
            // Parse the timestamp query parameter as an Instant (timestamp)
            Instant fromTimestamp = Instant.parse(timestamp);

            List<Booking> bookings = bookingService.getBookingsAfter(fromTimestamp);

            if (bookings.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("No bookings found after the given time")
                        .build();
            } else {
                return Response.ok(bookings).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid timestamp format or error processing the request")
                    .build();
        }
    }

    @GET
    @Path("/by-booking-id")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByBookingId(@QueryParam("bookingId") Long bookingId) {
        Optional<Booking> booking = bookingService.getByBookingId(bookingId);
        if (booking.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("No booking found")
                    .build();
        } else {
            return Response.ok(booking).build();
        }
    }

    @GET
    @Path("/by-booking-hash")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByBookingHash(@QueryParam("hash") String bookingHash) {
        Optional<Booking> booking = bookingService.getByBookingHash(bookingHash);
        if (booking.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("No booking found")
                    .build();
        } else {
            return Response.ok(booking).build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBooking(@RequestBody DeleteBookingRequest deleteBookingRequest) {
        Optional<Booking> booking = bookingService.deleteBooking(deleteBookingRequest);
        if (booking.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("No booking found")
                    .build();
        } else {
            return Response.ok(booking).build();
        }
    }
}
