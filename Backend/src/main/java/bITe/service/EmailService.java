package bITe.service;

import bITe.common.Connection;
import bITe.common.ConnectionSegment;
import bITe.entity.Contact;
import bITe.entity.Booking;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import jakarta.ws.rs.core.UriBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.jetbrains.annotations.Blocking;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
public class EmailService {

    public enum EmailType {
        ADMIN,
        CONTROL_CENTER,
        USER
    }

    @Inject
    Logger LOGGER;
    public static final String CONFIRMATION_ADMIN_EMAIL = "confirmation-admin.html";
    public static final String CONFIRMATION_CONTROL_CENTER_EMAIL = "confirmation-control-center.html";
    public static final String CONFIRMATION_USER_EMAIL = "confirmation-user.html";
    public static final String CANCELLATION_ADMIN_EMAIL = "cancellation-admin.html";
    public static final String CANCELLATION_CONTROL_CENTER_EMAIL = "cancellation-control-center.html";
    public static final String CANCELLATION_USER_EMAIL = "cancellation-user.html";
    private static final String TEMPLATE_PATH = "/email-templates";

    @Inject
    Mailer mailer;
    private static String frontendUrl;
    private static String adminEmail;
    private static String controlCenterEmail;
    private static DateTimeFormatter formatter;

    public EmailService(@ConfigProperty(name = "frontend.url") String frontendUrl,
                        @ConfigProperty(name = "admin.email") String adminEmail,
                        @ConfigProperty(name = "control.center.email") String controlCenterEmail) {
        EmailService.frontendUrl = frontendUrl;
        EmailService.adminEmail = adminEmail;
        EmailService.controlCenterEmail = controlCenterEmail;
        formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    }

    @Blocking
    public void sendBookingEmail(Contact contact, Booking booking, Connection connection, boolean isConfirmationEmail) {
        LOGGER.info("Sending emails...");
        String subject = (isConfirmationEmail ? "Bestätigt" : "Storniert") + " - VGI Bus Gruppenbuchung für den " + connection.from().departure().format(formatter);
        String connectionHtml = formatConnectionAsHtml(connection);

        URI cancellationURI = null;
        if (isConfirmationEmail) {
            cancellationURI = UriBuilder.fromUri(URI.create(frontendUrl))
                    .path("/booking")
                    .queryParam("hash", booking.getBookingHash())
                    .build();
        }

        for (EmailType type : EmailType.values()) {
            LOGGER.info("Building email for: " + type);
            Optional<String> optionalBody = buildEmailBody(isConfirmationEmail, contact, booking, connectionHtml, cancellationURI, type);

            if (optionalBody.isEmpty()) {
                LOGGER.info("Error building email body. Continuing...");
                continue;
            }

            String recipient = switch (type) {
                case ADMIN -> adminEmail;
                case CONTROL_CENTER -> controlCenterEmail;
                default -> contact.getEmail();
            };

            Mail mail = Mail.withHtml(recipient, subject, optionalBody.get());

            try {
                mailer.send(mail);
                LOGGER.info((isConfirmationEmail ? "Booking confirmation" : "Booking cancellation") + " email sent to " + contact.getEmail());
            } catch (Exception e) {
                LOGGER.error("Failed to send " + (isConfirmationEmail ? "booking confirmation" : "booking cancellation") + " email to " + contact.getEmail(), e);
            }
        }
    }

