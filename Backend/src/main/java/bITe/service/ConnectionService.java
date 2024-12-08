package bITe.service;

import java.time.*;
import java.util.*;

import bITe.client.GoogleGeocodingClient;
import bITe.client.GoogleRoutesClient;
import bITe.common.*;
import bITe.entity.Stop;
import bITe.repository.StopRepository;
import com.google.maps.routing.v2.*;
import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
@Transactional
public class ConnectionService {

    @Inject
    Logger LOGGER;

    private static final ZoneId BERLIN_TIME_ZONE = ZoneId.of("Europe/Berlin");

    @ConfigProperty(name = "pax.threshold")
    int PAX_THRESHOLD;

    @Inject
    StopRepository stopRepository;

    @Inject
    GoogleRoutesClient googleRoutesClient;

    @Inject
    GoogleGeocodingClient googleGeocodingClient;

    @Inject
    RouteBlockService routeBlockService;

    public Map<String, Coordinates> getStopNamesWithCoordinates() {
        LOGGER.info("Getting all stops...");
        List<Stop> stops = stopRepository.listAll();  // Retrieve all stops from the database
        Map<String, Coordinates> stopNamesWithCoordinates = new HashMap<>();

        for (Stop stop : stops) {
            Coordinates coordinates = new Coordinates(stop.getStopLatitude(), stop.getStopLongitude());
            stopNamesWithCoordinates.put(stop.getStopName(), coordinates);
        }

        return stopNamesWithCoordinates;
    }

    public List<Connection> findConnections(ConnectionRequest request) {
        LOGGER.info("Finding connections...");
        List<Connection> connectionResponse = new ArrayList<>();
        ZonedDateTime dateTime;
        boolean isDepartureDateTime;

        if (request.departure() == null && request.arrival() == null) {
            LOGGER.info("Departure and arrival are null. Aborting to find connections.");
            return Collections.emptyList();
        } else if (request.arrival() == null) {
            dateTime = request.departure();
            isDepartureDateTime = true;
        } else {
            dateTime = request.arrival();
            isDepartureDateTime = false;
        }

        double fromLatitude = request.fromCoordinates().latitude();
        double fromLongitude = request.fromCoordinates().longitude();
        double toLatitude = request.toCoordinates().latitude();
        double toLongitude = request.toCoordinates().longitude();

        Optional<String> fromPlaceId = googleGeocodingClient.getPlaceId(fromLatitude, fromLongitude);
        Optional<String> toPlaceId = googleGeocodingClient.getPlaceId(toLatitude, toLongitude);

        Waypoint from = googleRoutesClient.buildWaypoint(fromPlaceId, fromLatitude, fromLongitude);
        Waypoint to = googleRoutesClient.buildWaypoint(toPlaceId, toLatitude, toLongitude);


        // Loop to get 5 routes, incrementing the dateTime by 15 minutes each time
        LOGGER.info("Getting routes from Google Routes API...");
        for (int i = 0; i < 5; i++) {
            // Get the route for the current / incremented dateTime
            ComputeRoutesResponse computeRoutesResponse = googleRoutesClient.computeBusRoute(
                    dateTime.plusMinutes(i * 15), isDepartureDateTime, from, to
            );

            Connection connection = buildConnectionResponse(computeRoutesResponse, request.pax());

            // Check if the connection is a duplicate
            LOGGER.info("Checking if connection is a duplicate...");
            boolean isDuplicate = false;
            if (connectionResponse.isEmpty()) {
                connectionResponse.add(connection);
            } else {
                for (Connection c : connectionResponse) {
                    if (isDuplicate(connection, c)) {
                        isDuplicate = true;
                        break;
                    }
                }
                if (!isDuplicate) {
                    connectionResponse.add(connection);
                }
            }
        }
        LOGGER.info("Returning all connections...");
        return connectionResponse;
    }

