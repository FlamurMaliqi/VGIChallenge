package bITe.service;

import bITe.common.Connection;
import bITe.common.ConnectionSegment;
import bITe.common.ConnectionStop;
import bITe.common.Coordinates;
import bITe.entity.Booking;
import bITe.entity.Contact;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@QuarkusTest
public class EmailServiceTest {

    private static final Logger LOGGER = Logger.getLogger(EmailServiceTest.class);

    @Inject
    EmailService emailService;

    @Test
    public void testSendBookingEmail() {
        // Mock contact and booking data
        Contact contact = new Contact();
        contact.setEmail("schultzmoritz@gmail.com");
        contact.setFirstName("Moritz");
        contact.setLastName("Schultz");
        contact.setPhoneNumber("123456789");
        contact.setInstitution("Test Institution");

        Booking booking = new Booking();
        booking.setPax(10);
        booking.setBookingId(1L);
        booking.setBookingHash("unique-hash");

        Connection connection = buildHardCodedConnection();

        // Execute sendBookingEmail method
        assertDoesNotThrow(() -> {
            emailService.sendBookingEmail(contact, booking, connection, true);
        });
    }

    public static Connection buildHardCodedConnection() {
        // Hardcoded 'from' and 'to' stops
        ConnectionStop fromStop = new ConnectionStop.Builder()
                .setStopName("München-Flughafen, Airport Center (MAC)")
                .setDeparture(ZonedDateTime.parse("2024-11-15T12:07:00+01:00"))
                .setCoordinates(new Coordinates(48.353283, 11.785883))
                .build();

        ConnectionStop toStop = new ConnectionStop.Builder()
                .setStopName("Am Westpark/Audi-Ring - Ingolstadt")
                .setArrival(ZonedDateTime.parse("2024-11-15T13:12:00+01:00"))
                .setCoordinates(new Coordinates(48.772359, 11.389049))
                .build();

        // Hardcoded connection segments
        List<ConnectionSegment> segments = Collections.singletonList(
                new ConnectionSegment.Builder()
                        .setRouteShortName("X109")
                        .setTripHeadsign("AE Ingolstadt")
                        .setExpectedOccupation(54)
                        .setIsBlocked(false)
                        .setFromStop(fromStop)
                        .setToStop(toStop)
                        .build()
        );

        // Build the connection
        return new Connection.Builder()
                .setFrom(fromStop)
                .setTo(toStop)
                .setConnectionSegments(segments)
                .setDurationInMinutes(58L)
                .setIsBlocked(false)
                .build();
    }

}
