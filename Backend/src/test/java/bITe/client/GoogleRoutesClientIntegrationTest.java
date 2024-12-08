package bITe.client;

import bITe.common.Connection;
import bITe.common.ConnectionRequest;
import bITe.common.Coordinates;
import bITe.service.ConnectionService;
import com.google.maps.routing.v2.ComputeRoutesResponse;
import com.google.maps.routing.v2.Waypoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@QuarkusTest
public class GoogleRoutesClientIntegrationTest {

    private static final Logger LOGGER = Logger.getLogger(GoogleRoutesClientIntegrationTest.class);


    @Inject
    GoogleRoutesClient googleRoutesClient;

    @Inject
    ConnectionService connectionService;

    @Test
    public void testComputeBusRouteWithRealApiCall() {
        // Set up test waypoints
        Waypoint origin = googleRoutesClient.buildWaypoint(Optional.empty(), 48.7602430, 11.4224340);
        Waypoint destination = googleRoutesClient.buildWaypoint(Optional.empty(), 48.7571210, 11.4004590);
        ZonedDateTime departureTime = ZonedDateTime.now()
                .plusDays(1) // Move to tomorrow
                .withHour(10) // Set hour to 10 AM
                .withMinute(0); // Set minutes to 0

        // Call the method and ensure it makes an API request and returns a response
        ComputeRoutesResponse response = googleRoutesClient.computeBusRoute(departureTime, true, origin, destination);

        LOGGER.info(response.toString());

        Assertions.assertNotNull(response, "The response should not be null");
        Assertions.assertFalse(response.getRoutesList().isEmpty(), "The response should contain at least one route");
    }

    @Test
    public void testFindConnections() {
        // Set up connection request
        // Sample coordinates for from and to stops
        Coordinates fromCoordinates = new Coordinates(48.760258, 11.422542);  // Christoph-Scheiner-Gymnasium - Ingolstadt
        Coordinates toCoordinates = new Coordinates(48.7837310, 11.4124720);  // Audi Forum

        // Sample departure and arrival times in Berlin time zone
        ZonedDateTime departureTime = ZonedDateTime.of(2024, 11, 14, 11, 22, 0, 0, ZoneId.of("Europe/Berlin"));

        // Create ConnectionRequest object
        ConnectionRequest connectionRequest = new ConnectionRequest(
                "Christoph-Scheiner-Gymnasium - Ingolstadt",
                fromCoordinates,
                "Universität (Kreuztor)",
                toCoordinates,
                departureTime,
                null,
                10
        );

        LOGGER.info("ConnectionRequest: " +  connectionRequest);

        List<Connection> connections = connectionService.findConnections(connectionRequest);

        LOGGER.info(connections.toString());

        Assertions.assertNotNull(connections, "The response should not be null");
        Assertions.assertFalse(connections.isEmpty(), "The response should contain at least one route");
    }
}