    private Optional<String> buildEmailBody(boolean isConfirmationEmail, Contact contact, Booking booking, String connectionHtml, URI cancellationURI, EmailType type) {
        //String templateFile = getTemplateFileName(isConfirmationEmail, type);
        //Optional<String> optionalEmailBody = loadEmailTemplate(templateFile);
        String htmlTemplate = getHtmlTemplate(isConfirmationEmail, type);

        /*
        if (optionalEmailBody.isPresent()) {
            // Replace placeholders with values from the contact and booking objects
            String emailBody = optionalEmailBody.get()
                    .replace("{{pax}}", booking != null ? String.valueOf(booking.getPax()) : "")
                    .replace("{{firstName}}", contact != null && contact.getFirstName() != null ? contact.getFirstName() : "")
                    .replace("{{lastName}}", contact != null && contact.getLastName() != null ? contact.getLastName() : "")
                    .replace("{{email}}", contact != null && contact.getEmail()!= null ? contact.getEmail() : "")
                    .replace("{{phoneNumber}}", contact != null && contact.getPhoneNumber() != null ? contact.getPhoneNumber() : "")
                    .replace("{{institution}}", contact != null && contact.getInstitution()!= null ? contact.getInstitution() : "")
                    .replace("{{bookingId}}", booking != null ? booking.getBookingId().toString() : "")
                    .replace("{{connection}}", connectionHtml != null ? connectionHtml : "");

            // Add cancellation URI only if this is a confirmation email and URI is not null
            if (isConfirmationEmail && cancellationURI != null) {
                emailBody = emailBody.replace("{{cancellationURI}}", cancellationURI.toString());
            }

            return Optional.of(emailBody);
        } else {
            return Optional.empty();
        }
        */
        // Replace placeholders with values from the contact and booking objects
        String emailBody = htmlTemplate
                .replace("{{pax}}", booking != null ? String.valueOf(booking.getPax()) : "")
                .replace("{{firstName}}", contact != null && contact.getFirstName() != null ? contact.getFirstName() : "")
                .replace("{{lastName}}", contact != null && contact.getLastName() != null ? contact.getLastName() : "")
                .replace("{{email}}", contact != null && contact.getEmail()!= null ? contact.getEmail() : "")
                .replace("{{phoneNumber}}", contact != null && contact.getPhoneNumber() != null ? contact.getPhoneNumber() : "")
                .replace("{{institution}}", contact != null && contact.getInstitution()!= null ? contact.getInstitution() : "")
                .replace("{{bookingId}}", booking != null ? booking.getBookingId().toString() : "")
                .replace("{{connection}}", connectionHtml != null ? connectionHtml : "");

        // Add cancellation URI only if this is a confirmation email and URI is not null
        if (isConfirmationEmail && cancellationURI != null) {
            emailBody = emailBody.replace("{{cancellationURI}}", cancellationURI.toString());
        }

        return Optional.of(emailBody);
    }

    private String getTemplateFileName(boolean isConfirmationEmail, EmailType type) {
        if(isConfirmationEmail) {
            return switch (type) {
                case ADMIN -> CONFIRMATION_ADMIN_EMAIL;
                case CONTROL_CENTER -> CONFIRMATION_CONTROL_CENTER_EMAIL;
                default -> CONFIRMATION_USER_EMAIL;
            };
        } else {
            return switch (type) {
                case ADMIN -> CANCELLATION_ADMIN_EMAIL;
                case CONTROL_CENTER -> CANCELLATION_CONTROL_CENTER_EMAIL;
                default -> CANCELLATION_USER_EMAIL;
            };
        }
    }

