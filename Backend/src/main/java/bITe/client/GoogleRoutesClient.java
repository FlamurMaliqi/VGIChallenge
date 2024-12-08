package bITe.client;

import com.google.auth.ApiKeyCredentials;
import com.google.auth.Credentials;
import com.google.maps.routing.v2.*;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.protobuf.Timestamp;
import com.google.type.LatLng;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public class GoogleRoutesClient {

    @Inject
    Logger LOGGER;
    private static final ZoneId BERLIN_TIME_ZONE = ZoneId.of("Europe/Berlin");
    private static final boolean COMPUTE_ALTERNATIVE_ROUTES = false;

    private static RoutesClient routesClient;

    public GoogleRoutesClient(@ConfigProperty(name = "google.api.key")  String googleApiKey) {
        try {
            Credentials credentials = ApiKeyCredentials.create(googleApiKey);
            RoutesSettings routesSettings = RoutesSettings.newBuilder()
                    .setCredentialsProvider(() -> credentials)
                    .setHeaderProvider(() -> {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("X-Goog-FieldMask", "routes.legs.steps.transitDetails.*");
                        return headers;
                    }).build();
            routesClient = RoutesClient.create(routesSettings);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ComputeRoutesResponse computeBusRoute(ZonedDateTime dateTime, boolean isDepartureDateTime, Waypoint origin, Waypoint destination) {
        LOGGER.info("Computing new bus route...");
        Timestamp timestamp = convertToTimestamp(dateTime);

        ComputeRoutesRequest.Builder requestBuilder = ComputeRoutesRequest.newBuilder()
                .setLanguageCode("de-DE")
                .setOrigin(origin)
                .setDestination(destination)
                .setTravelMode(RouteTravelMode.TRANSIT)
                .setComputeAlternativeRoutes(COMPUTE_ALTERNATIVE_ROUTES)
                .setTransitPreferences(TransitPreferences.newBuilder()
                        .setRoutingPreference(TransitPreferences.TransitRoutingPreference.LESS_WALKING)
                        .addAllowedTravelModes(TransitPreferences.TransitTravelMode.BUS)
                        .build());

        if (isDepartureDateTime) {
            requestBuilder.setDepartureTime(timestamp);
        } else {
            requestBuilder.setArrivalTime(timestamp);
        }

        ComputeRoutesRequest computeRoutesRequest = requestBuilder.build();

        return routesClient.computeRoutes(computeRoutesRequest);
    }

    private Timestamp convertToTimestamp(ZonedDateTime dateTime) {
        // Convert to Instant
        LOGGER.info("Converting ZonedDateTime to timestamp...");
        Instant instant = dateTime.withZoneSameInstant(BERLIN_TIME_ZONE).toInstant();
        // Create Protobuf Timestamp
        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }

    public Waypoint buildWaypoint(Optional<String> placeId, double latitude, double longitude) {
        // Build the waypoint based on the place id or fall back to the latitude and longitude values
        LOGGER.info("Building new waypoint...");
        return placeId
                .map(id -> Waypoint.newBuilder().setPlaceId(id).build())
                .orElse(Waypoint.newBuilder()
                        .setLocation(Location.newBuilder()
                                .setLatLng(LatLng.newBuilder().setLatitude(latitude).setLongitude(longitude))
                                .build())
                        .build());
    }
}