    private boolean isDuplicate(Connection connection1, Connection connection2) {
        // Check if the connection segments are the same length
        if (connection1.connectionSegments().size() != connection2.connectionSegments().size()) {
            return false;
        }

        // Compare departure and arrival times
        if (connection1.from().departure().isEqual(connection2.from().departure()) &&
                connection1.to().arrival().isEqual(connection2.to().arrival()) &&
                connection1.from().stopName().equals(connection2.from().stopName()) &&
                connection1.to().stopName().equals(connection2.to().stopName())) {

            // Compare connection segments
            List<ConnectionSegment> segments1 = connection1.connectionSegments();
            List<ConnectionSegment> segments2 = connection2.connectionSegments();

            for (int i = 0; i < segments1.size(); i++) {
                ConnectionSegment segment1 = segments1.get(i);
                ConnectionSegment segment2 = segments2.get(i);

                // Check for differences in route short name
                if (!segment1.routeShortName().equals(segment2.routeShortName())) {
                    return false;  // Return false immediately if there's a mismatch
                }
            }

            // If no mismatches were found, they are considered duplicates
            return true;
        }

        // If any of the other checks fail, the connections are not duplicates
        return false;
    }

    private Connection buildConnectionResponse(ComputeRoutesResponse computeRoutesResponse, int pax) {
        LOGGER.info("Building connection response...");
        // Instantiate new builders for each call to ensure thread safety and prevent data from previous calls from carrying over
        Connection.Builder connectionBuilder = new Connection.Builder();

        // Collect connectionSegments from the response
        List<ConnectionSegment> connectionSegments = new ArrayList<>();
        long totalDurationInMinutes = 0;
        boolean connectionIsBlocked = false;

        for (Route route : computeRoutesResponse.getRoutesList()) {
            for (RouteLeg leg : route.getLegsList()) {
                for (RouteLegStep step : leg.getStepsList()) {
                    if (step.hasTransitDetails()) {
                        // Extract transit details
                        RouteLegStepTransitDetails transitDetails = step.getTransitDetails();
                        RouteLegStepTransitDetails.TransitStopDetails stopDetails = transitDetails.getStopDetails();

                        // Build ConnectionStop for departure
                        ZonedDateTime departureTime = ZonedDateTime.ofInstant(
                                Instant.ofEpochSecond(stopDetails.getDepartureTime().getSeconds()), BERLIN_TIME_ZONE);
                        Coordinates departureCoordinates = new Coordinates(
                                stopDetails.getDepartureStop().getLocation().getLatLng().getLatitude(),
                                stopDetails.getDepartureStop().getLocation().getLatLng().getLongitude());

                        ConnectionStop fromStop = new ConnectionStop.Builder()
                                .setStopName(stopDetails.getDepartureStop().getName())
                                .setDeparture(departureTime)
                                .setCoordinates(departureCoordinates)
                                .build();

                        // Build ConnectionStop for arrival
                        ZonedDateTime arrivalTime = ZonedDateTime.ofInstant(
                                Instant.ofEpochSecond(stopDetails.getArrivalTime().getSeconds()), BERLIN_TIME_ZONE);
                        Coordinates arrivalCoordinates = new Coordinates(
                                stopDetails.getArrivalStop().getLocation().getLatLng().getLatitude(),
                                stopDetails.getArrivalStop().getLocation().getLatLng().getLongitude());

                        ConnectionStop toStop = new ConnectionStop.Builder()
                                .setStopName(stopDetails.getArrivalStop().getName())
                                .setArrival(arrivalTime)
                                .setCoordinates(arrivalCoordinates)
                                .build();

                        // Calculate durationInMinutes for this segment and add it to total durationInMinutes
                        long segmentDuration = Duration.between(departureTime, arrivalTime).toMinutes();
                        totalDurationInMinutes += segmentDuration;

                        // Build ConnectionSegment
                        String routeShortName = transitDetails.getTransitLine().getNameShort();
                        String tripHeadsign = transitDetails.getHeadsign();
                        int expectedOccupation = getExpectedOccupation(departureTime);
                        boolean segmentIsBlocked = false;

                        // Check if segment is blocked
                        LOGGER.info("Checking if connection segment is blocked...");
                        if (routeBlockService.isBlocked(routeShortName, tripHeadsign, departureTime, arrivalTime)) {
                            if (pax >= PAX_THRESHOLD) {
                                LOGGER.info("Connection segment is blocked.");
                                segmentIsBlocked = true;
                                connectionIsBlocked = true;
                            }
                        }

                        ConnectionSegment segment = new ConnectionSegment.Builder()
                                .setRouteShortName(routeShortName)
                                .setTripHeadsign(tripHeadsign)
                                .setFromStop(fromStop)
                                .setToStop(toStop)
                                .setIsBlocked(segmentIsBlocked)
                                .setExpectedOccupation(expectedOccupation)
                                .build();

                        // Add segment to the list
                        connectionSegments.add(segment);

                        // Set initial "from" and final "to" stops for the connection
                        if (connectionSegments.size() == 1) {
                            connectionBuilder.setFrom(fromStop);
                        }
                        connectionBuilder.setTo(toStop);
                    }
                }
            }
        }

        // Build the connection with all connectionSegments and durationInMinutes
        LOGGER.info("Building and returning single connection...");
        return connectionBuilder
                .setConnectionSegments(connectionSegments)
                .setDurationInMinutes(totalDurationInMinutes)
                .setIsBlocked(connectionIsBlocked)
                .build();
    }