    @Blocking
    private Optional<String> loadEmailTemplate(String templateFileName) {
        try {
            String s = Files.readString(Paths.get(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(TEMPLATE_PATH + "/" + templateFileName)).toURI()));
            return Optional.of(s);
        } catch (IOException e) {
            LOGGER.error("Failed to load email template: " + TEMPLATE_PATH + "/"+ templateFileName, e);
            return Optional.empty();
        } catch (URISyntaxException e) {
            LOGGER.error("Failed to convert path to URI " + TEMPLATE_PATH + "/"+ templateFileName, e);
            return Optional.empty();
        }
    }

    private String formatConnectionAsHtml(Connection connection) {
        StringBuilder html = new StringBuilder();

        // From and To Stops
        html.append("<p><strong>Von:</strong> ").append(connection.from().stopName()).append("</p>");
        html.append("<p><strong>Nach:</strong> ").append(connection.to().stopName()).append("</p>");

        // Duration
        html.append("<p><strong>Dauer:</strong> ").append(connection.durationInMinutes()).append(" Minuten ").append("</p>");

        // Segments
        html.append("<h4>Segmente</h4>");
        html.append("<ul>");

        for (ConnectionSegment segment : connection.connectionSegments()) {
            html.append("<li>");
            html.append("<p><strong>Linie:</strong> ").append(segment.routeShortName()).append(" Richtung ").append(segment.tripHeadsign()).append("</p>");

            // Segment Stops
            html.append("<p><strong>Von Haltestelle:</strong> ").append(segment.fromStop().stopName())
                    .append(" um ").append(segment.fromStop().departure().format(formatter)).append(" Uhr").append("</p>");
            html.append("<p><strong>Nach Haltestelle:</strong> ").append(segment.toStop().stopName())
                    .append(" um ").append(segment.toStop().arrival().format(formatter)).append(" Uhr ").append("</p>");
            html.append("</li>");
        }

        html.append("</ul>");
        html.append("</body></html>");

        return html.toString();
    }

    private String getHtmlTemplate(boolean isConfirmationEmail, EmailType type) {
        if(isConfirmationEmail) {
            return switch (type) {
                case ADMIN -> "<!DOCTYPE html>\n" +
                        "<html lang=\"de\">\n" +
                        "<head>\n" +
                        "  <meta charset=\"UTF-8\">\n" +
                        "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "  <title>Bestätigt - VGI Bus Gruppenbuchen</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<h1>Neue VGI Bus Gruppenbuchung</h1>\n" +
                        "<br>\n" +
                        "<p>Es wurde eine neue Bus Gruppenbuchung erstellt.</p>\n" +
                        "<hr>\n" +
                        "<h2>Buchungsdetails:</h2>\n" +
                        "<table>\n" +
                        "  <tr><td><strong>Pax:</strong></td><td>{{pax}}</td></tr>\n" +
                        "  <tr><td><strong>Name:</strong></td><td>{{firstName}} {{lastName}}</td></tr>\n" +
                        "  <tr><td><strong>Email:</strong></td><td>{{email}}</td></tr>\n" +
                        "  <tr><td><strong>Telefonnummer:</strong></td><td>{{phoneNumber}}</td></tr>\n" +
                        "  <tr><td><strong>Institution:</strong></td><td>{{institution}}</td></tr>\n" +
                        "  <tr><td><strong>Booking ID:</strong></td><td>{{bookingId}}</td></tr>\n" +
                        "</table>\n" +
                        "<hr>\n" +
                        "<h2>Verbindung:</h2>\n" +
                        "{{connection}}\n" +
                        "<hr>\n" +
                        "<br>\n" +
                        "<p>Wenn Sie diese Buchung stornieren müssen, können Sie dies tun, indem Sie auf den folgenden Link klicken:</p>\n" +
                        "<p><a href=\"{{cancellationURI}}\">Buchung stornieren</a></p>\n" +
                        "<br>\n" +
                        "<br>\n" +
                        "<p>Diese E-Mail wurde automatisch erstellt und kann nicht beantwortet werden.</p>\n" +
                        "</body>\n" +
                        "</html>\n";
                case CONTROL_CENTER -> "<!DOCTYPE html>\n" +
                        "<html lang=\"de\">\n" +
                        "<head>\n" +
                        "  <meta charset=\"UTF-8\">\n" +
                        "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "  <title>Bestätigt - VGI Bus Gruppenbuchen</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<h1>Neue VGI Bus Gruppenbuchung</h1>\n" +
                        "<br>\n" +
                        "<p>Sehr geehrte Leitstelle,</p>\n" +
                        "<p>Es wurde eine neue Bus Gruppenbuchung erstellt.</p>\n" +
                        "<strong>Bitte stellen Sie den Bussen, der folgenden Verbindung, die passenden Benachrichtigungen ein.</strong>\n" +
                        "<hr>\n" +
                        "<h2>Buchungsdetails:</h2>\n" +
                        "<table>\n" +
                        "  <tr><td><strong>Pax:</strong></td><td>{{pax}}</td></tr>\n" +
                        "  <tr><td><strong>Name:</strong></td><td>{{firstName}} {{lastName}}</td></tr>\n" +
                        "  <tr><td><strong>Email:</strong></td><td>{{email}}</td></tr>\n" +
                        "  <tr><td><strong>Telefonnummer:</strong></td><td>{{phoneNumber}}</td></tr>\n" +
                        "  <tr><td><strong>Institution:</strong></td><td>{{institution}}</td></tr>\n" +
                        "  <tr><td><strong>Booking ID:</strong></td><td>{{bookingId}}</td></tr>\n" +
                        "</table>\n" +
                        "<hr>\n" +
                        "<h2>Verbindung:</h2>\n" +
                        "{{connection}}\n" +
                        "<hr>\n" +
                        "<br>\n" +
                        "<p>Wenn Sie diese Buchung stornieren müssen, können Sie dies tun, indem Sie auf den folgenden Link klicken: <a href=\"{{cancellationURI}}\">Buchung stornieren</a></p>\n" +
                        "<p>Für Fragen wenden Sie sich bitte an Ihren Kontakt bei den VGI. Wir bedanken uns für Ihre Mitarbeit!</p>\n" +
                        "<br>\n" +
                        "<p>Mit freundlichen Grüßen,</p>\n" +
                        "<p>Ihr VGI</p>\n" +
                        "<br>\n" +
                        "<br>\n" +
                        "<br>\n" +
                        "<p>Diese E-Mail wurde automatisch erstellt und kann nicht beantwortet werden.</p>\n" +
                        "</body>\n" +
                        "</html>\n";
                default -> "<!DOCTYPE html>\n" +
                        "<html lang=\"de\">\n" +
                        "<head>\n" +
                        "  <meta charset=\"UTF-8\">\n" +
                        "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "  <title>Bestätigt - VGI Bus Gruppenbuchen</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<h1>Ihre Buchung wurde erfolgreich bestätigt</h1>\n" +
                        "<br>\n" +
                        "<p>Sehr geehrte/r {{firstName}} {{lastName}},</p>\n" +
                        "<p>Wir freuen uns, Ihnen mitteilen zu können, dass Ihre Buchung erfolgreich abgeschlossen wurde.</p>\n" +
                        "<hr>\n" +
                        "<h2>Buchungsdetails:</h2>\n" +
                        "<p><strong>Personen:</strong> {{pax}}</p>\n" +
                        "<p><strong>Booking ID:</strong> {{bookingId}}</p>\n" +
                        "<hr>\n" +
                        "<h2>Verbindung:</h2>\n" +
                        "{{connection}}\n" +
                        "<hr>\n" +
                        "<br>\n" +
                        "<p>Wenn Sie Ihre Buchung stornieren möchten, können Sie dies jederzeit tun, indem Sie auf den folgenden Link klicken:</p>\n" +
                        "<p><a href=\"{{cancellationURI}}\">Buchung stornieren</a></p>\n" +
                        "<br>\n" +
                        "<p>Vielen Dank, dass Sie nachhaltig mit dem VGI reisen!</p>\n" +
                        "<p>Mit freundlichen Grüßen,</p>\n" +
                        "<p>Ihr VGI</p>\n" +
                        "<br>\n" +
                        "<br>\n" +
                        "<br>\n" +
                        "<p>Diese E-Mail wurde automatisch erstellt und kann nicht beantwortet werden. Bitte wenden Sie sich bei Fragen an den Kundensupport.</p>\n" +
                        "</body>\n" +
                        "</html>\n";
            };
        } else {
            return switch (type) {
                case ADMIN -> "<!DOCTYPE html>\n" +
                        "<html lang=\"de\">\n" +
                        "<head>\n" +
                        "  <meta charset=\"UTF-8\">\n" +
                        "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "  <title>Storniert - VGI Bus Gruppenbuchen</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<h1>Eine VGI Bus Gruppenbuchung wurde storniert</h1>\n" +
                        "<br>\n" +
                        "<p>Es wurde eine Bus Gruppenbuchung storniert.</p>\n" +
                        "<hr>\n" +
                        "<h2>Stornierungsdetails:</h2>\n" +
                        "<table>\n" +
                        "  <tr><td><strong>Pax:</strong></td><td>{{pax}}</td></tr>\n" +
                        "  <tr><td><strong>Name:</strong></td><td>{{firstName}} {{lastName}}</td></tr>\n" +
                        "  <tr><td><strong>Email:</strong></td><td>{{email}}</td></tr>\n" +
                        "  <tr><td><strong>Telefonnummer:</strong></td><td>{{phoneNumber}}</td></tr>\n" +
                        "  <tr><td><strong>Institution:</strong></td><td>{{institution}}</td></tr>\n" +
                        "  <tr><td><strong>Booking ID:</strong></td><td>{{bookingId}}</td></tr>\n" +
                        "</table>\n" +
                        "<hr>\n" +
                        "<h2>Stornierte Verbindung:</h2>\n" +
                        "<br>\n" +
                        "{{connection}}\n" +
                        "<hr>\n" +
                        "<br>\n" +
                        "<br>\n" +
                        "<p>Diese E-Mail wurde automatisch erstellt und kann nicht beantwortet werden.</p>\n" +
                        "</body>\n" +
                        "</html>\n";
                case CONTROL_CENTER -> "<!DOCTYPE html>\n" +
                        "<html lang=\"de\">\n" +
                        "<head>\n" +
                        "  <meta charset=\"UTF-8\">\n" +
                        "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "  <title>Storniert - VGI Bus Gruppenbuchen</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<h1>Eine VGI Bus Gruppenbuchung wurde storniert</h1>\n" +
                        "<br>\n" +
                        "<p>Sehr geehrte Leitstelle,</p>\n" +
                        "<p>Es wurde eine Bus Gruppenbuchung storniert.</p>\n" +
                        "<strong>Bitte stellen Sie den Bussen, der folgenden Verbindung, die passenden Benachrichtigungen ein.</strong>\n" +
                        "<hr>\n" +
                        "<h2>Stornierungsdetails:</h2>\n" +
                        "<table>\n" +
                        "  <tr><td><strong>Pax:</strong></td><td>{{pax}}</td></tr>\n" +
                        "  <tr><td><strong>Name:</strong></td><td>{{firstName}} {{lastName}}</td></tr>\n" +
                        "  <tr><td><strong>Email:</strong></td><td>{{email}}</td></tr>\n" +
                        "  <tr><td><strong>Telefonnummer:</strong></td><td>{{phoneNumber}}</td></tr>\n" +
                        "  <tr><td><strong>Institution:</strong></td><td>{{institution}}</td></tr>\n" +
                        "  <tr><td><strong>Booking ID:</strong></td><td>{{bookingId}}</td></tr>\n" +
                        "</table>\n" +
                        "<hr>\n" +
                        "<h2>Stornierte Verbindung:</h2>\n" +
                        "<br>\n" +
                        "{{connection}}\n" +
                        "<hr>\n" +
                        "xs<p>Für Fragen wenden Sie sich bitte an Ihren Kontakt bei den VGI. Wir bedanken uns für Ihre Mitarbeit!</p>\n" +
                        "<br>\n" +
                        "<p>Mit freundlichen Grüßen,</p>\n" +
                        "<p>Ihr VGI</p>\n" +
                        "<br>\n" +
                        "<br>\n" +
                        "<br>\n" +
                        "<p>Diese E-Mail wurde automatisch erstellt und kann nicht beantwortet werden.</p>\n" +
                        "</body>\n" +
                        "</html>\n";
                default -> "<!DOCTYPE html>\n" +
                        "<html lang=\"de\">\n" +
                        "<head>\n" +
                        "  <meta charset=\"UTF-8\">\n" +
                        "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "  <title>Storniert - VGI Bus Gruppenbuchen</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<h1>Ihre Buchung wurde storniert</h1>\n" +
                        "<br>\n" +
                        "<p>Sehr geehrte/r {{firstName}} {{lastName}},</p>\n" +
                        "<p>Falls Sie die Stornierung nicht selbst vorgenommen haben, wurde Ihre Buchung möglicherweise durch einen Administrator oder die Leitstelle storniert.</p>\n" +
                        "<p>Bei Fragen antworten Sie bitte nicht auf diese E-Mail, sondern wenden Sie sich direkt an den Kundenservice des VGI.</p>\n" +
                        "<hr>\n" +
                        "<h2>Stornierungsdetails:</h2>\n" +
                        "<p><strong>Personen:</strong> {{pax}}</p>\n" +
                        "<p><strong>Booking ID:</strong> {{bookingId}}</p>\n" +
                        "<hr>\n" +
                        "<h2>Stornierte Verbindung:</h2>\n" +
                        "<br>\n" +
                        "{{connection}}\n" +
                        "<hr>\n" +
                        "<br>\n" +
                        "<p>Vielen Dank, dass Sie nachhaltig mit dem VGI reisen!</p>\n" +
                        "<p>Mit freundlichen Grüßen,</p>\n" +
                        "<p>Ihr VGI</p>\n" +
                        "<br>\n" +
                        "<br>\n" +
                        "<br>\n" +
                        "<p>Diese E-Mail wurde automatisch erstellt und kann nicht beantwortet werden. Bitte wenden Sie sich bei Fragen an den Kundensupport.</p>\n" +
                        "</body>\n" +
                        "</html>\n";
            };
        }
    }
}
