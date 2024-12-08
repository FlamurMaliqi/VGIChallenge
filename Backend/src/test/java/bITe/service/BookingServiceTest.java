package bITe.service;

import bITe.common.Connection;
import bITe.common.CreateBookingRequest;
import bITe.entity.Booking;
import bITe.entity.Contact;
import bITe.entity.RouteBlock;
import bITe.repository.BookingRepository;
import bITe.repository.ContactRepository;
import bITe.repository.RouteBlockRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class BookingServiceTest {

    @InjectMock
    BookingService bookingService;

    @Inject
    BookingRepository bookingRepository;

    @Inject
    ContactRepository contactRepository;

    @Inject
    RouteBlockRepository routeBlockRepository;

    @Inject
    EmailService emailService;
/*
    @Test
    @Transactional
    void testCreateBooking_successfulBooking() {
        // Prepare the test data
        CreateBookingRequest createBookingRequest = createTestBookingRequest();

        // Call the method
        Optional<Booking> result = bookingService.createBooking(createBookingRequest);

        // Validate the result
        assertTrue(result.isPresent(), "Booking should be successfully created.");

        // Fetch the persisted contact and verify it
        Contact persistedContact = contactRepository.findById(result.get().getContact().getContactId());
        assertNotNull(persistedContact, "Persisted contact should not be null.");
        assertEquals(createBookingRequest.contact().getEmail(), persistedContact.getEmail(), "Emails should match.");

        // Fetch the persisted booking and verify it
        Booking persistedBooking = bookingRepository.findById(result.get().getBookingId());
        assertNotNull(persistedBooking, "Persisted booking should not be null.");
        assertEquals(result.get().getBookingHash(), persistedBooking.getBookingHash(), "Booking hashes should match.");

        // Fetch the persisted route blocks and verify they are saved
        for (RouteBlock routeBlock : result.get().getRouteBlocks()) {
            RouteBlock persistedRouteBlock = routeBlockService.findById(routeBlock.getRouteBlockId());
            assertNotNull(persistedRouteBlock, "RouteBlock should be successfully persisted.");
        }}

    @Test
    @Transactional
    void testCreateBooking_contactNotPersisted() {
        // Prepare the test data with invalid contact
        CreateBookingRequest createBookingRequest = createTestBookingRequestWithInvalidContact();

        // Call the method
        Optional<Booking> result = bookingService.createBooking(createBookingRequest);

        // Validate the result
        assertFalse(result.isPresent(), "Booking should not be created when contact is not persisted.");
    }

    @Test
    @Transactional
    void testCreateBooking_routeBlockNotPersisted() {
        // Prepare the test data with invalid route block
        CreateBookingRequest createBookingRequest = createTestBookingRequestWithInvalidRouteBlock();

        // Call the method
        Optional<Booking> result = bookingService.createBooking(createBookingRequest);

        // Validate the result
        assertFalse(result.isPresent(), "Booking should not be created when route block is not persisted.");
    }

    // Helper method to create a valid booking request
    private CreateBookingRequest createTestBookingRequest() {
        // Example setup, adapt as needed
        Contact contact = new Contact("schultzmoritz@gmail.com", "Moritz", "Schultz", "190238910238", "Test");
        // Set up a mock connection or use a real one
        Connection connection = new Connection();
        return new CreateBookingRequest(connection, contact, 100);
    }

    private CreateBookingRequest createTestBookingRequestWithInvalidContact() {
        // Return a request with an invalid contact setup to test failure cases
        Contact contact = new Contact("", "", "", "", "");
        Connection connection = new Connection();
        return new CreateBookingRequest(connection, contact, 100);
    }

    private CreateBookingRequest createTestBookingRequestWithInvalidRouteBlock() {
        // Return a request with an invalid route block setup to test failure cases
        Contact contact = new Contact("schultzmoritz@gmail.com", "Moritz", "Schultz", "190238910238", "Test");
        Connection connection = new Connection();
        return new CreateBookingRequest(connection, contact, 100);
    }
    */

}