    // TODO
    // Mocked: Full implementation needs GTFS data
    // https://developers.google.com/maps/documentation/routes/reference/rest/v2/TopLevel/computeRoutes?hl=de
    // https://support.google.com/transitpartners/answer/10106342?hl=de
    // https://gtfs.org/documentation/realtime/reference/#
    private int getExpectedOccupation(ZonedDateTime departureTime) {
        // Mocked implementation based on the departure time

        // Define time ranges for different levels of bus occupancy
        LocalTime rushHourMorningStart = LocalTime.of(6, 30);
        LocalTime rushHourMorningEnd = LocalTime.of(9, 0);
        LocalTime rushHourMiddyStart = LocalTime.of(11, 30);
        LocalTime rushHourMiddyEnd = LocalTime.of(13, 30);
        LocalTime rushHourEveningStart = LocalTime.of(16, 0);
        LocalTime rushHourEveningEnd = LocalTime.of(18, 0);

        // Extract the local time for departure
        LocalTime departureLocalTime = departureTime.toLocalTime();

        // High occupancy during morning rush hour
        if (departureLocalTime.isAfter(rushHourMorningStart) && departureLocalTime.isBefore(rushHourMorningEnd)) {
            return getRandomNumber(40, 80);
        }

        // High occupancy during midday rush hour
        if (departureLocalTime.isAfter(rushHourMiddyStart) && departureLocalTime.isBefore(rushHourMiddyEnd)) {
            return getRandomNumber(40, 80);
        }

        // High occupancy during evening rush hour
        if (departureLocalTime.isAfter(rushHourEveningStart) && departureLocalTime.isBefore(rushHourEveningEnd)) {
            return getRandomNumber(40, 80);
        }

        // Moderate occupancy during typical daytime hours
        if (departureLocalTime.isAfter(rushHourMorningEnd) && departureLocalTime.isBefore(rushHourMiddyStart) ||
                departureLocalTime.isAfter(rushHourMiddyEnd) && departureLocalTime.isBefore(rushHourEveningStart)) {
            return getRandomNumber(10, 50);
        }

        // Low occupancy during late evening and early morning
        return getRandomNumber(10, 40);
    }

    public int getRandomNumber(int x, int y) {
        return (int)(Math.random() * (y - x + 1)) + x; // Generates a number between x and y, inclusive
    }
}
