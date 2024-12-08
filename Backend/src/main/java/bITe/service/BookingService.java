package bITe.service;

import bITe.common.CreateBookingRequest;
import bITe.common.DeleteBookingRequest;
import bITe.entity.Booking;
import bITe.entity.Contact;
import bITe.entity.RouteBlock;
import bITe.repository.BookingRepository;
import bITe.repository.ContactRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class BookingService {

    @Inject
    Logger LOGGER;
    private static final ZoneId BERLIN_TIME_ZONE = ZoneId.of("Europe/Berlin");
    private static final SecureRandom secureRandom = new SecureRandom();

    @ConfigProperty(name = "pax.threshold")
    int PAX_THRESHOLD;
    @Inject
    BookingRepository bookingRepository;
    @Inject
    ContactRepository contactRepository;
    @Inject
    RouteBlockService routeBlockService;
    @Inject
    EmailService emailService;

    public BookingService() {}

    private String generateRandomHash() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);

        byte[] hashedBytes = DigestUtils.sha256(randomBytes);
        return Base64.encodeBase64URLSafeString(hashedBytes);
    }

    public Optional<Booking> createBooking(CreateBookingRequest createBookingRequest) {
        LOGGER.info("Creating new booking...");
        List<RouteBlock> routeBlocks = createBookingRequest.connection().mapToRouteBlocks();

        // Check if the routeBlocks are already blocked only when the pax exceeds threshold
        if (createBookingRequest.pax() >= PAX_THRESHOLD) {
            LOGGER.info("PAX exceeds threshold. Checking if route is blocked...");
            for (RouteBlock routeBlock : routeBlocks) {
                if (routeBlockService.isBlocked(routeBlock)) {
                    LOGGER.info("Route is blocked. Aborting the booking creation.");
                    return Optional.empty();
                }
            }
        }

        // Create an unique hash
        LOGGER.info("Creating a unique booking hash...");
        String bookingHash;
        do {
            bookingHash = generateRandomHash();
        } while (bookingRepository.existsByBookingHash(bookingHash));

        LOGGER.info("Checking if user with email already exists...");
        Contact contact = createBookingRequest.contact();
        Optional<Contact> optionalContact = contactRepository.findContactByEmail(createBookingRequest.contact().getEmail());
        if (optionalContact.isPresent()) {
            contact = optionalContact.get();
            // Update the contact
            contact.setFirstName(createBookingRequest.contact().getFirstName());
            contact.setLastName(createBookingRequest.contact().getLastName());
            contact.setPhoneNumber(createBookingRequest.contact().getPhoneNumber());
            contact.setInstitution(createBookingRequest.contact().getInstitution());
        }

        LOGGER.info("Creating the booking...");
        Booking booking = new Booking(contact, createBookingRequest.pax(), bookingHash, routeBlocks);
        // Persist contact
        LOGGER.info("Persisting...");
        bookingRepository.persist(booking);
        // Check if the booking was persisted successfully
        if(bookingRepository.isPersistent(booking)) {
            LOGGER.info("Booking has been persisted successfully.");
            emailService.sendBookingEmail(createBookingRequest.contact(), booking, createBookingRequest.connection(), true);
            return Optional.of(booking);
        } else {
            LOGGER.info("Error persisting the booking to the database.");
            return Optional.empty();
        }
    }

    public List<Booking> getBookingsAfter(Instant timestamp) {
        LOGGER.info("Getting bookings after: " + timestamp.toString());
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(timestamp, BERLIN_TIME_ZONE);
        return bookingRepository.getBookingsAfter(zonedDateTime);
    }

    public Optional<Booking> getByBookingId(Long bookingId) {
        LOGGER.info("Getting booking by id: " + bookingId);
        return bookingRepository.findBookingById(bookingId);
    }

    public Optional<Booking> getByBookingHash(String bookingHash) {
        LOGGER.info("Getting booking by hash: " + bookingHash);
        return bookingRepository.findBookingByHash(bookingHash);
    }

    public Optional<Booking> deleteBooking(DeleteBookingRequest deleteBookingRequest) {
        LOGGER.info("Deleting booking by hash:" + deleteBookingRequest.bookingHash());
        Optional<Booking> bookingOptional = bookingRepository.findBookingByHash(deleteBookingRequest.bookingHash());

        if (bookingOptional.isPresent()) {
            LOGGER.info("Booking has been found.");
            Booking booking = bookingOptional.get();

            // Check if the email matches
            if (booking.getContact().getEmail().equalsIgnoreCase(deleteBookingRequest.email())) {
                bookingRepository.delete(booking);

                // Check if the booking was deleted successfully
                if(!bookingRepository.isPersistent(booking)) {
                    LOGGER.info("Booking has been deleted successfully.");
                    emailService.sendBookingEmail(booking.getContact(), booking, booking.mapToConnection(), false);
                    return Optional.of(booking);
                } else {
                    LOGGER.info("Error deleting the booking from the database.");
                    return Optional.empty();
                }
            }
        }
        LOGGER.info("Booking has not been found. Aborting deletion.");
        return Optional.empty();
    }
}
